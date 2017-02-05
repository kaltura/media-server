package com.kaltura.media_server.modules;

/**
 * Created by ron.yadgar on 02/02/2017.
 */


import com.wowza.wms.amf.*;
import com.wowza.wms.stream.IMediaStream;
import org.apache.log4j.Logger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import com.kaltura.media_server.services.Constants;
import com.wowza.wms.stream.MediaStreamActionNotifyBase;
import com.wowza.wms.stream.*;

    class AMFInjection{

        private long runningId=0;
        private final Map<String,Timer> listenerStreams;
        private static final String OBJECT_TYPE_KEY = "objectType";
        private static final String OBJECT_TYPE_SYNCPOINT = "KalturaSyncPoint";
        private static final String TIMESTAMP_KEY = "timestamp";
        private static final String ID_KEY = "id";
        private static final Logger logger = Logger.getLogger(AMFInjection.class);
        private static final String PUBLIC_METADATA = "onMetaDataRecording";
        private static final long START_SYNC_POINTS_DELAY = 0;
        private int syncPointsInterval = Constants.KALTURA_SYNC_POINTS_INTERVAL_PROPERTY;

        public AMFInjection() {
            logger.debug("Creating a new instance of CuePointManagerLiveStreamListener");
            this.listenerStreams = new ConcurrentHashMap<String,Timer>();
        }


        public void onUnPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend) {
            Timer t;
            synchronized (listenerStreams) {
                t = listenerStreams.remove(streamName);
            }
            cancelScheduledTask(t,streamName);
        }

        public void cancelScheduledTask(Timer t,String streamName){
            if (t!=null) {
                logger.debug("Stopping CuePoints timer for stream " + streamName);
                t.cancel();
                t.purge();
            } else {

                logger.error("Stream " + streamName + " does not exist in streams map");
            }
        }

        public void dispose() {
            synchronized (listenerStreams) {

                for (String streamName : listenerStreams.keySet()) {
                    Timer t = listenerStreams.get(streamName);
                    cancelScheduledTask(t,streamName);
                }
                listenerStreams.clear();
            }
        }

        public void onPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend) {

            logger.debug("Stream [" + streamName + "]");


            if(stream.getClientId() < 0){
                logger.debug("Stream [" + stream.getName() + "] entry [" + streamName + "] is a transcoded rendition");
                return;
            }

            logger.debug("Stream [" + streamName + "] entry [" + streamName + "]");

            Timer t;
            synchronized (listenerStreams) {
                if (listenerStreams.containsKey(streamName)) {
                    logger.error("Stream with name " + streamName + " already exists in streams map, cancelling old one");
                    t = listenerStreams.remove(streamName);
                    cancelScheduledTask(t,streamName);
                }
                else {
                    logger.debug("Stream with name " + streamName + " does not exist in streams map. creating a new map entry.");
                }
                t = new Timer();
                listenerStreams.put(streamName, t);

            }

            logger.debug("Running timer to create sync points for stream " + streamName);
            startSyncPoints(t, stream, streamName);
        }

        private void startSyncPoints(Timer t, final IMediaStream stream, final String entryId) {
            TimerTask tt = new TimerTask() {
                @Override
                public void run() {
                    try {
                        createSyncPoint(stream, entryId);
                    } catch (Exception e) {
                        logger.error("Error occured while running sync points timer", e);
                    }
                }
            };
            try {
                t.schedule(tt,START_SYNC_POINTS_DELAY, syncPointsInterval);
            } catch (Exception e) {
                logger.error("Error occurred while scheduling a timer task",e);
            }
        }

        private void createSyncPoint(IMediaStream stream, String entryId) {
            String id = entryId+"_"+(runningId++);
            double currentTime = new Date().getTime();

            sendSyncPoint(stream, id, currentTime);
        }

        public void sendSyncPoint(final IMediaStream stream, String id, double time) {

            AMFDataObj data = new AMFDataObj();

            data.put(OBJECT_TYPE_KEY, OBJECT_TYPE_SYNCPOINT);
            data.put(ID_KEY, id);
            data.put(TIMESTAMP_KEY, time);

            //This condition is due to huge duration time (invalid) in the first chunk after stop-start on FMLE.
            //According to Wowza calling sendDirect() before stream contains any packets causes problems.
            if (stream.getPlayPackets().size() > 0) {
                stream.sendDirect(PUBLIC_METADATA, data);
                ((MediaStream)stream).processSendDirectMessages();
                logger.info("[" + stream.getName() + "] send sync point  [" +id + "] data:" + data.toString());

            }
            else{
                logger.info("[" + stream.getName() + "] sync point cancelled  [" +id + "],  getPlayPackets = " + stream.getPlayPackets().size());
            }
        }


}