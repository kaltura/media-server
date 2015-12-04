package com.kaltura.media.quality.comparators.vqmt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.kaltura.media.quality.comparators.SegmentsComparator;
import com.kaltura.media.quality.configurations.SegmentsComparatorConfig;
import com.kaltura.media.quality.configurations.TestConfig;
import com.kaltura.media.quality.model.Segment;
import com.kaltura.media.quality.utils.StringUtils;

public class QualitySegmentsComparator implements SegmentsComparator {
	private static final Logger log = Logger.getLogger(QualitySegmentsComparator.class);
	private Process process;
	private SegmentsComparatorConfig comparatorConfig;

	public QualitySegmentsComparator(SegmentsComparatorConfig comparatorConfig){
		this.comparatorConfig = comparatorConfig;
	}

	class Killer extends Thread {
		@Override
		public void run() {
			process.destroy();
		}
	}

	private String getBaseDir(Segment segment) {
		return TestConfig.get().getDestinationFolder() + "/" + segment.getEntryId();
	}

	private File getDiffDir(Segment segment) {
		File file = new File(getBaseDir(segment) + "/diff");
		if(!file.exists()){
			file.mkdirs();
		}
		
		return file;
	}

	private String[] getCommandLine(List<Segment> segments) throws Exception {
		// "C:\Program Files\MSU VQMT\msu_metric_64.exe" -metr ALL -cc ALL -sc 1 -cod "C:\tmp\test\1_77ff6g8b\logs" -ct 0 -fpd 0 -sv 1 -f "C:\tmp\test\1_77ff6g8b\chunklist_b1017600.m3u8\0\2015_12_02_17_05_54\media-uq1nblfbr_b1017600_540.ts" -f "C:\tmp\test\1_77ff6g8b\chunklist_b987136.m3u8\0\2015_12_02_17_05_54\media-utkecmdne_b987136_540.ts" -f "C:\tmp\test\1_77ff6g8b\chunklist_b987136.m3u8\0\2015_12_02_17_05_54\media-utkecmdne_b987136_540.ts"
		if(!comparatorConfig.hasOtherProperty("path-to-executable")){
			throw new Exception("Path to MSU VQMT executable configuratio is missing");
		}
		
		String metric = "ALL";
		if(comparatorConfig.hasOtherProperty("metric")){
			metric = (String) comparatorConfig.getOtherProperty("metric");
		}
		
		String colorComponent = "ALL";
		if(comparatorConfig.hasOtherProperty("color-component")){
			colorComponent = (String) comparatorConfig.getOtherProperty("color-component");
		}
		
		File logsDir = new File(getBaseDir(segments.get(0)) + "/logs/" + metric + "/" + colorComponent);
		if(!logsDir.exists()){
			logsDir.mkdirs();
		}
				
		List<String> commandLine = new ArrayList<String>();
		commandLine.add("\"" + (String) comparatorConfig.getOtherProperty("path-to-executable") + "\"");
		
		// desired metric; specify "ALL" key to compute all metrics
		commandLine.add("-metr");
		commandLine.add(metric);
		
		// color component to compute desired metric for
		//        * specify "ALL" key to compute metric for all supported color components
		//        * specify "YVU" key to compute metric for all YUV color components
		//        * specify "RGB" key to compute metric for all RGB color components
		commandLine.add("-cc");
		commandLine.add(colorComponent);
		
		// 1 - save CSV file, 0 - no CSV
		commandLine.add("-sc");
		commandLine.add("1");
		
		// dir for saving CSV files
		commandLine.add("-cod");
		commandLine.add("\"" + logsDir.getAbsolutePath() + "\"");
		
		// CSV delimiter(0 - ',', 1 - ';')
		commandLine.add("-ct");
		commandLine.add("0");
		
		// floating-point delimiter(0 - '.', 1 - ',')
		commandLine.add("-fpd");
		commandLine.add("0");
		
		// 1 - save visualization file, 0 - no vis. file
		commandLine.add("-sv");
		commandLine.add("0");

		// assume that highest bitrate is the source 
		Segment source = null;
		for(Segment segment : segments){
			if(source == null || segment.getRendition().getBandwidth() > source.getRendition().getBandwidth()){
				source = segment;
			}
		}

		// files to compare (the first one is assumed as original, colorspace is applicable for raw files only)
		commandLine.add("-f");
		commandLine.add("\"" + source.getFile().getAbsolutePath() + "\"");
		
		for(Segment segment : segments){
			if(segment != source){
				commandLine.add("-f");
				commandLine.add("\"" + segment.getFile().getAbsolutePath() + "\"");
			}
		}
		
		return commandLine.toArray(new String[]{});
	}
	
	public void execute(List<Segment> segments) throws Exception {
		String[] cmd = getCommandLine(segments);
		String cmdString = StringUtils.join(cmd, " ");

		log.info("Executing command: " + cmdString);

		ProcessBuilder processBuilder = new ProcessBuilder(cmd);
		processBuilder.directory(getDiffDir(segments.get(0)));
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
	}
	
	@Override
	public void compare(List<Segment> segments) {
		try {
			execute(segments);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
