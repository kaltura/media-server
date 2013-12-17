package com.kaltura.media.server.wowza.listeners;

import com.kaltura.media.server.KalturaServer;
import com.kaltura.media.server.KalturaServerException;
import com.wowza.wms.application.WMSProperties;
import com.wowza.wms.logging.WMSLogger;
import com.wowza.wms.logging.WMSLoggerFactory;
import com.wowza.wms.server.*;

public class ServerListener implements IServerNotify2 {
	
	KalturaServer kalturaServer;

	public void onServerConfigLoaded(IServer server) {
	}

	public void onServerCreate(IServer server) {
	}

	@SuppressWarnings("unchecked")
	public void onServerInit(IServer server) {
		WMSLogger logger = WMSLoggerFactory.getLogger(null);
		WMSProperties config = server.getProperties();
		try {
			kalturaServer = KalturaServer.init(logger, config);
			logger.info("Initialized Kaltura server");
		} catch (KalturaServerException e) {
			logger.error("Failed to initialize Kaltura server: " + e.getMessage());
		}
	}

	public void onServerShutdownStart(IServer server) {
		kalturaServer.stop();
	}

	public void onServerShutdownComplete(IServer server) {
	}

}
