package com.kaltura.media.server;

import com.kaltura.client.enums.KalturaMediaServerIndex;
import com.kaltura.client.types.KalturaLiveEntry;

public interface ILiveManager extends IManager {

	public void onPublish(String entryId, KalturaMediaServerIndex serverIndex);
	
	public void onUnPublish(KalturaLiveEntry liveEntry, KalturaMediaServerIndex serverIndex);
	
	public void onDisconnect(String entryId);

	public KalturaLiveEntry get(String entryId);

	public Integer getDvrWindow(KalturaLiveEntry liveStreamEntry);
	
	public void restartRecordings();
}
