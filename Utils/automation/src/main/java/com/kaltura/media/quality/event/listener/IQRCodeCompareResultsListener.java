package com.kaltura.media.quality.event.listener;

import java.io.File;

import com.kaltura.media.quality.model.IFrame;
import com.kaltura.media.quality.model.Segment;

public interface IQRCodeCompareResultsListener extends IListener {
	void onQRCodeCompareResult(Segment segment, File image, IFrame frame, double qrCode);
}
