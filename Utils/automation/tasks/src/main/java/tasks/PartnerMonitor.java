package tasks;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.client.enums.KalturaNullableBoolean;
import com.kaltura.client.types.KalturaLiveStreamEntry;
import com.kaltura.client.types.KalturaLiveStreamEntryFilter;
import com.kaltura.client.types.KalturaLiveStreamListResponse;
import configurations.ConfigurationReader;
import configurations.TestConfig;
import downloaders.hls.HLSDownloader;
import kaltura.actions.StartSession;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class PartnerMonitor {

	private TestConfig config;
	private KalturaClient client;
	
	public static void main(String[] args) throws Exception {
		PartnerMonitor monitor = new PartnerMonitor();
		monitor.execute(args);
	}
	 
	public void execute(String[] args) throws Exception {
		//load the user defined conf file
		if (args.length == 1) {
			config = getTestConfiguration(args[0]);
		}
		else {
			//load the default conf file
			config = getDefaultTestConfiguration("test-conf.json");
		}

		int partnerId = Integer.valueOf(config.getPartnerId());
		StartSession session = new StartSession(partnerId,
				config.getServiceUrl(), config.getAdminSecret());
		client = session.execute();

		Set<String> entries = new HashSet<>();

		while (true) {
			System.out.println("...");
			Set<String> curEntries = getEntries();
			for (String entryId : curEntries) {
				if (!entries.contains(entryId)) {
					// Create downloaders threads
					System.out.println("### Create new thread for entry - " + entryId);
					Timer timer = new Timer(entryId, true);
					timer.schedule(new Downloader(entryId, true, config.getDestinationFolder()), 1);
					timer = new Timer(entryId, true);
					timer.schedule(new Downloader(entryId, false, config.getDestinationFolder()), 1);
				}
			}

			entries = curEntries;
			Thread.sleep(60 * 1000);
		}
	}

	private TestConfig getDefaultTestConfiguration(String configFileName) throws Exception {
		URL u = getClass().getResource("/" + configFileName);
		if (u == null) {
			throw new Exception("Configuration file: " + configFileName
					+ " not found.");
		}
		return getTestConfiguration(u.getPath());
	}

	private TestConfig getTestConfiguration(String configFilePath) throws Exception {
		return ConfigurationReader.getTestConfigurations(configFilePath);
    }
	
	private Set<String> getEntries() throws KalturaApiException {
		Set<String> result = new HashSet<String>();
		
		KalturaLiveStreamEntryFilter filter = new KalturaLiveStreamEntryFilter();
		filter.isLive = KalturaNullableBoolean.TRUE_VALUE;
		KalturaLiveStreamListResponse results = client.getLiveStreamService()
				.list(filter);
		if (results.totalCount == 0)
			return result;

		for (KalturaLiveStreamEntry entry : results.objects) {
			result.add(entry.id);
		}
		return result;
	}
	
	public class Downloader extends TimerTask {
		
		private static final String PREFIX = "http://kalsegsec-a.akamaihd.net/dc-1/m/ny-live-publish1/kLive/smil:";
		private static final String SUFFIX = "_all.smil/playlist.m3u8";
		private final String downloadPath;// = "/root/output";
		
		private String entryId;
		private boolean useDvr;
		
		Downloader (String entryId, boolean useDvr, String destinationPath) {
			this.entryId = entryId;
			this.useDvr = useDvr;
			this.downloadPath = destinationPath;
		}

		@Override
		public void run() {
			String manifestUrl = PREFIX + entryId + SUFFIX;
			if(this.useDvr)
				manifestUrl += "?DVR";
			
			String downloadDir = downloadPath + "/" + entryId;
			if(this.useDvr)
				downloadDir += "_DVR";
						
			HLSDownloader d = new HLSDownloader();
			try {
				d.downloadFiles(manifestUrl, downloadDir);
			} catch (Exception e) {
				System.out.println("Failed to download content");
				e.printStackTrace();
			}
		}
	}
}
