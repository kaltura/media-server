package com.kaltura.media.server.wowza;

import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.infra.StringUtils;
import com.kaltura.media.server.KalturaEventsManager;
import com.kaltura.media.server.KalturaServer;
import com.kaltura.media.server.events.IKalturaEvent;
import com.kaltura.media.server.events.KalturaEventType;
import com.kaltura.media.server.managers.ILiveManager;
import com.kaltura.media.server.managers.ILiveStreamManager;
import com.kaltura.media.server.managers.KalturaCuePointsManager;
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

import java.util.*;

public class CuePointsManager extends KalturaCuePointsManager implements ILiveManager.ILiveEntryReferrer {

	public static final String PUBLIC_METADATA = "onMetaData";
	protected final static int SYNC_POINTS_INTERVAL_IN_MS = 10000;

	protected final HashMap<IMediaStream, Timer> streams;
	protected LiveStreamPacketizerListener liveStreamPacketizerListener;

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
			return 1 + serializeStringLen(text);
		}

		public int serializeBody(byte bytes[], int pos){

			int length = 0;
			bytes[pos] = (byte)(textEncoding & 255);
			length += 1;

			if(text != null)
				length += serializeString(text, bytes, pos + length);

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

		private String jsonEscape(String string) {
			if (string == null || string.length() == 0) {
				return "";
			}

			char         c = 0;
			int          i;
			int          len = string.length();
			StringBuilder sb = new StringBuilder(len + 4);
			String       t;

			for (i = 0; i < len; i += 1) {
				c = string.charAt(i);
				switch (c) {
					case '\\':
					case '"':
						sb.append('\\');
						sb.append(c);
						break;
					case '/':
						sb.append('\\');
						sb.append(c);
						break;
					case '\b':
						sb.append("\\b");
						break;
					case '\t':
						sb.append("\\t");
						break;
					case '\n':
						sb.append("\\n");
						break;
					case '\f':
						sb.append("\\f");
						break;
					case '\r':
						sb.append("\\r");
						break;
					default:
						if (c < ' ') {
							t = "000" + Integer.toHexString(c);
							sb.append("\\u" + t.substring(t.length() - 4));
						} else {
							sb.append(c);
						}
				}
			}
			return sb.toString();
		}

		private String jsonAMF(AMFData data) {
			StringBuilder sb = new StringBuilder();
			int t = data.getType();

			if (t == AMFData.DATA_TYPE_STRING || t == AMFData.DATA_TYPE_DATE) {
				sb.append("\"").append(jsonEscape(((AMFDataItem) data).toString())).append("\"");
			}

			else if (t == AMFData.DATA_TYPE_NUMBER || t == AMFData.DATA_TYPE_BOOLEAN) {
				sb.append(data);
			}

			else if (t == AMFData.DATA_TYPE_UNDEFINED || t == AMFData.DATA_TYPE_UNKNOWN || t == AMFData.DATA_TYPE_NULL) {
				sb.append("null");
			}

			// Mixed Array is not a perfect match:
			// JSON only has string keys, whereas this allows integer keys as
			// well as strings
			else if (t == AMFData.DATA_TYPE_OBJECT || t == AMFData.DATA_TYPE_MIXED_ARRAY) {
				AMFDataObj o = (AMFDataObj) data;
				sb.append("{");
				@SuppressWarnings("rawtypes")
				Iterator i = o.getKeys().iterator();
				boolean notfirst = false;
				while (i.hasNext()) {
					if (notfirst) {
						sb.append(",");
					} else {
						notfirst = true;
					}
					String k = (String) i.next();
					sb.append("\"").append(jsonEscape(k)).append("\":");
					sb.append(jsonAMF(o.get(k)));
				}
				sb.append("}");
			}

			else if (t == AMFData.DATA_TYPE_ARRAY) {
				AMFDataArray o = (AMFDataArray) data;
				sb.append("[");
				int i = 1, z = o.size();
				if (z > 0) {
					sb.append(jsonAMF(o.get(0)));
				}
				for (; i < z; i++) {
					sb.append(",");
					sb.append(jsonAMF(o.get(i)));
				}
				sb.append("]");
			}

			else {
				logger.error("Invalid amf data for json encode! (" + t + ")");
				return "null /* Invalid amf data for json encode! (" + t + ") */";
			}

			return sb.toString();
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
			AMFData data = amfList.getObject(1);

			if (!metaDataStr.equals(CuePointsManager.PUBLIC_METADATA)) {
				logger.info("Stream [" + streamName + "] metadata [" + metaDataStr + "]");
				return;
			}

			String json = jsonAMF(data);
			//escape not ascii chars that are not in 0x20-0x7F
			json = json.replaceAll("[^\\x20-\\x7F]", "");
			logger.debug("Stream [" + streamName + "] JSON after char removal:\n" + json);

			//TODO, not used
//			String hashedString = new String(Base64.encodeBase64(json.getBytes()));
//			logger.info("Stream [" + streamName + "] Hashed: " + hashedString);

			ID3V2FrameBase frame;
			frame = new ID3V2FrameObject();
			((ID3V2FrameObject) frame).setText(json);
			id3Frames.putFrame(frame);
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

		KalturaEventsManager.registerEventConsumer(this, KalturaEventType.STREAM_PUBLISHED, KalturaMediaEventType.APPLICATION_INSTANCE_STARTED);
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
			if (!streams.containsKey(mediaStream)) {
				logger.error("Unpublishing a stream that does not exist in streams map: " + streamName);
				return;
			}
			t = streams.remove(mediaStream);
		}
		logger.debug("Stopping CuePoints timer for stream " + streamName);
		t.cancel();
		t.purge();

		ILiveStreamManager liveManager = KalturaServer.getManager(ILiveStreamManager.class);
		liveManager.removeReferrer(entryId, this);

	}

	protected void onAppStart(IApplicationInstance applicationInstance) {
		logger.debug("Application instance started, adding live stream packetizer listener");
		applicationInstance.addLiveStreamPacketizerListener(liveStreamPacketizerListener);
	}

	protected void onPublish(final IMediaStream stream, final String entryId) {
		if(stream.getClientId() < 0){
			logger.debug("Stream [" + stream.getName() + "] entry [" + entryId + "] is a transcoded rendition");
			return;
		}

		String streamName = stream.getName();
		logger.debug("Stream [" + streamName + "] entry [" + entryId + "]");
		ILiveStreamManager liveManager = KalturaServer.getManager(ILiveStreamManager.class);
		liveManager.addReferrer(entryId, this);

		final Timer t;
		synchronized (streams) {
			if (streams.containsKey(stream)) {
				logger.debug("Stream with name " + streamName + " already exists in streams map");
				t = streams.get(stream);
			}
			else {
				logger.debug("Stream with name " + streamName + " does not exist in streams map. creating a new map entry.");
				t = new Timer();
				streams.put(stream, t);
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
					createSyncPoint(stream,entryId);
				} catch (Exception e) {
					logger.error("Error occured while running sync points timer", e);
				}
			}
		};
		try {
			t.schedule(tt,0,SYNC_POINTS_INTERVAL_IN_MS);
		} catch (Exception e) {
			logger.error("Error occurred while scheduling a timer task",e);
		}
	}

	private void createSyncPoint(IMediaStream stream, String entryId) {
		logger.debug("createSyncPoint. entryId:"+entryId);
		KalturaLiveEntry liveEntry = liveManager.get(entryId);
		String id = StringUtils.getUniqueId();
		double offset = liveEntry.duration + stream.getElapsedTime().getTimeSeconds();

		try{
			sendSyncPoint(stream,entryId, id, offset);
		} catch (KalturaManagerException e) {
			logger.error("Failed sending sync-point [" + id + "] to entry [" + entryId + "]: " + e.getMessage());
		}
	}

	public void sendSyncPoint(IMediaStream stream, String entryId, String id, double offset) throws KalturaManagerException {
		logger.debug("sendSyncPoint: entryId: " + entryId + " id: " + id + " offest: " + offset);

		Date date = new Date();
		AMFDataObj data = new AMFDataObj();
		data.put("objectType", "KalturaSyncPoint");
		data.put("id", id);
		data.put("offset", offset * 1000);
		data.put("timestamp", date.getTime());

		stream.sendDirect(CuePointsManager.PUBLIC_METADATA, data);
		((MediaStream)stream).processSendDirectMessages();
		logger.info("Sent sync-point [" + id + "] to entry [" + entryId + "] stream [" + stream.getName() + "] offset [" + offset + "]");

		//TODO need to override the data?
//		TimerTask task = new TimerTask() {
//
//			@Override
//			public void run() {
//				AMFDataObj clear = new AMFDataObj();
//				clear.put("objectType", "");
//
//				for (IMediaStream stream : entryStreams) {
//					stream.sendDirect(CuePointsManager.PUBLIC_METADATA, clear);
//					((MediaStream)stream).processSendDirectMessages();
////					logger.debug("clearing sync point. stream name:" + stream.getName());
//				}
//			}
//		};
//
//		Timer timer = new Timer("sendSyncPoint-" + entryId, true);
//		timer.schedule(task, 1000);
	}
}
