package com.kaltura.media.server.events;

import com.kaltura.client.enums.KalturaMediaServerIndex;
import com.kaltura.client.types.KalturaLiveEntry;

public class KalturaStreamEvent implements IKalturaEvent {

	private KalturaLiveEntry entry;
	private KalturaMediaServerIndex serverIndex;
	protected IKalturaEventType type;

	public KalturaStreamEvent(IKalturaEventType type) {
		this.type = type;
	}
	
	@Override
	public IKalturaEventType getType() {
		return type;
	}
	
	public String getEntryId() {
		return entry.id;
	}

	public KalturaLiveEntry getEntry() {
		return entry;
	}

	public void setEntry(KalturaLiveEntry entry) {
		this.entry = entry;
	}

	public KalturaMediaServerIndex getServerIndex() {
		return serverIndex;
	}

	public void setServerIndex(KalturaMediaServerIndex serverIndex) {
		this.serverIndex = serverIndex;
	}
}
