package com.kaltura.media.quality.aggregators;

import com.kaltura.media.quality.configurations.AggregatorConfig;

public class RenditionsCompareAggregator extends Aggregator {

	public RenditionsCompareAggregator(AggregatorConfig aggregatorConfig) {
		super(aggregatorConfig);
	}

	@Override
	protected void aggregate(CSV csv, String streamName) {
		int segmentNumber;
		String domainHash;
//		String sourceBitrate;
		int bitrate;
		double diff;

		for (String[] record : csv) {
			segmentNumber = Integer.valueOf(trim(record[0]));
			domainHash = trim(record[1]);
//			sourceBitrate = record[2];
			bitrate = Integer.valueOf(trim(record[3]));
			diff = Double.valueOf(trim(record[4]));

			addData(streamName, segmentNumber, domainHash, bitrate, "Image Magic Diff", diff);
		}
	}
}
