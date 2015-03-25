package com.kaltura.media.server.managers;

import com.kaltura.client.types.KalturaLiveEntry;

public interface ICuePointsManager extends IManager {

	void createPeriodicSyncPoints(String liveEntryId, int interval, int duration);
	
	void createSyncPoint(String liveEntryId);
	
	double getEntryCurrentTime(KalturaLiveEntry liveEntry) throws KalturaManagerException;

	void sendSyncPoint(String entryId, String id, double offset) throws KalturaManagerException;

}
