package com.kaltura.media.quality.provider.url;

import java.net.URI;
import java.net.URISyntaxException;

import com.kaltura.media.quality.configurations.UrlBuilderConfig;

public class UrlBuilder {

	protected String template;

	public UrlBuilder(UrlBuilderConfig urlBuilderConfig){
		template = urlBuilderConfig.getUrlTemplate();
	}
	
	public URI build(String uniqueId) throws URISyntaxException {
		return new URI(String.format(template, uniqueId));
	}

}
