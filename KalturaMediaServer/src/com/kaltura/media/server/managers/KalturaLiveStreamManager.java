package com.kaltura.media.server.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.client.KalturaServiceBase;
import com.kaltura.client.types.KalturaLiveStreamEntry;



abstract public class KalturaLiveStreamManager extends KalturaLiveManager implements ILiveStreamManager {

	protected final static String KALTURA_SYNC_ENTRY_IDS = "KalturaSyncEntryids";
	protected List<String> syncEntryIds = new ArrayList<String>();
	
	@Override
	public void init() throws KalturaManagerException {
		super.init();

		if (serverConfiguration.containsKey(KalturaLiveStreamManager.KALTURA_SYNC_ENTRY_IDS)) {

			String[] entryIds = ((String) serverConfiguration.get(KalturaLiveStreamManager.KALTURA_SYNC_ENTRY_IDS)).replaceAll(" ", "").split(",");
			logger.debug("Sync entry ids: " + entryIds);
			syncEntryIds = Arrays.asList(entryIds);
		}
	}
	
	public boolean shouldSync(String entryId) {
		return syncEntryIds.contains(entryId);
	}
	
	public KalturaLiveStreamEntry authenticate(String entryId, int partnerId, String token) throws KalturaApiException {
		KalturaClient impersonateClient = impersonate(partnerId);
		KalturaLiveStreamEntry liveStreamEntry = impersonateClient.getLiveStreamService().authenticate(entryId, token);
		impersonateClient = null;
		
		synchronized (entries) {
			entries.put(liveStreamEntry.id, new LiveEntryCache(liveStreamEntry));
		}
		
		return liveStreamEntry;
	}
	
	public KalturaLiveStreamEntry get(String entryId, int partnerId) throws KalturaApiException {
		KalturaClient impersonateClient = impersonate(partnerId);
		KalturaLiveStreamEntry liveStreamEntry = impersonateClient.getLiveStreamService().get(entryId);
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
