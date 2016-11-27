package com.kaltura.media_server.modules;

import com.wowza.wms.amf.*;
import com.wowza.wms.application.*;
import com.wowza.wms.media.model.MediaCodecInfoAudio;
import com.wowza.wms.media.model.MediaCodecInfoVideo;
import com.wowza.wms.module.*;
import com.wowza.wms.stream.IMediaStreamActionNotify2;
import com.wowza.wms.stream.IMediaStreamActionNotify3;
import com.wowza.wms.stream.livetranscoder.LiveStreamTranscoderItem;
import com.wowza.wms.transcoder.model.*;
import org.apache.log4j.Logger;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.stream.livetranscoder.ILiveStreamTranscoder;
import com.wowza.wms.stream.livetranscoder.ILiveStreamTranscoderNotify;
import com.wowza.util.FLVUtils;

public class TemplateControl extends ModuleBase {

    private static final Logger logger = Logger.getLogger(TranscoderNotifier.class);
    private TranscoderNotifier TransNotify = null;
    public static final String AMFSETDATAFRAME = "amfsetdataframe";
    public static final String ONMETADATA_VIDEOCODECIDSTR = "videocodecidstring";
    public static final String ONMETADATA_AUDIOCODECIDSTR = "audiocodecidstring";

    public class TranscoderNotifier implements ILiveStreamTranscoderNotify
    {

        public void onLiveStreamTranscoderCreate(ILiveStreamTranscoder liveStreamTranscoder, IMediaStream stream) {

            logger.info("onLiveStreamTranscoderCreate: " + stream.getSrc());
            TranscoderActionNotifier transcoderActionNotifier = new TranscoderActionNotifier(stream);
            ((LiveStreamTranscoder)liveStreamTranscoder).addActionListener(
                    transcoderActionNotifier);

            WMSProperties props = stream.getProperties();
            synchronized (props)
            {
                props.put("TranscoderActionNotifier", transcoderActionNotifier);
            }

        }
        public void onLiveStreamTranscoderDestroy(ILiveStreamTranscoder liveStreamTranscoder, IMediaStream stream) {

            TranscoderActionNotifier transcoderActionNotifier;
            WMSProperties props = stream.getProperties();
            synchronized (props)
            {
                transcoderActionNotifier = (TranscoderActionNotifier) props.get("TranscoderActionNotifier");
            }
            if (transcoderActionNotifier != null)
            {
                ((LiveStreamTranscoder)liveStreamTranscoder).removeActionListener(transcoderActionNotifier);
                logger.info("remove TranscoderActionNotifier: " + stream.getSrc());
            }

        }
        public void onLiveStreamTranscoderInit(ILiveStreamTranscoder liveStreamTranscoder, IMediaStream stream) { }
    }


    class TranscoderActionNotifier implements ILiveStreamTranscoderActionNotify
    {
        private IMediaStream TransSourceStream = null;
        public static final String ONMETADATA_AUDIODATARATE = "audiodatarate";
        public static final String ONMETADATA_VIDEODATARATE = "videodatarate";
        public static final String ONMETADATA_WIDTH = "width";
        public static final String ONMETADATA_HEIGHT = "height";
        public static final String ONMETADATA_FRAMERATE= "framerate";


        public  final String[] streamParams = {ONMETADATA_AUDIODATARATE, ONMETADATA_VIDEODATARATE, ONMETADATA_WIDTH,
                ONMETADATA_HEIGHT, ONMETADATA_FRAMERATE, ONMETADATA_VIDEOCODECIDSTR, ONMETADATA_AUDIOCODECIDSTR};

        public TranscoderActionNotifier (IMediaStream transsourcestream) {
            this.TransSourceStream = transsourcestream;
        }



        public void onInitBeforeLoadTemplate(LiveStreamTranscoder liveStreamTranscoder) {

            String template = liveStreamTranscoder.getTemplateName();
            IMediaStream stream = liveStreamTranscoder.getStream();

            logger.info("[" + stream.getName() +" ] Template name is "+template);


            WMSProperties props = stream.getProperties();
            AMFDataObj obj;


            synchronized (props)
            {
                obj = (AMFDataObj) props.getProperty(AMFSETDATAFRAME);
            }

            if (obj == null){
                logger.info("[" + stream.getName() +" ] Cant find property AMFDataObj for stream " + stream.getName());
                return;
            }

            for (String streamParam: streamParams) {
                if (obj.containsKey(streamParam)){
                    template += '/' + streamParam + '/' + obj.getString(streamParam);
                }
            }


            liveStreamTranscoder.setTemplateName(template);
            logger.info("[" + stream.getName() +" ] New Template name is "+liveStreamTranscoder.getTemplateName());
        }

