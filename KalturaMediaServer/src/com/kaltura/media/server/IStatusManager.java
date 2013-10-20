package com.kaltura.media.server;

import com.kaltura.client.types.KalturaMediaServerStatus;

public interface IStatusManager extends IManager {

	public String KALTURA_SERVER_STATUS_INTERVAL = "KalturaServerStatusInterval";
	
	KalturaMediaServerStatus getServerStatus();
}
