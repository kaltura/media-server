package downloaders;

import tasks.StatusWatcher;

/**
 * Created by asher.saban on 2/25/2015.
 */
public interface StreamDownloader extends StatusWatcher {

    public void downloadFiles(String manifestUrl, String destination) throws Exception;
}