        public void onInitStart(LiveStreamTranscoder liveStreamTranscoder, String streamName, String transcoderName,
                                IApplicationInstance appInstance, LiveStreamTranscoderItem liveStreamTranscoderItem) { }
        public void onInitAfterLoadTemplate(LiveStreamTranscoder liveStreamTranscoder) { }
        public void onInitStop(LiveStreamTranscoder liveStreamTranscoder) { }
        public void onCalculateSourceVideoBitrate(LiveStreamTranscoder liveStreamTranscoder, long bitrate) {}
        public void onCalculateSourceAudioBitrate(LiveStreamTranscoder liveStreamTranscoder, long bitrate) {}
        public void onSessionDestinationCreate(LiveStreamTranscoder liveStreamTranscoder,
                                               TranscoderSessionDestination sessionDestination) { }
        public void onSessionVideoEncodeCreate(LiveStreamTranscoder liveStreamTranscoder,
                                               TranscoderSessionVideoEncode sessionVideoEncode) { }
        public void onSessionAudioEncodeCreate(LiveStreamTranscoder liveStreamTranscoder,
                                               TranscoderSessionAudioEncode sessionAudioEncode) {}
        public void onSessionDataEncodeCreate(LiveStreamTranscoder liveStreamTranscoder,
                                              TranscoderSessionDataEncode sessionDataEncode) {}
        public void onSessionVideoEncodeInit(LiveStreamTranscoder liveStreamTranscoder,
                                             TranscoderSessionVideoEncode sessionVideoEncode) { }
        public void onSessionAudioEncodeInit(LiveStreamTranscoder liveStreamTranscoder,
                                             TranscoderSessionAudioEncode sessionAudioEncode) {}
        public void onSessionDataEncodeInit(LiveStreamTranscoder liveStreamTranscoder,
                                            TranscoderSessionDataEncode sessionDataEncode) {}
        public void onSessionVideoEncodeSetup(LiveStreamTranscoder liveStreamTranscoder,
                                              TranscoderSessionVideoEncode sessionVideoEncode) {}
        public void onSessionAudioEncodeSetup(LiveStreamTranscoder liveStreamTranscoder,
                                              TranscoderSessionAudioEncode sessionAudioEncode) { }
        public void onSessionVideoEncodeCodecInfo(LiveStreamTranscoder liveStreamTranscoder,
                                                  TranscoderSessionVideoEncode sessionVideoEncode, MediaCodecInfoVideo codecInfoVideo) {}
        public void onSessionAudioEncodeCodecInfo(LiveStreamTranscoder liveStreamTranscoder,
                                                  TranscoderSessionAudioEncode sessionAudioEncode, MediaCodecInfoAudio codecInfoAudio) {}
        public void onSessionVideoDecodeCodecInfo(LiveStreamTranscoder liveStreamTranscoder,
                                                  MediaCodecInfoVideo codecInfoVideo) {}
        public void onSessionAudioDecodeCodecInfo(LiveStreamTranscoder liveStreamTranscoder,
                                                  MediaCodecInfoAudio codecInfoAudio) {}
        public void onRegisterStreamNameGroup(LiveStreamTranscoder liveStreamTranscoder,
                                              TranscoderStreamNameGroup streamNameGroup) { }
        public void onUnregisterStreamNameGroup(LiveStreamTranscoder liveStreamTranscoder,
                                                TranscoderStreamNameGroup streamNameGroup) { }
        public void onShutdownStart(LiveStreamTranscoder liveStreamTranscoder) { }
        public void onShutdownStop(LiveStreamTranscoder liveStreamTranscoder) { }
        public void onResetStream(LiveStreamTranscoder liveStreamTranscoder) { }
    }

    class StreamListener implements IMediaStreamActionNotify3
    {
        public static final String ONMETADATA_VIDEOCODECID = "videocodecid";
        public static final String ONMETADATA_AUDIOCODECID = "audiocodecid";

