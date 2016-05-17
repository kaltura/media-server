package com.kaltura.media.server.managers;

import com.kaltura.client.enums.KalturaEntryServerNodeType;
import com.kaltura.client.types.KalturaConversionProfileAssetParams;
import com.kaltura.client.types.KalturaLiveAsset;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.client.types.KalturaLiveParams;

public interface ILiveManager extends IManager {
	
	/**
	 *	Marking interface to indicate classes as entry-cache dependent 
	 */
	public interface ILiveEntryReferrer {
		
	}
	
	/**
	 * Registers an object as referrer to a given entry
	 * @param entryId The entry it refers to
	 * @param obj The referrer
	 */
	public void addReferrer(String entryId, ILiveEntryReferrer obj);
	
	/**
	 * Unregisters an object as referrer to a given entry
	 * @param entryId The entry it referred to
	 * @param obj The referrer
	 */
	public void removeReferrer(String entryId, ILiveEntryReferrer obj);
		
	public KalturaLiveEntry get(String entryId);
	
	public KalturaEntryServerNodeType getMediaServerIndexForEntry(String entryId);

	public Integer getDvrWindow(KalturaLiveEntry liveStreamEntry);
	
	public KalturaConversionProfileAssetParams getConversionProfileAssetParams(String entryId, int assetParamsId);
	
	public KalturaLiveAsset getLiveAsset(String entryId, int assetParamsId);
	
	public KalturaLiveParams getLiveAssetParams(int assetParamsId);
	
	public Object getOrAddMetadata(String entryId, String key, Object defaultValue);
	public Object getMetadata(String entryId, String key);
	public Object setMetadata(String entryId, String key, Object value);
}
