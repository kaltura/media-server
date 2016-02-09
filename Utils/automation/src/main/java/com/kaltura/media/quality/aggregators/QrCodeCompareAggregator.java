package com.kaltura.media.quality.aggregators;

import com.kaltura.media.quality.configurations.AggregatorConfig;

public class QrCodeCompareAggregator extends Aggregator {

	public QrCodeCompareAggregator(AggregatorConfig aggregatorConfig) {
		super(aggregatorConfig);
	}

	@Override
	protected void aggregate(CSV csv, String streamName) {
		int segmentNumber;
		String domain;
		int bitrate;
//		String dts;
//		String pts;
//		String keyFrame;
//		String qrCodeTime;
		double diff;

		for (String[] record : csv) {
			segmentNumber = Integer.valueOf(trim(record[0]));
			domain = trim(record[1]);
			bitrate = Integer.valueOf(trim(record[2]));
//			dts = record[3];
//			pts = record[4];
//			keyFrame = record[5];
//			qrCodeTime = record[6];
			diff = Double.valueOf(trim(record[7]));

			addData(streamName, segmentNumber, domain, bitrate, "Time Diff - QR Code", diff);
		}
	}
}
