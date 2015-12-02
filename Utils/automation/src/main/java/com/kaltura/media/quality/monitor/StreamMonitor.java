package com.kaltura.media.quality.monitor;

import java.lang.reflect.Constructor;

import org.apache.log4j.Logger;

import com.kaltura.media.quality.configurations.EncoderConfig;
import com.kaltura.media.quality.encoder.Encoder;
import com.kaltura.media.quality.event.listener.IListener;
import com.kaltura.media.quality.event.listener.IProcessCompleteListener;
import com.kaltura.media.quality.utils.StringUtils;
import com.kaltura.media.quality.utils.ThreadManager;

public class StreamMonitor extends Monitor {
	private static final Logger log = Logger.getLogger(StreamMonitor.class);

	public static void main(String[] args) throws Exception {
		StreamMonitor monitor = new StreamMonitor(args);
		monitor.execute();
	}

	public StreamMonitor(String[] args) throws Exception {
		super(args);
	}

	@SuppressWarnings("serial")
	@Override
	protected void execute() throws Exception {
		Thread thread = Thread.currentThread();
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.setName("main");

		String uniqueId = StringUtils.generateRandomSuffix();
        EncoderConfig encoderConfig = config.getEncoder();
		Constructor<Encoder> encoderConstructor = encoderConfig.getType().getConstructor(String.class, EncoderConfig.class);
		Encoder encoder = encoderConstructor.newInstance(uniqueId, encoderConfig);

		encoder.addProcessCompleteListener(new IProcessCompleteListener() {
			
			@Override
			public void onProcessComplete(int exitCode) {
				if(exitCode != 0){
					log.error("Encoder failed");
					ThreadManager.stop();
				}
			}

			@Override
			public int compareTo(IListener o) {
				if(o == this){
					return 0;
				}
				return 1;
			}

			@Override
			public boolean isDeffered() {
				return false;
			}

			@Override
			public String getTitle() {
				return null;
			}
		});
		
		handleStream(uniqueId);

		encoder.start();
		
		while(ThreadManager.shouldContinue()) {
			Thread.sleep(1000);
		}
		
		log.info("Stopping all threads");
		ThreadManager.stop();
	}
}
