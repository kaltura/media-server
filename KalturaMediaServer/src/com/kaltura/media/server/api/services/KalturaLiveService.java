package com.kaltura.media.server.api.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import com.kaltura.media.server.ILiveStreamManager;
import com.kaltura.media.server.KalturaServer;
import com.kaltura.media.server.api.IWebService;

@WebService(name = "live")
public class KalturaLiveService implements IWebService{
	
	@WebMethod(action = "createMediaEntry")
	@RequestWrapper(localName = "CreateMediaEntryRequest")
	@ResponseWrapper(localName = "CreateMediaEntryResponse")
	public boolean createMediaEntry(
			@WebParam(name = "liveEntryId") String liveEntryId)
	{
		ILiveStreamManager liveStreamManager = (ILiveStreamManager) KalturaServer.getManager(ILiveStreamManager.class);
		return liveStreamManager.createMediaEntry(liveEntryId);
	}

	@WebMethod(action = "createMediaClipEntry")
	@RequestWrapper(localName = "CreateMediaClipEntryRequest")
	@ResponseWrapper(localName = "CreateMediaClipEntryResponse")
	public boolean createMediaClipEntry(
			@WebParam(name = "liveEntryId") String liveEntryId, 
			@WebParam(name = "offset") int offset, 
			@WebParam(name = "duration") int duration)
	{
		ILiveStreamManager liveStreamManager = (ILiveStreamManager) KalturaServer.getManager(ILiveStreamManager.class);
		return liveStreamManager.createMediaEntry(liveEntryId, offset, duration);
	}
}
