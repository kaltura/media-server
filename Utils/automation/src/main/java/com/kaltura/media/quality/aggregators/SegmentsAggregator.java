package com.kaltura.media.quality.aggregators;

import com.kaltura.media.quality.configurations.AggregatorConfig;

public class SegmentsAggregator extends Aggregator {

	public SegmentsAggregator(AggregatorConfig aggregatorConfig) {
		super(aggregatorConfig);
	}

	@Override
	protected void aggregate(CSV csv, String streamName) throws Exception {
		String filePath;
		String domain;
		int bitrate;
//		String resolution;
		long segmentNumber;
//		String durationExpected;
		double durationDiff;
//		String ffprobeScore;
//		String ffprobeContainerStartTime;
//		String ffprobeVideoStartTime;
//		String ffprobeAudioStartTime;
		double ffprobeContainerDurationDiff;
		double ffprobeVideoDurationDiff;
		double ffprobeAudioDurationDiff;
//		String ffprobeWidthDiff;
//		String ffprobeHeightDiff;
		double ffprobeBitrateDiff;
		double mediainfoAudioDelay;
		double mediainfoContainerDurationDiff;
		double mediainfoVideoDurationDiff;
		double mediainfoAudioDurationDiff;
//		String mediainfoWidthDiff;
//		String mediainfoHeightDiff;
		double mediainfoBitrateDiff;
		double mediainfoVideoMaxBitrateDiff;

		for (String[] record : csv) {
			filePath = trim(record[0]);
			domain = trim(record[1]);
			bitrate = Integer.valueOf(trim(record[2]));
//			resolution = record[3];
			segmentNumber = Long.valueOf(trim(record[4]));
//			durationExpected = record[5];
			durationDiff = Double.valueOf(trim(record[6]));
//			ffprobeScore = record[7];
//			ffprobeContainerStartTime = record[8];
//			ffprobeVideoStartTime = record[9];
//			ffprobeAudioStartTime = record[10];
			ffprobeContainerDurationDiff = Double.valueOf(trim(record[11]));
			ffprobeVideoDurationDiff = Double.valueOf(trim(record[12]));
			ffprobeAudioDurationDiff = Double.valueOf(trim(record[13]));
//			ffprobeWidthDiff = record[14];
//			ffprobeHeightDiff = record[15];
			ffprobeBitrateDiff = Double.valueOf(trim(record[16]));
			mediainfoAudioDelay = Double.valueOf(trim(record[17]));
			mediainfoContainerDurationDiff = Double.valueOf(trim(record[18]));
			mediainfoVideoDurationDiff = Double.valueOf(trim(record[19]));
			mediainfoAudioDurationDiff = Double.valueOf(trim(record[20]));
//			mediainfoWidthDiff = record[21];
//			mediainfoHeightDiff = record[22];
			mediainfoBitrateDiff = Double.valueOf(trim(record[23]));
			mediainfoVideoMaxBitrateDiff = Double.valueOf(trim(record[24]));

			addData(streamName, filePath, segmentNumber, domain, bitrate, "Duration Diff", durationDiff);
			addData(streamName, filePath, segmentNumber, domain, bitrate, "Duration Diff - FFprobe Container", ffprobeContainerDurationDiff);
			addData(streamName, filePath, segmentNumber, domain, bitrate, "Duration Diff - FFprobe Video", ffprobeVideoDurationDiff);
			addData(streamName, filePath, segmentNumber, domain, bitrate, "Duration Diff - FFprobe Audio", ffprobeAudioDurationDiff);
			addData(streamName, filePath, segmentNumber, domain, bitrate, "Bitrate Diff - FFprobe", ffprobeBitrateDiff);
			addData(streamName, filePath, segmentNumber, domain, bitrate, "Audio Delay - Media-info", mediainfoAudioDelay);
			addData(streamName, filePath, segmentNumber, domain, bitrate, "Duration Diff - Media-info Container", mediainfoContainerDurationDiff);
			addData(streamName, filePath, segmentNumber, domain, bitrate, "Duration Diff - Media-info Video", mediainfoVideoDurationDiff);
			addData(streamName, filePath, segmentNumber, domain, bitrate, "Duration Diff - Media-info Audio", mediainfoAudioDurationDiff);
			addData(streamName, filePath, segmentNumber, domain, bitrate, "Bitrate Diff - Media-info", mediainfoBitrateDiff);
			addData(streamName, filePath, segmentNumber, domain, bitrate, "Bitrate Diff - Media-info Video Max", mediainfoVideoMaxBitrateDiff);
		}
	}
}
