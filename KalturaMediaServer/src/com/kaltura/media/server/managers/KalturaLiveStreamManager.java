package com.kaltura.media.server.managers;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.client.KalturaFile;
import com.kaltura.client.KalturaMultiResponse;
import com.kaltura.client.enums.KalturaMediaServerIndex;
import com.kaltura.client.enums.KalturaRecordStatus;
import com.kaltura.client.types.KalturaDataCenterContentResource;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.client.types.KalturaLiveStreamEntry;
import com.kaltura.client.types.KalturaServerFileResource;
import com.kaltura.client.types.KalturaUploadToken;
import com.kaltura.client.types.KalturaUploadedFileTokenResource;

import java.io.File;


abstract public class KalturaLiveStreamManager extends KalturaLiveManager implements ILiveStreamManager {
	
	public KalturaLiveStreamEntry authenticate(String entryId, int partnerId, String token) throws KalturaApiException {
		KalturaClient impersonateClient = impersonate(partnerId);
		KalturaLiveStreamEntry liveStreamEntry = impersonateClient.getLiveStreamService().authenticate(entryId, token);

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
		KalturaClient impersonateClient = impersonate(partnerId);
		KalturaLiveStreamEntry liveStreamEntry;
		try {
			liveStreamEntry = impersonateClient.getLiveStreamService().get(entryId);
		} catch (KalturaApiException e) {
			logger.error("KalturaLiveStreamManager::reloadEntry unable to get entry [" + entryId + "]: " + e.getMessage());
			return null;
		}

		synchronized (entries) {
			LiveEntryCache liveStreamEntryCache = entries.get(entryId);
			liveStreamEntryCache.setLiveEntry(liveStreamEntry);
		}
		
		return liveStreamEntry;
	}

	@Override
	protected void setEntryMediaServer(KalturaLiveEntry liveStreamEntry, KalturaMediaServerIndex serverIndex) {
		KalturaClient impersonateClient = impersonate(liveStreamEntry.partnerId);
		try {
			impersonateClient.getLiveStreamService().registerMediaServer(liveStreamEntry.id, hostname, serverIndex);
		} catch (KalturaApiException e) {
			logger.error("KalturaLiveStreamManager::setEntryMediaServer unable to register media server: " + e.getMessage());
		}
	}

	@Override
	protected void unsetEntryMediaServer(KalturaLiveEntry liveStreamEntry, KalturaMediaServerIndex serverIndex) {
		KalturaClient impersonateClient = impersonate(liveStreamEntry.partnerId);
		try {
			impersonateClient.getLiveStreamService().unregisterMediaServer(liveStreamEntry.id, hostname, serverIndex);
		} catch (KalturaApiException e) {
			logger.error("KalturaLiveStreamManager::unsetEntryMediaServer unable to unregister media server: " + e.getMessage());
		}
	}
	
	@Override
	public void appendRecording(String entryId, KalturaMediaServerIndex index, String filePath, float duration) {

		logger.info("KalturaLiveStreamManager::appendRecording: entry [" + entryId + "] index [" + index + "] filePath [" + filePath + "] duration [" + duration + "]");
		
		KalturaLiveStreamEntry liveEntry = get(entryId);
		KalturaDataCenterContentResource resource = getContentResource(filePath, liveEntry);
		
		KalturaClient impersonateClient = impersonate(liveEntry.partnerId);
		try {
			KalturaLiveEntry updatedEntry = impersonateClient.getLiveStreamService().appendRecording(entryId, index, resource, duration);
			synchronized (entries) {
				LiveEntryCache liveStreamEntryCache = entries.get(entryId);
				liveStreamEntryCache.setLiveEntry(updatedEntry);
			}
		} catch (KalturaApiException e) {
			logger.error("Append live recording error: " + e.getMessage());
		}
		
		if(liveEntry.recordStatus == KalturaRecordStatus.ENABLED && index == KalturaMediaServerIndex.PRIMARY)
			appendRecording(liveEntry);
	}
	
	protected KalturaDataCenterContentResource getContentResource (String filePath, KalturaLiveStreamEntry liveEntry) {
		if (this.serverConfiguration.get(KALTURA_WOWZA_SERVER_WORK_MODE) == KALTURA_WOWZA_SERVER_WORK_MODE_KALTURA) {
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
		}
		
		return null;
	}
}
