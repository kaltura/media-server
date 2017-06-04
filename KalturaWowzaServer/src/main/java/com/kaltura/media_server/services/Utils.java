package com.kaltura.media_server.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import com.kaltura.client.types.KalturaLiveEntry;
import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.client.IClient;
import com.wowza.wms.rtp.model.RTPSession;
import com.wowza.wms.stream.live.MediaStreamLive;
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
            if (mediaStream.getName().startsWith(entryId)) {
                properties = getConnectionProperties(mediaStream);
            }
            if (properties != null) {
                logger.debug("Find properties for entry [" + entryId + "]  for stream [" + streamName + "]");
                return properties;
            }
        }
        logger.error("Cannot find properties for entry [" + entryId + "]  for stream [" + streamName + "]");
        return null;

    }

    public static boolean isNumeric(String str) {
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

    public static HashMap<String, String> getQueryMap(String query) {
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


    public static String getEntryIdFromClient(IClient client) throws Exception
    {
       return getEntryIdFromIngestProperties(client.getProperties(), String.valueOf(client.getClientId()));
    }

    public static String getEntryIdFromRTPSession(RTPSession rtpSession) throws Exception
    {
        return getEntryIdFromIngestProperties(rtpSession.getProperties(), rtpSession.getSessionId());
    }

    public static Set<String> getEntriesFromApplication(IApplicationInstance appInstance) {
        Set<String>entriesSet = new HashSet<>();
        List<IMediaStream> streamList = appInstance.getStreams().getStreams();
        for (IMediaStream stream : streamList) {
            if (!( stream instanceof MediaStreamLive)){
                continue;
            }
            String entryId = getEntryIdFromStreamName(stream.getName());
            if (entryId != null) {
                entriesSet.add(entryId);
            }
        }

        return entriesSet;
    }

    private static String getEntryIdFromIngestProperties(WMSProperties properties, String sessionId) throws Exception {
        String entryId = null;
        try {
            synchronized (properties) {
                entryId = (String) properties.getProperty(Constants.KALTURA_LIVE_ENTRY_ID);
            }
        } catch (Exception e) {
            logger.warn("(" + sessionId + ") no streams attached to client." + e);
        }

        if (entryId == null) {
            logger.error("(" + sessionId + ") failed to get property \"" + Constants.KALTURA_LIVE_ENTRY_ID + " \" from client");
        }
        return entryId;
    }

    // 05-04-2017: this method is tricky. If stream is RTMP the client instace will be available for ingest stream
    // if stream is RTSP, client is not available for ingest stream but is some cases neither is RTPSteam object.
    // In that case reply will be "UNKNOWN_STREAM_TYPE" even though it is RTSP stream.
    // Waiting reply from wowza how to get correct type.
    public static KalturaStreamType getStreamType(IMediaStream stream, String streamName) {

        if (stream.isTranscodeResult()) {
            logger.warn("[ "+ streamName +" ] cannot verify stream type from transcode stream");
            return KalturaStreamType.UNKNOWN_STREAM_TYPE;
        }
        if (stream.getClient() != null) {
            return KalturaStreamType.RTMP;
        } else if (stream.getRTPStream() != null && stream.getRTPStream().getSession() !=null){
            return KalturaStreamType.RTSP;
        }

        return KalturaStreamType.UNKNOWN_STREAM_TYPE;

    }

    // 05-04-2017: this method is meant to get valid stream name whether live stream is RTMP or RTSP.
    // currently until wowza replies to our support ticket no valid stream name is available for RTSP.
    public static String getStreamName(IMediaStream stream) {
        return (stream.getName() != null &&  stream.getName().length() > 0) ? stream.getName() : "";
    }

    public static String getMediaServerHostname() throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec("hostname -f");
        BufferedReader input = new BufferedReader(new InputStreamReader(
                p.getInputStream()));
        p.waitFor();
        return input.readLine();
    }
}
