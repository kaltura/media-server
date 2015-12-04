package com.kaltura.media.quality.configurations;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by asher.saban on 3/1/2015.
 */
public class EntryConfig extends Config {
	private static final long serialVersionUID = 4000220616083680845L;

	@JsonProperty("entry-name")
    private String entryName;

    @JsonProperty("is-random")
    private boolean isRandom;

    @JsonProperty("conversion-profile-id")
    private int conversionProfileId;

    @JsonProperty("dvr")
    private boolean dvr;

    @JsonProperty("recording")
    private boolean recording;

    public String getEntryName() {
        return entryName;
    }

    public boolean isRandom() {
        return isRandom;
    }

    public int getConversionProfileId() {
        return conversionProfileId;
    }

    public boolean isDvr() {
        return dvr;
    }

    public boolean isRecording() {
        return recording;
    }
}
