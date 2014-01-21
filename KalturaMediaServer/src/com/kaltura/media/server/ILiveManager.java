package com.kaltura.media.server;

import com.kaltura.client.enums.KalturaMediaServerIndex;
import com.kaltura.client.types.KalturaConversionProfileAssetParams;
import com.kaltura.client.types.KalturaLiveAsset;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.client.types.KalturaLiveParams;

public interface ILiveManager extends IManager {

	public void onPublish(String entryId, KalturaMediaServerIndex serverIndex);
	
	public void onUnPublish(KalturaLiveEntry liveEntry, KalturaMediaServerIndex serverIndex);
	
	public void onDisconnect(String entryId);

	public KalturaLiveEntry get(String entryId);

	public Integer getDvrWindow(KalturaLiveEntry liveStreamEntry);
	
	public void restartRecordings();
	
	public KalturaConversionProfileAssetParams getConversionProfileAssetParams(String entryId, int assetParamsId);
	
	public KalturaLiveAsset getLiveAsset(String entryId, int assetParamsId);
	
	public KalturaLiveParams getLiveAssetParams(int assetParamsId);
}
