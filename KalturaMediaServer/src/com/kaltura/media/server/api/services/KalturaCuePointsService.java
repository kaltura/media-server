package com.kaltura.media.server.api.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import com.kaltura.media.server.KalturaServer;
import com.kaltura.media.server.api.IWebService;
import com.kaltura.media.server.managers.ICuePointsManager;

@WebService(name = "cuePoints")
public class KalturaCuePointsService implements IWebService{
	
	@WebMethod(action = "createTimeCuePoints")
	@RequestWrapper(localName = "CreateTimeCuePointsRequest")
	@ResponseWrapper(localName = "CreateTimeCuePointsResponse")
	public boolean createTimeCuePoints(
			@WebParam(name = "liveEntryId") String liveEntryId,
			@WebParam(name = "interval") int interval,
			@WebParam(name = "duration") int duration
			)
	{
		ICuePointsManager cuePointsManager = (ICuePointsManager) KalturaServer.getManager(ICuePointsManager.class);
		return cuePointsManager.createTimeCuePoints(liveEntryId, interval, duration);
	}
}
