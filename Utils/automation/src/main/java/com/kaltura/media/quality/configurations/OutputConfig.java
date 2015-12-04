package com.kaltura.media.quality.configurations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaltura.media.quality.encoder.output.Output;

public class OutputConfig extends TypedConfig<Output> {
	private static final long serialVersionUID = 1108949855670165144L;

	@JsonProperty("path-to-executable")
    private String pathToExecutable;

    @JsonProperty("args")
    private String args = "";

    @JsonProperty("primary")
    private boolean primary;

    @JsonProperty("framerate")
	private Integer framerate;

    @JsonProperty("keyframe-interval")
	private Integer keyframeInterval;

    @JsonProperty("video-codec")
	private String videoCodec;

    @JsonProperty("preset")
	private String preset;

    @JsonProperty("audio-codec")
	private String audioCodec;

    @JsonProperty("resolution")
	private String resolution;

    @JsonProperty("video-bitrate")
	private Integer videoBitrate;

    @JsonProperty("audio-bitrate")
	private Integer audioBitrate;

    @JsonProperty("broadcasting-url")
	private String broadcastingUrl;

    public String getArgs() {
        return args;
    }

	public boolean getPrimary() {
		return primary;
	}

	public Integer getFramerate() {
		return framerate;
	}

	public Integer getKeyframeInterval() {
		return keyframeInterval;
	}

	public String getVideoCodec() {
		return videoCodec;
	}

	public String getPreset() {
		return preset;
	}

	public String getAudioCodec() {
		return audioCodec;
	}

	public String getResolution() {
		return resolution;
	}

	public Integer getVideoBitrate() {
		return videoBitrate;
	}

	public Integer getAudioBitrate() {
		return audioBitrate;
	}

	public String getBroadcastingUrl() {
		return broadcastingUrl;
	}
}
