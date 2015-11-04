package com.kaltura.media.quality.configurations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asher.saban on 2/22/2015.
 */
public class TestConfig extends Config {

    @JsonProperty("test-name")
    private String testName;

    @JsonProperty("admin-secret")
    private String adminSecret;

    @JsonProperty("stream-type")
    private String streamType;

    @JsonProperty("encoder")
    private EncoderConfig encoder;

    @JsonProperty("test-duration")
    private int testDuration = Integer.MIN_VALUE;

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

    @JsonProperty("data-providers")
    private List<DataProvider> dataProviders = new ArrayList<DataProvider>();

    @JsonProperty("data-validators")
	private List<DataValidator> dataValidators = new ArrayList<DataValidator>();

    private static TestConfig config = null;
    
    public static TestConfig init(String[] args) throws Exception{
    	String path = null; 
		if (args.length == 1) {
			path = args[0];
		}
		else{
			path = "test-conf.json";
		}
		File file = new File(path);
		
		if(!file.exists())
		{
			//load the default conf file
			URL url = TestConfig.class.getResource("/" + path);
			if (url == null) {
				throw new Exception("Configuration file test-conf.json not found.");
			}
			path = url.getPath();
			file = new File(path);
		}

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
        config = mapper.readValue(file, TestConfig.class);

        return config;
    }

    public static TestConfig get() {
        return config;
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

	public List<DataProvider> getDataProviders() {
		return dataProviders;
	}

	public List<DataValidator> getDataValidators() {
		return dataValidators;
	}
}
