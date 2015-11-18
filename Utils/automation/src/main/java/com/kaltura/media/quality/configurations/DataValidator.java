package com.kaltura.media.quality.configurations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaltura.media.quality.validator.Validator;

public class DataValidator extends TypedConfig<Validator> {

    @JsonProperty("deffered")
    private boolean deffered;
    
	public boolean getDeffered() {
		return deffered;
	}
    
}
