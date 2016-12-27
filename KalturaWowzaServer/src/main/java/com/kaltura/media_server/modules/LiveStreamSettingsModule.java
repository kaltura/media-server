package com.kaltura.media_server.modules;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.media_server.services.Utils;

import com.wowza.wms.amf.*;
import com.wowza.wms.application.*;
import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.*;
import com.wowza.wms.media.mp3.model.idtags.*;
import com.wowza.wms.module.*;
import com.wowza.wms.stream.livepacketizer.*;

//import com.wowza.wms.application.IApplicationInstance;
//import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.CupertinoPacketHolder2;
//import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.LiveStreamPacketizerCupertinoChunk;
//import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.IHTTPStreamerCupertinoLivePacketizerDataHandler;
//import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.LiveStreamPacketizerCupertino;
//import com.wowza.wms.media.mp3.model.idtags.ID3Frames;
//import com.wowza.wms.media.mp3.model.idtags.ID3V2FrameBase;
//import com.wowza.wms.media.mp3.model.idtags.ID3V2FrameRawBytes;
//import com.wowza.wms.module.ModuleBase;
import com.wowza.wms.stream.IMediaStream;
//import com.wowza.wms.stream.livepacketizer.ILiveStreamPacketizer;
//import com.wowza.wms.stream.livepacketizer.ILiveStreamPacketizerActionNotify;
import com.wowza.wms.client.IClient;
import com.wowza.wms.application.WMSProperties;
import com.wowza.wms.stream.MediaStreamActionNotifyBase;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.text.SimpleDateFormat;
import java.lang.Thread;


// Todo Lilach: idea, to extract entry Id and manage single ID3 tag obj per Live stream entry and not per audio/video and flavor
// this way the exact ID3 tag will be reported for all at almost same time using single timer/timestamp
public class LiveStreamSettingsModule extends ModuleBase {

	private static final String OBJECT_TYPE_KEY = "objectType";
	private static final String OBJECT_TYPE_SYNCPOINT = "KalturaSyncPoint";
	private static final String OBJECT_TYPE_TIMECODE = "KalturaSyncTimecode";
	private static final String HEADER_TAG_TIMECODE = "KALTURA_SYNC_TIMESTAMP";
	private static final String TIMESTAMP_KEY = "timestamp";
	private static final String ID_KEY = "id";
	private static final String META_DATA_TIMECODE = "onFI";


	private static final Logger logger = Logger.getLogger(LiveStreamSettingsModule.class);
	private static final String PUBLIC_METADATA = "onMetaData";
	private final static int DEFAULT_SYNC_POINTS_INTERVAL_IN_MS = 8000;
	private final static String KALTURA_SYNC_POINTS_INTERVAL_PROPERTY = "KalturaSyncPointsInterval";
	private static final long START_SYNC_POINTS_DELAY = 0;
	//private final ConcurrentHashMap<IMediaStream, CuePointManagerLiveStreamListener> streams;

	private LiveStreamPacketizerListener liveStreamPacketizerListener;
	private int syncPointsInterval;

	public LiveStreamSettingsModule() {
		logger.debug("Creating a new instance of Modules.CuePointsManager");
		//this.streams = new ConcurrentHashMap<IMediaStream, CuePointManagerLiveStreamListener>();
	}

	class ID3V2FrameObject extends ID3V2FrameRawBytes {

		private int textEncoding = ID3V2FrameBase.TEXTENCODING_UTF8;
		private String text = null;

		public ID3V2FrameObject() {
			super(ID3V2FrameBase.TAG_TEXT);
		}

		public int getBodySize() {
			return 1 + serializeStringLen(text, TEXTENCODING_DEFAULT, true);
		}

		public int serializeBody(byte bytes[], int pos) {

			int length = 0;
			bytes[pos] = (byte) (textEncoding & 255);
			length += 1;

			if (text != null)
				length += serializeString(text, bytes, pos + length, TEXTENCODING_DEFAULT, true);

			return length;
		}

		public void setText(String text) {
			this.text = text;
		}
	}

	class LiveStreamPacketizerDataHandler implements IHTTPStreamerCupertinoLivePacketizerDataHandler2 {

		private LiveStreamPacketizerCupertino liveStreamPacketizer = null;
		private String streamName = null;
		private long runningId;


