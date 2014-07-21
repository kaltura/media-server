package com.kaltura.media.server.managers;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.kaltura.media.server.KalturaServer;

public class KalturaMemoryDebugManager implements IManager {

	protected final static String KALTURA_MEMORY_DEBUG_INTERVAL = "KalturaMemoryDebugInterval";

	protected static Logger logger = Logger.getLogger(KalturaMemoryDebugManager.class);
	
	protected long interval = 60000;
	private Timer timer;
	
	@Override
	public void init() throws KalturaManagerException {

		Map<String, Object> config = KalturaServer.getConfiguration();
		
		if (config.containsKey(KalturaMemoryDebugManager.KALTURA_MEMORY_DEBUG_INTERVAL))
			interval = Long.parseLong((String) config.get(KalturaMemoryDebugManager.KALTURA_MEMORY_DEBUG_INTERVAL)) * 1000; // miliseconds
		
		TimerTask timerTask = new TimerTask(){

			@Override
			public void run() {
				reportMemory();
			}
		};

		timer = new Timer(true);
		timer.schedule(timerTask, 0, interval);
		
		KalturaServer.setManagerInitialized(getClass().getName());
	}

	@Override
	public void stop() {
		timer.cancel();
		timer.purge();
	}

	public void reportMemory() {
		System.gc();
		Runtime rt = Runtime.getRuntime();

		long totalMB = rt.totalMemory() / 1024 / 1024;
		long freeMB = rt.freeMemory() / 1024 / 1024;
		long usedMB = totalMB - freeMB;

		logger.debug("Total Memory: " + rt.totalMemory() + " (" + totalMB + "MB)");
		logger.debug("Free Memory: " + rt.freeMemory() + " (" + freeMB + "MB)");
		logger.debug("Memory Usage: " + (rt.totalMemory() - rt.freeMemory()) + " (" + usedMB + "MB)");
	}
}
