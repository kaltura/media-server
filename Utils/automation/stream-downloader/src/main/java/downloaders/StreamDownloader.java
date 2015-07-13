package downloaders;

/**
 * Created by asher.saban on 2/25/2015.
 */
public interface StreamDownloader {

    public void downloadFiles(String manifestUrl, String destination) throws Exception;
    public void shutdownDownloader();
}