        public void onMetaData(IMediaStream stream, AMFPacket metaDataPacket)
        {
            logger.info("onMetaData[" + stream.getContextStr() + "]: " + metaDataPacket.toString());
            getMetaDataParams(metaDataPacket, stream);
        }

        @SuppressWarnings("unchecked")
        public void getMetaDataParams(AMFPacket metaDataPacket, IMediaStream stream)
        {

                AMFDataList dataList = new AMFDataList(metaDataPacket.getData());
                for (int i = 0 ; i < dataList.size(); i++ ){
                        logger.debug("[" + stream.getName() +" ] Found DATA_TYPE_OBJECT");
                        AMFData amfData = dataList.get(i);
                        if (amfData.getType() == AMFData.DATA_TYPE_OBJECT)
                        {
                            AMFDataObj obj = (AMFDataObj) amfData;
                            String videocodec, audiocodec;
                            if (obj.containsKey(ONMETADATA_VIDEOCODECID)){
                                try {
                                     videocodec = FLVUtils.videoCodecToString(obj.getInt(ONMETADATA_VIDEOCODECID));
                                }
                                catch (NumberFormatException e){
                                     videocodec = obj.getString(ONMETADATA_VIDEOCODECID);
                                }
                                obj.put(ONMETADATA_VIDEOCODECIDSTR, videocodec);
                            }

                            if (obj.containsKey(ONMETADATA_AUDIOCODECID)){
                                try{
                                    audiocodec = FLVUtils.audioCodecToString(obj.getInt(ONMETADATA_AUDIOCODECID));
                                }
                                catch (NumberFormatException e){
                                    audiocodec = obj.getString(ONMETADATA_VIDEOCODECID);
                                }
                                obj.put(ONMETADATA_AUDIOCODECIDSTR, audiocodec);
                            }
                            WMSProperties props = stream.getProperties();
                            synchronized (props) {
                                props.put(AMFSETDATAFRAME, obj);
                            }
                            removeListener(stream);
                            return;
                    }
                }
                logger.warn("[" + stream.getName() +" ]  metadata not found");
        }

        public void onPauseRaw(IMediaStream stream, boolean isPause, double location) {}

        public void onPause(IMediaStream stream, boolean isPause, double location) {}

        public void onPlay(IMediaStream stream, String streamName, double playStart, double playLen, int playReset) {}

        public void onPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend) { }

        public void onSeek(IMediaStream stream, double location) {}

        public void onStop(IMediaStream stream){}

        public void onUnPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend) {}

        public void onCodecInfoAudio(IMediaStream stream, MediaCodecInfoAudio codecInfoAudio) {}

        public void onCodecInfoVideo(IMediaStream stream, MediaCodecInfoVideo codecInfoVideo) {}
    }

    public void onAppStart(IApplicationInstance appInstance) {
        String fullname = appInstance.getApplication().getName() + "/"
                + appInstance.getName();
        this.TransNotify = new TranscoderNotifier();
        appInstance.addLiveStreamTranscoderListener(this.TransNotify);
        logger.info("onAppStart: " + fullname);
    }

    @SuppressWarnings("unchecked")
    public void onStreamCreate(IMediaStream stream)
    {
        logger.info("onStreamCreate  clientId:" + stream.getClientId());
        IMediaStreamActionNotify2 actionNotify = new StreamListener();

        WMSProperties props = stream.getProperties();
        synchronized (props)
        {
            props.put("streamActionNotifier", actionNotify);
        }
        stream.addClientListener(actionNotify);
    }

    public void removeListener(IMediaStream stream){
        WMSProperties props = stream.getProperties();
        IMediaStreamActionNotify2 actionNotify = null;
        synchronized (props)
        {
            actionNotify = (IMediaStreamActionNotify2) props.get("streamActionNotifier");
        }
        if (actionNotify != null)
        {
            stream.removeClientListener(actionNotify);
            logger.info("removeClientListener: " + stream.getSrc());
        }
    }
    public void onStreamDestroy(IMediaStream stream)
    {
        logger.info("onStreamDestroy["+stream.getName()+"]: clientId:" + stream.getClientId());

        removeListener(stream);
    }


}