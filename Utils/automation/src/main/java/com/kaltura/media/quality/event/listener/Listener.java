package com.kaltura.media.quality.event.listener;

import com.kaltura.media.quality.event.EventsManager;
import com.kaltura.media.quality.event.Persistable;

abstract public class Listener extends Persistable implements IListener {
	private static final long serialVersionUID = 8280414320445163027L;

	@Override
	protected String getPath() {
		return EventsManager.getDefferedConsumersPath();
	}

	@Override
	protected String getExtension() {
		return getClass().getSimpleName() + ".listener";
	}

	abstract public void register();
}
