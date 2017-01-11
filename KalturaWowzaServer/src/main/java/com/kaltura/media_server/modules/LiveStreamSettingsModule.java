package com.kaltura.media_server.modules;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.media_server.services.Constants;
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
import com.wowza.wms.vhost.*;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.text.SimpleDateFormat;
import java.lang.Thread;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

// todo: test and add relevant code ofr ptsTimeCode moving back in time and PTS wrap arround are supported.

// todo: this code for PTS smoothing contains following assumption: ref_t + ref_pts_timecode - last_pts_timecode < new_ref_t

public class LiveStreamSettingsModule extends ModuleBase {

	private static final String OBJECT_TYPE_KEY = "objectType";
	private static final String OBJECT_TYPE_SYNCPOINT = "KalturaSyncPoint";
	private static final String HEADER_TAG_TIMECODE = "EXT-KALTURA-SYNC-POINT";
	private static final String TIMESTAMP_KEY = "timestamp";
	private static final String ID_KEY = "id";
	private static final Logger logger = Logger.getLogger(LiveStreamSettingsModule.class);
	private static final String MAX_ALLOWED_PTS_DRIFT_MILLISEC = "KalturaMaxAllowedPTSDriftiMillisec";
	private static final int DEFAULT_MAX_ALLOWED_PTS_DRIFT_MILLISEC = 10000;
	private static final String STREAM_ACTION_LISTENER_PROPERTY = "KalturaSyncPTS";
//	private static final int BASE_SYSTEM_TIME = 0;
//	private static final int BASE_PTS = 1;
//	private static final int LAST_IN_PTS = 2;

	private LiveStreamPacketizerListener liveStreamPacketizerListener;
	private int maxAllowedPTSDriftMillisec;
	private ConcurrentHashMap<String, long[]> mapLiveEntryToBaseSystemTime = null;

	public LiveStreamSettingsModule() {
		logger.debug("Creating a new instance of Modules.CuePointsManager");
		mapLiveEntryToBaseSystemTime = new ConcurrentHashMap<String, long[]>();
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
		}

		public void onLiveStreamPacketizerDestroy(ILiveStreamPacketizer liveStreamPacketizer) {
			String streamName = liveStreamPacketizer.getAndSetStartStream(null).getName();
			logger.debug("(" + streamName + ") onLiveStreamPacketizerDestroy");
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
		synchronized (props) {
			props.setProperty(STREAM_ACTION_LISTENER_PROPERTY, listener);
		}

		stream.addLivePacketListener(listener);
	}

	public void onStreamDestroy(IMediaStream stream) {
		PacketListener listener = null;
		String streamName = stream.getName();
		WMSProperties props = stream.getProperties();
		synchronized (props) {
			listener = (PacketListener) props.get(STREAM_ACTION_LISTENER_PROPERTY);
		}
		if (listener != null) {
			stream.removeLivePacketListener(listener);
			String entryId = Utils.getEntryIdFromStreamName(streamName);
			this.removeEntryBaseSystemTime(entryId);
			logger.info("remove PacketListener: " + stream.getSrc());
		}

	}

	class PacketListener implements IMediaStreamLivePacketNotify {

		private String entryId = null;

		public PacketListener() {
		}

