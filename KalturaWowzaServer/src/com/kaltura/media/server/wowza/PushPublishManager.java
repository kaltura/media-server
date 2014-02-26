package com.kaltura.media.server.wowza;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.kaltura.client.enums.KalturaLivePublishStatus;
import com.kaltura.client.types.KalturaLiveAsset;
import com.kaltura.client.types.KalturaLiveEntry;
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
import com.wowza.wms.client.IClient;
import com.wowza.wms.plugin.pushpublish.protocol.rtp.PushPublisherRTP;
import com.wowza.wms.stream.IMediaStream;

public class PushPublishManager extends KalturaManager implements IKalturaEventConsumer {

	protected final static String MULTICAST_IP_CONFIG_FIELD_NAME = "MulticastIP";
	protected final static String MULTICAST_TAG_FIELD_NAME = "MulticastTag";
	protected final static String MULTICAST_PORT_RANGE_FIELD_NAME = "MulticastPortRange";
	
	protected static Logger logger = Logger.getLogger(KalturaLiveManager.class);
	protected ILiveManager liveManager;
	protected int maxPort;
	protected int minPort;
	protected int minFreePort;
	
	protected ConcurrentHashMap<String,Integer> multicastPortsInUse = new ConcurrentHashMap<String,Integer>();
	protected ConcurrentHashMap<String,PushPublisherRTP> multicastPublishers = new ConcurrentHashMap<String,PushPublisherRTP>();
	
	@Override
	public void init() throws KalturaManagerException {
		super.init();
		logger.info("Initialize PushPublisherManager");
		KalturaEventsManager.registerEventConsumer(this, KalturaEventType.STREAM_UNPUBLISHED, KalturaMediaEventType.MEDIA_STREAM_PUBLISHED);
		liveManager = (ILiveManager) KalturaServer.getManager(ILiveManager.class);
		
		String portRangeConfig = (String)serverConfiguration.get(PushPublishManager.MULTICAST_PORT_RANGE_FIELD_NAME);
	    String[] portRange = portRangeConfig.split("-");
	    minPort = Integer.parseInt(portRange[0]);
	    maxPort = Integer.parseInt(portRange[1]);
	    minFreePort = Integer.parseInt(portRange[0]);
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
			KalturaStreamEvent streamEvent;
			switch((KalturaEventType) event.getType())
			{
				case STREAM_UNPUBLISHED:
					streamEvent = (KalturaStreamEvent) event;
					onUnPublish(streamEvent.getEntryId());
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
		
		IClient client = stream.getClient();
		String streamName = stream.getName();
		if (!streamName.startsWith("push-") && client == null) {
			logger.debug("Attempting to publish stream [" + streamName + "] entry [" + entry.id + "] asset params id [" + assetParamsId + "]");
			
			KalturaLiveAsset liveAsset = liveManager.getLiveAsset(entry.id, assetParamsId);
			if (liveAsset == null) {
				logger.error("live asset for entry [" + entry.id + "] and assetParamsId [" + assetParamsId + "] was not found");
			}
			
			if (!liveAsset.tags.contains((String) serverConfiguration.get(PushPublishManager.MULTICAST_TAG_FIELD_NAME)))
			{
				return;
			}
			
			logger.info("live asset for entry [" + entry.id + "] and assetParamsId [" + assetParamsId + "] will be multicast");
			
		    PushPublisherRTP publisher = new PushPublisherRTP();
		    publisher.setAppInstance(stream.getStreams().getAppInstance());
		   
		    // Destination stream
		    publisher.setHost((String)serverConfiguration.get(PushPublishManager.MULTICAST_IP_CONFIG_FIELD_NAME));
		    publisher.setPort(minFreePort);
		    
		    synchronized (multicastPortsInUse) {
		    	multicastPortsInUse.put(entry.id, minFreePort);
		    	while (multicastPortsInUse.containsValue(minFreePort))
		    	{
		    		minFreePort += 4; 
		    	}
			}
		    
		    publisher.setDstStreamName("push-" + streamName);
		    logger.debug("publishing stream " + publisher.getDstStreamName());
		    publisher.setStreamName(streamName);
		
		    publisher.setDebugLog(true);
		    publisher.connect();
		    
		    synchronized (multicastPublishers) {
		    	multicastPublishers.put(entry.id, publisher);
		    }
		}
	}
	
	protected void onUnPublish (String entryId)
	{
		logger.info("unpublishing entry [" + entryId + "]");
		synchronized (multicastPublishers) {
			PushPublisherRTP currentPublisher = multicastPublishers.remove(entryId);
			if (currentPublisher != null)
				currentPublisher.disconnect();
		}
		
		synchronized (multicastPortsInUse) {
			int freePort = multicastPortsInUse.remove(entryId);
			if (freePort < minFreePort)
				minFreePort = freePort;
		}
	}
	
}
