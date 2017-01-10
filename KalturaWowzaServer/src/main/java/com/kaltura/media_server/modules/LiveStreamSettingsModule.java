package com.kaltura.media_server.modules;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.media_server.services.Utils;
import com.wowza.wms.amf.*;
import com.wowza.wms.application.*;
import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.*;
import com.wowza.wms.media.mp3.model.idtags.*;
import com.wowza.wms.stream.livepacketizer.*;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.module.*;
import com.wowza.wms.stream.*;
import com.wowza.wms.client.IClient;
import com.wowza.wms.application.WMSProperties;
import com.wowza.wms.stream.MediaStreamActionNotifyBase;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.text.SimpleDateFormat;
import java.lang.Thread;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

// todo: test and add relevant code ofr ptsTimeCode moving back in time and PTS wrap arround are supported.

public class LiveStreamSettingsModule extends ModuleBase {

	private static final String OBJECT_TYPE_KEY = "objectType";
	private static final String OBJECT_TYPE_SYNCPOINT = "KalturaSyncPoint";
	private static final String HEADER_TAG_TIMECODE = "EXT-KALTURA-SYNC-POINT";
	private static final String TIMESTAMP_KEY = "timestamp";
	private static final String ID_KEY = "id";
	private static final Logger logger = Logger.getLogger(LiveStreamSettingsModule.class);
	private static final String MAX_ALLOWED_PTS_DRIFT_MILLISEC = "KalturaMaxAllowedPTSDriftiMillisec";
	private static final int DEFAULT_MAX_ALLOWED_PTS_DRIFT_MILLISEC = 1000;
	private static final String STREAM_ACTION_LISTENER_PROPERTY = "KalturaSyncPTS";

	private LiveStreamPacketizerListener liveStreamPacketizerListener;
	private int maxAllowedPTSDriftMillisec;
	private ConcurrentHashMap<String, Long> mapLiveEntryToBaseSystemTime = null;
	private ConcurrentHashMap<String, Long> mapLiveEntryStreamsCount = null;

	public LiveStreamSettingsModule() {
		logger.debug("Creating a new instance of Modules.CuePointsManager");
		mapLiveEntryToBaseSystemTime = new ConcurrentHashMap<>();
		mapLiveEntryStreamsCount = new ConcurrentHashMap<>();
		//packetListeners = new ConcurrentHashMap<>();
	}


	class LiveStreamPacketizerDataHandler implements IHTTPStreamerCupertinoLivePacketizerDataHandler2 {

		private LiveStreamPacketizerCupertino liveStreamPacketizer = null;
		private String streamName = null;

		public LiveStreamPacketizerDataHandler(LiveStreamPacketizerCupertino liveStreamPacketizer, String streamName) {
			logger.debug("creating LiveStreamPacketizerDataHandler for stream name: " + streamName);
			this.streamName = streamName;
			this.liveStreamPacketizer = liveStreamPacketizer;
		}

		public void onFillChunkStart(LiveStreamPacketizerCupertinoChunk chunk) {
			double packetStartTime = (double) chunk.getStartTimecode();
			String startTimeStr = Double.toString(packetStartTime);
			String id = this.streamName + "_" + chunk.getChunkIndexForPlaylist();

			logger.info("adding ID3 frame (timestamp=" + startTimeStr + ") to chunk [" + chunk.getRendition().toString() + ":" + this.liveStreamPacketizer.getContextStr() + "]: chunkId:" + chunk.getChunkIndexForPlaylist());

			// Add custom M3U tag to chunklist header
			CupertinoUserManifestHeaders userManifestHeaders = this.liveStreamPacketizer.getUserManifestHeaders(chunk.getRendition());
			if (userManifestHeaders != null) {
				userManifestHeaders.addHeader(HEADER_TAG_TIMECODE, TIMESTAMP_KEY, packetStartTime);
			}

			try {

				Map<String, Object> map = new HashMap<>();
				map.put(OBJECT_TYPE_KEY, OBJECT_TYPE_SYNCPOINT);
				map.put(TIMESTAMP_KEY, packetStartTime);
				map.put(ID_KEY, id);
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(map);
				// Add ID3 tag to start of chunk
				ID3Frames id3Frames = this.liveStreamPacketizer.getID3FramesHeader();
				ID3V2FrameTextInformation id3Tag = new ID3V2FrameTextInformation(ID3V2FrameBase.TAG_TEXT);
				logger.info("adding new id3Frame [" + this.streamName + "] " + json);
				id3Tag.setValue(json);
				// it is essential to call clear. This method is used to indicate ID3 is set in specified chunk
				// if clear() is not called multiple ID3 tag frame will be inserted to each chunk.
				id3Frames.clear();
				id3Frames.putFrame(id3Tag);

			} catch (JsonProcessingException e) {
				logger.error("stream [" + this.streamName + "] failed to add sync points data to ID3Tag " + e.toString());
			}

		}

