package com.kaltura.media.quality.configurations;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaltura.media.quality.provider.url.UrlBuilder;

public class UrlBuilderConfig extends TypedConfig<UrlBuilder> {
	private static final long serialVersionUID = -8570005452419380268L;
	
	@JsonProperty("url-template")
	private String urlTemplate;

	public String getUrlTemplate() {
		return urlTemplate;
	}
}
