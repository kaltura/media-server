package com.kaltura.media.quality.encoder.output;


import com.kaltura.client.types.KalturaLiveStreamEntry;
import com.kaltura.media.quality.configurations.OutputConfig;

public class RTMP extends Output {

    public RTMP(String uniqueId, OutputConfig outputConfig) {
        super(uniqueId, outputConfig);
    }
    
    public RTMP(KalturaLiveStreamEntry entry, OutputConfig outputConfig) {
    	super(entry, outputConfig);
    }
    
    public String getStreamName(){
    	return uniqueId + "_%";
    }

    public String getStreamName(int index){
    	return uniqueId + "_" + index;
    }

    public String getUrl() throws Exception{
    	if(outputConfig.getBroadcastingUrl() != null){
    		return outputConfig.getBroadcastingUrl();
    	}
    		
    	if(entry == null){
    		throw new Exception("Entry was not supplied and broadcasting URL was not specified");
    	}
    	
    	if(outputConfig.getPrimary()){
    		return entry.primaryBroadcastingUrl;
    	}
    	return entry.secondaryBroadcastingUrl;
    }

    public String getUrl(int index) throws Exception{
    	return getUrl() + "/" + getStreamName(index);
    }

    @Override
    public String getFfmpegCommandArguments(int index) throws Exception{
    	String command = super.getFfmpegCommandArguments(index);
        
    	command += " -f flv \"" + getUrl(index) + "\"";
        
        return command;
    }
}
