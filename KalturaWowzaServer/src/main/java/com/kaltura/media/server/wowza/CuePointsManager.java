package com.kaltura.media.server.wowza;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.infra.StringUtils;
import com.kaltura.media.server.KalturaEventsManager;
import com.kaltura.media.server.KalturaServer;
import com.kaltura.media.server.events.IKalturaEvent;
import com.kaltura.media.server.events.IKalturaEventConsumer;
import com.kaltura.media.server.events.KalturaEventType;
import com.kaltura.media.server.managers.ILiveManager;
import com.kaltura.media.server.managers.KalturaManager;
import com.kaltura.media.server.managers.KalturaManagerException;
import com.kaltura.media.server.wowza.events.KalturaApplicationInstanceEvent;
import com.kaltura.media.server.wowza.events.KalturaMediaEventType;
import com.kaltura.media.server.wowza.events.KalturaMediaStreamEvent;
import com.wowza.wms.amf.*;
import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.CupertinoPacketHolder;
import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.IHTTPStreamerCupertinoLivePacketizerDataHandler;
import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.LiveStreamPacketizerCupertino;
import com.wowza.wms.media.mp3.model.idtags.ID3Frames;
import com.wowza.wms.media.mp3.model.idtags.ID3V2FrameBase;
import com.wowza.wms.media.mp3.model.idtags.ID3V2FrameRawBytes;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.stream.MediaStream;
import com.wowza.wms.stream.livepacketizer.ILiveStreamPacketizer;
import com.wowza.wms.stream.livepacketizer.ILiveStreamPacketizerActionNotify;
import org.apache.log4j.Logger;

import java.util.*;

public class CuePointsManager extends KalturaManager implements IKalturaEventConsumer, ILiveManager.ILiveEntryReferrer {

	private static final String OBJECT_TYPE_KEY = "objectType";
	private static final String OBJECT_TYPE_VALUE = "KalturaSyncPoint";
	private static final String OFFSET_KEY = "offset";
	private static final String TIMESTAMP_KEY = "timestamp";
	private static final String ID_KEY = "id";

	private static final Logger logger = Logger.getLogger(CuePointsManager.class);
	private static final String PUBLIC_METADATA = "onMetaData";
	private final static int DEFAULT_SYNC_POINTS_INTERVAL_IN_MS = 8000;
	private final static String KALTURA_SYNC_POINTS_INTERVAL_PROPERTY = "KalturaSyncPointsInterval";
	private static final long START_SYNC_POINTS_DELAY = 0;

	private final Map<String,Timer> streams;
	private LiveStreamPacketizerListener liveStreamPacketizerListener;
	private ILiveManager liveManager;
	private int syncPointsInterval;

	public CuePointsManager() {
		logger.debug("Creating a new instance of CuePointsManager");
		this.streams = new HashMap<>();
		this.liveStreamPacketizerListener = new LiveStreamPacketizerListener();
	}

	class ID3V2FrameObject extends ID3V2FrameRawBytes{

		private int textEncoding = ID3V2FrameBase.TEXTENCODING_UTF8;
		private String text = null;

		public ID3V2FrameObject() {
			super(ID3V2FrameBase.TAG_TEXT);
		}

		public int getBodySize() {
			return 1 + serializeStringLen(text, TEXTENCODING_DEFAULT, true);
		}

		public int serializeBody(byte bytes[], int pos){

			int length = 0;
			bytes[pos] = (byte)(textEncoding & 255);
			length += 1;

			if(text != null)
				length += serializeString(text, bytes, pos + length, TEXTENCODING_DEFAULT, true);

			return length;
		}

		public void setText(String text) {
			this.text = text;
		}
	}

	class LiveStreamPacketizerDataHandler implements IHTTPStreamerCupertinoLivePacketizerDataHandler {
		String streamName = null;

		public LiveStreamPacketizerDataHandler(String streamName) {
			logger.debug("creating LiveStreamPacketizerDataHandler for stream name: " + streamName);
			this.streamName = streamName;
		}

		private void addToMap(Map<String, Object> map, AMFDataObj o, String key) {
			AMFData obj = o.get(key);
			if (obj != null) {
				map.put(key, obj.getValue());
			} else {
				throw new NoSuchElementException(key + " is not found in AMFDataObj");
			}
		}

