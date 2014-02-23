package com.kaltura.media.server.wowza.events;

import com.kaltura.media.server.events.IKalturaEventType;
import com.kaltura.media.server.events.KalturaStreamEvent;
import com.wowza.wms.application.IApplicationInstance;

public class KalturaApplicationInstanceEvent extends KalturaStreamEvent {

	public KalturaApplicationInstanceEvent(IKalturaEventType type) {
		super(type);
	}

	private IApplicationInstance applicationInstance;
	private KalturaMediaEventType type;

	@Override
	public IKalturaEventType getType() {
		return type;
	}

	public IApplicationInstance getApplicationInstance() {
		return applicationInstance;
	}

	public void setApplicationInstance(IApplicationInstance applicationInstance) {
		this.applicationInstance = applicationInstance;
	}
}
