package com.kaltura.media.quality.encoder.output;

import com.kaltura.client.types.KalturaLiveStreamEntry;
import com.kaltura.media.quality.configurations.OutputConfig;

public class RTSP extends Output {

    public RTSP(String uniqueId, OutputConfig outputConfig) {
        super(uniqueId, outputConfig);
    }
    
    public RTSP(KalturaLiveStreamEntry entry, OutputConfig outputConfig) {
    	super(entry, outputConfig);
    }
    
    public String getStreamName(){
    	return entry.id + "_%";
    }

    public String getStreamName(int index){
    	return entry.id + "_" + index;
    }

    public String getUrl() throws Exception{
    	if(outputConfig.getBroadcastingUrl() != null){
    		return outputConfig.getBroadcastingUrl();
    	}
    		
    	if(entry == null){
    		throw new Exception("Entry was not supplied and broadcasting URL was not specified");
    	}
    	
    	if(outputConfig.getPrimary()){
    		return entry.primaryRtspBroadcastingUrl;
    	}
    	return entry.secondaryRtspBroadcastingUrl;
    }

    public String getUrl(int index) throws Exception{
    	return getUrl() + "/" + getStreamName(index);
    }

    @Override
    public String getFfmpegCommandArguments(int index) throws Exception{
    	String command = super.getFfmpegCommandArguments(index);

    	String transportProtocol = "tcp";
    	if(outputConfig.hasOtherProperty("transport-protocol")){
    		transportProtocol = (String) outputConfig.getOtherProperty("transport-protocol");
    	}
        
    	command += " -f rtsp -rtsp_transport " + transportProtocol + " \"" + getUrl(index) + "\"";
    	
        return command;
    }
}
