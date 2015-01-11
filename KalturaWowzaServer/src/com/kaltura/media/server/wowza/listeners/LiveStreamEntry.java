package com.kaltura.media.server.wowza.listeners;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.enums.KalturaDVRStatus;
import com.kaltura.client.enums.KalturaMediaServerIndex;
import com.kaltura.client.types.KalturaLiveAsset;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.client.types.KalturaLiveStreamEntry;
import com.kaltura.media.server.KalturaEventsManager;
import com.kaltura.media.server.KalturaServer;
import com.kaltura.media.server.events.IKalturaEvent;
import com.kaltura.media.server.events.KalturaEventType;
import com.kaltura.media.server.events.KalturaMetadataEvent;
import com.kaltura.media.server.events.KalturaStreamEvent;
import com.kaltura.media.server.managers.ILiveStreamManager;
import com.kaltura.media.server.wowza.LiveStreamManager;
import com.kaltura.media.server.wowza.SmilManager;
import com.kaltura.media.server.wowza.events.KalturaApplicationInstanceEvent;
import com.kaltura.media.server.wowza.events.KalturaMediaEventType;
import com.kaltura.media.server.wowza.events.KalturaMediaStreamEvent;
import com.wowza.wms.amf.AMFData;
import com.wowza.wms.amf.AMFDataItem;
import com.wowza.wms.amf.AMFDataList;
import com.wowza.wms.amf.AMFDataMixedArray;
import com.wowza.wms.amf.AMFDataObj;
import com.wowza.wms.amf.AMFPacket;
import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.application.WMSProperties;
import com.wowza.wms.client.IClient;
import com.wowza.wms.dvr.DvrApplicationContext;
import com.wowza.wms.dvr.IDvrConstants;
import com.wowza.wms.httpstreamer.model.IHTTPStreamerSession;
import com.wowza.wms.medialist.MediaListRendition;
import com.wowza.wms.module.ModuleBase;
import com.wowza.wms.request.RequestFunction;
import com.wowza.wms.rtp.model.RTPSession;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.stream.IMediaStreamCallback;
import com.wowza.wms.stream.MediaStream;
import com.wowza.wms.stream.MediaStreamActionNotifyBase;
import com.wowza.wms.stream.livedvr.ILiveStreamDvrRecorderControl;
import com.wowza.wms.stream.livepacketizer.ILiveStreamPacketizerControl;
import com.wowza.wms.stream.livetranscoder.ILiveStreamTranscoder;
import com.wowza.wms.stream.livetranscoder.ILiveStreamTranscoderControl;
import com.wowza.wms.stream.livetranscoder.LiveStreamTranscoderNotifyBase;
import com.wowza.wms.stream.publish.Publisher;
import com.wowza.wms.stream.publish.Stream;
import com.wowza.wms.transcoder.model.LiveStreamTranscoder;
import com.wowza.wms.transcoder.model.LiveStreamTranscoderActionNotifyBase;
import com.wowza.wms.transcoder.model.TranscoderStreamNameGroup;
import com.wowza.wms.transcoder.model.TranscoderStreamNameGroupMember;

public class LiveStreamEntry extends ModuleBase {

	protected final static String REQUEST_PROPERTY_PARTNER_ID = "p";
	protected final static String REQUEST_PROPERTY_ENTRY_ID = "e";
	protected final static String REQUEST_PROPERTY_SERVER_INDEX = "i";
	protected final static String REQUEST_PROPERTY_TOKEN = "t";
	protected final static String REQUEST_PROPERTY_FLAVOR_IDS = "flavorIds";

	protected final static String CLIENT_PROPERTY_CONNECT_APP = "connectapp";
	protected final static String CLIENT_PROPERTY_PARTNER_ID = "partnerId";
	protected final static String CLIENT_PROPERTY_SERVER_INDEX = "serverIndex";
	protected final static String CLIENT_PROPERTY_ENTRY_ID = "entryId";
	protected final static String STREAM_PROPERTY_SUFFIX = "suffix";
	protected final static String APPLICATION_MANAGERS_PROPERTY_NAME = "ApplicationManagers";

	protected final static int INVALID_SERVER_INDEX = -1;

	protected static Logger logger = Logger.getLogger(LiveStreamEntry.class);

