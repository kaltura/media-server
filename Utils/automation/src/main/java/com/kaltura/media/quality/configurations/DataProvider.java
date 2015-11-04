package com.kaltura.media.quality.configurations;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaltura.media.quality.provider.Provider;

public class DataProvider extends TypedConfig<Provider> {

    @JsonProperty("playlist-enhancers")
	private List<PlaylistEnhancerConfig> playlistEnhancers = new ArrayList<PlaylistEnhancerConfig>();

	public List<PlaylistEnhancerConfig> getPlaylistEnhancers() {
		return playlistEnhancers;
	}
}