		private String jsonAMF(AMFDataObj dataObj) throws JsonProcessingException {
			Map<String, Object> map = new HashMap<>();
			addToMap(map, dataObj, OBJECT_TYPE_KEY);
			addToMap(map, dataObj, OFFSET_KEY);
			addToMap(map, dataObj, TIMESTAMP_KEY);
			addToMap(map, dataObj, ID_KEY);
			return new ObjectMapper().writeValueAsString(map);
		}

		public void onFillChunkDataPacket(CupertinoPacketHolder holder, AMFPacket packet, ID3Frames id3Frames) {

			logger.debug("onFillChunkDataPacket");
			byte[] buffer = packet.getData();
			if (buffer == null) {
				logger.info("Empty buffer");
				return;
			}

			if (packet.getSize() <= 2) {
				logger.info("Packet size [" + packet.getSize() + "]");
				return;
			}

			int offset = 0;
			if (buffer[0] == 0)
				offset++;

			AMFDataList amfList = new AMFDataList(buffer, offset, buffer.length - offset);

			if (amfList.size() <= 1) {
				logger.info("Stream [" + streamName + "] AMFList size [" + amfList.size() + "]");
				return;
			}

			if (amfList.get(0).getType() != AMFData.DATA_TYPE_STRING) {
				logger.info("Stream [" + streamName + "] AMFList type [" + amfList.get(0).getType() + ", " + amfList.get(1).getType() + "]");
				return;
			}

			String metaDataStr = amfList.getString(0);
			AMFDataObj data = amfList.getObject(1);

			if (!metaDataStr.equals(CuePointsManager.PUBLIC_METADATA)) {
				logger.info("Stream [" + streamName + "] metadata [" + metaDataStr + "]");
				return;
			}

			try {
				if (data == null) {
					return;
				}

				if ( data.get(OBJECT_TYPE_KEY) != null){
					String json = jsonAMF(data);
					logger.debug("Stream [" + streamName + "] JSON:\n" + json);

					ID3V2FrameBase frame;
					frame = new ID3V2FrameObject();
					((ID3V2FrameObject) frame).setText(json);
					id3Frames.putFrame(frame);
				}

			} catch (Exception e) {
				logger.error("failed to add sync points data to ID3Tag",e);
			}

		}
	}

	class LiveStreamPacketizerListener implements ILiveStreamPacketizerActionNotify {

		public LiveStreamPacketizerListener() {
			logger.debug("creating new LiveStreamPacketizerListener");
		}

		public void onLiveStreamPacketizerCreate(ILiveStreamPacketizer liveStreamPacketizer, String streamName) {
			logger.debug("onLiveStreamPacketizerCreate. stream: " + streamName);
			if (!(liveStreamPacketizer instanceof LiveStreamPacketizerCupertino))
				return;

			logger.info("Create [" + streamName + "]: " + liveStreamPacketizer.getClass().getSimpleName());
			((LiveStreamPacketizerCupertino) liveStreamPacketizer).setDataHandler(new LiveStreamPacketizerDataHandler(streamName));
		}

		public void onLiveStreamPacketizerDestroy(ILiveStreamPacketizer liveStreamPacketizer) {
			logger.debug("onLiveStreamPacketizerDestroy");
		}

		public void onLiveStreamPacketizerInit(ILiveStreamPacketizer liveStreamPacketizer, String streamName) {
			logger.debug("onLiveStreamPacketizerInit");
		}
	}

	@Override
	public void init() throws KalturaManagerException {
		super.init();
		KalturaEventsManager.registerEventConsumer(this, KalturaEventType.STREAM_PUBLISHED, KalturaEventType.STREAM_UNPUBLISHED, KalturaMediaEventType.APPLICATION_INSTANCE_STARTED);
		liveManager = KalturaServer.getManager(ILiveManager.class);
		setInitialized();
	}

