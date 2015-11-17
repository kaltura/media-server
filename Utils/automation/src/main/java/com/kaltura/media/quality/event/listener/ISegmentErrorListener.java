package com.kaltura.media.quality.event.listener;

import com.kaltura.media.quality.model.Segment;

public interface ISegmentErrorListener extends IListener {
	void onSegmentError(Segment segment, Exception error);
}
