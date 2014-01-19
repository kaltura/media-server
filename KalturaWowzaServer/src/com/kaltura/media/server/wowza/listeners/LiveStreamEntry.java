package com.kaltura.media.server.wowza.listeners;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.enums.KalturaDVRStatus;
import com.kaltura.client.enums.KalturaMediaServerIndex;
import com.kaltura.client.types.KalturaLiveStreamEntry;
import com.kaltura.infra.XmlUtils;
import com.kaltura.media.server.ILiveStreamManager;
import com.kaltura.media.server.KalturaServer;
import com.kaltura.media.server.wowza.LiveStreamManager;
import com.wowza.wms.amf.AMFDataList;
import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.application.WMSProperties;
import com.wowza.wms.client.IClient;
import com.wowza.wms.dvr.DvrApplicationContext;
import com.wowza.wms.dvr.IDvrConstants;
import com.wowza.wms.medialist.MediaList;
import com.wowza.wms.module.ModuleBase;
import com.wowza.wms.request.RequestFunction;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.stream.IMediaStreamActionNotify;
import com.wowza.wms.stream.livedvr.ILiveStreamDvrRecorderControl;
import com.wowza.wms.stream.livepacketizer.ILiveStreamPacketizerControl;
import com.wowza.wms.stream.livetranscoder.ILiveStreamTranscoder;
import com.wowza.wms.stream.livetranscoder.ILiveStreamTranscoderNotify;
import com.wowza.wms.transcoder.model.LiveStreamTranscoder;
import com.wowza.wms.transcoder.model.LiveStreamTranscoderActionNotifyBase;
import com.wowza.wms.transcoder.model.TranscoderStreamNameGroup;
import com.wowza.wms.util.MediaListUtils;

public class LiveStreamEntry extends ModuleBase {

	protected final static String REQUEST_PROPERTY_PARTNER_ID = "p";
	protected final static String REQUEST_PROPERTY_ENTRY_ID = "e";
	protected final static String REQUEST_PROPERTY_SERVER_INDEX = "i";
	protected final static String REQUEST_PROPERTY_TOKEN = "t";

	protected final static String CLIENT_PROPERTY_CONNECT_APP = "connectapp";
	protected final static String CLIENT_PROPERTY_PARTNER_ID = "partnerId";
	protected final static String CLIENT_PROPERTY_SERVER_INDEX = "serverIndex";
	protected final static String CLIENT_PROPERTY_ENTRY_ID = "entryId";
	protected final static String STREAM_PROPERTY_SUFFIX = "suffix";

	protected final static int INVALID_SERVER_INDEX = -1;

	private LiveStreamManager liveStreamManager;
	private LiveStreamTranscoderActionListener liveStreamTranscoderActionListener = new LiveStreamTranscoderActionListener();

	private class DvrRecorderControl implements ILiveStreamDvrRecorderControl, ILiveStreamPacketizerControl {

		@Override
		public boolean shouldDvrRecord(String recorderName, IMediaStream stream) {
			return this.isThatStreamNeeded(stream);
		}

		private boolean isThatStreamNeeded(IMediaStream stream) {
			String streamName = stream.getName();
			IApplicationInstance appInstance = stream.getStreams().getAppInstance();
			DvrApplicationContext ctx = appInstance.getDvrApplicationContext();

			String entryId = null;
			IClient client = stream.getClient();
			if (client != null) {
				WMSProperties clientProperties = client.getProperties();
				if (!clientProperties.containsKey(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID)) {
					getLogger().info("DvrRecorderControl.isThatStreamNeeded: stream [" + streamName + "] is not associated with entry");
					return false;
				}
				entryId = clientProperties.getPropertyStr(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID);
			} else {
				Pattern pattern = Pattern.compile("^(\\d_[\\d\\w]{8})_");
				Matcher matcher = pattern.matcher(streamName);
				if (!matcher.find()) {
					getLogger().info("DvrRecorderControl.isThatStreamNeeded: stream [" + streamName + "] has no client");
					return false;
				}

				entryId = matcher.group(1);
			}

			KalturaLiveStreamEntry liveStreamEntry = liveStreamManager.get(entryId);

			if (liveStreamEntry == null) {
				getLogger().debug("DvrRecorderControl.isThatStreamNeeded: [" + streamName + "] entry [" + entryId + "] not found");
				return false;
			}

			if (liveStreamEntry.dvrStatus != KalturaDVRStatus.ENABLED) {
				getLogger().debug("DvrRecorderControl.isThatStreamNeeded: [" + streamName + "] DVR disabled");
				return false;
			}

			int dvrWindow = liveStreamManager.getDvrWindow(liveStreamEntry);
			getLogger().debug("DvrRecorderControl.isThatStreamNeeded: [" + streamName + "] DVR window [" + dvrWindow + "]");

			ctx.setWindowDuration(dvrWindow);
			ctx.setArchiveStrategy(IDvrConstants.ARCHIVE_STRATEGY_DELETE);

			return true;
		}

