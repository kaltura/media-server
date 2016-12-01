package com.kaltura.media_server.services;

/**
 * Created by ron.yadgar on 23/11/2016.
 */

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wowza.wms.amf.AMFDataObj;
import com.wowza.wms.application.*;
import com.wowza.wms.client.*;
import com.wowza.wms.logging.*;
import com.wowza.wms.server.*;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.vhost.*;
import com.wowza.wms.http.*;
import com.wowza.util.*;
import org.apache.log4j.Logger;
import com.kaltura.media_server.services.Constants;

public class EntriesDataProvider extends HTTProvider2Base
{
    private static final Logger logger = Logger.getLogger(HTTPConnectionCountsXML.class);
    private String httpSessionId;

    private void outputIOPerformanceInfo( HashMap<String,Object> hashMapInstance, IOPerformanceCounter ioPerformance)
    {
        hashMapInstance.put("total bitrate (actual)", ioPerformance.getMessagesInBytesRate() * 8 / 1000);
        hashMapInstance.put("MessagesOutBytesRate", ioPerformance.getMessagesOutBytesRate());

    }

    private void addStreamProperties(IMediaStream stream,  HashMap<String,Object> streamHash){

        int src = stream.getSrc();
        double videoBitrate = stream.getPublishBitrateVideo() / 1000;
        double audioBitrate = stream.getPublishBitrateAudio() / 1000;
        double totalBitrate =  videoBitrate + audioBitrate;
        double framerate = stream.getPublishFramerateVideo();
        streamHash.put("src", src);
        streamHash.put("video bitrate (publish)", videoBitrate);
        streamHash.put("audio bitrate (publish)", audioBitrate);
        streamHash.put("total bitrate (publish)", totalBitrate);
        streamHash.put("framerate (publish)", framerate);
        getMetaDataProperties(stream, streamHash);
        logger.debug(httpSessionId+"[" + stream.getName() + "] Add the following params: videoBitrate "+ videoBitrate +  ", audioBitrate " + audioBitrate + ", framerate "+ framerate);
    }

    private void getMetaDataProperties(IMediaStream stream,  HashMap<String,Object> streamHash){

        WMSProperties props = stream.getProperties();

        AMFDataObj obj;

        if (props == null){
            logger.warn(httpSessionId+"[" + stream.getName() + "] Can't find properties");
            return;
        }

        synchronized (props) {
            obj = (AMFDataObj) props.getProperty(Constants.AMFSETDATAFRAME);
        }
        if (obj == null) {
            logger.warn(httpSessionId+"[" + stream.getName() + "] Can't find meta data");
            return;
        }

        logger.info(httpSessionId+"[" + stream.getName() + " ] Find property AMFDataObj for stream");

        for (String streamParam : Constants.streamParams) {
            if (obj.containsKey(streamParam)) {
                if (streamParam.equals(Constants.ONMETADATA_VIDEOCODECIDSTR) ||  streamParam.equals(Constants.ONMETADATA_AUDIOCODECIDSTR)){
                    streamHash.put(streamParam, obj.getString(streamParam));
                }
                else{
                    streamHash.put(streamParam, obj.getDouble(streamParam));
                }

            }
        }
    }

