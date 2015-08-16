package tasks.systemmonitor;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import kaltura.actions.StartSession;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.client.enums.KalturaLiveStreamEntryOrderBy;
import com.kaltura.client.enums.KalturaNullableBoolean;
import com.kaltura.client.types.KalturaFilterPager;
import com.kaltura.client.types.KalturaLiveStreamEntry;
import com.kaltura.client.types.KalturaLiveStreamEntryFilter;
import com.kaltura.client.types.KalturaLiveStreamListResponse;

import configurations.ConfigurationReader;
import configurations.TestConfig;

/**
 * This class monitors the system:
 * For each live entry in the system it creates a downloading thread
 * and comparator thread.
 */
public class SystemMonitor {

	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	private TestConfig config;
	private StartSession session;
	private KalturaClient client;
	
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("Usage: [path to conf file]");
			System.exit(1);
		}
		SystemMonitor monitor = new SystemMonitor();
		monitor.execute(args[0]);
	}
	 
	public void execute(String pathToConfFile) throws Exception {
		config = ConfigurationReader.getTestConfigurations(new File(pathToConfFile));
		int partnerId = Integer.valueOf(config.getPartnerId());
		List<String> syncEntries = config.getSyncEntries();
		
		session = new StartSession(partnerId,
				config.getServiceUrl(), config.getAdminSecret());
		client = session.execute();
		
		System.out.println("Loaded!");
		
		Set<String> entries = new HashSet<String>();

		while(true) {
			System.out.println("...");
			Map<String, Integer> curEntries = getEntries();
			for (Entry<String, Integer> entry : curEntries.entrySet()) {
				if(!entries.contains(entry.getKey())) {
					// Create downloaders threads
					System.out.println("### Create new thread for entry - " + entry.getKey());
					String downloadDir = createDefaultDownloadDir(config,  entry.getKey());
					
					EntryDownloader downloader = new EntryDownloader(config, entry.getValue(), entry.getKey(), syncEntries.contains(entry.getKey()));
					downloader.setDownloadDir(downloadDir);
					(new Thread(downloader, "Downloader_" + entry.getKey())).start();
					EntryComparator comparator = new EntryComparator(config, downloader, entry.getKey(), downloadDir);
					(new Thread(comparator, "Comparator_" + entry.getKey())).start();
				}
			}
			
			entries = curEntries.keySet();
			Thread.sleep(60*1000);
		}
	}
	
	private String createDefaultDownloadDir(TestConfig config, String entryId) {
		String reportDate = dateFormat.format(new Date());
		return config.getDestinationFolder() + "/" + entryId + "/" + reportDate;
	}
	
	private Map<String, Integer> getEntries() throws KalturaApiException {
		Map<String, Integer> result = new HashMap<String, Integer>();
		
		KalturaLiveStreamEntryFilter filter = new KalturaLiveStreamEntryFilter();
		filter.isLive = KalturaNullableBoolean.TRUE_VALUE;
		filter.orderBy = KalturaLiveStreamEntryOrderBy.CREATED_AT_ASC.getHashCode();
		
		KalturaFilterPager pager = new KalturaFilterPager();
		pager.pageSize = 500;
		pager.pageIndex = 1;
		
		while(true) {
			KalturaLiveStreamListResponse entries = null;
			
			try {
				entries = client.getLiveStreamService().list(filter, pager);
			} catch (KalturaApiException e) {
				if("INVALID_KS".equals(e.code)) {
					client = session.execute();
					continue;
				} else {
					throw e;
				}
			}
			for(KalturaLiveStreamEntry entry : entries.objects) {
				result.put(entry.id, entry.partnerId);
				filter.createdAtGreaterThanOrEqual = entry.createdAt;
			}
			
			pager.pageIndex++;
			if(entries.totalCount < 500)
				break;
		}
		return result;
	}
	
}