		public void onFillChunkEnd(LiveStreamPacketizerCupertinoChunk chunk, long timecode) {
			logger.info("[" + chunk.getRendition().toString() + ":" + liveStreamPacketizer.getContextStr() + "]: chunkId:" + chunk.getChunkIndexForPlaylist());
		}

		public void onFillChunkMediaPacket(LiveStreamPacketizerCupertinoChunk chunk, CupertinoPacketHolder holder, AMFPacket packet) {
			logger.info("[" + chunk.getRendition().toString() + ":" + liveStreamPacketizer.getContextStr() + "]: chunkId:" + chunk.getChunkIndexForPlaylist());
		}

		public void onFillChunkDataPacket(LiveStreamPacketizerCupertinoChunk chunk, CupertinoPacketHolder holder, AMFPacket packet, ID3Frames id3Frames) {
			logger.info("[" + chunk.getRendition().toString() + ":" + liveStreamPacketizer.getContextStr() + "]: chunkId:" + chunk.getChunkIndexForPlaylist());
		}

	}

	class LiveStreamPacketizerListener implements ILiveStreamPacketizerActionNotify {

		private IApplicationInstance appInstance = null;

		public LiveStreamPacketizerListener(IApplicationInstance appInstance) {
			logger.debug("creating new LiveStreamPacketizerListener");
			this.appInstance = appInstance;
		}

		public void onLiveStreamPacketizerCreate(ILiveStreamPacketizer liveStreamPacketizer, String streamName) {
			logger.debug("onLiveStreamPacketizerCreate. stream: " + streamName);
			if (!(liveStreamPacketizer instanceof LiveStreamPacketizerCupertino))
				return;

			LiveStreamPacketizerCupertino cupertinoPacketizer = (LiveStreamPacketizerCupertino) liveStreamPacketizer;
			IMediaStream stream = this.appInstance.getStreams().getStream(streamName);

			logger.info("Create [" + streamName + "]: " + liveStreamPacketizer.getClass().getSimpleName());
			cupertinoPacketizer.setDataHandler(new LiveStreamPacketizerDataHandler(cupertinoPacketizer, streamName));
			incrementStreams(streamName);
		}

		public void onLiveStreamPacketizerDestroy(ILiveStreamPacketizer liveStreamPacketizer) {
			String streamName = liveStreamPacketizer.getAndSetStartStream(null).getName();
			logger.debug("(" + streamName + ") onLiveStreamPacketizerDestroy");
			decrementStreams(streamName);
		}

		public void onLiveStreamPacketizerInit(ILiveStreamPacketizer liveStreamPacketizer, String streamName) {
			logger.debug("(" + streamName + ") onLiveStreamPacketizerInit");
		}
	}

	public void onAppStart(IApplicationInstance applicationInstance) {
		this.liveStreamPacketizerListener = new LiveStreamPacketizerListener(applicationInstance);
		applicationInstance.addLiveStreamPacketizerListener(this.liveStreamPacketizerListener);
		this.maxAllowedPTSDriftMillisec = applicationInstance.getProperties().getPropertyInt(MAX_ALLOWED_PTS_DRIFT_MILLISEC, DEFAULT_MAX_ALLOWED_PTS_DRIFT_MILLISEC);
		logger.info("Modules.LiveStreamSettingsModule application instance started");
	}

	public void onStreamCreate(IMediaStream stream) {
		PacketListener listener = new PacketListener();
		WMSProperties props = stream.getProperties();
		synchronized (props)
		{
			props.setProperty(STREAM_ACTION_LISTENER_PROPERTY, listener);
		}

		stream.addLivePacketListener(listener);
	}

	public void onStreamDestroy(IMediaStream stream) {
		PacketListener listener = null;
		WMSProperties props = stream.getProperties();
		synchronized (props) {
			listener = (PacketListener) props.get(STREAM_ACTION_LISTENER_PROPERTY);
		}
		if (listener != null) {
			stream.removeLivePacketListener(listener);
			logger.info("remove PacketListener: " + stream.getSrc());
		}

	}

	class PacketListener implements IMediaStreamLivePacketNotify {

