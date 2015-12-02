package com.kaltura.media.quality.provider.hls;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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
import com.kaltura.media.quality.event.Event;
import com.kaltura.media.quality.event.EventsManager;
import com.kaltura.media.quality.event.listener.IListener;
import com.kaltura.media.quality.event.listener.ISegmentListener;
import com.kaltura.media.quality.event.listener.ISegmentsListener;
import com.kaltura.media.quality.model.Rendition;
import com.kaltura.media.quality.model.Segment;
import com.kaltura.media.quality.provider.Provider;
import com.kaltura.media.quality.provider.hls.enhancer.PlaylistEnhancer;
import com.kaltura.media.quality.provider.url.UrlBuilder;
import com.kaltura.media.quality.utils.HttpUtils;
import com.kaltura.media.quality.utils.ThreadManager;

/**
 * Downloads all renditions manifests and start new rendition downloader for each of them
 */
public class Downloader extends Provider implements ISegmentListener {

	private static final long serialVersionUID = 5232197354276259655L;
	private static final Logger log = Logger.getLogger(Downloader.class);
	private static final long MANIFEST_DOWNLOAD_TIMEOUT_SEC = 120;

	private List<PlaylistEnhancer> enhancers = new ArrayList<PlaylistEnhancer>();
	
	protected String downloadDir;
	protected URI masterPlaylistUrl;
	protected String uniqueId;
	private boolean deffered = false;;
	
	private Map<String, Integer> downloadersCount = null;
	private transient Map<String, Map<Integer, ExpectedSegment>> expectedSegments = null;

	public Downloader(){
		super();
	}
	
	public Downloader(String uniqueId, DataProvider providerConfig) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, URISyntaxException {
		this();
		
		this.uniqueId = uniqueId;
		if(providerConfig.hasOtherProperty("deffered")){
			this.deffered = (boolean) providerConfig.getOtherProperty("deffered");
		}
		
		UrlBuilder urlBuilder = providerConfig.getUrlBuilder();
		this.masterPlaylistUrl = urlBuilder.build(uniqueId);
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
		
		register();
	}
	
	@Override
	public void register() {
		EventsManager.get().addListener(ISegmentListener.class, this);
	}

	class ExpectedSegment{

		private String domainHash;
		private List<Segment> segments = new ArrayList<Segment>();
		private long created = System.currentTimeMillis();
		
		public ExpectedSegment(String domainHash){
			this.domainHash = domainHash;
		}

		public void addSegment(Segment segment) {
			segments.add(segment);
		}

		public boolean complete() {
			int compeleted = segments.size();
			return downloadersCount.containsKey(domainHash) && compeleted >= downloadersCount.get(domainHash);
		}

		public List<Segment> getSegments() {
			return segments;
		}

		public boolean expired() {
			if(deffered){
				return false;
			}
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
		return getClass().getSimpleName() + "-" + uniqueId;
	}

	private Set<Rendition> getStreamsListsFromMasterPlaylist(String masterPlaylist) {
		String[] lines = masterPlaylist.split("\n");
		String[] lineParts;
		String[] renditionInfo;
		String[] renditionInfoParts;
		Set<Rendition> renditions = new HashSet<>();
		Rendition rendition;

		for (int i = 0; i < lines.length; i++) {
			String line = lines[i].trim();
			if (line.startsWith("#EXT-X-STREAM-INF:")) {
				rendition = new Rendition(uniqueId);
				lineParts = line.split(":", 2);
				renditionInfoParts = lineParts[1].split(",");
				for(String renditionInfoItem : renditionInfoParts){
					renditionInfo = renditionInfoItem.split("=", 2);
					if(renditionInfo[0].equals("PROGRAM-ID")){
						rendition.setProgramId(Integer.parseInt(renditionInfo[1]));
					}
					if(renditionInfo[0].equals("BANDWIDTH")){
						rendition.setBandwidth(Integer.parseInt(renditionInfo[1]));
					}
					if(renditionInfo[0].equals("RESOLUTION")){
						rendition.setResolution(renditionInfo[1]);
					}
				}
				int j = i + 1;
				String tempLine = lines[j].trim();
				while (j < lines.length && (tempLine.startsWith("#") || tempLine.equals(""))) {
					j++;
					tempLine = lines[j].trim();
				}
				i = j; // TODO index out of bounds in case there is nothing
						// after #EXT-X-STREAM-INF:

				rendition.setUrl(tempLine);
				log.info("Adding stream: " + tempLine);
				renditions.add(rendition);
			}
		}
		return renditions;
	}

