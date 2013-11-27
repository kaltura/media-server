package com.kaltura.media.server.wowza.demo;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.kaltura.client.KalturaParams;
import com.kaltura.client.types.KalturaAnnotation;
import com.wowza.wms.amf.AMFData;
import com.wowza.wms.amf.AMFDataItem;
import com.wowza.wms.amf.AMFDataList;
import com.wowza.wms.amf.AMFDataObj;
import com.wowza.wms.amf.AMFPacket;
import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.CupertinoPacketHolder;
import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.IHTTPStreamerCupertinoLivePacketizerDataHandler;
import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.LiveStreamPacketizerCupertino;
import com.wowza.wms.media.mp3.model.idtags.ID3Frames;
import com.wowza.wms.media.mp3.model.idtags.ID3V2FrameBase;
import com.wowza.wms.media.mp3.model.idtags.ID3V2FrameTextInformation;
import com.wowza.wms.module.ModuleBase;
import com.wowza.wms.stream.livepacketizer.ILiveStreamPacketizer;
import com.wowza.wms.stream.livepacketizer.ILiveStreamPacketizerActionNotify;
import com.wowza.wms.stream.publish.Playlist;
import com.wowza.wms.stream.publish.Stream;

public class LiveCuePointsDemo extends ModuleBase {

	Timer timer = null;
	private int count = 1;
	IApplicationInstance appInstance;

	class LiveStreamPacketizerDataHandler implements IHTTPStreamerCupertinoLivePacketizerDataHandler
	{
		IApplicationInstance appInstance = null;
		
		public LiveStreamPacketizerDataHandler(IApplicationInstance appInstance)
		{
			this.appInstance = appInstance;
		}

		public void onFillChunkDataPacket(CupertinoPacketHolder holder, AMFPacket packet, ID3Frames id3Frames)
		{			
			while(true)
			{
				byte[] buffer = packet.getData();
				if (buffer == null)
					break;
				
				if (packet.getSize() <= 2)
					break;
				
				int offset = 0;
				if (buffer[0] == 0)
					offset++;
				
				AMFDataList amfList = new AMFDataList(buffer, offset, buffer.length-offset);
				
				if (amfList.size() <= 1)
					break;
				
				if (amfList.get(0).getType() != AMFData.DATA_TYPE_STRING && amfList.get(1).getType() != AMFData.DATA_TYPE_OBJECT)
					break;
				
				String metaDataStr = amfList.getString(0);
				AMFDataObj dataObj = amfList.getObject(1);
								
				if (!metaDataStr.equalsIgnoreCase("onTextData"))
					break;
				
				AMFDataItem textData = (AMFDataItem)dataObj.get("text");
				if (textData == null)
					break;
				
				getLogger().info("LiveCuePointsDemo#LiveStreamPacketizerDataHandler.onFillChunkDataPacket["+appInstance.getContextStr()+"] Send string: "+textData.toString());
								
				ID3V2FrameTextInformation comment = new ID3V2FrameTextInformation(ID3V2FrameBase.TAG_TIT1);
				comment.setValue(textData.toString());
				id3Frames.putFrame(comment);
				break;
			}
		}
	}
	
	class LiveStreamPacketizerListener implements ILiveStreamPacketizerActionNotify
	{
		IApplicationInstance appInstance = null;
		
		public LiveStreamPacketizerListener(IApplicationInstance appInstance)
		{
			this.appInstance = appInstance;
		}

		public void onLiveStreamPacketizerCreate(ILiveStreamPacketizer liveStreamPacketizer, String streamName)
		{
			getLogger().info("LiveCuePointsDemo#LiveStreamPacketizerListener.onLiveStreamPacketizerCreate["+streamName+"]");
			if (liveStreamPacketizer instanceof LiveStreamPacketizerCupertino)
			{
				((LiveStreamPacketizerCupertino)liveStreamPacketizer).setDataHandler(new LiveStreamPacketizerDataHandler(appInstance));
			}
		}

		public void onLiveStreamPacketizerDestroy(ILiveStreamPacketizer liveStreamPacketizer)
		{
		}

		public void onLiveStreamPacketizerInit(ILiveStreamPacketizer liveStreamPacketizer, String streamName)
		{
		}
	}
	
	public void sendTextDataMessage()
	{
		Date now = new Date();  	
		Long longTime = new Long(now.getTime() / 1000);
	
		KalturaAnnotation annotation = new KalturaAnnotation();
		annotation.text = "T: " + (count++);
		annotation.createdAt = longTime.intValue();
		KalturaParams params = annotation.toParams();
		
		AMFDataObj data = new AMFDataObj();
		for(String param : params.keySet())
			data.put(param, new AMFDataItem(params.get(param)));
		
		appInstance.broadcastMsg("onTextData", data);
			
		getLogger().info("LiveCuePointsDemo.sendTextDataMessage[" + annotation.text + "]: SENT");
	}
	
	public void onAppStart(IApplicationInstance appInstance) {
		this.appInstance = appInstance;

		appInstance.addLiveStreamPacketizerListener(new LiveStreamPacketizerListener(appInstance));
		
		String streamName = "live.cuepoint.sample";
		Playlist playlist = new Playlist("pl2");	
		playlist.setRepeat(true);
		playlist.addItem("mp4:sample.mp4", 0, -1);
	
		Stream stream = Stream.createInstance(appInstance, streamName);
		stream.setMoveToNextIfLiveStreamMissing(true);
		playlist.open(stream);

		if(timer == null)
		{
			timer = new Timer();
			TimerTask timerTask = new TimerTask() {
				
				@Override
				public void run() {
					sendTextDataMessage();
				}
			};
			
			timer.schedule(timerTask , 3000, 3000);
		}
	}
}