	private String applicationName;
	private LiveStreamManager liveStreamManager;
	private DvrRecorderControl dvrRecorderControl = new DvrRecorderControl();
	private LiveStreamListener liveStreamListener = new LiveStreamListener();
	private LiveStreamTranscoderListener liveStreamTranscoderListener = new LiveStreamTranscoderListener();
	private LiveStreamTranscoderActionListener liveStreamTranscoderActionListener = new LiveStreamTranscoderActionListener();
	private LiveStreamCallbackListener liveStreamCallbackListener = new LiveStreamCallbackListener (); 
	private IApplicationInstance appInstance;
	private Map<String, Map<String, Stream>> restreams = new HashMap<String, Map<String, Stream>>();

	@SuppressWarnings("serial")
	private class ClientConnectException extends Exception{

		public ClientConnectException(String message) {
			super(message);
		}
	}
	
	private class DvrRecorderControl implements ILiveStreamDvrRecorderControl, ILiveStreamPacketizerControl {

		@Override
		public boolean shouldDvrRecord(String recorderName, IMediaStream stream) {
			return this.isThatStreamNeeded(stream);
		}

		private boolean isThatStreamNeeded(IMediaStream stream) {
			String streamName = stream.getName();
			IApplicationInstance appInstance = stream.getStreams().getAppInstance();
			DvrApplicationContext ctx = appInstance.getDvrApplicationContext();

			if (!stream.isTranscodeResult() && !stream.isPublisherStream()) {
				logger.info("Stream [" + streamName + "] is input stream");
				return false;
			}

			String entryId = getEntryIdFromStreamName(streamName);
			
			if(entryId == null){
				logger.info("Stream [" + streamName + "] does not match entry id");
				return false;
			}

			KalturaLiveStreamEntry liveStreamEntry = liveStreamManager.get(entryId);

			if (liveStreamEntry == null) {
				logger.debug("Stream [" + streamName + "] entry [" + entryId + "] not found");
				return false;
			}

			if (liveStreamEntry.dvrStatus != KalturaDVRStatus.ENABLED) {
				logger.debug("Stream [" + streamName + "] DVR disabled");
				return false;
			}

			int dvrWindow = liveStreamManager.getDvrWindow(liveStreamEntry);
			logger.debug("Stream [" + streamName + "] DVR window [" + dvrWindow + "]");

			ctx.setWindowDuration(dvrWindow);
			ctx.setArchiveStrategy(IDvrConstants.ARCHIVE_STRATEGY_DELETE);

			return true;
		}

		public boolean isLiveStreamPacketize(String packetizer, IMediaStream stream) {
			logger.debug("Packetizer [" + packetizer + ", " + stream.getName() + "]");

			if(!stream.isTranscodeResult() && !stream.isPublisherStream()){
				logger.debug("Stream [" + stream.getName() + "] is input stream");
				return false;
			}
			
			if (packetizer.compareTo("dvrstreamingpacketizer") == 0) {
				return this.isThatStreamNeeded(stream);
			}

			return true;
		}
	}

	class TranscoderControl implements ILiveStreamTranscoderControl {
		public boolean isLiveStreamTranscode(String transcoder, IMediaStream stream) {
			if (stream.getName().endsWith("_publish")) {
				logger.debug("Stream [" + stream.getName() + "] republished, no transcoding needed");
				return false;
			}

			if (stream.isTranscodeResult()) {
				logger.debug("Stream [" + stream.getName() + "] transcoded, no transcoding needed");
				return false;
			}

			logger.debug("Stream [" + stream.getName() + "] requires transcode");
			return true;
		}
	}

	class LiveStreamListener extends MediaStreamActionNotifyBase {

