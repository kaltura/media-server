package com.kaltura.media.quality.monitor;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.enums.KalturaLiveStreamEntryOrderBy;
import com.kaltura.client.enums.KalturaNullableBoolean;
import com.kaltura.client.types.KalturaFilterPager;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.client.types.KalturaLiveStreamEntry;
import com.kaltura.client.types.KalturaLiveStreamEntryFilter;
import com.kaltura.client.types.KalturaLiveStreamListResponse;
import com.kaltura.media.quality.configurations.DataProvider;
import com.kaltura.media.quality.configurations.DataValidator;
import com.kaltura.media.quality.configurations.LoggerConfig;
import com.kaltura.media.quality.provider.Provider;
import com.kaltura.media.quality.utils.ThreadManager;
import com.kaltura.media.quality.validator.Validator;
import com.kaltura.media.quality.validator.logger.ResultsLogger;

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
		Thread thread = Thread.currentThread();
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.setName("main");
		
		Set<String> entries = new HashSet<String>();
		Collection<KalturaLiveEntry> curEntries;
		int maxNumberOfEntries = config.getMaxNumberOfEntries();

		while(ThreadManager.shouldContinue()) {
			log.debug("...");
			curEntries = getEntries();
			if(curEntries.size() == 0){
				log.info("No live entries found");
			}
			for (KalturaLiveEntry entry : curEntries) {
				if(!entries.contains(entry.id) && entries.size() < maxNumberOfEntries) {
					entries.add(entry.id);
					
					log.info("Create providers for entry - " + entry.id);					
					for(DataProvider dataProvider : config.getDataProviders()){
						Constructor<Provider> constructor = dataProvider.getType().getConstructor(KalturaLiveEntry.class, DataProvider.class);
						Provider provider = constructor.newInstance(entry, dataProvider);
						provider.start();
					}

					log.info("Create validators for entry - " + entry.id);
					for(DataValidator dataValidator : config.getDataValidators()){
						Constructor<Validator> constructor = dataValidator.getType().getConstructor(String.class, DataValidator.class);
						constructor.newInstance(entry.id, dataValidator);
					}

					log.info("Create loggers for entry - " + entry.id);
					for(LoggerConfig loggerConfig : config.getResultLoggers()){
						Constructor<ResultsLogger> constructor = loggerConfig.getType().getConstructor(String.class, LoggerConfig.class);
						constructor.newInstance(entry.id, loggerConfig);
					}
				}
			}
			
			Thread.sleep(1000);
		}
		log.info("Stopping all threads");
		ThreadManager.stop();
		System.exit(0);
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
}