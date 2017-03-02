package com.kaltura.media_server.services;

/**
 * Created by ron.yadgar on 23/11/2016.
 */

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wowza.wms.amf.AMFData;
import com.wowza.wms.amf.AMFDataObj;
import com.wowza.wms.application.*;
import com.wowza.wms.client.*;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.vhost.*;
import com.wowza.wms.http.*;
import com.wowza.util.*;
import org.apache.log4j.Logger;
import com.wowza.util.FLVUtils;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import com.wowza.wms.stream.live.MediaStreamLive;
import java.net.InetAddress;
import com.kaltura.media_server.modules.LiveStreamSettingsModule.PacketListener;

public class DiagnosticsProvider extends HTTProvider2Base
{

    public interface CommandProvider {
        public abstract void execute (HashMap<String,Object> data, HashMap<String,String > quaryString, IApplicationInstance appInstance );
    }

    class ErrorProvider implements CommandProvider {

        public void execute(HashMap<String,Object> data, HashMap<String,String > quaryString, IApplicationInstance appInstance) {
                synchronized(DiagnosticsProvider.errorDiagnostics){
                    Iterator<HashMap<String,String> > myIterator = DiagnosticsProvider.errorDiagnostics.iterator();
                    while(myIterator.hasNext()){
                        HashMap<String,String> errorSession = myIterator.next();
                        String time = errorSession.get("Time");
                        data.put(time, errorSession);
                    }
                }
            }
    }

    class InfoProvider implements CommandProvider {


        public String getJarName(){
            return  new java.io.File(InfoProvider.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
        }

        public void execute(HashMap<String,Object> data, HashMap<String,String > quaryString, IApplicationInstance appInstance) {

            String jarName = getJarName();
            String version = this.getClass().getPackage().getImplementationVersion();
            String dateStarted = appInstance.getDateStarted();
            String timeRunning = Double.toString(appInstance.getTimeRunningSeconds());
            String hostName;
            try{
                hostName = InetAddress.getLocalHost().getHostName();
            }
            catch (java.net.UnknownHostException e){
                hostName = "UNKNOWN";
            }
            data.put("jarName", jarName);
            data.put("version", version);
            data.put("dateStarted", dateStarted);
            data.put("timeRunning", timeRunning);
            data.put("hostName", hostName);
        }
    }

    class killConnectionAction implements CommandProvider {

        public void execute(HashMap<String,Object> data, HashMap<String,String > quaryString, IApplicationInstance appInstance){
            String clientId = quaryString.get("clientId");
            if (clientId ==null){
                String msg = "Can't find clientId on query string";
                data.put("Error", msg);
                logger.error(msg);
                return;
            }
            int clientIdInt = Integer.parseInt(clientId);
            IClient client  = appInstance.getClientById(clientIdInt);
            if (client ==null){
                String msg = "Can't find client for clientId "+ clientId;
                data.put("Error", msg);
                logger.error(msg);
                return;
            }
            client.setShutdownClient(true);
            data.put("Succeed", true);
        }
    }

    @SuppressWarnings("unchecked")
    class EntriesProvider implements CommandProvider {

        public void execute(HashMap<String,Object> data, HashMap<String,String > quaryString, IApplicationInstance appInstance) {
            List<IMediaStream> streamList = appInstance.getStreams().getStreams();
            for (IMediaStream stream : streamList) {
                if (!( stream instanceof MediaStreamLive)){
                    continue;
                }
                HashMap<String, Object> entryHashInstance;
                HashMap<String, Object > inputEntryHashInstance, outputEntryHashInstance;
                String streamName = stream.getName();
                try {
                    Matcher matcher = Utils.getStreamNameMatches(streamName);
                    if (matcher == null) {
                        logger.warn(httpSessionId + "Unknown published stream [" + streamName + "]");
                        continue;
                    }
                    String entryId = matcher.group(1);
                    String flavor = matcher.group(2);

                    if (!data.containsKey(entryId)) {
                        entryHashInstance = new HashMap<String, Object>();
                        inputEntryHashInstance = new HashMap<String, Object>();
                        outputEntryHashInstance = new HashMap<String, Object>();
                        entryHashInstance.put("inputs", inputEntryHashInstance);
                        entryHashInstance.put("outputs", outputEntryHashInstance);
                        entryHashInstance.put("currentTime",System.currentTimeMillis());
                        data.put(entryId, entryHashInstance);
                    } else {
                        entryHashInstance = (HashMap<String, Object> ) data.get(entryId);
                        inputEntryHashInstance = (HashMap<String, Object>) entryHashInstance.get("inputs");
                        outputEntryHashInstance = (HashMap<String, Object>) entryHashInstance.get("outputs");

                    }

                    HashMap<String, Object> streamHash = new HashMap<String, Object>();
                    addStreamProperties(stream, streamHash);
                    IOPerformanceCounter perf = stream.getMediaIOPerformance();
                    outputIOPerformanceInfo(streamHash, perf);

                    if (stream.isTranscodeResult()) {
                        outputEntryHashInstance.put(flavor, streamHash);
                    } else {
                        Client client = (Client) stream.getClient();
                        addClientProperties(client, streamHash, entryId);
                        inputEntryHashInstance.put(flavor, streamHash);

                    }
                }
                catch (Exception e){
                    logger.error(httpSessionId+"[" + stream.getName() + "] Error while try to add stream to JSON: " + e.toString());
                }
            }
        }
    }


