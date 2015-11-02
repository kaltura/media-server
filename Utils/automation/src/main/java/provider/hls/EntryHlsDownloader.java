package provider.hls;

import java.net.URI;
import java.net.URISyntaxException;

import com.kaltura.client.types.KalturaLiveEntry;

public class EntryHlsDownloader extends HLSDownloader {
	
	private KalturaLiveEntry entry;
	
	protected String getThreadName() {
		return getClass().getSimpleName() + "-" + entry.id;
	}
	
	public EntryHlsDownloader (KalturaLiveEntry entry) throws URISyntaxException {
		super();
		
		this.entry = entry;
		this.downloadDir = config.getDestinationFolder() + "/" + entry.id;
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