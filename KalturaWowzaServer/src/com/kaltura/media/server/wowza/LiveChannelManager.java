package com.kaltura.media.server.wowza;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.client.enums.KalturaFileSyncObjectType;
import com.kaltura.client.enums.KalturaFlavorAssetStatus;
import com.kaltura.client.enums.KalturaMediaServerIndex;
import com.kaltura.client.enums.KalturaNullableBoolean;
import com.kaltura.client.services.KalturaFileSyncService;
import com.kaltura.client.types.KalturaAssetFilter;
import com.kaltura.client.types.KalturaBaseEntry;
import com.kaltura.client.types.KalturaFileSync;
import com.kaltura.client.types.KalturaFileSyncFilter;
import com.kaltura.client.types.KalturaFileSyncListResponse;
import com.kaltura.client.types.KalturaFilterPager;
import com.kaltura.client.types.KalturaFlavorAsset;
import com.kaltura.client.types.KalturaFlavorAssetListResponse;
import com.kaltura.client.types.KalturaFlavorParams;
import com.kaltura.client.types.KalturaFlavorParamsListResponse;
import com.kaltura.client.types.KalturaLiveChannel;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.client.types.KalturaLiveParams;
import com.kaltura.client.types.KalturaLiveStreamEntry;
import com.kaltura.infra.StringUtils;
import com.kaltura.media.server.managers.KalturaLiveChannelManager;
import com.kaltura.media.server.managers.KalturaManagerException;
import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.stream.publish.Playlist;
import com.wowza.wms.stream.publish.Stream;
import com.wowza.wms.vhost.IVHost;
import com.wowza.wms.vhost.VHostSingleton;

public class LiveChannelManager extends KalturaLiveChannelManager {

	protected final static String KALTURA_CHANNELS_VHOST_NAME = "KalturaChannelsVHostName";
	protected final static String KALTURA_CHANNELS_APP_NAME = "KalturaChannelsApplicationName";

	protected final static String DEFAULT_CHANNELS_APP_NAME = "kLive";

	private RecordingManager recordingManager;
	private IApplicationInstance appInstance;

	class LiveChannelContainer {
		protected final static int FILE_SYNC_ASSET_SUB_TYPE_ASSET = 1;

		private KalturaLiveChannel liveChannel;

		private Set<String> renditions = new HashSet<String>();
		private Map<String, Long> bitrates = new HashMap<String, Long>();
		private Map<String, Playlist> playlists = new HashMap<String, Playlist>();
		
		private Map<String, Integer> flavorParams = new HashMap<String, Integer>();
		private Map<String, Integer> liveParams = new HashMap<String, Integer>();
		
		private Map<String, String> fileSyncKeys = new HashMap<String, String>();
		private Set<Integer> assetParamsIds = new HashSet<Integer>();

		public LiveChannelContainer(KalturaLiveChannel liveChannel) {
			this(liveChannel, null);
		}
			
		public LiveChannelContainer(KalturaLiveChannel liveChannel, List<KalturaBaseEntry> segmentEntries) {
			this.liveChannel = liveChannel;
			
			if(segmentEntries != null)
				initSegmentEntries(segmentEntries);
		}
		
		private void initSegmentEntries(List<KalturaBaseEntry> segmentEntries){
			
			KalturaFilterPager pager = new KalturaFilterPager();
			pager.pageSize = 500;

			KalturaAssetFilter assetsFilter = new KalturaAssetFilter();
			KalturaFlavorAssetListResponse assetsList;

			String fileSyncKey;

			KalturaClient impersonateClient = impersonate(liveChannel.partnerId);
			try {

				// compose a list of all needed asset params
				for (KalturaBaseEntry entry : segmentEntries) {

					assetsFilter.entryIdEqual = entry.id;

					assetsList = impersonateClient.getFlavorAssetService().list(assetsFilter, pager);

					for (KalturaFlavorAsset flavorAsset : assetsList.objects) {
						
						if(flavorAsset.status != KalturaFlavorAssetStatus.READY || flavorAsset.isOriginal)
							continue;
						
						fileSyncKey = flavorAsset.entryId + "_" + flavorAsset.flavorParamsId;
						logger.debug("Associate file sync key [" + fileSyncKey + "] with flavor asset [" + flavorAsset.id + "]");
						fileSyncKeys.put(flavorAsset.id, fileSyncKey);
						assetParamsIds.add(flavorAsset.flavorParamsId);
					}
				}

				// copmose maps for live and flavor params system names
				KalturaFlavorParamsListResponse assetsParams = impersonateClient.getFlavorParamsService().list(null, pager);
				String systemName;
				long bitrate;
				String rendition;
				
				for (KalturaFlavorParams assetsParamsItem : assetsParams.objects) {

					if (!assetParamsIds.contains(assetsParamsItem.id))
						continue;

					systemName = getFlavorParamsSystemName(assetsParamsItem);

					if (assetsParamsItem instanceof KalturaLiveParams) {
						liveParams.put(systemName, assetsParamsItem.id);
					} else {
						flavorParams.put(systemName, assetsParamsItem.id);
					}

					bitrate = assetsParamsItem.videoBitrate * 1000;
					rendition = liveChannel.id + "_" + systemName;
					if(bitrates.containsKey(rendition))
						bitrate = Math.max(bitrates.get(rendition), bitrate);
					bitrates.put(rendition, bitrate);

					assetParamsIds.remove(assetsParamsItem.id);
				}
				
				impersonateClient = null;
			} catch (KalturaApiException e) {
				impersonateClient = null;
				logger.error("Failed to start channel [" + liveChannel.id + "]: " + e.getMessage());
				return;
			}

			initRenditions();
			initPlaylists(segmentEntries);
		}

