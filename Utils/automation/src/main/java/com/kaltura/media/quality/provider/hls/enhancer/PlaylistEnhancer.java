package com.kaltura.media.quality.provider.hls.enhancer;

import java.util.Set;

/**
 * Created by asher.saban on 7/15/2015.
 */
public interface PlaylistEnhancer {

	public Set<String> enhanceStreamsSet(Set<String> streams);
}
