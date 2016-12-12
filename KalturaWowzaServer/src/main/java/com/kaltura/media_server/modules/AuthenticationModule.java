package com.kaltura.media_server.modules;
/**
 * Created by ron.yadgar on 09/05/2016.
 */

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.enums.KalturaEntryServerNodeType;
import com.kaltura.client.types.KalturaLiveEntry;
import com.wowza.wms.amf.AMFDataList;
import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.application.WMSProperties;
import com.wowza.wms.client.IClient;
import com.wowza.wms.module.ModuleBase;
import com.wowza.wms.request.RequestFunction;
import org.apache.log4j.Logger;
import com.kaltura.media_server.services.*;
import java.util.ArrayList;

import java.util.HashMap;

public class AuthenticationModule extends ModuleBase  {


    ArrayList <HashMap<String,String> >rejectedStreams = new ArrayList <HashMap<String,String> >();
    private static final Logger logger = Logger.getLogger(AuthenticationModule.class);

    @SuppressWarnings("serial")
    public class ClientConnectException extends Exception{

        public ClientConnectException(String message) {
            super(message);
        }
    }
    public void onAppStart(IApplicationInstance appInstance) {
        WMSProperties properties = appInstance.getProperties();
        properties.setProperty(Constants.KALTURA_REJECTED_STREAMS, rejectedStreams);
        logger.info("Initiallizing " +appInstance.getName());
    }

    public void onConnect(IClient client, RequestFunction function, AMFDataList params) {

        WMSProperties properties = client.getProperties();
        String rtmpUrl = properties.getPropertyStr(Constants.CLIENT_PROPERTY_CONNECT_URL);
        String IP = client.getIp();
        logger.debug("Geting url: " + rtmpUrl+ " from client "+ IP);

        try {
            HashMap<String, String>  queryParameters = Utils.getRtmpUrlParameters(rtmpUrl, client.getQueryStr());
            onClientConnect(properties, queryParameters);
        } catch (Exception  e) {
            logger.error("Entry authentication failed with url [" + rtmpUrl + "]: " + e.getMessage());
            client.rejectConnection();
            sendClientOnStatusError((IClient)client, "NetStream.Play.Failed","Unable to authenticate url; [" + rtmpUrl + "]: " + e.getMessage());

        }
    }

    private void addRejectedStream(Exception  e, IClient client){

        WMSProperties properties = client.getProperties();
        String rtmpUrl = properties.getPropertyStr(Constants.CLIENT_PROPERTY_CONNECT_URL);
        String IP = client.getIp();

        HashMap<String,String> rejcetedStream =  new HashMap<String,String>();
        rejcetedStream.put("rtmpUrl", rtmpUrl);
        rejcetedStream.put("message", e.getMessage());
        rejcetedStream.put("IP", IP);
        String timeStamp = Long.toString(System.currentTimeMillis());
        rejcetedStream.put("Time" , timeStamp);
        if (rejectedStreams.size() >= Constants.KALTURA_REJECTED_STEAMS_SIZE){
            rejectedStreams.remove(0);
        }
        rejectedStreams.add(rejcetedStream);
    }
    private KalturaLiveEntry onClientConnect(WMSProperties properties, HashMap<String, String> requestParams) throws KalturaApiException, ClientConnectException, Exception {



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
