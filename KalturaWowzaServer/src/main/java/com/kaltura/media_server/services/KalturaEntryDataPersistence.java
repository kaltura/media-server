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

	public static Object getProperty(String subKey, String streamName, Object defaultValue) {

		KalturaEntryIdKey entryIdKey = null;
		try {
			entryIdKey = Utils.getEntryIdKeyFromStreamName(streamName);
			return getProperty(entryIdKey, subKey, streamName);
		} catch (Exception e) {
			logger.error(e + " returning the default value for sub key " + subKey);
		}

		return defaultValue;
	}

	public static Object getProperty(String subKey, String streamName) throws Exception {

		KalturaEntryIdKey entryIdKey = Utils.getEntryIdKeyFromStreamName(streamName);

		Object value = getProperty(entryIdKey, subKey, streamName);

		return value;
	}

	private static Object getProperty(KalturaEntryIdKey entryIdKey, String subKey, String streamName) throws Exception {

		Object value = null;
		try {
			synchronized (entryIdToKalturaLiveEntryMap) {
				ConcurrentHashMap<String, Object> entryMap = entryIdToKalturaLiveEntryMap.get(entryIdKey);
				value = entryMap.get(subKey);
			}
		} catch (Exception e) {
			logger.error("(" + streamName + ") failed to get value for key " + subKey + ". " + e);
			throw e;
		}

		logger.debug("(" + streamName + ") ("+ entryIdKey.getEntryId() +") successfully got entry");

		return value;
	}

	// note: call add entry on connect (after successful authentication)
	public static Object setProperty(String subKey, Object value, String streamName) throws Exception {

		KalturaEntryIdKey entryIdKey = Utils.getEntryIdKeyFromStreamName(streamName);

		synchronized (entryIdToKalturaLiveEntryMap) {
			if (!entryIdToKalturaLiveEntryMap.containsKey(entryIdKey)) {
				entryIdToKalturaLiveEntryMap.put(entryIdKey, new ConcurrentHashMap<String, Object>());
			}
		}

		setProperty(entryIdKey, subKey, value, streamName);

		logger.debug("(" + streamName + ") successfully added entry");

		return value;
	}

	private static void setProperty(KalturaEntryIdKey entryIdKey, String subKey, Object value, String streamName) throws Exception {

		synchronized (entryIdToKalturaLiveEntryMap) {
			if (!entryIdToKalturaLiveEntryMap.containsKey(entryIdKey)) {
				entryIdToKalturaLiveEntryMap.put(entryIdKey, new ConcurrentHashMap<String, Object>());
			}

			ConcurrentHashMap<String, Object> entryMap = entryIdToKalturaLiveEntryMap.get(entryIdKey);
			entryMap.put(subKey, value);
		}

		logger.debug("(" + streamName + ") successfully updated entry " + entryIdKey.getEntryId() + " sub key " + subKey + ".");
	}

}