		public boolean isLiveStreamPacketize(String packetizer, IMediaStream stream) {
			getLogger().debug("DvrRecorderControl.isLiveStreamPacketize [" + packetizer + ", " + stream.getName() + "]");

			if (packetizer.compareTo("dvrstreamingpacketizer") == 0) {
				getLogger().debug("DvrRecorderControl.isLiveStreamPacketize check shouldDvrRecord");
				return this.isThatStreamNeeded(stream);
			}

			return true;
		}
	}

	class LiveStreamListener implements IMediaStreamActionNotify {

		public void onPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend) {
			IClient client = stream.getClient();
			if (client != null){ // streamed from the client
				WMSProperties clientProperties = client.getProperties();
				if (!clientProperties.containsKey(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID)){
					getLogger().error("LiveStreamListener::onPublish: unauthenticated client tried to publish stream [" + streamName + "]");
					client.rejectConnection("Client did not authenticated", "Client did not authenticated");
					return;
				}
	
				String entryId = clientProperties.getPropertyStr(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID);
				KalturaMediaServerIndex serverIndex = KalturaMediaServerIndex.get(clientProperties.getPropertyInt(LiveStreamEntry.CLIENT_PROPERTY_SERVER_INDEX, LiveStreamEntry.INVALID_SERVER_INDEX));
	
				getLogger().debug("LiveStreamListener::onPublish: " + entryId);

				if (!entryId.equals(streamName)){
					Pattern pattern = Pattern.compile("^([01]_.{8})_(.+)$");
					Matcher matcher = pattern.matcher(streamName);
	
					if (!matcher.find()) {
						getLogger().error("LiveStreamListener::onPublish: unknown published stream [" + streamName + "]");
						return;
					}
	
					if (!entryId.equals(matcher.group(1))) {
						getLogger().error("LiveStreamListener::onPublish: published stream stream name [" + streamName + "] does not match entry id [" + entryId + "]");
						return;
					}
				}
				
				liveStreamManager.onPublish(entryId, serverIndex);
			}
			else{ // streamed from the transcoder

				Pattern pattern = Pattern.compile("^([01]_.{8})_(\\d+)$");
				Matcher matcher = pattern.matcher(streamName);

				if (!matcher.find()) {
					getLogger().error("LiveStreamListener::onPublish: unknown published stream [" + streamName + "]");
					return;
				}

				String entryId = matcher.group(1);
				int assetParamsId = Integer.parseInt(matcher.group(2));
				getLogger().debug("LiveStreamListener::onPublish stream [" + streamName + "] entry [" + entryId + "] asset params id [" + assetParamsId + "]");
				
				IMediaStream sourceStream = stream.getStreams().getStream(entryId);
				if(sourceStream == null){
					getLogger().error("LiveStreamListener::onPublish: source stream not found for stream [" + streamName + "]");
					return;
				}
				
				client = sourceStream.getClient();
				if(client == null){
					getLogger().error("LiveStreamListener::onPublish: client not found for stream [" + streamName + "]");
					return;
				}
				
				WMSProperties clientProperties = client.getProperties();
				KalturaMediaServerIndex serverIndex = KalturaMediaServerIndex.get(clientProperties.getPropertyInt(LiveStreamEntry.CLIENT_PROPERTY_SERVER_INDEX, LiveStreamEntry.INVALID_SERVER_INDEX));

				liveStreamManager.onPublish(stream, entryId, serverIndex, assetParamsId);
			}
		}

