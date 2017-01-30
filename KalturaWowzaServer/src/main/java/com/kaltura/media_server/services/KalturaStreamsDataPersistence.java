package com.kaltura.media_server.services;

import com.kaltura.client.types.KalturaLiveEntry;
import com.wowza.wms.application.WMSProperties;
import com.wowza.wms.client.IClient;
import com.wowza.wms.stream.*;
import org.apache.log4j.Logger;

import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;

import org.apache.commons.lang3.tuple.*;


/**
 * Created by lilach.maliniak on 29/01/2017.
 */
public class KalturaStreamsDataPersistence {

	private static Logger logger = Logger.getLogger(KalturaStreamsDataPersistence.class);
	private static WeakHashMap<KalturaEntryIdKey, ConcurrentHashMap<String, Object>> entryIdToKalturaLiveEntryMap = new WeakHashMap<>();


	public static Pair<Object, KalturaEntryIdKey> getEntry(IClient client, String subKey) throws Exception{

		WMSProperties properties = client.getProperties();
		KalturaEntryIdKey entryIdKey = null;
		List<IMediaStream> names = client.getPlayStreams();
		String streamName = names.size() > 0 ? names.get(0).getName() : "client with no stream";

		Object value = getEntry(subKey, streamName);

		return Pair.of(value, entryIdKey);

	}

	public static Pair<Object, KalturaEntryIdKey> getEntry(String subKey, String streamName, Object defaultValue) {

		try {
			return getEntry(subKey, streamName, defaultValue);
		} catch (Exception e) {
			logger.error(e + " returning the default value for sub key " + subKey);
			String entryId = Utils.getEntryIdFromStreamName(streamName);
			KalturaEntryIdKey entryIdKey = new KalturaEntryIdKey(entryId);

			return Pair.of(defaultValue, entryIdKey);
		}
	}

	public static Pair<Object, KalturaEntryIdKey> getEntry(String subKey, String streamName) throws Exception {

		String entryId = Utils.getEntryIdFromStreamName(streamName);
		KalturaEntryIdKey entryIdKey = new KalturaEntryIdKey(entryId);

		Object value = getEntry(entryIdKey, subKey, streamName);
		Pair<Object, KalturaEntryIdKey> result = Pair.of(value, entryIdKey);
		return result;
	}

	public static Object getEntry(KalturaEntryIdKey entryIdKey, String subKey, String uniqueName) throws Exception {

		Object value = null;

		synchronized (entryIdToKalturaLiveEntryMap) {
			ConcurrentHashMap<String, Object> entryMap = entryIdToKalturaLiveEntryMap.get(entryIdKey);

			if (entryMap == null) {
				throw new Exception("(" + uniqueName + ") ("+ entryIdKey.getEntryId() +") failed to get value for " + subKey + ". The key (" + entryIdKey.getEntryId() + ") doesn't exist in map.");
			} else if (entryMap.get(subKey) == null) {
				throw new Exception("(" + uniqueName + ") ("+ entryIdKey.getEntryId() +") failed to get value for " + subKey + ". The sub key (" + subKey + ") doesn't exist in map.");
			}

			value = entryMap.get(subKey);
		}

		logger.debug("(" + uniqueName + ") ("+ entryIdKey.getEntryId() +") successfully got entry");

		return value;
	}

	// add entry on connect (after successful authentication)
	public static Pair<Object, KalturaEntryIdKey> addEntry(String entryId, String subKey, Object value, String partnerId) throws Exception {

		KalturaEntryIdKey entryIdKey = new KalturaEntryIdKey(entryId);

		synchronized (entryIdToKalturaLiveEntryMap) {
			if (!entryIdToKalturaLiveEntryMap.containsKey(entryIdKey)) {
				entryIdToKalturaLiveEntryMap.put(entryIdKey, new ConcurrentHashMap<String, Object>());
			}
		}

		update(entryIdKey, subKey, value, partnerId);

		logger.debug("(" + entryId + ") successfully added entry");

		return Pair.of(value, entryIdKey);
	}

	// note: stream name can be any unique name related to specific live entry
	public static void update(KalturaEntryIdKey entryIdKey, String subKey, Object value, String streamName) throws Exception {

		synchronized (entryIdToKalturaLiveEntryMap) {
			if (!entryIdToKalturaLiveEntryMap.containsKey(entryIdKey)) {
				entryIdToKalturaLiveEntryMap.put(entryIdKey, new ConcurrentHashMap<String, Object>());
			}

			ConcurrentHashMap<String, Object> entryMap = entryIdToKalturaLiveEntryMap.get(entryIdKey);
			entryMap.put(subKey, value);
		}

		logger.debug("(" + streamName + ") successfully updated entry " + entryIdKey.getEntryId() + " sub key " + subKey + ".");
	}

	public static ConcurrentHashMap<String, Object> getAll(String entryId) {

		KalturaEntryIdKey entryIdKey = new KalturaEntryIdKey(entryId);

		synchronized (entryIdToKalturaLiveEntryMap) {
			if (entryIdToKalturaLiveEntryMap.containsKey(entryIdKey)) {
				return entryIdToKalturaLiveEntryMap.get(entryIdKey);
			}
	}

		logger.error("failed to get persistent data for entry id " + entryId );

		return null;
	}

}
