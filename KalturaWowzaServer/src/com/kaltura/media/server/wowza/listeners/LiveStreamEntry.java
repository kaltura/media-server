package com.kaltura.media.server.wowza.listeners;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.enums.KalturaDVRStatus;
import com.kaltura.client.enums.KalturaRecordStatus;
import com.kaltura.client.types.KalturaLiveStreamEntry;
import com.kaltura.media.server.ILiveStreamManager;
import com.kaltura.media.server.KalturaServer;
import com.wowza.wms.amf.AMFDataList;
import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.application.WMSProperties;
import com.wowza.wms.client.IClient;
import com.wowza.wms.dvr.DvrApplicationContext;
import com.wowza.wms.module.*;
import com.wowza.wms.request.RequestFunction;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.stream.IMediaStreamActionNotify;
import com.wowza.wms.stream.livedvr.ILiveStreamDvrRecorderControl;
import com.wowza.wms.stream.livepacketizer.ILiveStreamPacketizerControl;
import com.wowza.wms.plugin.integration.liverecord.ILiveStreamRecord;
import com.wowza.wms.plugin.integration.liverecord.LiveStreamRecorderMP4;

public class LiveStreamEntry extends ModuleBase {

	protected final static String REQUEST_PROPERTY_PARTNER_ID = "p";
	protected final static String REQUEST_PROPERTY_ENTRY_ID = "e";
	protected final static String REQUEST_PROPERTY_SERVER_INDEX = "i";
	protected final static String REQUEST_PROPERTY_TOKEN = "t";

	protected final static String CLIENT_PROPERTY_CONNECT_APP = "connectapp";
	protected final static String CLIENT_PROPERTY_PARTNER_ID = "partnerId";
	protected final static String CLIENT_PROPERTY_SERVER_INDEX = "serverIndex";
	protected final static String CLIENT_PROPERTY_ENTRY_ID = "entryId";
	
	protected final static int INVALID_SERVER_INDEX = -1;

	private Map<String, ILiveStreamRecord> recorders = new ConcurrentHashMap<String, ILiveStreamRecord>();
	private Map<String, KalturaLiveStreamEntry> entries = new ConcurrentHashMap<String, KalturaLiveStreamEntry>();
	
	private class DvrRecorderControl implements ILiveStreamDvrRecorderControl, ILiveStreamPacketizerControl {

		@Override
		public boolean shouldDvrRecord(String recorderName, IMediaStream stream) {
			return this.isThatStreamNeeded(stream);
		}
		
		private boolean isThatStreamNeeded(IMediaStream stream) {
			String streamName = stream.getName();
			
			String entryId = null;
			IClient client = stream.getClient();
			if(client != null){
				WMSProperties clientProperties = client.getProperties();
				if(!clientProperties.containsKey(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID)){
					getLogger().info("DvrRecorderControl.isThatStreamNeeded: stream [" + streamName + "] is not associated with entry");
					return false;
				}
				entryId = clientProperties.getPropertyStr(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID);
			}
			else{				
				Pattern pattern = Pattern.compile("^(\\d_[\\d\\w]{8})_\\d+$");
				Matcher matcher = pattern.matcher(streamName);
				if(!matcher.find()){
					getLogger().info("DvrRecorderControl.isThatStreamNeeded: stream [" + streamName + "] has no client");
					return false;
				}

				entryId = matcher.group(1);
			}
			
			if(!entries.containsKey(entryId)){
				getLogger().debug("DvrRecorderControl.isThatStreamNeeded: [" + streamName + "] entry [" + entryId + "] not found");
				return false;
			}
			
			KalturaLiveStreamEntry liveStreamEntry = entries.get(entryId);
			if(liveStreamEntry.dvrStatus != KalturaDVRStatus.ENABLED){
				getLogger().debug("DvrRecorderControl.isThatStreamNeeded: [" + streamName + "] DVR disabled");
				return false;
			}

			ILiveStreamManager liveStreamManager = (ILiveStreamManager) KalturaServer.getManager(ILiveStreamManager.class);
			int dvrWindow = liveStreamManager.getDvrWindow(liveStreamEntry);
			getLogger().debug("DvrRecorderControl.isThatStreamNeeded: [" + streamName + "] DVR window [" + dvrWindow + "]");

			IApplicationInstance appInstance = stream.getStreams().getAppInstance();
			DvrApplicationContext ctx = appInstance.getDvrApplicationContext();
			ctx.setWindowDuration(dvrWindow);

			return true;
		}
		
