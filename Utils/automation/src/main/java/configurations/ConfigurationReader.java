package configurations;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Created by asher.saban on 2/22/2015.
 */
public class ConfigurationReader {

    /**
     * Deserialize the given json file into a TestConfig object.
     * @param pathToJson path to json file.
     * @return TestConfig object.
     * @throws java.io.IOException
     */
    public static TestConfig getTestConfigurations(String pathToJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper.readValue(new File(pathToJson), TestConfig.class);
    }
}
