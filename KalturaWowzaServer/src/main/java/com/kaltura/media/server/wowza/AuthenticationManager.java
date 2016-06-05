package com.kaltura.media.server.wowza;

/**
 * Created by ron.yadgar on 09/05/2016.
 */



import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.client.KalturaApiException;
import com.kaltura.media.server.wowza.Constants;
import com.kaltura.client.enums.KalturaEntryServerNodeType;
import com.kaltura.media.server.wowza.listeners.KalturaUncaughtExceptionHnadler;
import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.module.ModuleBase;
import com.wowza.wms.application.WMSProperties;
import com.wowza.wms.client.IClient;
import com.wowza.wms.request.RequestFunction;
import com.wowza.wms.amf.AMFDataList;
import com.wowza.wms.stream.IMediaStream;


import org.apache.log4j.Logger;

import java.util.HashMap;

public class AuthenticationManager extends ModuleBase  {



    private static final Logger logger = Logger.getLogger(AuthenticationManager.class);

    @SuppressWarnings("serial")
    public class ClientConnectException extends Exception{

        public ClientConnectException(String message) {
            super(message);
        }
    }
    public void onAppStart(IApplicationInstance appInstance) {
        logger.info("Initiallizing "+appInstance.getName());
    }

    public void onConnect(IClient client, RequestFunction function, AMFDataList params) {
        WMSProperties properties = client.getProperties();
        String rtmpUrl = properties.getPropertyStr(Constants.CLIENT_PROPERTY_CONNECT_URL);
        logger.debug("Geting url: " + rtmpUrl+ " from client "+client.getIp());

        try {
            HashMap<String, String>  queryParameters = Utils.getRtmpUrlParameters(rtmpUrl, client.getQueryStr());
            onClientConnect(properties, queryParameters);
        } catch (ClientConnectException  e) {
            logger.error("Entry authentication failed with url [" + rtmpUrl + "]: " + e.getMessage());
            sendClientOnStatusError((IClient)client, "NetStream.Play.Failed","Unable to authenticate url; [" + rtmpUrl + "] ("+ e.getMessage()+")");
            client.shutdownClient();
        } catch (Exception  e) {
            logger.error("Entry authentication failed with url [" + rtmpUrl + "]: " + e.getMessage());
            sendClientOnStatusError((IClient)client, "NetStream.Play.Failed","Unable to authenticate url; [" + rtmpUrl + "]: " + e.getMessage());
            client.shutdownClient();
        }
    }

    private KalturaLiveEntry onClientConnect(WMSProperties properties, HashMap<String, String> requestParams) throws KalturaApiException, ClientConnectException {



        if (!requestParams.containsKey(Constants.REQUEST_PROPERTY_ENTRY_ID)){
            throw new ClientConnectException("Missing argument: entryId");
        }
        if (!requestParams.containsKey(Constants.REQUEST_PROPERTY_TOKEN)){
            throw new ClientConnectException("Missing argument: token");
        }
        if (!requestParams.containsKey(Constants.REQUEST_PROPERTY_PARTNER_ID)){
            throw new ClientConnectException("Missing argument: partnerId");
        }
        if (!requestParams.containsKey(Constants.REQUEST_PROPERTY_SERVER_INDEX)){
            throw new ClientConnectException("Missing argument: server index");
        }

        int partnerId = Integer.parseInt(requestParams.get(Constants.REQUEST_PROPERTY_PARTNER_ID));
        String entryId = requestParams.get(Constants.REQUEST_PROPERTY_ENTRY_ID);
        String token = requestParams.get(Constants.REQUEST_PROPERTY_TOKEN);

        KalturaEntryServerNodeType serverIndex = KalturaEntryServerNodeType.get(requestParams.get(Constants.REQUEST_PROPERTY_SERVER_INDEX));
        KalturaLiveEntry liveEntry = KalturaAPI.getKalturaAPI().authenticate(entryId, partnerId, token, serverIndex);

        properties.setProperty(Constants.CLIENT_PROPERTY_SERVER_INDEX, requestParams.get(Constants.REQUEST_PROPERTY_SERVER_INDEX));
        properties.setProperty(Constants.CLIENT_PROPERTY_KALTURA_LIVE_ENTRY, liveEntry);
        logger.info("Entry added [" + entryId + "]");

        return liveEntry;
    }


    public void onDisconnect(IClient client) {
        WMSProperties clientProperties = client.getProperties();
        if (clientProperties.containsKey(Constants.CLIENT_PROPERTY_KALTURA_LIVE_ENTRY)) {
            KalturaLiveEntry liveEntry = (KalturaLiveEntry) clientProperties.getProperty(Constants.CLIENT_PROPERTY_KALTURA_LIVE_ENTRY);
            logger.info("Entry removed [" + liveEntry.id + "]");
        }
    }
}
