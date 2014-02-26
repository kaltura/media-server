package com.kaltura.media.server.wowza.events;

import com.kaltura.media.server.events.IKalturaEventType;
import com.kaltura.media.server.events.KalturaStreamEvent;
import com.wowza.wms.stream.IMediaStream;

public class KalturaMediaStreamEvent extends KalturaStreamEvent {

	public KalturaMediaStreamEvent(IKalturaEventType type) {
		super(type);
	}

	private IMediaStream mediaStream;
	private int assetParamsId;

	public IMediaStream getMediaStream() {
		return mediaStream;
	}

	public void setMediaStream(IMediaStream mediaStream) {
		this.mediaStream = mediaStream;
	}

	public int getAssetParamsId() {
		return assetParamsId;
	}

	public void setAssetParamsId(int assetParamsId) {
		this.assetParamsId = assetParamsId;
	}
}
