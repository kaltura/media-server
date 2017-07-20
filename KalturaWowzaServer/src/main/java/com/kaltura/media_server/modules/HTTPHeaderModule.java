package com.kaltura.media_server.modules;
/**
 * Created by noam.arad on 7/19/2017.
 */

import com.kaltura.media_server.services.Utils;
import com.wowza.wms.http.*;
import com.wowza.wms.vhost.*;
import org.apache.log4j.Logger;


public class HTTPHeaderModule extends HTTProvider2Base {

    private static final Logger logger = Logger.getLogger(HTTPHeaderModule.class);

    public void onHTTPRequest(IVHost vhost, IHTTPRequest req, IHTTPResponse resp)
    {
        try {
            String hostname = Utils.getMediaServerHostname(false);
            resp.setHeader("X-Me", hostname);
        } catch (Exception e) {
            logger.warn("Could not get hostname ", e);
        }
        resp.removeHeader("Server");
    }
}
