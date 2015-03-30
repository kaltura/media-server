package com.kaltura.media.server.api.services;

import java.util.Timer;
import java.util.TimerTask;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import org.apache.log4j.Logger;

import com.kaltura.media.server.KalturaServer;
import com.kaltura.media.server.api.IWebService;
import com.kaltura.media.server.managers.ICuePointsManager;

@WebService(name = "cuePoints")
public class KalturaCuePointsService implements IWebService{
	
	protected static Logger logger = Logger.getLogger(KalturaCuePointsService.class);
	
	@WebMethod(action = "createTimeCuePoints")
	@RequestWrapper(localName = "CreateTimeCuePointsRequest")
	@ResponseWrapper(localName = "CreateTimeCuePointsResponse")
	public boolean createTimeCuePoints(
			@WebParam(name = "liveEntryId") final String liveEntryId,
			@WebParam(name = "interval") final int interval,
			@WebParam(name = "duration") final int duration
			)
	{
		logger.debug("liveEntryId [" + liveEntryId + "] interval [" + interval + "] duration [" + duration + "]");
		TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {
				ICuePointsManager cuePointsManager = (ICuePointsManager) KalturaServer.getManager(ICuePointsManager.class);
				cuePointsManager.createPeriodicSyncPoints(liveEntryId, interval, duration);
			}
		};
		
		Timer timer = new Timer("createPeriodicSyncPoints-" + liveEntryId, true);
		timer.schedule(timerTask, 0);
		
		logger.debug("done");
		return true;
	}
}
