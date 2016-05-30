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
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;


import com.kaltura.client.KalturaApiException;
import com.kaltura.client.enums.KalturaRecordStatus;
import com.kaltura.client.types.*;
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
    private final static String DEFAULT_RECORDED_SEGMENT_DURATION_FIELD_NAME = "DefaultRecordedSegmentDuration";
    private final static String COPY_SEGMENT_TO_LOCATION_FIELD_NAME = "CopySegmentToLocation";
    private final static String INVALID_SERVER_INDEX = "-1";
    private final static String CLIENT_PROPERTY_SERVER_INDEX = "serverIndex";
    private final static String LIVE_STREAM_EXCEEDED_MAX_RECORDED_DURATION = "LIVE_STREAM_EXCEEDED_MAX_RECORDED_DURATION";
    private final static String KALTURA_WOWZA_SERVER_WORK_MODE = "KalturaWorkMode";
    private final static String KALTURA_WOWZA_SERVER_WORK_MODE_KALTURA = "kaltura";
    private final static String CLIENT_PROPERTY_KALTURA_LIVE_ENTRY = "KalturaLiveEntry";
    private final static String RECORDING_ANCHOR_TAG_VALUE = "recording_anchor";
    private final static String CLIENT_PROPERTY_KALTURA_LIVE_ASSET_LIST = "KalturaLiveAssetList";
    private final static int DEFAULT_RECORDED_SEGMENT_DURATION = 900000; //~15 minutes
    private static Logger logger = Logger.getLogger(RecordingManager.class);

    static private Map<String, Map<String, FlavorRecorder>> entryRecorders = new ConcurrentHashMap<String, Map<String, FlavorRecorder>>();

    static private Boolean groupInitialized = false;
    static private GroupPrincipal group;
    static private String OS = System.getProperty("os.name");
    private final ConcurrentHashMap<IMediaStream, RecordingManagerLiveStreamListener> streams;
    Map<String, Object> serverConfiguration;

    class FlavorRecorder extends LiveStreamRecorderMP4 implements ILiveStreamRecordNotify {
        private String entryId;
        private String assetId;
        private KalturaLiveEntry liveEntry;
        private KalturaLiveAsset liveAsset;
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

        public FlavorRecorder(KalturaLiveEntry liveEntry, KalturaLiveAsset liveAsset, KalturaEntryServerNodeType index) {
            super();

            this.liveEntry = liveEntry; //todo check if need to synchronize
            this.liveAsset = liveAsset;
            this.entryId = liveEntry.id;
            this.assetId = liveAsset.id;
            this.index = index;
            this.isLastChunk = false;

            this.addListener(this);
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

                    KalturaLiveEntry updatedEntry;

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
                            logger.error("An error occurred copying file from [" + filePath + "] to [" + filePath + "] :: " + e);
                        }
                    }

                    Path path = Paths.get(filePath);
                    logger.info("Stream [" + stream.getName() + "] file [" + filePath + "] changing group name to [" + group.getName() + "]");

                    if (group != null) {
                        PosixFileAttributeView fileAttributes = Files.getFileAttributeView(path, PosixFileAttributeView.class);

                        try {
                            fileAttributes.setGroup(group);
                        } catch (IOException e) {
                            logger.error(e);
                        }
                    }

                     updatedEntry = appendRecording(liveEntry, entryId, assetId, index, filePath, appendTime, lastChunkFlag);
                    if (updatedEntry != null){
                        liveEntry = updatedEntry;
                    }

                }
            };

            Timer appendRecordingTimer = new Timer("appendRecording-"+liveEntry.id+"-"+assetId, true);
            appendRecordingTimer.schedule(appendRecording, 1);
            this.isLastChunk = false;
        }

        @Override
        public void onUnPublish() {
            logger.info("Stop recording: entry Id [" + entryId + "], asset Id [" + assetId + "], current media server index: " + index);


            if (liveAsset != null && liveAsset.tags.contains(RECORDING_ANCHOR_TAG_VALUE) && KalturaEntryServerNodeType.LIVE_PRIMARY.equals(index)) {
                logger.info("Cancel replacement is required");      //todo ask Yossi
                if (liveEntry.recordedEntryId != null && liveEntry.recordedEntryId.length() > 0) {
                    KalturaAPI.getKalturaAPI().cancelReplace(liveEntry);
                }
            }

            this.isLastChunk = true;
            super.onUnPublish();

            this.stopRecording();

            //remove record from map
            entryRecorders.remove(entryId);

            this.removeListener(this);
        }

    }


    public RecordingManager() throws  NullPointerException{
        logger.debug("Creating a new instance of RecordingManager");
        this.streams = new ConcurrentHashMap<IMediaStream, RecordingManagerLiveStreamListener>();
        serverConfiguration = ServerListener.getServerConfig();
        if (serverConfiguration == null) {
            throw new NullPointerException("serverConfiguration is not available");
        }
    }

    public void onAppStart(IApplicationInstance applicationInstance) {

        if (OS.startsWith("Windows")) {
            logger.error("Recording manager is not supported in Windows");
            return;
        }
        if (serverConfiguration == null) {
            logger.error("serverConfiguration is not available");
            return;
        }
        synchronized (groupInitialized) {
            if (!groupInitialized) {
                String groupName = DEFAULT_RECORDED_FILE_GROUP;
                if (serverConfiguration.containsKey(KALTURA_RECORDED_FILE_GROUP)) {
                    groupName = (String) serverConfiguration.get(KALTURA_RECORDED_FILE_GROUP);
                }
                UserPrincipalLookupService lookupService = FileSystems.getDefault().getUserPrincipalLookupService();
                try {
                    group = lookupService.lookupPrincipalByGroupName(groupName);
                } catch (IOException e) {
                    logger.error("Group [" + groupName + "] not found", e);
                    return;
                }
                groupInitialized = true;
            }
        }

        logger.debug("Application started");
    }

    public void onConnectAccept(IClient client)
    {
        logger.debug("onConnectAccept");
                //todo duplicateCode
                try {
                    WMSProperties properties = client.getProperties();

                    if (properties==null){
                        logger.error("Failed to retrieve property");
                        return;
                    }

                    KalturaLiveEntry liveEntry= (KalturaLiveEntry) properties.getProperty(CLIENT_PROPERTY_KALTURA_LIVE_ENTRY);

                    if (liveEntry == null){
                        logger.error("Failed to retrieve LiveEntry property ");
                        return ;
                    }

                    if(liveEntry.recordStatus == null || liveEntry.recordStatus == KalturaRecordStatus.DISABLED){
                        logger.info("Entry [" + liveEntry.id + "] recording disabled");
                        return;
                    }

                    KalturaFlavorAssetListResponse  liveAssetList = KalturaAPI.getKalturaAPI().getKalturaFlavorAssetListResponse(liveEntry);
                    if (liveAssetList != null){
                        properties.setProperty(CLIENT_PROPERTY_KALTURA_LIVE_ASSET_LIST, liveAssetList);
                        logger.debug("Adding live asset list for entry"+liveEntry.id);
                    }
                }
                catch (Exception e ){
                    logger.error("not good");
                }
         //   }

    }

    public void onConnectReject(IClient client)
    {

    }


    //Note that all streams (also all that are with out recording  insert to streams,
    public void onStreamCreate(IMediaStream stream) {

        try {
            RecordingManagerLiveStreamListener listener = new RecordingManagerLiveStreamListener();
            streams.put(stream, listener);
            stream.addClientListener(listener);
        }
         catch (Exception  e) {
            logger.error("Exception in onStreamCreate: ", e);
        }
    }

    public void onStreamDestroy(IMediaStream stream) {

        RecordingManagerLiveStreamListener listener = streams.remove(stream);
        if (listener != null) {
            logger.debug("Remove clientListener: stream " + stream.getName() + " and clientId " + stream.getClientId());
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


            KalturaLiveEntry liveEntry= (KalturaLiveEntry) properties.getProperty(CLIENT_PROPERTY_KALTURA_LIVE_ENTRY);

            if (liveEntry == null){
                logger.error("Failed to retrieve LiveEntry property from stream  [" + stream.getName() + "]");
                return ;
            }

            if(liveEntry.recordStatus == null || liveEntry.recordStatus == KalturaRecordStatus.DISABLED){
                logger.info("Entry [" + liveEntry.id + "] recording disabled");
                return;
            }
            // This code section should run for source steam that has recording
            KalturaEntryServerNodeType serverIndex = KalturaEntryServerNodeType.get(properties.getPropertyStr(CLIENT_PROPERTY_SERVER_INDEX, INVALID_SERVER_INDEX));


            int assetParamsId ;
            Matcher matcher = Utils.getStreamNameMatches(streamName);
            if (matcher == null) {
                logger.error("Transcoder published stream [" + streamName + "] does not match entry regex");
                return;
            }

            assetParamsId = Integer.parseInt(matcher.group(2));

            logger.debug("Stream [" + streamName + "] entry [" + liveEntry.id + "] asset params id [" + assetParamsId + "]");

            KalturaLiveAsset liveAsset;
            KalturaFlavorAssetListResponse  liveAssetList= (KalturaFlavorAssetListResponse) properties.getProperty(CLIENT_PROPERTY_KALTURA_LIVE_ASSET_LIST);
            liveAsset = Utils.getliveAsset(liveAssetList, assetParamsId);
            if (liveAsset == null) {
                logger.warn("Cannot find liveAsset"); //todo fix it
                liveAsset = KalturaAPI.getKalturaAPI().getAssetParams(liveEntry, assetParamsId);
            }

            if (liveAsset == null) {
                logger.error("Entry [" + liveEntry.id + "] asset params id [" + assetParamsId + "] asset not found");
                return;
            }
            startRecording(liveEntry, liveAsset, stream, serverIndex, true, true, true);
        }
    }


    public String startRecording(KalturaLiveEntry liveEntry , KalturaLiveAsset liveAsset, IMediaStream stream, KalturaEntryServerNodeType index, boolean versionFile, boolean startOnKeyFrame, boolean recordData){
        logger.debug("Stream name [" + stream.getName() + "] entry [" + liveEntry.id + "]");

        // create a stream recorder and save it in a map of recorders
        FlavorRecorder recorder = new FlavorRecorder(liveEntry, liveAsset, index);


        // remove existing recorder from the recorders list
        Map<String, FlavorRecorder> entryRecorder = entryRecorders.get(liveEntry.id);
        if(entryRecorder != null){
            ILiveStreamRecord prevRecorder = entryRecorder.get(liveAsset.id);
            if (prevRecorder != null){
                prevRecorder.stopRecording();
                entryRecorder.remove(liveAsset.id);
            }
        }

        File writeFile = stream.getStreamFileForWrite(liveEntry.id + "." + liveAsset.id, index.getHashCode() + ".mp4", "");   //Check this function
        String filePath = writeFile.getAbsolutePath();

        logger.debug("Entry [" + liveEntry.id + "]  file path [" + filePath + "] version [" + versionFile + "] start on key frame [" + startOnKeyFrame + "] record data [" + recordData + "]");

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

        // Add it to the recorders list -  necessary for instance if onUnPublish is not called.
        synchronized (entryRecorders){      //todo check synchronized
            entryRecorder = entryRecorders.get(liveEntry.id);
            if(entryRecorder==null){
                entryRecorder = new ConcurrentHashMap<String, FlavorRecorder>();
                entryRecorders.put(liveEntry.id, entryRecorder);
            }
        }
        entryRecorder.put(liveAsset.id, recorder);

        return filePath;
    }

    public KalturaLiveEntry appendRecording(KalturaLiveEntry liveEntry, String entryId, String assetId, KalturaEntryServerNodeType index, String filePath, double duration, boolean isLastChunk) {

        KalturaLiveEntry updateEntry= null;
        logger.info("Entry [" + entryId + "] asset [" + assetId + "] index [" + index + "] filePath [" + filePath + "] duration [" + duration + "] isLastChunk [" + isLastChunk + "]");

        if (serverConfiguration.containsKey(UPLOAD_XML_SAVE_PATH))
        {
            //todo this function is related to ECDN, and therefore was not checked.
            boolean result = saveUploadAsXml (entryId, assetId, index, filePath, duration, isLastChunk, liveEntry.partnerId);
            if (result) {
                liveEntry.msDuration += duration;
                return null;
            }

        }

        try {
            updateEntry = KalturaAPI.getKalturaAPI().appendRecording(liveEntry.partnerId,  entryId, assetId,  index,  filePath,  duration,  isLastChunk);
        }
        catch (Exception e) {
            if(e instanceof KalturaApiException && ((KalturaApiException) e).code == LIVE_STREAM_EXCEEDED_MAX_RECORDED_DURATION){
                logger.info("Entry [" + entryId + "] exceeded max recording duration: " + e.getMessage());
            }
            logger.error("Failed to appendRecording: Unexpected error occurred [" + entryId + "]", e);
        }

        return updateEntry;
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
            logger.error("Error occurred creating upload XML: " + e);
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