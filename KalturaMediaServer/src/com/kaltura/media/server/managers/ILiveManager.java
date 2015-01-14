package com.kaltura.media.server.managers;

import com.kaltura.client.enums.KalturaMediaServerIndex;
import com.kaltura.client.types.KalturaConversionProfileAssetParams;
import com.kaltura.client.types.KalturaLiveAsset;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.client.types.KalturaLiveParams;

public interface ILiveManager extends IManager {

	public KalturaLiveEntry get(String entryId);
	
	public KalturaMediaServerIndex getMediaServerIndexForEntry(String entryId);

	public Integer getDvrWindow(KalturaLiveEntry liveStreamEntry);
	
	public KalturaConversionProfileAssetParams getConversionProfileAssetParams(String entryId, int assetParamsId);
	
	public KalturaLiveAsset getLiveAsset(String entryId, int assetParamsId);
	
	public KalturaLiveParams getLiveAssetParams(int assetParamsId);
}
