package com.kaltura.media_server.modules;

import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.media_server.services.Constants;
import com.kaltura.media_server.services.Utils;

import java.util.Objects;
import java.util.Iterator;

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
	public static final int MAX_ALLOWED_CHUNK_DURATION_MILLISECONDS = 20000;
	public static final int MIN_ALLOWED_CHUNK_DURATION_MILLISECONDS = 1000;
	
	private boolean isValidSegmentDuration(int segmentDuration) {

		if (segmentDuration < MIN_ALLOWED_CHUNK_DURATION_MILLISECONDS || segmentDuration > MAX_ALLOWED_CHUNK_DURATION_MILLISECONDS) {
			logger.error("[segmentDuration=" + segmentDuration + "], value is out of range [" + MIN_ALLOWED_CHUNK_DURATION_MILLISECONDS + ", " + MAX_ALLOWED_CHUNK_DURATION_MILLISECONDS + "]");
			return false;
		}

		return true;
	}

	private void setSegmentDuration(LiveStreamPacketizerCupertino cupertinoPacketizer, IMediaStream stream) {
		String streamName = stream.getName();
		int segmentDuration = Constants.DEFAULT_CHUNK_DURATION_MILLISECONDS;

		try {
            KalturaLiveEntry liveEntry = Utils.getLiveEntryFromStream(stream);
			if (isValidSegmentDuration(liveEntry.segmentDuration)) {
				segmentDuration = liveEntry.segmentDuration;
				logger.debug("(" + streamName + ") successfully set \"cupertinoChunkDurationTarget\" to " + segmentDuration + " milliseconds");
			} else {
				logger.error("(" + streamName + ") failed to get \"segmentDuration\". Using default value, " + Constants.DEFAULT_CHUNK_DURATION_MILLISECONDS + " milliseconds. Call developer.");
			}
		} catch (Exception e) {
				logger.error("stream [" + streamName + "] failed to get segmentDuration. Using default value, \" + Constants.DEFAULT_CHUNK_DURATION_MILLISECONDS + \" milliseconds. Call developer." + e);
		}

		cupertinoPacketizer.getProperties().setProperty("cupertinoChunkDurationTarget", segmentDuration);
	}

	public void checkAndUpdateSettings(LiveStreamPacketizerCupertino cupertinoPacketizer, IMediaStream stream) {

		if (stream.getClientId() >= 0) {
			return;
		}
		setSegmentDuration(cupertinoPacketizer, stream);
	}

}
