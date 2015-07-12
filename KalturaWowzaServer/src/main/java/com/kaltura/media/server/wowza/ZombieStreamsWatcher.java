package com.kaltura.media.server.wowza;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.wowza.util.FLVUtils;
import com.wowza.wms.amf.AMFPacket;
import com.wowza.wms.client.IClient;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.stream.IMediaStreamLivePacketNotify;
import com.wowza.wms.stream.MediaStreamActionNotifyBase;

/**
 * This watcher was added in order to identify zombie streams.  
 *
 */
public class ZombieStreamsWatcher extends MediaStreamActionNotifyBase implements IMediaStreamLivePacketNotify {

	private static final Logger logger = Logger.getLogger(ZombieStreamsWatcher.class);
	// This is the threshold for milliseconds from last packet after which we will define the stream as zombie.
	private static final int ZOMBIE_STREAM_THRESHOLD = 60 * 1000;
	
	// Map between stream name to its count-down timer
	private static Map<String,TimerTask> streams = new HashMap<String, TimerTask>();
	private final Timer timer = new Timer();
	
	@Override
	public void onLivePacket(IMediaStream stream, AMFPacket packet) {
		if (FLVUtils.isVideoKeyFrame(packet)) {
			
			if(stream.getClientId() < 0){
				// Stream is a transcoded rendition
				return;
			}
			
			// Whenever a packet arrives - set the last packet time to be now
			synchronized (streams) {
				resetTimer(stream);
			}
		}
	}
	
	public void onUnPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend) {
		if(stream.getClientId() < 0){
			// Stream is a transcoded rendition
			return;
		}
		
		TimerTask t = null;
		synchronized (streams) {
			if (!streams.containsKey(streamName)) {
				logger.error("Unpublishing a stream that does not exist in streams map: " + streamName);
				return;
			}
			t = streams.remove(streamName);
		}
		
		if(t != null) {
			logger.debug("Stopping zombie timer for stream " + streamName);
			t.cancel();
		}
	}

	private void resetTimer(IMediaStream stream) {
		String streamName = stream.getName();
		synchronized (streams) {
			if (streams.containsKey(streamName)) {
				// Reset stream upon packet arrival
				TimerTask t = streams.get(streamName);
				t.cancel();
			}
			
			TimerTask task = createStreamTimeoutTask(stream);
			streams.put(streamName, task);
			
			// Schedule task
			try {
				timer.schedule(task, ZOMBIE_STREAM_THRESHOLD);
			} catch (Exception e) {
				logger.error("Error occurred while scheduling a timer task",e);
			}
		}
	}

	private TimerTask createStreamTimeoutTask(final IMediaStream stream) {
		TimerTask tt = new TimerTask() {
			
			/**
			 *  If this function was triggered, it means that the stream wasn't active for ZOMBIE_STREAM_THREASHOLD 
			 *  and therefore should be killed.
			 */
			@Override
			public void run() {
				try {
					String streamName = stream.getName();
					
					synchronized (streams) {
						streams.remove(streamName);
					}
					
					logger.error("Published stream [" + streamName + "] didn't recieve packets for more than " + ZOMBIE_STREAM_THRESHOLD + " mSecs");
					IClient client = stream.getClient();
					client.rejectConnection("Published stream [" + streamName + "] didn't recieve packets for more than " + ZOMBIE_STREAM_THRESHOLD + " mSecs");
					client.shutdownClient();
					
				} catch (Exception e) {
					// Swallow exception
					logger.error("Error occured while running zombie shutdown process", e);
				}
			}
		};
		
		return tt;
	}

}