		private String getFlavorParamsSystemName(KalturaFlavorParams flavorParams) {
			
			if (flavorParams.systemName == null)
				return "" + flavorParams.id;
			
			String systemName = flavorParams.systemName
			.replace(' ', '_')
			.replace('-', '_')
			.replace("(", "")
			.replace(")", "")
			.replace("/", "")
			.replace("\\", "")
			.replace("[", "")
			.replace("]", "");
			
			return systemName;
		}

		private void initRenditions() {

			// validate that all asset params found
			if (!assetParamsIds.isEmpty()) {
				logger.error("Not all assets params found for channel [" + liveChannel.id + "]: " + StringUtils.join(assetParamsIds));
				return;
			}

			// make sure that there are asset params
			if (liveParams.isEmpty() && flavorParams.isEmpty()) {
				logger.error("No asset params items found for channel [" + liveChannel.id + "]");
				return;
			}

			// validates that live params and flavors params are matching each
			// other's system names
			if (!liveParams.isEmpty() && !flavorParams.isEmpty()) {
				for (String systemName : liveParams.keySet()) {
					if (!flavorParams.containsKey(systemName)) {
						logger.error("Live params [" + systemName + "] don't have a matching system name in the flavor params items for channel [" + liveChannel.id + "]");
						return;
					}
				}
				for (String systemName : flavorParams.keySet()) {
					if (!liveParams.containsKey(systemName)) {
						logger.error("Flavor params [" + systemName + "] don't have a matching system name in the live params items for channel [" + liveChannel.id + "]");
						return;
					}
				}
			}

			// compose list of all renditions
			if (flavorParams.isEmpty()) {
				for (String systemName : liveParams.keySet()) {
					renditions.add(systemName);
				}
			} else {
				for (String systemName : flavorParams.keySet()) {
					renditions.add(systemName);
				}
			}
		}

		private Map<String, KalturaFileSync> getFileSyncs(List<KalturaBaseEntry> segmentEntries) {

			KalturaFilterPager pager = new KalturaFilterPager();
			pager.pageSize = 500;

			KalturaFileSyncFilter fileSyncFilter = new KalturaFileSyncFilter();
			fileSyncFilter.fileObjectTypeEqual = KalturaFileSyncObjectType.ASSET;
			fileSyncFilter.objectSubTypeEqual = LiveChannelContainer.FILE_SYNC_ASSET_SUB_TYPE_ASSET;
			fileSyncFilter.objectIdIn = StringUtils.join(fileSyncKeys.keySet());

			KalturaFileSyncService fileSyncService = new KalturaFileSyncService(client);
			
			Map<String, KalturaFileSync> fileSyncs = new HashMap<String, KalturaFileSync>();
			try {
				KalturaFileSyncListResponse fileSyncsList;
				String fileSyncKey;
				
				do {
					fileSyncsList = fileSyncService.list(fileSyncFilter, pager);
					
					for (KalturaFileSync fileSync : fileSyncsList.objects) {
						fileSyncKey = fileSyncKeys.get(fileSync.objectId);
						if(!fileSyncs.containsKey(fileSyncKey) || Integer.parseInt(fileSyncs.get(fileSyncKey).version) < Integer.parseInt(fileSync.version)){
							fileSyncs.put(fileSyncKey, fileSync);
							logger.debug("Add file sync [" + fileSync.id + "] for flavor asset [" + fileSync.objectId + "] with key [" + fileSyncKey + "]");
						}
					}
					pager.pageIndex++;
					
				} while (fileSyncsList.objects.size() == pager.pageSize);
	
			} catch (KalturaApiException e) {
				logger.error("Failed to start channel [" + liveChannel.id + "]: " + e.getMessage());
				return null;
			}
			
			return fileSyncs;
		}

