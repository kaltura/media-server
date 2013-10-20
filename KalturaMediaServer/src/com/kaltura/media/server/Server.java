package com.kaltura.media.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.kaltura.client.KalturaClient;
import com.kaltura.client.KalturaConfiguration;
import com.kaltura.client.enums.KalturaSessionType;

public class Server {
	public static int MEDIA_SERVER_PARTNER_ID = -5;

	public static String KALTURA_SERVER_URL = "KalturaServerURL";
	public static String KALTURA_SERVER_ADMIN_SECRET = "KalturaServerAdminSecret";
	public static String KALTURA_SERVER_TIMEOUT = "KalturaServerTimeout";
	public static String KALTURA_SERVER_MANAGERS = "KalturaServerManagers";

	protected static Server instance;
	protected static Map<String, ?> config;
	protected static KalturaClient client;
	protected static String hostname;
	
	protected List<IManager> managers;

	protected Server() throws ServerException {
		Server.instance = this;
		
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			throw new ServerException("Failed to determine server host name");
		}

		initClient();

		managers = new ArrayList<IManager>();
		if (config.containsKey(Server.KALTURA_SERVER_MANAGERS)) {
			String managersNames = (String) config.get(Server.KALTURA_SERVER_MANAGERS);
			initManagers(managersNames.replaceAll(" ", "").split(","));
		}
	}

	protected void initClient() throws ServerException {
		KalturaConfiguration clientConfig = new KalturaConfiguration();
		clientConfig.setPartnerId(Server.MEDIA_SERVER_PARTNER_ID);
		clientConfig.setClientTag("MediaServer-" + hostname);

		if (!config.containsKey(Server.KALTURA_SERVER_URL))
			throw new ServerException("Missing configuration [" + Server.KALTURA_SERVER_URL + "]");

		if (!config.containsKey(Server.KALTURA_SERVER_ADMIN_SECRET))
			throw new ServerException("Missing configuration [" + Server.KALTURA_SERVER_ADMIN_SECRET + "]");

		clientConfig.setEndpoint((String) config.get(Server.KALTURA_SERVER_URL));

		if (config.containsKey(Server.KALTURA_SERVER_TIMEOUT) && ((Object) config.get(Server.KALTURA_SERVER_TIMEOUT)) instanceof Integer)
			clientConfig.setTimeout((Integer) config.get(Server.KALTURA_SERVER_TIMEOUT));

		String adminSecretForSigning = (String) config.get(Server.KALTURA_SERVER_ADMIN_SECRET);
		String userId = "MediaServer";
		KalturaSessionType type = KalturaSessionType.ADMIN;
		int expiry = 86400;
		String privileges = "";

		client = new KalturaClient(clientConfig);
		try {
			client.setSessionId(client.generateSessionV2(adminSecretForSigning, userId, type, Server.MEDIA_SERVER_PARTNER_ID, expiry, privileges));
		} catch (Exception e) {
			throw new ServerException("Failed to generate Kaltura session key");
		}
	}

	protected void initManagers(String[] managersNames) throws ServerException {
		Object obj; 
		IManager manager;
		for (String managerName : managersNames) {
			try {
				obj = Class.forName(managerName).newInstance();
			} catch (ClassNotFoundException e) {
				throw new ServerException("Server manager class [" + managerName + "] not found");
			} catch (Exception e) {
				throw new ServerException("Server manager class [" + managerName + "] failed to instantiate", e);
			}
			
			if(!(obj instanceof IManager))
				throw new ServerException("Server manager class [" + managerName + "] is not instance of IManager");
			
			manager = (IManager) obj;
			managers.add(manager);
			manager.init();
		}
	}

	public static IManager getManager(Class<?> managerInterface){
		if(instance == null)
			return null;
		
		if(!managerInterface.isInterface() || !IManager.class.isAssignableFrom(managerInterface))
			return null;
		
		for(IManager manager : instance.managers){
			if(managerInterface.isInstance(manager))
				return manager;
		}
		
		return null;
	}

	public static Server init(Map<String, ?> config) throws ServerException {
		Server.config = config;
		
		if (instance == null)
			instance = new Server();

		return instance;
	}

	public static Server getInstance() throws ServerException {
		if (instance == null)
			throw new ServerException("Server not initialized");

		return instance;
	}

	public static Map<String, ?> getConfiguration() {
		return config;
	}

	public static KalturaClient getClient() {
		return client;
	}

	public static String getHostName() {
		return hostname;
	}
}
