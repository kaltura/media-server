package com.kaltura.media_server.modules;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wowza.wms.amf.*;
import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.CupertinoPacketHolder;
import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.IHTTPStreamerCupertinoLivePacketizerDataHandler;
import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.LiveStreamPacketizerCupertino;
import com.wowza.wms.media.mp3.model.idtags.ID3Frames;
import com.wowza.wms.media.mp3.model.idtags.ID3V2FrameBase;
import com.wowza.wms.media.mp3.model.idtags.ID3V2FrameRawBytes;
import com.wowza.wms.module.ModuleBase;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.stream.livepacketizer.ILiveStreamPacketizer;
import com.wowza.wms.stream.livepacketizer.ILiveStreamPacketizerActionNotify;
import org.apache.log4j.Logger;
import com.wowza.wms.stream.MediaStreamActionNotifyBase;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.text.SimpleDateFormat;

public class CuePointsModule  extends ModuleBase  {

	private static final String OBJECT_TYPE_KEY = "objectType";
	private static final String OBJECT_TYPE_SYNCPOINT = "KalturaSyncPoint";
	private static final String OBJECT_TYPE_TIMECODE = "KalturaSyncTimecode";
	private static final String TIMESTAMP_KEY = "timestamp";
	private static final String ID_KEY = "id";
	private static final String META_DATA_TIMECODE= "onFI";


	private static final Logger logger = Logger.getLogger(CuePointsModule.class);
	private static final String PUBLIC_METADATA = "onMetaData";
	private final static int DEFAULT_SYNC_POINTS_INTERVAL_IN_MS = 8000;
	private final static String KALTURA_SYNC_POINTS_INTERVAL_PROPERTY = "KalturaSyncPointsInterval";
	private static final long START_SYNC_POINTS_DELAY = 0;
	private final ConcurrentHashMap<IMediaStream,CuePointManagerLiveStreamListener> streams;

	private LiveStreamPacketizerListener liveStreamPacketizerListener;
	private int syncPointsInterval;

