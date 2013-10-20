package com.kaltura.media.server;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;

public abstract class StatusManager implements IStatusManager {

	protected long interval;
	protected KalturaClient client;
	protected String hostname;
	private Timer timer;
	
	@Override
	public void init() throws ManagerException {

		Map<String, ?> config = Server.getConfiguration();
		
		if (!config.containsKey(IStatusManager.KALTURA_SERVER_STATUS_INTERVAL))
			throw new ManagerException("Missing configuration [" + IStatusManager.KALTURA_SERVER_STATUS_INTERVAL + "]");

		interval = (Long) config.get(IStatusManager.KALTURA_SERVER_STATUS_INTERVAL);
		
		TimerTask timerTask = new TimerTask(){

			@Override
			public void run() {
				reportStatus();
			}
		};

		hostname = Server.getHostName();
		client = Server.getClient();
		
		timer = new Timer(true);
		timer.schedule(timerTask, 0, interval);
	}

	public void reportStatus() {
		try {
			client.getMediaServerService().reportStatus(hostname, getServerStatus());
		} catch (KalturaApiException e) {
			IExceptionManager exceptionManager = (IExceptionManager) Server.getManager(IExceptionManager.class);
			exceptionManager.handleException(e);
		}
	}
}
