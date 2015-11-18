package com.kaltura.media.quality.event.listener;

import com.kaltura.media.quality.model.Segment;

public interface ISegmentListener extends IListener {
	void onSegmentDownloadComplete(Segment segment);
}
