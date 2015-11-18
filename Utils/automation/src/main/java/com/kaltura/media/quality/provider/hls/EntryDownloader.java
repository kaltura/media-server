package com.kaltura.media.quality.provider.hls;

import java.net.URI;
import java.net.URISyntaxException;

import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.media.quality.configurations.DataProvider;

/**
 * HLS Downloader with entry context
 */
public class EntryDownloader extends Downloader {
	
	private static final long serialVersionUID = -1330759906974242260L;
	private KalturaLiveEntry entry;
	
	protected String getThreadName() {
		return getClass().getSimpleName() + "-" + entry.id;
	}
	
	public EntryDownloader(){
		super();
	}
	
	public EntryDownloader (KalturaLiveEntry entry, DataProvider providerConfig) throws URISyntaxException {
		super(entry.id, null, providerConfig);
		
		this.entry = entry;
		this.masterPlaylistUrl = getManifestUrl();
	}

    public String getDestinationFolder() throws URISyntaxException {
        return config.getDestinationFolder() + "/" + entry.id;
    }

    public URI getManifestUrl() throws URISyntaxException {
        URI base = new URI(config.getServiceUrl());
        return base.resolve(String.format("/p/%1$s/sp/%1$s00/playManifest/entryId/%2$s/format/applehttp", entry.partnerId, entry.id));
    }
}