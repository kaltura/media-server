package com.kaltura.media_server.modules;

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

import com.wowza.wms.livestreamrecord.model.ILiveStreamRecord;
import com.wowza.wms.livestreamrecord.model.ILiveStreamRecordNotify;
import com.wowza.wms.livestreamrecord.model.LiveStreamRecorderMP4;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.module.ModuleBase;
import com.wowza.wms.application.*;
import com.wowza.wms.stream.*;
import com.wowza.wms.client.IClient;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.kaltura.media_server.services.*;
import com.kaltura.media_server.listeners.ServerListener;

public class RecordingModule  extends ModuleBase {


    private static Logger logger = Logger.getLogger(RecordingModule.class);

    static private Map<String, Map<String, FlavorRecorder>> entryRecorders = new ConcurrentHashMap<String, Map<String, FlavorRecorder>>();

    static private Boolean groupInitialized = false;
    static private GroupPrincipal group;
    static private String OS = System.getProperty("os.name");
    private final ConcurrentHashMap<IMediaStream, RecordingManagerLiveStreamListener> streams;
    Map<String, Object> serverConfiguration;

    class FlavorRecorder extends LiveStreamRecorderMP4 implements ILiveStreamRecordNotify {
        private KalturaLiveEntry liveEntry;
        private KalturaLiveAsset liveAsset;
        private KalturaEntryServerNodeType index;
        private boolean isLastChunk = false;
        private boolean isNewLiveRecordingEnabled = false;
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

        public FlavorRecorder(KalturaLiveEntry liveEntry, KalturaLiveAsset liveAsset, KalturaEntryServerNodeType index, boolean isNewLiveRecordingEnabled) {
            super();

            this.liveEntry = liveEntry;
            this.liveAsset = liveAsset;
            this.index = index;
            this.isLastChunk = false;
            this.isNewLiveRecordingEnabled = isNewLiveRecordingEnabled;
            this.addListener(this);
        }

        public void copySegmentToLocationFieldsName( String filePath){
            String copyTarget = null;
            copyTarget = serverConfiguration.get(Constants.COPY_SEGMENT_TO_LOCATION_FIELD_NAME).toString() + File.separator + (new File(filePath).getName());
            try {
                if (copyTarget != null) {
                    Files.move(Paths.get(filePath), Paths.get(copyTarget));
                    filePath = copyTarget;
                }
            } catch (Exception e) {
                logger.error("An error occurred copying file from [" + filePath + "] to [" + filePath + "] :: " + e);
            }
        }

        public void setGroupPermision(String filePath){
            Path path = Paths.get(filePath);
            PosixFileAttributeView fileAttributes = Files.getFileAttributeView(path, PosixFileAttributeView.class);

            try {
                fileAttributes.setGroup(group);
            } catch (IOException e) {
                logger.error(e);
            }
        }
        @Override
        public void onSegmentStart(ILiveStreamRecord ilivestreamrecord) {
            // Receive a notification that a new file has been opened for writing.
            logger.info("New segment start: entry ID [" + liveEntry.id + "], asset ID [" + liveAsset.id + "], segment number [" + this.segmentNumber + "]");
        }

