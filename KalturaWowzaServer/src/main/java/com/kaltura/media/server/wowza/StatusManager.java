package com.kaltura.media.server.wowza;

import com.kaltura.client.types.KalturaMediaServerStatus;
import com.kaltura.media.server.managers.KalturaStatusManager;

public class StatusManager extends KalturaStatusManager {

	@Override
	public KalturaMediaServerStatus getServerStatus() {
		return new KalturaMediaServerStatus();
	}
}
