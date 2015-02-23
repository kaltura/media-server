package com.kaltura.media.server.events;

public class KalturaEvent implements IKalturaEvent {

	protected IKalturaEventType type;

	public KalturaEvent(IKalturaEventType type) {
		this.type = type;
	}
	
	@Override
	public IKalturaEventType getType() {
		return type;
	}
}