		public LiveStreamPacketizerDataHandler(LiveStreamPacketizerCupertino liveStreamPacketizer, String streamName) {
			logger.debug("creating LiveStreamPacketizerDataHandler for stream name: " + streamName);
			this.streamName = streamName;
			this.runningId = 1;
			this.liveStreamPacketizer = liveStreamPacketizer;
		}

/*
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
			addToMap(map, dataObj, TIMESTAMP_KEY);
			addToMap(map, dataObj, ID_KEY);
			return new ObjectMapper().writeValueAsString(map);
		}
*/
		public void onFillChunkStart(LiveStreamPacketizerCupertinoChunk chunk)
		{

			logger.info("ModuleCupertinoVODOnTextToID3.onFillChunkStart["+chunk.getRendition().toString()+":"+this.liveStreamPacketizer.getContextStr()+"]: chunkId:"+chunk.getChunkIndexForPlaylist());

			String now =Double.toString( new Date().getTime());

			// Add custom M3U tag to chunklist header
			CupertinoUserManifestHeaders userManifestHeaders = this.liveStreamPacketizer.getUserManifestHeaders(chunk.getRendition());
			if (userManifestHeaders != null) {
				userManifestHeaders.addHeader(HEADER_TAG_TIMECODE, TIMESTAMP_KEY, now);
			}

			try {


				Map<String, Object> map = new HashMap<>();
				map.put(OBJECT_TYPE_KEY, OBJECT_TYPE_SYNCPOINT);
				map.put(TIMESTAMP_KEY, now);
				String id = this.streamName + "_" + (runningId++);
				map.put(ID_KEY, id);
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(map);
				// Add ID3 tag to start of chunk
				ID3Frames id3Frames = this.liveStreamPacketizer.getID3FramesHeader();
				ID3V2FrameTextInformation id3Tag = new ID3V2FrameTextInformation(ID3V2FrameBase.TAG_TEXT);
				logger.info("<< update KalturaSyncPoint >> adding new id3Frame [" + this.streamName + "] " + json);
				id3Tag.setValue(json);
				// todo: Lilach check the purpose of below
				id3Frames.clear();
				id3Frames.putFrame(id3Tag);

				/*// based on the old code
				ID3V2FrameBase frame = new ID3V2FrameObject();
				((ID3V2FrameObject) frame).setText(json);

				id3Frames.clear();
				id3Frames.putFrame(frame);*/
			} catch (JsonProcessingException e) {
				logger.error("<< update KalturaSyncPoint >> Stream [" + this.streamName + "] failed to add sync points data to ID3Tag", e);
			}
			//textId = 1;
		}

		public void onFillChunkEnd(LiveStreamPacketizerCupertinoChunk chunk, long timecode)
		{
			logger.info("<< update KalturaSyncPoint >>  ModuleCupertinoVODOnTextToID3.onFillChunkEnd["+chunk.getRendition().toString()+":"+liveStreamPacketizer.getContextStr()+"]: chunkId:"+chunk.getChunkIndexForPlaylist());
		}

		public void onFillChunkMediaPacket(LiveStreamPacketizerCupertinoChunk chunk, CupertinoPacketHolder holder, AMFPacket packet)
		{
			//logger.info("<< update KalturaSyncPoint >>  ModuleCupertinoVODOnTextToID3.onFillChunkEnd["+chunk.getRendition().toString()+":"+liveStreamPacketizer.getContextStr()+"]: chunkId:"+chunk.getChunkIndexForPlaylist());
		}

