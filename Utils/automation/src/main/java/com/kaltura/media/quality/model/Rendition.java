package com.kaltura.media.quality.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class Rendition implements Cloneable, Serializable {
	private static final long serialVersionUID = -8276747625640628048L;
	private static final Logger log = Logger.getLogger(Rendition.class);
	
	private static Map<String, List<Rendition>> deserializedRenditions = new HashMap<String, List<Rendition>>();

	private String entryId;
	private int programId;
	private int bandwidth;
	private String resolution;
	private String url;
	private String name;
	private int width;
	private int height;

	public Rendition(String entryId){
		this.entryId = entryId;
	}
	
	@Override
	public Rendition clone() {
		try {
			return (Rendition) super.clone();
		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		
		List<Rendition> deserialized;
		if(deserializedRenditions.containsKey(getUrl())) {
			deserialized = deserializedRenditions.get(getUrl());
		}
		else {
			deserialized = new ArrayList<Rendition>();
			deserializedRenditions.put(getUrl(), deserialized);
		}
		deserialized.add(this);
	}

	public void setProgramId(int programId) {
		this.programId = programId;
	}

	public void setBandwidth(int bandwidth) {
		this.bandwidth = bandwidth;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;

		String[] resolutionParts = resolution.split("x");
		width = Integer.valueOf(resolutionParts[0]);
		height = Integer.valueOf(resolutionParts[1]);
	}

	public int getProgramId() {
		return programId;
	}

	public int getBandwidth() {
		return bandwidth;
	}

	public String getResolution() {
		return resolution;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		try {
			setUrl(new URI(url));
		} catch (URISyntaxException e) {
			log.error("Invalid URI: " + url, e);
		}
	}

	public void setUrl(URI url) {
		this.url = url.toString();
	}
	
	public void setProviderName(String name) {
		this.name = name;
	}

	public String getProviderName() {
		return name;
	}

	public String getEntryId() {
		return entryId;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setWidth(int width) {
		setWidthInternal(width);
		
		if(deserializedRenditions.containsKey(getUrl())) {
			List<Rendition> deserialized = deserializedRenditions.get(getUrl());
			for(Rendition rendition : deserialized){
				rendition.setWidthInternal(width);
			}
		}
	}

	public void setHeight(int height) {
		setHeightInternal(height);
		
		if(deserializedRenditions.containsKey(getUrl())) {
			List<Rendition> deserialized = deserializedRenditions.get(getUrl());
			for(Rendition rendition : deserialized){
				rendition.setHeightInternal(height);
			}
		}
	}

	protected void setWidthInternal(int width) {
		this.width = width;
	}

	protected void setHeightInternal(int height) {
		this.height = height;
	}
}
