package com.kaltura.media_server.modules;

import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.media_server.services.Constants;
import com.kaltura.media_server.services.Utils;

import java.util.Objects;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.*;
import com.wowza.wms.stream.*;
import com.wowza.wms.client.IClient;
import com.wowza.wms.application.WMSProperties;


/**
 * Created by lilach.maliniak on 25/01/2017.
 */
public class DynamicStreamSettings {

	private static final int SEGMENT_RURATION_INDEX = 0;
	private static final Logger logger = Logger.getLogger(DynamicStreamSettings.class);
	private ConcurrentHashMap<String, int[]> mapLiveEntryToProperties = null;

	public DynamicStreamSettings() {
		mapLiveEntryToProperties = new ConcurrentHashMap<String, int[]>();
	}


	private boolean isValidSegmentDuration(int segmentDuration) {

		if (segmentDuration < Constants.MIN_ALLOWED_CHUNK_DURATION_MILLISECONDS || segmentDuration > Constants.MAX_ALLOwED_CHUNK_DURATION_MILLISECONDS) {
			throw new IllegalArgumentException("[segmentDuration=" + segmentDuration + "], value is out of range [" + Constants.MIN_ALLOWED_CHUNK_DURATION_MILLISECONDS + ", " + Constants.MAX_ALLOwED_CHUNK_DURATION_MILLISECONDS+ "]");
		}

		return true;
	}

	// todo: consult whether to always replace value or add only if doesn't exist yet
	private void addLiveEntryDynamicSettings(String streamName, int[] dynamicSettings) {

		String entryId = Utils.getEntryIdFromStreamName(streamName);

		synchronized (mapLiveEntryToProperties) {
			if (!mapLiveEntryToProperties.containsKey(entryId)) {
				mapLiveEntryToProperties.put(entryId, dynamicSettings);
			/*} else {
				// todo: check if this avoids race conditions???
				mapLiveEntryToProperties.remove(streamName);
				mapLiveEntryToProperties.put(entryId, entryProperties);
			}*/
			}
		}
	}

	private void removeLiveEntryDynamicSettings(String streamName) {

		String entryId = Utils.getEntryIdFromStreamName(streamName);

		synchronized (mapLiveEntryToProperties) {
			if (!mapLiveEntryToProperties.containsKey(entryId)) {
				mapLiveEntryToProperties.remove(entryId);
			}
		}
	}

	private int[] getLiveEntryDynamicSettings(String streamName) {

		int[] dynamicSettings = null;

		synchronized (mapLiveEntryToProperties) {
			String entryId = Utils.getEntryIdFromStreamName(streamName);
			dynamicSettings = mapLiveEntryToProperties.get(entryId);

			if (dynamicSettings == null) {
				throw new NullPointerException("(" + streamName + ") failed to get dynamic settings for live entry");
			}
		}

		return dynamicSettings;
	}

	private void getDynamicSettingsFromBEClientProperties(IMediaStream stream, String streamName) {

		int segmentDuration = Constants.DEFAULT_CHUNK_DURATION_MILLISECONDS;

		try {
			KalturaLiveEntry liveEntry = Utils.getLiveEntryFromStream(stream);
			segmentDuration = liveEntry.segmentDuration;

			if(!isValidSegmentDuration(segmentDuration)) {
				throw new IllegalArgumentException("stream [" + streamName + "] \"segmentDuratoin\"=" + segmentDuration + "is out of allowed range [" +Constants.MIN_ALLOWED_CHUNK_DURATION_MILLISECONDS + ", " +Constants.MAX_ALLOwED_CHUNK_DURATION_MILLISECONDS+ "]. ");
			} else {
				logger.info("stream [" + streamName + "] successfully retrieved live entry dynamic settings: \"segmentDuratoin\"=" + segmentDuration);
			}

		} catch( Exception e) {
			segmentDuration = Constants.DEFAULT_CHUNK_DURATION_MILLISECONDS;
			if (e instanceof IllegalArgumentException) {
				logger.error(e);
			}else {
				logger.error("stream [" + streamName + "] failed to get dynamic live entry settings. " + e);
			}
		}

		addLiveEntryDynamicSettings(streamName, new int[] {segmentDuration});

	}

	// todo: consult if synchronized is needed ... as in the example we got from wowza support there isn't
	// //cupertinoPacketizer.getProperties().setProperty("cupertinoChunkDurationTarget", segmentDuration);
	private void updateTranscodeStreamPacketizerProperties(LiveStreamPacketizerCupertino cupertinoPacketizer, IMediaStream stream, String streamName) {

		int[] dynmicSettings = {Constants.DEFAULT_CHUNK_DURATION_MILLISECONDS};

		try {
			dynmicSettings = getLiveEntryDynamicSettings(streamName);
		} catch(Exception e) {
			logger.error(e);
		}

		// update packetizer properties
		WMSProperties properties = cupertinoPacketizer.getProperties();

		synchronized (properties) {
			properties.setProperty("cupertinoChunkDurationTarget", dynmicSettings[SEGMENT_RURATION_INDEX]);
		}
	}

	public void addLiveEntrySettings(LiveStreamPacketizerCupertino cupertinoPacketizer, IMediaStream stream, String streamName) {

		if (stream.getClientId() >= 0) { // ingest stream (input)
			getDynamicSettingsFromBEClientProperties(stream, streamName);
			return;
		}
		// transcoded stream (output)
		updateTranscodeStreamPacketizerProperties(cupertinoPacketizer, stream, streamName);
	}

	public void removeLiveEntrySettings(String streamName) {
		removeLiveEntryDynamicSettings(streamName);
	}

}