		public void onUnPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend) {
			IClient client = stream.getClient();
			if (client == null)
				return;

			WMSProperties clientProperties = client.getProperties();
			if (!clientProperties.containsKey(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID))
				return;

			KalturaLiveStreamEntry liveStreamEntry = liveStreamManager.get(clientProperties.getPropertyStr(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID));
			KalturaMediaServerIndex serverIndex = KalturaMediaServerIndex.get(clientProperties.getPropertyInt(LiveStreamEntry.CLIENT_PROPERTY_SERVER_INDEX, LiveStreamEntry.INVALID_SERVER_INDEX));

			getLogger().debug("LiveStreamListener::onUnPublish: " + liveStreamEntry.id);
			liveStreamManager.onUnPublish(liveStreamEntry, serverIndex);
		}

		public void onPause(IMediaStream stream, boolean isPause, double location) {
		}

		public void onPlay(IMediaStream stream, String streamName, double playStart, double playLen, int playReset) {
		}

		public void onSeek(IMediaStream stream, double location) {
		}

		public void onStop(IMediaStream stream) {
		}
	}

	class LiveStreamTranscoderSmilManager {

		protected final static String SMIL_VIDEOS_XPATH = "/smil/body/switch";

		private IApplicationInstance appInstance;
		private String entryId;
		
		public LiveStreamTranscoderSmilManager(IApplicationInstance appInstance, String entryId) {
			this.appInstance = appInstance;
			this.entryId = entryId;
		}

		public synchronized void generate(String groupName) {
			String appName = appInstance.getContextStr();
			getLogger().debug("LiveStreamEntry#LiveStreamTranscoderSmilManager.generate [" + appName + "/" + groupName + "]");

			MediaList mediaList = MediaListUtils.parseMediaList(appInstance, groupName, "ngrp", null);
			if (mediaList == null) {
				getLogger().error("LiveStreamEntry#LiveStreamTranscoderSmilManager.generate: MediaList not found: " + appName + "/" + groupName);
				return;
			}

			String smil = mediaList.toSMILString();
			
			String filePath = appInstance.getStreamStoragePath() + File.separator + entryId + ".smil";
			File file = new File(filePath);
			if(file.exists()){
				File tmpFile;
				try {
					tmpFile = File.createTempFile(groupName, ".smil");
					PrintWriter out = new PrintWriter(tmpFile);
					out.print(smil);
					out.close();
				} catch (Exception e) {
					getLogger().error("LiveStreamEntry#LiveStreamTranscoderSmilManager.generate: Failed writing to temp file [" + filePath + "]: " + e.getMessage());
					return;
				}
				
				try {
					XmlUtils.merge(file, LiveStreamTranscoderSmilManager.SMIL_VIDEOS_XPATH, file, tmpFile);
				} catch (Exception e) {
					getLogger().error("LiveStreamEntry#LiveStreamTranscoderSmilManager.generate: Failed merging files [" + filePath + ", " + tmpFile.getAbsolutePath() + "]: " + e.getMessage());
					return;
				}
			}
			else{
				try {
					PrintWriter out = new PrintWriter(file);
					out.print(smil);
					out.close();
				} catch (FileNotFoundException e) {
					getLogger().error("LiveStreamEntry#LiveStreamTranscoderSmilManager.generate: Failed writing to file [" + filePath + "]: " + e.getMessage());
					return;
				}
			}

			getLogger().info("LiveStreamEntry#LiveStreamTranscoderSmilManager.generate: Created smil file [" + filePath + "] for stream " + appName + "/" + groupName + ":\n" + smil + "\n\n");
		}

	}

	class LiveStreamTranscoderActionListener extends LiveStreamTranscoderActionNotifyBase {

		Map<String, LiveStreamTranscoderSmilManager> smilManagers = new HashMap<String, LiveStreamTranscoderSmilManager>();
		
		@Override
		public void onRegisterStreamNameGroup(LiveStreamTranscoder liveStreamTranscoder, TranscoderStreamNameGroup streamNameGroup){

			final IApplicationInstance appInstance = liveStreamTranscoder.getAppInstance();

			final String groupName = streamNameGroup.getStreamName();
			String appName = appInstance.getContextStr();
			
			IMediaStream stream = liveStreamTranscoder.getStream();
			if(stream == null){
				getLogger().error("LiveStreamEntry#LiveStreamTranscoderActionListener.onRegisterStreamNameGroup [" + appName + "/" + groupName + "] source stream not found");
				return;
			}
			
			IClient client = stream.getClient();
			if(client == null){
				getLogger().error("LiveStreamEntry#LiveStreamTranscoderActionListener.onRegisterStreamNameGroup [" + appName + "/" + groupName + "] client not found");
				return;
			}

			WMSProperties clientProperties = client.getProperties();
			if (!clientProperties.containsKey(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID)) {
				getLogger().error("LiveStreamEntry#LiveStreamTranscoderActionListener.onRegisterStreamNameGroup [" + appName + "/" + groupName + "] entry id not defined");
				return;
			}
			final String entryId = clientProperties.getPropertyStr(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID);
			
			TimerTask generateSmilTask = new TimerTask() {
				
				@Override
				public void run() {

					LiveStreamTranscoderSmilManager smilManager = null;
					
					synchronized (smilManagers) {
						
						if(smilManagers.containsKey(entryId)){
							smilManager = smilManagers.get(entryId);
						}
						else{
							smilManager = new LiveStreamTranscoderSmilManager(appInstance, entryId);
							smilManagers.put(entryId, smilManager);
						}
					}
					smilManager.generate(groupName);
				}
			};
			
			Timer timer = new Timer();
			timer.schedule(generateSmilTask, 1);
		}

		@Override
		public void onUnregisterStreamNameGroup(LiveStreamTranscoder liveStreamTranscoder, TranscoderStreamNameGroup streamNameGroup){

			IApplicationInstance appInstance = liveStreamTranscoder.getAppInstance();
			
			String groupName = streamNameGroup.getStreamName();
			String appName = appInstance.getContextStr();

			getLogger().debug("LiveStreamEntry#LiveStreamTranscoderActionListener.onUnregisterStreamNameGroup [" + appName + "/" + groupName + "]");

			String filePath = appInstance.getStreamStoragePath() + File.separator + groupName + ".smil";
			
			File file = new File(filePath);
			if(file.exists())
				file.delete();
			
			getLogger().info("LiveStreamEntry#LiveStreamTranscoderActionListener.onUnregisterStreamNameGroup: Deleted smil file [" + filePath + "] for stream " + appName + "/" + groupName);
		}
	}
		
	class LiveStreamTranscoderListener implements ILiveStreamTranscoderNotify {

		@Override
		public void onLiveStreamTranscoderCreate(ILiveStreamTranscoder liveStreamTranscoder, IMediaStream mediaStream) {
			((LiveStreamTranscoder)liveStreamTranscoder).addActionListener(liveStreamTranscoderActionListener);
		}

		@Override
		public void onLiveStreamTranscoderDestroy(ILiveStreamTranscoder liveStreamTranscoder, IMediaStream mediaStream) {
		}

		@Override
		public void onLiveStreamTranscoderInit(ILiveStreamTranscoder iLiveStreamTranscoder, IMediaStream mediaStream) {
		}
	}

	public void onStreamCreate(IMediaStream stream) {
		stream.addClientListener(new LiveStreamListener());
	}

	public void onDisconnect(IClient client) {
		WMSProperties clientProperties = client.getProperties();
		if (clientProperties.containsKey(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID)) {
			String entryId = clientProperties.getPropertyStr(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID);
			liveStreamManager.onDisconnect(entryId);
			getLogger().info("LiveStreamEntry::onDisconnect: Entry removed [" + entryId + "]");
		}
	}

	public void onConnect(IClient client, RequestFunction function, AMFDataList params) {
		WMSProperties clientProperties = client.getProperties();
		String entryPoint = clientProperties.getPropertyStr(LiveStreamEntry.CLIENT_PROPERTY_CONNECT_APP);
		getLogger().debug("LiveStreamEntry::onConnect: " + entryPoint);

		String[] requestParts = entryPoint.split("/");
		HashMap<String, String> requestParams = new HashMap<String, String>();
		String field = null;
		for (int i = 1; i < requestParts.length; ++i) {
			if (field == null) {
				field = requestParts[i];
			} else {
				requestParams.put(field, requestParts[i]);
				getLogger().debug("LiveStreamEntry::onConnect: " + field + ": " + requestParams.get(field));
				field = null;
			}
		}
		
		if(!requestParams.containsKey(LiveStreamEntry.REQUEST_PROPERTY_PARTNER_ID))
			return;

		int partnerId = Integer.parseInt(requestParams.get(LiveStreamEntry.REQUEST_PROPERTY_PARTNER_ID));
		String entryId = requestParams.get(LiveStreamEntry.REQUEST_PROPERTY_ENTRY_ID);
		String token = requestParams.get(LiveStreamEntry.REQUEST_PROPERTY_TOKEN);

		try {
			liveStreamManager.authenticate(entryId, partnerId, token);
			clientProperties.setProperty(LiveStreamEntry.CLIENT_PROPERTY_PARTNER_ID, partnerId);
			clientProperties.setProperty(LiveStreamEntry.CLIENT_PROPERTY_SERVER_INDEX, Integer.parseInt(requestParams.get(LiveStreamEntry.REQUEST_PROPERTY_SERVER_INDEX)));
			clientProperties.setProperty(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID, entryId);
			getLogger().info("LiveStreamEntry::onConnect: Entry added [" + entryId + "]");
		} catch (KalturaApiException e) {
			getLogger().error("LiveStreamEntry::onConnect: Entry authentication failed [" + entryId + "]: " + e.getMessage());
			client.rejectConnection("Unable to authenticate entry [" + entryId + "]", "Unable to authenticate entry [" + entryId + "]");
		}


	}

	public void onAppStart(IApplicationInstance appInstance) {
		DvrRecorderControl dvrRecorderControl = new DvrRecorderControl();
		appInstance.setLiveStreamDvrRecorderControl(dvrRecorderControl);
		appInstance.setLiveStreamPacketizerControl(dvrRecorderControl);

		ILiveStreamManager serverLiveStreamManager = (ILiveStreamManager) KalturaServer.getManager(ILiveStreamManager.class);

		if (serverLiveStreamManager == null || !(serverLiveStreamManager instanceof LiveStreamManager)) {
			getLogger().error("LiveStreamEntry::onAppStart: Live stream manager not defined");
			return;
		}

		liveStreamManager = (LiveStreamManager) serverLiveStreamManager;
		appInstance.addLiveStreamTranscoderListener(new LiveStreamTranscoderListener());
	}
}