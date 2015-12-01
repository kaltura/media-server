package com.kaltura.media.quality.validator.info;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.ProcessBuilder.Redirect;

import org.apache.log4j.Logger;

import com.kaltura.media.quality.utils.StringUtils;

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

		ProcessBuilder processBuilder = new ProcessBuilder(cmd);
		File stdOut = File.createTempFile("stdout", ".log");
		processBuilder.redirectErrorStream(true);
		processBuilder.redirectOutput(Redirect.appendTo(stdOut));
		process = processBuilder.start();

		int exitValue = process.waitFor();
		log.debug("Exit code [" + exitValue + "]");

		BufferedReader reader = new BufferedReader(new FileReader(stdOut));
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append("\n");
		}
		reader.close();
		String output = stringBuilder.toString();
		log.debug("##### Process Output Messages #####\n" + output);

		if (exitValue != 0) {
			throw new Exception("Exec failed with exit code [" + exitValue + "]: " + cmdString);
		}

		return output.toString();
	}
}
