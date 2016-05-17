package com.kaltura.media.server.wowza.events;

import com.kaltura.client.enums.KalturaMediaServerIndex;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.media.server.events.IKalturaEventType;
import com.kaltura.media.server.events.KalturaStreamEvent;
import com.wowza.wms.stream.IMediaStream;

public class KalturaMediaStreamEvent extends KalturaStreamEvent {

	public KalturaMediaStreamEvent(IKalturaEventType type, KalturaLiveEntry entry, KalturaMediaServerIndex serverIndex, String applicationName, IMediaStream mediaStream) {
		super(type, entry, serverIndex, applicationName);

		this.mediaStream = mediaStream;
	}

	public KalturaMediaStreamEvent(IKalturaEventType type, KalturaLiveEntry entry, KalturaMediaServerIndex serverIndex, String applicationName, IMediaStream mediaStream, int assetParamsId) {
		this(type, entry, serverIndex, applicationName, mediaStream);

		this.assetParamsId = assetParamsId;
	}

	private IMediaStream mediaStream;
	private int assetParamsId;

	public IMediaStream getMediaStream() {
		return mediaStream;
	}

	public int getAssetParamsId() {
		return assetParamsId;
	}
}
