package com.kaltura.media.server.wowza;

import java.util.Arrays;
import java.util.List;

import com.kaltura.client.enums.KalturaAssetParamsOrigin;
import com.kaltura.client.enums.KalturaMediaServerIndex;
import com.kaltura.client.enums.KalturaRecordStatus;
import com.kaltura.client.types.KalturaConversionProfileAssetParams;
import com.kaltura.client.types.KalturaLiveAsset;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.infra.StringUtils;
import com.kaltura.media.server.KalturaEventsManager;
import com.kaltura.media.server.events.IKalturaEvent;
import com.kaltura.media.server.managers.KalturaLiveStreamManager;
import com.kaltura.media.server.managers.KalturaManagerException;
import com.kaltura.media.server.wowza.events.KalturaMediaEventType;
import com.kaltura.media.server.wowza.events.KalturaMediaStreamEvent;
import com.wowza.wms.stream.IMediaStream;

public class LiveStreamManager extends KalturaLiveStreamManager {

	protected final static String KALTURA_ASSET_TAG_SOURCE = "source";
	
	private RecordingManager recordingManager;

	@Override
	public void init() throws KalturaManagerException {
		super.init();

		recordingManager = new RecordingManager(this);
		KalturaEventsManager.registerEventConsumer(this, KalturaMediaEventType.MEDIA_STREAM_PUBLISHED);
	}

	@Override
	public void restartRecordings(){
		logger.debug("Restart recordings");
		recordingManager.restart();
	}

	protected boolean restartRecording(String entryId){
		logger.debug("Restart recording: " + entryId);
		return recordingManager.restart(entryId);
	}
	
	public String startRecord(String entryId, String assetId, IMediaStream stream, KalturaMediaServerIndex index, boolean versionFile, boolean startOnKeyFrame, boolean recordData){
		logger.debug("Start recording: " + entryId);
		return recordingManager.start(entryId, assetId, stream, index, versionFile, startOnKeyFrame, recordData);
	}
	
	@Override
	public boolean splitRecordingNow(String entryId) {
		return restartRecording(entryId);
	}

	public RecordingManager getRecordingManager() {
		return recordingManager;
	}

	@Override
	public void onEvent(IKalturaEvent event){
		
		if(event.getType() instanceof KalturaMediaEventType){
			KalturaMediaStreamEvent streamEvent;
			
			switch((KalturaMediaEventType) event.getType())
			{
				case MEDIA_STREAM_PUBLISHED:
					streamEvent = (KalturaMediaStreamEvent) event;
					onPublish(streamEvent.getMediaStream(), streamEvent.getEntryId(), streamEvent.getServerIndex(), streamEvent.getAssetParamsId());
					break;
					
				default:
					break;
			}
		}
		
		super.onEvent(event);
	}
	
	protected void onPublish(IMediaStream stream, String entryId, KalturaMediaServerIndex serverIndex, int assetParamsId) {

		logger.debug("Stream [" + stream.getName() + "] entry [" + entryId + "] index [" + serverIndex.hashCode + "] asset params id [" + assetParamsId + "]");
		
		KalturaLiveEntry liveEntry = get(entryId);
		if(liveEntry == null){
			logger.error("Entry [" + entryId + "] not found for asset params id [" + assetParamsId + "]");
			return;
		}

		if(liveEntry.recordStatus != KalturaRecordStatus.ENABLED){
			logger.info("Entry [" + entryId + "] recording is not enabled");
			return;
		}
		
		KalturaConversionProfileAssetParams conversionProfileAssetParams = getConversionProfileAssetParams(entryId, assetParamsId);
		if(conversionProfileAssetParams == null){
			logger.error("Entry [" + entryId + "] conversion profile asset params id [" + assetParamsId + "] not found");
			return;
		}
		if(conversionProfileAssetParams.origin != KalturaAssetParamsOrigin.INGEST){
			logger.debug("Entry [" + entryId + "] asset params id [" + assetParamsId + "] is not ingested");
			return;
		}
		
		KalturaLiveAsset liveAsset = getLiveAsset(entryId, assetParamsId);
		if(liveAsset == null){
			logger.error("Entry [" + entryId + "] asset params id [" + assetParamsId + "] asset not found");
			return;
		}
		
		if(!StringUtils.contains(liveAsset.tags, LiveStreamManager.KALTURA_ASSET_TAG_SOURCE)){
			logger.debug("Entry [" + entryId + "] asset params id [" + assetParamsId + "] asset id [" + liveAsset.id + "] is not the source");
			return;
		}

		String fileName = startRecord(liveEntry.id, liveAsset.id, stream, serverIndex, true, true, true);
		logger.debug("Entry [" + entryId + "] asset params id [" + assetParamsId + "] recording to file [" + fileName + "]");
	}

	@Override
	protected void onUnPublish(KalturaLiveEntry liveEntry, KalturaMediaServerIndex serverIndex) {
		recordingManager.stop(liveEntry.id);
		super.onUnPublish(liveEntry, serverIndex);
	}
}
