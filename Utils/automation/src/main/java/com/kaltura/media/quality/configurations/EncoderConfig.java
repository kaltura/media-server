package com.kaltura.media.quality.configurations;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaltura.media.quality.encoder.Encoder;

/**
 * Created by asher.saban on 3/1/2015.
 */
public class EncoderConfig extends TypedConfig<Encoder> {
    @JsonProperty("path-to-executable")
    private String pathToExecutable;

    @JsonProperty("args")
    private String args = "";

    @JsonProperty("outputs")
	private List<OutputConfig> outputs;

    public String getPathToExecutable() {
        return pathToExecutable;
    }

    public String getArgs() {
        return args;
    }

	public List<OutputConfig> getOutputs() {
		return outputs;
	}
}
