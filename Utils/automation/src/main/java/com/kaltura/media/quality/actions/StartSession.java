package com.kaltura.media.quality.actions;

import com.kaltura.client.KalturaClient;
import com.kaltura.client.KalturaConfiguration;
import com.kaltura.client.enums.KalturaSessionType;

/**
 * Created by asher.saban on 3/8/2015.
 */
public class StartSession {

    public int partnerId;
    public String endPoint;
    public String adminSecret;

    public StartSession(int partnerId, String endPoint, String adminSecret) {
        this.partnerId = partnerId;
        this.endPoint = endPoint;
        this.adminSecret = adminSecret;
    }

    public KalturaClient execute() throws Exception {

            KalturaConfiguration config = new KalturaConfiguration();
            config.setEndpoint(endPoint);

            KalturaClient client = new KalturaClient(config);
            String ks = client.generateSession(adminSecret, null, KalturaSessionType.ADMIN, partnerId);
            client.setSessionId(ks);
            return client;
    }
}
