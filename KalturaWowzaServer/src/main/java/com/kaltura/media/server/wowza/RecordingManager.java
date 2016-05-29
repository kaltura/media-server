package com.kaltura.media.server.wowza;


import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;


import com.kaltura.client.KalturaApiException;
import com.kaltura.client.enums.KalturaRecordStatus;
import com.kaltura.client.types.*;
import com.kaltura.media.server.wowza.Utils;
import org.apache.log4j.Logger;
import com.kaltura.client.enums.KalturaEntryServerNodeType;
import com.wowza.wms.stream.publish.Publisher;
import com.wowza.wms.stream.publish.Stream;
import com.wowza.wms.transcoder.model.LiveStreamTranscoder;
import com.wowza.wms.transcoder.model.LiveStreamTranscoderActionNotifyBase;
import com.wowza.wms.stream.livetranscoder.ILiveStreamTranscoder;

import com.wowza.wms.stream.livetranscoder.ILiveStreamTranscoderControl;
import com.wowza.wms.stream.livetranscoder.LiveStreamTranscoderNotifyBase;
import com.wowza.wms.livestreamrecord.model.ILiveStreamRecord;
import com.wowza.wms.livestreamrecord.model.ILiveStreamRecordNotify;
import com.wowza.wms.livestreamrecord.model.LiveStreamRecorderMP4;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.module.ModuleBase;
import com.wowza.wms.application.*;
import com.wowza.wms.stream.*;
import com.wowza.wms.stream.livetranscoder.*;
import com.wowza.wms.module.ModuleBase;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.client.IClient;

