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
import com.kaltura.media.server.managers.ILiveStreamManager;

@WebService(name = "live")
public class KalturaLiveService implements IWebService{

	protected static Logger logger = Logger.getLogger(KalturaLiveService.class);
	
	@WebMethod(action = "splitRecordingNow")
	@RequestWrapper(localName = "SplitRecordingNowRequest")
	@ResponseWrapper(localName = "SplitRecordingNowResponse")
	public boolean splitRecordingNow(
			@WebParam(name = "liveEntryId") final String liveEntryId)
	{
		logger.debug("liveEntryId [" + liveEntryId + "]");
		TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {
				ILiveStreamManager liveStreamManager = (ILiveStreamManager) KalturaServer.getManager(ILiveStreamManager.class);
				liveStreamManager.splitRecordingNow(liveEntryId);
			}
		};
		
		Timer timer = new Timer("splitRecordingNow-" + liveEntryId, true);
		timer.schedule(timerTask, 0);
		
		logger.debug("done");
		return true;
	}
}
