package com.kaltura.media_server.services;

import com.kaltura.media_server.services.Constants;
import com.kaltura.client.*;
import com.kaltura.client.enums.KalturaSessionType;
import com.kaltura.client.types.*;
import com.kaltura.client.enums.KalturaEntryServerNodeType;
import com.kaltura.client.services.KalturaPermissionService;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ron.yadgar on 15/05/2016.
 */

public class KalturaAPI {

    // use the same session key for all Wowza sessions, so all (within a DC) will be directed to the same sphinx to prevent synchronization problems


    private static Logger logger = Logger.getLogger(KalturaAPI.class);
    private static Map<String, Object> serverConfiguration;
    private static KalturaClient client;
    private static String hostname;
    private   KalturaConfiguration clientConfig;
    private  static KalturaAPI KalturaAPIInstance = null;
    private final int ENABLE = 1;

    public static synchronized void  initKalturaAPI(Map<String, Object> serverConfiguration)  throws KalturaServerException {
        if  (KalturaAPIInstance!=null){
            logger.warn("services.KalturaAPI instance is already initialized");
            return;
        }
        KalturaAPIInstance =  new KalturaAPI(serverConfiguration);
    }

    public static synchronized KalturaAPI getKalturaAPI(){
        if (KalturaAPIInstance== null){
            throw new NullPointerException("services.KalturaAPI is not initialized");
        }
        return KalturaAPIInstance;
    }

