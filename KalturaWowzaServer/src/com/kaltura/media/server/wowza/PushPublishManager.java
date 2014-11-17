package com.kaltura.media.server.wowza;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.client.KalturaMultiResponse;
import com.kaltura.client.enums.KalturaLivePublishStatus;
import com.kaltura.client.enums.KalturaMediaServerIndex;
import com.kaltura.client.enums.KalturaPlaybackProtocol;
import com.kaltura.client.types.KalturaLiveAsset;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.client.types.KalturaLiveStreamPushPublishConfiguration;
import com.kaltura.client.types.KalturaLiveStreamPushPublishRTMPConfiguration;
import com.kaltura.media.server.KalturaEventsManager;
import com.kaltura.media.server.KalturaServer;
import com.kaltura.media.server.events.IKalturaEvent;
import com.kaltura.media.server.events.IKalturaEventConsumer;
import com.kaltura.media.server.events.KalturaEventType;
import com.kaltura.media.server.events.KalturaStreamEvent;
import com.kaltura.media.server.managers.ILiveManager;
import com.kaltura.media.server.managers.KalturaLiveManager;
import com.kaltura.media.server.managers.KalturaManager;
import com.kaltura.media.server.managers.KalturaManagerException;
import com.kaltura.media.server.wowza.events.KalturaMediaEventType;
import com.kaltura.media.server.wowza.events.KalturaMediaStreamEvent;
import com.wowza.wms.pushpublish.model.PushPublishBase;
import com.wowza.wms.pushpublish.protocol.rtmp.PushPublishRTMP;
import com.wowza.wms.pushpublish.protocol.rtp.PushPublishRTP;
import com.wowza.wms.server.LicensingException;
import com.wowza.wms.stream.IMediaStream;

public class PushPublishManager extends KalturaManager implements IKalturaEventConsumer {

	protected final static String MULTICAST_IP_CONFIG_FIELD_NAME = "MulticastIP";
	protected final static String MULTICAST_TAG_FIELD_NAME = "MulticastTag";
	protected final static String MULTICAST_PORT_RANGE_FIELD_NAME = "MulticastPortRange";
	protected final static String MULTICAST_STREAM_TIMEOUT = "multicastStreamTimeout";
	protected final static int MULTICAST_DEFAULT_STREAM_TIMEOUT = 10000;
	protected final static int AKAMAI_ASSET_PARAMS_ID=32;
	
	protected static Logger logger = Logger.getLogger(KalturaLiveManager.class);
	protected ILiveManager liveManager;
	protected int maxPort;
	protected int minPort;
	protected int minFreePort;
	protected int maxFreePort;
	
	protected ConcurrentHashMap<String,Integer> multicastPortsInUse = new ConcurrentHashMap<String,Integer>();
	protected ConcurrentHashMap<String, ArrayList<PushPublishBase>> publishers = new ConcurrentHashMap<String,ArrayList<PushPublishBase>>();
	
	@Override
	public void init() throws KalturaManagerException {
		super.init();
		logger.info("Initialize PushPublisherManager");
		KalturaEventsManager.registerEventConsumer(this, KalturaEventType.STREAM_UNPUBLISHED, KalturaEventType.STREAM_PUBLISHED, KalturaMediaEventType.MEDIA_STREAM_PUBLISHED);
		liveManager = (ILiveManager) KalturaServer.getManager(ILiveManager.class);
		
		if (serverConfiguration.containsKey(PushPublishManager.MULTICAST_PORT_RANGE_FIELD_NAME)) {
			String portRangeConfig = (String)serverConfiguration.get(PushPublishManager.MULTICAST_PORT_RANGE_FIELD_NAME);
		    String[] portRange = portRangeConfig.split("-");
		    minPort = Integer.parseInt(portRange[0]);
		    maxPort = Integer.parseInt(portRange[1]);
		    minFreePort = Integer.parseInt(portRange[0]);
		    maxFreePort = Integer.parseInt(portRange[0]);
		}
	    
	    setInitialized();
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}


	@Override
	public void onEvent(IKalturaEvent event) {
		logger.info("pushPublishManager onEvent");
		if(event.getType() instanceof KalturaMediaEventType){
			KalturaMediaStreamEvent mediaStreamEvent;
			switch((KalturaMediaEventType) event.getType())
			{
				case MEDIA_STREAM_PUBLISHED:
					mediaStreamEvent = (KalturaMediaStreamEvent) event;
					onPublish(mediaStreamEvent.getMediaStream(), mediaStreamEvent.getEntry(), mediaStreamEvent.getAssetParamsId());
					break;
				default:
					break;
				
			}
		}
		
		if(event.getType() instanceof KalturaEventType){
			switch((KalturaEventType) event.getType())
			{
				case STREAM_PUBLISHED:
					KalturaMediaStreamEvent publishStreamEvent = (KalturaMediaStreamEvent) event;
					onSourcePublish(publishStreamEvent.getMediaStream(), publishStreamEvent.getEntry(), publishStreamEvent.getAssetParamsId(), publishStreamEvent.getServerIndex());
				break;
				case STREAM_UNPUBLISHED:
					KalturaStreamEvent streamEvent = (KalturaStreamEvent) event;
					onUnPublish(streamEvent.getEntry());
					break;
				default:
					break;
			}
		}
	
	}
	
