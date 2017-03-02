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
	private static IApplicationInstance _appInstance = null;
	public static ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> entryIdToKalturaLiveEntryMap = new ConcurrentHashMap<>();

	public static void setAppInstance(IApplicationInstance appInstance) {
		_appInstance = appInstance;
	}

	public static void entriesMapCleanUp() {
	    Timer cleanUpTimer = new Timer();
	    cleanUpTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (entryIdToKalturaLiveEntryMap) {
                    try {
                        Set<String> playingEntriesList = Utils.getEntriesFromApplication(_appInstance);
                        Set<String> hashedEntriesList = entryIdToKalturaLiveEntryMap.keySet();
                        for (String entry : hashedEntriesList) {
                            if (!playingEntriesList.contains(entry)) {
                                logger.info("Entry " + entry + " no longer exists. Removing it from Persistence Hash Map");
                                entryIdToKalturaLiveEntryMap.remove(entry);
                            }
                        }
                    }
                    catch (Exception e) {
                        logger.error("Error occurred while cleaning Persistence Data map: " + e);
                    }
                }
            }
        }, Constants.KALTURA_ENTRY_PERSISTENCE_CLEANUP_START);
        logger.info("Persistence hash map cleanup will start in " + Constants.KALTURA_ENTRY_PERSISTENCE_CLEANUP_START / 1000 + " seconds");
	}

	public static Object getProperty(String streamName, String subKey) throws Exception {

		String entryIdKey = Utils.getEntryIdFromStreamName(streamName);
		Object value = getProperty(streamName, entryIdKey, subKey);

		return value;
	}

	public static Object getProperty(String streamName, String entryIdKey, String subKey) throws Exception {

		Object value;

		try {
			ConcurrentHashMap<String, Object> entryMap = entryIdToKalturaLiveEntryMap.get(entryIdKey);
			value = entryMap.get(subKey);
		} catch (Exception e) {
			logger.error("(" + streamName + ") Failed to get value for key \"" + subKey + "\". " + e);
			throw e;
		}

		logger.debug("(" + streamName + ") (" + entryIdKey + ") Successfully got entry (map size:" + entryIdToKalturaLiveEntryMap.size() + ")");

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

		logger.debug("(" + entryId + ") Successfully updated entry. Sub key \"" + subKey + "\" (map size:" + entryIdToKalturaLiveEntryMap.size() + ")");
	}
}
