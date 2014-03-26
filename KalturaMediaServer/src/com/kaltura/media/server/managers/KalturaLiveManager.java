package com.kaltura.media.server.managers;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.client.KalturaFile;
import com.kaltura.client.KalturaMultiResponse;
import com.kaltura.client.KalturaParamsValueDefaults;
import com.kaltura.client.KalturaServiceBase;
import com.kaltura.client.enums.KalturaDVRStatus;
import com.kaltura.client.enums.KalturaMediaServerIndex;
import com.kaltura.client.enums.KalturaMediaType;
import com.kaltura.client.enums.KalturaRecordStatus;
import com.kaltura.client.enums.KalturaSourceType;
import com.kaltura.client.types.KalturaConversionProfileAssetParams;
import com.kaltura.client.types.KalturaConversionProfileAssetParamsFilter;
import com.kaltura.client.types.KalturaConversionProfileAssetParamsListResponse;
import com.kaltura.client.types.KalturaDataCenterContentResource;
import com.kaltura.client.types.KalturaEntryResource;
import com.kaltura.client.types.KalturaFlavorAsset;
import com.kaltura.client.types.KalturaFlavorAssetListResponse;
import com.kaltura.client.types.KalturaFlavorParams;
import com.kaltura.client.types.KalturaFlavorParamsListResponse;
import com.kaltura.client.types.KalturaLiveAsset;
import com.kaltura.client.types.KalturaLiveAssetFilter;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.client.types.KalturaLiveParams;
import com.kaltura.client.types.KalturaMediaEntry;
import com.kaltura.client.types.KalturaServerFileResource;
import com.kaltura.client.types.KalturaUploadToken;
import com.kaltura.client.types.KalturaUploadedFileTokenResource;
import com.kaltura.media.server.KalturaEventsManager;
import com.kaltura.media.server.events.IKalturaEvent;
import com.kaltura.media.server.events.IKalturaEventConsumer;
import com.kaltura.media.server.events.KalturaEventType;
import com.kaltura.media.server.events.KalturaStreamEvent;

abstract public class KalturaLiveManager extends KalturaManager implements ILiveManager, IKalturaEventConsumer {

	protected final static String KALTURA_RECORDED_CHUNCK_MAX_DURATION = "KalturaRecordedChunckMaxDuration";
	protected final static String UPLOAD_XML_SAVE_PATH = "uploadXMLSavePath";
	protected final static String KALTURA_LIVE_STREAM_KEEP_ALIVE_INTERVAL = "KalturaLiveStreamKeepAliveInterval";
	protected final static String KALTURA_LIVE_STREAM_MAX_DVR_WINDOW = "KalturaLiveStreamMaxDvrWindow";
	protected final static String KALTURA_IS_LIVE_REGISTRATION_MIN_BUFFER_TIME = "KalturaIsLiveRegistrationMinBufferTime";
	protected final static String KALTURA_WOWZA_SERVER_WORK_MODE = "KalturaWorkMode";
	
	protected final static long DEFAULT_RECORDED_CHUNCK_MAX_DURATION = 60;
	protected final static long DEFAULT_IS_LIVE_REGISTRATION_MIN_BUFFER_TIME = 5;
	protected final static String KALTURA_WOWZA_SERVER_WORK_MODE_REMOTE = "remote";
	protected final static String KALTURA_WOWZA_SERVER_WORK_MODE_KALTURA = "kaltura";

	protected static Logger logger = Logger.getLogger(KalturaLiveManager.class);
	
	protected ConcurrentHashMap<String, LiveEntryCache> entries = new ConcurrentHashMap<String, LiveEntryCache>();
	protected ConcurrentHashMap<Integer, KalturaLiveParams> liveAssetParams = new ConcurrentHashMap<Integer, KalturaLiveParams>();
	protected long isLiveRegistrationMinBufferTime = KalturaLiveManager.DEFAULT_IS_LIVE_REGISTRATION_MIN_BUFFER_TIME;

	private Timer setMediaServerTimer;
	private Timer splitRecordingTimer;

	protected class LiveEntryCache {
		private KalturaLiveEntry liveEntry;
		private boolean registered = false;
		private KalturaMediaServerIndex index = null;
		private Date registerTime = null;
		private ArrayList<KalturaConversionProfileAssetParams> conversionProfileAssetParams;
		private Map<Integer, KalturaLiveAsset> liveAssets = new HashMap<Integer, KalturaLiveAsset>();
		private Timer timer;

