package com.kaltura.media.server.wowza;

import com.kaltura.client.enums.KalturaMediaServerIndex;
import com.kaltura.media.server.KalturaLiveStreamManager;
import com.kaltura.media.server.KalturaManagerException;
import com.wowza.wms.stream.IMediaStream;

public class LiveStreamManager extends KalturaLiveStreamManager {

	private RecordingManager recordingManager;

	@Override
	public void init() throws KalturaManagerException {
		super.init();

		recordingManager = new RecordingManager(this);
	}

	@Override
	public void restartRecordings(){
		logger.debug("LiveStreamEntry::restartRecordings");
		recordingManager.restartRecordings();
	}

	protected boolean restartRecording(String entryId){
		logger.debug("LiveStreamEntry::restartRecording: " + entryId);
		return recordingManager.restartRecording(entryId);
	}
	
	public String startRecord(String entryId, IMediaStream stream, KalturaMediaServerIndex index, boolean versionFile, boolean startOnKeyFrame, boolean recordData){
		logger.debug("LiveStreamEntry::startRecord: " + entryId);
		return recordingManager.startRecord(entryId, stream, index, versionFile, startOnKeyFrame, recordData);
	}
	
	@Override
	public boolean splitRecordingNow(String entryId) {
		return restartRecording(entryId);
	}
}
