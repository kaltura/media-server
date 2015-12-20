package com.kaltura.media.quality.model;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.log4j.Logger;

public class Rendition implements Cloneable, Serializable {
	private static final long serialVersionUID = -8276747625640628048L;
	private static final Logger log = Logger.getLogger(Rendition.class);

	private String entryId;
	private int programId;
	private int bandwidth;
	private String resolution;
	private String url;
	private String domain;
	private String domainHash;
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

	public void setUrl(String url, String name) {
		try {
			setUrl(new URI(url), name);
		} catch (URISyntaxException e) {
			log.error("Invalid URI: " + url, e);
		}
	}

	public void setUrl(URI url, String name) {
		this.url = url.toString();

		URL streamUrl;
		try {
			streamUrl = url.resolve("../../").toURL();
			domain = streamUrl.getHost();
			if (streamUrl.getPath().startsWith("/dc-")) {
				domain = streamUrl.getPath().substring(4, 5);
				domainHash = domain;
				return;
			}
		} catch (MalformedURLException e) {
			log.error("Invalid URI: " + url, e);
			domain = url.getHost();
		}
		
		domainHash = name;
	}

	public String getDomain() {
		return domain;
	}

	public String getDomainHash() {
		return domainHash;
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
}