		public LiveEntryCache(KalturaLiveEntry liveEntry, KalturaMediaServerIndex serverIndex) {
			this(liveEntry);
			register(serverIndex);
		}

		public LiveEntryCache(KalturaLiveEntry liveEntry) {
			this.liveEntry = liveEntry;

			loadAssetParams();
		}

		private void loadAssetParams() {
			if(liveEntry.conversionProfileId <= 0)
				return;

			KalturaConversionProfileAssetParamsFilter assetParamsFilter = new KalturaConversionProfileAssetParamsFilter();
			assetParamsFilter.conversionProfileIdEqual = liveEntry.conversionProfileId;

			KalturaLiveAssetFilter asstesFilter = new KalturaLiveAssetFilter();
			asstesFilter.entryIdEqual = liveEntry.id;
			
			KalturaClient impersonateClient = impersonate(liveEntry.partnerId);
			impersonateClient.startMultiRequest();
			try {
				impersonateClient.getConversionProfileAssetParamsService().list(assetParamsFilter);
				impersonateClient.getFlavorAssetService().list(asstesFilter);
				KalturaMultiResponse responses = impersonateClient.doMultiRequest();
				
				
				Object conversionProfileAssetParamsList = responses.get(0);
				Object flavorAssetsList = responses.get(1);
				
				if(conversionProfileAssetParamsList instanceof KalturaConversionProfileAssetParamsListResponse)
					conversionProfileAssetParams = ((KalturaConversionProfileAssetParamsListResponse) conversionProfileAssetParamsList).objects;

				if(flavorAssetsList instanceof KalturaFlavorAssetListResponse){
					for(KalturaFlavorAsset liveAsset : ((KalturaFlavorAssetListResponse) flavorAssetsList).objects){
						if(liveAsset instanceof KalturaLiveAsset){
							liveAssets.put(liveAsset.flavorParamsId, (KalturaLiveAsset) liveAsset);
							
							if(!liveAssetParams.containsKey(liveAsset.flavorParamsId)){
								KalturaFlavorParams liveParams = impersonateClient.getFlavorParamsService().get(liveAsset.flavorParamsId);
								if(liveParams instanceof KalturaLiveParams)
									liveAssetParams.put(liveAsset.flavorParamsId, (KalturaLiveParams) liveParams);
							}
						}
					}
				}
				
			} catch (KalturaApiException e) {
				logger.error("Failed to load asset params for live entry [" + liveEntry.id + "]:" + e.getMessage());
			}
			impersonateClient = null;
		}

		public synchronized void register(KalturaMediaServerIndex serverIndex) {
			logger.debug("LiveEntryCache::register");
			if(registered)
				return;
			
			index = serverIndex;

			if (index == KalturaMediaServerIndex.PRIMARY)
				cancelRedirect(liveEntry);

			TimerTask setMediaServerTask = new TimerTask() {

				@Override
				public void run() {
					logger.debug("LiveEntryCache::register: initial timer task running");
					setEntryMediaServer(liveEntry, index);
				}
			};
			
			timer = new Timer();
			timer.schedule(setMediaServerTask, isLiveRegistrationMinBufferTime);
			logger.debug("LiveEntryCache::register: scheduled initial timer");

			registerTime = new Date();
			registerTime.setTime(registerTime.getTime() + isLiveRegistrationMinBufferTime);

			if (liveEntry.recordStatus == KalturaRecordStatus.ENABLED && index == KalturaMediaServerIndex.PRIMARY)
				createMediaEntry(liveEntry);
			
			registered = true;
		}

		public synchronized void unregister() {
			logger.debug("LiveEntryCache::unregister");
			KalturaMediaServerIndex tmpIndex = index;
			index = null;
			registerTime = null;

			if(timer != null){
				timer.cancel();
				timer.purge();
				timer = null;
			}
			registered = false;
			
			unsetEntryMediaServer(liveEntry, tmpIndex);
		}