	public CuePointsModule() {
		logger.debug("Creating a new instance of Modules.CuePointsManager");
		this.liveStreamPacketizerListener = new LiveStreamPacketizerListener();
		this.streams = new ConcurrentHashMap<IMediaStream,CuePointManagerLiveStreamListener>();
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
			}
			else {
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

			if (metaDataStr.equals(CuePointsModule.PUBLIC_METADATA) || metaDataStr.equals(CuePointsModule.META_DATA_TIMECODE)) {


				try {
					if (data == null) {
						return;
					}
					if (data.get("sd")!=null && data.get("st")!=null){	//Check if AMFdata containing Timecode
						String datestring=(String)data.get("sd").getValue();
						datestring+=" "+(String)data.get("st").getValue();
						SimpleDateFormat sdf;
						if (data.get("tz")!=null) {
							String timezone = (String) data.get("tz").getValue();
							datestring = datestring + " " + timezone;
							sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss.SSS XXX");
						}
						else {
							 sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss.SSS");
						}
						Date dateObj = sdf.parse(datestring);
						long UnixTime =dateObj.getTime();
						// Add following fields to the AMFDataObj
						data.put(OBJECT_TYPE_KEY, OBJECT_TYPE_TIMECODE);
						data.put(TIMESTAMP_KEY, UnixTime);
						data.put(ID_KEY, "null");
					}
					if (data.get(OBJECT_TYPE_KEY) != null) {
						String json = jsonAMF(data);
						logger.debug("Stream [" + streamName + "] JSON: " + json);

						ID3V2FrameBase frame;
						frame = new ID3V2FrameObject();
						((ID3V2FrameObject) frame).setText(json);
						id3Frames.putFrame(frame);
					}

				} catch (Exception e) {
					logger.error("Stream [" + streamName + "] failed to add sync points data to ID3Tag", e);
				}
			}
			else {
				logger.info("Stream [" + streamName + "] metadata [" + metaDataStr + "]");
				return;
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

	public void onAppStart(IApplicationInstance applicationInstance) {
		applicationInstance.addLiveStreamPacketizerListener(liveStreamPacketizerListener);
		syncPointsInterval = applicationInstance.getProperties().getPropertyInt(KALTURA_SYNC_POINTS_INTERVAL_PROPERTY, DEFAULT_SYNC_POINTS_INTERVAL_IN_MS);
		logger.info("Modules.CuePointsManager application instance started. Sync points interval: " + syncPointsInterval);
	}

	public void onStreamCreate(IMediaStream stream) {
		if(stream.getClientId() < 0){ //transcoded rendition
			return;
		}

		CuePointManagerLiveStreamListener listener=new CuePointManagerLiveStreamListener();
		streams.put(stream,listener);
		logger.debug("Modules.CuePointsManager::onStreamCreate with stream.getName() "+stream.getName()+" and stream.getClientId() "+stream.getClientId());
		stream.addClientListener(listener);
	}

	public void onStreamDestroy(IMediaStream stream)
	{
		logger.debug("Modules.CuePointsManager::onStreamDestroy with stream.getName() "+stream.getName()+" and stream.getClientId() "+stream.getClientId());

		CuePointManagerLiveStreamListener listener=streams.remove(stream);
		if (listener!=null) {
			listener.dispose();
			stream.removeClientListener(listener);
		}
	}

	class CuePointManagerLiveStreamListener extends MediaStreamActionNotifyBase  {

		private long runningId=0;
		private final Map<String,Timer> listenerStreams;
		public CuePointManagerLiveStreamListener() {
			logger.debug("Creating a new instance of CuePointManagerLiveStreamListener");
			this.listenerStreams = new ConcurrentHashMap<String,Timer>();
		}

		@Override
		public void onUnPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend) {
			Timer t;
			synchronized (listenerStreams) {
				t = listenerStreams.remove(streamName);
			}
			cancelScheduledTask(t,streamName);
		}

		public void cancelScheduledTask(Timer t,String streamName){
			if (t!=null) {
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
					cancelScheduledTask(t,streamName);
				}
				listenerStreams.clear();
			}
		}

		@Override
		public void onPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend) {

			logger.debug("Stream [" + streamName + "]");


			if(stream.getClientId() < 0){
				logger.debug("Stream [" + stream.getName() + "] entry [" + streamName + "] is a transcoded rendition");
				return;
			}

			logger.debug("Stream [" + streamName + "] entry [" + streamName + "]");

			Timer t;
			synchronized (listenerStreams) {
				if (listenerStreams.containsKey(streamName)) {
					logger.error("Stream with name " + streamName + " already exists in streams map, cancelling old one");
					t = listenerStreams.remove(streamName);
					cancelScheduledTask(t,streamName);
				}
				else {
					logger.debug("Stream with name " + streamName + " does not exist in streams map. creating a new map entry.");
				}
				t = new Timer();
				listenerStreams.put(streamName, t);

			}

			logger.debug("Running timer to create sync points for stream " + streamName);
			startSyncPoints(t, stream, streamName);
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
			String id = entryId+"_"+(runningId++);
			double currentTime = new Date().getTime();

			logger.debug("Sending sync point: entryId: " + entryId + " stream: " + stream.getName() + " id: " + id + " timestamp: " + currentTime );
			sendSyncPoint(stream, id, currentTime);
		}

		public void sendSyncPoint(final IMediaStream stream, String id, double time) {

			AMFDataObj data = new AMFDataObj();

			data.put(OBJECT_TYPE_KEY, OBJECT_TYPE_SYNCPOINT);
			data.put(ID_KEY, id);
			data.put(TIMESTAMP_KEY, time);

			//This condition is due to huge duration time (invalid) in the first chunk after stop-start on FMLE.
			//According to Wowza calling sendDirect() before stream contains any packets causes problems.
			if (stream.getPlayPackets().size() > 0) {
				stream.sendDirect(CuePointsModule.PUBLIC_METADATA, data);
			}
			logger.info("Sent sync-point: stream " + stream.getName() + "time: " + time  + "id: " + id);
		}


	}
}
