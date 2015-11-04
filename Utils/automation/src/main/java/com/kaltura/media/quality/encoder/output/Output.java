package com.kaltura.media.quality.encoder.output;

import com.kaltura.client.types.KalturaLiveStreamEntry;
import com.kaltura.media.quality.configurations.OutputConfig;
import com.kaltura.media.quality.configurations.TestConfig;
import com.kaltura.media.quality.utils.ThreadManager;

public abstract class Output {

    protected static TestConfig config = null;

	protected String uniqueId;
    protected KalturaLiveStreamEntry entry;
	protected OutputConfig outputConfig;


    public Output(String uniqueId, OutputConfig outputConfig) {
        this.uniqueId = uniqueId;
        this.outputConfig = outputConfig;
        
        if(config == null){
        	config = TestConfig.get();
        }
    }

    public Output(KalturaLiveStreamEntry entry, OutputConfig outputConfig) {
    	this(entry.id, outputConfig);
        this.entry = entry;
    }
    
    public String getFfmpegCommandArguments(int index) throws Exception{
    	String command = outputConfig.getArgs();
    	
        if(config.getTestDuration() > 0){
        	command += " -t " + (ThreadManager.leftTimeToRun() / 1000);
        }

        if(outputConfig.getFramerate() != null){
        	command += " -framerate " + outputConfig.getFramerate();
        }

        if(outputConfig.getVideoCodec() != null){
        	command += " -vcodec " + outputConfig.getVideoCodec();
        }

        if(outputConfig.getKeyframeInterval() != null){
        	command += " -x264opts \"keyint=" + outputConfig.getKeyframeInterval() + "\"";
        }

        if(outputConfig.getPreset() != null){
        	command += " -preset " + outputConfig.getPreset();
        }

        if(outputConfig.getAudioCodec() != null){
        	command += " -acodec " + outputConfig.getAudioCodec();
        }

        if(outputConfig.getResolution() != null){
	        command += " -vf scale=" + outputConfig.getResolution();
        }

        if(outputConfig.getVideoBitrate() != null){
	        command += " -b:v " + outputConfig.getVideoBitrate() + "k";
        }

        if(outputConfig.getAudioBitrate() != null){
	        command += " -b:a " + outputConfig.getAudioBitrate() + "k";
        }
        
        return command;
    }
}
