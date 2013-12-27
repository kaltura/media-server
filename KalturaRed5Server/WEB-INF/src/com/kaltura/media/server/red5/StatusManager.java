package com.kaltura.media.server.red5;

import com.kaltura.client.types.KalturaMediaServerStatus;
import com.kaltura.media.server.KalturaStatusManager;

public class StatusManager extends KalturaStatusManager {

	@Override
	public KalturaMediaServerStatus getServerStatus() {
		return new KalturaMediaServerStatus();
	}
}
