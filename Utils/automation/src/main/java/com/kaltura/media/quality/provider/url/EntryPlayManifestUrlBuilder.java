package com.kaltura.media.quality.provider.url;

import java.net.URI;
import java.net.URISyntaxException;

import com.kaltura.media.quality.configurations.TestConfig;
import com.kaltura.media.quality.configurations.UrlBuilderConfig;

public class EntryPlayManifestUrlBuilder extends UrlBuilder {

	private TestConfig config;

	public EntryPlayManifestUrlBuilder(UrlBuilderConfig urlBuilderConfig){
		super(urlBuilderConfig);

		config = TestConfig.get();
		
		if(template == null){
			template = "/index.php/extwidget/playManifest/entryId/%s/format/applehttp";
		}
	}
	
	public URI build(String uniqueId) throws URISyntaxException {
        URI base = new URI(config.getServiceUrl());
        return base.resolve(String.format(template, uniqueId));
	}

}
