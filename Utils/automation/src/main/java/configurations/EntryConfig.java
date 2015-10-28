package configurations;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asher.saban on 3/1/2015.
 */
public class EntryConfig {

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

    private final Map<String , Object> otherProperties = new HashMap<>();

    @JsonAnySetter
    private void set(String name, Object value) {
        otherProperties.put(name, value);
    }

    public Map<String, Object> getOtherProperties() {
        return otherProperties;
    }

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
