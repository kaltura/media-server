package com.kaltura.media.quality.event;

public interface IProcessCompleteListener extends IListener {

	void onProcessComplete(int exitCode);

}
