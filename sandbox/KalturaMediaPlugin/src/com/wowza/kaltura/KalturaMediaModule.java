package com.wowza.kaltura;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;

import com.wowza.wms.amf.AMFDataList;
import com.wowza.wms.amf.AMFDataObj;
import com.wowza.wms.amf.AMFPacket;
import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.application.WMSProperties;
import com.wowza.wms.client.IClient;
import com.wowza.wms.logging.WMSLoggerFactory;
import com.wowza.wms.media.model.MediaCodecInfoAudio;
import com.wowza.wms.media.model.MediaCodecInfoVideo;
import com.wowza.wms.module.ModuleBase;
import com.wowza.wms.request.RequestFunction;
import com.wowza.wms.rtp.model.RTPSession;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.stream.IMediaStreamActionNotify;
import com.wowza.wms.stream.IMediaStreamActionNotify3;

public class KalturaMediaModule extends ModuleBase  implements IMediaStreamActionNotify3
{
	static final String methodPublish = "publish";

	static final String methodUnpublish = "unpublish";
	
	static final String methodAuthenticate = "authenticate";

	private static class KalturaMediaServerConfig {

		public KalturaMediaServerConfig(String path)
		{
			try
			{
				BufferedReader in = new BufferedReader( new FileReader(path) );
				String inpitLine;
				while((inpitLine = in.readLine()) != null){
					String [] args = inpitLine.split("=");
					switch(args[0])
					{
					case "SERVER_NODE_NAME":
						hostName = nodeName = args[1];
						break;
					case "apphome_url":
						kalturaAPIServerURL = args[1];
						break;
					}
				}
				in.close();
				hostName = InetAddress.getLocalHost().getHostName();
				Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
				while (interfaces.hasMoreElements()){
				    NetworkInterface current = interfaces.nextElement();
				    System.out.println(current);
				    if (!current.isUp() || current.isLoopback() || current.isVirtual()) continue;
				    Enumeration<InetAddress> addresses = current.getInetAddresses();
				    while (addresses.hasMoreElements()){
				        InetAddress current_addr = addresses.nextElement();
				        if(!(current_addr instanceof Inet4Address))
				        	continue;
			//	        if(current_addr.isSiteLocalAddress())
				//        	continue;
				        if (current_addr.isLoopbackAddress()) 
				        	continue;
				        WMSLoggerFactory.getLogger(null).warn("KalturaMediaServerConfig : hostName: " + current_addr.getHostAddress());
				        System.out.println(current_addr.getCanonicalHostName());
				        hostName = current_addr.getHostAddress();
				        break;
				    }
				}
				
			}
			catch(Exception e)
			{

			}
		}

		private static final KalturaMediaServerConfig s_Instance = new KalturaMediaServerConfig("/opt/kaltura/app/configurations/ecdn.ini");

		static public KalturaMediaServerConfig getInstance(){
			return s_Instance;
		}

		public String nodeName;

		public String hostName;

		public String kalturaAPIServerURL;
	};

	static boolean reportKalturaEvent(String method,Hashtable<String,String> props)
	{
		return reportKalturaEvent(method,props,false);
	}
	
	static String extractStreamName(IMediaStream stream)
	{
		WMSProperties props = stream.getClient().getProperties();
		String [] arr = ((String)props.getProperty("connectapp")).split("/");
		return stream.getName() + arr[arr.length - 1];
	}

