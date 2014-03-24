package com.kaltura.media.server.api.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import com.kaltura.media.server.KalturaServer;
import com.kaltura.media.server.api.IWebService;
import com.kaltura.media.server.managers.ILiveStreamManager;

@WebService(name = "live")
public class KalturaLiveService implements IWebService{
	
	@WebMethod(action = "splitRecordingNow")
	@RequestWrapper(localName = "SplitRecordingNowRequest")
	@ResponseWrapper(localName = "SplitRecordingNowResponse")
	public boolean splitRecordingNow(
			@WebParam(name = "liveEntryId") String liveEntryId)
	{
		ILiveStreamManager liveStreamManager = (ILiveStreamManager) KalturaServer.getManager(ILiveStreamManager.class);
		return liveStreamManager.splitRecordingNow(liveEntryId);
	}
}
