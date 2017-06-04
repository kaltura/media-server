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
import com.wowza.wms.rtp.model.RTPSession;
import com.kaltura.media_server.services.*;

import java.util.HashMap;
import java.util.regex.Matcher;
import com.kaltura.media_server.services.KalturaStreamType;

public class AuthenticationModule extends ModuleBase  {
    private static final Logger logger = Logger.getLogger(AuthenticationModule.class);
    public static final String STREAM_ACTION_PROPERTY = "AuthenticatioStreamActionNotifier";
    private IApplicationInstance appInstance;
    @SuppressWarnings("serial")
    public class ClientConnectException extends Exception{

        public ClientConnectException(String message) {
            super(message);
        }
    }

    public void onAppStart(final IApplicationInstance appInstance) {
        logger.info("Initiallizing " + appInstance.getName());
        this.appInstance = appInstance;
        KalturaEntryDataPersistence.setAppInstance(appInstance);
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
            sendClientOnStatusError((IClient)client, "NetStream.Play.Failed","Failure! " + e.getMessage());
            DiagnosticsProvider.addRejectedRTMPStream(client, e.getMessage());
        }
    }

    private void onClientConnect(WMSProperties properties, HashMap<String, String> requestParams) throws KalturaApiException, ClientConnectException, Exception {

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
        String propertyServerIndex = requestParams.get(Constants.REQUEST_PROPERTY_SERVER_INDEX);
        String token = requestParams.get(Constants.REQUEST_PROPERTY_TOKEN);
        KalturaEntryServerNodeType serverIndex = KalturaEntryServerNodeType.get(propertyServerIndex);

        synchronized (properties) {
            properties.setProperty(Constants.CLIENT_PROPERTY_SERVER_INDEX, propertyServerIndex);
            properties.setProperty(Constants.KALTURA_LIVE_ENTRY_ID, entryId);
        }
        authenticate(entryId, partnerId, token, serverIndex);
    }

    private void authenticate(String entryId, int partnerId, String token, KalturaEntryServerNodeType serverIndex) throws KalturaApiException, ClientConnectException, Exception {
        Object authenticationLock = KalturaEntryDataPersistence.getLock(entryId);
        synchronized (authenticationLock) {
            try {
                logger.debug("(" + entryId + ") Starting authentication process");
                if (Boolean.TRUE.equals(KalturaEntryDataPersistence.getPropertyByEntry(entryId, Constants.KALTURA_ENTRY_AUTHENTICATION_ERROR_FLAG))) {
                    long currentTime = System.currentTimeMillis();
                    long errorTime = (long)KalturaEntryDataPersistence.getPropertyByEntry(entryId, Constants.KALTURA_ENTRY_AUTHENTICATION_ERROR_TIME);
                    String errMsg = (String)KalturaEntryDataPersistence.getPropertyByEntry(entryId, Constants.KALTURA_ENTRY_AUTHENTICATION_ERROR_MSG);
                    long timeDiff = (currentTime - errorTime)/1000;
                    throw new Exception("Connection blocked due to previous error [" + timeDiff + "] seconds ago: " + errMsg +
                            ". Try to connect in " + ((Constants.KALTURA_PERSISTENCE_DATA_MIN_ENTRY_TIME / 1000) - timeDiff) + " seconds");
                }
                long currentTime = System.currentTimeMillis();
                Object entryLastValidationTime = KalturaEntryDataPersistence.setProperty(entryId, Constants.KALTURA_ENTRY_VALIDATED_TIME, currentTime);

                if ((entryLastValidationTime == null) || (currentTime - (long)entryLastValidationTime > Constants.KALTURA_MIN_TIME_BETWEEN_AUTHENTICATIONS)) {
                    KalturaLiveEntry liveEntry = (KalturaLiveEntry) KalturaAPI.getKalturaAPI().authenticate(entryId, partnerId, token, serverIndex);
                    KalturaEntryDataPersistence.setProperty(entryId, Constants.CLIENT_PROPERTY_KALTURA_LIVE_ENTRY, liveEntry);
                    KalturaEntryDataPersistence.setProperty(entryId, Constants.KALTURA_ENTRY_AUTHENTICATION_ERROR_FLAG, false);
                    logger.info("(" + entryId + ") Entry authenticated successfully!");
                } else {
                    logger.debug("(" + entryId + ") Entry did not authenticate! Last authentication: [" + (currentTime - (long)entryLastValidationTime) + "] MS ago");
                }
            }
            catch (Exception e) {
                logger.error("(" + entryId + ") Exception was thrown during authentication process");
                KalturaEntryDataPersistence.setProperty(entryId, Constants.KALTURA_ENTRY_AUTHENTICATION_ERROR_FLAG, true);
                KalturaEntryDataPersistence.setProperty(entryId, Constants.KALTURA_ENTRY_AUTHENTICATION_ERROR_TIME, System.currentTimeMillis());
                KalturaEntryDataPersistence.setProperty(entryId, Constants.KALTURA_ENTRY_AUTHENTICATION_ERROR_MSG, e.getMessage());
                KalturaEntryDataPersistence.setProperty(entryId, Constants.KALTURA_ENTRY_VALIDATED_TIME, (long)0);
                throw new ClientConnectException("(" + entryId + ") authentication failed. " + e.getMessage());
            }
        }
    }

    public void onDisconnect(IClient client) {
        try{
            String entryId = Utils.getEntryIdFromClient(client);
            logger.info("(" + entryId + ") Entry stopped");
            KalturaEntryDataPersistence.entriesMapCleanUp();
        }
        catch (Exception  e){
            logger.info("Error" + e.getMessage());
        }
    }

    public void onStreamCreate(IMediaStream stream) {
        LiveStreamListener  actionListener = new LiveStreamListener();
        String streamName = Utils.getStreamName(stream);
        logger.debug("onStreamCreate - [" + streamName + "]");
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

    public void onRTPSessionCreate(RTPSession rtpSession)
    {
        String queryStr = rtpSession.getQueryStr();
        String uriStr = rtpSession.getUri();
        try {
            WMSProperties properties = rtpSession.getProperties();
            HashMap<String, String>  queryParameters = Utils.getRtmpUrlParameters(uriStr, queryStr);
            if (queryParameters.containsKey(Constants.REQUEST_PROPERTY_ENTRY_ID)) {
                logger.debug("onRTPSessionCreate - [" + queryParameters.get(Constants.REQUEST_PROPERTY_ENTRY_ID) + "]");
            }

            onClientConnect(properties, queryParameters);
        } catch (Exception  e) {
            logger.error("Entry authentication failed with url [" + uriStr + "]: " + e.getMessage());
            // 06-05-2017 todo: find which function call can be used to send error back to client
            // sendStreamOnStatusError doesn't work because there is no stream object available
            //sendStreamOnStatusError(rtpSession.getRTSPStream().getStream(), "NetStream.Play.Failed", error);
            appInstance.getVHost().getRTPContext().shutdownRTPSession(rtpSession);
            DiagnosticsProvider.addRejectedRTSPStream(rtpSession, e.getMessage());
        }
    }

    public void onRTPSessionDestroy(RTPSession rtpSession)
    {
        try{
            logger.debug("onRTPSessionDestroy - [" + rtpSession.getSessionId() + "]");
            String entryId = Utils.getEntryIdFromRTPSession(rtpSession);
            logger.info("Entry removed [" + entryId + "]");
            KalturaEntryDataPersistence.entriesMapCleanUp();
        }
        catch (Exception  e){
            logger.info("Error" + e.getMessage());
        }
    }



    class LiveStreamListener extends MediaStreamActionNotifyBase{

        private void shutdown(IMediaStream stream, String msg) {

            logger.error(msg);
            IClient client = stream.getClient();
            KalturaStreamType streamType = Utils.getStreamType(stream, stream.getName());

            switch(streamType) {
                case RTMP:
                    sendClientOnStatusError((IClient)client, "NetStream.Play.Failed", msg);
                    client.setShutdownClient(true);
                    DiagnosticsProvider.addRejectedRTMPStream(client, msg);
                    break;
                case RTSP: // rtp
                    sendStreamOnStatusError(stream, "NetStream.Play.Failed", msg);
                    RTPSession rtpSession = stream.getRTPStream().getSession();
                    rtpSession.setShutdownClient(true);
                    DiagnosticsProvider.addRejectedRTSPStream(rtpSession, msg);
                    break;
                default:
                    sendStreamOnStatusError(stream, "NetStream.Play.Failed", msg);
                    logger.error("Critical, " + streamType + " stream type! failed to play stream. " + msg);
                    break;

            }

        }

        public void onPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend) {
            if (stream.isTranscodeResult()){
                return;
            }
            WMSProperties properties = stream.getProperties();
            try {
                String entryByClient;
                if (stream.getClient() != null) {
                    IClient client = stream.getClient();
                    entryByClient = Utils.getEntryIdFromClient(client);
                }
                else if (stream.getRTPStream() != null && stream.getRTPStream().getSession() !=null) {
                    RTPSession rtpSession = stream.getRTPStream().getSession();
                    entryByClient = Utils.getEntryIdFromRTPSession(rtpSession);
                } else {
                    // Lilach todo: check if there's a way to shutdown unknown stream!!!
                    logger.error("Fatal Error! Client does not exist");
                    return;
                }
                Matcher matcher = Utils.getStreamNameMatches(streamName);
                if (matcher == null) {
                    String msg = "Published stream is invalid [" + streamName + "]";
                    shutdown(stream, msg);
                    return;
                }
                String entryByStream = matcher.group(1);
                String flavor = matcher.group(2);

                if (!entryByStream.equals(entryByClient)) {
                    String msg = "Published  stream name [" + streamName + "] does not match entry id [" + entryByClient  + "]";
                    shutdown(stream, msg);
                    return;
                }
                if (!Utils.isNumeric(flavor)) {
                    String msg = "Published  stream name [" + streamName + "], has wrong suffix stream name: " + flavor;
                    shutdown(stream, msg);
                    return;
                }

            }
            catch (Exception  e) {
                logger.error("Exception in onPublish: ", e);
            }
        }
    }
}
