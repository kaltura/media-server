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
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.kaltura.client.enums.KalturaMediaServerIndex;
import com.kaltura.media.server.KalturaLiveManager;
import com.kaltura.media.server.KalturaManagerException;
import com.kaltura.media.server.KalturaServer;
import com.wowza.wms.livestreamrecord.model.ILiveStreamRecord;
import com.wowza.wms.livestreamrecord.model.LiveStreamRecorderMP4;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.stream.IMediaWriterActionNotify;

public class RecordingManager implements IMediaWriterActionNotify {

	protected final static String KALTURA_RECORDED_FILE_GROUP = "KalturaRecordedFileGroup";
	protected final static String DEFAULT_RECORDED_FILE_GROUP = "kaltura";
	
	static private Map<String, EntryRecorder> recorders = new ConcurrentHashMap<String, EntryRecorder>();

	static protected Boolean groupInitialized = false;
	static protected GroupPrincipal group;

	private KalturaLiveManager liveManager;
	private Logger logger;
	
	class EntryRecorder extends LiveStreamRecorderMP4
	{
		private KalturaMediaServerIndex index;
		
		public EntryRecorder(KalturaMediaServerIndex index) {
			this.index = index;
		}

		public KalturaMediaServerIndex getIndex() {
			return index;
		}
	}

	public RecordingManager(KalturaLiveManager liveManager) throws KalturaManagerException {
		this.liveManager = liveManager;

		logger = KalturaServer.getLogger();
		
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

	public void restartRecordings(){
		logger.debug("RecordingManager::restartRecordings");
		synchronized (recorders)
		{
			for(String entryId : recorders.keySet()){
				if(liveManager.isEntryRegistered(entryId)){
					ILiveStreamRecord streamRecorder = recorders.get(entryId);
					if(streamRecorder != null)
						streamRecorder.splitRecordingNow();
				}
			}
		}
	}
	
	public boolean restartRecording(String entryId){
		logger.debug("RecordingManager::restartRecording: " + entryId);

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
	
	public String startRecord(String entryId, IMediaStream stream, KalturaMediaServerIndex index, boolean versionFile, boolean startOnKeyFrame, boolean recordData){
		logger.debug("RecordingManager::startRecord: " + entryId);

		// create a stream recorder and save it in a map of recorders
		EntryRecorder recorder = new EntryRecorder(index);

		// remove existing recorder from the recorders list
		synchronized (recorders){
			ILiveStreamRecord prevRecorder = recorders.get(entryId);
			if (prevRecorder != null)
				prevRecorder.stopRecording();
			recorders.remove(entryId);
		}

//		File writeFile = stream.getStreamFileForWrite(entryId, index.getHashCode() + ".flv", "");
		File writeFile = stream.getStreamFileForWrite(entryId, index.getHashCode() + ".mp4", "");
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
		float duration = (float) stream.getElapsedTime().getTimeSeconds();

		if(group != null){
			Path path = Paths.get(file.getAbsolutePath());
			PosixFileAttributeView fileAttributes = Files.getFileAttributeView(path, PosixFileAttributeView.class);
				
			try {
				fileAttributes.setGroup(group);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		
		KalturaMediaServerIndex index;
		
		synchronized (recorders)
		{
			EntryRecorder streamRecorder = recorders.get(entryId);
			if (streamRecorder == null){
				logger.error("Unable to find recorder for entry [" + entryId + "]");
			}
			
			index = streamRecorder.getIndex();
		}

		liveManager.appendRecording(entryId, index, file.getAbsolutePath(), duration);
	}

	public boolean splitRecordingNow(String entryId) {
		return restartRecording(entryId);
	}
}
