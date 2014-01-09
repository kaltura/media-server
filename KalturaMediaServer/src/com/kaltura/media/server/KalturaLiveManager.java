package com.kaltura.media.server;

import java.util.ArrayList;
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
import com.kaltura.client.enums.KalturaRecordStatus;
import com.kaltura.client.enums.KalturaSourceType;
import com.kaltura.client.types.KalturaConversionProfileAssetParams;
import com.kaltura.client.types.KalturaConversionProfileAssetParamsFilter;
import com.kaltura.client.types.KalturaConversionProfileAssetParamsListResponse;
import com.kaltura.client.types.KalturaEntryResource;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.client.types.KalturaMediaEntry;

abstract public class KalturaLiveManager implements ILiveManager {

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
	protected ConcurrentHashMap<String, LiveEntryCache> entries;
	protected Logger logger;
	protected long isLiveRegistrationMinBufferTime = KalturaLiveManager.DEFAULT_IS_LIVE_REGISTRATION_MIN_BUFFER_TIME;

	private Timer setMediaServerTimer;
	private Timer splitRecordingTimer;

	protected class LiveEntryCache {
		private KalturaLiveEntry liveEntry;
		private KalturaMediaServerIndex index = null;
		private Date registerTime = null;
		private ArrayList<KalturaConversionProfileAssetParams> conversionProfileAssetParams;

		public LiveEntryCache(KalturaLiveEntry liveEntry, KalturaMediaServerIndex index, Date registerTime) {
			this(liveEntry);
			register(index, registerTime);
		}

		public LiveEntryCache(KalturaLiveEntry liveEntry) {
			this.liveEntry = liveEntry;

			loadAssetParams();
		}

		private void loadAssetParams() {
			if(liveEntry.conversionProfileId <= 0)
				return;
			
			KalturaConversionProfileAssetParamsFilter filter = new KalturaConversionProfileAssetParamsFilter();
			filter.conversionProfileIdEqual = liveEntry.conversionProfileId;
			
			KalturaClient impersonateClient = impersonate(liveEntry.partnerId);
			try {
				KalturaConversionProfileAssetParamsListResponse conversionProfileAssetParamsList = impersonateClient.getConversionProfileAssetParamsService().list(filter);
				conversionProfileAssetParams = conversionProfileAssetParamsList.objects;
			} catch (KalturaApiException e) {
				logger.error("KalturaLiveManager::LiveEntryCache.loadAssetParams failed to load asset params for live entry [" + liveEntry.id + "]:" + e.getMessage());
			}
		}

		public void register(KalturaMediaServerIndex index, Date registerTime) {
			this.index = index;
			this.registerTime = registerTime;
		}

		public void unregister() {
			index = null;
			registerTime = null;
		}

		public boolean isRegistered() {
			if (index == null)
				return false;

			return registerTime.before(new Date());
		}

		public void setLiveEntry(KalturaLiveEntry liveEntry) {
			this.liveEntry = liveEntry;
		}

		public KalturaLiveEntry getLiveEntry() {
			return liveEntry;
		}

		public ArrayList<KalturaConversionProfileAssetParams> getConversionProfileAssetParams() {
			return conversionProfileAssetParams;
		}

		public KalturaMediaServerIndex getIndex() {
			return index;
		}
	}

	abstract protected void setEntryMediaServer(KalturaLiveEntry liveEntry, KalturaMediaServerIndex serverIndex);

	abstract protected void unsetEntryMediaServer(KalturaLiveEntry liveEntry, KalturaMediaServerIndex serverIndex);

	abstract public KalturaLiveEntry reloadEntry(String entryId, int partnerId);

	abstract public void appendRecording(String entryId, KalturaMediaServerIndex index, String filePath, float duration);

	abstract public void restartRecordings();

	protected KalturaClient impersonate(int partnerId) {
		KalturaConfiguration impersonateConfig = new KalturaConfiguration();
		impersonateConfig.setPartnerId(partnerId);
		impersonateConfig.setClientTag(config.getClientTag());
		impersonateConfig.setEndpoint(config.getEndpoint());
		impersonateConfig.setTimeout(config.getTimeout());

		KalturaClient cloneClient = new KalturaClient(impersonateConfig);
		cloneClient.setSessionId(client.getSessionId());

		return cloneClient;
	}

	public KalturaLiveEntry get(String entryId) {

		synchronized (entries) {
			if (entries.containsKey(entryId)) {
				LiveEntryCache liveEntryCache = entries.get(entryId);
				return liveEntryCache.getLiveEntry();
			}
		}

		return null;
	}