		public boolean isLiveStreamPacketize(String packetizer, IMediaStream stream) {
			getLogger().debug("DvrRecorderControl.isLiveStreamPacketize [" + packetizer + ", " + stream.getName() + "]");
		
			if(packetizer.compareTo("dvrstreamingpacketizer") == 0){
				getLogger().debug("DvrRecorderControl.isLiveStreamPacketize check shouldDvrRecord");
				return this.isThatStreamNeeded(stream);
			}
			
			return true;
		}
	}
	
	class LiveStreamListener implements IMediaStreamActionNotify{
		
		public void onPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend){
			//IApplicationInstance appInstance = stream.getStreams().getAppInstance();

			IClient client = stream.getClient();
			if(client == null)
				return;
			
			WMSProperties clientProperties = client.getProperties();
			if(!clientProperties.containsKey(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID))
				return;
			
			KalturaLiveStreamEntry liveStreamEntry = entries.get(clientProperties.getProperty(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID));

			getLogger().debug("LiveStreamListener::onPublish: " + liveStreamEntry.id);

			if(liveStreamEntry.recordStatus == KalturaRecordStatus.ENABLED){
				startRecord(liveStreamEntry.id, stream, true, true, true, false);
			}
			
			ILiveStreamManager liveStreamManager = (ILiveStreamManager) KalturaServer.getManager(ILiveStreamManager.class);
			liveStreamManager.onPublish(liveStreamEntry, clientProperties.getPropertyInt(LiveStreamEntry.CLIENT_PROPERTY_SERVER_INDEX, LiveStreamEntry.INVALID_SERVER_INDEX));
		}
	