		private long lastPacketTimeCode;
		private long baseSystemTime;
		private long basePacketTimeCode;
		private String entryId = null;

		public PacketListener() {
			this.lastPacketTimeCode = 0;
			this.basePacketTimeCode = 0;
			this.baseSystemTime = 0;
		}

		public void onLivePacket(IMediaStream stream, AMFPacket thisPacket) {

			long currentTime = System.currentTimeMillis();
			long currentPacketTimeCode = thisPacket.getAbsTimecode();
			long currentPTS = thisPacket.getTimecode();
			boolean isAudio = thisPacket.isAudio();
			boolean isVideo = thisPacket.isVideo();
			String streamName = stream.getName();

			if (this.entryId == null) {
				this.entryId = extractEntryId(streamName);
			}

			// get global entry baseSystemTime
			long liveEntryBaseTimestamp = getLiveEntryBaseSystemTime(this.entryId, currentTime);

			logger.debug("Stream name: " + streamName + " currentTime: " + currentTime + " current TimeCode: " + currentPacketTimeCode + " currentPTS " + currentPTS + " Audio " + Boolean.toString(isAudio) + " Video " + Boolean.toString(isVideo));

			if (liveEntryBaseTimestamp != this.baseSystemTime ||
					this.baseSystemTime == 0 || this.lastPacketTimeCode == 0
					|| Math.abs(currentPacketTimeCode - this.lastPacketTimeCode) > maxAllowedPTSDriftMillisec) {

				if (this.baseSystemTime == liveEntryBaseTimestamp && currentTime != this.baseSystemTime) {

					// update globe entry base timestamp
					liveEntryBaseTimestamp = setLiveEntryBaseSystemTime(this.entryId, currentTime);
				}
				this.basePacketTimeCode = currentPacketTimeCode;
				this.baseSystemTime = liveEntryBaseTimestamp;
			}
			this.lastPacketTimeCode = currentPacketTimeCode;

			long packetTimeCode = currentPacketTimeCode - this.basePacketTimeCode + this.baseSystemTime;
			// Todo: handle time code that went back in time

			thisPacket.setAbsTimecode(packetTimeCode);
		}

	}

	public long getLiveEntryBaseSystemTime(String entryId, long baseSystemTime) {
		try {
			if (!this.mapLiveEntryToBaseSystemTime.containsKey(entryId)) {
				this.mapLiveEntryToBaseSystemTime.put(entryId, baseSystemTime);
			} else {
				baseSystemTime = this.mapLiveEntryToBaseSystemTime.get(entryId);
			}
		} catch (Exception e) {
			if (Boolean.valueOf(entryId)) {
				logger.error("(" + entryId + ") fail to get base timestamp for live entry." + e.toString());
			} else {
				logger.error("( UNDEFIMED ENTRY ID ) could not get base timestamp, entryId is invalid. " + e.toString());
			}
		}

		return baseSystemTime;
	}

	public long setLiveEntryBaseSystemTime(String entryId, long baseSystemTime) {
		this.mapLiveEntryToBaseSystemTime.put(entryId, baseSystemTime);
		return baseSystemTime;
	}

	public void incrementStreams(String streamName) {
		String entryId = extractEntryId(streamName);
		if (mapLiveEntryStreamsCount.containsKey(entryId)) {
			mapLiveEntryStreamsCount.put(streamName, mapLiveEntryStreamsCount.get(entryId) + 1);
		} else {
			mapLiveEntryStreamsCount.put(streamName, new Long(1));
		}
	}

	public void decrementStreams(String streamName) {
		String entryId = extractEntryId(streamName);
		if (mapLiveEntryStreamsCount.containsKey(entryId)) {
			if (mapLiveEntryStreamsCount.put(streamName, mapLiveEntryStreamsCount.get(entryId) - 1) <= 1) {
				mapLiveEntryToBaseSystemTime.remove(entryId);
			}
		} else {
			logger.warn("didn't find " + entryId + " in map");
		}
	}

	public String extractEntryId(String streamName) {
		if (streamName == null) {
			logger.error("NULL stream name. Failed to extract entryId");
			return null;
		}

		String entryId = streamName;
		Pattern entryIdPattern = Pattern.compile("(^[0-1]_[a-zA-Z0-9]+)");
		Matcher m = entryIdPattern.matcher(streamName);

		if (m.find()) {
			streamName = m.group(1);
			logger.debug("stream: " + streamName + " entryId: " + entryId);
		} else {
			logger.error("failed to extract entryId from " + streamName);
		}

		return entryId;
	}

}
