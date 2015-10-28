package configurations;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asher.saban on 3/1/2015.
 */
public class EncoderConfig {

    @JsonProperty("name")
    private String encoderName;

    @JsonProperty("path-to-executable")
    private String pathToExecutable;

    @JsonProperty("args")
    private String args;

    private final Map<String , Object> otherProperties = new HashMap<>();

    @JsonAnySetter
    private void set(String name, Object value) {
        otherProperties.put(name, value);
    }

    public String getEncoderName() {
        return encoderName;
    }

    public String getPathToExecutable() {
        return pathToExecutable;
    }

    public String getArgs() {
        return args;
    }

    public Map<String, Object> getOtherProperties() {
        return otherProperties;
    }

}
