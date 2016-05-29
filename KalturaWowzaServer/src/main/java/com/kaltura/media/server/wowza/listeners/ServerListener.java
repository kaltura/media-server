package com.kaltura.media.server.wowza.listeners;

import org.apache.log4j.Logger;
import com.kaltura.media.server.KalturaServerException;
import com.kaltura.media.server.wowza.KalturaAPI;

import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.application.WMSProperties;
import com.wowza.wms.server.*;
import com.wowza.wms.vhost.IVHost;
import com.wowza.wms.vhost.VHostSingleton;

import java.util.Map;

public class ServerListener implements IServerNotify2 {

	protected static Logger logger = Logger.getLogger(ServerListener.class);

	private static Map<String, Object> config;

	public void onServerConfigLoaded(IServer server) {
	}

	public void onServerCreate(IServer server) {
	}


	@SuppressWarnings("unchecked")
	public void onServerInit(IServer server) {
		config = server.getProperties();
		try {
			KalturaAPI.initKalturaAPI(config);
			logger.info("ServerListener::onServerInit Initialized Kaltura server");
		} catch ( Exception e) {
			logger.error("ServerListener::onServerInit Failed to initialize KalturaAPI: " + e.getMessage());
		}
		
		loadAndLockAppInstance(IVHost.VHOST_DEFAULT, "kLive", IApplicationInstance.DEFAULT_APPINSTANCE_NAME);
	}

	public void onServerShutdownStart(IServer server) {
		//todo should write here something?
		/*
		if(kalturaServer != null)
			kalturaServer.stop();
			*/
	}

	public void onServerShutdownComplete(IServer server) {
	}

	private void loadAndLockAppInstance(String vhostName, String appName, String appInstanceName)
	{
		IVHost vhost = VHostSingleton.getInstance(vhostName);
		if(vhost != null)
		{
			if (vhost.startApplicationInstance(appName, appInstanceName))	//invoke OnAppsrart in all managers
			{//todo check it
				vhost.getApplication(appName).getAppInstance(appInstanceName).setApplicationTimeout(0); //stop the instance from shutting down:
			}
			else
			{
				logger.warn("Application folder ([install-location]/applications/" + appName + ") is missing");
			}
		}
	}
	public static Map<String, Object> getServerConfig(){
		return config;
	}
}
