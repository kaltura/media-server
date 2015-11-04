package com.kaltura.media.quality.encoder;

import com.kaltura.client.types.KalturaLiveStreamEntry;
import com.kaltura.media.quality.configurations.EncoderConfig;

public class FMLE extends Encoder {

    public FMLE(String uniqueId, EncoderConfig encoderConfig) {
        super(uniqueId, encoderConfig);
    }
    
    public FMLE(KalturaLiveStreamEntry entry, EncoderConfig encoderConfig) {
		super(entry, encoderConfig);
	}

    @Override
    protected String getCommandLineArguments() {
    	// TODO create XML
    	
        String command = encoderConfig.getArgs();
        
        return command;
    }
}
