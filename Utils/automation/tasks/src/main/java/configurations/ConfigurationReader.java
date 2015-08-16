package configurations;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by asher.saban on 2/22/2015.
 */
public class ConfigurationReader {

    /**
     * Deserialize the given json file into a TestConfig object.
     * @param is path to json file.
     * @return TestConfig object.
     * @throws java.io.IOException
     */
    public static TestConfig getTestConfigurations(InputStream is) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper.readValue(is, TestConfig.class);
    }

    public static TestConfig getTestConfigurationFromResource(String resourceName)
            throws Exception {
        InputStream u = ConfigurationReader.class.getResourceAsStream("/" + resourceName);
        if (u == null) {
            throw new Exception("Configuration file: " + resourceName
                    + " not found.");
        }
        return getTestConfigurations(u);
    }

    public static TestConfig getTestConfigurations(File confFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper.readValue(confFile, TestConfig.class);
    }
}