		private void initPlaylists(List<KalturaBaseEntry> segmentEntries) {

			Map<String, KalturaFileSync> fileSyncs = getFileSyncs(segmentEntries);
			
			for (String rendition : renditions) {
				Playlist playlist = new Playlist("pl_" + liveChannel.id + "_" + rendition);
				playlist.setRepeat(liveChannel.repeat == KalturaNullableBoolean.TRUE_VALUE);

				if(segmentEntries != null){

					String fileSyncKey;
					for (KalturaBaseEntry entry : segmentEntries) {
	
						if (entry instanceof KalturaLiveStreamEntry) {
							playlist.addItem(entry.id + "_" + rendition, 0, -1);
						} else {
							int assetsParamsId = flavorParams.get(rendition);
							fileSyncKey = entry.id + "_" + assetsParamsId;
							
							logger.debug("Adding file sync key [" + fileSyncKey + "] to playlist [" + playlist.getName() + "]");
							KalturaFileSync fileSync = fileSyncs.get(fileSyncKey);
							
							if(fileSync != null){
								logger.debug("Adding file  [" + fileSync.filePath + "] to playlist [" + playlist.getName() + "]");
								playlist.addItem("mp4:" + fileSync.filePath, 0, -1);
							}
							else{
								logger.error("File sync key [" + fileSyncKey + "] not found");
								break;
							}
						}
					}
				}

				playlists.put(rendition, playlist);
			}
		}

		public void start() {
			Stream stream;
			Map<String, Stream> streams = new HashMap<String, Stream>();
			
			for(String rendition : renditions){
				stream = Stream.createInstance(appInstance, liveChannel.id + "_" + rendition);
				streams.put(rendition, stream);
			}

			long startTime = System.currentTimeMillis();
			for(String rendition : renditions){
				stream = streams.get(rendition);
				playlists.get(rendition).open(stream);
			}
			long time = System.currentTimeMillis() - startTime;
			
			logger.info("LiveChannelContainer::start started channel [" + liveChannel.id + "] renditions [" + StringUtils.join(renditions) + "] start time [" + time + "]");
			
			SmilManager.generate(appInstance, liveChannel.id, liveChannel.id + "_all", bitrates, liveChannel.conversionProfileId);
			
			onPublish(liveChannel.id, KalturaMediaServerIndex.PRIMARY, appInstance.getApplication().getName()); // TODO support fallback
		}
	}

	@Override
	public void init() throws KalturaManagerException {
		super.init();

		String vhostName = IVHost.VHOST_DEFAULT;
		String appName = LiveChannelManager.DEFAULT_CHANNELS_APP_NAME;

		if (serverConfiguration.containsKey(LiveChannelManager.KALTURA_CHANNELS_VHOST_NAME))
			vhostName = (String) serverConfiguration.get(LiveChannelManager.KALTURA_CHANNELS_VHOST_NAME);
		if (serverConfiguration.containsKey(LiveChannelManager.KALTURA_CHANNELS_APP_NAME))
			appName = (String) serverConfiguration.get(LiveChannelManager.KALTURA_CHANNELS_APP_NAME);

		loadApplicationInstance(vhostName, appName);
		recordingManager = new RecordingManager(this);
		setInitialized();
	}

	private void loadApplicationInstance(String vhostName, String appName) {
		loadApplicationInstance(vhostName, appName, null);
	}

	private void loadApplicationInstance(String vhostName, String appName, String appInstanceName) {
		if(appInstanceName == null)
			appInstanceName = IApplicationInstance.DEFAULT_APPINSTANCE_NAME;
		
		List<?> vhostNames = VHostSingleton.getVHostNames();
		Iterator<?> vhostIterator = vhostNames.iterator();
		while (vhostIterator.hasNext()) {
			logger.debug("LiveChannelManager::loadApplicationInstance found VHost instance [" + (String) vhostIterator.next() + "]");
		}

		IVHost vhost = VHostSingleton.getInstance(vhostName);
		if (vhost == null) {
			logger.error("LiveChannelManager::loadApplicationInstance unable get VHost instance [" + vhostName + "]");
		} else {
			if (vhost.startApplicationInstance(appName, appInstanceName)) {
				appInstance = vhost.getApplication(appName).getAppInstance(appInstanceName);
				appInstance.setApplicationTimeout(0);
				logger.debug("LiveChannelManager::loadApplicationInstance application instance [" + appName + "/" + appInstanceName + ") started");
			} else {
				logger.warn("LiveChannelManager::loadApplicationInstance application folder ([install-location]/applications/" + appName + ") is missing");
			}
		}
	}

	protected boolean restartRecording(String entryId) {
		logger.debug("LiveStreamEntry::restartRecording: " + entryId);
		return recordingManager.restart(entryId);
	}

	public String startRecord(String entryId, String assetId, IMediaStream stream, KalturaMediaServerIndex index, boolean versionFile, boolean startOnKeyFrame, boolean recordData) {
		logger.debug("LiveStreamEntry::startRecord: " + entryId);
		return recordingManager.start(entryId, assetId, stream, index, versionFile, startOnKeyFrame, recordData);
	}

	@Override
	public void start(KalturaLiveChannel liveChannel, List<KalturaBaseEntry> segmentEntries) {
		LiveChannelContainer liveChannelContainer = new LiveChannelContainer(liveChannel, segmentEntries);
		liveChannelContainer.start();
	}

	@Override
	protected void disconnectStream(String entryId) {
		// TODO
	}
	
	protected void entryStillAlive(KalturaLiveEntry liveEntry, KalturaMediaServerIndex serverIndex, String appName){
		super.entryStillAlive(liveEntry, serverIndex, appName);
		
		SmilManager.updateSmils(liveEntry.id);
	}
}
