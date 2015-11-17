package com.kaltura.media.quality.event.listener;

import java.util.Date;

public interface ILoadListener extends IListener {
	void onLoadResult(Date time, double loadAverage, double cpu, double physicalMemory);
}
