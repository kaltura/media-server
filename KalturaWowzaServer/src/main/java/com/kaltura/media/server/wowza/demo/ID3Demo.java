package com.kaltura.media.server.wowza.demo;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wowza.wms.application.IApplication;
import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.http.HTTProvider2Base;
import com.wowza.wms.http.IHTTPRequest;
import com.wowza.wms.http.IHTTPResponse;
import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.LiveStreamPacketizerCupertino;
import com.wowza.wms.media.mp3.model.idtags.ID3Frames;
import com.wowza.wms.media.mp3.model.idtags.ID3V2FrameBase;
import com.wowza.wms.media.mp3.model.idtags.ID3V2FrameComment;
import com.wowza.wms.media.mp3.model.idtags.ID3V2FramePrivate;
import com.wowza.wms.media.mp3.model.idtags.ID3V2FrameTextInformation;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.stream.MediaStreamMap;
import com.wowza.wms.stream.livepacketizer.ILiveStreamPacketizer;
import com.wowza.wms.vhost.IVHost;

public class ID3Demo extends HTTProvider2Base {

	protected static Logger logger = Logger.getLogger(ID3Demo.class);
	
	public int sendTag(IMediaStream mediaStream, String tagType, String data) throws Exception {

		ILiveStreamPacketizer packetizer = mediaStream.getLiveStreamPacketizer("cupertinostreamingpacketizer");
		if(packetizer == null || !(packetizer instanceof LiveStreamPacketizerCupertino)){
			throw new Exception("Packetizer not found for stream [" + mediaStream.getName() + "]!");
		}

		LiveStreamPacketizerCupertino cupertinoLiveStreamPacketizer = (LiveStreamPacketizerCupertino) packetizer;
		ID3Frames id3Frames = cupertinoLiveStreamPacketizer.getID3FramesHeader();

		ID3V2FrameBase frame;
		switch(tagType){
			case ID3V2FrameBase.TAG_COMM:
				frame = new ID3V2FrameComment();
				((ID3V2FrameComment) frame).setShortDescription("test description");
				((ID3V2FrameComment) frame).setText(data);
				break;

			case ID3V2FrameBase.TAG_PRIV:
				frame = new ID3V2FramePrivate();
				((ID3V2FramePrivate) frame).setOwnerIdentifier("testOwnerIdentifier");
				((ID3V2FramePrivate) frame).setData(data.getBytes());
				break;
				
			case ID3V2FrameBase.TAG_WXXX:
			case ID3V2FrameBase.TAG_TXXX:
			case ID3V2FrameBase.TAG_TIT1:
			case ID3V2FrameBase.TAG_TIT2:
			case ID3V2FrameBase.TAG_TIT3:
			case ID3V2FrameBase.TAG_TEXT:
			default: 
				frame = new ID3V2FrameTextInformation(tagType);
				((ID3V2FrameTextInformation) frame).setTextEncoding(ID3V2FrameBase.TEXTENCODING_UTF8);
				((ID3V2FrameTextInformation) frame).setValue(data);
		}
		id3Frames.putFrame(frame);
		
		return (cupertinoLiveStreamPacketizer.getLastChunkId() + 1);
	}
	
	@Override
	public void onHTTPRequest(IVHost iVhost, IHTTPRequest httpRequest, IHTTPResponse httpResponse) {
		logger.debug("http request [" + httpRequest.getRequestURL() + "]");
		httpRequest.parseBodyForParams(true);
		
		Map<String, List<String>> params = httpRequest.getParameterMap();

		String errorMessage = "";
		String okMessage = "";
		String streamsOptions = "";
		
		IApplication app = iVhost.getApplication("kLive");
		if(app == null){
			errorMessage = "Application not found!";
		}
		else{
			IApplicationInstance appInstance = app.getAppInstance(IApplicationInstance.DEFAULT_APPINSTANCE_NAME);
			
			if(appInstance == null){
				errorMessage = "Application instance not found!";
			}
			else{
				MediaStreamMap streams = appInstance.getStreams();
				for(IMediaStream mediaStream : streams.getStreams()){
					if(mediaStream.getStreamType().equals("live") && mediaStream.getClientId() < 0){
						streamsOptions += "				<OPTION>" + mediaStream.getName() + "</OPTION>\n";
					}
				}
				
				if (params.containsKey("streamName")) {
					int lastChunkId;
					String streamName = params.get("streamName").get(0);
					if(streamName.equals("All")){
						for(IMediaStream mediaStream : streams.getStreams()){
							if(mediaStream.getStreamType().equals("live") && mediaStream.getClientId() < 0){
								try {
									lastChunkId = sendTag(mediaStream, params.get("tagType").get(0), params.get("data").get(0));				
									String link = "http://dev-hudson9.dev.kaltura.com:1935/kLive/" + streamName + "/playlist.m3u8";
									okMessage += "ID3 tag sent to stream [" + streamName + "] to chunk [" + lastChunkId + "]: <A href=\"" + link + "\">" + link + "</A><BR/>\n";
								} catch (Exception e) {
									errorMessage += e.getMessage() + "<BR/>\n";
								}
							}
						}
					}
					else{
						IMediaStream mediaStream = streams.getStream(streamName);
						if(mediaStream == null){
							errorMessage = "Stream name [" + streamName + "] not found!";
						}
						else{
							try {
								lastChunkId = sendTag(mediaStream, params.get("tagType").get(0), params.get("data").get(0));				
								String link = "http://dev-hudson9.dev.kaltura.com:1935/kLive/" + streamName + "/playlist.m3u8";
								okMessage = "ID3 tag sent to stream [" + streamName + "] to chunk [" + lastChunkId + "]: <A href=\"" + link + "\">" + link + "</A>";
							} catch (Exception e) {
								errorMessage = e.getMessage();
							}
						}
					}
				}
			}
		}

		if(errorMessage.length() > 0)
			logger.error(errorMessage);
		if(okMessage.length() > 0)
			logger.info(okMessage);
		
		String responseBody = "<HTML>\n"
				+ "	<BODY>\n"
				+ "		<P style=\"color: red\">" + errorMessage + "</P><BR/>\n"
				+ "		<P style=\"color: green\">" + okMessage + "</P><BR/>\n"
				+ "		<FORM method=\"POST\">\n"
				+ "			Stream Name: \n"
				+ "			<SELECT name=\"streamName\">"
				+ "				<OPTION>All</OPTION>\n" + streamsOptions
				+ "			</SELECT><BR/>\n"
				+ "			ID3 Tag Type: \n"
				+ "			<SELECT name=\"tagType\">\n"
				+ "				<OPTION>COMM</OPTION>\n"
				+ "				<OPTION>WXXX</OPTION>\n"
				+ "				<OPTION>TXXX</OPTION>\n"
				+ "				<OPTION>TIT1</OPTION>\n"
				+ "				<OPTION>TIT2</OPTION>\n"
				+ "				<OPTION>TIT3</OPTION>\n"
				+ "				<OPTION>TEXT</OPTION>\n"
				+ "				<OPTION>PRIV</OPTION>\n"
				+ "			</SELECT><BR/>\n"
				+ "			Data: \n"
				+ "			<TEXTAREA name=\"data\"></TEXTAREA><BR/>\n"
				+ "			<INPUT type=\"SUBMIT\" />\n"
				+ "		</FORM>\n"
				+ "	</BODY>\n"
				+ "</HTML>";
		
		OutputStream out = httpResponse.getOutputStream();
		try {
			out.write(responseBody.getBytes());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

}
