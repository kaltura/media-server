package com.kaltura.media.quality.aggregators;

import com.kaltura.media.quality.configurations.AggregatorConfig;

public class QualitySegmentsCompareAggregator extends Aggregator {

	public QualitySegmentsCompareAggregator(AggregatorConfig aggregatorConfig) {
		super(aggregatorConfig);
	}

	@Override
	protected void aggregate(CSV csv, String streamName) throws Exception {
		String[] record = csv.get(0);
		String title = record[0];

		record = csv.get(2);
		String fileName = record[0];

		record = csv.get(3);
		String avg = record[0];
		
		record = avg.split(":");
		double value = Double.valueOf(record[1].trim());
		
		addData(streamName, fileName, title, value);
	}
}