		public boolean isRegistered() {
			if (!registered)
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

		public KalturaLiveAsset getLiveAsset(int assetParamsId) {
			if(liveAssets.containsKey(assetParamsId))
				return liveAssets.get(assetParamsId);
			
			logger.error("Asset params id [" + assetParamsId + "] not found");
			return null;
		}

		public KalturaLiveAsset getLiveAsset(String streamSuffix) {
			KalturaLiveParams assetParams;
			for(KalturaLiveAsset liveAsset : liveAssets.values()){
				assetParams = getLiveAssetParams(liveAsset.flavorParamsId);
				if(assetParams != null && assetParams.streamSuffix != null && assetParams.streamSuffix.equals(streamSuffix)){
					return liveAsset;
				}
			}
			
			logger.error("Asset with stream suffix [" + streamSuffix + "] not found");
			return null;
		}

		public KalturaMediaServerIndex getIndex() {
			return index;
		}
	}

	abstract public void restartRecordings();
	
	abstract public KalturaServiceBase getLiveServiceInstance (KalturaClient impersonateClient);

	public KalturaLiveEntry get(String entryId) {

		synchronized (entries) {
			if (entries.containsKey(entryId)) {
				LiveEntryCache liveEntryCache = entries.get(entryId);
				return liveEntryCache.getLiveEntry();
			}
		}

		return null;
	}

	public KalturaLiveParams getLiveAssetParams(int assetParamsId){

		synchronized (liveAssetParams) {
			if (!liveAssetParams.containsKey(assetParamsId)) {
				logger.error("Asset params id [" + assetParamsId + "] not found");
				return null;
			}
			
			return liveAssetParams.get(assetParamsId);
		}
	}
	
	public KalturaLiveAsset getLiveAsset(String entryId, int assetParamsId) {

		synchronized (entries) {
			if (!entries.containsKey(entryId)) {
				logger.error("Entry id [" + entryId + "] not found");
				return null;
			}
			
			LiveEntryCache liveEntryCache = entries.get(entryId);
			return liveEntryCache.getLiveAsset(assetParamsId);
		}
	}
	
	public KalturaLiveAsset getLiveAsset(String entryId, String streamSuffix) {

		synchronized (entries) {
			if (!entries.containsKey(entryId)) {
				logger.error("Entry id [" + entryId + "] not found");
				return null;
			}
			
			LiveEntryCache liveEntryCache = entries.get(entryId);
			return liveEntryCache.getLiveAsset(streamSuffix);
		}
	}
	
	public KalturaConversionProfileAssetParams getConversionProfileAssetParams(String entryId, int assetParamsId) {

		synchronized (entries) {
			if (!entries.containsKey(entryId)) {
				logger.error("Entry id [" + entryId + "] not found");
				return null;
			}
			
			LiveEntryCache liveEntryCache = entries.get(entryId);
			ArrayList<KalturaConversionProfileAssetParams> conversionProfileAssetParamsList = liveEntryCache.getConversionProfileAssetParams();
			for(KalturaConversionProfileAssetParams conversionProfileAssetParams : conversionProfileAssetParamsList){
				if(conversionProfileAssetParams.assetParamsId == assetParamsId)
					return conversionProfileAssetParams;
			}
		}

		logger.error("Asset id [" + assetParamsId + "] in entry [" + entryId + "] not found");
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
	public void onEvent(IKalturaEvent event){
		KalturaStreamEvent streamEvent;

		if(event.getType() instanceof KalturaEventType){
			switch((KalturaEventType) event.getType())
			{
				case STREAM_PUBLISHED:
					streamEvent = (KalturaStreamEvent) event;
					onPublish(streamEvent.getEntryId(), streamEvent.getServerIndex());
					break;
		
				case STREAM_UNPUBLISHED:
					streamEvent = (KalturaStreamEvent) event;
					onUnPublish(streamEvent.getEntry(), streamEvent.getServerIndex());
					break;
		
				case STREAM_DISCONNECTED:
					streamEvent = (KalturaStreamEvent) event;
					onDisconnect(streamEvent.getEntryId());
					break;
					
				default:
					break;
			}
		}
	}
	
	protected void onPublish(String entryId, final KalturaMediaServerIndex serverIndex) {
		logger.debug("Entry [" + entryId + "]");

		synchronized (entries) {

			if (entries.containsKey(entryId)){
				LiveEntryCache liveEntryCache = entries.get(entryId);
				liveEntryCache.register(serverIndex);
			}
			else{
				logger.error("entry [" + entryId + "] not found in entries array");
			}
		}
	}

	protected void onUnPublish(KalturaLiveEntry liveEntry, KalturaMediaServerIndex serverIndex) {
		logger.debug("entry [" + liveEntry.id + "]");

		if (serverIndex == KalturaMediaServerIndex.PRIMARY)
			setRedirect(liveEntry);

		synchronized (entries) {
			if (entries.containsKey(liveEntry.id)) {
				LiveEntryCache liveEntryCache = entries.get(liveEntry.id);
				if (liveEntryCache.index != null) {
					liveEntryCache.unregister();
				}
			}
		}
	}

	protected void onDisconnect(final String entryId) {
		logger.debug("entry [" + entryId + "]");

		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				synchronized (entries) {
					if (entries.containsKey(entryId)) {
						LiveEntryCache liveEntryCache = entries.remove(entryId);
						if (liveEntryCache.index != null) {
							liveEntryCache.unregister();
						}
					}
				}
			}
		};
		
