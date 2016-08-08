package com.kaltura.media_server.modules;

import com.wowza.wms.amf.*;
import com.wowza.wms.application.*;
import com.wowza.wms.client.*;
import com.wowza.wms.logging.WMSLogger;
import com.wowza.wms.logging.WMSLoggerFactory;
import com.wowza.wms.logging.WMSLoggerIDs;
import com.wowza.wms.module.*;
import com.wowza.wms.request.*;
import com.wowza.wms.stream.*;

public class RTMPPublishDebugModule extends ModuleBase
{
	WMSLogger logger = null;
	String category = WMSLoggerIDs.CAT_application;
	String event = WMSLoggerIDs.EVT_comment;
	String context = "";
	
	public void onAppCreate(IApplicationInstance appInstance)
	{
		logger = WMSLoggerFactory.getLoggerObj(appInstance);
		context = appInstance.getContextStr();
		logger.info("ModuleClientPublishDebug.onAppCreate [" + context + "]", category, event);
	}

	public void onAppStop(IApplicationInstance appInstance)
	{
		logger.info("ModuleClientPublishDebug.onAppStop [" + context + "]", category, event);
	}

	public void onConnect(IClient client, RequestFunction function, AMFDataList params)
	{
		logger.info("ModuleClientPublishDebug.onConnect [" + context + "/" + client.getClientId() + "/" + System.currentTimeMillis() + "]", category, event);
	}

	public void onConnectAccept(IClient client)
	{
		logger.info("ModuleClientPublishDebug.onConnectAccept [" + context + "/" + client.getClientId() + "/" + System.currentTimeMillis() + "]", category, event);
	}

	public void onConnectReject(IClient client)
	{
		logger.info("ModuleClientPublishDebug.onConnectReject [" + context + "/" + client.getClientId() + "/" + System.currentTimeMillis() + "]", category, event);
	}

	public void onDisconnect(IClient client)
	{
		logger.info("ModuleClientPublishDebug.onDisconnect [" + context + "/" + client.getClientId() + "/" + System.currentTimeMillis() + "]", category, event);
	}

	public void onStreamCreate(IMediaStream stream)
	{
		logger.info("ModuleClientPublishDebug.onStreamCreate [" + context + "/" + stream.getSrc() + "/" + System.currentTimeMillis() + "]", stream, category, event, 200, null);
	}

	public void onStreamDestroy(IMediaStream stream)
	{
		logger.info("ModuleClientPublishDebug.onStreamDestroy [" + context + "/" + stream.getName() + "/" + stream.getSrc() + "/" + System.currentTimeMillis() + "]", stream, category, event, 200, null);
	}

	public void createStream(IClient client, RequestFunction function, AMFDataList params)
	{
		try
		{
			logger.info("ModuleClientPublishDebug.createStream entry [" + context + "/" + client.getClientId() + "/" + params.toString() + "/" + System.currentTimeMillis() + "]", category, event);
			invokePrevious(client, function, params);
			logger.info("ModuleClientPublishDebug.createStream exit [" + context + "/" + client.getClientId() + "/" + System.currentTimeMillis() + "]", category, event);
		}
		catch (Throwable t)
		{
			logger.error("ModuleClientPublishDebug.createStream error", t);
			throw t;
		}
	}

	public void initStream(IClient client, RequestFunction function, AMFDataList params)
	{
		try
		{
			logger.info("ModuleClientPublishDebug.initStream entry [" + context + "/" + client.getClientId() + "/" + params.toString() + "/" + System.currentTimeMillis() + "]", category, event);
			invokePrevious(client, function, params);
			logger.info("ModuleClientPublishDebug.initStream exit [" + context + "/" + client.getClientId() + "/" + System.currentTimeMillis() + "]", category, event);
		}
		catch (Throwable t)
		{
			logger.error("ModuleClientPublishDebug.initStream error", t);
			throw t;
		}
	}

