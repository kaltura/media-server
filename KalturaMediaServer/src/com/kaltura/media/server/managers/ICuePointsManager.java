package com.kaltura.media.server.managers;

public interface ICuePointsManager extends IManager {

	boolean createTimeCuePoints(String liveEntryId, int interval, int duration);
	
	boolean createTimeCuePoint(String liveEntryId);

}