		Timer delayedRemoveTimer = new Timer();
		delayedRemoveTimer.schedule(task, 5000);
	}

	protected void cancelRedirect(KalturaLiveEntry liveEntry) {
		logger.debug("cancel live entry [" + liveEntry.id + "] redirect");

		KalturaLiveEntry updateLiveEntry;
		try {
			updateLiveEntry = liveEntry.getClass().newInstance();
		} catch (Exception e) {
			logger.error("failed to instantiate [" + liveEntry.getClass().getName() + "]: " + e.getMessage());
			return;
		}
		updateLiveEntry.redirectEntryId = KalturaParamsValueDefaults.KALTURA_NULL_STRING;

		KalturaClient impersonateClient = impersonate(liveEntry.partnerId);
		try {
			impersonateClient.getBaseEntryService().update(liveEntry.id, updateLiveEntry);
		} catch (KalturaApiException e) {
			logger.error("failed to upload file: " + e.getMessage());
		}
		impersonateClient = null;
	}

	protected void setRedirect(KalturaLiveEntry liveEntry) {
		logger.debug("set live entry [" + liveEntry.id + "] redirect");

		if (liveEntry.recordedEntryId == null) {
			logger.error("no recorded entry id on live entry [" + liveEntry.id + "]");
			liveEntry = reloadEntry(liveEntry.id, liveEntry.partnerId);
			if (liveEntry == null)
				return;

			if (liveEntry.recordedEntryId == null) {
				logger.error("no recorded entry id on live entry [" + liveEntry.id + "] after reloading");
				return;
			}
		}

		KalturaLiveEntry updateLiveEntry;
		try {
			updateLiveEntry = liveEntry.getClass().newInstance();
		} catch (Exception e) {
			logger.error("failed to instantiate [" + liveEntry.getClass().getName() + "]: " + e.getMessage());
			return;
		}
		updateLiveEntry.redirectEntryId = liveEntry.recordedEntryId;

		KalturaClient impersonateClient = impersonate(liveEntry.partnerId);
		try {
			impersonateClient.getBaseEntryService().update(liveEntry.id, updateLiveEntry);
		} catch (KalturaApiException e) {
			logger.error("failed to upload file: " + e.getMessage());
		}
		impersonateClient = null;
	}

	protected KalturaMediaEntry createMediaEntry(KalturaLiveEntry liveEntry) {
		logger.debug("creating media entry for live entry [" + liveEntry.id + "]");
		KalturaClient impersonateClient = impersonate(liveEntry.partnerId);

		KalturaMediaEntry mediaEntry = null;
		if (liveEntry.recordedEntryId != null) {
			try {
				mediaEntry = impersonateClient.getMediaService().get(liveEntry.recordedEntryId);
			} catch (KalturaApiException e) {
				logger.warn("failed to get recorded media entry [" + liveEntry.recordedEntryId + "]: " + e.getMessage());
			}
		}

		if (mediaEntry == null) {
			mediaEntry = new KalturaMediaEntry();
			mediaEntry.rootEntryId = liveEntry.id;
			mediaEntry.name = liveEntry.name;
			mediaEntry.description = liveEntry.description;
			mediaEntry.sourceType = KalturaSourceType.RECORDED_LIVE;
			mediaEntry.mediaType = KalturaMediaType.VIDEO;
			mediaEntry.accessControlId = liveEntry.accessControlId;
			mediaEntry.userId = liveEntry.userId;

			try {
				mediaEntry = impersonateClient.getMediaService().add(mediaEntry);
			} catch (KalturaApiException e) {
				impersonateClient = null;
				logger.error("failed to create media entry: " + e.getMessage());
				return null;
			}
			logger.debug("created media entry [" + mediaEntry.id + "] for live entry [" + liveEntry.id + "]");
		}

		synchronized (entries) {
			liveEntry.recordedEntryId = mediaEntry.id;
		}

		KalturaLiveEntry updateLiveEntry;
		try {
			updateLiveEntry = liveEntry.getClass().newInstance();
		} catch (Exception e) {
			impersonateClient = null;
			logger.error("failed to instantiate [" + liveEntry.getClass().getName() + "]: " + e.getMessage());
			return null;
		}
		updateLiveEntry.recordedEntryId = mediaEntry.id;
		try {
			impersonateClient.getBaseEntryService().update(liveEntry.id, updateLiveEntry);
		} catch (KalturaApiException e) {
			logger.error("failed to upload file: " + e.getMessage());
		}
		impersonateClient = null;

		return mediaEntry;
	}

