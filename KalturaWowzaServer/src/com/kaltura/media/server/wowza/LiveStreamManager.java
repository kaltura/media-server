package com.kaltura.media.server.wowza;

import java.util.Arrays;
import java.util.List;

import com.kaltura.client.enums.KalturaAssetParamsOrigin;
import com.kaltura.client.enums.KalturaMediaServerIndex;
import com.kaltura.client.types.KalturaConversionProfileAssetParams;
import com.kaltura.client.types.KalturaLiveAsset;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.media.server.KalturaLiveStreamManager;
import com.kaltura.media.server.KalturaManagerException;
import com.wowza.wms.stream.IMediaStream;

public class LiveStreamManager extends KalturaLiveStreamManager {

	protected final static String KALTURA_ASSET_TAG_SOURCE = "source";
	
	private RecordingManager recordingManager;

	@Override
	public void init() throws KalturaManagerException {
		super.init();

		recordingManager = new RecordingManager(this);
	}

	@Override
	public void restartRecordings(){
		logger.debug("LiveStreamManager::restartRecordings");
		recordingManager.restart();
	}

	protected boolean restartRecording(String entryId){
		logger.debug("LiveStreamManager::restartRecording: " + entryId);
		return recordingManager.restart(entryId);
	}
	
	public String startRecord(String entryId, String assetId, IMediaStream stream, KalturaMediaServerIndex index, boolean versionFile, boolean startOnKeyFrame, boolean recordData){
		logger.debug("LiveStreamManager::startRecord: " + entryId);
		return recordingManager.start(entryId, assetId, stream, index, versionFile, startOnKeyFrame, recordData);
	}
	
	@Override
	public boolean splitRecordingNow(String entryId) {
		return restartRecording(entryId);
	}

	public RecordingManager getRecordingManager() {
		return recordingManager;
	}

	public void onPublish(IMediaStream stream, String entryId, KalturaMediaServerIndex serverIndex, int assetParamsId) {

		logger.debug("LiveStreamManager::onPublish stream [" + stream.getName() + "] entry [" + entryId + "] index [" + serverIndex.hashCode + "] asset params id [" + assetParamsId + "]");
		
		KalturaLiveEntry liveEntry = get(entryId);
		if(liveEntry == null){
			logger.error("LiveStreamManager::onPublish entry [" + entryId + "] not found for asset params id [" + assetParamsId + "]");
			return;
		}
		
		KalturaConversionProfileAssetParams conversionProfileAssetParams = getConversionProfileAssetParams(entryId, assetParamsId);
		if(conversionProfileAssetParams == null){
			logger.error("LiveStreamManager::onPublish entry [" + entryId + "] conversion profile asset params id [" + assetParamsId + "] not found");
			return;
		}
		if(conversionProfileAssetParams.origin != KalturaAssetParamsOrigin.INGEST){
			logger.debug("LiveStreamManager::onPublish entry [" + entryId + "] asset params id [" + assetParamsId + "] is not ingested");
			return;
		}
		
		KalturaLiveAsset liveAsset = getLiveAsset(entryId, assetParamsId);
		if(liveAsset == null){
			logger.error("LiveStreamManager::onPublish entry [" + entryId + "] asset params id [" + assetParamsId + "] asset not found");
			return;
		}
		
		String[] liveAssetTags = liveAsset.tags.split(",");
		List<String> liveAssetTagsSet = Arrays.asList(liveAssetTags);
		if(!liveAssetTagsSet.contains(LiveStreamManager.KALTURA_ASSET_TAG_SOURCE)){
			logger.debug("LiveStreamManager::onPublish entry [" + entryId + "] asset params id [" + assetParamsId + "] asset id [" + liveAsset.id + "] is not the source");
			return;
		}

		String fileName = startRecord(liveEntry.id, liveAsset.id, stream, serverIndex, true, true, false);
		logger.debug("LiveStreamManager::onPublish entry [" + entryId + "] asset params id [" + assetParamsId + "] recording to file [" + fileName + "]");
	}

	@Override
	public void onUnPublish(KalturaLiveEntry liveEntry, KalturaMediaServerIndex serverIndex) {
		recordingManager.stop(liveEntry.id);
		super.onUnPublish(liveEntry, serverIndex);
	}
}
