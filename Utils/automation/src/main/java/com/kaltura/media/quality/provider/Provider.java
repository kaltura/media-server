package com.kaltura.media.quality.provider;

import com.kaltura.media.quality.configurations.TestConfig;
import com.kaltura.media.quality.utils.ThreadManager;

abstract public class Provider implements Runnable {
	protected static TestConfig config;

	public Provider() {
		config = TestConfig.get();
	}

	public void start() {
		ThreadManager.start(this);
	}
}
