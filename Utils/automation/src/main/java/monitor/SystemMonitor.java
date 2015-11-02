package monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import provider.Provider;
import utils.ThreadManager;
import validator.Validator;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.enums.KalturaLiveStreamEntryOrderBy;
import com.kaltura.client.enums.KalturaNullableBoolean;
import com.kaltura.client.types.KalturaFilterPager;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.client.types.KalturaLiveStreamEntry;
import com.kaltura.client.types.KalturaLiveStreamEntryFilter;
import com.kaltura.client.types.KalturaLiveStreamListResponse;

import configurations.DataProvider;
import configurations.DataValidator;

/**
 * This class monitors the system:
 * For each live entry in the system it creates a downloading thread
 * and comparator thread.
 */
public class SystemMonitor extends Monitor {
	private static final Logger log = Logger.getLogger(SystemMonitor.class);

	public static void main(String[] args) throws Exception {
		SystemMonitor monitor = new SystemMonitor(args);
		monitor.execute();
	}

	public SystemMonitor(String[] args) throws Exception {
		super(args);
	}

	protected void execute() throws Exception {
		Thread.currentThread().setName("main");
		Set<String> entries = new HashSet<String>();
		Collection<KalturaLiveEntry> curEntries;

		while(ThreadManager.shouldContinue()) {
			log.debug("...");
			curEntries = getEntries();
			if(curEntries.size() == 0){
				log.info("No live entries found");
			}
			for (KalturaLiveEntry entry : curEntries) {
				if(!entries.contains(entry.id)) {
					entries.add(entry.id);
					
					log.info("Create providers for entry - " + entry.id);					
					List<Provider> providers = new ArrayList<Provider>();
					for(DataProvider dataProvider : config.getDataProviders()){
						Constructor<Provider> constructor = dataProvider.getType().getConstructor(KalturaLiveEntry.class);
						Provider provider = constructor.newInstance(entry);
						providers.add(provider);
						provider.start();
					}

					log.info("Create validators for entry - " + entry.id);
					for(DataValidator dataValidator : config.getDataValidators()){
						Constructor<Validator> constructor = dataValidator.getType().getConstructor(KalturaLiveEntry.class, List.class);
						constructor.newInstance(entry, providers);
					}
				}
			}
			
			Thread.sleep(60*1000);
		}
		log.info("Stopping all threads");
		ThreadManager.stop();
		
		Thread[] threads = getAllThreads();
		for(Thread thread : threads){
			System.err.println("Thread " + thread.getName() + " is still alive");
		}
	}
	
	protected Collection<KalturaLiveEntry> getEntries() throws KalturaApiException {
		Map<String, KalturaLiveEntry> result = new HashMap<String, KalturaLiveEntry>();
		
		KalturaLiveStreamEntryFilter filter = new KalturaLiveStreamEntryFilter();
		filter.isLive = KalturaNullableBoolean.TRUE_VALUE;
		filter.orderBy = KalturaLiveStreamEntryOrderBy.CREATED_AT_ASC.getHashCode();
		
		KalturaFilterPager pager = new KalturaFilterPager();
		pager.pageSize = 500;
		pager.pageIndex = 1;
		
		while(ThreadManager.shouldContinue()) {
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
				result.put(entry.id, entry);
				filter.createdAtGreaterThanOrEqual = entry.createdAt;
			}
			
			pager.pageIndex++;
			if(entries.totalCount < 500)
				break;
		}
		return result.values();
	}
	
	
	
	
	ThreadGroup getRootThreadGroup( ) {
	    ThreadGroup tg = Thread.currentThread( ).getThreadGroup( );
	    ThreadGroup ptg;
	    while ( (ptg = tg.getParent( )) != null )
	        tg = ptg;
	    return tg;
	}
	
	Thread[] getAllThreads( ) {
	    final ThreadGroup root = getRootThreadGroup( );
	    final ThreadMXBean thbean = ManagementFactory.getThreadMXBean( );
	    int nAlloc = thbean.getThreadCount( );
	    int n = 0;
	    Thread[] threads;
	    do {
	        nAlloc *= 2;
	        threads = new Thread[ nAlloc ];
	        n = root.enumerate( threads, true );
	    } while ( n == nAlloc );
	    return java.util.Arrays.copyOf( threads, n );
	}
}