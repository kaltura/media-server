package com.kaltura.media.quality.validator.info;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.kaltura.media.quality.utils.StringUtils;
import com.kaltura.media.quality.utils.ThreadManager;

public abstract class MediaInfoBase {
	private static final Logger log = Logger.getLogger(MediaInfoBase.class);

	private Process process;

	protected File file;
	protected String rawData;
	protected Info data;

	public MediaInfoBase(File file) throws Exception {
		this.file = file;
		this.rawData = getRawData();
		this.data = parse();
	}

	protected abstract Info parse() throws Exception;

	protected abstract String getExecutablePath();

	protected abstract String[] getCommandLine();

	public enum StreamType {
		VIDEO("video"), AUDIO("audio"), DATA("data"), GENERAL("general"), MENU("menu");

		private String type;

		StreamType(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}
	}

	public interface IStreamInfo {
		public StreamType getType();

		public String getCodec();
	}

	public static abstract class VideoInfo implements IStreamInfo {
		public abstract int getWidth();

		public abstract int getHeight();

		public abstract String getAspectRatio();

		public abstract double getDuration();
	}

	public static abstract class AudioInfo implements IStreamInfo {
		public abstract int getChannels();

		public abstract int getSampleRate();

		public abstract int getBitrate();

		public abstract double getDuration();
	}

	public static abstract class Info {
		public abstract VideoInfo getVideo();

		public abstract AudioInfo getAudio();

		public abstract int getBitrate();

		public abstract double getDuration();
	}

	class Killer extends Thread {
		@Override
		public void run() {
			process.destroy();
		}
	}

	private class ProcessStreamReader implements RunnableFuture<String> {

		private InputStream input;
		private StringBuilder output;
		private boolean canceled = false;
		private boolean done = false;

		private ProcessStreamReader(InputStream input) {
			this.input = input;
			this.output = new StringBuilder();
		}

		@Override
		public void run() {
			Thread thread = Thread.currentThread();
			thread.setPriority(Thread.MIN_PRIORITY);
			thread.setName(getClass().getSimpleName() + "-" + thread.getId());

			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			try {
				while (!canceled && !Thread.interrupted() && (line = reader.readLine()) != null) {
					output.append(line + "\n");
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
			done = true;

			try {
				input.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}

		@Override
		public boolean cancel(boolean arg0) {
			canceled = true;
			return false;
		}

		@Override
		public String get() throws InterruptedException, ExecutionException {
			try {
				return get(0, TimeUnit.MILLISECONDS);
			} catch (TimeoutException e) {
				// shouldn't happen
			}
			return null;
		}

		@Override
		public String get(long units, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
			long endTime = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(units, timeUnit);
			while (!isDone()) {
				if (units > 0 && endTime < System.currentTimeMillis()) {
					log.info("Process input stream timeout");
					break;
				}
				Thread.sleep(500);
				log.debug(".");
			}
			cancel(true);
			return output.toString();
		}

		@Override
		public boolean isCancelled() {
			return canceled;
		}

		@Override
		public boolean isDone() {
			return done;
		}
	}

	public Info getInfo() {
		return data;
	}

	protected String getRawData() throws Exception {
		return this.execute();
	}

	protected String execute() throws Exception {
		String[] cmd = this.getCommandLine();
		String cmdString = StringUtils.join(cmd, " ");

		log.info("Executing command: " + cmdString);

		Killer terminator;
		try {
			process = Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new Exception("Unable to execute command " + cmdString);
		}

		ProcessStreamReader processOutputReader = new ProcessStreamReader(process.getInputStream());
		ProcessStreamReader processErrorReader = new ProcessStreamReader(process.getErrorStream());
		ThreadManager.start(processOutputReader);
		ThreadManager.start(processErrorReader);

		terminator = new Killer();
		Runtime.getRuntime().addShutdownHook(terminator);
		
		boolean done = process.waitFor(2, TimeUnit.MINUTES);
		Runtime.getRuntime().removeShutdownHook(terminator);
		if (!done) {
			terminator.start();
			throw new Exception("Exec timeout: " + cmdString);
		}

		int exitValue = process.exitValue();
		log.debug("Exit code [" + exitValue + "]");
		if (exitValue != 0) {
			throw new Exception("Exec failed with exit code [" + exitValue + "]: " + cmdString);
		}

		String processOutput = processOutputReader.get(1, TimeUnit.MINUTES);
		String processError = processErrorReader.get(30, TimeUnit.MILLISECONDS);

		log.debug("##### Process Output Messages #####\n" + processOutput);

		if (processError.trim().length() > 0) {
			log.warn("##### Process Error Messages #####\n" + processError);
		}

		return processOutput;
	}
}
