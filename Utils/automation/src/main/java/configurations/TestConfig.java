package configurations;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asher.saban on 2/22/2015.
 */
public class TestConfig {

    @JsonProperty("test-name")
    private String testName;

    @JsonProperty("admin-secret")
    private String adminSecret;

    @JsonProperty("stream-type")
    private String streamType;

    @JsonProperty("encoder")
    private EncoderConfig encoder;

    @JsonProperty("test-duration")
    private int testDuration;

    @JsonProperty("path-to-ffmpeg")
    private String pathToFfmpeg;

    @JsonProperty("destination-folder")
    private String destinationFolder;

    @JsonProperty("delete-files")
    private boolean deleteFiles;

    @JsonProperty("service-url")
    private String ServiceUrl;

    @JsonProperty("partner-id")
    private String partnerId;

    @JsonProperty("entry-details")
    private EntryConfig entryDetails;
    
    @JsonProperty("sync-entries")
    private List<String> syncEntries;

    private final Map<String , Object> otherProperties = new HashMap<>();

    @JsonAnySetter
    private void set(String name, Object value) {
        otherProperties.put(name, value);
    }

    public String getTestName() {
        return testName;
    }

    public EncoderConfig getEncoder() {
        return encoder;
    }

    public int getTestDuration() {
        return testDuration;
    }

    public String getPathToFfmpeg() {
        return pathToFfmpeg;
    }

    public String getDestinationFolder() {
        return destinationFolder;
    }

    public String getStreamType() {
        return streamType;
    }

    public boolean isDeleteFiles() {
        return deleteFiles;
    }

    public Map<String, Object> getOtherProperties() {
        return otherProperties;
    }

    public String getServiceUrl() {
        return ServiceUrl;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public EntryConfig getEntryDetails() {
        return entryDetails;
    }

    public String getAdminSecret() {
        return adminSecret;
    }

	public List<String> getSyncEntries() {
		return syncEntries;
	}
}
