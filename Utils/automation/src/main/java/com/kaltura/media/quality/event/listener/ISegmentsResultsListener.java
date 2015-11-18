package com.kaltura.media.quality.event.listener;

import com.kaltura.media.quality.model.Segment;


public interface ISegmentsResultsListener extends IListener {
	void onSegmentsResult(Segment segment1, Segment segment2, double diff);
}
