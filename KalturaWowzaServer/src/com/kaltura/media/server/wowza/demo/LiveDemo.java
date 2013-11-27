package com.kaltura.media.server.wowza.demo;

import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.module.ModuleBase;
import com.wowza.wms.stream.publish.Playlist;
import com.wowza.wms.stream.publish.Stream;

public class LiveDemo extends ModuleBase {

	public void onAppStart(IApplicationInstance appInstance) {
		String streamName = "live.sample";
		Playlist playlist = new Playlist("pl1");	
		playlist.setRepeat(true);
		playlist.addItem("mp4:sample.mp4", 0, -1);
	
		Stream stream = Stream.createInstance(appInstance, streamName);
		stream.setMoveToNextIfLiveStreamMissing(true);
		playlist.open(stream);
	}
}