	public void downloadFiles() throws Exception {
		downloadersCount = new ConcurrentHashMap<String, Integer>();
		
		// get playlist data:
		String masterPlaylistData = getPlaylistData();

		log.debug("Master playlist");
		log.debug(masterPlaylistData);

		// save playlist to disk
		String playlistDestination = downloadDir + "/playlist.m3u8";
		FileUtils.writeStringToFile(new File(playlistDestination), masterPlaylistData);
		log.info("Wrote master playlist to: " + playlistDestination);

		// get streams urls:
		Set<Rendition> renditions = getStreamsListsFromMasterPlaylist(masterPlaylistData);
		for(PlaylistEnhancer enhancer : enhancers) {
			renditions = enhancer.enhanceStreamsSet(renditions);
		}

		int numStreamFound = renditions.size();
		log.info("Got total of " + numStreamFound + " streams from master playlist");

		String stream;
		String domainHash;
		String playlistFileName;
		String streamDestination;
		URI playlistUrl;
		Integer domainDownloadersCount;
		
		// download streams:
		for (Rendition rendition : renditions) {

			stream = rendition.getUrl();
			domainHash = rendition.getDomainHash();
			playlistFileName = stream.substring(stream.lastIndexOf("/") + 1);
			streamDestination = downloadDir + "/" + playlistFileName + "/" + domainHash;
			
			// write stream name to file:
			FileUtils.writeStringToFile(new File(streamDestination + "/stream.txt"), stream);

			// if stream is not a valid URL, concat it to the base URL
			try {
				playlistUrl = new URL(stream).toURI();
			} catch (MalformedURLException e) {
				playlistUrl = masterPlaylistUrl.resolve(stream);
			}

			domainDownloadersCount = 1;
			if(downloadersCount.containsKey(domainHash)){
				domainDownloadersCount = downloadersCount.get(domainHash);
				domainDownloadersCount +=1;
			}
			downloadersCount.put(domainHash, domainDownloadersCount);
			
			RenditionDownloader worker = new RenditionDownloader(playlistUrl, streamDestination, rendition);
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
	public void onSegmentDownloadComplete(Segment segment) {
		if(!segment.getEntryId().equals(uniqueId)){
			return;
		}
		
		if(expectedSegments == null){
			expectedSegments = new ConcurrentHashMap<String, Map<Integer, ExpectedSegment>>();
		}
		
		String domainHash = segment.getRendition().getDomainHash();

		Map<Integer, ExpectedSegment> domainExpectedSegment;
		if(expectedSegments.containsKey(domainHash)){
			domainExpectedSegment = expectedSegments.get(domainHash);
		}
		else{
			domainExpectedSegment = new ConcurrentHashMap<Integer, ExpectedSegment>();
			expectedSegments.put(domainHash, domainExpectedSegment);
		}

		ExpectedSegment expectedSegment;
		if(domainExpectedSegment.containsKey(segment.getNumber())){
			expectedSegment = domainExpectedSegment.get(segment.getNumber());
		}
		else{
			expectedSegment = new ExpectedSegment(domainHash);
			domainExpectedSegment.put(segment.getNumber(), expectedSegment);
		}
		expectedSegment.addSegment(segment);
		
		if(expectedSegment.complete()){
			onSegmentsDownloadComplete(expectedSegment.getSegments());
			expectedSegments.remove(segment.getNumber());
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

	class SegmentsDownloadCompleteEvent extends Event<ISegmentsListener>{
		private static final long serialVersionUID = 8289169486072160080L;
		private List<Segment> segments;
		
		public SegmentsDownloadCompleteEvent(List<Segment> segments) {
			super(ISegmentsListener.class);
			
			this.segments = segments;
		}

		@Override
		protected void callListener(ISegmentsListener listener) {
			listener.onSegmentsDownloadComplete(segments);
		}

		@Override
		protected String getTitle() {
			Segment segment = segments.get(0);
			String title = uniqueId;
			title += "-" + segment.getRendition().getDomainHash();
			title += "-" + segment.getRendition().getBandwidth();
			title += "-" + segment.getNumber();
			return title;
		}
	}
	
	protected void onSegmentsDownloadComplete(List<Segment> segments) {
		EventsManager.get().raiseEvent(new SegmentsDownloadCompleteEvent(segments));
	}

	@Override
	public int compareTo(IListener o) {
		if(o == this){
			return 0;
		}
		return 1;
	}

	@Override
	public boolean isDeffered() {
		return deffered;
	}

	@Override
	public String getTitle() {
		return uniqueId;
	}
}
