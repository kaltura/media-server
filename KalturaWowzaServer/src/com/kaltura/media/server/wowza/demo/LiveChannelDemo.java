package com.kaltura.media.server.wowza.demo;

import com.kaltura.media.server.KalturaManagerException;
import com.kaltura.media.server.wowza.LiveChannelManager;

public class LiveChannelDemo extends LiveChannelManager {

	protected final static String KALTURA_CHANNELS_DEMO_PARTNER_ID = "KalturaChannelsDemoPartnerId";
	protected final static String KALTURA_CHANNELS_DEMO_CHANNEL_IDS = "KalturaChannelsDemoChannelIds";

	private int partnerId;
	
	@Override
	public void init() throws KalturaManagerException {
		super.init();
		
		if (serverConfiguration.containsKey(LiveChannelDemo.KALTURA_CHANNELS_DEMO_PARTNER_ID) && serverConfiguration.containsKey(LiveChannelDemo.KALTURA_CHANNELS_DEMO_CHANNEL_IDS)) {
			
			partnerId = Integer.parseInt((String) serverConfiguration.get(LiveChannelDemo.KALTURA_CHANNELS_DEMO_PARTNER_ID));
			String channelIds = (String) serverConfiguration.get(LiveChannelDemo.KALTURA_CHANNELS_DEMO_CHANNEL_IDS);
			
			logger.debug("LiveChannelDemo::init Initializing channels: " + channelIds);
			start(channelIds.replaceAll(" ", "").split(","));
		}
	}

	private void start(String[] channelIds) {
		for(String channelId : channelIds)
			start(channelId, partnerId);
	}
}