        @Override
        public void onSegmentEnd(ILiveStreamRecord liveStreamRecord) {
            //Receive a notification that the current recorded file has been closed (data is no longer being written to the file).
            logger.info("Stream [" + stream.getName() + "] segment number [" + this.getSegmentNumber() + "] duration [" + this.getCurrentDuration() + "]");
            if (this.getCurrentDuration() == 0) {
                logger.warn("Stream [" + stream.getName() + "] include  duration [" + this.getCurrentDuration() + "]");
                return;
            }
            AppendRecordingTimerTask appendRecording = new AppendRecordingTimerTask(file.getAbsolutePath(), isLastChunk, (double) this.getCurrentDuration() / 1000) {

                @Override
                public void run() {

                    KalturaLiveEntry updatedEntry;

                    logger.debug("Running appendRecording task");

                    if (serverConfiguration.containsKey(Constants.COPY_SEGMENT_TO_LOCATION_FIELD_NAME)) { // copy the file to a diff location
                        copySegmentToLocationFieldsName(filePath);
                    }

                    logger.info("Stream [" + stream.getName() + "] file [" + filePath + "] changing group name to [" + group.getName() + "]");

                    if (group != null) {
                        setGroupPermision(filePath);
                    }

                    if (!isNewLiveRecordingEnabled) {
                        updatedEntry = appendRecording(liveEntry, liveAsset.id, index, filePath, appendTime, lastChunkFlag);
                        if (updatedEntry != null){
                            liveEntry = updatedEntry;
                        }
                    } else {
                        logger.debug("skipping append recording API call, new recording enabled");
                    }
                }
            };

            Timer appendRecordingTimer = new Timer("appendRecording-"+liveEntry.id+"-"+liveAsset.id, true);
            appendRecordingTimer.schedule(appendRecording, 1);
            this.isLastChunk = false;
        }

        @Override
        public void onUnPublish() {
            logger.info("Stop recording: entry Id [" + liveEntry.id + "], asset Id [" + liveAsset.id + "], current media server index: " + index);


            if (liveAsset.tags.contains(Constants.RECORDING_ANCHOR_TAG_VALUE) && KalturaEntryServerNodeType.LIVE_PRIMARY.equals(index)) {
                if (liveEntry.recordedEntryId != null && liveEntry.recordedEntryId.length() > 0 && !isNewLiveRecordingEnabled) {
                    logger.info("Cancel replacement is required");
                    KalturaAPI.getKalturaAPI().cancelReplace(liveEntry);
                } else {
                    logger.info("skipping cancel replacement, new recording enabled");
                }
            }


            this.isLastChunk = true;
            super.onUnPublish();

            this.stopRecording();

            //remove record from map
            entryRecorders.remove(liveEntry.id);

            this.removeListener(this);
        }

    }


