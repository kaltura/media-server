package com.kaltura.media.quality.configurations;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TypedConfig<T> extends Config implements Serializable {
	private static final long serialVersionUID = 6304987929871598762L;
	
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
