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
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.kaltura.client.enums.KalturaMediaServerIndex;
import com.kaltura.client.types.KalturaLiveAsset;
import com.kaltura.media.server.KalturaServer;
import com.kaltura.media.server.managers.KalturaLiveManager;
import com.kaltura.media.server.managers.KalturaManagerException;
import com.wowza.wms.livestreamrecord.model.ILiveStreamRecord;
import com.wowza.wms.livestreamrecord.model.ILiveStreamRecordNotify;
import com.wowza.wms.livestreamrecord.model.LiveStreamRecorderMP4;
import com.wowza.wms.stream.IMediaStream;

public class RecordingManager {

	protected final static String KALTURA_RECORDED_FILE_GROUP = "KalturaRecordedFileGroup";
	protected final static String DEFAULT_RECORDED_FILE_GROUP = "kaltura";
	
	//The default live segment duration is 15 minutes long
	protected final static int DEFAULT_RECORDED_SEGMENT_DURATION = 900000;
	protected final static String DEFAULT_RECORDED_SEGMENT_DURATION_FIELD_NAME = "DefaultRecordedSegmentDuration";
	protected static Logger logger = Logger.getLogger(RecordingManager.class);
	
	static private Map<String, Map<String, EntryRecorder>> recorders = new ConcurrentHashMap<String, Map<String, EntryRecorder>>();

	static protected Boolean groupInitialized = false;
	static protected GroupPrincipal group;

	private KalturaLiveManager liveManager;
	
	class EntryRecorder extends LiveStreamRecorderMP4 implements ILiveStreamRecordNotify
	{
		private String entryId;
		private String assetId;
		private KalturaMediaServerIndex index;
		private double appendedDuration = 0;
		private boolean isLastChunk = false;
		
		public EntryRecorder(String entryId, String assetId, KalturaMediaServerIndex index) {
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
		

		public KalturaMediaServerIndex getIndex() {
			return index;
		}

		@Override
		public void onSegmentStart(ILiveStreamRecord ilivestreamrecord) {
		}

		@Override
		public void onSegmentEnd(ILiveStreamRecord liveStreamRecord) {
			if(liveStreamRecord.getCurrentDuration() == 0){
				logger.info("Stream [" + stream.getName() + "] duration [" + liveStreamRecord.getCurrentDuration() + "]");
				return;
			}
			
			logger.info("Stream [" + stream.getName() + "] file [" + file.getAbsolutePath() + "] folder [" + file.getParent() + "]");
			Date date = new Date();
			double duration = ((double)(date.getTime() - liveStreamRecord.getStartTime().getMillis())) / 1000;
			double currentChunkDuration = duration - appendedDuration;
			appendedDuration = duration;

			if(group != null){
				Path path = Paths.get(file.getAbsolutePath());
				PosixFileAttributeView fileAttributes = Files.getFileAttributeView(path, PosixFileAttributeView.class);
					
				try {
					fileAttributes.setGroup(group);
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}

			// TODO appendRecordedSyncPoints
			liveManager.appendRecording(entryId, assetId, index, file.getAbsolutePath(), currentChunkDuration, this.isLastChunk);

			this.isLastChunk = false;
		}
		
		@Override
		public void onUnPublish () {
			logger.info("Stop recording: entry Id [" + entryId + "], asset Id [" + assetId + "]");
			
			//If the current live asset being unpublished is the recording anchor - send cancelReplace call
			KalturaLiveAsset liveAsset = liveManager.getLiveAssetById(entryId, assetId);
			if (liveAsset != null && liveAsset.tags.contains("recording_anchor")) {
				liveManager.cancelReplace(entryId);
			}

			this.isLastChunk = true;

			this.stopRecording();
		}
	}

	public RecordingManager(KalturaLiveManager liveManager) throws KalturaManagerException {
		this.liveManager = liveManager;

		if(!KalturaServer.isWindows()){
			synchronized (groupInitialized) {
				if(!groupInitialized){
					String groupName = RecordingManager.DEFAULT_RECORDED_FILE_GROUP;
					Map<String, Object> serverConfiguration = KalturaServer.getConfiguration();
					if (serverConfiguration.containsKey(RecordingManager.KALTURA_RECORDED_FILE_GROUP))
						groupName = (String) serverConfiguration.get(RecordingManager.KALTURA_RECORDED_FILE_GROUP);
			
					UserPrincipalLookupService lookupService = FileSystems.getDefault().getUserPrincipalLookupService();
					try {
						group = lookupService.lookupPrincipalByGroupName(groupName);
					} catch (IOException e) {
						throw new KalturaManagerException("Group [" + groupName + "] not found", e);
					}
					groupInitialized = true;
				}
			}
		}
	}

	public void restart(){
		logger.debug("Restart");
		synchronized (recorders)
		{
			for(String entryId : recorders.keySet()){
				if(liveManager.isEntryRegistered(entryId)){
					Map<String, EntryRecorder> entryRecorders = recorders.get(entryId);
					if (entryRecorders != null){
						for(ILiveStreamRecord streamRecorder : entryRecorders.values()){
							streamRecorder.splitRecordingNow();
						}
					}
				}
			}
		}
	}
	
	public boolean restart(String entryId){
		logger.debug("Restart: " + entryId);

		synchronized (recorders)
		{
			Map<String, EntryRecorder> entryRecorders = recorders.get(entryId);
			if (entryRecorders != null){
				for(EntryRecorder streamRecorder : entryRecorders.values()){
					KalturaLiveAsset liveAsset = liveManager.getLiveAssetById(entryId, streamRecorder.getAssetId());
					if (liveAsset.tags.contains("recording_anchor")) {
						liveManager.cancelReplace(entryId);
					}
					
					streamRecorder.splitRecordingNow();
				}
				return true;
			}
		}
		
		return false;
	}
	
	
	public String start(String entryId, String assetId, IMediaStream stream, KalturaMediaServerIndex index, boolean versionFile, boolean startOnKeyFrame, boolean recordData){
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
		
		int segmentDuration = RecordingManager.DEFAULT_RECORDED_SEGMENT_DURATION;
		Map<String, Object> serverConfiguration = KalturaServer.getConfiguration();
		if (serverConfiguration.containsKey(RecordingManager.DEFAULT_RECORDED_SEGMENT_DURATION_FIELD_NAME))
			segmentDuration = (int) serverConfiguration.get(RecordingManager.DEFAULT_RECORDED_SEGMENT_DURATION_FIELD_NAME);
		
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
}
