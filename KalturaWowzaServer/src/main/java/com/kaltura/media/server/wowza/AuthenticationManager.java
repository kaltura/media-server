package com.kaltura.media.server.wowza;

/**
 * Created by ron.yadgar on 09/05/2016.
 */



import com.kaltura.client.*;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.enums.KalturaEntryServerNodeType;
import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.module.ModuleBase;
import com.wowza.wms.application.WMSProperties;
import com.wowza.wms.client.IClient;
import com.wowza.wms.request.RequestFunction;
import com.wowza.wms.amf.AMFDataList;
import com.wowza.wms.stream.IMediaStream;
import com.kaltura.client.KalturaClient;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthenticationManager extends ModuleBase  {

    protected final static String CLIENT_PROPERTY_CONNECT_URL = "connecttcUrl";
    protected final static String REQUEST_PROPERTY_PARTNER_ID = "p";
    protected final static String REQUEST_PROPERTY_ENTRY_ID = "e";
    protected final static String REQUEST_PROPERTY_SERVER_INDEX = "i";
    protected final static String REQUEST_PROPERTY_TOKEN = "t";
    protected final static String CLIENT_PROPERTY_PARTNER_ID = "partnerId";
    protected final static String CLIENT_PROPERTY_SERVER_INDEX = "serverIndex";
    protected final static String CLIENT_PROPERTY_ENTRY_ID = "entryId";
    protected final static String CLIENT_PROPERTY_KALTURA_LIVE_ENTRY = "KalturaLiveEntry";
    protected final static String APPLICATION_MANAGERS_PROPERTY_NAME = "ApplicationManagers";
    private static final Logger logger = Logger.getLogger(AuthenticationManager.class);
    protected KalturaConfiguration config;
    private KalturaClient client;
    @SuppressWarnings("serial") //todo - do we need it?
    public class ClientConnectException2 extends Exception{

        public ClientConnectException2(String message) {
            super(message);
        }
    }
    public void onAppStart(IApplicationInstance appInstance) {
        logger.info("Initiallizing "+appInstance.getName());
    }

    public void onConnect(IClient client, RequestFunction function, AMFDataList params) {
        WMSProperties properties = client.getProperties();
        String rtmpUrl = properties.getPropertyStr(CLIENT_PROPERTY_CONNECT_URL);
        logger.debug("Geting url: " + rtmpUrl+ " from client "+client.getIp());
        String queryString = getRtmpUrlParameters(rtmpUrl);
        if (queryString ==null) {
            logger.error("Invalid rtmp url provided: " + rtmpUrl);
            client.rejectConnection("Invalid rtmp url provided: " + rtmpUrl);
            client.shutdownClient();
            return ;
        }

        try {
             onClientConnect(properties, queryString);
        } catch (KalturaApiException | ClientConnectException2 | NullPointerException  e) {
            logger.error("Entry authentication failed with url [" + rtmpUrl + "]: " + e.getMessage());
            client.rejectConnection("Unable to authenticate ur; [" + rtmpUrl + "]");
            client.shutdownClient();
        }

        return ;

    }

    private KalturaLiveEntry onClientConnect(WMSProperties properties, String queryString) throws KalturaApiException, ClientConnectException2 {
        logger.info("Query-String [" + queryString + "]");

        String[] queryParams = queryString.split("&");
        HashMap<String, String> requestParams = new HashMap<String, String>();
        String[] queryParamsParts;
        for (int i = 0; i < queryParams.length; ++i) {
            queryParamsParts = queryParams[i].split("=", 2);
            if (queryParamsParts.length == 2)
                requestParams.put(queryParamsParts[0], queryParamsParts[1]);
        }

        if (!requestParams.containsKey(REQUEST_PROPERTY_PARTNER_ID) || !requestParams.containsKey(REQUEST_PROPERTY_ENTRY_ID) || !requestParams.containsKey(REQUEST_PROPERTY_SERVER_INDEX)){
            throw new ClientConnectException2("Missing arguments [" + queryString + "]");
        }

        int partnerId = Integer.parseInt(requestParams.get(REQUEST_PROPERTY_PARTNER_ID));
        String entryId = requestParams.get(REQUEST_PROPERTY_ENTRY_ID);
        String token = requestParams.get(REQUEST_PROPERTY_TOKEN);
        KalturaEntryServerNodeType serverIndex = KalturaEntryServerNodeType.get(requestParams.get(REQUEST_PROPERTY_SERVER_INDEX));

        KalturaLiveEntry liveEntry = KalturaAPI.getKalturaAPI().authenticate(entryId, partnerId, token, serverIndex);
        //todo check if all parameters needed
        properties.setProperty(CLIENT_PROPERTY_PARTNER_ID, partnerId);
        properties.setProperty(CLIENT_PROPERTY_SERVER_INDEX, requestParams.get(REQUEST_PROPERTY_SERVER_INDEX));
        properties.setProperty(CLIENT_PROPERTY_ENTRY_ID, entryId);
        properties.setProperty(CLIENT_PROPERTY_KALTURA_LIVE_ENTRY, liveEntry);
        logger.info("Entry added [" + entryId + "]");

        return liveEntry;
    }


    public void onDisconnect(IClient client) {
        WMSProperties clientProperties = client.getProperties();
        if (clientProperties.containsKey(CLIENT_PROPERTY_ENTRY_ID)) {
            String entryId = clientProperties.getPropertyStr(CLIENT_PROPERTY_ENTRY_ID);
            logger.info("Entry removed [" + entryId + "]");
        }
    }

    private String getRtmpUrlParameters(String rtmpUrl) {

        final String OldPattern= "\\/kLive\\/\\?(p=[0-9]+)&(e=[01]_[\\d\\w]{8})&(i=[01])&(t=[\\d\\w]+)";
        final String NewPattern= "rtmp:\\/\\/([01]_[\\d\\w]{8}).([pb])\\.kpublish\\.kaltura\\.com:\\d*\\/kLive\\/\\?(p=[0-9]+)&(t=[\\d\\w]+)";
        Matcher matcher;


        //first, try the old url pattern
        matcher = getMatches(rtmpUrl, OldPattern);

        if (matcher != null && matcher.groupCount() ==4) {
            return  matcher.group(1)+'&'+matcher.group(2)+'&'+matcher.group(3)+'&'+matcher.group(4);
        }
        else{
            //if not match, try the new pattrn
            matcher = getMatches(rtmpUrl, NewPattern);
            if (matcher != null  && matcher.groupCount() ==4) {

                String i= matcher.group(2).equals("p") ? "i=0" : "i=1" ;
                return  "e="+matcher.group(1)+'&'+i+'&'+matcher.group(3)+'&'+matcher.group(4);
            }

        }
        return null;
    }
    private Matcher getMatches(String streamName, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(streamName);
        if (!matcher.find()) {
            logger.info("Stream [" + streamName + "] does not match regex");
            return null;
        }

        return matcher;
    }

}
