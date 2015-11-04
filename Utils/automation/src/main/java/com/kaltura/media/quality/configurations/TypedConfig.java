package com.kaltura.media.quality.configurations;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TypedConfig<T> extends Config {
    @JsonProperty("type")
    private Class<T> type;

	public Class<T> getType() {
		return type;
	}

	@SuppressWarnings("unchecked")
	public void setType(String type) throws ClassNotFoundException {
		this.type = (Class<T>) Class.forName(type);
	}
    
    
}