    public RecordingModule() throws  NullPointerException{
        logger.debug("Creating a new instance of Modules.RecordingManager");
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
                String groupName = Constants.DEFAULT_RECORDED_FILE_GROUP;
                if (serverConfiguration.containsKey(Constants.KALTURA_RECORDED_FILE_GROUP)) {
                    groupName = (String) serverConfiguration.get(Constants.KALTURA_RECORDED_FILE_GROUP);
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

    public void onConnectAccept(IClient client)//Should called after onConnect (if authentication is not failed )
    {
        logger.debug("onConnectAccept"+ client.getClientId());

        try {
            WMSProperties properties = client.getProperties();
            // At this stage there isn't stream objects available yet
            // but the key to get liveEntry from entry's persistent data is available
            String entryId = Utils.getEntryIdFromClient(client);
            KalturaLiveEntry liveEntry = (KalturaLiveEntry)KalturaEntryDataPersistence.getPropertyByEntry(entryId, Constants.CLIENT_PROPERTY_KALTURA_LIVE_ENTRY);
            if (liveEntry.recordStatus == null || liveEntry.recordStatus == KalturaRecordStatus.DISABLED){
                return;
            }

            KalturaFlavorAssetListResponse liveAssetList = KalturaAPI.getKalturaAPI().getKalturaFlavorAssetListResponse(liveEntry);
            if (liveAssetList != null) {
                synchronized(properties) {
                    properties.setProperty(Constants.CLIENT_PROPERTY_KALTURA_LIVE_ASSET_LIST, liveAssetList);
                }
                logger.debug("Adding live asset list for entry [" + liveEntry.id + "]" );
            }
        }
        catch (Exception e ){
            logger.error("Failed to load liveAssetList:",e);
        }
    }

    public void onConnectReject(IClient client)//Should called after onConnect (if authentication failed)
    {
        logger.debug("onConnectRejected"+ client.getClientId());

    }


    //should called for all streams (input and output)
    //This method should be called after onConnectAccept
    public void onStreamCreate(IMediaStream stream) {

        try {
            RecordingManagerLiveStreamListener listener = new RecordingManagerLiveStreamListener();
            streams.put(stream, listener);
            stream.addClientListener(listener);//register to onPublish

        }
         catch (Exception  e) {
            logger.error("Exception in onStreamCreate: ", e);
        }
    }

    public void onStreamDestroy(IMediaStream stream) {

        RecordingManagerLiveStreamListener listener = streams.remove(stream);
        if (listener != null) {
            logger.debug("Remove clientListener: stream " + stream.getName() + " and clientId " + stream.getClientId());
            listener.dispose(stream);
            stream.removeClientListener(listener);
        }

    }

    class RecordingManagerLiveStreamListener extends MediaStreamActionNotifyBase {

        AMFInjection amfInjectionListener;

        public void dispose(IMediaStream stream){
            if (amfInjectionListener != null){
                amfInjectionListener.dispose(stream);
                amfInjectionListener = null;
            }
        }

        public void onPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend) {

            KalturaLiveEntry liveEntry;
            WMSProperties properties;


            try {
                properties = Utils.getEntryProperties(stream);
                liveEntry = (KalturaLiveEntry) KalturaEntryDataPersistence.getPropertyByStream(streamName, Constants.CLIENT_PROPERTY_KALTURA_LIVE_ENTRY);
            }
            catch(Exception e){
                logger.error("Failed to retrieve liveEntry for "+ streamName+" :"+e);
                return;
            }

            if(liveEntry.recordStatus == null || liveEntry.recordStatus == KalturaRecordStatus.DISABLED){
                logger.info("Entry [" + liveEntry.id + "] recording disabled");
                return;
            }

            if (!stream.isTranscodeResult()) {
                if (amfInjectionListener != null){
                    logger.error("amfInjectionListener in already initialized");
                    return;
                }
                amfInjectionListener = new AMFInjection();
                amfInjectionListener.onPublish(stream, streamName, isRecord, isAppend);
                return;
            }



            KalturaEntryServerNodeType serverIndex;
            // This code section should run for source steam that has recording
            synchronized(properties) {
                 serverIndex = KalturaEntryServerNodeType.get(properties.getPropertyStr(Constants.CLIENT_PROPERTY_SERVER_INDEX, Constants.INVALID_SERVER_INDEX));
            }


            int assetParamsId ;
            Matcher matcher = Utils.getStreamNameMatches(streamName);
            if (matcher == null) {
                logger.error("Transcoder published stream [" + streamName + "] does not match entry regex");
                return;
            }

            assetParamsId = Integer.parseInt(matcher.group(2));

            logger.debug("Stream [" + streamName + "] entry [" + liveEntry.id + "] asset params id [" + assetParamsId + "]");

            KalturaLiveAsset liveAsset;
            KalturaFlavorAssetListResponse liveAssetList;
            synchronized(properties) {
                 liveAssetList = (KalturaFlavorAssetListResponse) properties.getProperty(Constants.CLIENT_PROPERTY_KALTURA_LIVE_ASSET_LIST);
            }
            liveAsset = KalturaAPI.getliveAsset(liveAssetList, assetParamsId);
            if (liveAsset == null) {
                logger.warn("Cannot find liveAsset "+assetParamsId+"for stream [" + streamName + "]");
                liveAsset = KalturaAPI.getKalturaAPI().getAssetParams(liveEntry, assetParamsId);
                if (liveAsset == null) {
                    logger.error("Entry [" + liveEntry.id + "] asset params id [" + assetParamsId + "] asset not found");
                    return;
                }
            }

            boolean isNewLiveRecordingEnabled = KalturaAPI.getKalturaAPI().isNewRecordingEnabled(liveEntry);
            startRecording(liveEntry, liveAsset, stream, serverIndex, true, true, true, isNewLiveRecordingEnabled);
        }

        public void onUnPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend) {
                logger.debug("Remove amfInjectionListener: stream " + stream.getName() + " and clientId " + stream.getClientId());
                if (amfInjectionListener !=null){
                    amfInjectionListener.onUnPublish(stream, streamName, isRecord, isAppend);
                }
        }

    }


