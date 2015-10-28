package downloaders;

import downloaders.hls.HLSDownloader;

/**
 * Created by asher.saban on 3/1/2015.
 */
public class StreamDownloaderFactory {

    private static final String HLS = "HLS";
    private static final String HDS = "HDS";

    public static StreamDownloader getDownloader(String type) {
        type = type.toUpperCase();
        switch (type) {
            case HLS:
                return new HLSDownloader();
            case HDS:
                return null;    //new HDSDownloader();
            default:
                throw new IllegalArgumentException("Stream downloader of type: '" + type + "' is not supported");
        }
    }
}
