package com.kaltura.media_server.services;

import org.apache.log4j.Logger;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lilach.maliniak on 29/01/2017.
 */
public class KalturaEntryDataPersistence {

	private static Logger logger = Logger.getLogger(KalturaEntryDataPersistence.class);
	public static ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> entryIdToKalturaLiveEntryMap = new ConcurrentHashMap<>();

	public static Object getProperty(String streamName, String subKey) throws Exception {

		String entryIdKey = Utils.getEntryIdFromStreamName(streamName);
		Object value = getProperty(streamName, entryIdKey, subKey);

		return value;
	}

	public static Object getProperty(String streamName, String entryIdKey, String subKey) throws Exception {

		Object value;

		synchronized (entryIdToKalturaLiveEntryMap) {
			try {
				ConcurrentHashMap<String, Object> entryMap = entryIdToKalturaLiveEntryMap.get(entryIdKey);
				value = entryMap.get(subKey);
			} catch (Exception e) {
				logger.error("(" + streamName + ") failed to get value for key \"" + subKey + "\". " + e);
				throw e;
			}
		}

		logger.debug("(" + streamName + ") (" + entryIdKey + ") successfully got entry (map size:" + entryIdToKalturaLiveEntryMap.size() + ")");

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

		logger.debug("(" + entryId + ") successfully updated entry. Sub key \"" + subKey + "\" (map size:" + entryIdToKalturaLiveEntryMap.size() + ")");
	}
}