    public String startRecording(KalturaLiveEntry liveEntry , KalturaLiveAsset liveAsset, IMediaStream stream, KalturaEntryServerNodeType index, boolean versionFile, boolean startOnKeyFrame, boolean recordData, boolean isNewLiveRecordingEnabled){
        logger.debug("Stream name [" + stream.getName() + "] entry [" + liveEntry.id + "]");

        // create a stream recorder and save it in a map of recorders
        FlavorRecorder recorder = new FlavorRecorder(liveEntry, liveAsset, index, isNewLiveRecordingEnabled);


        // remove existing recorder from the recorders list
        Map<String, FlavorRecorder> entryRecorder = entryRecorders.get(liveEntry.id);
        if(entryRecorder != null){  //todo check if this section is not called, then remove it
            ILiveStreamRecord prevRecorder = entryRecorder.get(liveAsset.id);
            if (prevRecorder != null){
                prevRecorder.stopRecording();
                entryRecorder.remove(liveAsset.id);
                logger.warn("Stop recording previous recorder for stream name [" + stream.getName() + "] entry [" + liveEntry.id + "]");
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

        int segmentDuration = Constants.DEFAULT_RECORDED_SEGMENT_DURATION;
        if (serverConfiguration.containsKey(Constants.DEFAULT_RECORDED_SEGMENT_DURATION_FIELD_NAME))
            segmentDuration = (int) serverConfiguration.get(Constants.DEFAULT_RECORDED_SEGMENT_DURATION_FIELD_NAME);

        // start recording
        recorder.startRecordingSegmentByDuration(stream, filePath, null, segmentDuration);

        // Add it to the recorders list -  necessary for instance if onUnPublish is not called.
        synchronized (entryRecorders){
            entryRecorder = entryRecorders.get(liveEntry.id);
            if(entryRecorder==null){
                entryRecorder = new ConcurrentHashMap<String, FlavorRecorder>();
                entryRecorders.put(liveEntry.id, entryRecorder);
            }
        }
        entryRecorder.put(liveAsset.id, recorder);

        return filePath;
    }

    public KalturaLiveEntry appendRecording(KalturaLiveEntry liveEntry, String assetId, KalturaEntryServerNodeType index, String filePath, double duration, boolean isLastChunk) {

        KalturaLiveEntry updateEntry= null;
        logger.info("Entry [" + liveEntry.id + "] asset [" + assetId + "] index [" + index + "] filePath [" + filePath + "] duration [" + duration + "] isLastChunk [" + isLastChunk + "]");

        if (serverConfiguration.containsKey(Constants.KALTURA_SERVER_UPLOAD_XML_SAVE_PATH))
        {

            boolean result = saveUploadAsXml (liveEntry.id, assetId, index, filePath, duration, isLastChunk, liveEntry.partnerId);
            if (result) {
                liveEntry.msDuration += duration;
                return null;
            }

        }

        try {
            updateEntry = KalturaAPI.getKalturaAPI().appendRecording(liveEntry.partnerId,  liveEntry.id, assetId,  index,  filePath,  duration,  isLastChunk);
        }
        catch (Exception e) {
            if(e instanceof KalturaApiException && ((KalturaApiException) e).code == Constants.LIVE_STREAM_EXCEEDED_MAX_RECORDED_DURATION){
                logger.info("Entry [" + liveEntry.id + "] exceeded max recording duration: " + e.getMessage());
            }
            logger.error("Failed to appendRecording: Unexpected error occurred [" + liveEntry.id + "]", e);
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
            String workmode = serverConfiguration.containsKey(Constants.KALTURA_SERVER_WOWZA_WORK_MODE) ? (String)serverConfiguration.get(Constants.KALTURA_SERVER_WOWZA_WORK_MODE) : Constants.WOWZA_WORK_MODE_KALTURA;
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
        sb.append(serverConfiguration.get(Constants.KALTURA_SERVER_UPLOAD_XML_SAVE_PATH));
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