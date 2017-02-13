package com.kaltura.media_server.modules;

/**
* Created by ron.yadgar on 02/02/2017.
*/


import com.wowza.wms.amf.*;
import com.wowza.wms.stream.IMediaStream;
import org.apache.log4j.Logger;
import java.util.*;
import com.kaltura.media_server.services.Constants;

class AMFInjection{

    private long runningId=0;
    private static final String OBJECT_TYPE_KEY = "objectType";
    private static final String OBJECT_TYPE_SYNCPOINT = "KalturaSyncPoint";
    private static final String TIMESTAMP_KEY = "timestamp";
    private static final String ID_KEY = "id";
    private static final Logger logger = Logger.getLogger(AMFInjection.class);
    private static final String PUBLIC_METADATA = "onMetaDataRecording";
    private static final long START_SYNC_POINTS_DELAY = 0;
    private int syncPointsInterval = Constants.KALTURA_SYNC_POINTS_INTERVAL_PROPERTY;
    private Timer t;

    public AMFInjection() {
        logger.debug("Creating a new instance of CuePointManagerLiveStreamListener");
    }


    public void onUnPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend) {
        cancelScheduledTask(streamName);
    }

    public void cancelScheduledTask(String streamName){
        if (t!=null) {
            logger.debug("Stopping CuePoints timer for stream " + streamName);
            t.cancel();
            t.purge();
            t = null ;
        }
    }

    public void dispose(IMediaStream stream) {
        logger.debug("Stream [" + stream.getName());
        cancelScheduledTask(stream.getName());
    }

    public void onPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend) {


        t = new Timer();
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
            //logger.info("[" + stream.getName() + "] send sync point  [" +id + "] data:" + data.toString());

        }
        else{
            logger.info("[" + stream.getName() + "] sync point cancelled  [" +id + "],  getPlayPackets = " + stream.getPlayPackets().size());
        }
    }


}