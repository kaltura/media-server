package downloaders.hls;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;

import utils.GlobalContext;
import utils.HttpUtils;
import downloaders.StreamDownloader;

/**
 * Created by asher.saban on 2/17/2015.
 */
public class HLSDownloader implements StreamDownloader {

    private static final Logger log = Logger.getLogger(HLSDownloader.class);
    private static final long MANIFEST_DOWNLOAD_TIMEOUT_SEC = 120;
    private List<AbstractMap.SimpleEntry<HLSDownloaderWorker,Thread>> threadsList;


    @Override
    public void shutdownDownloader() {
        //order each downloader to shut down
        for (AbstractMap.SimpleEntry<HLSDownloaderWorker, Thread> e : threadsList) {
            e.getKey().stopDownload();
        }

        //wait for all threads to finish
        for (AbstractMap.SimpleEntry<HLSDownloaderWorker, Thread> e : threadsList) {
            Thread t = e.getValue();
            log.debug("waiting for thread: " + t.getId());
            try {
                t.join();
            } catch (InterruptedException e1) {
                e1.printStackTrace();   //TODO, while loop?
            }
        }
    }

    private Set<String> getStreamsListsFromMasterPlaylist(String masterPlaylist) {
        String[] lines = masterPlaylist.split("\n");
        Set<String> streamsSet = new HashSet<>();

        for (int i=0; i<lines.length; i++) {
            String line = lines[i].trim();
            if (line.startsWith("#EXT-X-STREAM-INF:")) {
//                System.out.println(line);
                int j = i + 1;
                String tempLine = lines[j].trim();
                while (j < lines.length && (tempLine.startsWith("#") || tempLine.equals(""))) {
                    j++;
                    tempLine = lines[j].trim();
                }
                i=j;    //TODO index out of bounds in case there is nothing after #EXT-X-STREAM-INF:

                if (streamsSet.contains(tempLine)) {
                    continue;
                }
                log.info("Adding stream: " + tempLine);
                streamsSet.add(tempLine);
            }
        }
        return streamsSet;
    }

    @Override
    public void downloadFiles(String masterPlaylistUrl, String filesDestination) throws Exception {
        //extract base url:
        String baseUrl = masterPlaylistUrl.substring(0, masterPlaylistUrl.lastIndexOf("/"));

        //get playlist data:

        String masterPlaylistData = getPlaylistData(masterPlaylistUrl);

        log.debug("Master playlist");
        log.debug(masterPlaylistData);

        //save playlist to disk
        String playlistDestination = filesDestination + "/playlist.m3u8";
        FileUtils.writeStringToFile(new File(playlistDestination), masterPlaylistData);
        log.info("Wrote master playlist to: " + playlistDestination);

        //get streams urls:
        Set<String> streamsSet = getStreamsListsFromMasterPlaylist(masterPlaylistData);
        int numStreamFound = streamsSet.size();

        //TODO, put in constant. create global context tags in the future
        GlobalContext.putValue("NUM_STREAMS", numStreamFound);

        log.info("Got total of " + numStreamFound + " streams from master playlist");
        threadsList = new ArrayList<>(numStreamFound);

        //download streams:
        int counter = 0;
        for (String stream : streamsSet) {

            String streamDestination = filesDestination + "/flavor_" + counter;
            //write stream name to file:
            FileUtils.writeStringToFile(new File(streamDestination + "/stream.txt"), stream);

            String playlistUrl;
            //if stream is not a valid URL, concat it to the base URL
            try {
                playlistUrl = new URL(stream).toString();
            } catch (MalformedURLException e) {
                playlistUrl = baseUrl + "/" + stream;
            }

            HLSDownloaderWorker worker = new HLSDownloaderWorker(playlistUrl, streamDestination);
            Thread t = new Thread(worker, stream);
            threadsList.add(new AbstractMap.SimpleEntry<>(worker, t));
            t.start();
            counter++;
        }

    }

    private String getPlaylistData(String masterPlaylistUrl) throws Exception {

        log.info("Downloading playlist from URL: " + masterPlaylistUrl + " . timeout in seconds: " + MANIFEST_DOWNLOAD_TIMEOUT_SEC);

        long timeoutInMillis = MANIFEST_DOWNLOAD_TIMEOUT_SEC * 1000;
        long startTime = System.currentTimeMillis();
        long currentTime = startTime;

        CloseableHttpClient client = null;
        try {
            client = HttpUtils.getHttpClient();
            String masterPlaylistData;
            while ((currentTime - startTime) <= timeoutInMillis) {
                try {
                    masterPlaylistData = HttpUtils.doGetRequest(client, masterPlaylistUrl);
                    if (masterPlaylistData != null) {
                        log.info("Downloaded playlist manifest after " + (currentTime - startTime) + " milliseconds");
                        return masterPlaylistData;
                    }

                    //wait 5 seconds before next download attempt
                    log.info("Failed to download playlist. waiting additional 5 seconds");  //TODO magic numbers
                    currentTime = System.currentTimeMillis();
                    Thread.sleep(5000);

                } catch (UnknownHostException ex) {
                	System.err.println("Host not found, no point in retrying. host name: " + ex.getMessage());
                	break;
                } catch (IOException | InterruptedException e) {
                	System.err.println("Failed to download manifest: " + masterPlaylistUrl);
                    e.printStackTrace();
                }
            }
            throw new Exception("Failed to download playlist manifest after " + (currentTime - startTime) + " milliseconds");

        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

	@Override
	public boolean isAlive() {
		// While there is at least one thread alive - the HLS downloader is alive.
		for (SimpleEntry<HLSDownloaderWorker, Thread> threadItr : threadsList) {
			if(threadItr.getValue().isAlive())
				return true;
		}
		return false;
	}
}
