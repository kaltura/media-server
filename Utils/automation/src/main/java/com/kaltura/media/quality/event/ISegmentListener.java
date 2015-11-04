package com.kaltura.media.quality.event;

import java.io.File;

public interface ISegmentListener extends IListener {
	void onSegmentDownloadStart(int segmentNumber, String domainHash);
	void onSegmentDownloadComplete(int segmentNumber, String domainHash, File segment);
}