	@Override
	public void onEvent(IKalturaEvent event) {

		if (event.getType() instanceof KalturaMediaEventType) {

			switch ((KalturaMediaEventType) event.getType()) {
				case APPLICATION_INSTANCE_STARTED:
					KalturaApplicationInstanceEvent applicationInstanceEvent = (KalturaApplicationInstanceEvent) event;
					onAppStart(applicationInstanceEvent.getApplicationInstance());
					break;
				default:
					break;
			}
		}

		if (event.getType() instanceof KalturaEventType) {

			KalturaMediaStreamEvent streamEvent = (KalturaMediaStreamEvent) event;
			switch ((KalturaEventType) event.getType()) {
				case STREAM_PUBLISHED:
					onPublish(streamEvent.getMediaStream(), streamEvent.getEntryId());
					break;
				case STREAM_UNPUBLISHED:
					onUnPublish(streamEvent.getMediaStream(), streamEvent.getEntryId());
					break;
				default:
					break;
			}
		}
	}

	private void onUnPublish(IMediaStream mediaStream, String entryId) {
		final String streamName = mediaStream.getName();
		Timer t;
		synchronized (streams) {
			if (!streams.containsKey(streamName)) {
				logger.error("Unpublishing a stream that does not exist in streams map: " + streamName);
				return;
			}
			t = streams.remove(streamName);
		}
		logger.debug("Stopping CuePoints timer for stream " + streamName);
		t.cancel();
		t.purge();

		liveManager.removeReferrer(entryId, this);

	}

	protected void onAppStart(IApplicationInstance applicationInstance) {
		applicationInstance.addLiveStreamPacketizerListener(liveStreamPacketizerListener);
		syncPointsInterval = applicationInstance.getProperties().getPropertyInt(KALTURA_SYNC_POINTS_INTERVAL_PROPERTY, DEFAULT_SYNC_POINTS_INTERVAL_IN_MS);
		logger.info("CuePointsManager application instance started. Sync points interval: " + syncPointsInterval);
	}

	protected void onPublish(final IMediaStream stream, final String entryId) {
		if(stream.getClientId() < 0){
			logger.debug("Stream [" + stream.getName() + "] entry [" + entryId + "] is a transcoded rendition");
			return;
		}

		String streamName = stream.getName();
		logger.debug("Stream [" + streamName + "] entry [" + entryId + "]");
		liveManager.addReferrer(entryId, this);

		final Timer t;
		synchronized (streams) {
			if (streams.containsKey(streamName)) {
				//TODO, can this situation occur? need to reset timer?
				logger.error("Stream with name " + streamName + " already exists in streams map");
				t = streams.get(streamName);
			}
			else {
				logger.debug("Stream with name " + streamName + " does not exist in streams map. creating a new map entry.");
				t = new Timer();
				streams.put(streamName, t);
			}
		}

		logger.debug("Running timer to create sync points for stream " + streamName);
		startSyncPoints(t, stream, entryId);
	}

	private void startSyncPoints(Timer t, final IMediaStream stream, final String entryId) {
		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				try {
					createSyncPoint(stream, entryId);
				} catch (Exception e) {
					logger.error("Error occured while running sync points timer", e);
				}
			}
		};
		try {
			t.schedule(tt,START_SYNC_POINTS_DELAY, syncPointsInterval);
		} catch (Exception e) {
			logger.error("Error occurred while scheduling a timer task",e);
		}
	}

	private void createSyncPoint(IMediaStream stream, String entryId) {
		KalturaLiveEntry liveEntry = liveManager.get(entryId);
		String id = StringUtils.getUniqueId();
		double currentTime = new Date().getTime();
		double offset = currentTime - liveEntry.currentBroadcastStartTime * 1000;	//offset in ms

		logger.debug("Sending sync point: entryId: " + entryId + " stream: " + stream.getName() + " id: " + id + " timestamp: " + currentTime + " offset: " + offset + " BCStartTime: " + (liveEntry.currentBroadcastStartTime * 1000));
		sendSyncPoint(stream, id, currentTime, offset);
	}

	public void sendSyncPoint(final IMediaStream stream, String id, double time, double offset) {

		AMFDataObj data = new AMFDataObj();

		data.put(OBJECT_TYPE_KEY, OBJECT_TYPE_VALUE);
		data.put(ID_KEY, id);
		data.put(OFFSET_KEY, offset);
		data.put(TIMESTAMP_KEY, time);

		//This condition is due to huge duration time (invalid) in the first chunk after stop-start on FMLE.
		if (stream.getPlayPackets().size() > 0) {
			stream.sendDirect(CuePointsManager.PUBLIC_METADATA, data);
		}
		logger.info("Sent sync-point: stream " + stream.getName() + "time: " + time + " offset: " + offset + "id: " + id);
	}
}