	protected KalturaConversionProfileAssetParams getConversionProfileAssetParams(String entryId, int assetParamsId) {

		synchronized (entries) {
			if (entries.containsKey(entryId)) {
				LiveEntryCache liveEntryCache = entries.get(entryId);
				ArrayList<KalturaConversionProfileAssetParams> conversionProfileAssetParamsList = liveEntryCache.getConversionProfileAssetParams();
				for(KalturaConversionProfileAssetParams conversionProfileAssetParams : conversionProfileAssetParamsList){
					if(conversionProfileAssetParams.assetParamsId == assetParamsId)
						return conversionProfileAssetParams;
				}
			}
		}

		return null;
	}

	public Integer getDvrWindow(KalturaLiveEntry liveEntry) {
		if (liveEntry.dvrStatus != KalturaDVRStatus.ENABLED)
			return null;

		int maxDvrWindow = Integer.parseInt((String) serverConfiguration.get(KalturaLiveManager.KALTURA_LIVE_STREAM_MAX_DVR_WINDOW));
		int dvrWindowSeconds = liveEntry.dvrWindow * 60;
		if (dvrWindowSeconds <= 0 || dvrWindowSeconds > maxDvrWindow)
			return maxDvrWindow;

		return dvrWindowSeconds;
	}

	@Override
	public void onPublish(String entryId, final KalturaMediaServerIndex serverIndex) {
		logger.debug("KalturaLiveManager::onPublish entry [" + entryId + "]");

		final KalturaLiveEntry liveEntry = get(entryId);
		if(liveEntry == null){
			logger.error("KalturaLiveManager::onPublish entry [" + entryId + "] not found");
			return;
		}
		
		if (serverIndex == KalturaMediaServerIndex.PRIMARY)
			cancelRedirect(liveEntry);

		TimerTask setMediaServerTask = new TimerTask() {

			@Override
			public void run() {
				setEntryMediaServer(liveEntry, serverIndex);
			}
		};

		Timer timer = new Timer();
		timer.schedule(setMediaServerTask, isLiveRegistrationMinBufferTime);

		Date registerTime = new Date();
		registerTime.setTime(registerTime.getTime() + isLiveRegistrationMinBufferTime);

		synchronized (entries) {

			if (entries.containsKey(entryId)) {
				LiveEntryCache liveEntryCache = entries.get(entryId);
				liveEntryCache.register(serverIndex, registerTime);
			} else {
				entries.put(entryId, new LiveEntryCache(liveEntry, serverIndex, registerTime));
			}
		}

		if (liveEntry.recordStatus == KalturaRecordStatus.ENABLED && serverIndex == KalturaMediaServerIndex.PRIMARY)
			createMediaEntry(liveEntry);
	}

	public void onUnPublish(KalturaLiveEntry liveEntry, KalturaMediaServerIndex serverIndex) {
		logger.debug("KalturaLiveManager::onUnPublish entry [" + liveEntry.id + "]");

		if (serverIndex == KalturaMediaServerIndex.PRIMARY)
			setRedirect(liveEntry);

		synchronized (entries) {
			if (entries.containsKey(liveEntry.id)) {
				LiveEntryCache liveEntryCache = entries.get(liveEntry.id);
				if (liveEntryCache.index != null) {
					unsetEntryMediaServer(liveEntry, liveEntryCache.index);
					liveEntryCache.unregister();
				}
			}
		}
	}

