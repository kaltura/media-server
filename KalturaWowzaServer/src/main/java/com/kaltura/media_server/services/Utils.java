package com.kaltura.media_server.services;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kaltura.client.types.KalturaLiveEntry;
import org.apache.log4j.Logger;
import com.wowza.wms.stream.*;
import com.wowza.wms.application.WMSProperties;

/**
 * Created by ron.yadgar on 26/05/2016.
 */
public class Utils {


    private static Logger logger = Logger.getLogger(Utils.class);

    public static HashMap<String, String> getRtmpUrlParameters(String rtmpUrl, String queryString){


        final String NewPattern= ":\\/\\/([01]_[\\d\\w]{8}).([pb])\\.(?:[^.]*\\.)*kpublish\\.kaltura\\.com";
        Matcher matcher;

        logger.info("Query-String [" + queryString + "]");

        //parse the Query-String into Hash map.
        String[] queryParams = queryString.split("&");
        HashMap<String, String> requestParams = new HashMap<String, String>();
        String[] queryParamsParts;
        for (int i = 0; i < queryParams.length; ++i) {
            queryParamsParts = queryParams[i].split("=", 2);
            if (queryParamsParts.length == 2)
                requestParams.put(queryParamsParts[0], queryParamsParts[1]);
        }

        //Check if the Url is the new pattern, if so, parse the entryid and server index
        matcher = getMatches(rtmpUrl, NewPattern);

        if (matcher != null && matcher.groupCount() ==2) {

            requestParams.put(Constants.REQUEST_PROPERTY_ENTRY_ID, matcher.group(1));

            //check if the parter id include in query string ...
            if (!requestParams.containsKey(Constants.REQUEST_PROPERTY_PARTNER_ID)){
                requestParams.put(Constants.REQUEST_PROPERTY_PARTNER_ID, "-5");
            }

            if (!requestParams.containsKey(Constants.REQUEST_PROPERTY_SERVER_INDEX)){
                String i = matcher.group(2).equals("p") ? "0" : "1";
                requestParams.put(Constants.REQUEST_PROPERTY_SERVER_INDEX, i);
            }
        }

        return requestParams;


    }


    private static Matcher getMatches(String streamName, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(streamName);
        if (!matcher.find()) {
            return null;
        }

        return matcher;
    }

    public static String getEntryIdFromStreamName(String streamName) {
        Matcher matcher = getStreamNameMatches(streamName);
        if (matcher == null) {
            return null;
        }

        return matcher.group(1);
    }

    public static Matcher getStreamNameMatches(String streamName) {
        return getMatches(streamName, "^([01]_[\\d\\w]{8})_(.+)$");
    }
    private static WMSProperties getConnectionProperties(IMediaStream stream) {

        WMSProperties properties = null;

        if (stream.getClient() != null) {
            properties = stream.getClient().getProperties();
        } else if (stream.getRTPStream() != null && stream.getRTPStream().getSession() != null) {
            properties = stream.getRTPStream().getSession().getProperties();
        } else {
            return null;
        }

        return properties;
    }


    public static WMSProperties getEntryProperties(IMediaStream stream) {

        WMSProperties properties = null;
        String streamName = stream.getName();
        String entryId = Utils.getEntryIdFromStreamName (streamName);
        properties = getConnectionProperties(stream);

        if (properties != null) {
            logger.debug("Find properties for entry [" + entryId + "]  for stream [" + streamName + "]");
            return properties;
        }
        // For loop over all published mediaStream (source and transcoded) in order to find the corresponding source stream
        for (IMediaStream mediaStream : stream.getStreams().getStreams()) {
            properties = getConnectionProperties(mediaStream);

            if (properties != null && mediaStream.getName().startsWith(entryId)) {
                logger.debug("Find properties for entry [" + entryId + "]  for stream [" + streamName + "]");
                return properties;
            }
        }
        logger.error("Cannot find properties for entry [" + entryId + "]  for stream [" + streamName + "]");
        return null;

    }


    public static KalturaLiveEntry getLiveEntry(WMSProperties properties) throws Exception{

        if (properties==null){
            throw new Exception("Failed to retrieve property");
        }

        KalturaLiveEntry liveEntry= (KalturaLiveEntry) properties.getProperty(Constants.CLIENT_PROPERTY_KALTURA_LIVE_ENTRY);

        if (liveEntry == null){
            throw new Exception("Failed to retrieve LiveEntry property ");

        }
        return liveEntry;
    }
}
