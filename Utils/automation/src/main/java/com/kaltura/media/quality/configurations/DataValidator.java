package com.kaltura.media.quality.configurations;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaltura.media.quality.validator.Validator;

public class DataValidator extends TypedConfig<Validator> {
	private static final long serialVersionUID = 8114986699442465723L;

	@JsonProperty("deffered")
    private boolean deffered;

    @JsonProperty("comparators")
	private List<SegmentsComparatorConfig> comparators = new ArrayList<SegmentsComparatorConfig>();

	public boolean getDeffered() {
		return deffered;
	}

	public List<SegmentsComparatorConfig> getComparators() {
		return comparators;
	}
    
}