	protected void onPublish (IMediaStream stream, KalturaLiveEntry entry, int assetParamsId)
	{
		if (entry.pushPublishEnabled == KalturaLivePublishStatus.DISABLED)
		{
			return;
		}
		
		String streamName = stream.getName();
		if (!streamName.contains("@") && !streamName.startsWith("push-")) {
			logger.debug("Attempting to publish stream [" + streamName + "] entry [" + entry.id + "] asset params id [" + assetParamsId + "]");
			
			KalturaLiveAsset liveAsset = liveManager.getLiveAsset(entry.id, assetParamsId);
			if (liveAsset == null) {
				logger.error("live asset for entry [" + entry.id + "] and assetParamsId [" + assetParamsId + "] was not found");
			}
			
			if (serverConfiguration.containsKey(PushPublishManager.MULTICAST_TAG_FIELD_NAME) && liveAsset.tags.contains((String) serverConfiguration.get(PushPublishManager.MULTICAST_TAG_FIELD_NAME)))
			{
				logger.info("Attempting to multicast entry [" + entry.id + "], asset id [" + liveAsset.id  + "]");
				multicastPublish (stream, entry, liveAsset);
			}

		}
	}
	
	protected void onSourcePublish (IMediaStream stream, KalturaLiveEntry entry, int assetParamsId, KalturaMediaServerIndex serverIndex)
	{
		if (entry.pushPublishEnabled == KalturaLivePublishStatus.DISABLED)
		{
			return;
		}
		
		String streamName = stream.getName();
		
		if (!streamName.contains("@") && !streamName.startsWith("push-") && assetParamsId == PushPublishManager.AKAMAI_ASSET_PARAMS_ID) {
			logger.info("Attempting to publish to Akamai entry [" + entry.id + "], asset id [" + assetParamsId  + "]");
			rtmpPublish (stream, entry, assetParamsId, serverIndex);
		}
	}
	
	protected void multicastPublish (IMediaStream stream, KalturaLiveEntry entry, KalturaLiveAsset liveAsset)
	{
		String streamName = stream.getName();
		logger.info("live asset for entry [" + entry.id + "] and assetParamsId [" + liveAsset.flavorParamsId + "] will be multicast");
		
		if (!stream.isTranscodeResult())
			return;
		
	    PushPublishRTP publisher = new PushPublishRTP();
	    publisher.setAppInstance(stream.getStreams().getAppInstance());
	    publisher.setSrcStream(stream);
	    //Destination stream
	    publisher.setHost((String)serverConfiguration.get(PushPublishManager.MULTICAST_IP_CONFIG_FIELD_NAME));
	    
	    int defaultStreamTimeout = serverConfiguration.containsKey(PushPublishManager.MULTICAST_STREAM_TIMEOUT) ? (int)serverConfiguration.get(PushPublishManager.MULTICAST_STREAM_TIMEOUT) : PushPublishManager.MULTICAST_DEFAULT_STREAM_TIMEOUT;
	    
	    logger.debug("timeout: " + defaultStreamTimeout);
	    publisher.setRtpStreamWaitTimeout(defaultStreamTimeout);
	    
	    if (liveAsset.multicastPort > 0 && !(multicastPortsInUse.containsValue(liveAsset.multicastPort))) {
	    	publisher.setPort(liveAsset.multicastPort);
	    }
	    else
	    {
	    	publisher.setPort (maxFreePort);
	    }
	    
	    publisher.setTimeToLive("63");
	    
	    synchronized (multicastPortsInUse) {
	    	multicastPortsInUse.put(entry.id, maxFreePort);
	    	while (multicastPortsInUse.containsValue(maxFreePort))
	    	{
	    		maxFreePort += 4;
	    		if (maxFreePort > maxPort) {
	    			maxFreePort = minFreePort;
	    		}
	    	}
		}
	    publisher.setDstStreamName("push-" + streamName);
	    logger.debug("publishing stream " + publisher.getDstStreamName());
	    publisher.setStreamName(streamName);
	    publisher.setDebugLog(true);
	    publisher.connect();
	    
	    synchronized (publishers) {
	    	ArrayList<PushPublishBase> entryPublishers;
	    	if (!publishers.contains(entry.id)) {
	    		publishers.put(entry.id, new ArrayList<PushPublishBase>());
	    	}
	    	entryPublishers = publishers.get(entry.id);
	    	entryPublishers.add(publisher);
	    }
	    
//		    //Update the liveAsset
	    liveAsset.multicastIP = publisher.getHostname();
	    liveAsset.multicastPort = publisher.getPort();
	    try
	    {
	    	KalturaClient impersonateClient = impersonate(entry.partnerId);
	    	impersonateClient.startMultiRequest();
	    	impersonateClient.getFlavorAssetService().update(liveAsset.id, liveAsset);
	    	impersonateClient.getLiveStreamService().addLiveStreamPushPublishConfiguration(entry.id, KalturaPlaybackProtocol.MULTICAST_SL, publisher.getHostname() +  ":" + publisher.getPort());
	    	KalturaMultiResponse responses = impersonateClient.doMultiRequest();
	    	
	    	for (Object response : responses) {
	    		if (response instanceof KalturaApiException) {
	    			logger.error("Error occured during request: [" +  ((KalturaApiException)response).getMessage() + "]");
	    		}
			}
	    }
	    catch (KalturaApiException e)
	    {
	    	logger.error("Operation failed. Exception message:  [" + e.getMessage() + "]");
	    }
	}
	
