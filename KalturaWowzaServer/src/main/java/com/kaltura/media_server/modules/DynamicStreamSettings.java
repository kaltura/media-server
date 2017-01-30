package com.kaltura.media_server.modules;

import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.media_server.services.Constants;
import com.kaltura.media_server.services.Utils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.*;
import com.wowza.wms.stream.*;
import com.wowza.wms.client.IClient;
import com.wowza.wms.application.WMSProperties;


/**
 * Created by lilach.maliniak on 25/01/2017.
 */
public class DynamicStreamSettings {

	private static final Logger logger = Logger.getLogger(DynamicStreamSettings.class);

	private boolean isValidSegmentDuration(int segmentDuration, String streamName) {

		if (segmentDuration < Constants.MIN_ALLOWED_CHUNK_DURATION_MILLISECONDS || segmentDuration > Constants.MAX_ALLOwED_CHUNK_DURATION_MILLISECONDS) {
			logger.error("(" +streamName + ")[segmentDuration=" + segmentDuration + "], value is out of range [" + Constants.MIN_ALLOWED_CHUNK_DURATION_MILLISECONDS + ", " + Constants.MAX_ALLOwED_CHUNK_DURATION_MILLISECONDS + "]");
			return false;
		}

		return true;
	}

	private KalturaLiveEntry updateStreamProperties(LiveStreamPacketizerCupertino cupertinoPacketizer, String streamName, KalturaLiveEntry entry) {

		int segmentDuration = Constants.DEFAULT_CHUNK_DURATION_MILLISECONDS;

		try {
		if (isValidSegmentDuration(entry.segmentDuration, streamName)) {
			segmentDuration = entry.segmentDuration;
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

		return entry;
	}

	public void onStreamCreate(LiveStreamPacketizerCupertino cupertinoPacketizer, IMediaStream stream, String streamName, KalturaLiveEntry entry) {

		if (stream.getClientId() >= 0) { // ingest stream (input)
			return;
		}
		// transcoded stream (output)
		updateStreamProperties(cupertinoPacketizer, streamName, entry);
	}

}
