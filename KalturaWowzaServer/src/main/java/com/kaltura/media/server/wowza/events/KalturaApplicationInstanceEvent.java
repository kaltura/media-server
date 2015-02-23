package com.kaltura.media.server.wowza.events;

import com.kaltura.media.server.events.IKalturaEventType;
import com.kaltura.media.server.events.KalturaEvent;
import com.wowza.wms.application.IApplicationInstance;

public class KalturaApplicationInstanceEvent extends KalturaEvent {

	public KalturaApplicationInstanceEvent(IKalturaEventType type, IApplicationInstance applicationInstance) {
		super(type);

		this.applicationInstance = applicationInstance;
	}

	private IApplicationInstance applicationInstance;

	public IApplicationInstance getApplicationInstance() {
		return applicationInstance;
	}
}
