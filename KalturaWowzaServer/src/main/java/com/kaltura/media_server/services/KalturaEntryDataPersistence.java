package com.kaltura.media_server.services;

import com.wowza.wms.application.IApplicationInstance;
import com.kaltura.media_server.services.*;
import org.apache.log4j.Logger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.TimerTask;
import java.util.Timer;
import java.util.Set;

/**
 * Created by lilach.maliniak on 29/01/2017.
 */
public class KalturaEntryDataPersistence {

	private static Logger logger = Logger.getLogger(KalturaEntryDataPersistence.class);
	private static ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> entryIdToKalturaLiveEntryMap = new ConcurrentHashMap<>();
	private static long lastMapCleanUp = 0;
	private static IApplicationInstance _appInstance = null;

	public static void setAppInstance(IApplicationInstance appInstance) {
		_appInstance = appInstance;
	}

	public static void entriesMapCleanUp() {
	    Timer cleanUpTimer = new Timer();
	    cleanUpTimer.schedule(new TimerTask() {
            @Override
            public void run() {
				CleanUp();
            }
        }, Constants.KALTURA_ENTRY_PERSISTENCE_CLEANUP_START);
        logger.info("Persistence hash map cleanup will start in " + Constants.KALTURA_ENTRY_PERSISTENCE_CLEANUP_START / 1000 + " seconds");
	}

	private static void CleanUp() {
        synchronized (entryIdToKalturaLiveEntryMap) {
            // Do not perform entries cleanup more than once a minute, to prevent a load of timer tasks running at the same time.
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastMapCleanUp > Constants.KALTURA_TIME_BETWEEN_PERSISTENCE_CLEANUP) {
                try {
                    Set<String> playingEntriesList = Utils.getEntriesFromApplication(_appInstance);
                    Set<String> hashedEntriesList = entryIdToKalturaLiveEntryMap.keySet();
                    long currTime = System.currentTimeMillis();
                    for (String entry : hashedEntriesList) {
                        // Check start time to avoid a race between the thread adding the entry to the map and this
                        // current thread that wants to remove it. Worst case scenario entry will ve erased in the next run.
                        long startTime = (long) getPropertyByEntry(entry, Constants.KALTURA_ENTRY_START_TIME);
                        if ((!playingEntriesList.contains(entry)) && (currTime - startTime > Constants.KALTURA_PERSISTENCE_DATA_MIN_ENTRY_TIME)) {
                            logger.info("Entry " + entry + " no longer exists. Removing it from Persistence Hash Map");
                            entryIdToKalturaLiveEntryMap.remove(entry);
                        }
                    }
                    lastMapCleanUp = currentTime;
                } catch (Exception e) {
                    logger.error("Error occurred while cleaning Persistence Data map: " + e);
                }
            }
            else {
                logger.debug("Persistence hash map was cleaned less than 1 min ago. Ignoring call!");
            }
		}
	}

	public static Object getPropertyByStream(String streamName, String subKey) throws Exception {

		String entryIdKey = Utils.getEntryIdFromStreamName(streamName);
		return getPropertyByEntry(entryIdKey, subKey);
	}

	public static Object getPropertyByEntry(String entryId, String subKey) throws Exception {

		Object value;

		try {
			ConcurrentHashMap<String, Object> entryMap = entryIdToKalturaLiveEntryMap.get(entryId);
			value = entryMap.get(subKey);
		} catch (Exception e) {
			logger.error("(" + entryId + ") Failed to get value for key \"" + subKey + "\". " + e);
			throw e;
		}

		logger.debug("(" + entryId + ") Successfully got entry (map size:" + entryIdToKalturaLiveEntryMap.size() + ")");

		return value;
	}

	// note: call add entry on connect (after successful authentication)
	public static void setProperty(String entryId, String subKey, Object value) throws Exception {
		synchronized (entryIdToKalturaLiveEntryMap) {
			entryIdToKalturaLiveEntryMap.putIfAbsent(entryId, new ConcurrentHashMap<String, Object>());
			setValueProperty(entryId, subKey, value);
		}
	}

	private static void setValueProperty(String entryId, String subKey, Object value) throws Exception {
		ConcurrentHashMap<String, Object> entryMap = entryIdToKalturaLiveEntryMap.get(entryId);
		entryMap.put(subKey, value);
		entryMap.put(Constants.KALTURA_ENTRY_START_TIME, System.currentTimeMillis());
		logger.debug("(" + entryId + ") Successfully updated entry. Sub key \"" + subKey + "\" (map size:" + entryIdToKalturaLiveEntryMap.size() + ")");
	}
}
