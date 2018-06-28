package com.kaltura.media_server.modules;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaltura.media_server.services.Constants;
import com.kaltura.media_server.services.Utils;
import com.wowza.wms.amf.*;
import com.wowza.wms.application.*;
import com.wowza.wms.client.IClient;
import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.*;
import com.wowza.wms.media.mp3.model.idtags.*;
import com.wowza.wms.stream.livepacketizer.*;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.module.*;
import com.wowza.wms.stream.*;
import com.wowza.wms.application.WMSProperties;
import com.wowza.wms.rtp.model.*;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// todo: test and add relevant code ofr ptsTimeCode moving back in time and PTS wrap arround are supported.

// todo: this code for PTS smoothing contains following assumption: ref_t + ref_pts_timecode - last_pts_timecode < new_ref_t

public class LiveStreamSettingsModule extends ModuleBase {

	private static final String OBJECT_TYPE_KEY = "objectType";
	private static final String OBJECT_TYPE_SYNCPOINT = "KalturaSyncPoint";
	//private static final String HEADER_TAG_TIMECODE = "EXT-KALTURA-SYNC-POINT";
	private static final String TIMESTAMP_KEY = "timestamp";
	private static final String ID_KEY = "id";
	private static final Logger logger = Logger.getLogger(LiveStreamSettingsModule.class);
	private static final String MAX_ALLOWED_PTS_DRIFT_MILLISEC = "KalturaMaxAllowedPTSDriftiMillisec";
	private static final int DEFAULT_MAX_ALLOWED_PTS_DRIFT_MILLISEC = 10000;
	private static final int GLOBAL_SYSTEM_TIME_INDEX = 0;
	private static final int GLOBAL_BASE_PTS_INDEX = 1;

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

			//logger.info("adding ID3 frame (timestamp=" + startTimeStr + ") to chunk [" + chunk.getRendition().toString() + ":" + this.liveStreamPacketizer.getContextStr() + "]: chunkId:" + chunk.getChunkIndexForPlaylist());

