package com.kaltura.media.quality.configurations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaltura.media.quality.aggregators.Aggregator;

public class AggregatorConfig extends TypedConfig<Aggregator> {
	private static final long serialVersionUID = 7925447910326809747L;

	@JsonProperty("path")
    private String path;

	@JsonProperty("skip-headers")
    private boolean skipHeaders = true;

	public String getPath() {
		return path;
	}

	public boolean getSkipHeaders() {
		return skipHeaders;
	}
}
