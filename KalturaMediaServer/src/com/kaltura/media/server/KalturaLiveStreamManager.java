package com.kaltura.media.server;

import java.util.Date;
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
import com.kaltura.client.types.KalturaEntryResource;
import com.kaltura.client.types.KalturaLiveStreamEntry;
import com.kaltura.client.types.KalturaMediaEntry;

abstract public class KalturaLiveStreamManager implements ILiveStreamManager {

	protected final static String KALTURA_RECORDED_CHUNCK_MAX_DURATION = "KalturaRecordedChunckMaxDuration";
	protected final static String KALTURA_LIVE_STREAM_KEEP_ALIVE_INTERVAL = "KalturaLiveStreamKeepAliveInterval";
	protected final static String KALTURA_LIVE_STREAM_MAX_DVR_WINDOW = "KalturaLiveStreamMaxDvrWindow";
	protected final static String KALTURA_IS_LIVE_REGISTRATION_MIN_BUFFER_TIME = "KalturaIsLiveRegistrationMinBufferTime";

	protected final static long DEFAULT_RECORDED_CHUNCK_MAX_DURATION = 60;
	protected final static long DEFAULT_IS_LIVE_REGISTRATION_MIN_BUFFER_TIME = 5;
	
	protected String hostname;
	protected KalturaClient client;
	protected KalturaConfiguration config;
	protected Map<String, Object> serverConfiguration;
	protected ConcurrentHashMap<String, LiveStreamEntryCache> entries;
	protected Logger logger;
	protected long isLiveRegistrationMinBufferTime = KalturaLiveStreamManager.DEFAULT_IS_LIVE_REGISTRATION_MIN_BUFFER_TIME;
	
	private Timer setMediaServerTimer;
	private Timer splitRecordingTimer;
	
	protected class LiveStreamEntryCache
	{
		public KalturaLiveStreamEntry liveStreamEntry;
		public KalturaMediaServerIndex index = null;
		public Date registerTime = null;
		
		public LiveStreamEntryCache(KalturaLiveStreamEntry liveStreamEntry, KalturaMediaServerIndex index, Date registerTime)
		{
			this(liveStreamEntry);
			register(index, registerTime);
		}

		public LiveStreamEntryCache(KalturaLiveStreamEntry liveStreamEntry) {
			this.liveStreamEntry = liveStreamEntry;
		}

		public void register(KalturaMediaServerIndex index, Date registerTime) {
			this.index = index;
			this.registerTime = registerTime;
		}

		public void unregister() {
			this.index = null;
			this.registerTime = null;
		}

		public boolean isRegistered() {
			if(index == null)
				return false;
			
			return registerTime.before(new Date());
		}

		public void setLiveStreamEntry(KalturaLiveStreamEntry liveStreamEntry) {
			this.liveStreamEntry = liveStreamEntry;
		}
	}
	
	protected void impersonate(int partnerId) {
		config.setPartnerId(partnerId);
	}

	protected void unimpersonate() {
		config.setPartnerId(KalturaServer.MEDIA_SERVER_PARTNER_ID);
	}