	public void onDisconnect(final String entryId) {
		logger.debug("KalturaLiveManager::onDisconnect entry [" + entryId + "]");

		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				synchronized (entries) {
					if (entries.containsKey(entryId)) {
						entries.remove(entryId);
					}
				}
			}
		};
		
		Timer delayedRemoveTimer = new Timer();
		delayedRemoveTimer.schedule(task, 5000);
	}

	protected void cancelRedirect(KalturaLiveEntry liveEntry) {
		logger.debug("KalturaLiveManager::cancelRedirect cancel live entry [" + liveEntry.id + "] redirect");

		KalturaLiveEntry updateLiveEntry;
		try {
			updateLiveEntry = liveEntry.getClass().newInstance();
		} catch (Exception e) {
			logger.error("KalturaLiveManager::cancelRedirect failed to instantiate [" + liveEntry.getClass().getName() + "]: " + e.getMessage());
			return;
		}
		updateLiveEntry.redirectEntryId = KalturaParamsValueDefaults.KALTURA_NULL_STRING;

		KalturaClient impersonateClient = impersonate(liveEntry.partnerId);
		try {
			impersonateClient.getBaseEntryService().update(liveEntry.id, updateLiveEntry);
		} catch (KalturaApiException e) {
			logger.error("KalturaLiveManager::cancelRedirect failed to upload file: " + e.getMessage());
			return;
		}
	}

	protected void setRedirect(KalturaLiveEntry liveEntry) {
		logger.debug("KalturaLiveManager::setRedirect set live entry [" + liveEntry.id + "] redirect");

		if (liveEntry.recordedEntryId == null) {
			logger.error("KalturaLiveManager::setRedirect no recorded entry id on live entry [" + liveEntry.id + "]");
			liveEntry = reloadEntry(liveEntry.id, liveEntry.partnerId);
			if (liveEntry == null)
				return;

			if (liveEntry.recordedEntryId == null) {
				logger.error("KalturaLiveManager::setRedirect no recorded entry id on live entry [" + liveEntry.id + "] after reloading");
				return;
			}
		}

		KalturaLiveEntry updateLiveEntry;
		try {
			updateLiveEntry = liveEntry.getClass().newInstance();
		} catch (Exception e) {
			logger.error("KalturaLiveManager::cancelRedirect failed to instantiate [" + liveEntry.getClass().getName() + "]: " + e.getMessage());
			return;
		}
		updateLiveEntry.redirectEntryId = liveEntry.recordedEntryId;

		KalturaClient impersonateClient = impersonate(liveEntry.partnerId);
		try {
			impersonateClient.getBaseEntryService().update(liveEntry.id, updateLiveEntry);
		} catch (KalturaApiException e) {
			logger.error("KalturaLiveManager::setRedirect failed to upload file: " + e.getMessage());
			return;
		}
	}

	protected KalturaMediaEntry createMediaEntry(KalturaLiveEntry liveEntry) {
		logger.debug("KalturaLiveManager::createMediaEntry creating media entry for live entry [" + liveEntry.id + "]");
		KalturaClient impersonateClient = impersonate(liveEntry.partnerId);

		KalturaMediaEntry mediaEntry = null;
		if (liveEntry.recordedEntryId != null) {
			try {
				mediaEntry = impersonateClient.getMediaService().get(liveEntry.recordedEntryId);
			} catch (KalturaApiException e) {
				logger.warn("KalturaLiveManager::createMediaEntry failed to get recorded media entry [" + liveEntry.recordedEntryId + "]: " + e.getMessage());
			}
		}

		if (mediaEntry == null) {
			mediaEntry = new KalturaMediaEntry();
			mediaEntry.rootEntryId = liveEntry.id;
			mediaEntry.name = liveEntry.name;
			mediaEntry.description = liveEntry.description;
			mediaEntry.sourceType = KalturaSourceType.RECORDED_LIVE;
			mediaEntry.mediaType = KalturaMediaType.VIDEO;

			try {
				mediaEntry = impersonateClient.getMediaService().add(mediaEntry);
			} catch (KalturaApiException e) {
				logger.error("KalturaLiveManager::createMediaEntry failed to create media entry: " + e.getMessage());
				return null;
			}
			logger.debug("KalturaLiveManager::createMediaEntry created media entry [" + mediaEntry.id + "] for live entry [" + liveEntry.id + "]");
		}

		synchronized (entries) {
			liveEntry.recordedEntryId = mediaEntry.id;
		}

		KalturaLiveEntry updateLiveEntry;
		try {
			updateLiveEntry = liveEntry.getClass().newInstance();
		} catch (Exception e) {
			logger.error("KalturaLiveManager::cancelRedirect failed to instantiate [" + liveEntry.getClass().getName() + "]: " + e.getMessage());
			return null;
		}
		updateLiveEntry.recordedEntryId = mediaEntry.id;
		try {
			impersonateClient.getBaseEntryService().update(liveEntry.id, updateLiveEntry);
		} catch (KalturaApiException e) {
			logger.error("KalturaLiveManager::createMediaEntry failed to upload file: " + e.getMessage());
		}

		return mediaEntry;
	}

	protected void appendRecording(KalturaLiveEntry liveEntry) {
		logger.debug("KalturaLiveManager::appendRecordingToMediaEntry creating media entry for live entry [" + liveEntry.id + "]");

		KalturaMediaEntry mediaEntry;

		KalturaEntryResource resource = new KalturaEntryResource();
		resource.entryId = liveEntry.id;

		String recordedEntryId = liveEntry.recordedEntryId;
		if (recordedEntryId == null) {
			logger.warn("KalturaLiveManager::appendRecordingToMediaEntry recorded media entry is null for entry [" + liveEntry.id + "]: reloading");
			liveEntry = reloadEntry(liveEntry.id, liveEntry.partnerId);
			recordedEntryId = liveEntry.recordedEntryId;
		}

		if (recordedEntryId == null) {
			logger.warn("KalturaLiveManager::appendRecordingToMediaEntry recorded media entry is null for entry [" + liveEntry.id + "]: creating media entry");
			mediaEntry = createMediaEntry(liveEntry);
			if (mediaEntry == null)
				logger.error("KalturaLiveManager::appendRecordingToMediaEntry recorded media entry is null for entry [" + liveEntry.id + "]: creating media entry failed");

			recordedEntryId = mediaEntry.id;
		}

		KalturaClient impersonateClient = impersonate(liveEntry.partnerId);

		try {
			impersonateClient.getMediaService().cancelReplace(recordedEntryId);
			mediaEntry = impersonateClient.getMediaService().updateContent(recordedEntryId, resource);

			if (mediaEntry.replacingEntryId != null)
				impersonateClient.getMediaService().approveReplace(recordedEntryId);

		} catch (KalturaApiException e) {
			logger.error("KalturaLiveManager::appendRecordingToMediaEntry failed to add content resource [" + recordedEntryId + "]: " + e.getMessage());
			return;
		}
	}

	public boolean isEntryRegistered(String entryId) {
		boolean registered = false;
		synchronized (entries) {
			LiveEntryCache liveEntryCache = entries.get(entryId);
			if (liveEntryCache != null)
				registered = liveEntryCache.isRegistered();
		}

		return registered;
	}

	public void init() throws KalturaManagerException {

		entries = new ConcurrentHashMap<String, LiveEntryCache>();

		hostname = KalturaServer.getHostName();
		client = KalturaServer.getClient();
		config = client.getKalturaConfiguration();
		serverConfiguration = KalturaServer.getConfiguration();
		logger = KalturaServer.getLogger();

		isLiveRegistrationMinBufferTime = KalturaLiveManager.DEFAULT_IS_LIVE_REGISTRATION_MIN_BUFFER_TIME * 1000;
		if (serverConfiguration.containsKey(KalturaLiveManager.KALTURA_IS_LIVE_REGISTRATION_MIN_BUFFER_TIME))
			isLiveRegistrationMinBufferTime = Long.parseLong((String) serverConfiguration.get(KalturaLiveManager.KALTURA_IS_LIVE_REGISTRATION_MIN_BUFFER_TIME)) * 1000;

		long keepAliveInterval = Long.parseLong((String) serverConfiguration.get(KalturaLiveManager.KALTURA_LIVE_STREAM_KEEP_ALIVE_INTERVAL)) * 1000;

		if (keepAliveInterval > 0) {
			TimerTask setMediaServerTask = new TimerTask() {

				@Override
				public void run() {
					synchronized (entries) {
						for (String entryId : entries.keySet()) {
							LiveEntryCache liveEntryCache = entries.get(entryId);
							if (liveEntryCache.isRegistered())
								setEntryMediaServer(liveEntryCache.getLiveEntry(), liveEntryCache.index);
						}
					}
				}
			};

			setMediaServerTimer = new Timer();
			setMediaServerTimer.schedule(setMediaServerTask, keepAliveInterval, keepAliveInterval);
		}

		long splitRecordingInterval = KalturaLiveManager.DEFAULT_RECORDED_CHUNCK_MAX_DURATION * 60 * 1000;
		if (serverConfiguration.containsKey(KalturaLiveManager.KALTURA_RECORDED_CHUNCK_MAX_DURATION))
			splitRecordingInterval = Long.parseLong((String) serverConfiguration.get(KalturaLiveManager.KALTURA_RECORDED_CHUNCK_MAX_DURATION)) * 60 * 1000;

		if (splitRecordingInterval > 0) {
			TimerTask splitRecordingTask = new TimerTask() {

				@Override
				public void run() {
					restartRecordings();
				}
			};

			splitRecordingTimer = new Timer(true);
			splitRecordingTimer.schedule(splitRecordingTask, splitRecordingInterval, splitRecordingInterval);
		}
	}

	public void stop() {
		setMediaServerTimer.cancel();
		setMediaServerTimer.purge();

		splitRecordingTimer.cancel();
		splitRecordingTimer.purge();
	}
}
