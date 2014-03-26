package com.kaltura.media.server.managers;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.client.KalturaServiceBase;
import com.kaltura.client.types.KalturaBaseEntry;
import com.kaltura.client.types.KalturaLiveChannel;


abstract public class KalturaLiveChannelManager extends KalturaLiveManager implements ILiveChannelManager {

	protected final static String KALTURA_RELOAD_SCHEDULED_CHANNELS_INTERVAL = "KalturaReloadScheduledChannelsInterval";
	
	protected final static long DEFAULT_RELOAD_SCHEDULED_CHANNELS_INTERVAL = 60;

	private Timer reloadScheduledChannelsTimer;
	
	@Override
	public void init() throws KalturaManagerException {
		super.init();
		
		long reloadScheduledChannelsInterval = KalturaLiveChannelManager.DEFAULT_RELOAD_SCHEDULED_CHANNELS_INTERVAL * 1000;
		if (serverConfiguration.containsKey(KalturaLiveChannelManager.KALTURA_RELOAD_SCHEDULED_CHANNELS_INTERVAL))
			reloadScheduledChannelsInterval = Long.parseLong((String) serverConfiguration.get(KalturaLiveChannelManager.KALTURA_RELOAD_SCHEDULED_CHANNELS_INTERVAL)) * 1000;

		if(reloadScheduledChannelsInterval > 0){
			TimerTask reloadScheduledChannelsTask = new TimerTask(){
	
				@Override
				public void run() {
					reloadScheduledChannels();
				}

			};
			
			reloadScheduledChannelsTimer = new Timer(true);
			reloadScheduledChannelsTimer.schedule(reloadScheduledChannelsTask, reloadScheduledChannelsInterval, reloadScheduledChannelsInterval);
		}
	}

	protected void reloadScheduledChannels() {
		// TODO
	}

	public KalturaLiveChannel get(String liveChannelId){
		return (KalturaLiveChannel) super.get(liveChannelId);
	}

	public KalturaLiveChannel get(String liveChannelId, int partnerId) throws KalturaApiException{
		KalturaClient impersonateClient = impersonate(partnerId);
		KalturaLiveChannel liveEntry = impersonateClient.getLiveChannelService().get(liveChannelId);
		impersonateClient = null;

		synchronized (entries) {
			entries.put(liveEntry.id, new LiveEntryCache(liveEntry));
		}
		
		return liveEntry;
	}
	
	@Override
	public void start(String liveChannelId, int partnerId){
		KalturaLiveChannel liveChannel;
		try {
			liveChannel = get(liveChannelId, partnerId);
		} catch (KalturaApiException e) {
			logger.error("KalturaLiveChannelManager::start failed to get live channel [" + liveChannelId + "]: " + e.getMessage());
			return;
		}
		
		if(liveChannel.playlistId != null){
			List<KalturaBaseEntry> segmentEntries;
			KalturaClient impersonateClient = impersonate(partnerId);
			try {
				segmentEntries = impersonateClient.getPlaylistService().execute(liveChannel.playlistId);
				impersonateClient = null;
			} catch (KalturaApiException e) {
				impersonateClient = null;
				logger.error("KalturaLiveChannelManager::start failed to execute playlist [" + liveChannel.playlistId + "] for channel [" + liveChannelId + "]: " + e.getMessage());
				return;
			}
			start(liveChannel, segmentEntries);
		}
		else{
			// TODO
		}
	}

	@Override
	public void publishEntry(String liveChannelId, String entryId, int partnerId){
		// TODO
	}

	@Override
	public void publishSegment(int liveChannelSegmentId, int partnerId){
		// TODO
	}

	@Override
	public void stop() {
		reloadScheduledChannelsTimer.cancel();
		reloadScheduledChannelsTimer.purge();
		
		super.stop();

		synchronized (entries) {
			for(LiveEntryCache liveEntryCache : entries.values()){
				onUnPublish(liveEntryCache.getLiveEntry(), liveEntryCache.getIndex());
			}
		}
	}


	public KalturaServiceBase getLiveServiceInstance (KalturaClient impersonateClient)
	{
		return impersonateClient.getLiveChannelService();
	}
}
