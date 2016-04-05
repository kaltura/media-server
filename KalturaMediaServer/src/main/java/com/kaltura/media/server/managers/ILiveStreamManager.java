package com.kaltura.media.server.managers;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.types.KalturaLiveStreamEntry;
import com.kaltura.client.enums.KalturaEntryServerNodeType;

public interface ILiveStreamManager extends IManager, ILiveManager {

	public KalturaLiveStreamEntry get(String entryId);

	public KalturaLiveStreamEntry authenticate(String entryId, int partnerId, String token, String hostname, KalturaEntryServerNodeType serverIndex) throws KalturaApiException;

	public boolean splitRecordingNow(String entryId);
	
	public boolean shouldSync(String entryId);
}
