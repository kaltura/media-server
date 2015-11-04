import org.apache.http.impl.client.CloseableHttpClient;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.kaltura.media.quality.utils.HttpUtils;

import java.io.IOException;

/**
 * Created by asher.saban on 2/24/2015.
 */
public class HttpUtilsTest {

    @Test
    public static void requestShouldSucceed() throws Exception {
        CloseableHttpClient client = null;
        try {
            client = HttpUtils.getHttpClient();
            String response = HttpUtils.doGetRequest(client, "http://www.ynet.co.il/");
            Assert.assertNotNull(response);
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {

                }
            }
        }
    }

    @Test
    public static void requestShouldFail() throws Exception {
        CloseableHttpClient client = null;
        try {
            client = HttpUtils.getHttpClient();
            String response = HttpUtils.doGetRequest(client, "http://www.nosuchwebsite.com");
            Assert.assertNull(response);
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {

                }
            }
        }
    }
}
