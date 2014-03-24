package com.kaltura.media.server.managers;

import com.kaltura.client.types.KalturaLiveEntry;

public interface ICuePointsManager extends IManager {

	void createPeriodicSyncPoints(String liveEntryId, int interval, int duration);
	
	void createSyncPoint(String liveEntryId);
	
	float getEntryCurrentTime(KalturaLiveEntry liveEntry) throws KalturaManagerException;

	void sendSyncPoint(String entryId, String id, float offset) throws KalturaManagerException;

}
