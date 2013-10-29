package com.kaltura.media.server;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.types.KalturaLiveStreamEntry;

public interface ILiveStreamManager extends IManager {

	public void onPublish(KalturaLiveStreamEntry liveStreamEntry, int serverIndex);
	
	public void onUnPublish(KalturaLiveStreamEntry liveStreamEntry);

	public KalturaLiveStreamEntry get(int partnerId, String entryId) throws KalturaApiException;
	
	public Integer getDvrWindow(KalturaLiveStreamEntry liveStreamEntry);
}