	  //public void onFillDataChunkPacket(LiveStreamPacketizerCupertinoChunk chunk, AMFPacket packet, ID3Frames id3Frames) {
		public void onFillChunkDataPacket(LiveStreamPacketizerCupertinoChunk chunk, CupertinoPacketHolder holder, AMFPacket packet, ID3Frames id3Frames) {

			//logger.info("<< update KalturaSyncPoint >> started Stream [ "+ streamName + "] for ");

		/*	byte[] buffer = packet.getData();
			if (buffer == null) {
				logger.info("<< update KalturaSyncPoint >> Empty buffer");
				return;
			}

			if (packet.getSize() <= 2) {
				logger.info("<< update KalturaSyncPoint >> Packet size [" + packet.getSize() + "]");
				return;
			}

			int offset = 0;
			if (buffer[0] == 0)
				offset++;

			AMFDataList amfList = new AMFDataList(buffer, offset, buffer.length - offset);

			if (amfList.size() <= 1) {
				logger.info("<< update KalturaSyncPoint >> Stream [" + streamName + "] AMFList size [" + amfList.size() + "]");
				return;
			}

			if (amfList.get(0).getType() != AMFData.DATA_TYPE_STRING) {
				logger.info("<< update KalturaSyncPoint >> Stream [" + streamName + "] AMFList type [" + amfList.get(0).getType() + ", " + amfList.get(1).getType() + "]");
				return;
			}

			String metaDataStr = amfList.getString(0);
			AMFDataObj data = amfList.getObject(1);

			if (metaDataStr.equals(LiveStreamSettingsModule.PUBLIC_METADATA) || metaDataStr.equals(LiveStreamSettingsModule.META_DATA_TIMECODE)) {


				try {
					if (data == null) {
						return;
					}
					if (data.get("sd") != null && data.get("st") != null) {    //Check if AMFdata containing Timecode
						String datestring = (String) data.get("sd").getValue();
						datestring += " " + (String) data.get("st").getValue();
						SimpleDateFormat sdf;
						if (data.get("tz") != null) {
							String timezone = (String) data.get("tz").getValue();
							datestring = datestring + " " + timezone;
							sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss.SSS XXX");
						} else {
							sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss.SSS");
						}
						Date dateObj = sdf.parse(datestring);
						long UnixTime = dateObj.getTime();
						// Add following fields to the AMFDataObj
						data.put(OBJECT_TYPE_KEY, OBJECT_TYPE_TIMECODE);
						data.put(TIMESTAMP_KEY, UnixTime);
						data.put(ID_KEY, "null");
					}
					if (data.get(OBJECT_TYPE_KEY) != null) {
						String json = jsonAMF(data);
						//logger.debug("Stream [" + streamName + "] JSON: " + json);

						ID3V2FrameBase frame;
						frame = new ID3V2FrameObject();
						((ID3V2FrameObject) frame).setText(json);
						id3Frames.putFrame(frame);
						logger.info("<< update KalturaSyncPoint >> ID3 tag set to data [" + json + "] for stream [ "+ streamName + "]");
					}

				} catch (Exception e) {
					logger.error("<< update KalturaSyncPoint >> Stream [" + streamName + "] failed to add sync points data to ID3Tag", e);
				}
			} else {
				logger.info("<< update KalturaSyncPoint >> ID3TAG not set check if bug??? Stream [" + streamName + "] metadata [" + metaDataStr + "]");
				return;
			}*/
		}
	}

	class LiveStreamPacketizerListener implements ILiveStreamPacketizerActionNotify {

		private IApplicationInstance appInstance = null;
		private LiveStreamEntrySettingsHandler liveStreamEntrySettingsHandler = null;

