package com.kaltura.media.server.wowza;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
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

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.client.KalturaMultiResponse;
import com.kaltura.client.KalturaServiceBase;
import com.kaltura.client.enums.KalturaRecordStatus;
import com.kaltura.client.types.*;
import com.kaltura.media.server.wowza.listeners.ServerListener;
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

import com.kaltura.media.server.wowza.KalturaAPI;





public class RecordingManagerV2  extends ModuleBase {

    protected final static String KALTURA_RECORDED_FILE_GROUP = "g";
    protected final static String DEFAULT_RECORDED_FILE_GROUP = "kaltura";
    protected final static String RECORDING_ANCHOR_TAG_VALUE = "recording_anchor";
    protected final static String CLIENT_PROPERTY_ENTRY_ID = "entryId";

    //The default live segment duration is 15 minutes long
    protected final static int DEFAULT_RECORDED_SEGMENT_DURATION = 900000;
    protected final static String DEFAULT_RECORDED_SEGMENT_DURATION_FIELD_NAME = "DefaultRecordedSegmentDuration";
    protected final static String COPY_SEGMENT_TO_LOCATION_FIELD_NAME = "CopySegmentToLocation";
    protected final static String INVALID_SERVER_INDEX = "-1";
    protected final static String CLIENT_PROPERTY_SERVER_INDEX = "serverIndex";
    protected static Logger logger = Logger.getLogger(RecordingManagerV2.class);

    static private Map<String, Map<String, EntryRecorder>> recorders = new ConcurrentHashMap<String, Map<String, EntryRecorder>>(); //todo checkit

    static protected Boolean groupInitialized = false;
    static protected GroupPrincipal group;
    static private String OS = System.getProperty("os.name");
    private final ConcurrentHashMap<IMediaStream, RecordingManagerLiveStreamListener> streams;
    private Map<Integer, KalturaLiveAsset> liveAssets = new HashMap<Integer, KalturaLiveAsset>();
    protected ConcurrentHashMap<Integer, KalturaLiveParams> liveAssetParams = new ConcurrentHashMap<Integer, KalturaLiveParams>();
    Map<String, Object> serverConfiguration;

    class EntryRecorder extends LiveStreamRecorderMP4 implements ILiveStreamRecordNotify
    {
        private String entryId;
        private String assetId;
        private KalturaEntryServerNodeType index;
        private boolean isLastChunk = false;
        abstract class AppendRecordingTimerTask extends TimerTask {

            protected String filePath;
            protected boolean lastChunkFlag;
            protected double appendTime;

            public AppendRecordingTimerTask (String setFilePath, boolean lastChunk, double time) {
                filePath = setFilePath;
                lastChunkFlag = lastChunk;
                appendTime = time;
            }
        }

        public EntryRecorder(String entryId, String assetId, KalturaEntryServerNodeType index) {
            super();

            this.entryId = entryId;
            this.assetId = assetId;
            this.index = index;
            this.isLastChunk = false;

            this.addListener(this);
        }

        public String getEntryId () {
            return entryId;
        }

        public String getAssetId () {
            return assetId;
        }


        public KalturaEntryServerNodeType getIndex() {
            return index;
        }

        @Override
        public void onSegmentStart(ILiveStreamRecord ilivestreamrecord) {
            logger.info("New segment start: entry ID [" + entryId  + "], asset ID [" + assetId + "], segment number [" + this.segmentNumber + "]");
        }

        @Override
        public void onSegmentEnd(ILiveStreamRecord liveStreamRecord) {
            if(this.getCurrentDuration() != 0){
                logger.info("Stream [" + stream.getName() + "] segment number [" + this.getSegmentNumber() + "] duration [" + this.getCurrentDuration() + "]");
            }

            AppendRecordingTimerTask appendRecording = new AppendRecordingTimerTask(file.getAbsolutePath(), isLastChunk, (double)this.getCurrentDuration()/1000) {

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

                    if(group != null){
                        PosixFileAttributeView fileAttributes = Files.getFileAttributeView(path, PosixFileAttributeView.class);

                        try {
                            fileAttributes.setGroup(group);
                        } catch (IOException e) {
                            logger.error(e.getMessage());
                        }
                    }

               //     RecordingManagerV2.appendRecording(entryId, assetId, index, filePath, appendTime, lastChunkFlag);

                }
            };


