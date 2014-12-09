package com.kaltura.media.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.kaltura.client.KalturaClient;
import com.kaltura.client.KalturaConfiguration;
import com.kaltura.client.enums.KalturaSessionType;
import com.kaltura.media.server.api.IWebService;
import com.kaltura.media.server.api.KalturaWebServicesServer;
import com.kaltura.media.server.managers.IManager;

public class KalturaServer {
	public static int MEDIA_SERVER_PARTNER_ID = -5;

	public final static String KALTURA_SERVER_SECRET_KEY = "KalturaServerSecretKey";
	
	protected final static String KALTURA_SERVER_URL = "KalturaServerURL";
	protected final static String KALTURA_SERVER_ADMIN_SECRET = "KalturaServerAdminSecret";
	protected final static String KALTURA_SERVER_PARTNER_ID = "KalturaPartnerId";
	protected final static String KALTURA_SERVER_TIMEOUT = "KalturaServerTimeout";
	protected final static String KALTURA_SERVER_MANAGERS = "KalturaServerManagers";
	protected final static String KALTURA_SERVER_WEB_SERVICES = "KalturaServerWebServices";
	protected final static String KALTURA_SERVER_WEB_SERVICES_PORT = "KalturaServerWebServicesPort";
	protected final static String KALTURA_SERVER_WEB_SERVICES_HOST = "KalturaServerWebServicesHost";

	protected static Logger logger = Logger.getLogger(KalturaServer.class);
	protected static KalturaServer instance;
	protected static Map<String, Object> config;
	protected static KalturaClient client;
	protected static String hostname;
	protected static KalturaWebServicesServer webServicesServer;

	private static String OS;

	private static List<String> initManagers = new ArrayList<String>();
	protected static List<IManager> managers;

	protected KalturaServer() throws KalturaServerException {
		InputStream versionInputStream = getClass().getResourceAsStream("VERSION.txt");
		BufferedReader versionBufferedReader = new BufferedReader(new InputStreamReader(versionInputStream));
		String version = "Version not found";
		
		try {
			version = versionBufferedReader.readLine();
		} catch (IOException e1) {
			logger.error("Failed to read version file");
		}
		
		logger.debug("Initializing Kaltura server [" + version + "]");
		KalturaServer.instance = this;
		
		try {
			hostname = InetAddress.getLocalHost().getHostName();
			logger.debug("Kaltura server host name: " + hostname);
		} catch (UnknownHostException e) {
			throw new KalturaServerException("Failed to determine server host name: " + e.getMessage());
		}

		initClient();

		managers = new ArrayList<IManager>();
		if (!config.containsKey(KalturaServer.KALTURA_SERVER_MANAGERS)) {
			logger.error("Server managers [" + KalturaServer.KALTURA_SERVER_MANAGERS + "] are not defined");
		}
		else{
			String managersNames = (String) config.get(KalturaServer.KALTURA_SERVER_MANAGERS);
			logger.debug("Initializing server managers: " + managersNames);
			initServerManagers(managersNames.replaceAll(" ", "").split(","));
		}

		if (config.containsKey(KalturaServer.KALTURA_SERVER_WEB_SERVICES)) {
			int port = 888;
			String host = hostname;
			try {
				Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
	            while(networkInterfaces.hasMoreElements()){
	                NetworkInterface networkInterface = networkInterfaces.nextElement();
	                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
	                while(inetAddresses.hasMoreElements()){
	                    InetAddress inetAddress = inetAddresses.nextElement();
	                    host = inetAddress.getHostAddress();
	                }
	            }
			} catch (SocketException e) {
				logger.error("Find local server IP address: " + e.getMessage());
			}
			
			if (config.containsKey(KalturaServer.KALTURA_SERVER_WEB_SERVICES_PORT)) 
				port = Integer.parseInt((String) config.get(KalturaServer.KALTURA_SERVER_WEB_SERVICES_PORT));

			if (config.containsKey(KalturaServer.KALTURA_SERVER_WEB_SERVICES_HOST)) 
				host = (String) config.get(KalturaServer.KALTURA_SERVER_WEB_SERVICES_HOST);
			
			webServicesServer = new KalturaWebServicesServer(host, port, logger);
			String servicesNames = (String) config.get(KalturaServer.KALTURA_SERVER_WEB_SERVICES);
			logger.debug("Initializing web services: " + servicesNames);
			initWebServices(servicesNames.replaceAll(" ", "").split(","));
		}
	}

	protected void initClient() throws KalturaServerException {
		final KalturaConfiguration clientConfig = new KalturaConfiguration();

		int partnerId = config.containsKey(KalturaServer.KALTURA_SERVER_PARTNER_ID) ? (int) config.get(KalturaServer.KALTURA_SERVER_PARTNER_ID) : MEDIA_SERVER_PARTNER_ID;
		
		clientConfig.setPartnerId(partnerId);

		clientConfig.setClientTag("MediaServer-" + hostname);

		if (!config.containsKey(KalturaServer.KALTURA_SERVER_URL))
			throw new KalturaServerException("Missing configuration [" + KalturaServer.KALTURA_SERVER_URL + "]");

		if (!config.containsKey(KalturaServer.KALTURA_SERVER_ADMIN_SECRET))
			throw new KalturaServerException("Missing configuration [" + KalturaServer.KALTURA_SERVER_ADMIN_SECRET + "]");

		clientConfig.setEndpoint((String) config.get(KalturaServer.KALTURA_SERVER_URL));
		logger.debug("Initializing Kaltura client, URL: " + clientConfig.getEndpoint());

		if (config.containsKey(KalturaServer.KALTURA_SERVER_TIMEOUT))
			clientConfig.setTimeout(Integer.parseInt((String) config.get(KalturaServer.KALTURA_SERVER_TIMEOUT)) * 1000);

		client = new KalturaClient(clientConfig);
		generateClientSession();

		TimerTask generateSession = new TimerTask() {
			
			@Override
			public void run() {
				generateClientSession();
			}
		};

		long sessionGenerationInterval = 86000000;
		
		Timer timer = new Timer("clientSessionGeneration", true);
		timer.schedule(generateSession, sessionGenerationInterval, sessionGenerationInterval);
	}

