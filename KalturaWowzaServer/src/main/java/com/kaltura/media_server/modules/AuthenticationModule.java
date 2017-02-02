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
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.stream.MediaStreamActionNotifyBase;
import org.apache.log4j.Logger;
import com.kaltura.media_server.services.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.List;



public class AuthenticationModule extends ModuleBase  {

    private static final Logger logger = Logger.getLogger(AuthenticationModule.class);
    public static final String STREAM_ACTION_PROPERTY = "AuthenticatioStreamActionNotifier";
    @SuppressWarnings("serial")
    public class ClientConnectException extends Exception{

        public ClientConnectException(String message) {
            super(message);
        }
    }
    public void onAppStart(IApplicationInstance appInstance) {
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
            DiagnosticsProvider.addRejectedStream(e.getMessage(), client);
        }
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
        KalturaEntryIdKey key = KalturaEntryDataPersistence.setProperty(entryId, Constants.CLIENT_PROPERTY_KALTURA_LIVE_ENTRY, liveEntry);
        synchronized (properties) {
            properties.setProperty(Constants.KALTURA_ENTRY_DATA_PERSISTENCY_KEY, key);
        }
        logger.info("Entry added [" + entryId + "]");

        return liveEntry;
    }


    public void onDisconnect(IClient client) {
        try{
            WMSProperties properties = client.getProperties();
            String entryId = Utils.getEntryIdFromClient(client);
            synchronized (properties) {
                 // clear reference to persistence data
                properties.setProperty(Constants.KALTURA_ENTRY_DATA_PERSISTENCY_KEY, null);
            }

            logger.info("Entry removed [" + entryId + "]");
        }
        catch (Exception  e){
            logger.info("Error" + e.getMessage());
        }
    }

    public void onStreamCreate(IMediaStream stream) {
        LiveStreamListener  actionListener = new LiveStreamListener();
        logger.debug("onStreamCreate - [" + stream.getName() + "]");
        WMSProperties props = stream.getProperties();
        synchronized (props)
        {
            props.setProperty(STREAM_ACTION_PROPERTY, actionListener);
        }
        stream.addClientListener(actionListener);

    }

    public void onStreamDestroy(IMediaStream stream) {
        logger.debug("onStreamDestroy - [" + stream.getName() + "]");
        LiveStreamListener actionListener = null;
        WMSProperties props = stream.getProperties();
        synchronized (props)
        {
            actionListener = (LiveStreamListener) stream.getProperties().get(STREAM_ACTION_PROPERTY);
        }
        if (actionListener != null)
        {
            stream.removeClientListener(actionListener);
            logger.info("removeClientListener: " + stream.getSrc());
        }
    }


    class LiveStreamListener extends  MediaStreamActionNotifyBase{
        public void onPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend) {
            if (stream.isTranscodeResult()){
                return;
            }
            try {
                IClient client = stream.getClient();
                KalturaLiveEntry liveEntry = (KalturaLiveEntry) KalturaEntryDataPersistence.getProperty(streamName, Constants.CLIENT_PROPERTY_KALTURA_LIVE_ENTRY);
                Matcher matcher = Utils.getStreamNameMatches(streamName);

                if (matcher == null) {
                    String msg = "Published stream invalid [" + streamName + "]";
                    logger.error(msg);
                    sendClientOnStatusError((IClient)client, "NetStream.Play.Failed", msg);
                    stream.getClient().setShutdownClient(true);
                    DiagnosticsProvider.addRejectedStream(msg, client);
                    return;
                }
                String entryId = matcher.group(1);
                String flavor = matcher.group(2);


                if (! entryId.equals(liveEntry.id  )){
                    String msg = "Published  stream name [" + streamName + "] does not match entry id [" + liveEntry.id  + "]";
                    logger.error(msg);
                    sendClientOnStatusError((IClient)client, "NetStream.Play.Failed", msg);
                    stream.getClient().setShutdownClient(true);
                    DiagnosticsProvider.addRejectedStream(msg, client);
                    return;
                }
                if (! Utils.isNumeric(flavor)) {
                    String msg = "Published  stream name [" + streamName + "], Wrong suffix stream name:  "+flavor;
                    logger.error(msg);
                    sendClientOnStatusError((IClient)client, "NetStream.Play.Failed", msg);
                    stream.getClient().setShutdownClient(true);
                    DiagnosticsProvider.addRejectedStream(msg, client);
                    return;
                }

            }
            catch (Exception  e) {
                logger.error("Exception in onPublish: ", e);
            }
        }
    }

}
