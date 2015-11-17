package com.kaltura.media.quality.event.listener;


public interface ISegmentsResultsListener extends IListener {
	void onSegmentsResult(String entryId, int segmentNumber, int bitrate1, int bitrate2, double diff);
}
