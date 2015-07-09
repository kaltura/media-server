package tasks.systemmonitor;

import java.net.URI;
import java.net.URISyntaxException;

import tasks.StatusWatcher;
import utils.ManifestUrlBuilder;
import configurations.TestConfig;
import downloaders.hls.HLSDownloader;

/**
 * This thread is resposible for files downloading 
 */
public class EntryDownloader implements StatusWatcher, Runnable {
	
	private TestConfig config;
	private String partnerId;
	private String entryId;
	
	private boolean isAlive = true;
	private HLSDownloader hlsDownloader = null;
	
	public EntryDownloader (TestConfig config, Integer partnerId, String entryId) {
		this.config = config;
		this.partnerId = ""+partnerId;
		this.entryId = entryId;
	}

	@Override
	public void run() {
		URI manifestUri;
		try {
			manifestUri = ManifestUrlBuilder.buildManifestUrl(config.getServiceUrl(), entryId, partnerId);
		} catch (URISyntaxException e) {
			System.err.println("Failed to retrieve master manifest");
			e.printStackTrace();
			isAlive = false;
			return;
		}
		
		String manifestUrl = manifestUri.toString();
		System.out.println(manifestUrl);
		
		String downloadDir = config.getDestinationFolder() + "/" + entryId;
		hlsDownloader = new HLSDownloader();
		
		try {
			hlsDownloader.downloadFiles(manifestUrl, downloadDir);
		} catch (Exception e) {
			System.out.println("Failed to download content");
			isAlive = false;
			e.printStackTrace();
		}
	}
	
	/**
	 * This function indicates to the downloader' dependents when can they stop listening
	 */
	public boolean isAlive() {
		if(!isAlive)
			return false;
		if(hlsDownloader != null) {
			isAlive = hlsDownloader.isAlive();
			if(!isAlive)
				return false;
		}
		return true;
	}
}