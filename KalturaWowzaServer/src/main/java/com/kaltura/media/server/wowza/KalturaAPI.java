package com.kaltura.media.server.wowza;

import com.kaltura.client.*;
import com.kaltura.client.enums.KalturaSessionType;
import com.kaltura.client.types.*;
import com.kaltura.media.server.KalturaServerException;
import com.kaltura.media.server.KalturaUncaughtExceptionHnadler;
import com.kaltura.client.enums.KalturaEntryServerNodeType;
import org.apache.log4j.Logger;

import java.io.File;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ron.yadgar on 15/05/2016.
 */
    //todo Is this class should be static? Singleton?
public class KalturaAPI {

    protected final static String KALTURA_SERVER_URL = "KalturaServerURL";
    protected final static String KALTURA_SERVER_ADMIN_SECRET = "KalturaServerAdminSecret";
    protected final static String KALTURA_SERVER_PARTNER_ID = "KalturaPartnerId";
    protected final static String KALTURA_SERVER_TIMEOUT = "KalturaServerTimeout";
    protected final static String KALTURA_WOWZA_SERVER_WORK_MODE = "KalturaWorkMode";
    protected final static String KALTURA_WOWZA_SERVER_WORK_MODE_KALTURA = "kaltura";

    public static int MEDIA_SERVER_PARTNER_ID = -5;

    // use the same session key for all Wowza sessions, so all (within a DC) will be directed to the same sphinx to prevent synchronization problems
    private final static String KALTURA_PERMANENT_SESSION_KEY = "kalturaWowzaPermanentSessionKey";

    protected static Logger logger = Logger.getLogger(KalturaAPI.class);
    protected static Map<String, Object> serverConfiguration;
    protected static KalturaClient client;
    protected static String hostname;
    private   KalturaConfiguration clientConfig;

    public KalturaAPI(Map<String, Object> serverConfiguration)  throws KalturaServerException {  //TODO CHECK THIS CODE
        logger.info("Initializing KalturaUncaughtException handler");
        this.serverConfiguration=serverConfiguration;
        Thread.setDefaultUncaughtExceptionHandler(new KalturaUncaughtExceptionHnadler());   //TODO CHECK THIS CODE
        try {
            hostname = InetAddress.getLocalHost().getHostName();
            logger.debug("Kaltura server host name: " + hostname);
        } catch (UnknownHostException e) {
            throw new KalturaServerException("Failed to determine server host name: " + e.getMessage());
        }
        initClient();
    }

