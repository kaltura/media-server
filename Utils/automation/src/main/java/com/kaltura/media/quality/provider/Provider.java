package com.kaltura.media.quality.provider;

import com.kaltura.media.quality.configurations.TestConfig;
import com.kaltura.media.quality.event.listener.Listener;
import com.kaltura.media.quality.utils.ThreadManager;

abstract public class Provider extends Listener implements Runnable {
	private static final long serialVersionUID = 6876429207493181101L;
	protected static TestConfig config;

	public Provider() {
		config = TestConfig.get();
	}

	public void start() {
		ThreadManager.start(this);
	}
}
