package com.kaltura.media_server.modules;

import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.media_server.services.Constants;
import com.kaltura.media_server.services.KalturaEntryDataPersistence;

import org.apache.log4j.Logger;

import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.*;
import com.wowza.wms.stream.*;
import com.wowza.wms.application.WMSProperties;


/**
 * Created by lilach.maliniak on 25/01/2017.
 */
public class DynamicStreamSettings {

	private static final Logger logger = Logger.getLogger(DynamicStreamSettings.class);
	public static final int MAX_ALLOWED_CHUNK_DURATION_MILLISECONDS = 20000;
	public static final int MIN_ALLOWED_CHUNK_DURATION_MILLISECONDS = 1000;

	private boolean isValidSegmentDuration(int segmentDuration, String streamName) {

		if (segmentDuration < MIN_ALLOWED_CHUNK_DURATION_MILLISECONDS || segmentDuration > MAX_ALLOWED_CHUNK_DURATION_MILLISECONDS) {
			logger.error("(" +streamName + ")[segmentDuration=" + segmentDuration + "], value is out of range [" + MIN_ALLOWED_CHUNK_DURATION_MILLISECONDS + ", " + MAX_ALLOWED_CHUNK_DURATION_MILLISECONDS + "]");
			return false;
		}

		return true;
	}

	private void setSegmentDuration(LiveStreamPacketizerCupertino cupertinoPacketizer, String streamName) {

		int segmentDuration = Constants.DEFAULT_CHUNK_DURATION_MILLISECONDS;

		KalturaLiveEntry entry = null;
		try {
			entry = (KalturaLiveEntry) KalturaEntryDataPersistence.getPropertyByStream(streamName, Constants.CLIENT_PROPERTY_KALTURA_LIVE_ENTRY);

			if (isValidSegmentDuration(entry.segmentDuration, streamName)) {
				segmentDuration = entry.segmentDuration;
				logger.debug("(" + streamName + ") successfully validated \"segmentDuration\" value: " + segmentDuration + " milliseconds");
			} else {
				logger.error("(" + streamName + ") failed to get or invalid \"segmentDuration\". Using default value, " + Constants.DEFAULT_CHUNK_DURATION_MILLISECONDS + " milliseconds");
			}
		} catch (Exception e) {
			logger.error("(" + streamName + ") failed to get entry's \"segmentDuration\", default value of " + segmentDuration + " milliseconds will be used. " + e);
		}

		// update packetizer properties
		WMSProperties properties = cupertinoPacketizer.getProperties();
		synchronized (properties)
		{
			properties.setProperty("cupertinoChunkDurationTarget", segmentDuration);
		}

		logger.debug("(" + streamName + ") successfully set \"cupertinoChunkDurationTarget\" to " + segmentDuration + " milliseconds");

	}

	public void checkAndUpdateSettings(LiveStreamPacketizerCupertino cupertinoPacketizer, IMediaStream stream) {

		if (stream.getClientId() >= 0) { // ingest stream (input)
			return;
		}
		// transcoded stream (output)
		setSegmentDuration(cupertinoPacketizer, stream.getName());
	}

}
