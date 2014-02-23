package com.kaltura.media.server.managers;

import java.util.List;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.types.KalturaBaseEntry;
import com.kaltura.client.types.KalturaLiveChannel;


public interface ILiveChannelManager extends IManager, ILiveManager {

	public KalturaLiveChannel get(String liveChannelId, int partnerId) throws KalturaApiException;
	
	public void start(String liveChannelId, int partnerId);
	
	public void start(KalturaLiveChannel liveChannel, List<KalturaBaseEntry> segmentEntries);
	
	public void publishEntry(String liveChannelId, String entryId, int partnerId);
	
	public void publishSegment(int liveChannelSegmentId, int partnerId);
	
}
