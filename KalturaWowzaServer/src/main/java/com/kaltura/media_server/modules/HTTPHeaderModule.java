package com.kaltura.media_server.modules;
/**
 * Created by noam.arad on 7/19/2017.
 */

import com.kaltura.media_server.services.Constants;
import com.wowza.wms.http.*;
import com.wowza.wms.vhost.*;
import org.apache.log4j.Logger;

import java.util.Map;


public class HTTPHeaderModule extends HTTProvider2Base {

    private static final Logger logger = Logger.getLogger(HTTPHeaderModule.class);
    private static Map<String, Object> vhostConfig;

    public void onHTTPRequest(IVHost vhost, IHTTPRequest req, IHTTPResponse resp)
    {
        vhostConfig = vhost.getProperties();
        resp.setHeader("X-ME", (String) vhostConfig.get(Constants.KALTURA_HOSTNAME));
        resp.setHeader("Server", "noam");
        logger.fatal("@@NA noam was here in onHTTPRequest");
    }
}
