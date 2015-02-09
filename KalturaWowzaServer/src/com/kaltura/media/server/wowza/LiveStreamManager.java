package com.kaltura.media.server.wowza;

import java.util.concurrent.ConcurrentHashMap;

import com.kaltura.client.enums.KalturaMediaServerIndex;
import com.kaltura.client.enums.KalturaRecordStatus;
import com.kaltura.client.types.KalturaLiveAsset;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.media.server.KalturaEventsManager;
import com.kaltura.media.server.events.IKalturaEvent;
import com.kaltura.media.server.events.KalturaEventType;
import com.kaltura.media.server.managers.ILiveManager;
import com.kaltura.media.server.managers.KalturaLiveStreamManager;
import com.kaltura.media.server.managers.KalturaManagerException;
import com.kaltura.media.server.wowza.events.KalturaMediaEventType;
import com.kaltura.media.server.wowza.events.KalturaMediaStreamEvent;
import com.wowza.wms.stream.IMediaStream;

public class LiveStreamManager extends KalturaLiveStreamManager {
	
	protected class LiveStreamEntryReferrer implements ILiveManager.ILiveEntryReferrer {
		protected String entryId;
		protected String streamId;
		
		public LiveStreamEntryReferrer(String entryId, String streamId) {
			this.entryId = entryId;
			this.streamId = streamId;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((entryId == null) ? 0 : entryId.hashCode());
			result = prime * result
					+ ((streamId == null) ? 0 : streamId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			LiveStreamEntryReferrer other = (LiveStreamEntryReferrer) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (entryId == null) {
				if (other.entryId != null)
					return false;
			} else if (!entryId.equals(other.entryId))
				return false;
			if (streamId == null) {
				if (other.streamId != null)
					return false;
			} else if (!streamId.equals(other.streamId))
				return false;
			return true;
		}

		private LiveStreamManager getOuterType() {
			return LiveStreamManager.this;
		}
		
		
	}

	protected final static String KALTURA_ASSET_TAG_SOURCE = "source";
	
	private RecordingManager recordingManager;
	protected ConcurrentHashMap<String, IMediaStream> streams = new ConcurrentHashMap<String, IMediaStream>();

	@Override
	public void init() throws KalturaManagerException {
		super.init();

		recordingManager = new RecordingManager(this);
		KalturaEventsManager.registerEventConsumer(this, 
				KalturaMediaEventType.MEDIA_STREAM_PUBLISHED, KalturaEventType.STREAM_PUBLISHED, KalturaEventType.STREAM_UNPUBLISHED);
		setInitialized();
	}

	protected boolean restartRecording(String entryId){
		logger.debug("Restart recording: " + entryId);
		return recordingManager.restart(entryId);
	}
	
	public String startRecord(String entryId, String assetId, IMediaStream stream, KalturaMediaServerIndex index, boolean versionFile, boolean startOnKeyFrame, boolean recordData){
		logger.debug("Start recording: " + entryId);
		return recordingManager.start(entryId, assetId, stream, index, versionFile, startOnKeyFrame, recordData);
	}
	
	@Override
	public boolean splitRecordingNow(String entryId) {
		return restartRecording(entryId);
	}

	public RecordingManager getRecordingManager() {
		return recordingManager;
	}

	@Override
	public void onEvent(IKalturaEvent event){
		
		KalturaMediaStreamEvent mediaStreamEvent;
		logger.info("Handling event of type " + event.getType().toString() );
		if(event.getType() instanceof KalturaMediaEventType){
			
			switch((KalturaMediaEventType) event.getType())
			{
				case MEDIA_STREAM_PUBLISHED:
					mediaStreamEvent = (KalturaMediaStreamEvent) event;
					onMediaStreamPublish(mediaStreamEvent.getMediaStream(), mediaStreamEvent.getEntryId(), mediaStreamEvent.getServerIndex(), mediaStreamEvent.getAssetParamsId());
					break;
					
				default:
					break;
			}
		}
		
		if (event.getType() instanceof KalturaEventType) {
			switch((KalturaEventType) event.getType())
			{
				case STREAM_PUBLISHED:
					mediaStreamEvent = (KalturaMediaStreamEvent) event;
					IMediaStream stream = mediaStreamEvent.getMediaStream();
					
					addReferrer(mediaStreamEvent.getEntryId(), new LiveStreamEntryReferrer(mediaStreamEvent.getEntryId(), stream.getName()));
					
					if(stream.getClientId() < 0){
						logger.debug("Stream [" + stream.getName() + "] entry [" + mediaStreamEvent.getEntryId() + "] is a transcoded rendition");
						break;
					}
					
					logger.debug("Stream [" + stream.getName() + "] entry [" + mediaStreamEvent.getEntryId() + "] saved for case of diconnect");
					synchronized (streams) {
						streams.put(mediaStreamEvent.getEntryId(), mediaStreamEvent.getMediaStream());
					}
					break;
					
				case STREAM_UNPUBLISHED:
					KalturaMediaStreamEvent streamUnpublishedEvent = (KalturaMediaStreamEvent) event;
					IMediaStream streamUnpublished = streamUnpublishedEvent.getMediaStream();
					removeReferrer(streamUnpublishedEvent.getEntryId(), 
							new LiveStreamEntryReferrer(streamUnpublishedEvent.getEntryId(), streamUnpublished.getName()));
					break;
					
				default:
					break;
			}
		}
		
		super.onEvent(event);
	}
	
	protected void onMediaStreamPublish(IMediaStream stream, String entryId, KalturaMediaServerIndex serverIndex, int assetParamsId) {

		logger.debug("Stream [" + stream.getName() + "] entry [" + entryId + "] index [" + serverIndex.hashCode + "] asset params id [" + assetParamsId + "]");
		
		KalturaLiveEntry liveEntry = get(entryId);
		if(liveEntry == null){
			logger.error("Entry [" + entryId + "] not found for asset params id [" + assetParamsId + "]");
			return;
		}

		if(liveEntry.recordStatus == null || liveEntry.recordStatus == KalturaRecordStatus.DISABLED){
			logger.info("Entry [" + entryId + "] recording disabled");
			return;
		}
		
		KalturaLiveAsset liveAsset = getLiveAsset(entryId, assetParamsId);
		if(liveAsset == null){
			logger.error("Entry [" + entryId + "] asset params id [" + assetParamsId + "] asset not found");
			return;
		}
		
		String fileName = startRecord(liveEntry.id, liveAsset.id, stream, serverIndex, true, true, true);
		logger.debug("Entry [" + entryId + "] asset params id [" + assetParamsId + "] recording to file [" + fileName + "]");
	}

	@Override
	protected void disconnectStream(String entryId) {
		synchronized (streams) {
			if (streams.containsKey(entryId)) {
				logger.info("Disconnecting stream for " + entryId);
				IMediaStream streamToDisconnect = streams.get(entryId);
				if(streamToDisconnect.getClient() != null){
					streamToDisconnect.getClient().rejectConnection("Connection rejected- planned event");
					streamToDisconnect.getClient().shutdownClient();
				}
				else if(streamToDisconnect.getRTPStream() != null && streamToDisconnect.getRTPStream().getSession() != null){
					streamToDisconnect.getRTPStream().getSession().rejectSession();
					streamToDisconnect.getRTPStream().getSession().shutdown();
				}
				streams.remove(entryId);
			}
		}
		
	}
	
	protected void entryStillAlive(KalturaLiveEntry liveEntry, KalturaMediaServerIndex serverIndex, String applicationName){
		super.entryStillAlive(liveEntry, serverIndex, applicationName);
		
		SmilManager.updateSmils(liveEntry.id);
	}
}
