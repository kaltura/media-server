package com.kaltura.media_server.modules;

import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.media_server.services.Constants;
import com.kaltura.media_server.services.Utils;

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

	public DynamicStreamSettings() {
	}

	private void setSegmentDuration(LiveStreamPacketizerCupertino cupertinoPacketizer, IMediaStream stream, String streamName) {
		int segmentDuration = Constants.DEFAULT_CHUNK_DURATION_MILLISECONDS;
		boolean durationUpdated = false;
		MediaStreamMap streamsObj = stream.getStreams();
		//java.util.List<IMediaStream> streams = streamsObj.getStreams();
		java.util.List streamNames = streamsObj.getPublishStreamNames();
		//Iterator<IMediaStream> it = streams.iterator();
		Iterator<String> it = streamNames.iterator();

		while (!durationUpdated && it.hasNext()) {
			IMediaStream nextStream = streamsObj.getStream(it.next());
			try {
				KalturaLiveEntry liveEntry = Utils.getLiveEntryFromStream(nextStream);
				segmentDuration = liveEntry.segmentDuration;
				durationUpdated = true;
			} catch (Exception e) {
				if (!(e instanceof NullPointerException)) {
					logger.error("stream [" + nextStream.getName() + "] failed to get segmentDuration value from KalturaLiveEntry. " + e);
					//break;
				}
			}
		}

		cupertinoPacketizer.getProperties().setProperty("cupertinoChunkDurationTarget", segmentDuration);

		if (durationUpdated)
		{
			logger.debug("(" + streamName + ") successfully set \"cupertinoChunkDurationTarget\" to " + segmentDuration + " milliseconds");
		} else
		{
			logger.error("(" + streamName + ") failed to get \"segmentDuration\". Using default value, " + Constants.DEFAULT_CHUNK_DURATION_MILLISECONDS + " milliseconds. Call developer.");
		}

	}

	public void checkAndUpdateSettings(LiveStreamPacketizerCupertino cupertinoPacketizer, IMediaStream stream, String streamName) {

		if (stream.getClientId() >= 0) {
			return;
		}
		setSegmentDuration(cupertinoPacketizer, stream, streamName);
	}

}