    private void addClientProperties(Client client, HashMap<String,Object> hashMapInstance, String entryId){
        if (client == null){
            return;
        }
        WMSProperties clientProps = client.getProperties();
        String rtmpUrl, encoder,IP;
        rtmpUrl =  clientProps.getPropertyStr(Constants.CLIENT_PROPERTY_CONNECT_URL);
        encoder = clientProps.getPropertyStr(Constants.CLIENT_PROPERTY_ENCODER);
        IP = client.getIp();
        int port = client.getServerHostPort().getPort();
        hashMapInstance.put("rtmp url" , rtmpUrl);
        hashMapInstance.put("encoder" , encoder);
        hashMapInstance.put("IP" , IP);
        hashMapInstance.put("port" , port);

        logger.debug(httpSessionId + "[" + entryId + "] Add the following params: rtmpUrl "+ rtmpUrl +  ", encoder " + encoder + ", IP " + IP + ", port " + port);

    }
    @SuppressWarnings("unchecked")
    private void addAllStreamsProperties(List<IMediaStream> streamList, HashMap<String,Object> entryHash){
        for (IMediaStream stream : streamList) {
            HashMap<String, Object> entryHashInstance, inputEntryHashInstance, outputEntryHashInstance ;
            String streamName = stream.getName();

            Matcher matcher = Utils.getStreamNameMatches(streamName);
            if (matcher == null) {
                logger.warn(httpSessionId+"Unknown published stream [" + streamName + "]");
                continue;
            }
            String entryId =  matcher.group(1);
            String flavor = matcher.group(2);

            if (!entryHash.containsKey(entryId)){
                entryHashInstance = new HashMap<>();
                inputEntryHashInstance  = new HashMap<>();
                outputEntryHashInstance  = new HashMap<>();
                entryHashInstance.put("input", inputEntryHashInstance);
                entryHashInstance.put("output", outputEntryHashInstance);
                entryHash.put(entryId, entryHashInstance);
            }
            else{

                entryHashInstance = (HashMap<String, Object>) entryHash.get(entryId);
                inputEntryHashInstance= (HashMap<String, Object>) entryHashInstance.get("input");
                outputEntryHashInstance = (HashMap<String, Object>) entryHashInstance.get("output");

            }

            HashMap<String, Object> streamHash = new HashMap<>();
            addStreamProperties(stream, streamHash);
            IOPerformanceCounter perf = stream.getMediaIOPerformance();
            outputIOPerformanceInfo(streamHash, perf);

            if ( stream.isTranscodeResult()){
                outputEntryHashInstance.put(flavor, streamHash);
            }
            else {
                inputEntryHashInstance.put(flavor, streamHash);
                Client client = (Client) stream.getClient();
                addClientProperties(client, entryHashInstance, entryId);

            }
        }
    }

    private void writeAnswer(IHTTPResponse resp, HashMap<String, Object>  entryData){
        try {
            resp.setHeader("Content-Type", "application/json");
            ObjectMapper mapper = new ObjectMapper();
            OutputStream out = resp.getOutputStream();
            String data = mapper.writeValueAsString(entryData);
            byte[] outBytes = data.getBytes();
            out.write(outBytes);
        }
        catch (Exception e)
        {
            logger.error(httpSessionId+"Failed to write answer: "+e.toString());
        }
    }
    @SuppressWarnings("unchecked")
    public void onHTTPRequest(IVHost inVhost, IHTTPRequest req, IHTTPResponse resp)
    {
        httpSessionId = "[" + System.currentTimeMillis() / 1000L + "]";


        HashMap<String, Object> entryData = new HashMap<>();
        try
        {
            List<String> vhostNames = VHostSingleton.getVHostNames();
            logger.debug(httpSessionId + "Getting vhostNames" + vhostNames);
            Iterator<String> iter = vhostNames.iterator();
            while (iter.hasNext())
            {
                String vhostName = iter.next();
                IVHost vhost = (IVHost)VHostSingleton.getInstance(vhostName);
                if (vhost == null) {
                    continue;
                }

                List<String> appNames =  vhost.getApplicationNames();
                logger.debug(httpSessionId + "Getting appNames" + appNames);
                Iterator<String> appNameIterator = appNames.iterator();

                while (appNameIterator.hasNext()) {
                    String applicationName = appNameIterator.next();
                    IApplication application = vhost.getApplication(applicationName);
                    if (application == null)
                        continue;

                    List<String> appInstances = application.getAppInstanceNames();
                    logger.debug(httpSessionId + "Getting appInstances" + appInstances);
                    Iterator<String> iterAppInstances = appInstances.iterator();

                    while (iterAppInstances.hasNext()) {
                        String appInstanceName = iterAppInstances.next();
                        IApplicationInstance appInstance = application.getAppInstance(appInstanceName);
                        if (appInstance == null)
                            continue;

                        List<IMediaStream> streamList = appInstance.getStreams().getStreams();
                        addAllStreamsProperties(streamList, entryData);
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error(httpSessionId+"HTTPServerInfoXML.onHTTPRequest: "+e.toString());
        }
        writeAnswer(resp, entryData);
    }
}