package com.kaltura.media.server.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

import org.apache.log4j.Logger;

public class KalturaWebServicesServer {
	private String hostname;
	private int port;
	private Logger logger;

	HashMap<String, Endpoint> services = new HashMap<String, Endpoint>();

	public KalturaWebServicesServer(String hostname, int port, Logger logger)
	{
		this.hostname = hostname;
		this.port = port;
		this.logger = logger;
	}
	
	public Endpoint addService(IWebService service) 
	{
		WebService webService = service.getClass().getAnnotation(WebService.class);
		String name = webService.name();
		String endpointURL = "http://" + hostname + ":" + port + "/" + name;
		try
		{
			Endpoint endpoint = Endpoint.publish(endpointURL, service);
			services.put(service.getClass().getName(), endpoint);
			logger.info("Server.addService: " + endpointURL);
			return endpoint;
		}
		catch (Exception e)
		{
			logger.error("Server.addService: "+e.toString());
		}
		return null;
	}

	public void shutdown() {
		Iterator<Map.Entry<String, Endpoint>> serviceIter = services.entrySet().iterator();
		while(serviceIter.hasNext())
		{
			Endpoint endpoint = serviceIter.next().getValue();
			endpoint.stop();
			serviceIter.remove();
		}
	}
}
