package com.kaltura.media.server;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.client.KalturaConfiguration;
import com.kaltura.client.enums.KalturaDVRStatus;
import com.kaltura.client.enums.KalturaMediaServerIndex;
import com.kaltura.client.types.KalturaLiveStreamEntry;

public class KalturaLiveStreamManager implements ILiveStreamManager {

	protected final static String KALTURA_LIVE_STREAM_KEEP_ALIVE_INTERVAL = "KalturaLiveStreamKeepAliveInterval";
	protected final static String KALTURA_LIVE_STREAM_MAX_DVR_WINDOW = "KalturaLiveStreamMaxDvrWindow";

	protected String hostname;
	protected KalturaClient client;
	protected KalturaConfiguration config;
	protected Map<String, Object> serverConfiguration;
	protected ConcurrentHashMap<String, LiveStreamEntryCache> entries;
	protected Logger logger;

	protected class LiveStreamEntryCache
	{
		public KalturaLiveStreamEntry liveStreamEntry;
		public int index;
		
		public LiveStreamEntryCache(KalturaLiveStreamEntry liveStreamEntry, int index)
		{
			this.liveStreamEntry = liveStreamEntry;
			this.index = index;
		}
	}
	
	protected void impersonate(int partnerId) {
		config.setPartnerId(partnerId);
	}

	protected void unimpersonate() {
		config.setPartnerId(KalturaServer.MEDIA_SERVER_PARTNER_ID);
	}

	@Override
	public KalturaLiveStreamEntry get(int partnerId, String entryId) throws KalturaApiException {
		impersonate(partnerId);
		KalturaLiveStreamEntry liveStreamEntry = client.getLiveStreamService().get(entryId);
		unimpersonate();
		
		return liveStreamEntry;
	}

	public Integer getDvrWindow(KalturaLiveStreamEntry liveStreamEntry) {
		if(liveStreamEntry.dvrStatus != KalturaDVRStatus.ENABLED)
			return null;
		
		int maxDvrWindow = Integer.parseInt((String) serverConfiguration.get(KalturaLiveStreamManager.KALTURA_LIVE_STREAM_MAX_DVR_WINDOW));
		int dvrWindowSeconds = liveStreamEntry.dvrWindow * 60;
		if(dvrWindowSeconds <= 0 || dvrWindowSeconds > maxDvrWindow)
			return maxDvrWindow;
		
		return dvrWindowSeconds;
	}

	protected void setEntryMediaServer(KalturaLiveStreamEntry liveStreamEntry, int serverIndex) {
		impersonate(liveStreamEntry.partnerId);
		try {
			client.getLiveStreamService().registerMediaServer(liveStreamEntry.id, hostname, KalturaMediaServerIndex.get(serverIndex));
		} catch (KalturaApiException e) {
			logger.error("KalturaLiveStreamManager::setEntryMediaServer unable to register media server: " + e.getMessage());
		}
		unimpersonate();
	}

	protected void unsetEntryMediaServer(KalturaLiveStreamEntry liveStreamEntry, int serverIndex) {
		impersonate(liveStreamEntry.partnerId);
		try {
			client.getLiveStreamService().unregisterMediaServer(liveStreamEntry.id, hostname, KalturaMediaServerIndex.get(serverIndex));
		} catch (KalturaApiException e) {
			logger.error("KalturaLiveStreamManager::setEntryMediaServer unable to unregister media server: " + e.getMessage());
		}
		unimpersonate();
	}

	@Override
	public void onPublish(KalturaLiveStreamEntry liveStreamEntry, int serverIndex) {
		setEntryMediaServer(liveStreamEntry, serverIndex);
		entries.put(liveStreamEntry.id, new LiveStreamEntryCache(liveStreamEntry, serverIndex));
	}

	@Override
	public void onUnPublish(KalturaLiveStreamEntry liveStreamEntry) {
		LiveStreamEntryCache liveStreamEntryCache = entries.remove(liveStreamEntry.id);
		unsetEntryMediaServer(liveStreamEntry, liveStreamEntryCache.index);
	}

	@Override
	public void init() throws KalturaManagerException {
		entries = new ConcurrentHashMap<String, LiveStreamEntryCache>();
		
		hostname = KalturaServer.getHostName();
		client = KalturaServer.getClient();
		config = client.getKalturaConfiguration();
		serverConfiguration = KalturaServer.getConfiguration();
		logger = KalturaServer.getLogger();
		
		long keepAliveInterval = Long.parseLong((String) serverConfiguration.get(KalturaLiveStreamManager.KALTURA_LIVE_STREAM_KEEP_ALIVE_INTERVAL));

		TimerTask timerTask = new TimerTask(){

			@Override
			public void run() {
				for(String entryId : entries.keySet()){
					LiveStreamEntryCache liveStreamEntryCache = entries.get(entryId);
					setEntryMediaServer(liveStreamEntryCache.liveStreamEntry, liveStreamEntryCache.index);
				}
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(timerTask, keepAliveInterval, keepAliveInterval);
	}
}
