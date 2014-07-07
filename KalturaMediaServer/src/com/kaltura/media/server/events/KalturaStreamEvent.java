package com.kaltura.media.server.events;

import com.kaltura.client.enums.KalturaMediaServerIndex;
import com.kaltura.client.types.KalturaLiveEntry;

public class KalturaStreamEvent extends KalturaEvent {

	private KalturaLiveEntry entry;
	private KalturaMediaServerIndex serverIndex;
	private String applicationName;

	public KalturaStreamEvent(IKalturaEventType type, KalturaLiveEntry entry) {
		super(type);
		
		this.entry = entry;
	}
	
	public KalturaStreamEvent(IKalturaEventType type, KalturaLiveEntry entry, KalturaMediaServerIndex serverIndex) {
		this(type, entry);
		this.serverIndex = serverIndex;
	}
	
	public KalturaStreamEvent(IKalturaEventType type, KalturaLiveEntry entry, KalturaMediaServerIndex serverIndex, String applicationName) {
		this(type, entry, serverIndex);
		this.applicationName = applicationName;
	}
	
	public String getEntryId() {
		return entry.id;
	}

	public KalturaLiveEntry getEntry() {
		return entry;
	}

	public KalturaMediaServerIndex getServerIndex() {
		return serverIndex;
	}

	public String getApplicationName() {
		return applicationName;
	}
}
