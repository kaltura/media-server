package com.kaltura.media.server.api.services;

import com.kaltura.media.server.api.IWebService;
import org.apache.log4j.Logger;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

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
			) {
		logger.error("createTimeCuePoints is depricated");
		return false;
	}
}