    protected void initClient() throws KalturaServerException {
        clientConfig = new KalturaConfiguration();

        int partnerId = serverConfiguration.containsKey(KALTURA_SERVER_PARTNER_ID) ? (int) serverConfiguration.get(KALTURA_SERVER_PARTNER_ID) : MEDIA_SERVER_PARTNER_ID;


        if (!serverConfiguration.containsKey(KALTURA_SERVER_URL))
            throw new KalturaServerException("Missing configuration [" + KALTURA_SERVER_URL + "]");

        if (!serverConfiguration.containsKey(KALTURA_SERVER_ADMIN_SECRET))
            throw new KalturaServerException("Missing configuration [" + KALTURA_SERVER_ADMIN_SECRET + "]");

        clientConfig.setEndpoint((String) serverConfiguration.get(KALTURA_SERVER_URL));
        logger.debug("Initializing Kaltura client, URL: " + clientConfig.getEndpoint());

        if (serverConfiguration.containsKey(KALTURA_SERVER_TIMEOUT))
            clientConfig.setTimeout(Integer.parseInt((String) serverConfiguration.get(KALTURA_SERVER_TIMEOUT)) * 1000);

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

        long sessionGenerationInterval = 86000000;

        Timer timer = new Timer("clientSessionGeneration", true);
        timer.schedule(generateSession, sessionGenerationInterval, sessionGenerationInterval);
    }
    protected void generateClientSession() {
        int partnerId = serverConfiguration.containsKey(KALTURA_SERVER_PARTNER_ID) ? (int) serverConfiguration.get(KALTURA_SERVER_PARTNER_ID) : MEDIA_SERVER_PARTNER_ID;
        String adminSecretForSigning = (String) serverConfiguration.get(KALTURA_SERVER_ADMIN_SECRET);
        String userId = "MediaServer";
        KalturaSessionType type = KalturaSessionType.ADMIN;
        int expiry = 86400; // ~24 hours
        String privileges = "disableentitlement,sessionkey:" + KALTURA_PERMANENT_SESSION_KEY;
        String sessionId;

        try {   //TODO maybe should be less then 24 hours
            sessionId = client.generateSession(adminSecretForSigning, userId, type, partnerId, expiry, privileges);
        } catch (Exception e) {
            logger.error("Initializing Kaltura client, URL: " + client.getKalturaConfiguration().getEndpoint());
            return;
        }

        client.setSessionId(sessionId);
        logger.debug("Kaltura client session id: " + sessionId);    //session id - KS
    }
    public KalturaLiveStreamEntry authenticate(String entryId, int partnerId, String token, KalturaEntryServerNodeType serverIndex) throws KalturaApiException {
        KalturaClient impersonateClient = impersonate(partnerId);
        KalturaLiveStreamEntry liveStreamEntry = impersonateClient.getLiveStreamService().authenticate(entryId, token, hostname, serverIndex);  //todo note that before, hostname were passed by the function

        return liveStreamEntry;
    }
    protected KalturaClient  impersonate(int partnerId) {

        KalturaConfiguration impersonateConfig = new KalturaConfiguration();
        impersonateConfig.setEndpoint(clientConfig.getEndpoint());
        impersonateConfig.setTimeout(clientConfig.getTimeout());

        KalturaClient cloneClient = new KalturaClient(impersonateConfig);
        cloneClient.setPartnerId(partnerId);
        cloneClient.setClientTag(client.getClientTag());
        cloneClient.setSessionId(client.getSessionId());

        return cloneClient;
    }
    public KalturaLiveAsset getAssetParams(KalturaLiveEntry liveEntry) {
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


            Object conversionProfileAssetParamsList = responses.get(0);
            Object flavorAssetsList = responses.get(1);

            //if(conversionProfileAssetParamsList instanceof KalturaConversionProfileAssetParamsListResponse)
            //    conversionProfileAssetParams = ((KalturaConversionProfileAssetParamsListResponse) conversionProfileAssetParamsList).objects;

            if(flavorAssetsList instanceof KalturaFlavorAssetListResponse){
                for(KalturaFlavorAsset liveAsset : ((KalturaFlavorAssetListResponse) flavorAssetsList).objects){
                    if(liveAsset instanceof KalturaLiveAsset){
                     //   liveAssets.put(liveAsset.flavorParamsId, (KalturaLiveAsset) liveAsset);

                     //   if(!liveAssetParams.containsKey(liveAsset.flavorParamsId)){
                     //       KalturaFlavorParams liveParams = impersonateClient.getFlavorParamsService().get(liveAsset.flavorParamsId);
                     //       if(liveParams instanceof KalturaLiveParams)
                     //           liveAssetParams.put(liveAsset.flavorParamsId, (KalturaLiveParams) liveParams);
                     //   }
                        return (KalturaLiveAsset)liveAsset;
                    }
                }
            }

        } catch (KalturaApiException e) {
            logger.error("Failed to load asset params for live entry [" + liveEntry.id + "]:" + e.getMessage());
        }
        return null;
    }
    public static Map<String, Object> getServerConfig(){
        return serverConfiguration;
    }

    public KalturaLiveEntry appendRecording(int partnerId, String entryId, String assetId, KalturaEntryServerNodeType index, String filePath, double duration, boolean isLastChunk) throws Exception{
        KalturaDataCenterContentResource resource = getContentResource(filePath, partnerId);
        KalturaClient impersonateClient = impersonate(partnerId);
        KalturaServiceBase liveServiceInstance = impersonateClient.getLiveStreamService();
        Method method = liveServiceInstance.getClass().getMethod("appendRecording", String.class, String.class, KalturaEntryServerNodeType.class, KalturaDataCenterContentResource.class, double.class, boolean.class);
        KalturaLiveEntry updatedEntry = (KalturaLiveEntry)method.invoke(liveServiceInstance, entryId, assetId, index, resource, duration, isLastChunk);

        return updatedEntry;
    }
    protected KalturaDataCenterContentResource getContentResource (String filePath,  int partnerId) {
        if (!this.serverConfiguration.containsKey(KALTURA_WOWZA_SERVER_WORK_MODE) || (this.serverConfiguration.get(KALTURA_WOWZA_SERVER_WORK_MODE).equals(KALTURA_WOWZA_SERVER_WORK_MODE_KALTURA))) {
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
                logger.error("Content resource creation error: " + e.getMessage());
            }
        }

        return null;
    }



}