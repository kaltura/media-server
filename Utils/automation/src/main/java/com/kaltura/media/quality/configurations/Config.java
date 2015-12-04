package com.kaltura.media.quality.configurations;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Config implements Serializable {
	private static final long serialVersionUID = 4049521327355718540L;
	private final Map<String , Object> otherProperties = new HashMap<>();

    @JsonAnySetter
    private void set(String name, Object value) {
        otherProperties.put(name, value);
    }

    public Map<String, Object> getOtherProperties() {
        return otherProperties;
    }

	public boolean hasOtherProperty(String key) {
		return otherProperties != null && otherProperties.containsKey(key);
	}

	public Object getOtherProperty(String key) {
		return otherProperties.get(key);
	}
}