		public void onPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend) {

			logger.debug("Stream [" + streamName + "]");
			
			WMSProperties properties = getConnectionProperties(stream);
			int assetParamsId = Integer.MIN_VALUE;
			if (properties != null) { // streamed from the client

				logger.debug("Client IP: " + getStreamIp(stream));

				String entryId = properties.getPropertyStr(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID);
				KalturaMediaServerIndex serverIndex = KalturaMediaServerIndex.get(properties.getPropertyInt(LiveStreamEntry.CLIENT_PROPERTY_SERVER_INDEX, LiveStreamEntry.INVALID_SERVER_INDEX));

				KalturaLiveEntry entry = liveStreamManager.get(entryId);
				if (entry == null) {
					logger.debug("Unplanned disconnect occured earlier. Attempting reconnect.");
					entry = onClientConnect(stream);
					if (entry == null) {
						logger.error("Following reconnection attempt, client still not authenticated for stream [" + streamName + "]");
						return;
					}
				}

				synchronized (liveStreamManager.disconnectingTimers) {
					if (liveStreamManager.disconnectingTimers.containsKey(entryId)) {
						logger.info("This entry is scheduled for disconnect. Cancelling the diconnect.");
						Timer disconnectTimer = liveStreamManager.disconnectingTimers.get(entryId);
						disconnectTimer.cancel();
						disconnectTimer.purge();
						liveStreamManager.disconnectingTimers.remove(entryId);
					}
				}

				logger.debug("Publish: " + entryId);

				if (!entryId.equals(streamName)) {

					Matcher matcher = getStreamNameMatches(streamName);
					if (matcher == null) {
						logger.error("Unknown published stream [" + streamName + "]");
						return;
					}

					if (!entryId.equals(matcher.group(1))) {
						logger.error("Published stream stream name [" + streamName + "] does not match entry id [" + entryId + "]");
						return;
					}

					String streamSuffix = matcher.group(2);
					KalturaLiveAsset liveAsset = liveStreamManager.getLiveAsset(entry.id, streamSuffix);
					if (liveAsset != null) {
						assetParamsId = liveAsset.flavorParamsId;
					}
				}

				KalturaMediaStreamEvent event = new KalturaMediaStreamEvent(KalturaEventType.STREAM_PUBLISHED, entry, serverIndex, applicationName, stream, assetParamsId);
				KalturaEventsManager.raiseEvent(event);
			} else if (stream.isTranscodeResult()) {

				Matcher matcher = getStreamNameMatches(streamName);
				if (matcher == null) {
					logger.error("Transcoder published stream [" + streamName + "] does not match entry regex");
					return;
				}

				String entryId = matcher.group(1);

				synchronized (restreams) {
					if(restreams.containsKey(entryId)){
						Map<String, Stream> entryStreams = restreams.get(entryId);
						if(entryStreams.containsKey(streamName)){
							restream(entryStreams.get(streamName), streamName);
						}
					}
				}
				
				assetParamsId = Integer.parseInt(matcher.group(2));
				logger.debug("Stream [" + streamName + "] entry [" + entryId + "] asset params id [" + assetParamsId + "]");

				for (IMediaStream mediaStream : stream.getStreams().getStreams()) {
					properties = getConnectionProperties(mediaStream);
					if (properties != null && mediaStream.getName().startsWith(entryId)) {
						logger.debug("Source stream [" + mediaStream.getName() + "] found for stream [" + streamName + "]");
						break;
					}
				}

				KalturaMediaServerIndex serverIndex = KalturaMediaServerIndex.get(properties.getPropertyInt(LiveStreamEntry.CLIENT_PROPERTY_SERVER_INDEX, LiveStreamEntry.INVALID_SERVER_INDEX));

				KalturaMediaStreamEvent event = new KalturaMediaStreamEvent(KalturaMediaEventType.MEDIA_STREAM_PUBLISHED, liveStreamManager.get(entryId), serverIndex, applicationName, stream, assetParamsId);
				KalturaEventsManager.raiseEvent(event);
			}
		}

