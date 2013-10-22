package com.kaltura.media.server;

import com.kaltura.client.types.KalturaLiveStreamEntry;

public interface ILiveStreamManager extends IManager {

	public void onPublish(String entryId, int serverIndex);
	
	public void onUnPublish(String entryId, int serverIndex);

	public KalturaLiveStreamEntry get(int partnerId, String entryId);

}
