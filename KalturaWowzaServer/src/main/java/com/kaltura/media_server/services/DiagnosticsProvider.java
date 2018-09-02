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
import com.wowza.wms.rtp.model.RTPSession;
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
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

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

        private HashMap<String,Object> gpuData = null;
        final private Timer timer = new java.util.Timer();

        public InfoProvider() {
            timer.scheduleAtFixedRate(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            try {
                                if (Utils.isGpuAvailable())
                                    gpuData = Utils.getGpuUsage();
                            }catch (Exception e) {
                                logger.warn("Could not get GPU usage ", e);
                                gpuData = null;
                            }
                        }
                    },0, 10000
            );
        }

        public String getJarName(){
            return  new java.io.File(InfoProvider.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
        }

        public double getCpuUsage() {
            OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
            return operatingSystemMXBean.getSystemLoadAverage() / operatingSystemMXBean.getAvailableProcessors();
        }

        public void execute(HashMap<String,Object> data, HashMap<String,String > quaryString, IApplicationInstance appInstance) {

            String jarName = getJarName();
            double cpuUsage = getCpuUsage();
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
            data.put("cpuUsage", cpuUsage);

            if (Utils.isGpuAvailable())
                data.put("gpu", gpuData);
            data.putAll(getStreamsData(appInstance));
        }

        public HashMap<String,Object> getStreamsData(IApplicationInstance appInstance) {
            int in = 0, out = 0;
            Set<String>entriesSet = new HashSet<>();
            for (IMediaStream stream : appInstance.getStreams().getStreams()) {
                if (!( stream instanceof MediaStreamLive))
                    continue;
                String entryId = Utils.getEntryIdFromStreamName(stream.getName());
                if (entryId != null)
                    entriesSet.add(entryId);

                if (stream.isTranscodeResult())
                    out++;
                else
                    in++;
            }

            HashMap<String,Object> streamsData = new HashMap<String,Object>();
            streamsData.put("inputStreamsCount", in);
            streamsData.put("outputStreamsCount", out);
            streamsData.put("entriesCount", entriesSet.size());
            return streamsData;
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
                        if (client != null){
                            addRTMPProperties(client, streamHash, entryId);
                        }
                        else if (stream.getRTPStream() != null && stream.getRTPStream().getSession() !=null){
                            addRTSPProperties(stream.getRTPStream().getSession(), streamHash, entryId);
                        }
                        else logger.warn("Cant find client or RTPSession obj");

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

    public static void addRejectedStream(IMediaStream stream, String error){

        if (stream.getClient() != null){
            DiagnosticsProvider.addRejectedRTMPStream(stream.getClient(), error);
        }
        else
        if (stream.getRTPStream() != null && stream.getRTPStream().getSession() !=null){
            DiagnosticsProvider.addRejectedRTSPStream(stream.getRTPStream().getSession(), error);
        }
    }

    public static void  addRejectedRTMPStream(IClient client, String error){
        String msg = "Stream " + client.getClientId() + " " + error;
        logger.error(msg);

        WMSProperties properties = client.getProperties();
        String ConnectionUrl;
        synchronized(properties) {
            ConnectionUrl = properties.getPropertyStr(Constants.CLIENT_PROPERTY_CONNECT_URL);
        }
        String IP = client.getIp();
        addRejectedStream(msg, ConnectionUrl, IP);
    }

    public static void  addRejectedRTSPStream(RTPSession rtpSession, String error){
        String msg = "Stream " + rtpSession.getSessionId() + " " + error;
        logger.error(msg);

        String IP = rtpSession.getIp();
        String  RTSPUrl= rtpSession.getUri() + rtpSession.getQueryStr();
        addRejectedStream(msg, RTSPUrl, IP);
    }

    private static void  addRejectedStream(String message, String ConnectionUrl,  String IP){

        HashMap<String,String> rejcetedStream =  new HashMap<String,String>();
        rejcetedStream.put("ConnectionUrl", ConnectionUrl);
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

    private void addRTMPProperties(Client client, HashMap<String, Object> hashMapInstance, String entryId){

        WMSProperties clientProps = client.getProperties();
        if (clientProps == null){
            logger.warn(httpSessionId + "[" + entryId + "] Can't get properties");
            return;
        }

        String ConnectionUrl, encoder,IP;
        ConnectionUrl =  clientProps.getPropertyStr(Constants.CLIENT_PROPERTY_CONNECT_URL);
        encoder = clientProps.getPropertyStr(Constants.CLIENT_PROPERTY_ENCODER);
        IP = client.getIp();
        long pingRoundTripTime = client.getPingRoundTripTime();
        double timeRunningSeconds = client.getTimeRunningSeconds();
        HashMap<String,Object> propertiesHash =  new HashMap<String,Object>();
        propertiesHash.put("pingRoundTripTime" , pingRoundTripTime);
        propertiesHash.put("timeRunningSeconds" , timeRunningSeconds);
        propertiesHash.put("ConectionUrl" , ConnectionUrl);
        propertiesHash.put("encoder" , encoder);
        propertiesHash.put("IP" , IP);
        propertiesHash.put("Id" , client.getClientId());
        hashMapInstance.put("Properties", propertiesHash);

     //   logger.debug(httpSessionId + "[" + entryId + "] Add the following params: rtmpUrl "+ rtmpUrl +  ", encoder " + encoder + ", IP " + IP );

    }

    private void addRTSPProperties(RTPSession rtpSession,  HashMap<String, Object> hashMapInstance, String entryId){

        // Lilach Todo : remove this function and update call to addRTMPProperties
        // need verify why did Ron add this....

        HashMap<String,Object> propertiesHash =  new HashMap<String,Object>();
        String RTSPUrl, encoder,IP, sessionId;
        IP = rtpSession.getIp();
        sessionId = rtpSession.getSessionId();
        RTSPUrl = rtpSession.getUri() + rtpSession.getQueryStr();
        WMSProperties clientProps = rtpSession.getProperties();
        encoder = clientProps.getPropertyStr(Constants.CLIENT_PROPERTY_ENCODER);
        double timeRunningSeconds = rtpSession.getTimeRunningSeconds();
        propertiesHash.put("timeRunningSeconds" , timeRunningSeconds);
        propertiesHash.put("ConnectionUrl", RTSPUrl);
        propertiesHash.put("encoder" , encoder);
        propertiesHash.put("IP", IP);
        propertiesHash.put("Id", sessionId);
        hashMapInstance.put("Properties", propertiesHash);
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