	public void closeStream(IClient client, RequestFunction function, AMFDataList params)
	{
		try
		{
			logger.info("ModuleClientPublishDebug.closeStream entry [" + context + "/" + client.getClientId() + "/" + params.toString() + "/" + System.currentTimeMillis() + "]", category, event);
			invokePrevious(client, function, params);
			logger.info("ModuleClientPublishDebug.closeStream exit [" + context + "/" + client.getClientId() + "/" + System.currentTimeMillis() + "]", category, event);
		}
		catch (Throwable t)
		{
			logger.error("ModuleClientPublishDebug.closeStream error", t);
			throw t;
		}
	}

	public void deleteStream(IClient client, RequestFunction function, AMFDataList params)
	{
		try
		{
			logger.info("ModuleClientPublishDebug.deleteStream entry [" + context + "/" + client.getClientId() + "/" + params.toString() + "/" + System.currentTimeMillis() + "]", category, event);
			invokePrevious(client, function, params);
			logger.info("ModuleClientPublishDebug.deleteStream exit [" + context + "/" + client.getClientId() + "/" + System.currentTimeMillis() + "]", category, event);
		}
		catch (Throwable t)
		{
			logger.error("ModuleClientPublishDebug.deleteStream error", t);
			throw t;
		}
	}

	public void publish(IClient client, RequestFunction function, AMFDataList params)
	{
		try
		{
			logger.info("ModuleClientPublishDebug.publish entry [" + context + "/" + client.getClientId() + "/" + params.toString() + "/" + System.currentTimeMillis() + "]", category, event);
			invokePrevious(client, function, params);
			logger.info("ModuleClientPublishDebug.publish exit [" + context + "/" + client.getClientId() + "/" + System.currentTimeMillis() + "]", category, event);
		}
		catch (Throwable t)
		{
			logger.error("ModuleClientPublishDebug.publish error", t);
			throw t;
		}
	}

	public void releaseStream(IClient client, RequestFunction function, AMFDataList params)
	{
		try
		{
			logger.info("ModuleClientPublishDebug.releaseStream entry [" + context + "/" + client.getClientId() + "/" + params.toString() + "/" + System.currentTimeMillis() + "]", category, event);
			invokePrevious(client, function, params);
			logger.info("ModuleClientPublishDebug.releaseStream exit [" + context + "/" + client.getClientId() + "/" + System.currentTimeMillis() + "]", category, event);
		}
		catch (Throwable t)
		{
			logger.error("ModuleClientPublishDebug.releaseStream error", t);
			throw t;
		}
	}

	public void FCPublish(IClient client, RequestFunction function, AMFDataList params)
	{
		try
		{
			logger.info("ModuleClientPublishDebug.FCPublish entry [" + context + "/" + client.getClientId() + "/" + params.toString() + "/" + System.currentTimeMillis() + "]", category, event);
			invokePrevious(client, function, params);
			logger.info("ModuleClientPublishDebug.FCPublish exit [" + context + "/" + client.getClientId() + "/" + System.currentTimeMillis() + "]", category, event);
		}
		catch (Throwable t)
		{
			logger.error("ModuleClientPublishDebug.FCPublish error", t);
			throw t;
		}
	}

	public void FCUnPublish(IClient client, RequestFunction function, AMFDataList params)
	{
		try
		{
			logger.info("ModuleClientPublishDebug.FCUnPublish entry [" + context + "/" + client.getClientId() + "/" + params.toString() + "/" + System.currentTimeMillis() + "]", category, event);
			invokePrevious(client, function, params);
			logger.info("ModuleClientPublishDebug.FCUnPublish exit [" + context + "/" + client.getClientId() + "/" + System.currentTimeMillis() + "]", category, event);
		}
		catch (Throwable t)
		{
			logger.error("ModuleClientPublishDebug.FCUnPublish error", t);
			throw t;
		}
	}
}
