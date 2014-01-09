package com.kaltura.media.server;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.client.enums.KalturaMediaServerIndex;
import com.kaltura.client.enums.KalturaRecordStatus;
import com.kaltura.client.types.KalturaBaseEntry;
import com.kaltura.client.types.KalturaLiveChannel;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.client.types.KalturaServerFileResource;


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
			TimerTask splitRecordingTask = new TimerTask(){
	
				@Override
				public void run() {
					reloadScheduledChannels();
				}

			};
			
			reloadScheduledChannelsTimer = new Timer(true);
			reloadScheduledChannelsTimer.schedule(splitRecordingTask, reloadScheduledChannelsInterval, reloadScheduledChannelsInterval);
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
			} catch (KalturaApiException e) {
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
		super.stop();
	}

	public KalturaLiveChannel reloadEntry(String entryId, int partnerId) {
		KalturaClient impersonateClient = impersonate(partnerId);
		KalturaLiveChannel liveChannel;
		try {
			liveChannel = impersonateClient.getLiveChannelService().get(entryId);
		} catch (KalturaApiException e) {
			logger.error("KalturaLiveStreamManager::reloadEntry unable to get entry [" + entryId + "]: " + e.getMessage());
			return null;
		}

		synchronized (entries) {
			LiveEntryCache liveChannelCache = entries.get(entryId);
			liveChannelCache.setLiveEntry(liveChannel);
		}
		
		return liveChannel;
	}

	@Override
	protected void setEntryMediaServer(KalturaLiveEntry liveChannel, KalturaMediaServerIndex serverIndex) {
		KalturaClient impersonateClient = impersonate(liveChannel.partnerId);
		try {
			impersonateClient.getLiveChannelService().registerMediaServer(liveChannel.id, hostname, serverIndex);
		} catch (KalturaApiException e) {
			logger.error("KalturaLiveStreamManager::setEntryMediaServer unable to register media server: " + e.getMessage());
		}
	}

	@Override
	protected void unsetEntryMediaServer(KalturaLiveEntry liveChannel, KalturaMediaServerIndex serverIndex) {
		KalturaClient impersonateClient = impersonate(liveChannel.partnerId);
		try {
			impersonateClient.getLiveChannelService().unregisterMediaServer(liveChannel.id, hostname, serverIndex);
		} catch (KalturaApiException e) {
			logger.error("KalturaLiveStreamManager::unsetEntryMediaServer unable to unregister media server: " + e.getMessage());
		}
	}
	
	@Override
	public void appendRecording(String entryId, KalturaMediaServerIndex index, String filePath, float duration) {

		KalturaServerFileResource resource = new KalturaServerFileResource();
		resource.localFilePath = filePath;
		
		KalturaLiveChannel liveEntry = get(entryId);
		KalturaClient impersonateClient = impersonate(liveEntry.partnerId);
		try {
			impersonateClient.getLiveChannelService().appendRecording(entryId, index, resource, duration);
		} catch (KalturaApiException e) {
			logger.error("Append live recording error: " + e.getMessage());
		}
		
		if(liveEntry.recordStatus == KalturaRecordStatus.ENABLED && index == KalturaMediaServerIndex.PRIMARY)
			appendRecording(liveEntry);
	}
}
