package com.kaltura.media.server.managers;

import java.util.Map;

import com.kaltura.client.KalturaClient;
import com.kaltura.client.KalturaConfiguration;
import com.kaltura.media.server.KalturaServer;

abstract public class KalturaManager implements IManager {

	protected String hostname;
	private KalturaClient client;
	protected KalturaConfiguration config;
	protected Map<String, Object> serverConfiguration;

	protected KalturaClient impersonate(int partnerId) {
		KalturaConfiguration impersonateConfig = new KalturaConfiguration();
		impersonateConfig.setEndpoint(config.getEndpoint());
		impersonateConfig.setTimeout(config.getTimeout());

		KalturaClient cloneClient = new KalturaClient(impersonateConfig);
		cloneClient.setPartnerId(partnerId);
		cloneClient.setClientTag(client.getClientTag());
		cloneClient.setSessionId(client.getSessionId());

		return cloneClient;
	}
	
	/**
	 * Clones a client to create a new instance of it
	 * Pay attention that the KS isn't renewed and copied from the current client
	 * @return KalturaClient
	 */
	protected KalturaClient getClient() {
		KalturaClient cloneClient = new KalturaClient(config);
		cloneClient.setSessionId(client.getSessionId());
		return cloneClient;
	}

	public void init() throws KalturaManagerException {
		hostname = KalturaServer.getHostName();
		client = KalturaServer.getClient();
		config = client.getKalturaConfiguration();
		serverConfiguration = KalturaServer.getConfiguration();
	}

	protected void setInitialized() throws KalturaManagerException {
		KalturaServer.setManagerInitialized(getClass().getName());
	}
}
