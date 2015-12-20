package com.kaltura.media.quality.provider.hls.enhancer;

import java.util.HashSet;
import java.util.Set;

import com.kaltura.media.quality.model.Rendition;

/**
 * Created by asher.saban on 7/15/2015.
 * In case of a DVR stream, this enhancer also adds the input stream
 */
public class DVRInputStreamEnhancer implements PlaylistEnhancer {

	private static final String DVR_SUFFIX = "_DVR.m3u8";
	private static final String NO_DVR_SUFFIX = ".m3u8";
	private String name;
	
	@Override
	public Set<Rendition> enhanceStreamsSet(Set<Rendition> streams) {
		Set<Rendition> newSet = new HashSet<Rendition>(streams);
		
		String url;
		Rendition newRendition;
		
		for (Rendition rendition : streams) {
			url = rendition.getUrl();
			if (url.endsWith(DVR_SUFFIX)) {
				newRendition = rendition.clone();
				newRendition.setUrl(url.replace(DVR_SUFFIX, NO_DVR_SUFFIX), name);
				newSet.add(newRendition);
			}
		}
		return newSet;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
}
