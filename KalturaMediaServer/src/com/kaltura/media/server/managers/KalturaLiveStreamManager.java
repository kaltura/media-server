package com.kaltura.media.server.managers;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.client.KalturaServiceBase;
import com.kaltura.client.types.KalturaLiveStreamEntry;



abstract public class KalturaLiveStreamManager extends KalturaLiveManager implements ILiveStreamManager {
	
	public KalturaLiveStreamEntry authenticate(String entryId, int partnerId, String token) throws KalturaApiException {
		KalturaClient impersonateClient = impersonate(partnerId);
		KalturaLiveStreamEntry liveStreamEntry = impersonateClient.getLiveStreamService().authenticate(entryId, token);
		impersonateClient = null;
		
		synchronized (entries) {
			entries.put(liveStreamEntry.id, new LiveEntryCache(liveStreamEntry));
		}
		
		return liveStreamEntry;
	}
	
	@Override
	public KalturaLiveStreamEntry get(String entryId) {
		return (KalturaLiveStreamEntry) super.get(entryId);
	}


	public KalturaServiceBase getLiveServiceInstance (KalturaClient impersonateClient)
	{
		return impersonateClient.getLiveStreamService();
	}
}
