package com.kaltura.media.server.wowza.listeners;

import org.apache.log4j.Logger;

import com.kaltura.media.server.KalturaServer;
import com.kaltura.media.server.KalturaServerException;
import com.wowza.wms.application.WMSProperties;
import com.wowza.wms.server.*;

public class ServerListener implements IServerNotify2 {

	protected static Logger logger = Logger.getLogger(ServerListener.class);
	
	KalturaServer kalturaServer;

	public void onServerConfigLoaded(IServer server) {
	}

	public void onServerCreate(IServer server) {
	}

	@SuppressWarnings("unchecked")
	public void onServerInit(IServer server) {
		WMSProperties config = server.getProperties();
		try {
			kalturaServer = KalturaServer.init(config);
			logger.info("ServerListener::onServerInit Initialized Kaltura server");
		} catch (KalturaServerException e) {
			logger.error("ServerListener::onServerInit Failed to initialize Kaltura server: " + e.getMessage());
		}
	}

	public void onServerShutdownStart(IServer server) {
		if(kalturaServer != null)
			kalturaServer.stop();
	}

	public void onServerShutdownComplete(IServer server) {
	}

}
