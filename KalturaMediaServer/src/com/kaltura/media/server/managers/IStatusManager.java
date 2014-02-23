package com.kaltura.media.server.managers;

import com.kaltura.client.types.KalturaMediaServerStatus;

public interface IStatusManager extends IManager {
	
	public KalturaMediaServerStatus getServerStatus();
}
