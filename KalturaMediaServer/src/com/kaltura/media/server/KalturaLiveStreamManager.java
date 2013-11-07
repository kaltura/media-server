package com.kaltura.media.server;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.client.KalturaConfiguration;
import com.kaltura.client.KalturaParamsValueDefaults;
import com.kaltura.client.enums.KalturaDVRStatus;
import com.kaltura.client.enums.KalturaMediaServerIndex;
import com.kaltura.client.enums.KalturaMediaType;
import com.kaltura.client.enums.KalturaSourceType;
import com.kaltura.client.types.KalturaConcatAttributes;
import com.kaltura.client.types.KalturaEntryResource;
import com.kaltura.client.types.KalturaLiveStreamEntry;
import com.kaltura.client.types.KalturaMediaEntry;
import com.kaltura.client.types.KalturaOperationAttributes;
import com.kaltura.client.types.KalturaOperationResource;
import com.kaltura.client.types.KalturaResource;
import com.kaltura.client.types.KalturaServerFileResource;

abstract public class KalturaLiveStreamManager implements ILiveStreamManager {

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
			this(liveStreamEntry);
			
			this.index = index;
		}

		public LiveStreamEntryCache(KalturaLiveStreamEntry liveStreamEntry) {
			this.liveStreamEntry = liveStreamEntry;
		}
	}
	
	protected void impersonate(int partnerId) {
		config.setPartnerId(partnerId);
	}

	protected void unimpersonate() {
		config.setPartnerId(KalturaServer.MEDIA_SERVER_PARTNER_ID);
	}

	@Override
	public KalturaLiveStreamEntry get(String entryId) {
		
		synchronized (entries) {
			if(entries.containsKey(entryId)){
				LiveStreamEntryCache liveStreamEntryCache = entries.get(entryId);
				return liveStreamEntryCache.liveStreamEntry;
			}
		}
		
		return null;
	}

	@Override
	public KalturaLiveStreamEntry get(String entryId, int partnerId) throws KalturaApiException {
		
		KalturaLiveStreamEntry liveStreamEntry = get(entryId);
		if(liveStreamEntry != null)
			return liveStreamEntry;
		
		impersonate(partnerId);
		liveStreamEntry = client.getLiveStreamService().get(entryId);
		unimpersonate();

		synchronized (entries) {
			entries.put(liveStreamEntry.id, new LiveStreamEntryCache(liveStreamEntry));
		}
		
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
		logger.debug("KalturaLiveStreamManager::onPublish entry [" + liveStreamEntry.id + "]");
		setEntryMediaServer(liveStreamEntry, serverIndex);
		synchronized (entries) {
			if(entries.containsKey(liveStreamEntry.id)){
				LiveStreamEntryCache liveStreamEntryCache = entries.get(liveStreamEntry.id);
				liveStreamEntryCache.index = serverIndex;
			}
			else{
				entries.put(liveStreamEntry.id, new LiveStreamEntryCache(liveStreamEntry, serverIndex));
			}
		}
	}

	@Override
	public void onUnPublish(KalturaLiveStreamEntry liveStreamEntry) {
		logger.debug("KalturaLiveStreamManager::onPublish entry [" + liveStreamEntry.id + "]");
		synchronized (entries) {
			if(entries.containsKey(liveStreamEntry.id)){
				LiveStreamEntryCache liveStreamEntryCache = entries.get(liveStreamEntry.id);
				unsetEntryMediaServer(liveStreamEntry, liveStreamEntryCache.index);
			}
		}
	}

	@Override
	public void onDisconnect(String entryId) {
		logger.debug("KalturaLiveStreamManager::onDisconnect entry [" + entryId + "]");
		synchronized (entries) {
			if(entries.containsKey(entryId)){
				entries.remove(entryId);
			}
		}
	}

	protected void cancelRedirect(KalturaLiveStreamEntry liveStreamEntry) {
		logger.debug("KalturaLiveStreamManager::cancelRedirect cancel live entry [" + liveStreamEntry.id + "] redirect");
		impersonate(liveStreamEntry.partnerId);
		
		KalturaLiveStreamEntry updateLiveStreamEntry = new KalturaLiveStreamEntry();
		updateLiveStreamEntry.redirectEntryId = KalturaParamsValueDefaults.KALTURA_NULL_STRING;
		try {
			client.getLiveStreamService().update(liveStreamEntry.id, updateLiveStreamEntry);
		} catch (KalturaApiException e) {
			logger.error("KalturaLiveStreamManager::createMediaEntry failed to upload file: " + e.getMessage());
			unimpersonate();
			return;
		}
		
		unimpersonate();
	}

	protected void createMediaEntryOrAppend(KalturaLiveStreamEntry liveStreamEntry, String recordedFilePath) {
		logger.debug("KalturaLiveStreamManager::createMediaEntry creating media entry for live entry [" + liveStreamEntry.id + "]");
		impersonate(liveStreamEntry.partnerId);

		KalturaResource resource;
		KalturaServerFileResource serverFileResource = new KalturaServerFileResource();
		serverFileResource.localFilePath = recordedFilePath;
		
		KalturaMediaEntry mediaEntry;
		if(liveStreamEntry.recordedEntryId == null){
			mediaEntry = new KalturaMediaEntry();
			mediaEntry.rootEntryId = liveStreamEntry.id;
			mediaEntry.name = liveStreamEntry.name;
			mediaEntry.description = liveStreamEntry.description;
			mediaEntry.sourceType = KalturaSourceType.RECORDED_LIVE;
			mediaEntry.mediaType = KalturaMediaType.VIDEO;
	
			try {
				mediaEntry = client.getMediaService().add(mediaEntry);
			} catch (KalturaApiException e) {
				logger.error("KalturaLiveStreamManager::createMediaEntry failed to create media entry: " + e.getMessage());
				unimpersonate();
				return;
			}
			logger.debug("KalturaLiveStreamManager::createMediaEntry created media entry [" + mediaEntry.id + "] for live entry [" + liveStreamEntry.id + "]");

			resource = serverFileResource;
		}
		else{
			try {
				mediaEntry = client.getMediaService().get(liveStreamEntry.recordedEntryId);
			} catch (KalturaApiException e) {
				logger.error("KalturaLiveStreamManager::createMediaEntry failed to create media entry: " + e.getMessage());
				unimpersonate();
				return;
			}

			KalturaConcatAttributes operationAttributes = new KalturaConcatAttributes();
			operationAttributes.resource = serverFileResource;
			
			KalturaEntryResource entryResource = new KalturaEntryResource();
			entryResource.entryId = liveStreamEntry.recordedEntryId;
			KalturaOperationResource operationResource = new KalturaOperationResource();
			operationResource.resource = entryResource;
			operationResource.operationAttributes = new ArrayList<KalturaOperationAttributes>();
			operationResource.operationAttributes.add(operationAttributes);
			
			resource = operationResource;
		}
		
		try {
			client.getMediaService().updateContent(mediaEntry.id, resource);
		} catch (KalturaApiException e) {
			logger.error("KalturaLiveStreamManager::createMediaEntry failed to add content resource: " + e.getMessage());
			unimpersonate();
			return;
		}

		KalturaLiveStreamEntry updateLiveStreamEntry = new KalturaLiveStreamEntry();
		updateLiveStreamEntry.redirectEntryId = mediaEntry.id;
		updateLiveStreamEntry.recordedEntryId = mediaEntry.id;
		try {
			client.getLiveStreamService().update(liveStreamEntry.id, updateLiveStreamEntry);
		} catch (KalturaApiException e) {
			logger.error("KalturaLiveStreamManager::createMediaEntry failed to upload file: " + e.getMessage());
			unimpersonate();
			return;
		}
		
		unimpersonate();
	}

	@Override
	public void init() throws KalturaManagerException {
		entries = new ConcurrentHashMap<String, LiveStreamEntryCache>();
		
		hostname = KalturaServer.getHostName();
		client = KalturaServer.getClient();
		config = client.getKalturaConfiguration();
		serverConfiguration = KalturaServer.getConfiguration();
		logger = KalturaServer.getLogger();
		
		long keepAliveInterval = Long.parseLong((String) serverConfiguration.get(KalturaLiveStreamManager.KALTURA_LIVE_STREAM_KEEP_ALIVE_INTERVAL)) * 1000;

		TimerTask timerTask = new TimerTask(){

			@Override
			public void run() {
				synchronized (entries) {
					for(String entryId : entries.keySet()){
						LiveStreamEntryCache liveStreamEntryCache = entries.get(entryId);
						setEntryMediaServer(liveStreamEntryCache.liveStreamEntry, liveStreamEntryCache.index);
					}
				}
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(timerTask, keepAliveInterval, keepAliveInterval);
	}
}
