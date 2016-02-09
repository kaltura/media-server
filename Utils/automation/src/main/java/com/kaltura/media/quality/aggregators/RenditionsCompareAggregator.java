package com.kaltura.media.quality.aggregators;

import com.kaltura.media.quality.configurations.AggregatorConfig;

public class RenditionsCompareAggregator extends Aggregator {

	private String comperatorFilter;
	private String title;

	public RenditionsCompareAggregator(AggregatorConfig aggregatorConfig) {
		super(aggregatorConfig);
		
		if(aggregatorConfig.hasOtherProperty("comperator")){
			this.comperatorFilter = (String) aggregatorConfig.getOtherProperty("comperator");
		}
		
		if(aggregatorConfig.hasOtherProperty("title")){
			this.title = (String) aggregatorConfig.getOtherProperty("title");
		}
	}

	@Override
	protected void aggregate(CSV csv, String streamName) {
		int segmentNumber;
		String provider;
		String comperator;
//		String sourceBitrate;
		int bitrate;
		double diff;

		for (String[] record : csv) {
			segmentNumber = Integer.valueOf(trim(record[0]));
			provider = trim(record[1]);
			comperator = trim(record[2]);
//			sourceBitrate = record[3];
			bitrate = Integer.valueOf(trim(record[4]));
			diff = Double.valueOf(trim(record[5]));

			if(comperator.equals(comperatorFilter)){
				addData(streamName, segmentNumber, provider, bitrate, title, diff);
			}
		}
	}
}
