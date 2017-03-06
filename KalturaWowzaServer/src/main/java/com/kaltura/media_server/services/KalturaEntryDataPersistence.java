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
	private static Object _cleanUpLock = new Object();
	private static long _lastMapCleanUp = 0;
	private static IApplicationInstance _appInstance = null;

	public static void setAppInstance(IApplicationInstance appInstance) {
		_appInstance = appInstance;
	}

	public static void entriesMapCleanUp() {
	    synchronized (_cleanUpLock) {
            // Do not perform entries cleanup more than once a minute, to prevent a load of timer tasks running at the same time.
            long currentTime = System.currentTimeMillis();
            if (currentTime - _lastMapCleanUp > Constants.KALTURA_TIME_BETWEEN_PERSISTENCE_CLEANUP) {
                _lastMapCleanUp = currentTime;
                Timer cleanUpTimer = new Timer();
                cleanUpTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        CleanUp();
                    }
                }, Constants.KALTURA_ENTRY_PERSISTENCE_CLEANUP_START);
                logger.debug("Persistence hash map cleanup will start in " + Constants.KALTURA_ENTRY_PERSISTENCE_CLEANUP_START / 1000 + " seconds");
            } else {
                logger.debug("Persistence cleanup was called less than " + Constants.KALTURA_TIME_BETWEEN_PERSISTENCE_CLEANUP / 1000 + " seconds ago. Ignoring call!");
            }
        }
	}

	private static void CleanUp() {
	    try {
            synchronized (entryIdToKalturaLiveEntryMap) {
                logger.debug("KalturaEntryDataPersistence CleanUp started");
                Set<String> playingEntriesList = Utils.getEntriesFromApplication(_appInstance);
                Set<String> hashedEntriesList = entryIdToKalturaLiveEntryMap.keySet();
                long currentTime = System.currentTimeMillis();
                for (String entry : hashedEntriesList) {
                    // Check start time to avoid a race between the thread adding the entry to the map and this
                    // current thread that wants to remove it. Worst case scenario entry will ve erased in the next run.
                    long validationTime = (long)getPropertyByEntry(entry, Constants.KALTURA_ENTRY_VALIDATED_TIME);
                    if (!playingEntriesList.contains(entry)) {
                        logger.info("(" + entry + ") Entry is no longer playing!");
                        int minTimeInMap = Constants.KALTURA_PERSISTENCE_DATA_MIN_ENTRY_TIME;
                        if (currentTime - validationTime > minTimeInMap) {
                            logger.info("(" + entry + ") Entry validation time is greater than " + minTimeInMap / 1000 + " seconds; Removing it from Map!");
                            entryIdToKalturaLiveEntryMap.remove(entry);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            logger.error("Error occurred while cleaning Persistence Data map: " + e);
        }
	}

	public static Object getPropertyByStream(String streamName, String subKey) throws Exception {
        try {
            String entryIdKey = Utils.getEntryIdFromStreamName(streamName);
            return getPropertyByEntry(entryIdKey, subKey);
        }
        catch (Exception e) {
            logger.error("(" + streamName + ") Stream failed to retrieve value form entry. Error: " + e);
            throw e;
        }
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

		logger.debug("(" + entryId + ") Successfully got entry subKey: (" + subKey + ")");

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
		long time = System.currentTimeMillis();
		entryMap.put(Constants.KALTURA_ENTRY_VALIDATED_TIME, time);
		logger.debug("(" + entryId + ") Successfully updated entry ; Sub key \"" + subKey + "\" ; Time: (" + time + ")");
	}
}