    private KalturaAPI(Map<String, Object> serverConfiguration)  throws KalturaServerException {
        logger.info("Initializing KalturaUncaughtException handler");
        this.serverConfiguration = serverConfiguration;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
            logger.debug("Kaltura server host name: " + hostname);
            initClient();
        } catch (Exception e) {
            if (e instanceof UnknownHostException){
                logger.error("Failed to determine server host name: ", e);
            }
            throw new KalturaServerException("Error while loading services.KalturaAPI: " + e.getMessage());
        }
    }

    private void initClient() throws KalturaServerException {
        clientConfig = new KalturaConfiguration();

        int partnerId = serverConfiguration.containsKey(Constants.KALTURA_SERVER_PARTNER_ID) ? (int) serverConfiguration.get(Constants.KALTURA_SERVER_PARTNER_ID) : Constants.MEDIA_SERVER_PARTNER_ID;


        if (!serverConfiguration.containsKey(Constants.KALTURA_SERVER_URL))
            throw new KalturaServerException("Missing configuration [" + Constants.KALTURA_SERVER_URL + "]");

        if (!serverConfiguration.containsKey(Constants.KALTURA_SERVER_ADMIN_SECRET))
            throw new KalturaServerException("Missing configuration [" + Constants.KALTURA_SERVER_ADMIN_SECRET + "]");

        clientConfig.setEndpoint((String) serverConfiguration.get(Constants.KALTURA_SERVER_URL));
        logger.debug("Initializing Kaltura client, URL: " + clientConfig.getEndpoint());

        if (serverConfiguration.containsKey(Constants.KALTURA_SERVER_TIMEOUT))
            clientConfig.setTimeout(Integer.parseInt((String) serverConfiguration.get(Constants.KALTURA_SERVER_TIMEOUT)) * 1000);

        client = new KalturaClient(clientConfig);
        client.setPartnerId(partnerId);
        client.setClientTag("MediaServer-" + hostname);
        generateClientSession();

        TimerTask generateSession = new TimerTask() {

            @Override
            public void run() {     //run every 24 hours
                generateClientSession();
            }
        };

        long sessionGenerationInterval = 23*60*60*1000;// refresh every  23 hours  (KS is valid for a 24h);

        Timer timer = new Timer("clientSessionGeneration", true);
        timer.schedule(generateSession, sessionGenerationInterval, sessionGenerationInterval);
    }

    private void generateClientSession() {
        int partnerId = serverConfiguration.containsKey(Constants.KALTURA_SERVER_PARTNER_ID) ? (int) serverConfiguration.get(Constants.KALTURA_SERVER_PARTNER_ID) : Constants.MEDIA_SERVER_PARTNER_ID;
        String adminSecretForSigning = (String) serverConfiguration.get(Constants.KALTURA_SERVER_ADMIN_SECRET);
        String userId = "MediaServer";
        KalturaSessionType type = KalturaSessionType.ADMIN;
        int expiry = 86400; // ~24 hours
        String privileges = "disableentitlement,sessionkey:" + Constants.KALTURA_PERMANENT_SESSION_KEY;
        String sessionId;

        try {
            sessionId = client.generateSession(adminSecretForSigning, userId, type, partnerId, expiry, privileges);
        } catch (Exception e) {
            logger.error("Initializing Kaltura client, URL: " + client.getKalturaConfiguration().getEndpoint());
            return;
        }

        client.setSessionId(sessionId);
        logger.debug("Kaltura client session id: " + sessionId);    //session id - KS
    }

    public KalturaLiveStreamEntry authenticate(String entryId, int partnerId, String token, KalturaEntryServerNodeType serverIndex) throws Exception {
        if (partnerId == -5){
            KalturaClient Client= getClient();
            KalturaLiveEntry liveEntry = Client.getLiveStreamService().get(entryId);
            partnerId = liveEntry.partnerId;
        }

        KalturaClient impersonateClient = impersonate(partnerId);

        KalturaLiveStreamEntry updatedEntry = impersonateClient.getLiveStreamService().authenticate(entryId, token, hostname, serverIndex);

        return updatedEntry;
    }

    private KalturaClient getClient() {
        logger.warn("getClient");
        return client;

        //KalturaClient cloneClient = new KalturaClient(clientConfig);
        //cloneClient.setSessionId(client.getSessionId());
        //return cloneClient;
    }

    private KalturaClient  impersonate(int partnerId) {

        KalturaConfiguration impersonateConfig = new KalturaConfiguration();
        impersonateConfig.setEndpoint(clientConfig.getEndpoint());
        impersonateConfig.setTimeout(clientConfig.getTimeout());

        KalturaClient cloneClient = new KalturaClient(impersonateConfig);
        cloneClient.setPartnerId(partnerId);
        cloneClient.setClientTag(client.getClientTag());
        cloneClient.setSessionId(client.getSessionId());

        return cloneClient;
    }

    public KalturaLiveAsset getAssetParams(KalturaLiveEntry liveEntry, int assetParamsId) {
        //check this function
        if(liveEntry.conversionProfileId <= 0) {
            return null;
        }
        KalturaConversionProfileAssetParamsFilter assetParamsFilter = new KalturaConversionProfileAssetParamsFilter();
        assetParamsFilter.conversionProfileIdEqual = liveEntry.conversionProfileId;

        KalturaLiveAssetFilter asstesFilter = new KalturaLiveAssetFilter();
        asstesFilter.entryIdEqual = liveEntry.id;

        KalturaClient impersonateClient = impersonate(liveEntry.partnerId);
        impersonateClient.startMultiRequest();
        try {
            impersonateClient.getConversionProfileAssetParamsService().list(assetParamsFilter);
            impersonateClient.getFlavorAssetService().list(asstesFilter);
            KalturaMultiResponse responses = impersonateClient.doMultiRequest();

            Object flavorAssetsList = responses.get(1);

            if(flavorAssetsList instanceof KalturaFlavorAssetListResponse){
                for(KalturaFlavorAsset liveAsset : ((KalturaFlavorAssetListResponse) flavorAssetsList).objects){
                    if(liveAsset instanceof KalturaLiveAsset){
                        if (liveAsset.flavorParamsId == assetParamsId){
                            return (KalturaLiveAsset)liveAsset;
                        }
                    }
                }
            }

        } catch (KalturaApiException e) {
            logger.error("Failed to load asset params for live entry [" + liveEntry.id + "]:" + e);
        }
        return null;
    }

    public KalturaFlavorAssetListResponse getKalturaFlavorAssetListResponse(KalturaLiveEntry liveEntry) {
        //check this function
        if(liveEntry.conversionProfileId <= 0) {
            return null;
        }

        KalturaLiveAssetFilter asstesFilter = new KalturaLiveAssetFilter();
        asstesFilter.entryIdEqual = liveEntry.id;

        KalturaClient impersonateClient = impersonate(liveEntry.partnerId);

        try {
            KalturaFlavorAssetListResponse list = impersonateClient.getFlavorAssetService().list(asstesFilter);
            return list;
        } catch (KalturaApiException e) {
            logger.error("Failed to load asset params for live entry [" + liveEntry.id + "]:" + e);
        }
        return null;
    }

    public KalturaLiveEntry appendRecording(int partnerId, String entryId, String assetId, KalturaEntryServerNodeType nodeType, String filePath, double duration, boolean isLastChunk) throws Exception{
        KalturaDataCenterContentResource resource = getContentResource(filePath, partnerId);
        KalturaClient impersonateClient = impersonate(partnerId);
        KalturaLiveEntry updatedEntry = impersonateClient.getLiveStreamService().appendRecording(entryId, assetId, nodeType, resource, duration, isLastChunk);

        return updatedEntry;
    }

    public boolean isNewRecordingEnabled(KalturaLiveEntry liveEntry) {
        try {
            KalturaClient impersonateClient = impersonate(liveEntry.partnerId);
            KalturaPermission kalturaPermission = impersonateClient.getPermissionService().get("FEATURE_LIVE_STREAM_KALTURA_RECORDING");

            return kalturaPermission.status.getHashCode() == ENABLE;
        }
        catch (KalturaApiException e) {
            logger.error("(" + liveEntry.id + ") Error checking New Recording Permission. Code: " + e.code + " Message: " + e.getMessage());
            return false;
        }
    }

    private KalturaDataCenterContentResource getContentResource (String filePath,  int partnerId) {
        if (!this.serverConfiguration.containsKey(Constants.KALTURA_SERVER_WOWZA_WORK_MODE) || (this.serverConfiguration.get(Constants.KALTURA_SERVER_WOWZA_WORK_MODE).equals(Constants.WOWZA_WORK_MODE_KALTURA))) {
            KalturaServerFileResource resource = new KalturaServerFileResource();
            resource.localFilePath = filePath;
            return resource;
        }
        else {
            KalturaClient impersonateClient = impersonate(partnerId);
            try {
                impersonateClient.startMultiRequest();
                impersonateClient.getUploadTokenService().add(new KalturaUploadToken());

                File fileData = new File(filePath);
                impersonateClient.getUploadTokenService().upload("{1:result:id}", new KalturaFile(fileData));
                KalturaMultiResponse responses = impersonateClient.doMultiRequest();

                KalturaUploadedFileTokenResource resource = new KalturaUploadedFileTokenResource();
                Object response = responses.get(1);
                if (response instanceof KalturaUploadToken)
                    resource.token = ((KalturaUploadToken)response).id;
                else {
                    if (response instanceof KalturaApiException) {
                    }
                    logger.error("Content resource creation error: " + ((KalturaApiException)response).getMessage());
                    return null;
                }

                return resource;

            } catch (KalturaApiException e) {
                logger.error("Content resource creation error: " + e);
            }
        }

        return null;
    }

    public void cancelReplace(KalturaLiveEntry liveEntry){

        try{
            KalturaClient impersonateClient = impersonate(liveEntry.partnerId);
            impersonateClient.getMediaService().cancelReplace(liveEntry.recordedEntryId);
        }
        catch (Exception e) {

            logger.error("Error occured: " + e);
        }
    }
    public static KalturaLiveAsset getliveAsset(KalturaFlavorAssetListResponse liveAssetList, int assetParamsId){

        for(KalturaFlavorAsset liveAsset :  liveAssetList.objects){
            if(liveAsset instanceof KalturaLiveAsset){
                if (liveAsset.flavorParamsId == assetParamsId){
                    return (KalturaLiveAsset)liveAsset;
                }
            }
        }
        return null;
    }



}