	static boolean reportKalturaEvent(String method,Hashtable<String,String> props,boolean bWaitResponce)
	{
		WMSLoggerFactory.getLogger(null).debug(String.format("reportKalturaEvent: %s [%s] %s ",
				method,KalturaMediaServerConfig.getInstance().kalturaAPIServerURL,
				props.get("streamName")));
		try {
			URL url = new URL(KalturaMediaServerConfig.getInstance().kalturaAPIServerURL);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			if(bWaitResponce){
				conn.setRequestProperty("Accept", "application/json");
			}
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
	
			String urlParameters = String.format("{\"service\":\"liveStream\","+
					"\"method\":\"%s\","+
					"\"hostName\":\"%s\","+
					"\"serverNode\":\"%s\",",
					method, 
					KalturaMediaServerConfig.getInstance().nodeName,
					KalturaMediaServerConfig.getInstance().nodeName	);
			for (Enumeration<String> keys = props.keys() ; keys.hasMoreElements() ;  ){
				String key = keys.nextElement(); 
				urlParameters += String.format("\"%s\":\"%s\",",key,props.get(key));
			}
			urlParameters = urlParameters.substring(0,urlParameters.length()-1) + '}';
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			if(bWaitResponce){
				BufferedReader in = new BufferedReader( new InputStreamReader(conn.getInputStream()));
				String responce = "", line;
				while( (line = in.readLine()) != null){
					responce += line;
				}
				in.close();
				if(responce.matches(".*error:(.*?).*")){
					return false;
				} else {
					return  true;
				}
			} else {
				conn.getInputStream().close();
			}
		}
		catch(Exception e){
			e.printStackTrace();
			WMSLoggerFactory.getLogger(null).warn("reportKalturaEvent: exception " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public void onConnect(IClient client,
            com.wowza.wms.request.RequestFunction function,
            AMFDataList params)
	{
		this.invokePrevious(client, function, params);
	}
	
	public void	onAppStart (IApplicationInstance	 appInstance)
	{
		WMSLoggerFactory.getLogger(null).info("KalturaMediaModule.onAppStart: ");
		Hashtable<String,String> props = new Hashtable<String,String>(); 
		props.put("app",appInstance.getName());
		reportKalturaEvent("appinit",props);
	}

	public void	onAppStop (IApplicationInstance	 appInstance)
	{
		WMSLoggerFactory.getLogger(null).info("KalturaMediaModule.onAppStop: ");
		Hashtable<String,String> props = new Hashtable<String,String>(); 
		props.put("app",appInstance.getName());
		reportKalturaEvent("appstop",props);
	}

	public void onStreamCreate(IMediaStream stream) 
	{
		String streamName = extractStreamName(stream);
		Hashtable<String,String> props = new Hashtable<String,String>(); 
		props.put("streamName",streamName);
		if (reportKalturaEvent(methodAuthenticate,props,true))
		{
			stream.addClientListener((IMediaStreamActionNotify)this);
		}
		else
		{
			sendStreamOnStatusError(stream, "NetStream.Publish.BadName", "Stream authentication failed: "+streamName);
		}
	}

	public void onStreamDestroy(IMediaStream stream)
	{
		stream.removeClientListener((IMediaStreamActionNotify)this);
	}

	//IMediaStreamActionNotify3

	@Override
	public void onMetaData(IMediaStream stream, AMFPacket metaDataPacket)
	{
		WMSLoggerFactory.getLogger(null).info("onMetaData: " + extractStreamName(stream));
	}

	@Override
	public void onPauseRaw(IMediaStream stream, boolean isPause, double location)
	{
		WMSLoggerFactory.getLogger(null).info("onPauseRaw: " + extractStreamName(stream));
	}

	@Override
	public void onPlay(IMediaStream stream, String streamName, double playStart, double playLen, int playReset)
	{
		WMSLoggerFactory.getLogger(null).info("onPlay: " + extractStreamName(stream));
	}

	@Override
	public void onPublish(IMediaStream stream, String sn, boolean isRecord, boolean isAppend)
	{
		String streamName = extractStreamName(stream);
		WMSLoggerFactory.getLogger(null).info("onPublish: " + streamName + " ctxid: " );
		Hashtable<String,String> props = new Hashtable<String,String>(); 
		props.put("streamName",streamName);
		if(!reportKalturaEvent(methodPublish,props))
		{
			sendStreamOnStatusError(stream, "NetStream.Publish.BadName", "Stream publish failed: "+streamName);
		}
	}

	@Override
	public void onUnPublish(IMediaStream stream, String sn, boolean isRecord, boolean isAppend)
	{
		String streamName = extractStreamName(stream);
		WMSLoggerFactory.getLogger(null).info("onUnPublish: " + streamName );
		Hashtable<String,String> props = new Hashtable<String,String>(); 
		props.put("streamName",streamName);
		reportKalturaEvent(methodUnpublish,props);
	}

	@Override
	public void onPause(IMediaStream stream, boolean isPause, double location)
	{
		WMSLoggerFactory.getLogger(null).info("onPause: " + extractStreamName(stream));
	}

	@Override
	public void onSeek(IMediaStream stream, double location)
	{
		WMSLoggerFactory.getLogger(null).info("onSeek: " + extractStreamName(stream));
	}

	@Override
	public void onStop(IMediaStream stream)
	{
		WMSLoggerFactory.getLogger(null).info("onStop: " + extractStreamName(stream));
	}

	@Override
	public void onCodecInfoVideo(IMediaStream stream, MediaCodecInfoVideo codecInfoVideo)
	{
		WMSLoggerFactory.getLogger(null).info("onCodecInfoVideo: " + extractStreamName(stream));
	}

	@Override
	public void onCodecInfoAudio(IMediaStream stream, MediaCodecInfoAudio codecInfoAudio)
	{
		WMSLoggerFactory.getLogger(null).info("onCodecInfoAudio: " + extractStreamName(stream));
	}
}