			// Add custom M3U tag to chunklist header
/*
			CupertinoUserManifestHeaders userManifestHeaders = this.liveStreamPacketizer.getUserManifestHeaders(chunk.getRendition());
			if (userManifestHeaders != null) {
				userManifestHeaders.addHeader(HEADER_TAG_TIMECODE, TIMESTAMP_KEY, packetStartTime);
			}
*/

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
				// logger.info("adding new id3Frame [" + this.streamName + "] " + json);
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
			//logger.info("[" + chunk.getRendition().toString() + ":" + liveStreamPacketizer.getContextStr() + "]: chunkId:" + chunk.getChunkIndexForPlaylist());
		}

		public void onFillChunkMediaPacket(LiveStreamPacketizerCupertinoChunk chunk, CupertinoPacketHolder holder, AMFPacket packet) {
			//logger.info("[" + chunk.getRendition().toString() + ":" + liveStreamPacketizer.getContextStr() + "]: chunkId:" + chunk.getChunkIndexForPlaylist());
		}

		public void onFillChunkDataPacket(LiveStreamPacketizerCupertinoChunk chunk, CupertinoPacketHolder holder, AMFPacket packet, ID3Frames id3Frames) {
			//logger.info("[" + chunk.getRendition().toString() + ":" + liveStreamPacketizer.getContextStr() + "]: chunkId:" + chunk.getChunkIndexForPlaylist());
		}

	}

	class LiveStreamPacketizerListener implements ILiveStreamPacketizerActionNotify {

		private IApplicationInstance appInstance = null;
		private DynamicStreamSettings dynamicStreamSettings = null;

		public LiveStreamPacketizerListener(IApplicationInstance appInstance) {
			logger.debug("creating new LiveStreamPacketizerListener");
			this.appInstance = appInstance;
			this.dynamicStreamSettings = new DynamicStreamSettings();
		}

		public void onLiveStreamPacketizerCreate(ILiveStreamPacketizer liveStreamPacketizer, String streamName) {
			logger.debug("onLiveStreamPacketizerCreate. stream: " + streamName);
			if (!(liveStreamPacketizer instanceof LiveStreamPacketizerCupertino))
				return;

			LiveStreamPacketizerCupertino cupertinoPacketizer = (LiveStreamPacketizerCupertino) liveStreamPacketizer;
			IMediaStream stream = this.appInstance.getStreams().getStream(streamName);
			dynamicStreamSettings.checkAndUpdateSettings(cupertinoPacketizer, stream);
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

		// 05-04-2017 todo: uncomment when wowza support provides way to differ ingest from transcode in RTSP stream as exist in RTMP
/*		int clientId = stream.getClientId();

		if (clientId < 0) { //transcoded rendition
			return;
		}*/
		PacketListener listener = new PacketListener();
		WMSProperties props = stream.getProperties();
		synchronized (props) {
			props.setProperty(Constants.STREAM_ACTION_LISTENER_PROPERTY, listener);
		}
		stream.addLivePacketListener(listener);

	}


	public void onStreamDestroy(IMediaStream stream) {

		removeListener(stream);
	}

	private void removeListener(IMediaStream stream) {
		PacketListener listener = null;
		String streamName = stream.getName();
		WMSProperties props = stream.getProperties();
		synchronized (props) {
			listener = (PacketListener) props.get(Constants.STREAM_ACTION_LISTENER_PROPERTY);
		}
		if (listener != null) {
			stream.removeLivePacketListener(listener);
			String entryId = Utils.getEntryIdFromStreamName(streamName);
			this.removeGlobalPTSSyncData(streamName);
			logger.info("PTS_SYNC: (" + streamName + ") remove PacketListener: [" + stream.getSrc() + "] and removed global PTS sync data for entry [" + entryId + "]");
		}
	}

	public class PacketListener implements IMediaStreamLivePacketNotify {

		private static final int BASE_TIME_INDEX = 0;
		private static final int BASE_PTS_INDEX = 1;
		private static final int LAST_IN_PTS_INDEX = 2;
		private static final int SHOULD_SYNC = 3;
		private static final int VIDEO_INDEX = 0;
		private static final int AUDIO_INDEX = 1;
		private static final int DATA_INDEX = 2;
		private static final int NUM_TYPES = 3;
		private static final int SYNC_PARAMS_COUNT = 4;
		private final String[] TYPE_STR = {"video", "audio", "data"};
		private String entryId = null;
		private long[][] syncPTSData = null;
		private String streamName = null;


		public PacketListener() {
			this.syncPTSData = new long[NUM_TYPES][SYNC_PARAMS_COUNT];

		}

		public long[][] getSyncPTSData(){
            return this.syncPTSData;
        }

		private int getIndex(AMFPacket thisPacket) {
			if (thisPacket.isVideo()) {
				return VIDEO_INDEX;
			} else if (thisPacket.isAudio()) {
				return AUDIO_INDEX;
			}

			return DATA_INDEX;
		}

		// ignore data events. They will probably always cause false PTS jump alarm!!! (e.g. AMF PLAT-6959)
		public void onLivePacket(IMediaStream stream, AMFPacket thisPacket) {

			long baseSystemTime = 0;
			long baseInPTS = 0;
			long lastInPTS = 0;
			long currentTime = System.currentTimeMillis();

			if (this.entryId == null) {
				streamName = stream.getName();
				this.entryId = Utils.getEntryIdFromStreamName(streamName);
				if (stream.isTranscodeResult()) {
					logger.debug("PTS_SYNC: (" + streamName + ") removing live packet listener because it is transcode stream and PTS sync is done on ingest");
					stream.removeLivePacketListener(this);
					return;
				}
			}
			int typeIndex = this.getIndex(thisPacket);
			String streamType = TYPE_STR[typeIndex];

			// get data from input packet
			long inPTS = thisPacket.getAbsTimecode();

			baseSystemTime = syncPTSData[typeIndex][BASE_TIME_INDEX];
			baseInPTS = syncPTSData[typeIndex][BASE_PTS_INDEX];
			lastInPTS = syncPTSData[typeIndex][LAST_IN_PTS_INDEX];

			// init local parameters used to calculate output PTS
			boolean firstPacket = (baseSystemTime == 0) ? true : false;
			long clockDiff = currentTime - baseSystemTime;
			long ptsDiff = inPTS - baseInPTS;
			long ptsMisalignment = !(firstPacket) ? clockDiff - ptsDiff : 0;
			boolean ptsJump = (Math.abs(ptsMisalignment) > maxAllowedPTSDriftMillisec && typeIndex != DATA_INDEX) ? true : false;
			boolean doSync = checkIfShouldSync(typeIndex, streamName);

			//=================================================================
			// handle first packet & PTS jump
			//=================================================================
			if (firstPacket || ptsJump || doSync) {
				long[] globalPTSData = null;

				// check if sync required or update of global data
				if (doSync) {  // sync
					globalPTSData = getGlobalPTSSyncData(streamName);
				} else {                  // 1st packet or pts jump
					turnOnShouldSyncFlag(typeIndex);
					globalPTSData = getAndUpdateGlobalPTSSyncData(streamName, currentTime, inPTS, streamType);
				}

				baseSystemTime = globalPTSData[GLOBAL_SYSTEM_TIME_INDEX];
				baseInPTS = globalPTSData[GLOBAL_BASE_PTS_INDEX];
				syncPTSData[typeIndex][BASE_TIME_INDEX] = globalPTSData[GLOBAL_SYSTEM_TIME_INDEX];
				syncPTSData[typeIndex][BASE_PTS_INDEX] = globalPTSData[GLOBAL_BASE_PTS_INDEX];
			}

			//=================================================================
			// calc & update output PTS value
			//=================================================================
			long correction = baseSystemTime - baseInPTS;
			long outPTS = inPTS + correction;
			syncPTSData[typeIndex][LAST_IN_PTS_INDEX] = outPTS;

			thisPacket.setAbsTimecode(outPTS);

			// Do not uncomment or remove. To be used for development debugging only!!!
			//logger.debug("(" + streamName + ") [" + streamType + "] [time: "+ currentTime +"] PTS tuple [inPTS: " + inPTS + ", outPTS: " + outPTS +", correction: " + correction + "newPTS: " + baseInPTS + ", baseTime:" + baseSystemTime+ "]" );

			if (firstPacket) {
				logger.debug("PTS_SYNC: <<< first pts >>>> (" + streamName + ") [" + streamType + "] new PTS [" + outPTS + "] old PTS [" + inPTS + "] basePTS [" + baseInPTS + "] baseSystemTime [" + baseSystemTime + "] PTS diff [" + ptsMisalignment + "] ");
			} else if (doSync) {
				logger.warn("PTS_SYNC: <<< sync >>> (" + streamName + ") [" + streamType + "] new PTS [" + outPTS + "] old PTS [" + inPTS + "] basePTS [" + baseInPTS + "] baseSystemTime [" + baseSystemTime + "] PTS diff [" + ptsMisalignment + "] ");
			} else if (ptsJump) {
				logger.warn("PTS_SYNC: <<< found pts jump! >>> (" + streamName + ") [" + streamType + "] PTS diff [abs(" + ptsMisalignment + ")] > [" + maxAllowedPTSDriftMillisec + "] new PTS [" + outPTS + "] + old PTS [" + inPTS + "] new basePTS [" + baseInPTS + "] new baseSystemTime [" + baseSystemTime + "]");
			}
			//else {
		//		logger.debug("(" + streamName + ") [" + streamType + "] updated PTS [" + outPTS + "] in PTS [" + inPTS + "] correction " + correction);
	//		}
		}

		public void turnOnShouldSyncFlag(int typeIndex){
			for (int i=0; i<NUM_TYPES; i++){
				if (i != typeIndex){
					syncPTSData[i][SHOULD_SYNC] = 1;
				}
			}
		}

		public boolean checkIfShouldSync(int typeIndex, String streamName){
			if (syncPTSData[typeIndex][SHOULD_SYNC] == 1 ){
				syncPTSData[typeIndex][SHOULD_SYNC] = 0 ;	//turn off flag
				String streamType = TYPE_STR[typeIndex];
				logger.debug("PTS_SYNC: (" + streamName + ") [" + streamType + "] Found that streamType shouldSync");
				return true;
			}
			return false;
		}

	}


	public long[] getAndUpdateGlobalPTSSyncData(String streamName, long newSystemTime, long newPTS, String type) {

		long[] newSyncData = {newSystemTime, newPTS};
		try {
			String entryId = Utils.getEntryIdFromStreamName(streamName);

			synchronized (this.mapLiveEntryToBaseSystemTime) {
				if (!this.mapLiveEntryToBaseSystemTime.containsKey(entryId)) {
					this.mapLiveEntryToBaseSystemTime.put(entryId, newSyncData);
					logger.warn("PTS_SYNC: (" + streamName + ") [" + type + "] first PTS sync data for entry [" + entryId + "] initializing global PTS sync data to [newPTS:"+ newPTS + ", newSystemTime" + newSystemTime +"]");
				} else {

					long[] globalSyncData = this.mapLiveEntryToBaseSystemTime.get(entryId);
		            long clockDiff = newSystemTime - globalSyncData[GLOBAL_SYSTEM_TIME_INDEX];
					long ptsDiff = newPTS - globalSyncData[GLOBAL_BASE_PTS_INDEX];
					long ptsMisalignment = clockDiff - ptsDiff;
					if (Math.abs(ptsMisalignment) > maxAllowedPTSDriftMillisec)  {
						logger.warn("PTS_SYNC: (" + streamName + ") [" + type + "] found PTS jump, PTS misalignment [" + ptsMisalignment + "] milliseconds, replacing global PTS sync data from [newPTS:"+ globalSyncData[GLOBAL_BASE_PTS_INDEX] + ", newSystemTime" + globalSyncData[GLOBAL_SYSTEM_TIME_INDEX] +"] to [newPTS:" + newPTS + ", newSystemTime:" + newSystemTime +"]");
						this.mapLiveEntryToBaseSystemTime.put(entryId, newSyncData);
					} else {
						logger.warn("PTS_SYNC: (" + streamName + ") [" + type + "] not need to updated sync data for entry [" + entryId + "] PTS diff <=  " + maxAllowedPTSDriftMillisec);
						newSyncData = globalSyncData;
					}
				}
			}
		} catch (Exception e) {
			logger.error("PTS_SYNC: (" + streamName + ") fail to sync PTS base timestamp for live entry" + e.toString());
		}

		return newSyncData;
	}

	public long[] getGlobalPTSSyncData(String streamName) {
		long[] globalSyncData = null;

		try {
			String entryId = Utils.getEntryIdFromStreamName(streamName);

			synchronized (this.mapLiveEntryToBaseSystemTime) {
				globalSyncData = this.mapLiveEntryToBaseSystemTime.get(entryId);
			}
		} catch (Exception e) {
			logger.error("PTS_SYNC: (" + streamName + ") fail to get sync PTS data" + e.toString());
		}
		return globalSyncData;
	}

	public void removeGlobalPTSSyncData(String streamName) {
		String entryId = Utils.getEntryIdFromStreamName(streamName);
		synchronized (this.mapLiveEntryToBaseSystemTime) {
			if (this.mapLiveEntryToBaseSystemTime.containsKey(entryId)) {
				this.mapLiveEntryToBaseSystemTime.remove(entryId);
				logger.warn("PTS_SYNC: (" + streamName + ") removed entry [key=" + entryId + "] from global PTS sync data");
			}
		}
	}

}
