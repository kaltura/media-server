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


// Todo Lilach: idea, to extract entry Id and manage single ID3 tag obj per Live stream entry and not per audio/video and flavor
// this way the exact ID3 tag will be reported for all at almost same time using single timer/timestamp
public class LiveStreamSettingsModule extends ModuleBase {

	private static final String OBJECT_TYPE_KEY = "objectType";
	private static final String OBJECT_TYPE_SYNCPOINT = "KalturaSyncPoint";
	private static final String OBJECT_TYPE_TIMECODE = "KalturaSyncTimecode";
	private static final String HEADER_TAG_TIMECODE = "EXT-KALTURA-SYNC-POINT";
	private static final String TIMESTAMP_KEY = "timestamp";
	private static final String ID_KEY = "id";
	private static final String META_DATA_TIMECODE = "onFI";


	private static final Logger logger = Logger.getLogger(LiveStreamSettingsModule.class);
	// todo: remove from config file:
	private final static int DEFAULT_SYNC_POINTS_INTERVAL_IN_MS = 8000;
	private final static String KALTURA_SYNC_POINTS_INTERVAL_PROPERTY = "KalturaSyncPointsInterval";

	private LiveStreamPacketizerListener liveStreamPacketizerListener;
	private int syncPointsInterval;

	public LiveStreamSettingsModule() {
		logger.debug("Creating a new instance of Modules.CuePointsManager");
	}

	class LiveStreamPacketizerDataHandler implements IHTTPStreamerCupertinoLivePacketizerDataHandler2 {

		private LiveStreamPacketizerCupertino liveStreamPacketizer = null;
		private String streamName = null;

		public LiveStreamPacketizerDataHandler(LiveStreamPacketizerCupertino liveStreamPacketizer, String streamName) {
			logger.debug("creating LiveStreamPacketizerDataHandler for stream name: " + streamName);
			this.streamName = streamName;
			this.liveStreamPacketizer = liveStreamPacketizer;
		}

		public void onFillChunkStart(LiveStreamPacketizerCupertinoChunk chunk)
		{
			double packetStartTime = (double)chunk.getStartTimecode();
			String startTimeStr = Double.toString(packetStartTime);

			logger.info("ModuleCupertinoVODOnTextToID3.onFillChunkStart["+chunk.getRendition().toString()+":"+this.liveStreamPacketizer.getContextStr()+"]: chunkId:"+chunk.getChunkIndexForPlaylist());

			//todo: Lilach, check if it is important that the trailing id will be the same as ts id in filename.
			String id = this.streamName + "_" + chunk.getChunkIndexForPlaylist();

			logger.info("ModuleCupertinoVODOnTextToID3.onFillChunkStart elapsed " + syncPointsInterval + " milliseconds. Adding ID3 frame (timestamp=" + startTimeStr+ ") to chunk ["+chunk.getRendition().toString()+":"+this.liveStreamPacketizer.getContextStr()+"]: chunkId:"+chunk.getChunkIndexForPlaylist());

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
				logger.info("<< update KalturaSyncPoint >> adding new id3Frame [" + this.streamName + "] " + json);
				id3Tag.setValue(json);
				// it is essential to call clear. This method is used to indicate ID3 is set in specified chunk
				// if clear() is not called multiple ID3 tag frame will be inserted to each chunk.
				id3Frames.clear();
				id3Frames.putFrame(id3Tag);

			} catch (JsonProcessingException e) {
				logger.error("<< update KalturaSyncPoint >> Stream [" + this.streamName + "] failed to add sync points data to ID3Tag", e);
			}

		}

		public void onFillChunkEnd(LiveStreamPacketizerCupertinoChunk chunk, long timecode) {
			logger.info("<< update KalturaSyncPoint >>  ModuleCupertinoVODOnTextToID3.onFillChunkEnd["+chunk.getRendition().toString()+":"+liveStreamPacketizer.getContextStr()+"]: chunkId:"+chunk.getChunkIndexForPlaylist());
		}

		public void onFillChunkMediaPacket(LiveStreamPacketizerCupertinoChunk chunk, CupertinoPacketHolder holder, AMFPacket packet) {
		}

		public void onFillChunkDataPacket(LiveStreamPacketizerCupertinoChunk chunk, CupertinoPacketHolder holder, AMFPacket packet, ID3Frames id3Frames) {
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

			logger.info("Create [" + streamName + "]: " + liveStreamPacketizer.getClass().getSimpleName());
			cupertinoPacketizer.setDataHandler(new LiveStreamPacketizerDataHandler(cupertinoPacketizer, streamName));

			IMediaStream stream = this.appInstance.getStreams().getStream(streamName);

		}

		public void onLiveStreamPacketizerDestroy(ILiveStreamPacketizer liveStreamPacketizer) {
			logger.debug("onLiveStreamPacketizerDestroy");
		}

		public void onLiveStreamPacketizerInit(ILiveStreamPacketizer liveStreamPacketizer, String streamName) {
			logger.debug("onLiveStreamPacketizerInit");
		}
	}

	public void onAppStart(IApplicationInstance applicationInstance) {
		this.liveStreamPacketizerListener = new LiveStreamPacketizerListener(applicationInstance);
		applicationInstance.addLiveStreamPacketizerListener(this.liveStreamPacketizerListener);
		syncPointsInterval = applicationInstance.getProperties().getPropertyInt(KALTURA_SYNC_POINTS_INTERVAL_PROPERTY, DEFAULT_SYNC_POINTS_INTERVAL_IN_MS);
		logger.info("Modules.LiveStreamSettingsModule application instance started");
	}

	public void onStreamCreate(IMediaStream stream)
	{
		stream.addLivePacketListener(new PacketListener());
	}

	class PacketListener implements IMediaStreamLivePacketNotify {

		public PacketListener() {
		}

		public void onLivePacket(IMediaStream stream, AMFPacket thisPacket) {

			thisPacket.setAbsTimecode(System.currentTimeMillis());

		}


	}
}