		public void onUnPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend){
			IClient client = stream.getClient();
			if(client == null)
				return;
			
			WMSProperties clientProperties = client.getProperties();
			if(!clientProperties.containsKey(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID))
				return;
			
			KalturaLiveStreamEntry liveStreamEntry = entries.get(clientProperties.getProperty(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID));

			getLogger().debug("LiveStreamListener::onUnPublish: " + liveStreamEntry.id);
			
			ILiveStreamManager liveStreamManager = (ILiveStreamManager) KalturaServer.getManager(ILiveStreamManager.class);
			liveStreamManager.onUnPublish(liveStreamEntry);
		}
	
		public void onPause(IMediaStream stream, boolean isPause, double location){}
	
		public void onPlay(IMediaStream stream, String streamName, double playStart, double playLen, int playReset){}
	
		public void onSeek(IMediaStream stream, double location){}
	
		public void onStop(IMediaStream stream){}
	}

	public void onStreamCreate(IMediaStream stream){
		stream.addClientListener(new LiveStreamListener());
	}

	public void onDisconnect(IClient client) {
		WMSProperties clientProperties = client.getProperties();
		if(clientProperties.containsKey(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID)){
			String entryId = clientProperties.getPropertyStr(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID);
			entries.remove(entryId);
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
		for (int i = 1; i < requestParts.length; ++i)
		{
		    if(field == null)
		    {
		    	field = requestParts[i];
		    }
		    else
		    {
		    	requestParams.put(field, requestParts[i]);
		    	getLogger().debug("LiveStreamEntry::onConnect: " + field + ": " + requestParams.get(field));
		    	field = null;
		    }
		}
		
		int partnerId = Integer.parseInt(requestParams.get(LiveStreamEntry.REQUEST_PROPERTY_PARTNER_ID));
		String entryId = requestParams.get(LiveStreamEntry.REQUEST_PROPERTY_ENTRY_ID);
		String token = requestParams.get(LiveStreamEntry.REQUEST_PROPERTY_TOKEN);

		clientProperties.setProperty(LiveStreamEntry.CLIENT_PROPERTY_PARTNER_ID, partnerId);
		clientProperties.setProperty(LiveStreamEntry.CLIENT_PROPERTY_SERVER_INDEX, Integer.parseInt(requestParams.get(LiveStreamEntry.REQUEST_PROPERTY_SERVER_INDEX)));
		
		ILiveStreamManager liveStreamManager = (ILiveStreamManager) KalturaServer.getManager(ILiveStreamManager.class);
		if(liveStreamManager == null){
			getLogger().error("LiveStreamEntry::onConnect: Live stream manager not defined");
			client.rejectConnection("Live stream manager not defined", "Live stream manager not defined");
			return;
		}

		KalturaLiveStreamEntry liveStreamEntry;
		try {
			liveStreamEntry = liveStreamManager.get(partnerId, entryId);
		} catch (KalturaApiException e) {
			getLogger().error("LiveStreamEntry::onConnect: unable to get entry [" + entryId + "]: " + e.getMessage());
			client.rejectConnection("Unable to get entry [" + entryId + "]", "Unable to get entry [" + entryId + "]");
			return;
		}
		clientProperties.setProperty(LiveStreamEntry.CLIENT_PROPERTY_ENTRY_ID, entryId);
		entries.put(entryId, liveStreamEntry);
		getLogger().info("LiveStreamEntry::onConnect: Entry added [" + entryId + "]");
		
		if(token.compareTo(liveStreamEntry.streamPassword) != 0){
			getLogger().error("LiveStreamEntry::onConnect: Invalid token [" + token + "] for entry [" + entryId + "] with token [" + liveStreamEntry.streamPassword + "]");
			client.rejectConnection("Invalid token", "Invalid token");
		}
	}

	public void onAppStart(IApplicationInstance appInstance) {
		DvrRecorderControl dvrRecorderControl = new DvrRecorderControl();
		appInstance.setLiveStreamDvrRecorderControl(dvrRecorderControl);
		appInstance.setLiveStreamPacketizerControl(dvrRecorderControl);
	}
	
	private void startRecord(String entryId, IMediaStream stream, boolean append, boolean versionFile, boolean startOnKeyFrame, boolean recordData){
		getLogger().debug("LiveStreamEntry::startRecord: " + entryId);
			
		File writeFile = stream.getStreamFileForWrite(entryId, "mp4", "");
		String outputPath = writeFile.getAbsolutePath();
		
		getLogger().debug("LiveStreamEntry::startRecord: entry [" + entryId + "] append [" + append + "] file path [" + outputPath + "] version [" + versionFile + "] start on key frame [" + startOnKeyFrame + "] record data [" + recordData + "]");
		
		// create a stream recorder and save it in a map of recorders
		ILiveStreamRecord recorder = new LiveStreamRecorderMP4();
		
		// add it to the recorders list
		synchronized (recorders)
		{
			ILiveStreamRecord prevRecorder = recorders.get(entryId);
			if (prevRecorder != null)
				prevRecorder.stopRecording();
			recorders.put(entryId, recorder);
		}
		
		// if you want to record data packets as well as video/audio
		recorder.setRecordData(recordData);
		
		// Set to true if you want to version the previous file rather than overwrite it
		recorder.setVersionFile(versionFile);
		
		// If recording only audio set this to false so the recording starts immediately
		recorder.setStartOnKeyFrame(startOnKeyFrame);
		
		// start recording
		recorder.startRecording(stream, outputPath, append);		
	}
}