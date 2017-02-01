package com.kaltura.media_server.services;

import org.apache.log4j.Logger;

import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by lilach.maliniak on 29/01/2017.
 */
public class KalturaEntryDataPersistence {

	private static Logger logger = Logger.getLogger(KalturaEntryDataPersistence.class);
	private static WeakHashMap<KalturaEntryIdKey, ConcurrentHashMap<String, Object>> entryIdToKalturaLiveEntryMap = new WeakHashMap<>();

	public static Object getProperty(String streamName, String subKey, Object defaultValue) {

		KalturaEntryIdKey entryIdKey = null;
		try {
			entryIdKey = Utils.getEntryIdKeyFromStreamName(streamName);
			return getProperty(streamName, entryIdKey, subKey);
		} catch (Exception e) {
			logger.error(e + " returning the default value for sub key " + subKey);
		}

		return defaultValue;
	}

	public static Object getProperty(String streamName, String subKey) throws Exception {

		KalturaEntryIdKey entryIdKey = Utils.getEntryIdKeyFromStreamName(streamName);

		Object value = getProperty(streamName, entryIdKey, subKey);

		return value;
	}

	private static Object getProperty(String streamName, KalturaEntryIdKey entryIdKey, String subKey) throws Exception {

		Object value = null;

		synchronized (entryIdToKalturaLiveEntryMap) {
			try {
				ConcurrentHashMap<String, Object> entryMap = entryIdToKalturaLiveEntryMap.get(entryIdKey);
				value = entryMap.get(subKey);
			} catch (Exception e) {
				logger.error("(" + streamName + ") failed to get value for key " + subKey + ". " + e);
				throw e;
			}
		}

		logger.debug("(" + streamName + ") (" + entryIdKey.getEntryId() + ") successfully got entry");

		return value;
	}

	// note: call add entry on connect (after successful authentication)
	public static KalturaEntryIdKey setProperty(String entryId, String subKey, Object value) throws Exception {

		KalturaEntryIdKey entryIdKey = new KalturaEntryIdKey(entryId);

		synchronized (entryIdToKalturaLiveEntryMap) {
			if (!entryIdToKalturaLiveEntryMap.containsKey(entryIdKey)) {
				entryIdToKalturaLiveEntryMap.put(entryIdKey, new ConcurrentHashMap<String, Object>());
			}
			setProperty(entryId, entryIdKey, subKey, value);
		}

		logger.debug("(" + entryId + ") successfully added entry");

		return entryIdKey;
	}

	private static void setProperty(String entryId, KalturaEntryIdKey entryIdKey, String subKey, Object value) throws Exception {

		if (!entryIdToKalturaLiveEntryMap.containsKey(entryIdKey)) {
			entryIdToKalturaLiveEntryMap.put(entryIdKey, new ConcurrentHashMap<String, Object>());

			ConcurrentHashMap<String, Object> entryMap = entryIdToKalturaLiveEntryMap.get(entryIdKey);
			entryMap.put(subKey, value);
		}

		logger.debug("(" + entryId + ") successfully updated entry " + entryIdKey.getEntryId() + " sub key " + subKey + ".");
	}

}
