package com.kaltura.media.quality.event.listener;

import java.io.File;

import com.kaltura.media.quality.model.Segment;

public interface ISegmentFirstFrameImageListener extends IListener {

	void onSegmentFirstFrameCapture(Segment segment, File image);
}
