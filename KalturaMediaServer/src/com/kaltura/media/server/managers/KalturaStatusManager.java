package com.kaltura.media.server.managers;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.media.server.KalturaServer;

public abstract class KalturaStatusManager implements IStatusManager {

	protected final static String KALTURA_SERVER_STATUS_INTERVAL = "KalturaServerStatusInterval";

	protected static Logger logger = Logger.getLogger(KalturaStatusManager.class);
	
	protected long interval;
	protected KalturaClient client;
	protected String hostname;
	private Timer timer;
	
	@Override
	public void init() throws KalturaManagerException {

		Map<String, Object> config = KalturaServer.getConfiguration();
		
		if (!config.containsKey(KalturaStatusManager.KALTURA_SERVER_STATUS_INTERVAL))
			throw new KalturaManagerException("Missing configuration [" + KalturaStatusManager.KALTURA_SERVER_STATUS_INTERVAL + "]");

		interval = Long.parseLong((String) config.get(KalturaStatusManager.KALTURA_SERVER_STATUS_INTERVAL)) * 1000; // miliseconds
		
		TimerTask timerTask = new TimerTask(){

			@Override
			public void run() {
				reportStatus();
			}
		};

		hostname = KalturaServer.getHostName();
		client = KalturaServer.getClient();
		
		timer = new Timer(true);
		timer.schedule(timerTask, 0, interval);
		
		KalturaServer.setManagerInitialized(getClass().getName());
	}

	@Override
	public void stop() {
		timer.cancel();
		timer.purge();
	}

	public void reportStatus() {
		try {
			logger.debug("Reporting server status [" + hostname + "]");
			client.getMediaServerService().reportStatus(hostname, getServerStatus());
		} catch (KalturaApiException e) {
			IExceptionManager exceptionManager = (IExceptionManager) KalturaServer.getManager(IExceptionManager.class);
			logger.error(e);
			if(exceptionManager != null){
				exceptionManager.handleException(e);
			}
		}
	}
}
