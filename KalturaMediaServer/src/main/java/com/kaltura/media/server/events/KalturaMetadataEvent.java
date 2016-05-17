package com.kaltura.media.server.events;

import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.enums.KalturaMediaServerIndex;
import com.kaltura.client.types.KalturaLiveEntry;

public class KalturaMetadataEvent extends KalturaStreamEvent {

	private KalturaObjectBase object;

	public KalturaMetadataEvent(IKalturaEventType type, KalturaLiveEntry entry, KalturaMediaServerIndex serverIndex, KalturaObjectBase object) {
		super(type, entry, serverIndex);

		this.object = object;
	}

	public KalturaObjectBase getObject() {
		return object;
	}
}
