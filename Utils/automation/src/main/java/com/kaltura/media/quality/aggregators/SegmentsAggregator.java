package com.kaltura.media.quality.aggregators;

import com.kaltura.media.quality.configurations.AggregatorConfig;

public class SegmentsAggregator extends Aggregator {

	public SegmentsAggregator(AggregatorConfig aggregatorConfig) {
		super(aggregatorConfig);
	}

	@Override
	protected void aggregate(CSV csv, String streamName) throws Exception {
		String filePath;
		String domainHash;
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
			domainHash = trim(record[1]);
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

			addData(streamName, filePath, segmentNumber, domainHash, bitrate, "Duration Diff", durationDiff);
			addData(streamName, filePath, segmentNumber, domainHash, bitrate, "FFprobe Container Duration Diff", ffprobeContainerDurationDiff);
			addData(streamName, filePath, segmentNumber, domainHash, bitrate, "FFprobe Video Duration Diff", ffprobeVideoDurationDiff);
			addData(streamName, filePath, segmentNumber, domainHash, bitrate, "FFprobe Audio Duration Diff", ffprobeAudioDurationDiff);
			addData(streamName, filePath, segmentNumber, domainHash, bitrate, "FFprobe Bitrate Diff", ffprobeBitrateDiff);
			addData(streamName, filePath, segmentNumber, domainHash, bitrate, "Media-info Audio Delay", mediainfoAudioDelay);
			addData(streamName, filePath, segmentNumber, domainHash, bitrate, "Media-info Container Duration Diff", mediainfoContainerDurationDiff);
			addData(streamName, filePath, segmentNumber, domainHash, bitrate, "Media-info Video Duration Diff", mediainfoVideoDurationDiff);
			addData(streamName, filePath, segmentNumber, domainHash, bitrate, "Media-info Audio Duration Diff", mediainfoAudioDurationDiff);
			addData(streamName, filePath, segmentNumber, domainHash, bitrate, "Media-info Bitrate Diff", mediainfoBitrateDiff);
			addData(streamName, filePath, segmentNumber, domainHash, bitrate, "Media-info Video Max Bitrate Diff", mediainfoVideoMaxBitrateDiff);
		}
	}
}
