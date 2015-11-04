package com.kaltura.media.quality.encoder.output;

import java.io.File;

import com.kaltura.client.types.KalturaLiveStreamEntry;
import com.kaltura.media.quality.configurations.OutputConfig;

public class Record extends Output {

    public Record(String uniqueId, OutputConfig outputConfig) {
        super(uniqueId, outputConfig);
    }
    
    public Record(KalturaLiveStreamEntry entry, OutputConfig outputConfig) {
    	super(entry, outputConfig);
    }
    
    @Override
    public String getFfmpegCommandArguments(int index) throws Exception{
    	String command = super.getFfmpegCommandArguments(index);

    	String dirPath = config.getDestinationFolder() + "/" + uniqueId + "/";
    	File dir = new File(dirPath);
    	if(!dir.exists()){
    		dir.mkdirs();
    	}
    	String fileName = "recorded.mp4";
    	if(outputConfig.hasOtherProperty("file-name")){
    		fileName = (String) outputConfig.getOtherProperty("file-name");
    	}
    	command += " " + dirPath + fileName;
        
        return command;
    }
}
