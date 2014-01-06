package com.kaltura.media.server;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.enums.KalturaMediaServerIndex;
import com.kaltura.client.enums.KalturaRecordStatus;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.client.types.KalturaLiveStreamEntry;
import com.kaltura.client.types.KalturaServerFileResource;

abstract public class KalturaLiveStreamManager extends KalturaLiveManager implements ILiveStreamManager {
	
	public KalturaLiveStreamEntry authenticate(String entryId, int partnerId, String token) throws KalturaApiException {
		impersonate(partnerId);
		KalturaLiveStreamEntry liveStreamEntry = client.getLiveStreamService().authenticate(entryId, token);
		unimpersonate();

		synchronized (entries) {
			entries.put(liveStreamEntry.id, new LiveEntryCache(liveStreamEntry));
		}
		
		return liveStreamEntry;
	}
	
	@Override
	public KalturaLiveStreamEntry get(String entryId) {
		return (KalturaLiveStreamEntry) super.get(entryId);
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
			LiveEntryCache liveStreamEntryCache = entries.get(entryId);
			liveStreamEntryCache.setLiveEntry(liveStreamEntry);
		}
		
		return liveStreamEntry;
	}

	@Override
	protected void setEntryMediaServer(KalturaLiveEntry liveStreamEntry, KalturaMediaServerIndex serverIndex) {
		impersonate(liveStreamEntry.partnerId);
		try {
			client.getLiveStreamService().registerMediaServer(liveStreamEntry.id, hostname, serverIndex);
		} catch (KalturaApiException e) {
			logger.error("KalturaLiveStreamManager::setEntryMediaServer unable to register media server: " + e.getMessage());
		}
		unimpersonate();
	}

	@Override
	protected void unsetEntryMediaServer(KalturaLiveEntry liveStreamEntry, KalturaMediaServerIndex serverIndex) {
		impersonate(liveStreamEntry.partnerId);
		try {
			client.getLiveStreamService().unregisterMediaServer(liveStreamEntry.id, hostname, serverIndex);
		} catch (KalturaApiException e) {
			logger.error("KalturaLiveStreamManager::unsetEntryMediaServer unable to unregister media server: " + e.getMessage());
		}
		unimpersonate();
	}
	
	@Override
	public void appendRecording(String entryId, KalturaMediaServerIndex index, String filePath, float duration) {

		KalturaServerFileResource resource = new KalturaServerFileResource();
		resource.localFilePath = filePath;
		
		KalturaLiveStreamEntry liveEntry = get(entryId);
		impersonate(liveEntry.partnerId);
		try {
			client.getLiveStreamService().appendRecording(entryId, index, resource, duration);
		} catch (KalturaApiException e) {
			logger.error("Append live recording error: " + e.getMessage());
		}
		unimpersonate();
		
		if(liveEntry.recordStatus == KalturaRecordStatus.ENABLED && index == KalturaMediaServerIndex.PRIMARY)
			appendRecording(liveEntry);
	}
}
