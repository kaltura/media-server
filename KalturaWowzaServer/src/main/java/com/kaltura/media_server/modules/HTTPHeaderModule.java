/**
 * Created by noam.arad on 7/19/2017.
 */
package com.kaltura.media_server.modules;

import com.kaltura.media_server.services.Utils;
import com.wowza.wms.http.*;
import com.wowza.wms.vhost.*;
import org.apache.log4j.Logger;


public class HTTPHeaderModule extends HTTProvider2Base {

    private static final Logger logger = Logger.getLogger(HTTPHeaderModule.class);

    private static String mediaServerhostname;

    @Override
    public void init() {
        super.init();
        try {
            mediaServerhostname = Utils.getMediaServerHostname(false);
        }
        catch (Exception e) {
            logger.warn("Could not get hostname ", e);
            mediaServerhostname = "default_hostname";
        }
    }

    public void onHTTPRequest(IVHost vhost, IHTTPRequest req, IHTTPResponse resp)
    {
        resp.setHeader("X-Me", mediaServerhostname);
        resp.removeHeader("Server");
    }
}
