package com.kaltura.media.server.events;

import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.enums.KalturaEntryServerNodeType;
import com.kaltura.client.types.KalturaLiveEntry;

public class KalturaMetadataEvent extends KalturaStreamEvent {

	private KalturaObjectBase object;

	public KalturaMetadataEvent(IKalturaEventType type, KalturaLiveEntry entry, KalturaEntryServerNodeType serverIndex, KalturaObjectBase object) {
		super(type, entry, serverIndex);

		this.object = object;
	}

	public KalturaObjectBase getObject() {
		return object;
	}
}
