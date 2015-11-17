package com.kaltura.media.quality.model;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

import com.kaltura.media.quality.utils.StringUtils;

public class Rendition implements Cloneable {
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

	public void setUrl(String url) {
		this.url = url;

		URL streamUrl;
		try {
			streamUrl = new URI(url).resolve("../../").toURL();
			domain = streamUrl.getHost();
			if (streamUrl.getPath().startsWith("/dc-")) {
				domain = streamUrl.getPath().substring(4, 5);
				domainHash = domain;
				return;
			}
		} catch (MalformedURLException | URISyntaxException e) {
			log.error("Invalid URI: " + url);

			try {
				streamUrl = new URL(url);
				domain = streamUrl.getHost();
			} catch (MalformedURLException ex) {
				log.error("Invalid URL: " + url);
			}
		}

		try {
			domainHash = StringUtils.md5(domain);
		} catch (NoSuchAlgorithmException e) {
		}
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
