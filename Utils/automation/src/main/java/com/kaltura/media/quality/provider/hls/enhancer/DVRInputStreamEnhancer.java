package com.kaltura.media.quality.provider.hls.enhancer;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by asher.saban on 7/15/2015.
 * In case of a DVR stream, this enhancer also adds the input stream
 */
public class DVRInputStreamEnhancer implements PlaylistEnhancer {

	private static final String DVR_SUFFIX = "_DVR";
	@Override
	public Set<String> enhanceStreamsSet(Set<String> streams) {
		Set<String> newSet = new HashSet<>(streams);
		String suffix = DVR_SUFFIX + ".m3u8";
		for (String s : streams) {
			if (s.endsWith(suffix)) {
				newSet.add(s.replace(suffix, ".m3u8"));
			}
		}
		return newSet;
	}
}
