package com.kaltura.media.server.wowza;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.client.KalturaConfiguration;
import com.kaltura.client.KalturaMultiResponse;
import com.kaltura.client.enums.KalturaSessionType;
import com.kaltura.client.types.*;
import com.kaltura.media.server.KalturaServerException;
import com.kaltura.media.server.KalturaUncaughtExceptionHnadler;
import com.kaltura.client.enums.KalturaEntryServerNodeType;
import org.apache.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ron.yadgar on 15/05/2016.
 */

public class KalturaAPI {

    protected final static String KALTURA_SERVER_URL = "KalturaServerURL";
    protected final static String KALTURA_SERVER_ADMIN_SECRET = "KalturaServerAdminSecret";
    protected final static String KALTURA_SERVER_PARTNER_ID = "KalturaPartnerId";
    protected final static String KALTURA_SERVER_TIMEOUT = "KalturaServerTimeout";
    public static int MEDIA_SERVER_PARTNER_ID = -5;

    // use the same session key for all Wowza sessions, so all (within a DC) will be directed to the same sphinx to prevent synchronization problems
    private final static String KALTURA_PERMANENT_SESSION_KEY = "kalturaWowzaPermanentSessionKey";

    protected static Logger logger = Logger.getLogger(KalturaAPI.class);
    protected static Map<String, Object> config;
    protected static KalturaClient client;
    protected static String hostname;
    private   KalturaConfiguration clientConfig;

    public KalturaAPI(Map<String, Object> config)  throws KalturaServerException {  //TODO CHECK THIS CODE
        logger.info("Initializing KalturaUncaughtException handler");
        this.config=config;
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

        int partnerId = config.containsKey(KALTURA_SERVER_PARTNER_ID) ? (int) config.get(KALTURA_SERVER_PARTNER_ID) : MEDIA_SERVER_PARTNER_ID;


        if (!config.containsKey(KALTURA_SERVER_URL))
            throw new KalturaServerException("Missing configuration [" + KALTURA_SERVER_URL + "]");

        if (!config.containsKey(KALTURA_SERVER_ADMIN_SECRET))
            throw new KalturaServerException("Missing configuration [" + KALTURA_SERVER_ADMIN_SECRET + "]");

        clientConfig.setEndpoint((String) config.get(KALTURA_SERVER_URL));
        logger.debug("Initializing Kaltura client, URL: " + clientConfig.getEndpoint());

        if (config.containsKey(KALTURA_SERVER_TIMEOUT))
            clientConfig.setTimeout(Integer.parseInt((String) config.get(KALTURA_SERVER_TIMEOUT)) * 1000);

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
        int partnerId = config.containsKey(KALTURA_SERVER_PARTNER_ID) ? (int) config.get(KALTURA_SERVER_PARTNER_ID) : MEDIA_SERVER_PARTNER_ID;
        String adminSecretForSigning = (String) config.get(KALTURA_SERVER_ADMIN_SECRET);
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
        return config;
    }



}