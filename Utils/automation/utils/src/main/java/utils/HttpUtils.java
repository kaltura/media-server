package utils;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by asher.saban on 2/17/2015.
 */
public class HttpUtils {

    private static final Logger log = Logger.getLogger(HttpUtils.class);

    /**
     * user should close the httpClient in a finally block
     * @return
     */
    public static CloseableHttpClient getHttpClient() {
        return HttpClients.createDefault();
    }

    public static String doGetRequest(CloseableHttpClient httpClient, String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);

            //TODO who's responsible for errors? client or this object
            if (response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 201) {
                log.error("get request failed. error code: " + response.getStatusLine().getStatusCode());
                return null;
            }

            HttpEntity entity1 = response.getEntity();
            String body = EntityUtils.toString(entity1, "UTF-8");
            EntityUtils.consume(entity1);
            return body;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public static void downloadFile(String url, String destinationPath) throws IOException {
        url = url.trim();
        destinationPath = destinationPath.trim();

        File f = new File(destinationPath);
        URL urlObj = new URL(url);

        log.info("Downloading file: " + url);
        log.info("Destination: " + destinationPath);
        FileUtils.copyURLToFile(urlObj, f);
        log.info("File downloaded successfully");
    }
}
