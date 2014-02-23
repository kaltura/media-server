package com.kaltura.media.server.managers;

public interface ICuePointsManager extends IManager {

	boolean createPeriodicSyncPoints(String liveEntryId, int interval, int duration);
	
	boolean createSyncPoint(String liveEntryId);

}