	protected void generateClientSession() {
		int partnerId = config.containsKey(KalturaServer.KALTURA_SERVER_PARTNER_ID) ? (int) config.get(KalturaServer.KALTURA_SERVER_PARTNER_ID) : MEDIA_SERVER_PARTNER_ID;
		String adminSecretForSigning = (String) config.get(KalturaServer.KALTURA_SERVER_ADMIN_SECRET);
		String userId = "MediaServer";
		KalturaSessionType type = KalturaSessionType.ADMIN;
		int expiry = 86400;
		String privileges = "disableentitlement";
		String sessionId;
		
		try {
			sessionId = client.generateSession(adminSecretForSigning, userId, type, partnerId, expiry, privileges);
		} catch (Exception e) {
			logger.error("Initializing Kaltura client, URL: " + client.getKalturaConfiguration().getEndpoint());
			return;
		}

		client.setSessionId(sessionId);
		logger.debug("Kaltura client session id: " + sessionId);
	}

	protected void initWebServices(String[] servicesNames) throws KalturaServerException {

		Object obj; 
		IWebService service;
		for (String serviceName : servicesNames) {
			try {
				obj = Class.forName(serviceName).newInstance();
				logger.debug("Initializing Kaltura web service " + obj.getClass().getName());
			} catch (ClassNotFoundException e) {
				throw new KalturaServerException("Web service class [" + serviceName + "] not found");
			} catch (Exception e) {
				throw new KalturaServerException("Web service class [" + serviceName + "] failed to instantiate", e);
			}
			
			if(!(obj instanceof IWebService))
				throw new KalturaServerException("Web service class [" + serviceName + "] is not instance of IManager");
			
			service = (IWebService) obj;
			webServicesServer.addService(service);
			logger.info("Initialized Kaltura web service " + obj.getClass().getName());
		}

	}
	
	public static synchronized void setManagerInitialized(String managerName) {
		logger.debug("Initialized Kaltura manager " + managerName);

		if(initManagers == null || !initManagers.contains(managerName)){
			logger.error("Manager [" + managerName + "] already initialized");
			return;
		}
		
		initManagers.remove(managerName);
		
		if(initManagers.isEmpty()){
			initManagers = null;
			logger.debug("All managers initialized");
		}
	}
	
	public void initApplicationManagers (String[] managersNames) throws KalturaServerException {
		this.initManagers(managersNames);
	}
	
	protected void initServerManagers(String[] managersNames) throws KalturaServerException {
		
		for (String managerName : managersNames) {
			initManagers.add(managerName);			
		}
		
		this.initManagers(managersNames);
	}
	
	protected void initManagers(String[] managersNames) throws KalturaServerException {
		Object obj; 
		IManager manager;
		
		for (String managerName : managersNames) {
			try {
				obj = Class.forName(managerName).newInstance();
				logger.debug("Initializing Kaltura manager " + obj.getClass().getName());
			} catch (ClassNotFoundException e) {
				throw new KalturaServerException("Server manager class [" + managerName + "] not found");
			} catch (Exception e) {
				throw new KalturaServerException("Server manager class [" + managerName + "] failed to instantiate", e);
			}
			
			if(!(obj instanceof IManager))
				throw new KalturaServerException("Server manager class [" + managerName + "] is not instance of IManager");
			
			manager = (IManager) obj;
			managers.add(manager);
			manager.init();
			logger.info("Initialized Kaltura manager " + obj.getClass().getName());
		}
	}

	public static <T extends IManager> IManager getManager(Class<T> managerInterface){
		if(instance == null || managers == null){
			logger.error("Managers are not initialized");
			return null;
		}

		if(!managerInterface.isInterface() || !IManager.class.isAssignableFrom(managerInterface))
			return null;
		
		for(IManager manager : managers){
			if(managerInterface.isInstance(manager))
				return manager;
		}

		logger.error("Manager [" + managerInterface.getName() + "] not found");
		return null;
	}

	public static KalturaServer init(Map<String, Object> config) throws KalturaServerException {
		KalturaServer.config = config;
		
		if (instance == null)
			instance = new KalturaServer();

		return instance;
	}

	public void stop() {
		webServicesServer.shutdown();
		
		for(IManager manager : managers){
			manager.stop();
		}
	}

	public static boolean isInitialized() {
		if (instance == null || initManagers != null){
			return false;
		}
		
		return true;
	}

	public static KalturaServer getInstance() throws KalturaServerException {
		if (instance == null)
			throw new KalturaServerException("Server not initialized");

		return instance;
	}

	public static Map<String, Object> getConfiguration() {
		return config;
	}

	public static Object getConfiguration(String key) {
		if(config.containsKey(key)){
			return config.get(key);
		}
		
		return null;
	}

	public static KalturaClient getClient() {
		return client;
	}

	public static String getHostName() {
		return hostname;
	}

	public static boolean isWindows() {
		return getOsName().startsWith("Windows");
	}

	protected static String getOsName() {
		if(OS == null)
			OS = System.getProperty("os.name");
		
		return OS;
	}
}
