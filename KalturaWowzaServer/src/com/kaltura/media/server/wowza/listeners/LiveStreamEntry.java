package com.kaltura.media.server.wowza.listeners;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
import com.wowza.wms.stream.livetranscoder.LiveStreamTranscoderItem;
import com.wowza.wms.plugin.integration.liverecord.ILiveStreamRecord;
import com.wowza.wms.plugin.integration.liverecord.LiveStreamRecorderMP4;

public class LiveStreamEntry extends ModuleBase {

	protected final static String REQUEST_PROPERTY_PARTNER_ID = "p";
	protected final static String REQUEST_PROPERTY_SERVER_INDEX = "i";
	protected final static String REQUEST_PROPERTY_TOKEN = "t";

	protected final static String CLIENT_PROPERTY_CONNECT_APP = "connectapp";
	protected final static String CLIENT_PROPERTY_PARTNER_ID = "partnerId";
	protected final static String CLIENT_PROPERTY_SERVER_INDEX = "serverIndex";
	protected final static String CLIENT_PROPERTY_ENTRY = "entry";
	
	protected final static int INVALID_SERVER_INDEX = -1;

	private Map<String, ILiveStreamRecord> recorders = new HashMap<String, ILiveStreamRecord>();
	
	private class DvrRecorderControl implements ILiveStreamDvrRecorderControl, ILiveStreamPacketizerControl {

		@Override
		public boolean shouldDvrRecord(String recorderName, IMediaStream stream) {
			return this.isThatStreamNeeded(stream);
		}
		
		private boolean isThatStreamNeeded(IMediaStream stream) {

			IApplicationInstance appInstance = stream.getStreams().getAppInstance();
			
			WMSProperties clientProperties = stream.getClient().getProperties();
			KalturaLiveStreamEntry liveStreamEntry = (KalturaLiveStreamEntry) clientProperties.getProperty(LiveStreamEntry.CLIENT_PROPERTY_ENTRY);
			
			if(liveStreamEntry.dvrStatus == KalturaDVRStatus.DISABLED){
				getLogger().debug("DvrRecorderControl.isThatStreamNeeded: DVR disabled");	
				return false;
			}

			DvrApplicationContext ctx = appInstance.getDvrApplicationContext();
			ctx.setWindowDuration(liveStreamEntry.dvrWindow * 60); // liveStreamEntry.dvrWindow is in minutes

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

			WMSProperties clientProperties = stream.getClient().getProperties();
			
			ILiveStreamManager liveStreamManager = (ILiveStreamManager) KalturaServer.getManager(ILiveStreamManager.class);
			liveStreamManager.onPublish(streamName, clientProperties.getPropertyInt(LiveStreamEntry.CLIENT_PROPERTY_SERVER_INDEX, LiveStreamEntry.INVALID_SERVER_INDEX));
		}
	
		public void onUnPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend){
			//IApplicationInstance appInstance = stream.getStreams().getAppInstance();

			WMSProperties clientProperties = stream.getClient().getProperties();
			
			ILiveStreamManager liveStreamManager = (ILiveStreamManager) KalturaServer.getManager(ILiveStreamManager.class);
			liveStreamManager.onUnPublish(streamName, clientProperties.getPropertyInt(LiveStreamEntry.CLIENT_PROPERTY_SERVER_INDEX, LiveStreamEntry.INVALID_SERVER_INDEX));
		}
	
		public void onPause(IMediaStream stream, boolean isPause, double location){}
	
		public void onPlay(IMediaStream stream, String streamName, double playStart, double playLen, int playReset){}
	
		public void onSeek(IMediaStream stream, double location){}
	
		public void onStop(IMediaStream stream){}
	}

	public void onStreamCreate(IMediaStream stream){
		stream.addClientListener(new LiveStreamListener());

		WMSProperties clientProperties = stream.getClient().getProperties();
		String entryPoint = clientProperties.getPropertyStr(LiveStreamEntry.CLIENT_PROPERTY_CONNECT_APP);
		
		getLogger().debug("LiveStreamEntry::onStreamCreate: " + entryPoint);

		/*
		if(liveStreamEntry.recordStatus == KalturaRecordStatus.ENABLED){
			startRecord(stream, append, outputPath, versionFile, startOnKeyFrame, recordData)
		}
		*/
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
		    	getLogger().debug("LiveStreamEntry::onConnect: " + field + ": " + requestParams.get(field) + "");
		    	field = null;
		    }
		}

		int partnerId = Integer.getInteger(requestParams.get(LiveStreamEntry.REQUEST_PROPERTY_PARTNER_ID));
		String token = requestParams.get(LiveStreamEntry.REQUEST_PROPERTY_TOKEN);
		String entryId = params.getString(CALLBACK_PARAM1);

		clientProperties.setProperty(LiveStreamEntry.CLIENT_PROPERTY_PARTNER_ID, partnerId);
		clientProperties.setProperty(LiveStreamEntry.CLIENT_PROPERTY_SERVER_INDEX, Integer.getInteger(requestParams.get(LiveStreamEntry.REQUEST_PROPERTY_SERVER_INDEX)));
		
		ILiveStreamManager liveStreamManager = (ILiveStreamManager) KalturaServer.getManager(ILiveStreamManager.class);
		KalturaLiveStreamEntry liveStreamEntry = liveStreamManager.get(partnerId, entryId);
		
		clientProperties.setProperty(LiveStreamEntry.CLIENT_PROPERTY_ENTRY, liveStreamEntry);
		
		if(token.compareTo(liveStreamEntry.streamPassword) != 0)
			client.rejectConnection("Invalid token", "Invalid token");
	}

	public void onAppStart(IApplicationInstance appInstance) {
		DvrRecorderControl dvrRecorderControl = new DvrRecorderControl();
		appInstance.setLiveStreamDvrRecorderControl(dvrRecorderControl);
		appInstance.setLiveStreamPacketizerControl(dvrRecorderControl);
	}
	
	private void startRecord(IMediaStream stream, boolean append, boolean versionFile, boolean startOnKeyFrame, boolean recordData){
		String streamName = stream.getName();
			
		File writeFile = stream.getStreamFileForWrite();
		String outputPath = writeFile.getAbsolutePath();
		
		getLogger().debug("LiveStreamEntry::startRecording: stream [" + streamName + "] append [" + append + "] file path [" + outputPath + "] version [" + versionFile + "] start on key frame [" + startOnKeyFrame + "] record data [" + recordData + "]");
		
		// create a stream recorder and save it in a map of recorders
		ILiveStreamRecord recorder = new LiveStreamRecorderMP4();
		
		// add it to the recorders list
		synchronized (recorders)
		{
			ILiveStreamRecord prevRecorder = recorders.get(streamName);
			if (prevRecorder != null)
				prevRecorder.stopRecording();
			recorders.put(streamName, recorder);
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
	
	private void stopRecord(IMediaStream stream, String outputPath ){
		String streamName = stream.getName();
	
		ILiveStreamRecord recorder = null;
		synchronized (recorders)
		{
			recorder = recorders.remove(streamName);
		}
		
		outputPath  = null;
		if (recorder != null)
		{
			/*
			// grab the current path to the recorded file
			outputPath = recorder.getFilePath();
			File file = new File(outputPath);
			*/
			
			// stop recording
			recorder.stopRecording();
		}
	}
}