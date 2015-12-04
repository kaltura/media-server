package com.kaltura.media.quality.configurations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaltura.media.quality.provider.Provider;
import com.kaltura.media.quality.provider.url.UrlBuilder;

public class DataProvider extends TypedConfig<Provider> {
	private static final long serialVersionUID = -8814014190582926166L;

	@JsonProperty("playlist-enhancers")
	private List<PlaylistEnhancerConfig> playlistEnhancers = new ArrayList<PlaylistEnhancerConfig>();

    @JsonProperty("url-builder")
	private UrlBuilderConfig urlBuilderConfig;

	public List<PlaylistEnhancerConfig> getPlaylistEnhancers() {
		return playlistEnhancers;
	}

	public UrlBuilder getUrlBuilder() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Constructor<UrlBuilder> constructor = urlBuilderConfig.getType().getConstructor(UrlBuilderConfig.class);
		return constructor.newInstance(urlBuilderConfig);
	}
}
