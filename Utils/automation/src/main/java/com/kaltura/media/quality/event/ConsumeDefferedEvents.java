package com.kaltura.media.quality.event;

import com.kaltura.media.quality.monitor.Monitor;
import com.kaltura.media.quality.utils.ThreadManager;

/**
 * This class monitors the system:
 * For each live entry in the system it creates a downloading thread
 * and comparator thread.
 */
public class ConsumeDefferedEvents extends Monitor {
	public static void main(String[] args) throws Exception {
		ConsumeDefferedEvents monitor = new ConsumeDefferedEvents(args);
		monitor.execute();
	}

	public ConsumeDefferedEvents(String[] args) throws Exception {
		super(args);
	}

	protected void execute() throws Exception {
		Thread thread = Thread.currentThread();
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.setName("main");

		ThreadManager.runForever();
		EventsManager.consumeDefferedEvents();
	}
}