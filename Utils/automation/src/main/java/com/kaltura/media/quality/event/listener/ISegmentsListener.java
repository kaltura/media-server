package com.kaltura.media.quality.event.listener;

import java.util.List;

import com.kaltura.media.quality.model.Segment;

public interface ISegmentsListener extends IListener {
	void onSegmentsDownloadComplete(List<Segment> segments);
}
