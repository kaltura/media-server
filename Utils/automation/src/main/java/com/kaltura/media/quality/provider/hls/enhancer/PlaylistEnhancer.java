package com.kaltura.media.quality.provider.hls.enhancer;

import java.util.Set;

import com.kaltura.media.quality.model.Rendition;

/**
 * Created by asher.saban on 7/15/2015.
 */
public interface PlaylistEnhancer {

	public Set<Rendition> enhanceStreamsSet(Set<Rendition> streams);
}
