package tasks.monitor;

import java.net.URI;
import java.net.URISyntaxException;

import tasks.StatusWatcher;
import utils.ManifestUrlBuilder;
import configurations.TestConfig;
import downloaders.hls.DVRInputStreamEnhancer;
import downloaders.hls.HLSDownloader;

/**
 * This thread is resposible for files downloading 
 */
public class EntryDownloader implements StatusWatcher, Runnable {
	
	private TestConfig config;
	private String partnerId;
	private String entryId;
	private boolean runForever;
	private String downloadDir;
	
	private boolean isAlive = true;
	private HLSDownloader hlsDownloader = null;
	
	public EntryDownloader (Integer partnerId, String entryId, boolean runForever) {
		this.config = TestConfig.get();
		this.partnerId = ""+partnerId;
		this.entryId = entryId;
		this.runForever = runForever;
		this.downloadDir = config.getDestinationFolder() + "/" + entryId;
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
		
		hlsDownloader = new HLSDownloader(new DVRInputStreamEnhancer(), runForever);
		
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
	
	public void setDownloadDir(String downloadDir) {
		this.downloadDir = downloadDir;
	}
}