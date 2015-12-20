package com.kaltura.media.quality.aggregators;

import java.lang.reflect.Constructor;

import org.apache.log4j.Logger;

import com.kaltura.media.quality.configurations.AggregatorConfig;
import com.kaltura.media.quality.configurations.TestConfig;

public class AggregateResults {
	private static final Logger log = Logger.getLogger(AggregateResults.class);
	
	public static void main(String[] args) throws Exception{
		TestConfig config = TestConfig.init(args);
		
		for(AggregatorConfig aggregatorsConfig : config.getAggregators()){
			log.info("Create aggregator [" + aggregatorsConfig.getType().getName() + "]");
			Constructor<Aggregator> constructor = aggregatorsConfig.getType().getConstructor(AggregatorConfig.class);
			Aggregator aggregator = constructor.newInstance(aggregatorsConfig);
			aggregator.aggregate();
		}
		
		Aggregator.save();
	}
	
}