    private String requestRegex = Constants.HTTP_PROVIDER_KEY + "/(.+)";
    private static final String DIAGNOSTICS_ERROR = "errors";
    private static final String DIAGNOSTICS_LIVE_ENTRIES = "entries";
    private static final String DIAGNOSTICS_LIVE_INFO = "info";
    private static final String DIAGNOSTICS_KILL_CONNECTION = "killConnection";
    private static List <HashMap<String,String> >errorDiagnostics= Collections.synchronizedList(new ArrayList <HashMap<String,String> >());
    private static final Logger logger = Logger.getLogger(HTTPConnectionCountsXML.class);
    private String httpSessionId;
    private final Map<String, CommandProvider> CommandHash;
    {
        CommandHash = new HashMap<String, CommandProvider>();
        CommandHash.put(DIAGNOSTICS_ERROR, new ErrorProvider());
        CommandHash.put(DIAGNOSTICS_LIVE_ENTRIES, new EntriesProvider());
        CommandHash.put(DIAGNOSTICS_LIVE_INFO, new InfoProvider());
        CommandHash.put(DIAGNOSTICS_KILL_CONNECTION, new killConnectionAction());
    }

    private void outputIOPerformanceInfo( HashMap<String,Object> hashMapInstance, IOPerformanceCounter ioPerformance)
    {
        HashMap<String,Object> IOPerformanceInfoHash =  new HashMap<String,Object>();
        IOPerformanceInfoHash.put("bitrate", ioPerformance.getMessagesInBytesRate() * 8 / 1000);
        hashMapInstance.put("IOPerformance", IOPerformanceInfoHash);
    }
    private void writeError(HashMap<String,Object> entryHash, String arg){

        entryHash.put("Error", "Got argument: "+ arg + ", valid arguments: "  +CommandHash.keySet());
    }


    private void addStreamProperties(IMediaStream stream,  HashMap<String,Object> streamHash){

        HashMap<String,Object> EncodersHash =  new HashMap<String,Object>();
        double videoBitrate = stream.getPublishBitrateVideo() / 1000;
        double audioBitrate = stream.getPublishBitrateAudio() / 1000;
        double framerate = stream.getPublishFramerateVideo();
        String audioCodec = FLVUtils.audioCodecToString(stream.getPublishAudioCodecId());
        String videoCodec = FLVUtils.videoCodecToString(stream.getPublishVideoCodecId());
        EncodersHash.put("videoCodec", videoCodec);
        EncodersHash.put("audioCodec", audioCodec);
        EncodersHash.put("videoBitrate", videoBitrate);
        EncodersHash.put("audioBitrate", audioBitrate);
        EncodersHash.put("frameRate", framerate);
        getMetaDataProperties(stream, EncodersHash);
        streamHash.put("Encoder", EncodersHash);
        //logger.debug(httpSessionId+"[" + stream.getName() + "] Add the following params: videoBitrate "+ videoBitrate +  ", audioBitrate " + audioBitrate + ", framerate "+ framerate);
    }

    public static void  addRejectedStream(String message, IClient client){

        WMSProperties properties = client.getProperties();
        String rtmpUrl;
        synchronized(properties) {
            rtmpUrl = properties.getPropertyStr(Constants.CLIENT_PROPERTY_CONNECT_URL);
        }
        String IP = client.getIp();

        HashMap<String,String> rejcetedStream =  new HashMap<String,String>();
        rejcetedStream.put("rtmpUrl", rtmpUrl);
        rejcetedStream.put("message", message);
        rejcetedStream.put("IP", IP);
        String timeStamp = Long.toString(System.currentTimeMillis());
        rejcetedStream.put("Time" , timeStamp);
        if (errorDiagnostics.size() >= Constants.KALTURA_REJECTED_STEAMS_SIZE){
            errorDiagnostics.remove(0);
        }
        errorDiagnostics.add(rejcetedStream);
    }