		public LiveStreamPacketizerListener(IApplicationInstance appInstance) {
			logger.debug("creating new LiveStreamPacketizerListener");
			this.liveStreamEntrySettingsHandler = new LiveStreamEntrySettingsHandler();
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
			this.liveStreamEntrySettingsHandler.checkAndUpdateSettings(cupertinoPacketizer, stream, streamName);

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
		logger.info("Modules.CuePointsManager application instance started. Sync points interval: " + syncPointsInterval);
	}

/*	public void onStreamCreate(IMediaStream stream) {
		if (stream.getClientId() < 0) { //transcoded rendition
			return;
		}

		// Todo Lilach: need check first if an object of CuePointManagerLiveStreamListener already exists for the new stream
		// Todo Lilach: check how many times is onStreamCreate called per live stream entry. Only once? once per flavor?
		CuePointManagerLiveStreamListener listener = new CuePointManagerLiveStreamListener();
		// Todo Lilach: changed with replace
		streams.put(stream, listener);
		logger.debug("<< KalturaSyncPoint >> Modules.CuePointsManager::onStreamCreate with stream.getName() " + stream.getName() + " and stream.getClientId() " + stream.getClientId());
		stream.addClientListener(listener);
	}

	public void onStreamDestroy(IMediaStream stream) {

	*//*	try {
			// thread to sleep for 1000 milliseconds
			Thread.sleep(5000);
		} catch (Exception e) {
			logger.error("<< KalturaSyncPoint >> " + e);
		}*//*

		// Todo Lilach: check before remove
		CuePointManagerLiveStreamListener listener = streams.remove(stream);
		if (listener != null) {
			logger.debug("<< KalturaSyncPoint >> Modules.CuePointsManager::onStreamDestroy with stream.getName() " + stream.getName() + " and stream.getClientId() " + stream.getClientId());
			listener.dispose();
			stream.removeClientListener(listener);
		}
	}*/

/*	class CuePointManagerLiveStreamListener extends MediaStreamActionNotifyBase {

		private long runningId = 0;
		//private final Map<String, Timer> listenerStreams;

		public CuePointManagerLiveStreamListener() {
			logger.debug("Creating a new instance of CuePointManagerLiveStreamListener");
			//this.listenerStreams = new ConcurrentHashMap<String, Timer>();
		}

		@Override
		public void onUnPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend) {
			Timer t;
			synchronized (listenerStreams) {
				t = listenerStreams.remove(streamName);
			}
			cancelScheduledTask(t, streamName);
		}

		public void cancelScheduledTask(Timer t, String streamName) {
			if (t != null) {
				logger.debug("Stopping CuePoints timer for stream " + streamName);
				t.cancel();
				t.purge();
			} else {

				logger.error("Stream " + streamName + " does not exist in streams map");
			}
		}

		public void dispose() {
			synchronized (listenerStreams) {

				for (String streamName : listenerStreams.keySet()) {
					Timer t = listenerStreams.get(streamName);
					cancelScheduledTask(t, streamName);
				}
				listenerStreams.clear();
			}
		}

		@Override
		public void onPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend) {

			logger.debug("Stream [" + streamName + "]");


			if (stream.getClientId() < 0) {
				logger.debug("Stream [" + stream.getName() + "] entry [" + streamName + "] is a transcoded rendition");
				return;
			}

			logger.debug("Stream [" + streamName + "] entry [" + streamName + "]");

			Timer t;
			synchronized (listenerStreams) {
				if (listenerStreams.containsKey(streamName)) {
					logger.error("<< KalturaSyncPoint >> Stream with name " + streamName + " already exists in streams map, cancelling old one");
					t = listenerStreams.remove(streamName);
					cancelScheduledTask(t, streamName);
				} else {
					logger.debug("<< KalturaSyncPoint >> Stream with name " + streamName + " does not exist in streams map. creating a new map entry.");
				}
				// todo Lilach: use single timer. Suggest to report ID3 tag immediatly
				t = new Timer();
				listenerStreams.put(streamName, t);

			}

			logger.debug("Running timer to create sync points for stream " + streamName);
			startSyncPoints(t, stream, streamName);
		}

		// todo Lilach: suggesting to craete new class that extends Timer task and when object is ceated
		// pass stream and entryId and save it internally as private members.
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
				t.schedule(tt, START_SYNC_POINTS_DELAY, syncPointsInterval);
			} catch (Exception e) {
				logger.error("Error occurred while scheduling a timer task", e);
			}
		}

		private void createSyncPoint(IMediaStream stream, String entryId) {
			String id = entryId + "_" + (runningId++);
			double currentTime = new Date().getTime();

			sendSyncPoint(stream, id, currentTime);
		}

		public void sendSyncPoint(final IMediaStream stream, String id, double time) {
			logger.info("<< update KalturaSyncPoint >> skipping sendSyncPoint");
			return;

			AMFDataObj data = new AMFDataObj();

			data.put(OBJECT_TYPE_KEY, OBJECT_TYPE_SYNCPOINT);
			data.put(ID_KEY, id);
			data.put(TIMESTAMP_KEY, time);
			logger.info("<< update KalturaSyncPoint >> [" + stream.getName() + "] sync point [" + id + "],  getPlayPackets = " + stream.getPlayPackets().size());

			//This condition is due to huge duration time (invalid) in the first chunk after stop-start on FMLE.
			//According to Wowza calling sendDirect() before stream contains any packets causes problems.
			if (stream.getPlayPackets().size() > 0) {
				stream.sendDirect(LiveStreamSettingsModule.PUBLIC_METADATA, data);
			} else {
				logger.info("<< update KalturaSyncPoint >> [" + stream.getName() + "] sync point cancelled  [" + id + "],  getPlayPackets = " + stream.getPlayPackets().size());
			}
		}


	}*/

	class LiveStreamEntrySettingsHandler {

		private final static int DEFAULT_LOW_LATENCY_CHUNK_DURATION_MILLISECONDS = 4000;

		public void checkAndUpdateSettings(LiveStreamPacketizerCupertino cupertinoPacketizer, IMediaStream stream, String streamName) {
			KalturaLiveEntry liveEntry;
			WMSProperties properties;
			if (!stream.isTranscodeResult()) {
				return;
			}

			try {
				IClient client = stream.getClient();
				properties = client.getProperties();
				liveEntry = Utils.getLiveEntry(properties);
			} catch (Exception e) {
				logger.error("Failed to retrieve liveEntry for " + streamName + " :" + e);
				return;
			}

			/*if (liveEntry.enableLowLatency) {
				cupertinoPacketizer.getProperties().setProperty("cupertinoChunkDurationTarget", this.DEFAULT_LOW_LATENCY_CHUNK_DURATION_MILLISECONDS);
			}*/
		}

	}

}
