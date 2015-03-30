package com.kaltura.media.server.wowza.demo;

import java.util.Timer;
import java.util.TimerTask;

import com.wowza.wms.amf.AMFDataItem;
import com.wowza.wms.amf.AMFDataObj;
import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.module.ModuleBase;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.stream.IMediaStreamActionNotify;
import com.wowza.wms.stream.MediaStream;
import com.wowza.wms.stream.publish.Playlist;
import com.wowza.wms.stream.publish.Stream;

public class LiveCaptionsDemo extends ModuleBase 
{
	protected int count = 1;
	
	public void sendTextDataMessage(IApplicationInstance appInstance, MediaStream stream)
	{	
		String text = "Counter: " + (count++);
		
		AMFDataObj data = new AMFDataObj();
		data.put("text", new AMFDataItem(text));
		data.put("lang", new AMFDataItem("eng"));
		data.put("trackid", new AMFDataItem(99));
		
		stream.sendDirect("onTextData", data);
		stream.processSendDirectMessages();	
		
		//appInstance.broadcastMsg("onTextData", data);
			
		getLogger().info("LiveCaptionsDemo.sendTextDataMessage[" + text + "]: SENT");
	}

	public class MyMediaStreamListener implements IMediaStreamActionNotify
	{
		Timer timer;
		
		public void onPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend)
		{
			final IApplicationInstance appInstance = stream.getStreams().getAppInstance();
			final MediaStream mediaStream = (MediaStream) stream;
			
			if (!stream.isTranscodeResult())
			{
				Timer timer = new Timer("sendTextDataMessage-" + streamName, true);

				TimerTask timerTask = new TimerTask() {
					
					@Override
					public void run() {
						sendTextDataMessage(appInstance, mediaStream);
					}
				};
				
				timer.schedule(timerTask , 3000, 3000);
			}
		}

		public void onUnPublish(IMediaStream stream, String streamName, boolean isRecord, boolean isAppend)
		{
			timer.cancel();
			timer.purge();
		}

		public void onPause(IMediaStream stream, boolean isPause, double location) {
		}

		public void onPlay(IMediaStream stream, String streamName, double playStart, double playLen, int playReset) {
		}

		public void onSeek(IMediaStream stream, double location) {
		}

		public void onStop(IMediaStream stream) {
		}
	}
	
	public void onStreamCreate(IMediaStream stream)
	{
		stream.addClientListener(new MyMediaStreamListener());
	}
	
	public void onAppStart(IApplicationInstance appInstance) {
		String streamName = "live.caption.sample";
		Playlist playlist = new Playlist("pl3");	
		playlist.setRepeat(true);
		playlist.addItem("mp4:sample.mp4", 0, -1);
	
		Stream stream = Stream.createInstance(appInstance, streamName);
		stream.setMoveToNextIfLiveStreamMissing(true);
		playlist.open(stream);
	}
}