import com.kaltura.media.server.wowza.listeners.ServerListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class RecordingManager  extends ModuleBase {

    private final static String KALTURA_RECORDED_FILE_GROUP = "g";
    private final static String DEFAULT_RECORDED_FILE_GROUP = "kaltura";
    private final static String UPLOAD_XML_SAVE_PATH = "uploadXMLSavePath";
    private final static int DEFAULT_RECORDED_SEGMENT_DURATION = 900000; //~15 minutes
    private final static String DEFAULT_RECORDED_SEGMENT_DURATION_FIELD_NAME = "DefaultRecordedSegmentDuration";
    private final static String COPY_SEGMENT_TO_LOCATION_FIELD_NAME = "CopySegmentToLocation";
    private final static String INVALID_SERVER_INDEX = "-1";
    private final static String CLIENT_PROPERTY_SERVER_INDEX = "serverIndex";
    private final static String LIVE_STREAM_EXCEEDED_MAX_RECORDED_DURATION = "LIVE_STREAM_EXCEEDED_MAX_RECORDED_DURATION";
    private final static String KALTURA_WOWZA_SERVER_WORK_MODE = "KalturaWorkMode";
    private final static String KALTURA_WOWZA_SERVER_WORK_MODE_KALTURA = "kaltura";
    private final static String CLIENT_PROPERTY_KALTURA_LIVE_ENTRY = "KalturaLiveEntry";
    private static Logger logger = Logger.getLogger(RecordingManager.class);

    static private Map<String, Map<String, EntryRecorder>> recorders = new ConcurrentHashMap<String, Map<String, EntryRecorder>>(); //todo checkit

    static private Boolean groupInitialized = false;
    static private GroupPrincipal group;
    static private String OS = System.getProperty("os.name");
    private final ConcurrentHashMap<IMediaStream, RecordingManagerLiveStreamListener> streams;
    Map<String, Object> serverConfiguration;

    class EntryRecorder extends LiveStreamRecorderMP4 implements ILiveStreamRecordNotify {  //todo change to flavor recorder
        private String entryId;
        private String assetId;
        private KalturaLiveEntry liveEntry;
        private KalturaEntryServerNodeType index;
        private boolean isLastChunk = false;

        abstract class AppendRecordingTimerTask extends TimerTask {

            protected String filePath;
            protected boolean lastChunkFlag;
            protected double appendTime;

            public AppendRecordingTimerTask(String setFilePath, boolean lastChunk, double time) {
                filePath = setFilePath;
                lastChunkFlag = lastChunk;
                appendTime = time;
            }
        }

        public EntryRecorder(KalturaLiveEntry liveEntry, String entryId, String assetId, KalturaEntryServerNodeType index) {
            super();

            this.liveEntry = liveEntry;
            this.entryId = entryId;
            this.assetId = assetId;
            this.index = index;
            this.isLastChunk = false;

            this.addListener(this);
        }

        public String getEntryId() {
            return entryId;
        }

        public String getAssetId() {
            return assetId;
        }


        public KalturaEntryServerNodeType getIndex() {
            return index;
        }

        @Override
        public void onSegmentStart(ILiveStreamRecord ilivestreamrecord) {
            // Receive a notification that a new file has been opened for writing.
            logger.info("New segment start: entry ID [" + entryId + "], asset ID [" + assetId + "], segment number [" + this.segmentNumber + "]");
        }

        @Override
        public void onSegmentEnd(ILiveStreamRecord liveStreamRecord) {
            //Receive a notification that the current recorded file has been closed (data is no longer being written to the file).
            logger.info("Stream [" + stream.getName() + "] segment number [" + this.getSegmentNumber() + "] duration [" + this.getCurrentDuration() + "]");
            if (this.getCurrentDuration() == 0) {
                logger.warn("Stream [" + stream.getName() + "] include  duration [" + this.getCurrentDuration() + "]");
            }

            AppendRecordingTimerTask appendRecording = new AppendRecordingTimerTask(file.getAbsolutePath(), isLastChunk, (double) this.getCurrentDuration() / 1000) {

                @Override
                public void run() {
                    logger.debug("Running appendRecording task");

                    // copy the file to a diff location
                    String copyTarget = null;
                    if (serverConfiguration.containsKey(COPY_SEGMENT_TO_LOCATION_FIELD_NAME)) {
                        copyTarget = serverConfiguration.get(COPY_SEGMENT_TO_LOCATION_FIELD_NAME).toString() + File.separator + (new File(filePath).getName());
                        try {
                            if (copyTarget != null) {
                                Files.move(Paths.get(filePath), Paths.get(copyTarget));
                                filePath = copyTarget;
                            }
                        } catch (Exception e) {
                            logger.error("An error occurred copying file from [" + filePath + "] to [" + filePath + "] :: " + e.getMessage());
                        }
                    }

                    Path path = Paths.get(filePath);
                    logger.info("Stream [" + stream.getName() + "] file [" + filePath + "] changing group name to [" + group.getName() + "]");

                    if (group != null) {
                        PosixFileAttributeView fileAttributes = Files.getFileAttributeView(path, PosixFileAttributeView.class);

                        try {
                            fileAttributes.setGroup(group);
                        } catch (IOException e) {
                            logger.error(e.getMessage());
                        }
                    }

                    appendRecording(liveEntry, entryId, assetId, index, filePath, appendTime, lastChunkFlag);

                }
            };

            Timer appendRecordingTimer = new Timer("appendRecording", true);
            appendRecordingTimer.schedule(appendRecording, 1);
            this.isLastChunk = false;
        }

        @Override
        public void onUnPublish() {
            logger.info("Stop recording: entry Id [" + entryId + "], asset Id [" + assetId + "]");

            //  todo check the following code

            //If the current live asset being unpublished is the recording anchor - send cancelReplace call
            // KalturaLiveAsset liveAsset = liveManager.getLiveAssetById(entryId, assetId);
            logger.info("current media server index: " + index);
            //     if (liveAsset != null && liveAsset.tags.contains(RecordingManager.RECORDING_ANCHOR_TAG_VALUE) && KalturaEntryServerNodeType.LIVE_PRIMARY.equals(index)) {
            //            cancelReplace(entryId);
            //       }

            this.isLastChunk = true;
            super.onUnPublish();

            this.stopRecording();
            //remove record from map
            synchronized (recorders) {
                recorders.remove(entryId);
            }
            this.removeListener(this);
        }

    }


    public RecordingManager() {
        logger.debug("Creating a new instance of RecordingManager");
        this.streams = new ConcurrentHashMap<IMediaStream, RecordingManagerLiveStreamListener>();
        serverConfiguration = ServerListener.getServerConfig(); //todo null pointer expeption
        if (serverConfiguration == null) {
            logger.error("serverConfiguration is not available");
        }
    }

    public void onAppStart(IApplicationInstance applicationInstance) {   //todo CHECK WHY?

        if (OS.startsWith("Windows")) {

            return;
        }
        if (serverConfiguration == null) {
            return;
        }
        synchronized (groupInitialized) {  //todo is necessary?
            if (!groupInitialized) {
                String groupName = DEFAULT_RECORDED_FILE_GROUP;
                if (serverConfiguration.containsKey(KALTURA_RECORDED_FILE_GROUP)) {
                    groupName = (String) serverConfiguration.get(KALTURA_RECORDED_FILE_GROUP);
                }
                UserPrincipalLookupService lookupService = FileSystems.getDefault().getUserPrincipalLookupService();
                try {
                    group = lookupService.lookupPrincipalByGroupName(groupName);
                } catch (IOException e) {
                    logger.error("Group [" + groupName + "] not found", e);    //todo why might catch it?
                    return;
                }
                groupInitialized = true;
            }
        }

        logger.debug("Application started");
    }

    //todo Note that all streams (also all that are with out recording  insert to streams,
    public void onStreamCreate(IMediaStream stream) {   //todo check get parent

        RecordingManagerLiveStreamListener listener = new RecordingManagerLiveStreamListener();
        streams.put(stream, listener);
        logger.debug("Stream.getName() " + stream.getName() + " and stream.getClientId() " + stream.getClientId());
        stream.addClientListener(listener);
    }

    public void onStreamDestroy(IMediaStream stream) {


        logger.debug("Stream.getName() " + stream.getName() + " and stream.getClientId() " + stream.getClientId());

        RecordingManagerLiveStreamListener listener = streams.remove(stream);
        if (listener != null) {
            logger.debug("Remove clientListener: stream.getName() " + stream.getName() + " and stream.getClientId() " + stream.getClientId());
            stream.removeClientListener(listener);
        }
    }

    class RecordingManagerLiveStreamListener extends MediaStreamActionNotifyBase {


        public void onPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend) {

            if (!stream.isTranscodeResult()) {
                return;
            }


            WMSProperties properties = Utils.getEntryProperties(stream);
            if (properties==null){
                logger.error("Failed to retrieve property from stream  [" + stream.getName() + "]");
                return;
            }

            if (!properties.containsKey(CLIENT_PROPERTY_KALTURA_LIVE_ENTRY)) {
                logger.error("Property is not included KalturaLiveEntry");
                return ;
            }

            KalturaLiveEntry liveEntry= (KalturaLiveEntry) properties.getProperty(CLIENT_PROPERTY_KALTURA_LIVE_ENTRY);

            if (liveEntry == null){
                logger.error("Failed to retrieve LiveEntry property from stream  [" + stream.getName() + "]");
                return ;
            }

            if(liveEntry.recordStatus == null || liveEntry.recordStatus == KalturaRecordStatus.DISABLED){
                logger.info("Entry [" + liveEntry.id + "] recording disabled");
                return;
            }
            //todo This code section should run for source steam that has recording
            KalturaEntryServerNodeType serverIndex = KalturaEntryServerNodeType.get(properties.getPropertyStr(CLIENT_PROPERTY_SERVER_INDEX, INVALID_SERVER_INDEX));


            int assetParamsId = Integer.MIN_VALUE;

            Matcher matcher = Utils.getStreamNameMatches(streamName);
            if (matcher == null) {
                logger.error("Transcoder published stream [" + streamName + "] does not match entry regex");
                return;
            }


                //todo check that the following code is necessary

                //       synchronized (restreams) {
                //           if(restreams.containsKey(entryId)){
                //               Map<String, Stream> entryStreams = restreams.get(entryId);
                //               if(entryStreams.containsKey(streamName)){
                //                   restream(entryStreams.get(streamName), streamName);
                //               }
                //           }
                //       }

            assetParamsId = Integer.parseInt(matcher.group(2));
            logger.debug("Stream [" + streamName + "] entry [" + liveEntry.id + "] asset params id [" + assetParamsId + "]");



            KalturaLiveAsset liveAsset = KalturaAPI.getKalturaAPI().getAssetParams(liveEntry, assetParamsId);
            if (liveAsset == null) {
                logger.error("Entry [" + liveEntry.id + "] asset params id [" + assetParamsId + "] asset not found");
                return;
            }
            startRecording(liveEntry, liveEntry.id, liveAsset.id, stream, serverIndex, true, true, true);
        }


    }


    public String startRecording(KalturaLiveEntry liveEntry , String entryId, String assetId, IMediaStream stream, KalturaEntryServerNodeType index, boolean versionFile, boolean startOnKeyFrame, boolean recordData){
        logger.debug("Stream name [" + stream.getName() + "] entry [" + entryId + "]");

        // create a stream recorder and save it in a map of recorders
        EntryRecorder recorder = new EntryRecorder(liveEntry, entryId, assetId, index);

        // remove existing recorder from the recorders list
//todo fixit
        synchronized (recorders){   //remove synchronized
            Map<String, EntryRecorder> entryRecorders = recorders.get(entryId);
            if(entryRecorders != null){
                ILiveStreamRecord prevRecorder = entryRecorders.get(assetId);
                if (prevRecorder != null){
                    prevRecorder.stopRecording();
                    entryRecorders.remove(assetId);
                }
            }
        }

//		File writeFile = stream.getStreamFileForWrite(entryId, index.getHashCode() + ".flv", "");
        File writeFile = stream.getStreamFileForWrite(entryId + "." + assetId, index.getHashCode() + ".mp4", "");   //Check this function
        String filePath = writeFile.getAbsolutePath();

        logger.debug("Entry [" + entryId + "]  file path [" + filePath + "] version [" + versionFile + "] start on key frame [" + startOnKeyFrame + "] record data [" + recordData + "]");

        // if you want to record data packets as well as video/audio
        recorder.setRecordData(recordData);

        // Set to true if you want to version the previous file rather than overwrite it
        recorder.setVersionFile(versionFile);

        // If recording only audio set this to false so the recording starts immediately
        recorder.setStartOnKeyFrame(startOnKeyFrame);

        int segmentDuration = DEFAULT_RECORDED_SEGMENT_DURATION;
        if (serverConfiguration.containsKey(DEFAULT_RECORDED_SEGMENT_DURATION_FIELD_NAME))
            segmentDuration = (int) serverConfiguration.get(DEFAULT_RECORDED_SEGMENT_DURATION_FIELD_NAME);

        // start recording
        recorder.startRecordingSegmentByDuration(stream, filePath, null, segmentDuration);

        // add it to the recorders list
        synchronized (recorders){   //check  synchronized
            Map<String, EntryRecorder> entryRecorders;
            if(recorders.containsKey(entryId)){
                entryRecorders = recorders.get(entryId);
            }
            else{
                entryRecorders = new ConcurrentHashMap<String, EntryRecorder>();
                recorders.put(entryId, entryRecorders);
            }
            entryRecorders.put(assetId, recorder);
        }

        return filePath;
    }

    public void appendRecording(KalturaLiveEntry liveEntry, String entryId, String assetId, KalturaEntryServerNodeType index, String filePath, double duration, boolean isLastChunk) {

        logger.info("Entry [" + entryId + "] asset [" + assetId + "] index [" + index + "] filePath [" + filePath + "] duration [" + duration + "] isLastChunk [" + isLastChunk + "]");

        if (serverConfiguration.containsKey(UPLOAD_XML_SAVE_PATH))
        {
            //todo this function is related to ECDN, and therefore was not checked.
            boolean result = saveUploadAsXml (entryId, assetId, index, filePath, duration, isLastChunk, liveEntry.partnerId);
            if (result) {
                liveEntry.msDuration += duration;
                return;
            }

        }

        try {
            KalturaAPI.getKalturaAPI().appendRecording(liveEntry.partnerId,  entryId, assetId,  index,  filePath,  duration,  isLastChunk);
        }
        catch (Exception e) {
            if(e instanceof KalturaApiException && ((KalturaApiException) e).code == LIVE_STREAM_EXCEEDED_MAX_RECORDED_DURATION){
                logger.info("Entry [" + entryId + "] exceeded max recording duration: " + e.getMessage());
            }
            logger.error("Failed to appendRecording: Unexpected error occurred [" + entryId + "]", e);
        }
    }
    private boolean saveUploadAsXml (String entryId, String assetId, KalturaEntryServerNodeType index, String filePath, double duration, boolean isLastChunk, int partnerId)
    {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("upload");
            doc.appendChild(rootElement);

            // entryId element
            Element entryIdElem = doc.createElement("entryId");
            entryIdElem.appendChild(doc.createTextNode(entryId));
            rootElement.appendChild(entryIdElem);

            // assetId element
            Element assetIdElem = doc.createElement("assetId");
            assetIdElem.appendChild(doc.createTextNode(assetId));
            rootElement.appendChild(assetIdElem);

            // partnerId element
            Element partnerIdElem = doc.createElement("partnerId");
            partnerIdElem.appendChild(doc.createTextNode(Integer.toString(partnerId)));
            rootElement.appendChild(partnerIdElem);

            // index element
            Element indexElem = doc.createElement("index");
            indexElem.appendChild(doc.createTextNode(index.hashCode));
            rootElement.appendChild(indexElem);

            // duration element
            Element durationElem = doc.createElement("duration");
            durationElem.appendChild(doc.createTextNode(Double.toString(duration)));
            rootElement.appendChild(durationElem);

            // isLastChunk element
            Element isLastChunkElem = doc.createElement("isLastChunk");
            isLastChunkElem.appendChild(doc.createTextNode(Boolean.toString(isLastChunk)));
            rootElement.appendChild(isLastChunkElem);

            // filepath element
            Element filepathElem = doc.createElement("filepath");
            filepathElem.appendChild(doc.createTextNode(filePath));
            rootElement.appendChild(filepathElem);

            // workmode element
            String workmode = serverConfiguration.containsKey(KALTURA_WOWZA_SERVER_WORK_MODE) ? (String)serverConfiguration.get(KALTURA_WOWZA_SERVER_WORK_MODE) : KALTURA_WOWZA_SERVER_WORK_MODE_KALTURA;
            Element workmodeElem = doc.createElement("workMode");
            workmodeElem.appendChild(doc.createTextNode(workmode));
            rootElement.appendChild(workmodeElem);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            String xmlFilePath = buildXmlFilePath(entryId, assetId);
            StreamResult result = new StreamResult(new File(xmlFilePath));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

            logger.info("Upload XML saved at: " + xmlFilePath);
            return true;
        }
        catch (Exception e) {
            logger.error("Error occurred creating upload XML: " + e.getMessage());
            return false;
        }
    }
    private String buildXmlFilePath(String entryId, String assetId) {
        StringBuilder sb = new StringBuilder();
        sb.append(serverConfiguration.get(UPLOAD_XML_SAVE_PATH));
        sb.append("/");
        sb.append(entryId);
        sb.append("_");
        sb.append(assetId);
        sb.append("_");
        sb.append(System.currentTimeMillis());
        sb.append(".xml");
        return sb.toString();
    }


}