package com.kaltura.media.quality.encoder;

import java.lang.reflect.Constructor;

import com.kaltura.client.types.KalturaLiveStreamEntry;
import com.kaltura.media.quality.configurations.EncoderConfig;
import com.kaltura.media.quality.configurations.OutputConfig;
import com.kaltura.media.quality.encoder.output.Output;

public class FFMPEG extends Encoder {

    public FFMPEG(String uniqueId, EncoderConfig encoderConfig) {
        super(uniqueId, encoderConfig);
    }
    
    public FFMPEG(KalturaLiveStreamEntry entry, EncoderConfig encoderConfig) {
		super(entry, encoderConfig);
	}

    @Override
    protected String getCommandLineArguments() throws Exception {
        String command = " " + encoderConfig.getArgs();

        int index = 1;
        Output output = null;
        for(OutputConfig outputConfig : encoderConfig.getOutputs()){
        	if(entry != null){
				Constructor<Output> constructor = outputConfig.getType().getConstructor(KalturaLiveStreamEntry.class, OutputConfig.class);
				output = constructor.newInstance(entry, outputConfig);
        	}
        	else{
				Constructor<Output> constructor = outputConfig.getType().getConstructor(String.class, OutputConfig.class);
				output = constructor.newInstance(uniqueId, outputConfig);
        	}
			command += " " + output.getFfmpegCommandArguments(index);
			index++;
        }
        
        return command;
    }
}