            // TODO appendRecordedSyncPoints
            Timer appendRecordingTimer = new Timer("appendRecording",true);
            appendRecordingTimer.schedule(appendRecording, 1);
            this.isLastChunk = false;
        }
/*
        @Override
        public void onUnPublish () {
            logger.info("Stop recording: entry Id [" + entryId + "], asset Id [" + assetId + "]");

            //If the current live asset being unpublished is the recording anchor - send cancelReplace call
            KalturaLiveAsset liveAsset = liveManager.getLiveAssetById(entryId, assetId);
            logger.info("current media server index: " + index);
            if (liveAsset != null && liveAsset.tags.contains(RecordingManager.RECORDING_ANCHOR_TAG_VALUE) && KalturaEntryServerNodeType.LIVE_PRIMARY.equals(index)) {
                cancelReplace(entryId);
            }

            this.isLastChunk = true;
            super.onUnPublish();

            this.stopRecording();
            //remove record from map
            synchronized (recorders){
                recorders.remove(entryId);
            }
            this.removeListener(this);
        }
        */
    }


    public RecordingManagerV2() {
        logger.debug("Creating a new instance of RecordingManager");
        this.streams = new ConcurrentHashMap<IMediaStream, RecordingManagerLiveStreamListener>();
        serverConfiguration = KalturaAPI.getServerConfig(); //todo null pointer expeption
    }

    public void onAppStart(IApplicationInstance applicationInstance) {   //todo CHECK WHY?

        if (OS.startsWith("Windows")) { //todo is necessary?
            return;
        }
        if (serverConfiguration == null) {
            return;
        }
        synchronized (groupInitialized) {  //todo is necessary?
            if (!groupInitialized) {
                String groupName = DEFAULT_RECORDED_FILE_GROUP;
                if (serverConfiguration.containsKey(KALTURA_RECORDED_FILE_GROUP))
                    groupName = (String) serverConfiguration.get(KALTURA_RECORDED_FILE_GROUP);

                UserPrincipalLookupService lookupService = FileSystems.getDefault().getUserPrincipalLookupService();
                try {
                    group = lookupService.lookupPrincipalByGroupName(groupName);
                } catch (IOException e) {
                    logger.error("Group [" + groupName + "] not found", e);    //todo why might catch it?
                    return;
                }
                groupInitialized = true; //todo is necessary?
            }
        }

        logger.debug("Application started");
    }

    public void onStreamCreate(IMediaStream stream) {

        RecordingManagerLiveStreamListener listener = new RecordingManagerLiveStreamListener();
        streams.put(stream, listener);
        logger.debug("RecordingManager::onStreamCreate with stream.getName() " + stream.getName() + " and stream.getClientId() " + stream.getClientId());
        stream.addClientListener(listener);
    }

    public void onStreamDestroy(IMediaStream stream) {
        logger.debug("RecordingManager::onStreamDestroy with stream.getName() " + stream.getName() + " and stream.getClientId() " + stream.getClientId());

        RecordingManagerLiveStreamListener listener = streams.remove(stream);
        if (listener != null) {
            stream.removeClientListener(listener);
        }
    }

    class RecordingManagerLiveStreamListener extends MediaStreamActionNotifyBase {

        public void onPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend) {

            logger.debug("Stream [" + streamName + "]");
            String entryId;
            KalturaEntryServerNodeType serverIndex;

            WMSProperties properties = getConnectionProperties(stream);
            int assetParamsId = Integer.MIN_VALUE;
            if (properties != null) { // streamed from the client

                KalturaLiveEntry entry = (KalturaLiveEntry) properties.getProperty("KalturaLiveEntry");

            } else if (stream.isTranscodeResult()) {

                Matcher matcher = getStreamNameMatches(streamName);
                if (matcher == null) {
                    logger.error("Transcoder published stream [" + streamName + "] does not match entry regex");
                    return;
                }

                entryId = matcher.group(1);
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
                logger.debug("Stream [" + streamName + "] entry [" + entryId + "] asset params id [" + assetParamsId + "]");
                //todo check that the following code is necessary
                for (IMediaStream mediaStream : stream.getStreams().getStreams()) {
                    properties = getConnectionProperties(mediaStream);
                    if (properties != null && mediaStream.getName().startsWith(entryId)) {
                        logger.debug("Source stream [" + mediaStream.getName() + "] found for stream [" + streamName + "]");
                        break;
                    }
                }

                serverIndex = KalturaEntryServerNodeType.get(properties.getPropertyStr(CLIENT_PROPERTY_SERVER_INDEX, INVALID_SERVER_INDEX));
                KalturaLiveEntry liveEntry = (KalturaLiveEntry) properties.getProperty("KalturaLiveEntry");

                if(liveEntry.recordStatus == null || liveEntry.recordStatus == KalturaRecordStatus.DISABLED){
                    logger.info("Entry [" + entryId + "] recording disabled");
                    return;
                }

                KalturaLiveAsset liveAsset = ServerListener.getKalturaAPI().getAssetParams(liveEntry);
                    if(liveAsset == null){
                        logger.error("Entry [" + entryId + "] asset params id [" + assetParamsId + "] asset not found");
                        return;
                   }
                String fileName = start(liveEntry.id, liveAsset.id, stream, serverIndex, true, true, true);
            }
        }

        private Matcher getStreamNameMatches(String streamName) {
            return getMatches(streamName, "^([01]_[\\d\\w]{8})_(.+)$");
        }

        private Matcher getMatches(String streamName, String regex) { //todo duplicate code
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(streamName);
            if (!matcher.find()) {
                logger.info("Stream [" + streamName + "] does not match regex");
                return null;
            }

            return matcher;
        }

        private WMSProperties getConnectionProperties(IMediaStream stream) {
            //todo check this function
            WMSProperties properties = null;

            if (stream.getClient() != null) {
                properties = stream.getClient().getProperties();
            } else if (stream.getRTPStream() != null && stream.getRTPStream().getSession() != null) {
                properties = stream.getRTPStream().getSession().getProperties();
            } else {
                return null;
            }

            if (!properties.containsKey(CLIENT_PROPERTY_ENTRY_ID)) {
                logger.error("Property is not included Entry");
                return null;
            }

            //         if (onClientConnect(stream) != null) {
            //             return properties;
            //          }

            return properties;
        }


    }

    public String start(String entryId, String assetId, IMediaStream stream, KalturaEntryServerNodeType index, boolean versionFile, boolean startOnKeyFrame, boolean recordData){
        logger.debug("Stream name [" + stream.getName() + "] entry [" + entryId + "]");

        // create a stream recorder and save it in a map of recorders
        EntryRecorder recorder = new EntryRecorder(entryId, assetId, index);

        // remove existing recorder from the recorders list

        synchronized (recorders){
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
        File writeFile = stream.getStreamFileForWrite(entryId + "." + assetId, index.getHashCode() + ".mp4", "");
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
        synchronized (recorders){
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

    public void appendRecording(String entryId, String assetId, KalturaEntryServerNodeType index, String filePath, double duration, boolean isLastChunk) {

        logger.info("Entry [" + entryId + "] asset [" + assetId + "] index [" + index + "] filePath [" + filePath + "] duration [" + duration + "] isLastChunk [" + isLastChunk + "]");

        KalturaLiveEntry liveEntry = get(entryId);
        if(liveEntry == null){
            logger.info("Entry [" + entryId + "] not found");
            return;
        }


        KalturaDataCenterContentResource resource = getContentResource(filePath, liveEntry);

        KalturaClient impersonateClient = impersonate(liveEntry.partnerId);
        KalturaServiceBase liveServiceInstance = getLiveServiceInstance(impersonateClient);

        try {

            Method method = liveServiceInstance.getClass().getMethod("appendRecording", String.class, String.class, KalturaEntryServerNodeType.class, KalturaDataCenterContentResource.class, double.class, boolean.class);
            KalturaLiveEntry updatedEntry = (KalturaLiveEntry)method.invoke(liveServiceInstance, entryId, assetId, index, resource, duration, isLastChunk);

            if(updatedEntry != null){
                synchronized (entries) {
                    LiveEntryCache liveEntryCache = entries.get(entryId);
                    if(liveEntryCache != null){
                        liveEntryCache.setLiveEntry(updatedEntry);
                    }
                    else{
                        entries.put(entryId, new LiveEntryCache(updatedEntry));
                    }
                }
            }
        }
        catch (Exception e) {
            if(e instanceof KalturaApiException && ((KalturaApiException) e).code == KalturaLiveManager.LIVE_STREAM_EXCEEDED_MAX_RECORDED_DURATION){
                logger.info("Entry [" + entryId + "] exceeded max recording duration: " + e.getMessage());
            }
            logger.error("Unexpected error occurred [" + entryId + "]", e);
        }
    }
}
/*








//todo  probably not in use

  //  public boolean restart(String entryId){
  //      logger.debug("Restart: " + entryId);

//        synchronized (recorders)
//        {
//            Map<String, EntryRecorder> entryRecorders = recorders.get(entryId);
//            if (entryRecorders != null){
//                for(EntryRecorder streamRecorder : entryRecorders.values()){
 //                   KalturaLiveAsset liveAsset = liveManager.getLiveAssetById(entryId, streamRecorder.getAssetId());
 //                   if (liveAsset.tags.contains("recording_anchor")) {
 //                       liveManager.cancelReplace(entryId);
 //                   }

//                    streamRecorder.splitRecordingNow();
//                }
//                return true;
//            }
//        }

//        return false;
//    }





    public void cancelReplace (String entryId) {
        logger.info("Cancel replacement is required");
        KalturaLiveEntry liveEntry = get(entryId);

        KalturaClient impersonateClient = impersonate(liveEntry.partnerId);
        try {
            if (liveEntry.recordedEntryId != null && liveEntry.recordedEntryId.length() > 0) {
                impersonateClient.getMediaService().cancelReplace(liveEntry.recordedEntryId);
            }
        }
        catch (Exception e)
        {
            logger.error("Error occured: " + e.getMessage());
        }
    }
    private void loadLiveParams() {
        try {
            KalturaFlavorParamsListResponse flavorParamsList = getClient().getFlavorParamsService().list();
            for(KalturaFlavorParams flavorParams : flavorParamsList.objects){
                if(flavorParams instanceof KalturaLiveParams)
                    liveAssetParams.put(flavorParams.id, (KalturaLiveParams) flavorParams);
            }
            logger.info("loaded live params [" + liveAssetParams.size() + "]");
        } catch (KalturaApiException e) {
            logger.error("failed to load live params: " + e.getMessage());
        }
    }
    public KalturaLiveAsset getLiveAsset(int assetParamsId) {
        if(liveAssets.containsKey(assetParamsId))
            return liveAssets.get(assetParamsId);

        logger.error("Asset params id [" + assetParamsId + "] not found");
        return null;
    }
    private void loadAssetParams(KalturaLiveEntry liveEntry) {
        if(liveEntry.conversionProfileId <= 0)
            return;

        KalturaConversionProfileAssetParamsFilter assetParamsFilter = new KalturaConversionProfileAssetParamsFilter();
        assetParamsFilter.conversionProfileIdEqual = liveEntry.conversionProfileId;

        KalturaLiveAssetFilter asstesFilter = new KalturaLiveAssetFilter();
        asstesFilter.entryIdEqual = liveEntry.id;

        KalturaClient impersonateClient = impersonate(liveEntry.partnerId);
        impersonateClient.startMultiRequest();
        try {
            impersonateClient.getConversionProfileAssetParamsService().list(assetParamsFilter);
            impersonateClient.getFlavorAssetService().list(asstesFilter);
            KalturaMultiResponse responses = impersonateClient.doMultiRequest();


            Object conversionProfileAssetParamsList = responses.get(0);
            Object flavorAssetsList = responses.get(1);
//todo check if the follwing is needed

            //if(conversionProfileAssetParamsList instanceof KalturaConversionProfileAssetParamsListResponse)
            //    conversionProfileAssetParams = ((KalturaConversionProfileAssetParamsListResponse) conversionProfileAssetParamsList).objects;

            if(flavorAssetsList instanceof KalturaFlavorAssetListResponse){
                for(KalturaFlavorAsset liveAsset : ((KalturaFlavorAssetListResponse) flavorAssetsList).objects){
                    if(liveAsset instanceof KalturaLiveAsset){
                        liveAssets.put(liveAsset.flavorParamsId, (KalturaLiveAsset) liveAsset);
//todo check if the follwing is needed

               //         if(!liveAssetParams.containsKey(liveAsset.flavorParamsId)){
               //             KalturaFlavorParams liveParams = impersonateClient.getFlavorParamsService().get(liveAsset.flavorParamsId);
               //             if(liveParams instanceof KalturaLiveParams)
               //                 liveAssetParams.put(liveAsset.flavorParamsId, (KalturaLiveParams) liveParams);
               //         }

                    }
                }
            }

        } catch (KalturaApiException e) {
            logger.error("Failed to load asset params for live entry [" + liveEntry.id + "]:" + e.getMessage());
        }
    }
}
*/