package com.kaltura.media.server.wowza;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.kaltura.media.server.KalturaLiveStreamManager;
import com.kaltura.media.server.KalturaManagerException;
import com.kaltura.media.server.KalturaServer;
import com.wowza.wms.livestreamrecord.model.ILiveStreamRecord;
import com.wowza.wms.livestreamrecord.model.LiveStreamRecorderMP4;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.stream.IMediaWriterActionNotify;

public class LiveStreamManager extends KalturaLiveStreamManager implements IMediaWriterActionNotify {

	protected final static String KALTURA_RECORDED_CHUNCK_MAX_DURATION = "KalturaRecordedChunckMaxDuration";
	protected final static long DEFAULT_RECORDED_CHUNCK_MAX_DURATION = 10;
	
	private Map<String, ILiveStreamRecord> recorders = new ConcurrentHashMap<String, ILiveStreamRecord>();
	private ArrayList<MediaEntryCreator> entryCreators = new ArrayList<MediaEntryCreator>();

	class MediaEntryCreator{
		private String entryId;
		private int offset;
		private int duration;
		
		public MediaEntryCreator(String liveEntryId) {
			entryId = liveEntryId;
			
			synchronized (entryCreators){
				entryCreators.add(this);
			}
		}

		public MediaEntryCreator(String liveEntryId, int offset, int duration) {
			this(liveEntryId);
			
			this.offset = offset;
			this.duration = duration;
		}

		public String getEntryId() {
			return entryId;
		}

		public int getOffset() {
			return offset;
		}

		public int getDuration() {
			return duration;
		}

		public void create() {
			// TODO Auto-generated method stub
		}
	}

	@Override
	public void init() throws KalturaManagerException {
		super.init();

		long interval = LiveStreamManager.DEFAULT_RECORDED_CHUNCK_MAX_DURATION * 60 * 1000;
		if (serverConfiguration.containsKey(LiveStreamManager.KALTURA_RECORDED_CHUNCK_MAX_DURATION))
			interval = Long.parseLong((String) serverConfiguration.get(LiveStreamManager.DEFAULT_RECORDED_CHUNCK_MAX_DURATION)) * 60 * 1000;

		TimerTask timerTask = new TimerTask(){

			@Override
			public void run() {
				restartRecordings();
			}
		};

		hostname = KalturaServer.getHostName();
		client = KalturaServer.getClient();
		
		Timer timer = new Timer(true);
		timer.schedule(timerTask, interval, interval);
	}
	
	protected void restartRecordings(){
		logger.debug("LiveStreamEntry::restartRecordings");
		synchronized (recorders)
		{
			for(ILiveStreamRecord streamRecorder : recorders.values()){
				streamRecorder.splitRecordingNow();
			}
		}
	}
	
	protected boolean restartRecording(String entryId){
		logger.debug("LiveStreamEntry::restartRecording: " + entryId);

		synchronized (recorders)
		{
			ILiveStreamRecord streamRecorder = recorders.get(entryId);
			if (streamRecorder != null){
				streamRecorder.splitRecordingNow();
				return true;
			}
		}
		
		return false;
	}
	
	public String startRecord(String entryId, IMediaStream stream, boolean versionFile, boolean startOnKeyFrame, boolean recordData){
		logger.debug("LiveStreamEntry::startRecord: " + entryId);

		// create a stream recorder and save it in a map of recorders
		ILiveStreamRecord recorder = new LiveStreamRecorderMP4();

		// remove existing recorder from the recorders list
		synchronized (recorders){
			ILiveStreamRecord prevRecorder = recorders.get(entryId);
			if (prevRecorder != null)
				prevRecorder.stopRecording();
			recorders.remove(entryId);
		}

		File writeFile = stream.getStreamFileForWrite(entryId, "mp4", "");
		String filePath = writeFile.getAbsolutePath();
		
		logger.debug("StreamRecorder: entry [" + entryId + "]  file path [" + filePath + "] version [" + versionFile + "] start on key frame [" + startOnKeyFrame + "] record data [" + recordData + "]");
		
		// if you want to record data packets as well as video/audio
		recorder.setRecordData(recordData);
		
		// Set to true if you want to version the previous file rather than overwrite it
		recorder.setVersionFile(versionFile);
		
		// If recording only audio set this to false so the recording starts immediately
		recorder.setStartOnKeyFrame(startOnKeyFrame);
		
		// start recording
		recorder.startRecording(stream, filePath, false);	

		// add it to the recorders list
		synchronized (recorders){
			recorders.put(entryId, recorder);
		}
		
		return filePath;
	}
	
	@Override
	public void onFLVAddMetadata(IMediaStream stream, Map<String, Object> extraMetadata) {
	}

	@Override
	public void onWriteComplete(IMediaStream stream, File file) {
		logger.info("MediaWriterListener::onWriteComplete: stream [" + stream.getName() + "] file [" + file.getAbsolutePath() + "] folder [" + file.getParent() + "]");
		String entryId = stream.getName();
		
		synchronized (entryCreators){
			for(MediaEntryCreator entryCreator : entryCreators){
				if(entryId.compareTo(entryCreator.getEntryId()) == 0){
					entryCreator.create();
				}
			}
		}
	}

	@Override
	public boolean createMediaEntry(String liveEntryId) {
		new MediaEntryCreator(liveEntryId);
		return restartRecording(liveEntryId);
	}

	@Override
	public boolean createMediaEntry(String liveEntryId, int offset, int duration) {
		new MediaEntryCreator(liveEntryId, offset, duration);
		return restartRecording(liveEntryId);
	}
}
