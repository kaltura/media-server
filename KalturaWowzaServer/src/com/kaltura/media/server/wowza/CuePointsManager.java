package com.kaltura.media.server.wowza;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.kaltura.client.KalturaParams;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.client.types.KalturaSyncPoint;
import com.kaltura.media.server.KalturaEventsManager;
import com.kaltura.media.server.events.IKalturaEvent;
import com.kaltura.media.server.managers.KalturaCuePointsManager;
import com.kaltura.media.server.managers.KalturaManagerException;
import com.kaltura.media.server.wowza.events.KalturaApplicationInstanceEvent;
import com.kaltura.media.server.wowza.events.KalturaMediaEventType;
import com.kaltura.media.server.wowza.events.KalturaMediaStreamEvent;
import com.wowza.wms.amf.AMFData;
import com.wowza.wms.amf.AMFDataArray;
import com.wowza.wms.amf.AMFDataItem;
import com.wowza.wms.amf.AMFDataList;
import com.wowza.wms.amf.AMFDataObj;
import com.wowza.wms.amf.AMFPacket;
import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.CupertinoPacketHolder;
import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.IHTTPStreamerCupertinoLivePacketizerDataHandler;
import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.LiveStreamPacketizerCupertino;
import com.wowza.wms.media.mp3.model.idtags.ID3Frames;
import com.wowza.wms.media.mp3.model.idtags.ID3V2FrameBase;
import com.wowza.wms.media.mp3.model.idtags.ID3V2FrameTextInformation;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.stream.livepacketizer.ILiveStreamPacketizer;
import com.wowza.wms.stream.livepacketizer.ILiveStreamPacketizerActionNotify;

public class CuePointsManager extends KalturaCuePointsManager {

	protected ConcurrentHashMap<String, IMediaStream> streams = new ConcurrentHashMap<String, IMediaStream>();

	class LiveStreamPacketizerDataHandler implements IHTTPStreamerCupertinoLivePacketizerDataHandler {
		String streamName = null;

		public LiveStreamPacketizerDataHandler(String streamName) {
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
			StringBuffer sb = new StringBuffer();
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

			if (!metaDataStr.equalsIgnoreCase("onTextData")) {
				logger.info("Stream [" + streamName + "] metadata [" + metaDataStr + "]");
				return;
			}

			String json = jsonAMF(data);
			logger.info("Stream [" + streamName + "] packet sent:\n" + json);

			ID3V2FrameTextInformation comment = new ID3V2FrameTextInformation(ID3V2FrameBase.TAG_COMM);
			comment.setTextEncoding(ID3V2FrameBase.TEXTENCODING_UTF8);
			comment.setValue(json);
			id3Frames.putFrame(comment);
		}
	}

	class LiveStreamPacketizerListener implements ILiveStreamPacketizerActionNotify {

		public void onLiveStreamPacketizerCreate(ILiveStreamPacketizer liveStreamPacketizer, String streamName) {
			logger.info("Create [" + streamName + "]");
			if (liveStreamPacketizer instanceof LiveStreamPacketizerCupertino) {
				((LiveStreamPacketizerCupertino) liveStreamPacketizer).setDataHandler(new LiveStreamPacketizerDataHandler(streamName));
			}
		}

		public void onLiveStreamPacketizerDestroy(ILiveStreamPacketizer liveStreamPacketizer) {
		}

		public void onLiveStreamPacketizerInit(ILiveStreamPacketizer liveStreamPacketizer, String streamName) {
		}
	}

	@Override
	public void init() throws KalturaManagerException {
		super.init();

		KalturaEventsManager.registerEventConsumer(this, KalturaMediaEventType.MEDIA_STREAM_PUBLISHED);
	}

	@Override
	public void onEvent(IKalturaEvent event){
		
		if(event.getType() instanceof KalturaMediaEventType){
			
			switch((KalturaMediaEventType) event.getType())
			{
				case MEDIA_STREAM_PUBLISHED:
					KalturaMediaStreamEvent streamEvent = (KalturaMediaStreamEvent) event;
					onPublish(streamEvent.getMediaStream(), streamEvent.getEntryId());
					break;
	
				case APPLICATION_INSTANCE_STARTED:
					KalturaApplicationInstanceEvent applicationInstanceEvent = (KalturaApplicationInstanceEvent) event;
					onAppStart(applicationInstanceEvent.getApplicationInstance());
					break;
					
				default:
					break;
			}
		}
		
		super.onEvent(event);
	}
	
	protected void onAppStart(IApplicationInstance applicationInstance) {
		applicationInstance.addLiveStreamPacketizerListener(new LiveStreamPacketizerListener());
	}

	protected void onPublish(IMediaStream stream, String entryId) {
		logger.debug("Stream [" + stream.getName() + "] entry [" + entryId + "]");
		synchronized (streams) {
			streams.put(entryId, stream);
		}
	}

	@Override
	protected void onUnPublish(String entryId) {
		synchronized (streams) {
			streams.remove(entryId);
		}
		super.onUnPublish(entryId);
	}

	@Override
	public float getEntryCurrentTime(KalturaLiveEntry liveEntry) throws KalturaManagerException {
		IMediaStream stream;
		synchronized (streams) {
			if(!streams.containsKey(liveEntry.id))
				throw new KalturaManagerException("Entry media stream not found");
			
			stream = streams.get(liveEntry.id);
		}
		
		return (float) (liveEntry.msDuration + stream.getElapsedTime().getTimeSeconds());
	}

	@Override
	public void sendSyncPoint(String entryId, KalturaSyncPoint syncPoint) throws KalturaManagerException {
		IMediaStream stream;
		synchronized (streams) {
			if(!streams.containsKey(entryId))
				throw new KalturaManagerException("Entry media stream not found");
			
			stream = streams.get(entryId);
		}
		
		AMFDataObj data = new AMFDataObj();
		KalturaParams params = syncPoint.toParams();
		for (Map.Entry<String, String> param : params.entrySet()) {
			data.put(param.getKey(), param.getValue());
		}
		
		stream.sendDirect("onTextData", data);
		logger.info("Sent sync-point [" + syncPoint.id + "] to entry [" + entryId + "]");
	}
}
