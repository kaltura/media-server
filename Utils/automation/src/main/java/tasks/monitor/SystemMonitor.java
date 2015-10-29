package tasks.monitor;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utils.ThreadManager;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.enums.KalturaLiveStreamEntryOrderBy;
import com.kaltura.client.enums.KalturaNullableBoolean;
import com.kaltura.client.types.KalturaFilterPager;
import com.kaltura.client.types.KalturaLiveStreamEntry;
import com.kaltura.client.types.KalturaLiveStreamEntryFilter;
import com.kaltura.client.types.KalturaLiveStreamListResponse;

/**
 * This class monitors the system:
 * For each live entry in the system it creates a downloading thread
 * and comparator thread.
 */
public class SystemMonitor extends Monitor {

	public static void main(String[] args) throws Exception {
		SystemMonitor monitor = new SystemMonitor(args);
		monitor.execute();
	}

	public SystemMonitor(String[] args) throws Exception {
		super(args);
	}

	protected void execute() throws Exception {
		List<String> syncEntries = config.getSyncEntries();
		
		System.out.println("Loaded!");
		
		Set<String> entries = new HashSet<String>();
		String entryId;
		int curPartnerId;
		Map<String, Integer> curEntries;

		int numberOfThreads = 10;
		if(config.hasOtherProperty("number-of-threads")){
			numberOfThreads = (int) config.getOtherProperty("number-of-threads");
		}
		ExecutorService downloadersExecutor = Executors.newFixedThreadPool(numberOfThreads / 2);
		ExecutorService comparatorsExecutor = Executors.newFixedThreadPool(numberOfThreads / 2);

		while(ThreadManager.shouldContinue()) {
			System.out.println("...");
			curEntries = getEntries();
			if(curEntries.size() == 0){
				System.out.println("No live entries found");
			}
			for (Entry<String, Integer> entry : curEntries.entrySet()) {
				entryId = entry.getKey();
				if(!entries.contains(entryId)) {
					curPartnerId = entry.getValue();
					entries.add(entryId);
					
					// Create downloaders threads
					System.out.println("### Create new thread for entry - " + entryId);
					String downloadDir = createDefaultDownloadDir(entryId);
					
					EntryDownloader downloader = new EntryDownloader(curPartnerId, entryId, syncEntries.contains(entryId));
					downloader.setDownloadDir(downloadDir);
					downloadersExecutor.execute(downloader);
					EntryComparator comparator = new EntryComparator(downloader, entryId, downloadDir);
					comparatorsExecutor.execute(comparator);
				}
			}
			
			Thread.sleep(60*1000);
		}
		System.out.println("Stopping all threads");
		ThreadManager.stop();
	}
	
	protected Map<String, Integer> getEntries() throws KalturaApiException {
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