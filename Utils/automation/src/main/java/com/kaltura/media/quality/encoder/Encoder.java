package com.kaltura.media.quality.encoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.kaltura.client.types.KalturaLiveStreamEntry;
import com.kaltura.media.quality.configurations.EncoderConfig;
import com.kaltura.media.quality.configurations.TestConfig;
import com.kaltura.media.quality.event.Event;
import com.kaltura.media.quality.event.EventsManager;
import com.kaltura.media.quality.event.listener.IProcessCompleteListener;
import com.kaltura.media.quality.utils.ThreadManager;

/**
 * Created by asher.saban on 2/26/2015.
 */
public abstract class Encoder extends EventsManager {

    private static final Logger log = Logger.getLogger(Encoder.class);
    protected static TestConfig config;

    protected KalturaLiveStreamEntry entry;
    protected String uniqueId;
	protected EncoderConfig encoderConfig;
    private Process process;

	class ProcessCompleteEvent extends Event<IProcessCompleteListener>{
		private static final long serialVersionUID = 1929172675272850989L;
		private int exitCode;
		
		public ProcessCompleteEvent(int exitCode) {
			super(IProcessCompleteListener.class);
			
			this.exitCode = exitCode;
		}

		@Override
		protected void callListener(IProcessCompleteListener listener) {
			listener.onProcessComplete(exitCode);
		}

		@Override
		protected String getTitle() {
			return null;
		}
	}
	    
    public Encoder(String uniqueId, EncoderConfig encoderConfig) {
        this.uniqueId = uniqueId;
        this.encoderConfig = encoderConfig;
        
        if(config == null){
        	config = TestConfig.get();
        }
    }

    public Encoder(KalturaLiveStreamEntry entry, EncoderConfig encoderConfig) {
    	this(entry.id, encoderConfig);
        this.entry = entry;
    }

    protected abstract String getCommandLineArguments() throws Exception;

	protected File getLogFile() {
		String logPath = config.getDestinationFolder() + "/" + uniqueId + "/logs";
		File dir = new File(logPath);
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		return new File(dir.getAbsolutePath() + File.separator + getClass().getSimpleName() + ".log");
	}
	
    public void start() throws Exception {
    	String command = "\"" + encoderConfig.getPathToExecutable() + "\" " + getCommandLineArguments();
        log.info("Starting: " + command);

        final FileWriter stdOut = new FileWriter(getLogFile(), true);
        
        process = Runtime.getRuntime().exec(command);

        ThreadManager.start(new Runnable() {
			@Override
			public void run() {
				Thread thread = Thread.currentThread();
				thread.setPriority(Thread.MIN_PRIORITY);
				thread.setName("Encoder-std-out-" + thread.getId());
		        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()) );
		        String line;
		        try {
					while ((line = in.readLine()) != null) {
						stdOut.write(line + "\n");
					}
			        in.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e); 
				}
			}
        });

        ThreadManager.start(new Runnable() {
			@Override
			public void run() {
				Thread thread = Thread.currentThread();
				thread.setPriority(Thread.MIN_PRIORITY);
				thread.setName("Encoder-std-err-" + thread.getId());
		        BufferedReader in = new BufferedReader(new InputStreamReader(process.getErrorStream()) );
		        String line;
		        try {
					while ((line = in.readLine()) != null) {
						stdOut.write(line + "\n");
						log.error("Encoder: " + line.trim());
					}
			        in.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e); 
				}
			}
        });
        
        ThreadManager.start(new Runnable() {
			@Override
			public void run() {
				Thread thread = Thread.currentThread();
				thread.setPriority(Thread.MIN_PRIORITY);
				thread.setName("Encoder-exitCodeListener-" + thread.getId());
				try {
					int exitCode = process.waitFor();
					stdOut.close();
					onProcessComplete(exitCode);
				} catch (InterruptedException | IOException e) {
					log.error(e.getMessage(), e); 
				}
			}
		});
        
//        ThreadManager.start(new Runnable() {
//			@Override
//			public void run() {
//				Thread thread = Thread.currentThread();
//				thread.setPriority(Thread.MIN_PRIORITY);
//				thread.setName("Encoder-keepRunning-" + thread.getId());
//				while(!Thread.interrupted() && ThreadManager.shouldContinue()){
//					try {
//						Thread.sleep(200);
//					} catch (InterruptedException e) {
//						log.error(e.getMessage(), e); 
//					}
//				}
//				stop();
//			}
//		});
    }

    public void stop() {
        log.info("Stopping encoder");
        process.destroy();
    }

	public void addProcessCompleteListener(IProcessCompleteListener listener) {
		addListener(IProcessCompleteListener.class, listener);
	}

	public void removeProcessCompleteListener(IProcessCompleteListener listener) {
		removeListener(IProcessCompleteListener.class, listener);
	}

	private void onProcessComplete(int exitCode) {
		raiseEvent(new ProcessCompleteEvent(exitCode));
	}
}
