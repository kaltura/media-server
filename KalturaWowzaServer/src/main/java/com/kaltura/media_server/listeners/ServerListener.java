package com.kaltura.media_server.listeners;

import org.apache.log4j.Logger;

import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.server.*;
import com.wowza.wms.vhost.IVHost;
import com.wowza.wms.vhost.VHostSingleton;
import com.kaltura.media_server.services.KalturaAPI;
import com.kaltura.media_server.services.KalturaUncaughtExceptionHnadler;

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
			logger.info("listeners.ServerListener::onServerInit Initialized Kaltura server");

			loadAndLockAppInstance(IVHost.VHOST_DEFAULT, "kLive", IApplicationInstance.DEFAULT_APPINSTANCE_NAME);

		} catch ( Exception e) {
			logger.error("listeners.ServerListener::onServerInit Failed to initialize services.KalturaAPI: " + e.getMessage());
		}
		Thread.setDefaultUncaughtExceptionHandler(new KalturaUncaughtExceptionHnadler());
	}

	public void onServerShutdownStart(IServer server) {

	}

	public void onServerShutdownComplete(IServer server) {
	}

	private void loadAndLockAppInstance(String vhostName, String appName, String appInstanceName)
	{
		IVHost vhost = VHostSingleton.getInstance(vhostName);
		if(vhost != null)
		{
			if (vhost.startApplicationInstance(appName, appInstanceName))	//invoke OnAppsrart in all managers
			{
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
