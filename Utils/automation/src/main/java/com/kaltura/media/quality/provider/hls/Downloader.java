package com.kaltura.media.quality.provider.hls;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;

import com.kaltura.media.quality.configurations.DataProvider;
import com.kaltura.media.quality.configurations.PlaylistEnhancerConfig;
import com.kaltura.media.quality.event.IListener;
import com.kaltura.media.quality.event.ISegmentListener;
import com.kaltura.media.quality.event.ISegmentsListener;
import com.kaltura.media.quality.provider.Provider;
import com.kaltura.media.quality.provider.hls.enhancer.PlaylistEnhancer;
import com.kaltura.media.quality.utils.HttpUtils;
import com.kaltura.media.quality.utils.StringUtils;
import com.kaltura.media.quality.utils.ThreadManager;

/**
 * Downloads all renditions manifests and start new rendition downloader for each of them
 */
public class Downloader extends Provider implements ISegmentListener {

	private static final Logger log = Logger.getLogger(Downloader.class);
	private static final long MANIFEST_DOWNLOAD_TIMEOUT_SEC = 120;

	private List<PlaylistEnhancer> enhancers = new ArrayList<PlaylistEnhancer>();
	
	protected String downloadDir;
	protected URI masterPlaylistUrl;
	private Map<String, Integer> downloadersCount = new ConcurrentHashMap<String, Integer>();
	private Map<String, Map<Integer, ExpectedSegment>> expectedSegments = new ConcurrentHashMap<String, Map<Integer, ExpectedSegment>>();

	public Downloader(String uniqueId, URI playlistUrl, DataProvider providerConfig) {
		super();
		
		this.masterPlaylistUrl = playlistUrl;
		this.downloadDir = config.getDestinationFolder() + "/" + uniqueId;

		PlaylistEnhancer enhancer;
		for(PlaylistEnhancerConfig enhancerConfig : providerConfig.getPlaylistEnhancers()){
			try {
				enhancer = enhancerConfig.getType().newInstance();
				enhancers.add(enhancer);
			} catch (InstantiationException | IllegalAccessException e) {
				log.error(e.getMessage(), e); 
			}
		}
	}

	class ExpectedSegment{

		private String domainHash;
		private int expected = 0;
		private List<File> segments = new ArrayList<File>();
		private long created = System.currentTimeMillis();
		
		public ExpectedSegment(String domainHash){
			this.domainHash = domainHash;
		}

		public void increment() {
			expected ++;			
		}

		public void addSegment(File segment) {
			segments.add(segment);
		}

		public boolean complete() {
			int compeleted = segments.size();
			return compeleted >= expected && downloadersCount.containsKey(domainHash) && compeleted >= downloadersCount.get(domainHash);
		}

		public List<File> getSegments() {
			return segments;
		}

		public boolean expired() {
			return System.currentTimeMillis() > (created + (1000 * 600));
		}
		
	}
	
	@Override
	public void run() {
		Thread thread = Thread.currentThread();
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.setName(getThreadName());
		
		try {
			downloadFiles();
		} catch (Exception e) {
			System.out.println("Failed to download content");
			log.error(e.getMessage(), e);
		}
	}
	
	protected String getThreadName() {
		return getClass().getSimpleName() + "-" + masterPlaylistUrl;
	}

	private Set<String> getStreamsListsFromMasterPlaylist(String masterPlaylist) {
		String[] lines = masterPlaylist.split("\n");
		Set<String> streamsSet = new HashSet<>();

		for (int i = 0; i < lines.length; i++) {
			String line = lines[i].trim();
			if (line.startsWith("#EXT-X-STREAM-INF:")) {
				// System.out.println(line);
				int j = i + 1;
				String tempLine = lines[j].trim();
				while (j < lines.length && (tempLine.startsWith("#") || tempLine.equals(""))) {
					j++;
					tempLine = lines[j].trim();
				}
				i = j; // TODO index out of bounds in case there is nothing
						// after #EXT-X-STREAM-INF:

				if (streamsSet.contains(tempLine)) {
					continue;
				}
				log.info("Adding stream: " + tempLine);
				streamsSet.add(tempLine);
			}
		}
		return streamsSet;
	}