		public void onLivePacket(IMediaStream stream, AMFPacket thisPacket) {

			if (stream.isTranscodeResult()) {
				return;
			}
			long baseSystemTime = 0;
			long baseInPTS = 0;
			long lastInPTS = 0;
			String streamName = stream.getName();
			if (this.entryId == null) {
				this.entryId = Utils.getEntryIdFromStreamName(streamName);
			}

			WMSProperties properties = stream.getProperties();

			// get data from input packet
			long currentTime = System.currentTimeMillis();
			long inPTS = thisPacket.getAbsTimecode();

			int type = thisPacket.getType();
			String streamType = null;
			if (thisPacket.isAudio()) {
				streamType = "audio";
				baseSystemTime = properties.getPropertyLong(Constants.AUDIO_BASE_SYSTEM_TIME, 0);
				baseInPTS = properties.getPropertyLong(Constants.AUDIO_BASE_PTS, 0);
				lastInPTS = properties.getPropertyLong(Constants.AUDIO_LAST_IN_PTS, 0);

			} else if (thisPacket.isVideo()) {
				streamType = "video";
				baseSystemTime = properties.getPropertyLong(Constants.VIDEO_BASE_SYSTEM_TIME, 0);
				baseInPTS = properties.getPropertyLong(Constants.VIDEO_BASE_PTS, 0);
				lastInPTS = properties.getPropertyLong(Constants.VIDEO_LAST_IN_PTS, 0);
			} else {
				streamType = "data";
				baseSystemTime = properties.getPropertyLong(Constants.DATA_BASE_SYSTEM_TIME, 0);
				baseInPTS = properties.getPropertyLong(Constants.DATA_BASE_PTS, 0);
				lastInPTS = properties.getPropertyLong(Constants.DATA_LAST_IN_PTS, 0);
			}

			// init local parameters used to calculate output PTS
			long inPTSDiff = lastInPTS - inPTS;
			long absPTSTimeCodeDiff = Math.abs(inPTSDiff);
			boolean firstPacket = (baseInPTS == 0) ? true : false;

			//=================================================================
			// handle first packet in stream or handle PTS jump
			//=================================================================
			if (baseSystemTime == 0 || absPTSTimeCodeDiff > maxAllowedPTSDriftMillisec) {

				long[] globalPTSData = setLiveEntryBaseSystemTime(this.entryId, currentTime, inPTS);

				baseSystemTime = globalPTSData[0];
				baseInPTS = globalPTSData[1];
				lastInPTS = baseInPTS;

				if (thisPacket.isAudio()) {
					properties.setProperty(Constants.AUDIO_BASE_SYSTEM_TIME, baseSystemTime);
					properties.setProperty(Constants.AUDIO_BASE_PTS, baseInPTS);
					properties.setProperty(Constants.AUDIO_LAST_IN_PTS, lastInPTS);

				} else if (thisPacket.isVideo()) {
					properties.setProperty(Constants.VIDEO_BASE_SYSTEM_TIME, baseSystemTime);
					properties.setProperty(Constants.VIDEO_BASE_PTS, baseInPTS);
					properties.setProperty(Constants.VIDEO_LAST_IN_PTS, lastInPTS);
				} else {
					properties.setProperty(Constants.DATA_BASE_SYSTEM_TIME, baseSystemTime);
					properties.setProperty(Constants.DATA_BASE_PTS, baseInPTS);
					properties.setProperty(Constants.DATA_LAST_IN_PTS, lastInPTS);
				}
			} else {
				if (thisPacket.isAudio()) {
					properties.setProperty(Constants.AUDIO_LAST_IN_PTS, lastInPTS);
				} else if (thisPacket.isVideo()) {
					properties.setProperty(Constants.VIDEO_LAST_IN_PTS, lastInPTS);
				} else {
					properties.setProperty(Constants.DATA_LAST_IN_PTS, lastInPTS);
				}
			}

			//=================================================================
			// calc & update output PTS value
			//=================================================================
			long correction = baseSystemTime - baseInPTS;
			long outPTS = inPTS + correction;

			thisPacket.setAbsTimecode(outPTS);

			if (firstPacket) {
				logger.debug("(" + streamName + ") [" + streamType + "] first PTS updated to [" + outPTS + "] PTS was [" + inPTS + "] basePTS [" + baseInPTS + "] baseSystemTime [" + baseSystemTime + "]");
			} else if (absPTSTimeCodeDiff > maxAllowedPTSDriftMillisec) {
				logger.warn("(" + streamName + ") [" + streamType + "] PTS diff[" + inPTSDiff + "] > threshold [" + maxAllowedPTSDriftMillisec + "] last PTS [" + lastInPTS + "] current PTS [" + inPTS + "] basePTS [" + baseInPTS + "] baseSystemTime [" + baseSystemTime + "]");
			} else {
				logger.debug("(" + streamName + ") [" + streamType + "] updated PTS [" + outPTS + "] in PTS [" + inPTS + "]");
			}
		}
	}

	public long[] getLiveEntryBaseSystemTime(String entryId, long baseSystemTime, long basePTSTimeCode) {

		long[] refData = {baseSystemTime, basePTSTimeCode};

		synchronized (this.mapLiveEntryToBaseSystemTime) {
			try {
				if (!this.mapLiveEntryToBaseSystemTime.containsKey(entryId)) {
					this.mapLiveEntryToBaseSystemTime.put(entryId, refData);
				} else {
					refData = this.mapLiveEntryToBaseSystemTime.get(entryId);
				}
			} catch (Exception e) {
				if (Boolean.valueOf(entryId)) {
					logger.error("(" + entryId + ") fail to get base timestamp for live entry." + e.toString());
				} else {
					logger.error("( undefined entryId ) could not get base timestamp, entryId is invalid. " + e.toString());
				}
			}
		}

		return refData;
	}

	public long[] setLiveEntryBaseSystemTime(String entryId, long baseSystemTime, long basePTSTimeCode) {

		long[] refData = {baseSystemTime, basePTSTimeCode};

		try {
			synchronized (this.mapLiveEntryToBaseSystemTime) {
				long[] currentRefData = this.mapLiveEntryToBaseSystemTime.get(entryId);
				if (Math.abs(basePTSTimeCode - currentRefData[0]) > maxAllowedPTSDriftMillisec) {
					this.mapLiveEntryToBaseSystemTime.put(entryId, refData);
				} else {
					refData = currentRefData;
				}
			}
		} catch (Exception e) {
			logger.error("(" + entryId + ") fail to sync PTS base timestamp for live entry." + e.toString());
		}

		return refData;
	}

	public void removeEntryBaseSystemTime(String entryId) {
		synchronized (this.mapLiveEntryToBaseSystemTime) {
			if (this.mapLiveEntryToBaseSystemTime.containsKey(entryId)) {
				this.mapLiveEntryToBaseSystemTime.remove(entryId);
			}
		}
	}

}