	protected void rtmpPublish (IMediaStream stream, KalturaLiveEntry entry, int assetParamsId, KalturaMediaServerIndex serverIndex) {
		try {
			ArrayList<KalturaLiveStreamPushPublishConfiguration> configurations = entry.publishConfigurations;
			for (KalturaLiveStreamPushPublishConfiguration configuration : configurations) { 
				if (configuration instanceof KalturaLiveStreamPushPublishRTMPConfiguration) {
					// Found correct publishing configuration for Akamai.
					PushPublishRTMP publisher = new PushPublishRTMP();
					logger.info("Publishing entry [" + entry.id + "] asset params id [" + assetParamsId + "] to RTMP destination");
					publisher.setAppInstance(stream.getStreams().getAppInstance());
					publisher.setSrcStreamName(stream.getName());
					KalturaLiveStreamPushPublishRTMPConfiguration rtmpConfiguration = (KalturaLiveStreamPushPublishRTMPConfiguration)configuration;
					publisher.setAkamaiUserName(rtmpConfiguration.userId);
					publisher.setAkamaiPassword(rtmpConfiguration.password);
					
					logger.info("Server index to publish to: " + serverIndex);
					
					if (KalturaMediaServerIndex.PRIMARY.equals(serverIndex) ) {
						publisher.setHost(rtmpConfiguration.publishUrl);
					}
					else if (KalturaMediaServerIndex.SECONDARY.equals(serverIndex)) {
						publisher.setHost(rtmpConfiguration.backupPublishUrl);
					}
					
					String port = "1935";
					if (rtmpConfiguration.port != null)
						port = rtmpConfiguration.port;
					publisher.setPort(port);
					
					publisher.setDstApplicationName(rtmpConfiguration.applicationName);
					publisher.setDstStreamName(rtmpConfiguration.streamName);
					publisher.setConnectionFlashVersion(PushPublishRTMP.CURRENTFLASHVERSION);
					
					publisher.setSendFCPublish(true);
					publisher.setSendReleaseStream(true);
					publisher.setSendOnMetadata(true);
					publisher.setDebugLog(true);
					publisher.setDebugPackets(false);
					
					publisher.connect();
					
					synchronized (publishers) {
						ArrayList<PushPublishBase> entryPublishers;
				    	if (!publishers.containsKey(entry.id)) {
				    		publishers.put(entry.id, new ArrayList<PushPublishBase>());
				    	}
				    	entryPublishers = publishers.get(entry.id);
				    	entryPublishers.add(publisher);
					}
				}
			}

			
		} catch (LicensingException e) {
			// TODO Auto-generated catch block
			logger.error("Unexpected error occurred: " + e.getMessage());
		}
		
	}
	
	protected void onUnPublish (KalturaLiveEntry entry) {
		logger.info("unpublishing entry [" + entry.id + "]");
		synchronized (publishers) {
			ArrayList<PushPublishBase> currentPublishers = publishers.remove(entry.id);
			if (currentPublishers == null) {
				logger.info("No publishers were found for entry ID " + entry.id + ".");
				return;
			}
			
			for (PushPublishBase publisher : currentPublishers) {
				logger.info("Unpublishing stream " + publisher.getDstStreamName());
				publisher.disconnect();
			}
		}
		
		synchronized (multicastPortsInUse) {
			if (multicastPortsInUse.containsKey(entry.id)) {
				int freePort = multicastPortsInUse.remove(entry.id);
				if (freePort < minFreePort)
					minFreePort = freePort;
			}
		}
		
		try {
	    	KalturaClient impersonateClient = impersonate(entry.partnerId);
	    	impersonateClient.getLiveStreamService().removeLiveStreamPushPublishConfiguration(entry.id, KalturaPlaybackProtocol.MULTICAST_SL);
	    }
	    catch (KalturaApiException e) {
	    	logger.error("Operation failed. Exception message:  [" + e.getMessage() + "]");
	    }
	}
	
}
