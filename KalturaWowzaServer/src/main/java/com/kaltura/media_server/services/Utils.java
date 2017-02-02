package com.kaltura.media_server.services;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kaltura.client.types.KalturaLiveEntry;
import com.wowza.wms.client.IClient;
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
        queryString = queryString.replaceAll("/+$", "");
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

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Integer.parseInt(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    public static HashMap<String, String> getQueryMap(String query)
    {
        HashMap<String, String> map = new HashMap<String, String>();
        if (!query.equals("")){
            String[] params = query.split("&");
            for (String param : params)
            {
                String[] paramArr = param.split("=");
                if (paramArr.length!=2){
                    continue;
                }
                String name =paramArr[0];
                String value = paramArr[1];
                map.put(name, value);
            }
        }

        return map;
    }

    public static String getStreamName(IClient client) throws Exception {

        if (client == null || client.getPlayStreams().size() == 0) {
            throw new Exception("failed to get client Id. No streams found");
        }
        IMediaStream stream = (IMediaStream) client.getPlayStreams().get(0);

        return stream.getName();
    }

    public static KalturaEntryIdKey getEntryIdKeyFromClient(IClient client) throws Exception
    {
        KalturaEntryIdKey entryIdKey = null;
        try {
            WMSProperties properties = client.getProperties();
            synchronized (properties) {
                entryIdKey = (KalturaEntryIdKey)properties.getProperty(Constants.KALTURA_ENTRY_DATA_PERSISTENCY_KEY);
            }
        } catch (Exception e) {
            String error = "failed to get stream name from client. " + e;
            logger.error(error);
            throw e;
        }

        return entryIdKey;
    }

    public static KalturaEntryIdKey getEntryIdKeyFromStreamName(String streamName) throws Exception
    {
        String entryId = Utils.getEntryIdFromStreamName(streamName);

        return new KalturaEntryIdKey(entryId);
    }

    public static String getEntryIdFromClient(IClient client) throws Exception
    {
        String entryId = null;

        try {
            String streamName = getStreamName(client);
            entryId = getEntryIdFromStreamName(streamName);
        } catch (Exception e) {
            logger.warn("(" + client.getClientId() + ") no streams attached to client." + e);
        }

        if (entryId == null) {
            try {
                KalturaEntryIdKey entryIdKey = getEntryIdKeyFromClient(client);
                return entryIdKey.getEntryId();
            } catch (Exception e) {
                String error = "(" + client.getClientId() + ") failed to get property \"" + Constants.KALTURA_ENTRY_DATA_PERSISTENCY_KEY + " \" from client. " + e;
                logger.error(error);
                throw e;
            }
        }

        return entryId;

    }

}