	public KalturaLiveStreamEntry authenticate(String entryId, int partnerId, String token) throws KalturaApiException {
		impersonate(partnerId);
		KalturaLiveStreamEntry liveStreamEntry = client.getLiveStreamService().authenticate(entryId, token);
		unimpersonate();

		synchronized (entries) {
			entries.put(liveStreamEntry.id, new LiveStreamEntryCache(liveStreamEntry));
		}
		
		return liveStreamEntry;
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

	public KalturaLiveStreamEntry reloadEntry(String entryId, int partnerId) {
		impersonate(partnerId);
		KalturaLiveStreamEntry liveStreamEntry;
		try {
			liveStreamEntry = client.getLiveStreamService().get(entryId);
		} catch (KalturaApiException e) {
			logger.error("KalturaLiveStreamManager::reloadEntry unable to get entry [" + entryId + "]: " + e.getMessage());
			return null;
		}
		unimpersonate();

		synchronized (entries) {
			LiveStreamEntryCache liveStreamEntryCache = entries.get(entryId);
			liveStreamEntryCache.setLiveStreamEntry(liveStreamEntry);
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

	protected void setEntryMediaServer(KalturaLiveStreamEntry liveStreamEntry, KalturaMediaServerIndex serverIndex) {
		impersonate(liveStreamEntry.partnerId);
		try {
			client.getLiveStreamService().registerMediaServer(liveStreamEntry.id, hostname, serverIndex);
		} catch (KalturaApiException e) {
			logger.error("KalturaLiveStreamManager::setEntryMediaServer unable to register media server: " + e.getMessage());
		}
		unimpersonate();
	}

	protected void unsetEntryMediaServer(KalturaLiveStreamEntry liveStreamEntry, KalturaMediaServerIndex serverIndex) {
		impersonate(liveStreamEntry.partnerId);
		try {
			client.getLiveStreamService().unregisterMediaServer(liveStreamEntry.id, hostname, serverIndex);
		} catch (KalturaApiException e) {
			logger.error("KalturaLiveStreamManager::unsetEntryMediaServer unable to unregister media server: " + e.getMessage());
		}
		unimpersonate();
	}

	@Override
	public void onPublish(final KalturaLiveStreamEntry liveStreamEntry, final KalturaMediaServerIndex serverIndex) {
		logger.debug("KalturaLiveStreamManager::onPublish entry [" + liveStreamEntry.id + "]");
		
		if(serverIndex == KalturaMediaServerIndex.PRIMARY)
			cancelRedirect(liveStreamEntry);
		
		TimerTask setMediaServerTask = new TimerTask(){

			@Override
			public void run() {
				setEntryMediaServer(liveStreamEntry, serverIndex);
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(setMediaServerTask, isLiveRegistrationMinBufferTime);
		
		Date registerTime = new Date();
		registerTime.setTime(registerTime.getTime() + isLiveRegistrationMinBufferTime);
		
		synchronized (entries) {
			
			if(entries.containsKey(liveStreamEntry.id)){
				LiveStreamEntryCache liveStreamEntryCache = entries.get(liveStreamEntry.id);
				liveStreamEntryCache.register(serverIndex, registerTime);
			}
			else{
				entries.put(liveStreamEntry.id, new LiveStreamEntryCache(liveStreamEntry, serverIndex, registerTime));
			}
		}
	}

	@Override
	public void onUnPublish(KalturaLiveStreamEntry liveStreamEntry, KalturaMediaServerIndex serverIndex) {
		logger.debug("KalturaLiveStreamManager::onUnPublish entry [" + liveStreamEntry.id + "]");
		
		if(serverIndex == KalturaMediaServerIndex.PRIMARY)
			setRedirect(liveStreamEntry);
		
		synchronized (entries) {
			if(entries.containsKey(liveStreamEntry.id)){
				LiveStreamEntryCache liveStreamEntryCache = entries.get(liveStreamEntry.id);
				if(liveStreamEntryCache.index != null){
					unsetEntryMediaServer(liveStreamEntry, liveStreamEntryCache.index);
					liveStreamEntryCache.unregister();
				}
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
			logger.error("KalturaLiveStreamManager::cancelRedirect failed to upload file: " + e.getMessage());
			unimpersonate();
			return;
		}
		
		unimpersonate();
	}

	protected void setRedirect(KalturaLiveStreamEntry liveStreamEntry) {
		logger.debug("KalturaLiveStreamManager::setRedirect set live entry [" + liveStreamEntry.id + "] redirect");

		if(liveStreamEntry.recordedEntryId == null){
			logger.error("KalturaLiveStreamManager::setRedirect no recorded entry id on live entry [" + liveStreamEntry.id + "]");
			liveStreamEntry = reloadEntry(liveStreamEntry.id, liveStreamEntry.partnerId);
			if(liveStreamEntry == null)
				return;

			if(liveStreamEntry.recordedEntryId == null){
				logger.error("KalturaLiveStreamManager::setRedirect no recorded entry id on live entry [" + liveStreamEntry.id + "] after reloading");
				return;
			}
		}
		
		impersonate(liveStreamEntry.partnerId);
		
		KalturaLiveStreamEntry updateLiveStreamEntry = new KalturaLiveStreamEntry();
		updateLiveStreamEntry.redirectEntryId = liveStreamEntry.recordedEntryId;
		try {
			client.getLiveStreamService().update(liveStreamEntry.id, updateLiveStreamEntry);
		} catch (KalturaApiException e) {
			logger.error("KalturaLiveStreamManager::setRedirect failed to upload file: " + e.getMessage());
			unimpersonate();
			return;
		}
		
		unimpersonate();
	}

	protected void createMediaEntryOrAppend(KalturaLiveStreamEntry liveStreamEntry) {
		logger.debug("KalturaLiveStreamManager::createMediaEntryOrAppend creating media entry for live entry [" + liveStreamEntry.id + "]");
		impersonate(liveStreamEntry.partnerId);

		KalturaEntryResource resource = new KalturaEntryResource();
		resource.entryId = liveStreamEntry.id;
		
		KalturaMediaEntry mediaEntry = null;
		if(liveStreamEntry.recordedEntryId != null){
			try {
				mediaEntry = client.getMediaService().get(liveStreamEntry.recordedEntryId);
			} catch (KalturaApiException e) {
				logger.warn("KalturaLiveStreamManager::createMediaEntryOrAppend failed to get recorded media entry [" + liveStreamEntry.recordedEntryId + "]: " + e.getMessage());
			}
		}

		if(mediaEntry == null){
			mediaEntry = new KalturaMediaEntry();
			mediaEntry.rootEntryId = liveStreamEntry.id;
			mediaEntry.name = liveStreamEntry.name;
			mediaEntry.description = liveStreamEntry.description;
			mediaEntry.sourceType = KalturaSourceType.RECORDED_LIVE;
			mediaEntry.mediaType = KalturaMediaType.VIDEO;
	
			try {
				mediaEntry = client.getMediaService().add(mediaEntry);
			} catch (KalturaApiException e) {
				logger.error("KalturaLiveStreamManager::createMediaEntryOrAppend failed to create media entry: " + e.getMessage());
				unimpersonate();
				return;
			}
			logger.debug("KalturaLiveStreamManager::createMediaEntryOrAppend created media entry [" + mediaEntry.id + "] for live entry [" + liveStreamEntry.id + "]");
		}

		synchronized (entries){
			liveStreamEntry.recordedEntryId = mediaEntry.id;
		}
		
		KalturaLiveStreamEntry updateLiveStreamEntry = new KalturaLiveStreamEntry();
		updateLiveStreamEntry.recordedEntryId = mediaEntry.id;
		try {
			client.getLiveStreamService().update(liveStreamEntry.id, updateLiveStreamEntry);
		} catch (KalturaApiException e) {
			logger.error("KalturaLiveStreamManager::createMediaEntryOrAppend failed to upload file: " + e.getMessage());
			unimpersonate();
			return;
		}
		
		try {
			client.getMediaService().cancelReplace(mediaEntry.id);
			mediaEntry = client.getMediaService().updateContent(mediaEntry.id, resource);
			
			if(mediaEntry.replacingEntryId != null)
				client.getMediaService().approveReplace(mediaEntry.id);
			
		} catch (KalturaApiException e) {
			logger.error("KalturaLiveStreamManager::createMediaEntryOrAppend failed to add content resource [" + mediaEntry.id + "]: " + e.getMessage());
			unimpersonate();
			return;
		}
		
		unimpersonate();
	}

	public boolean isEntryRegistered(String entryId){
		boolean registered = false;
		synchronized (entries) {
			LiveStreamEntryCache liveStreamEntryCache = entries.get(entryId);
			if(liveStreamEntryCache != null)
				registered = liveStreamEntryCache.isRegistered();
		}
		
		return registered;
	}

	@Override
	public void init() throws KalturaManagerException {
		entries = new ConcurrentHashMap<String, LiveStreamEntryCache>();
		
		hostname = KalturaServer.getHostName();
		client = KalturaServer.getClient();
		config = client.getKalturaConfiguration();
		serverConfiguration = KalturaServer.getConfiguration();
		logger = KalturaServer.getLogger();

		isLiveRegistrationMinBufferTime = KalturaLiveStreamManager.DEFAULT_IS_LIVE_REGISTRATION_MIN_BUFFER_TIME * 1000;
		if (serverConfiguration.containsKey(KalturaLiveStreamManager.KALTURA_IS_LIVE_REGISTRATION_MIN_BUFFER_TIME))
			isLiveRegistrationMinBufferTime = Long.parseLong((String) serverConfiguration.get(KalturaLiveStreamManager.KALTURA_IS_LIVE_REGISTRATION_MIN_BUFFER_TIME)) * 1000;

		long keepAliveInterval = Long.parseLong((String) serverConfiguration.get(KalturaLiveStreamManager.KALTURA_LIVE_STREAM_KEEP_ALIVE_INTERVAL)) * 1000;

		if(keepAliveInterval > 0){
			TimerTask setMediaServerTask = new TimerTask(){
	
				@Override
				public void run() {
					synchronized (entries) {
						for(String entryId : entries.keySet()){
							LiveStreamEntryCache liveStreamEntryCache = entries.get(entryId);
							if(liveStreamEntryCache.isRegistered())
								setEntryMediaServer(liveStreamEntryCache.liveStreamEntry, liveStreamEntryCache.index);
						}
					}
				}
			};
			
			setMediaServerTimer = new Timer();
			setMediaServerTimer.schedule(setMediaServerTask, keepAliveInterval, keepAliveInterval);
		}


		long splitRecordingInterval = KalturaLiveStreamManager.DEFAULT_RECORDED_CHUNCK_MAX_DURATION * 60 * 1000;
		if (serverConfiguration.containsKey(KalturaLiveStreamManager.KALTURA_RECORDED_CHUNCK_MAX_DURATION))
			splitRecordingInterval = Long.parseLong((String) serverConfiguration.get(KalturaLiveStreamManager.KALTURA_RECORDED_CHUNCK_MAX_DURATION)) * 60 * 1000;

		if(splitRecordingInterval > 0){
			TimerTask splitRecordingTask = new TimerTask(){
	
				@Override
				public void run() {
					restartRecordings();
				}
			};
			
			splitRecordingTimer = new Timer(true);
			splitRecordingTimer.schedule(splitRecordingTask, splitRecordingInterval, splitRecordingInterval);
		}
	}

	@Override
	public void stop() {
		setMediaServerTimer.cancel();
		splitRecordingTimer.cancel();
	}
}
