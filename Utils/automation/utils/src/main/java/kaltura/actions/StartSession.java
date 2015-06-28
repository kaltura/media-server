package kaltura.actions;

import com.kaltura.client.KalturaApiException;
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

    public KalturaClient execute() throws KalturaApiException {

            KalturaConfiguration config = new KalturaConfiguration();
            config.setPartnerId(partnerId);
            config.setEndpoint(endPoint);
            config.setAdminSecret(adminSecret);

            KalturaClient client = new KalturaClient(config);
            String secret = adminSecret;
            String userId = null;
            KalturaSessionType type = KalturaSessionType.ADMIN;
            int expiry = Integer.MAX_VALUE;
            String privileges = null;
            Object result = client.getSessionService().start(secret, userId, type, partnerId, expiry, privileges);
            client.setSessionId(result.toString());
            return client;
    }
}