	public void downloadFiles() throws Exception {
		// get playlist data:
		String masterPlaylistData = getPlaylistData();

		log.debug("Master playlist");
		log.debug(masterPlaylistData);

		// save playlist to disk
		String playlistDestination = downloadDir + "/playlist.m3u8";
		FileUtils.writeStringToFile(new File(playlistDestination), masterPlaylistData);
		log.info("Wrote master playlist to: " + playlistDestination);

		// get streams urls:
		Set<String> streamsSet = getStreamsListsFromMasterPlaylist(masterPlaylistData);
		for(PlaylistEnhancer enhancer : enhancers) {
			streamsSet = enhancer.enhanceStreamsSet(streamsSet);
		}

		int numStreamFound = streamsSet.size();
		log.info("Got total of " + numStreamFound + " streams from master playlist");

		// download streams:
		for (String stream : streamsSet) {

			String playlistFileName = stream.substring(stream.lastIndexOf("/") + 1);
			URI streamUrl = new URI(stream).resolve("../../");
			String domainHash = StringUtils.md5(streamUrl.toString());
			String streamDestination = downloadDir + "/" + playlistFileName + "/" + domainHash;
			// write stream name to file:
			FileUtils.writeStringToFile(new File(streamDestination + "/stream.txt"), stream);

			URI playlistUrl;
			// if stream is not a valid URL, concat it to the base URL
			try {
				playlistUrl = new URL(stream).toURI();
			} catch (MalformedURLException e) {
				playlistUrl = masterPlaylistUrl.resolve(stream);
			}

			Integer domainDownloadersCount = 1;
			if(downloadersCount.containsKey(domainHash)){
				domainDownloadersCount = downloadersCount.get(domainHash);
			}
			downloadersCount.put(domainHash, domainDownloadersCount);
			
			RenditionDownloader worker = new RenditionDownloader(playlistUrl, streamDestination, domainHash);
			worker.addSegmentListener(this);
			worker.start();
		}
	}

	private String getPlaylistData() throws Exception {

		log.info("Downloading playlist from URL: " + masterPlaylistUrl + " . timeout in seconds: " + MANIFEST_DOWNLOAD_TIMEOUT_SEC);

		CloseableHttpClient client = null;
		try {
			client = HttpUtils.getHttpClient();
			String masterPlaylistData;
			while (ThreadManager.shouldContinue()) {
				try {
					masterPlaylistData = HttpUtils.doGetRequest(client, masterPlaylistUrl.toString());
					if (masterPlaylistData != null) {
						log.info("Downloaded playlist manifest");
						return masterPlaylistData;
					}

					// wait 5 seconds before next download attempt
					log.info("Failed to download playlist. waiting additional 5 seconds");
					Thread.sleep(5000);

				} catch (UnknownHostException ex) {
					System.err.println("Host not found, no point in retrying. host name: " + ex.getMessage());
					break;
				} catch (IOException e) {
					System.err.println("Failed to download manifest: " + masterPlaylistUrl);
					log.error(e.getMessage(), e); 
				} catch (InterruptedException e) {
					break;
				}
			}
			throw new Exception("Failed to download playlist manifest");

		} finally {
			if (client != null) {
				client.close();
			}
		}
	}
	
	@Override
	public void onSegmentDownloadStart(int segmentNumber, String domainHash) {
		Map<Integer, ExpectedSegment> domainExpectedSegment;
		if(expectedSegments.containsKey(domainHash)){
			domainExpectedSegment = expectedSegments.get(domainHash);
		}
		else{
			domainExpectedSegment = new ConcurrentHashMap<Integer, ExpectedSegment>();
			expectedSegments.put(domainHash, domainExpectedSegment);
		}
		
		if(domainExpectedSegment.containsKey(segmentNumber)){
			ExpectedSegment expectedSegment = domainExpectedSegment.get(segmentNumber);
			expectedSegment.increment();
		}
		else{
			domainExpectedSegment.put(segmentNumber, new ExpectedSegment(domainHash));
		}
	}
	
	@Override
	public void onSegmentDownloadComplete(int segmentNumber, String domainHash, File segment) {
		if(!expectedSegments.containsKey(domainHash)){
			return;
		}
		
		Map<Integer, ExpectedSegment> domainExpectedSegment = expectedSegments.get(domainHash);
		if(!domainExpectedSegment.containsKey(segmentNumber)){
			return;
		}

		ExpectedSegment expectedSegment = domainExpectedSegment.get(segmentNumber);
		expectedSegment.addSegment(segment);
		
		if(expectedSegment.complete()){
			onSegmentsDownloadComplete(segmentNumber, expectedSegment.getSegments());
			expectedSegments.remove(segmentNumber);
		}
		
		cleanupOldSegments();
	}

	private void cleanupOldSegments() {
		ExpectedSegment expectedSegment;
		for(Map<Integer, ExpectedSegment> domainExpectedSegment : expectedSegments.values()){
			for(int segmentNumber : domainExpectedSegment.keySet()){
				expectedSegment = domainExpectedSegment.get(segmentNumber);
				if(expectedSegment.expired()){
					log.error("Segment [" + segmentNumber + "] not all renditions downloaded");
					expectedSegments.remove(segmentNumber);
				}
			}
		}
	}

	protected void onSegmentsDownloadComplete(int segmentNumber, List<File> segments) {
		for(ISegmentsListener listener : getListeners(ISegmentsListener.class)){
			listener.onSegmentsDownloadComplete(segmentNumber, segments);
		}
	}

	@Override
	public int compareTo(IListener o) {
		if(o == this){
			return 0;
		}
		return 1;
	}
}