		public void onUnPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend) {
			if (stream.isTranscodeResult() || stream.isPublisherStream())
				return;

			WMSProperties clientProperties = getConnectionProperties(stream);
			if (!clientProperties.containsKey(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID))
				return;

			KalturaLiveStreamEntry liveStreamEntry = liveStreamManager.get(clientProperties.getPropertyStr(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID));
			KalturaMediaServerIndex serverIndex = KalturaMediaServerIndex.get(clientProperties.getPropertyInt(LiveStreamEntry.CLIENT_PROPERTY_SERVER_INDEX, LiveStreamEntry.INVALID_SERVER_INDEX));

			logger.debug("UnPublish: " + liveStreamEntry.id);

			KalturaMediaStreamEvent event = new KalturaMediaStreamEvent(KalturaEventType.STREAM_UNPUBLISHED, liveStreamEntry, serverIndex, applicationName, stream);
			KalturaEventsManager.raiseEvent(event);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onMetaData(IMediaStream stream, AMFPacket packet) {
			if (stream.getClientId() < 0) {
				return;
			}

			if (stream.isTranscodeResult() || stream.isPublisherStream()) {
				return;
			}

			WMSProperties properties = getConnectionProperties(stream);
			if (!properties.containsKey(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID)) {
				return;
			}

			String entryId = properties.getPropertyStr(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID);
			KalturaMediaServerIndex serverIndex = KalturaMediaServerIndex.get(properties.getPropertyInt(LiveStreamEntry.CLIENT_PROPERTY_SERVER_INDEX, LiveStreamEntry.INVALID_SERVER_INDEX));

			KalturaLiveEntry entry = liveStreamManager.get(entryId);
			if (entry == null) {
				return;
			}

			byte[] buffer = packet.getData();
			if (buffer == null) {
				return;
			}

			if (packet.getSize() <= 2) {
				return;
			}

			int offset = 0;
			if (buffer[0] == 0)
				offset++;

			AMFDataList amfList = new AMFDataList(buffer, offset, buffer.length - offset);

			if (amfList.size() <= 1) {
				return;
			}

			if (amfList.get(0).getType() != AMFData.DATA_TYPE_STRING) {
				return;
			}

			if (amfList.get(1).getType() != AMFData.DATA_TYPE_OBJECT) {
				return;
			}

			String method = amfList.getString(0);
			AMFDataObj amfData = (AMFDataObj) amfList.getObject(1);

			if (!amfData.containsKey("objectType")) {
				return;
			}

			String objectType = amfData.getString("objectType");
			logger.info("Stream [" + stream.getName() + "] metadata [" + method + "] object type [" + objectType + "]");
			String objectClassName = "com.kaltura.client.types." + objectType;
			Class<KalturaObjectBase> clazz;
			KalturaObjectBase object;
			try {
				clazz = (Class<KalturaObjectBase>) Class.forName(objectClassName);
				object = clazz.newInstance();
			} catch (ClassNotFoundException e) {
				logger.info("Stream [" + stream.getName() + "] metadata [" + method + "] class [" + objectClassName + "] not found");
				return;
			} catch (InstantiationException | IllegalAccessException e) {
				logger.info("Stream [" + stream.getName() + "] metadata [" + method + "] class [" + objectClassName + "] failed to initialize: " + e.getMessage());
				return;
			}

			@SuppressWarnings("rawtypes")
			Iterator i = amfData.getKeys().iterator();
			Field field;
			while (i.hasNext()) {
				String attributeName = (String) i.next();
				field = null;
				try {
					field = clazz.getField(attributeName);
				} catch (NoSuchFieldException e) {
					logger.info("Stream [" + stream.getName() + "] metadata [" + method + "] class [" + objectClassName + "] field [" + attributeName + "] not found");
				} catch (SecurityException e) {
					logger.info("Stream [" + stream.getName() + "] metadata [" + method + "] class [" + objectClassName + "] field [" + attributeName + "]: " + e.getMessage());
				}
				if (field == null) {
					continue;
				}

				try {
					field.set(object, amfData.get(attributeName));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					logger.info("Stream [" + stream.getName() + "] metadata [" + method + "] class [" + objectClassName + "] set field [" + attributeName + "]: " + e.getMessage());
				}
			}

			IKalturaEvent event = new KalturaMetadataEvent(KalturaEventType.METADATA, entry, serverIndex, object);
			KalturaEventsManager.raiseEvent(event);
		}
	}

	class LiveStreamTranscoderActionListener extends LiveStreamTranscoderActionNotifyBase {

		@Override
		public void onRegisterStreamNameGroup(LiveStreamTranscoder liveStreamTranscoder, final TranscoderStreamNameGroup streamNameGroup) {

			final IApplicationInstance appInstance = liveStreamTranscoder.getAppInstance();
			final String sourceGroupName = streamNameGroup.getStreamName();

			Matcher matcher = getGroupNameMatches(sourceGroupName);
			if (matcher == null) {
				logger.info("Group name [" + sourceGroupName + "] does not match group name regex");
				return;
			}

			final String entryId = matcher.group(1);
			final String tag = matcher.group(3);
			
			final String destGroupName = entryId + "_" + tag;
			final String appName = appInstance.getContextStr();
			
			logger.debug("Group [" + appName + "/" + destGroupName + "] for group name [" + streamNameGroup.getStreamName() + "]");

			IMediaStream stream = liveStreamTranscoder.getStream();
			if (stream == null) {
				logger.error("Group [" + appName + "/" + destGroupName + "] source stream not found");
				return;
			}

			WMSProperties properties = getConnectionProperties(stream);
			if (properties == null) {
				logger.error("Group [" + appName + "/" + destGroupName + "] properties not found");
				return;
			}

			if (!properties.containsKey(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID)) {
				logger.error("Group [" + appName + "/" + destGroupName + "] entry id not defined");
				return;
			}
			if (!entryId.equals(properties.getPropertyStr(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID))) {
				logger.error("Group [" + appName + "/" + destGroupName + "] entry id does not match group name");
				return;
			}

			TimerTask asyncTask = new TimerTask() {

				@Override
				public void run() {
					logger.debug("Group [" + appName + "/" + destGroupName + "] for group name [" + streamNameGroup.getStreamName() + "] tag [" + tag + "] entry id [" + entryId + "]");
					
					if(tag.equals("all") && liveStreamManager.shouldSync(entryId)){
						logger.debug("Group [" + appName + "/" + destGroupName + "] for group name [" + streamNameGroup.getStreamName() + "] restreaming");
						restreamGroup(entryId, streamNameGroup.getMembers());
					}
					//Entry required to get the value of the transcoding profile id
					KalturaLiveEntry entry = liveStreamManager.get(entryId);
					
					SmilManager.generate(appInstance, entryId, destGroupName, sourceGroupName, entry.conversionProfileId);
				}
			};

			Timer timer = new Timer("generateSmil-" + sourceGroupName, true);
			timer.schedule(asyncTask, 1);
		}
		
		public void onUnregisterStreamNameGroup(LiveStreamTranscoder liveStreamTranscoder, TranscoderStreamNameGroup streamNameGroup) {
			final IApplicationInstance appInstance = liveStreamTranscoder.getAppInstance();
			final String sourceGroupName = streamNameGroup.getStreamName();

			Matcher matcher = getGroupNameMatches(sourceGroupName);
			if (matcher == null) {
				logger.info("Group name [" + sourceGroupName + "] does not match group name regex");
				return;
			}

			final String entryId = matcher.group(1);
			final String tag = matcher.group(3);

			final String destGroupName = entryId + "_" + tag;
			final String appName = appInstance.getContextStr();
			
			if(tag.equals("all") && liveStreamManager.shouldSync(entryId)){
				logger.debug("Group [" + appName + "/" + destGroupName + "] for group name [" + streamNameGroup.getStreamName() + "] unRestreaming");
				unRestreamGroup(entryId);
			}
		}

		private void unRestreamGroup(String entryId) {
			
			logger.debug("Unpublish restreamed entry [" + entryId + "]");
			synchronized (restreams) {
				if(restreams.containsKey(entryId)){
					Map<String, Stream> entryStreams = restreams.remove(entryId);
					for(Stream stream : entryStreams.values()){
						stream.close();
					}
					SmilManager.removeGroupSmils(entryId, "publish");
				}
			}
		}

		private void restreamGroup(String entryId, List<TranscoderStreamNameGroupMember> groupMembers) {

			logger.info("Restreaming [" + entryId + "] members count [" + groupMembers.size() + "]");

			Map<String, Long> bitrates = new HashMap<String, Long>();

			synchronized (restreams) {
				Map<String, Stream> entryStreams;
				if(restreams.containsKey(entryId)){
					entryStreams = restreams.remove(entryId);
					for(Stream stream : entryStreams.values()){
						stream.close();
					}
				}
				entryStreams = new HashMap<String, Stream>();
				restreams.put(entryId, entryStreams);

				long bitrate;
				Stream stream;
				String streamName;
				String reStreamName;
				IMediaStream originalStream;
				MediaListRendition rendition;
				
				for(TranscoderStreamNameGroupMember groupMember : groupMembers){
					rendition = groupMember.getMediaListRendition();
					if(rendition == null){
						logger.info("Rendition [" + groupMember.getName() + "] not found for entry [" + entryId + "]");
						continue;
					}
					
					streamName = rendition.getName().replace("mp4:", "");
					reStreamName = streamName + "_publish";
					stream = Stream.createInstance(appInstance, reStreamName);
					entryStreams.put(streamName, stream);
					
					originalStream = appInstance.getStreams().getStream(streamName);
					if(originalStream != null && originalStream.isPlaying()){
						restream(stream, streamName);
					}
					else{
						logger.debug("Restreamed stream [" + streamName + "] is not published yet");
					}
					
					bitrate = groupMember.getMediaListRendition().getBitrateTotal();
					logger.info("Rendition [" + reStreamName + "] bitrate [" + bitrate + "]");
					bitrates.put(reStreamName, bitrate);
				}
			}

			KalturaLiveEntry entry = liveStreamManager.get(entryId);
			SmilManager.generate(appInstance, entryId, entryId + "_publish", bitrates, entry.conversionProfileId);
		}
	}

	class LiveStreamTranscoderListener extends LiveStreamTranscoderNotifyBase {

		@Override
		public void onLiveStreamTranscoderCreate(ILiveStreamTranscoder liveStreamTranscoder, IMediaStream mediaStream) {
			((LiveStreamTranscoder) liveStreamTranscoder).addActionListener(liveStreamTranscoderActionListener);
		}
	}

	class LiveStreamCallbackListener implements IMediaStreamCallback
	{

		@Override
		public void onCallback(IMediaStream arg0, RequestFunction arg1,
				AMFDataList arg2) {
			logger.debug("LiveStreamEntry::onCallback: stream name ["+ arg0.getName() + "] request func: [" + arg1.toString() + "] AMFDataList [" + arg2.toString() + "]");
			IClient client = arg0.getClient();
			
			
			if (client != null) {
				logger.debug("Encoder info: " + client.getFlashVer());
				WMSProperties clientProperties = client.getProperties();
				if (!clientProperties.containsKey(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID)){
					logger.error("Client does not contain entryId information. Captions will not be read");
					return;
				}
				
			}
		}
	}
	
	private void restream(Stream stream, String inputStreamName) {
		logger.debug("Restream [" + stream.getName() + "] from [" + inputStreamName + "]");
		Publisher publisher = stream.getPublisher();
		byte bytes[] = new byte[] {};
		Date date = new Date();
		publisher.addVideoData(bytes, 0, date.getTime());
		stream.play(inputStreamName, -2, -1, false);
	}

	private Matcher getMatches(String streamName, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(streamName);
		if (!matcher.find()) {
			logger.info("Stream [" + streamName + "] does not match regex");
			return null;
		}
		
		return matcher;
	}

	private Matcher getStreamNameMatches(String streamName) {
		return getMatches(streamName, "^([01]_[\\d\\w]{8})_(.+)$");
	}

	private Matcher getGroupNameMatches(String groupName) {
		return getMatches(groupName, "^([01]_[\\d\\w]{8})_([^_]+)_([^_]+)$");
	}

	private String getEntryIdFromStreamName(String streamName) {
		Matcher matcher = getStreamNameMatches(streamName);
		if (matcher == null) {
			return null;
		}

		return matcher.group(1);
	}

	private WMSProperties getConnectionProperties(IMediaStream stream) {
		WMSProperties properties = null;

		if (stream.getClient() != null) {
			properties = stream.getClient().getProperties();
		}
		else if(stream.getRTPStream() != null && stream.getRTPStream().getSession() != null){
			properties = stream.getRTPStream().getSession().getProperties();
		}
		else {
			return null;
		}

		if (properties.containsKey(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID)) {
			return properties;
		}

		if (onClientConnect(stream) != null) {
			return properties;
		}

		return null;
	}

	private String getStreamIp(IMediaStream stream) {
		if (stream.getClient() != null) {
			return stream.getClient().getIp().toString();
		}
		else if(stream.getRTPStream() != null && stream.getRTPStream().getSession() != null){
			return stream.getRTPStream().getRTSPOriginIpAddress();
		}

		return "unknown";
	}

	public void onStreamCreate(IMediaStream stream) {
		logger.debug("LiveStreamEntry::onStreamCreate");
		stream.addClientListener(liveStreamListener);
		stream.addCalbackListener(liveStreamCallbackListener);
	}

	public void onDisconnect(IClient client) {
		WMSProperties clientProperties = client.getProperties();
		if (clientProperties.containsKey(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID)) {
			String entryId = clientProperties.getPropertyStr(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID);
			logger.info("Entry removed [" + entryId + "]");

			KalturaStreamEvent event = new KalturaStreamEvent(KalturaEventType.STREAM_DISCONNECTED, liveStreamManager.get(entryId));
			KalturaEventsManager.raiseEvent(event);
		}
	}

	public void onRTPSessionCreate(RTPSession rtpSession) {
		logger.debug("Query string [" + rtpSession.getQueryStr() + "]");
		if (!setLiveStreamManager()) {
			logger.error("Live Stream Manager is not loaded yet");
			rtpSession.rejectSession();
			rtpSession.shutdown();
			return;
		}

		onClientConnect(rtpSession);
	}

	public void onRTPSessionDestroy(RTPSession rtpSession) {
		WMSProperties properties = rtpSession.getProperties();
		if (properties.containsKey(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID)) {
			String entryId = properties.getPropertyStr(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID);
			logger.info("Entry removed [" + entryId + "]");

			KalturaStreamEvent event = new KalturaStreamEvent(KalturaEventType.STREAM_DISCONNECTED, liveStreamManager.get(entryId));
			KalturaEventsManager.raiseEvent(event);
		}
	}

	public void onConnect(IClient client, RequestFunction function, AMFDataList params) {
		WMSProperties properties = client.getProperties();
		logger.debug("End point [" + properties.getPropertyStr(LiveStreamEntry.CLIENT_PROPERTY_CONNECT_APP) + "]");
		
		if (!setLiveStreamManager()) {
			logger.error("Live Stream Manager is not loaded yet");
			client.rejectConnection("Live Stream Manager is not loaded yet", "Live Stream Manager is not loaded yet");
			client.shutdownClient();
			return;
		}

		onClientConnect(client);
	}

	private KalturaLiveEntry onClientConnect(IMediaStream stream) {
		if (stream.getClient() != null) {
			return onClientConnect(stream.getClient());
		}
		else if(stream.getRTPStream() != null && stream.getRTPStream().getSession() != null){
			return onClientConnect(stream.getRTPStream().getSession());
		}

		return null;
	}

	private KalturaLiveEntry onClientConnect(RTPSession rtpSession) {
		String queryString = rtpSession.getQueryStr();

		try {
			return onClientConnect(rtpSession.getProperties(), queryString);
		} catch (KalturaApiException | ClientConnectException e) {
			logger.error("Entry authentication failed [" + queryString + "]: " + e.getMessage());
			rtpSession.rejectSession();
			rtpSession.shutdown();
		}

		return null;
	}

	private KalturaLiveEntry onClientConnect(IClient client) {
		WMSProperties properties = client.getProperties();
		String entryPoint = properties.getPropertyStr(LiveStreamEntry.CLIENT_PROPERTY_CONNECT_APP);
		logger.debug("Connect: " + entryPoint);

		String[] requestParts = entryPoint.split("\\?", 2);
		if (requestParts.length < 2)
			return null;

		String queryString = requestParts[1];
		if(queryString.indexOf("/") > 0){
			queryString = queryString.substring(0, queryString.indexOf("/"));
		}
		
		try {
			return onClientConnect(properties, queryString);
		} catch (KalturaApiException | ClientConnectException e) {
			logger.error("Entry authentication failed [" + entryPoint + "]: " + e.getMessage());
			client.rejectConnection("Unable to authenticate entry [" + entryPoint + "]", "Unable to authenticate entry [" + entryPoint + "]");
			client.shutdownClient();
		}

		return null;
	}

	private KalturaLiveEntry onClientConnect(WMSProperties properties, String queryString) throws KalturaApiException, ClientConnectException {
		logger.info("Query-String [" + queryString + "]");
		
		String[] queryParams = queryString.split("&");
		HashMap<String, String> requestParams = new HashMap<String, String>();
		String[] queryParamsParts;
		for (int i = 0; i < queryParams.length; ++i) {
			queryParamsParts = queryParams[i].split("=", 2);
			if (queryParamsParts.length == 2)
				requestParams.put(queryParamsParts[0], queryParamsParts[1]);
		}

		if (!requestParams.containsKey(LiveStreamEntry.REQUEST_PROPERTY_PARTNER_ID) || !requestParams.containsKey(LiveStreamEntry.REQUEST_PROPERTY_ENTRY_ID) || !requestParams.containsKey(LiveStreamEntry.REQUEST_PROPERTY_SERVER_INDEX)){
			throw new ClientConnectException("Missing arguments [" + queryString + "]");
		}

		int partnerId = Integer.parseInt(requestParams.get(LiveStreamEntry.REQUEST_PROPERTY_PARTNER_ID));
		String entryId = requestParams.get(LiveStreamEntry.REQUEST_PROPERTY_ENTRY_ID);
		String token = requestParams.get(LiveStreamEntry.REQUEST_PROPERTY_TOKEN);

		KalturaLiveEntry entry = liveStreamManager.authenticate(entryId, partnerId, token);
		properties.setProperty(LiveStreamEntry.CLIENT_PROPERTY_PARTNER_ID, partnerId);
		properties.setProperty(LiveStreamEntry.CLIENT_PROPERTY_SERVER_INDEX, Integer.parseInt(requestParams.get(LiveStreamEntry.REQUEST_PROPERTY_SERVER_INDEX)));
		properties.setProperty(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID, entryId);
		logger.info("Entry added [" + entryId + "]");

		return entry;
	}

	public boolean setLiveStreamManager() {
		if(!KalturaServer.isInitialized()){
			return false;
		}
		
		if (liveStreamManager != null){
			return true;
		}

		ILiveStreamManager serverLiveStreamManager = (ILiveStreamManager) KalturaServer.getManager(ILiveStreamManager.class);

		if (serverLiveStreamManager == null || !(serverLiveStreamManager instanceof LiveStreamManager)) {
			logger.error("Live stream manager not defined");
			return false;
		}

		liveStreamManager = (LiveStreamManager) serverLiveStreamManager;
		return true;
	}

	public void onAppStart(IApplicationInstance appInstance) {
		applicationName = appInstance.getApplication().getName();
		this.appInstance = appInstance;

		WMSProperties properties = this.appInstance.getProperties();
		//Init Application Managers
		if (properties.containsKey(LiveStreamEntry.APPLICATION_MANAGERS_PROPERTY_NAME)) {
			String managers = properties.getPropertyStr(LiveStreamEntry.APPLICATION_MANAGERS_PROPERTY_NAME);
			logger.debug("managers: " + managers);
			try {
				KalturaServer.getInstance().initApplicationManagers(managers.replaceAll(" ", "").split(","));
			} catch (Exception e) {
				logger.error("An error occurred: " + e.getMessage());
			}
		}
		
		appInstance.setLiveStreamDvrRecorderControl(dvrRecorderControl);
		appInstance.setLiveStreamPacketizerControl(dvrRecorderControl);
		appInstance.setLiveStreamTranscoderControl(new TranscoderControl());

		setLiveStreamManager();

		appInstance.addLiveStreamTranscoderListener(liveStreamTranscoderListener);

		KalturaApplicationInstanceEvent event = new KalturaApplicationInstanceEvent(KalturaMediaEventType.APPLICATION_INSTANCE_STARTED, appInstance);
		KalturaEventsManager.raiseEvent(event);
	}
	
	public void setCaption (IClient client, RequestFunction function, AMFDataList params) {
		logger.debug("Caption sent from stream: " + params.getObject(3).getString("streamName"));
		
		AMFDataObj captionsParams = params.getObject(3);
		//If the stream is a Kaltura entry stream, send the captions
		if (client != null && client.getProperties().containsKey(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID)) {
			IMediaStream stream = client.getAppInstance().getStreams().getStream (captionsParams.getString("streamName"));
			if (stream != null) {
				  AMFDataMixedArray data = new AMFDataMixedArray();
				  data.put("text", new AMFDataItem(captionsParams.getString("text")));
				  data.put("language", new AMFDataItem(captionsParams.getString("language")));
				  data.put("trackid", new AMFDataItem(captionsParams.getString("trackid")));
				  stream.sendDirect("onTextData", data);
				  ((MediaStream)stream).processSendDirectMessages();
				  getLogger().info("Caption: " + captionsParams.getString("text"));
			}
		}
	}

	public void onHTTPSessionCreate(IHTTPStreamerSession httpStreamerSession){
		String queryString = httpStreamerSession.getQueryStr();
		if(queryString == null || !httpStreamerSession.getStreamExt().equalsIgnoreCase("smil")){
			return;
		}
		
		String filePath = appInstance.getStreamStoragePath() + File.separator + httpStreamerSession.getStreamName();
		File file = new File(filePath);
		if(file.exists()){
			return;
		}
		
		String[] queryParamsParts;
		String[] queryParams = queryString.split("&");
		for (int i = 0; i < queryParams.length; ++i) {
			queryParamsParts = queryParams[i].split("=", 2);
			if (queryParamsParts.length == 2 && queryParamsParts[0].equals(REQUEST_PROPERTY_FLAVOR_IDS)){
				String entryId = getEntryIdFromStreamName(httpStreamerSession.getStreamName());
				SmilManager.merge(httpStreamerSession.getAppInstance(), entryId, file.getPath(), queryParamsParts[1].split(","));
			}
		}
		
	}
}