    private void getMetaDataProperties(IMediaStream stream,  HashMap<String,Object> streamHash){

        WMSProperties props = stream.getProperties();

        AMFDataObj obj;
        PacketListener listener;
        if (props == null){
            logger.warn(httpSessionId+"[" + stream.getName() + "] Can't find properties");
            return;
        }

        synchronized (props) {
            obj = (AMFDataObj) props.getProperty(Constants.AMFSETDATAFRAME);
            listener = (PacketListener) props.getProperty(Constants.STREAM_ACTION_LISTENER_PROPERTY);
        }
        if (obj == null) {
            logger.warn(httpSessionId+"[" + stream.getName() + "] Can't find meta data");
            return;
        }

        if (listener != null){
            long[][] syncPTSData = listener.getSyncPTSData();
            streamHash.put("syncPTSData", syncPTSData);
        }
        for (int i = 0 ;  i < obj.size() ;  i++){
            String key = obj.getKey(i);
            try{
                if ( obj.get(key).getType() == AMFData.DATA_TYPE_ARRAY || obj.get(key).getType() == AMFData.DATA_TYPE_OBJECT){
                    continue;
                }
                if (! key.equals(Constants.ONMETADATA_VIDEOCODECIDSTR) &&  ! key.equals(Constants.ONMETADATA_AUDIOCODECIDSTR)){
                    streamHash.put(key, obj.getString(key));
                }
            }
            catch (Exception e){
                logger.error(httpSessionId+"[" + stream.getName() + " ] Fail to add property " + key + ": " + e.getMessage());
            }

        }
    }

    private void addClientProperties(Client client, HashMap<String, Object> hashMapInstance, String entryId){
        if (client == null){
            logger.warn(httpSessionId + "[" + entryId + "] client is null");
            return;
        }
        WMSProperties clientProps = client.getProperties();
        if (clientProps == null){
            logger.warn(httpSessionId + "[" + entryId + "] Can't get properties");
            return;
        }

        String rtmpUrl, encoder,IP;
        rtmpUrl =  clientProps.getPropertyStr(Constants.CLIENT_PROPERTY_CONNECT_URL);
        encoder = clientProps.getPropertyStr(Constants.CLIENT_PROPERTY_ENCODER);
        IP = client.getIp();
        long pingRoundTripTime = client.getPingRoundTripTime();
        double timeRunningSeconds = client.getTimeRunningSeconds();
        HashMap<String,Object> ClientPropertiesHash =  new HashMap<String,Object>();
        ClientPropertiesHash.put("pingRoundTripTime" , pingRoundTripTime);
        ClientPropertiesHash.put("timeRunningSeconds" , timeRunningSeconds);
        ClientPropertiesHash.put("rtmpUrl" , rtmpUrl);
        ClientPropertiesHash.put("encoder" , encoder);
        ClientPropertiesHash.put("IP" , IP);
        ClientPropertiesHash.put("clientId" , client.getClientId());
        hashMapInstance.put("clientProperties", ClientPropertiesHash);

     //   logger.debug(httpSessionId + "[" + entryId + "] Add the following params: rtmpUrl "+ rtmpUrl +  ", encoder " + encoder + ", IP " + IP );

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

    private static String getArgument(String requestURL, String regex) throws Exception{
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(requestURL);
        if (!matcher.find()) {
            return null;
        }

        return matcher.group(1);
    }

    @SuppressWarnings("unchecked")
    public void onHTTPRequest(IVHost inVhost, IHTTPRequest req, IHTTPResponse resp)
    {
        httpSessionId = "[" + System.currentTimeMillis() / 1000L + "]";
        String queryStr = req.getQueryString();
        HashMap<String, String> queryStrMap = Utils.getQueryMap(queryStr);
        HashMap<String, Object> data = new HashMap<String,Object>();
        try {
            String requestURL = req.getRequestURL();
            String arg = getArgument(requestURL, requestRegex);
            if (arg == null){
                logger.error("Wrong requestURL, should be " + requestRegex + ", got "+ requestURL);
                return;
            }
            List<String> vhostNames = VHostSingleton.getVHostNames();
            //logger.debug(httpSessionId + "Getting vhostNames" + vhostNames);
            Iterator<String> iter = vhostNames.iterator();
            while (iter.hasNext()) {
                String vhostName = iter.next();
                IVHost vhost = (IVHost) VHostSingleton.getInstance(vhostName);
                if (vhost == null) {
                    continue;
                }

                List<String> appNames = vhost.getApplicationNames();
                //logger.debug(httpSessionId + "Getting appNames" + appNames);
                Iterator<String> appNameIterator = appNames.iterator();

                while (appNameIterator.hasNext()) {
                    String applicationName = appNameIterator.next();
                    IApplication application = vhost.getApplication(applicationName);
                    if (application == null)
                        continue;

                    List<String> appInstances = application.getAppInstanceNames();
                    //logger.debug(httpSessionId + "Getting appInstances" + appInstances);
                    Iterator<String> iterAppInstances = appInstances.iterator();

                    while (iterAppInstances.hasNext()) {
                        String appInstanceName = iterAppInstances.next();
                        IApplicationInstance appInstance = application.getAppInstance(appInstanceName);
                        if (appInstance == null)
                            continue;
                        if ( CommandHash.containsKey(arg)){
                             CommandHash.get(arg).execute(data, queryStrMap, appInstance);
                        }
                        else{
                            writeError(data, arg);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error(httpSessionId+"HTTPServerInfoXML.onHTTPRequest: "+e.toString());
        }
        writeAnswer(resp, data);
    }
}