	protected void appendRecording(KalturaLiveEntry liveEntry) {
		logger.debug("creating media entry for live entry [" + liveEntry.id + "]");

		KalturaMediaEntry mediaEntry;

		KalturaEntryResource resource = new KalturaEntryResource();
		resource.entryId = liveEntry.id;

		String recordedEntryId = liveEntry.recordedEntryId;
		if (recordedEntryId == null) {
			logger.warn("recorded media entry is null for entry [" + liveEntry.id + "]: reloading");
			liveEntry = reloadEntry(liveEntry.id, liveEntry.partnerId);
			recordedEntryId = liveEntry.recordedEntryId;
		}

		if (recordedEntryId == null) {
			logger.warn("recorded media entry is null for entry [" + liveEntry.id + "]: creating media entry");
			mediaEntry = createMediaEntry(liveEntry);
			if (mediaEntry == null)
				logger.error("recorded media entry is null for entry [" + liveEntry.id + "]: creating media entry failed");

			recordedEntryId = mediaEntry.id;
		}

		KalturaClient impersonateClient = impersonate(liveEntry.partnerId);

		try {
			impersonateClient.getMediaService().cancelReplace(recordedEntryId);
			mediaEntry = impersonateClient.getMediaService().updateContent(recordedEntryId, resource);

			if (mediaEntry.replacingEntryId != null)
				impersonateClient.getMediaService().approveReplace(recordedEntryId);

		} catch (KalturaApiException e) {
			logger.error("failed to add content resource [" + recordedEntryId + "]: " + e.getMessage());
		}
		impersonateClient = null;
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

		super.init();
		loadLiveParams();
		
		isLiveRegistrationMinBufferTime = KalturaLiveManager.DEFAULT_IS_LIVE_REGISTRATION_MIN_BUFFER_TIME * 1000;
		if (serverConfiguration.containsKey(KalturaLiveManager.KALTURA_IS_LIVE_REGISTRATION_MIN_BUFFER_TIME))
			isLiveRegistrationMinBufferTime = Long.parseLong((String) serverConfiguration.get(KalturaLiveManager.KALTURA_IS_LIVE_REGISTRATION_MIN_BUFFER_TIME)) * 1000;

		long keepAliveInterval = Long.parseLong((String) serverConfiguration.get(KalturaLiveManager.KALTURA_LIVE_STREAM_KEEP_ALIVE_INTERVAL)) * 1000;

		if (keepAliveInterval > 0) {
			TimerTask setMediaServerTask = new TimerTask() {

				@Override
				public void run() {
					logger.debug("running scheduled task");
					synchronized (entries) {
						for (String entryId : entries.keySet()) {
							LiveEntryCache liveEntryCache = entries.get(entryId);
							logger.debug("handling entry " + entryId);
							if (liveEntryCache.isRegistered()) {
								logger.debug("re-registering media server");
								setEntryMediaServer(liveEntryCache.getLiveEntry(), liveEntryCache.index);
							}
						}
					}
				}
			};

			setMediaServerTimer = new Timer();
			setMediaServerTimer.schedule(setMediaServerTask, keepAliveInterval, keepAliveInterval);
			logger.debug("scheduled setMediaServerTask");
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
		
		KalturaEventsManager.registerEventConsumer(this, KalturaEventType.STREAM_PUBLISHED, KalturaEventType.STREAM_UNPUBLISHED, KalturaEventType.STREAM_DISCONNECTED);
	}

	private void loadLiveParams() {
		try {
			KalturaFlavorParamsListResponse flavorParamsList = client.getFlavorParamsService().list();
			for(KalturaFlavorParams flavorParams : flavorParamsList.objects){
				if(flavorParams instanceof KalturaLiveParams)
					liveAssetParams.put(flavorParams.id, (KalturaLiveParams) flavorParams);
			}
			logger.info("loaded live params [" + liveAssetParams.size() + "]");
		} catch (KalturaApiException e) {
			logger.error("failed to load live params: " + e.getMessage());
		}
	}

	public void stop() {
		setMediaServerTimer.cancel();
		setMediaServerTimer.purge();

		splitRecordingTimer.cancel();
		splitRecordingTimer.purge();
	}
	
	public void appendRecording(String entryId, KalturaMediaServerIndex index, String filePath, float duration) {

		logger.info("KalturaLiveManager::appendRecording: entry [" + entryId + "] index [" + index + "] filePath [" + filePath + "] duration [" + duration + "]");
		
		KalturaLiveEntry liveEntry = get(entryId);
		if (serverConfiguration.containsKey(KalturaLiveManager.UPLOAD_XML_SAVE_PATH))
		{
			boolean result = saveUploadAsXml (entryId, index, filePath, duration, liveEntry.partnerId);
			if (result) {
				liveEntry.msDuration += duration;
				LiveEntryCache liveEntryCache = entries.get(entryId);
				liveEntryCache.setLiveEntry(liveEntry);
				return;
			}
				
		}
		
		KalturaDataCenterContentResource resource = getContentResource(filePath, liveEntry);
		
		KalturaClient impersonateClient = impersonate(liveEntry.partnerId);
		KalturaServiceBase liveServiceInstance = getLiveServiceInstance(impersonateClient);
		
		try {
			
			Method method = liveServiceInstance.getClass().getMethod("appendRecording", String.class, KalturaMediaServerIndex.class, KalturaDataCenterContentResource.class, float.class);
			KalturaLiveEntry updatedEntry = (KalturaLiveEntry)method.invoke(liveServiceInstance, entryId, index, resource, duration);
			
			synchronized (entries) {
				LiveEntryCache liveEntryCache = entries.get(entryId);
				liveEntryCache.setLiveEntry(updatedEntry);
			}
			
			if(liveEntry.recordStatus == KalturaRecordStatus.ENABLED && index == KalturaMediaServerIndex.PRIMARY)
				appendRecording(liveEntry);
			
		}  catch (Exception e) {
			logger.error("Unexpected error occurred: " + e.getMessage());
		}
		
		impersonateClient = null;
		
	}
	
	protected KalturaDataCenterContentResource getContentResource (String filePath, KalturaLiveEntry liveEntry) {
		if (!this.serverConfiguration.containsKey(KALTURA_WOWZA_SERVER_WORK_MODE) || !(this.serverConfiguration.get(KALTURA_WOWZA_SERVER_WORK_MODE).equals(KALTURA_WOWZA_SERVER_WORK_MODE_KALTURA))) {
			KalturaServerFileResource resource = new KalturaServerFileResource();
			resource.localFilePath = filePath;
			return resource;
		}
		else {
			KalturaClient impersonateClient = impersonate(liveEntry.partnerId);
			try {
				impersonateClient.startMultiRequest();
				impersonateClient.getUploadTokenService().add(new KalturaUploadToken());
				
				File fileData = new File(filePath);
				impersonateClient.getUploadTokenService().upload("{1:result:id}", new KalturaFile(fileData));
				KalturaMultiResponse responses = impersonateClient.doMultiRequest();
				
				KalturaUploadedFileTokenResource resource = new KalturaUploadedFileTokenResource();
				Object response = responses.get(1);
				if (response instanceof KalturaUploadToken)
					resource.token = ((KalturaUploadToken)response).id;
				else {
					if (response instanceof KalturaApiException) {
				}
					logger.error("Content resource creation error: " + ((KalturaApiException)response).getMessage());
					return null;
				}
					
				return resource;
				
			} catch (KalturaApiException e) {
				logger.error("Content resource creation error: " + e.getMessage());
			}
			impersonateClient = null;
		}
		
		return null;
	}
	
	protected void setEntryMediaServer(KalturaLiveEntry liveChannel, KalturaMediaServerIndex serverIndex) {
		KalturaClient impersonateClient = impersonate(liveChannel.partnerId);
		KalturaServiceBase liveServiceInstance = getLiveServiceInstance(impersonateClient);
		try {
			Method method = liveServiceInstance.getClass().getMethod("registerMediaServer", String.class, String.class, KalturaMediaServerIndex.class);
			method.invoke(liveServiceInstance, liveChannel.id, hostname, serverIndex);
		} catch (Exception e) {
			logger.error("KalturaLiveManager::setEntryMediaServer unable to register media server: " + e.getMessage());
		}
		impersonateClient = null;
	}

	protected void unsetEntryMediaServer(KalturaLiveEntry liveChannel, KalturaMediaServerIndex serverIndex) {
		KalturaClient impersonateClient = impersonate(liveChannel.partnerId);
		KalturaServiceBase liveServiceInstance = getLiveServiceInstance(impersonateClient);
		try {
			Method method = liveServiceInstance.getClass().getMethod("unregisterMediaServer", String.class, String.class, KalturaMediaServerIndex.class);
			method.invoke(liveServiceInstance, liveChannel.id, hostname, serverIndex);
		} catch (Exception e) {
			logger.error("KalturaLiveManager::unsetEntryMediaServer unable to unregister media server: " + e.getMessage());
		}
		impersonateClient = null;
	}
	
	public KalturaLiveEntry reloadEntry(String entryId, int partnerId) {
		KalturaClient impersonateClient = impersonate(partnerId);
		KalturaServiceBase liveServiceInstance = getLiveServiceInstance(impersonateClient);
		KalturaLiveEntry liveEntry;
		try {
			Method method = liveServiceInstance.getClass().getMethod("get", String.class);
			liveEntry = (KalturaLiveEntry)method.invoke(liveServiceInstance, entryId);
			impersonateClient = null;
		} catch (Exception e) {
			impersonateClient = null;
			logger.error("KalturaLiveManager::reloadEntry unable to get entry [" + entryId + "]: " + e.getMessage());
			return null;
		}

		synchronized (entries) {
			LiveEntryCache liveEntryCache = entries.get(entryId);
			liveEntryCache.setLiveEntry(liveEntry);
		}
		
		return liveEntry;
	}
	
	protected boolean saveUploadAsXml (String entryId, KalturaMediaServerIndex index, String filePath, float duration, int partnerId)
	{
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("upload");
			doc.appendChild(rootElement);
			
			// entryId element
			Element entryIdElem = doc.createElement("entryId");
			entryIdElem.appendChild(doc.createTextNode(entryId));
			rootElement.appendChild(entryIdElem);
			
			// partnerId element
			Element partnerIdElem = doc.createElement("partnerId");
			partnerIdElem.appendChild(doc.createTextNode(Integer.toString(partnerId)));
			rootElement.appendChild(partnerIdElem);
			
			// index element
			Element indexElem = doc.createElement("index");
			indexElem.appendChild(doc.createTextNode(Integer.toString(index.hashCode)));
			rootElement.appendChild(indexElem);
			
			// duration element
			Element durationElem = doc.createElement("duration");
			durationElem.appendChild(doc.createTextNode(Float.toString(duration)));
			rootElement.appendChild(durationElem);
			
			// filepath element
			Element filepathElem = doc.createElement("filepath");
			filepathElem.appendChild(doc.createTextNode(filePath));
			rootElement.appendChild(filepathElem);
			
			// workmode element
			String workmode = serverConfiguration.containsKey(KalturaLiveManager.KALTURA_WOWZA_SERVER_WORK_MODE) ? (String)serverConfiguration.get(KalturaLiveManager.KALTURA_WOWZA_SERVER_WORK_MODE) : KalturaLiveManager.KALTURA_WOWZA_SERVER_WORK_MODE_KALTURA;
			Element workmodeElem = doc.createElement("workMode");
			workmodeElem.appendChild(doc.createTextNode(workmode));
			rootElement.appendChild(workmodeElem);
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			
			String xmlFilePath = (String)serverConfiguration.get(KalturaLiveManager.UPLOAD_XML_SAVE_PATH) + "/" + entryId + "_" + System.currentTimeMillis() + ".xml";
			StreamResult result = new StreamResult(new File(xmlFilePath));
	 
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
	 
			transformer.transform(source, result);
	 
			logger.info("Upload XML saved at: " + xmlFilePath);
			return true;
		}
		catch (Exception e) {
			logger.error("Error occurred creating upload XML: " + e.getMessage());
			return false;
		}
	}
	
}
