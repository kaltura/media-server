package com.kaltura.media.quality.provider.hls;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;

import com.kaltura.media.quality.event.Event;
import com.kaltura.media.quality.event.EventsManager;
import com.kaltura.media.quality.event.listener.ISegmentListener;
import com.kaltura.media.quality.model.Rendition;
import com.kaltura.media.quality.model.Segment;
import com.kaltura.media.quality.provider.Provider;
import com.kaltura.media.quality.utils.HttpUtils;
import com.kaltura.media.quality.utils.ThreadManager;

/**
 * Created by asher.saban on 2/17/2015.
 */
class RenditionDownloader extends Provider implements Serializable {
	private static final long serialVersionUID = -8749958640311821000L;
	private static final Logger log = Logger.getLogger(RenditionDownloader.class);
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	private final URI url;
	private final String destinationPath;
	private Rendition rendition;
	private int lastTsNumber;
	private boolean runForever = false;
	private String tempPath;

	class SegmentDownloadCompleteEvent extends Event<ISegmentListener>{
		private static final long serialVersionUID = 2452191648704267709L;
		protected Segment segment;

		public SegmentDownloadCompleteEvent(Segment segment) {
			super(ISegmentListener.class);
			this.segment = segment;
		}

		@Override
		protected void callListener(ISegmentListener listener) {
			listener.onSegmentDownloadComplete(segment);
		}

		@Override
		protected String getTitle() {
			String title = segment.getEntryId();
			title += "-" + segment.getRendition().getDomainHash();
			title += "-" + segment.getRendition().getBandwidth();
			title += "-" + segment.getNumber();
			return title;
		}
	}
	
	public RenditionDownloader(URI url, String destinationPath, Rendition rendition) {
		this.url = url;
		this.destinationPath = destinationPath;
		this.rendition = rendition;
		this.lastTsNumber = -1;
		this.tempPath = dateFormat.format(new Date());
	}

	@Override
	public void run() {
		Thread thread = Thread.currentThread();
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.setName(getClass().getSimpleName() + "-" + url);
		
		log.info("Downloading stream: " + url);

		int counter = 0;
		while (ThreadManager.shouldContinue()) {

			counter++;
			log.debug("iteration: " + counter);
			
			//download m3u8:
			CloseableHttpClient httpClient = HttpUtils.getHttpClient();
			String content;
			try {
				content = HttpUtils.doGetRequest(httpClient, url.toString());

				//write to file
				String reportDate = dateFormat.format(new Date());
				FileUtils.writeStringToFile(new File(destinationPath + "/" + tempPath + "/iter_" + counter + "_" + reportDate + ".m3u8"),content);

			} catch (Exception e) {
				log.error(e.getMessage()); 
				continue; //TODO raise event
			}

            if(content == null) {
            	if(canHandleNullContent()) {
            		continue;
            	} else {
            		return;
            	}
            }
			
			long downloadTime = System.currentTimeMillis();
			long duration = 0;
			double tsDuration = 0;

			//get duration: search #EXT-X-TARGETDURATION
			String[] lines = content.split("\n");
			int numLines = lines.length;

			for (int i = 0; i < numLines; i++) {
				if(!ThreadManager.shouldContinue()){
					return;
				}
				String line = lines[i];
				log.debug(line);
				if (line.startsWith("#EXT-X-TARGETDURATION:")) {
					duration = Long.valueOf(line.substring("#EXT-X-TARGETDURATION:".length()).trim());
				}
				//a .ts file
				else if (line.startsWith("#EXTINF:")) {
					tsDuration = Double.valueOf(line.substring("#EXTINF:".length()).trim().replace(",", ""));
					//extract ts file:
					int j = i + 1;
					while (j < lines.length && (lines[j].startsWith("#") || lines[j].equals("")) && !lines[j].trim().endsWith(".ts")) {
						j++;
					}

					String tsName = lines[j];
					log.debug("Found .ts file: " + tsName);

					//parse ts address to relative address and file name:
					File ts = new File(tsName);
					String fileName = ts.getName();

					//get the ts number:
					int tsNumber;
					try {
						tsNumber = getTsNumber(fileName);
					} catch (Exception e) {
						log.error(e.getMessage(), e); 
						continue;
					}

					//skip .ts files we already downloaded in previous iterations
					if (tsNumber <= lastTsNumber) {
						continue;
					}
					lastTsNumber = tsNumber;

					//download ts:
					try {
						Segment segment = new Segment(lastTsNumber, tsDuration, rendition);
						ts = HttpUtils.downloadFile(url.resolve(tsName).toString(), destinationPath + "/" + tempPath + "/" + fileName);
						segment.setFile(ts);
						onSegmentDownloadComplete(segment);
					} catch (IOException | NoSuchAlgorithmException e) {
						log.error(e.getMessage(), e); 
						continue;
					}
					i = j;
				}
			}

			//sleep for at least the maximal duration of the .ts that is specified in the m3u8 file - #EXT-X-TARGETDURATION
			long endDownloadTime = System.currentTimeMillis();
			log.info("download took: " + (endDownloadTime - downloadTime) + " ms. max duration: " + duration * 1000);

			if ((endDownloadTime - downloadTime) < (duration * 1000)) {
				try {
					log.info("sleeping for: " + ((duration * 1000) - (endDownloadTime - downloadTime)));
					Thread.sleep((duration * 1000) - (endDownloadTime - downloadTime));
				} catch (InterruptedException e) {
					log.error(e.getMessage(), e); 
				}
			}
		}
	}

	private boolean canHandleNullContent() {
		log.error("Get request failed. Closing session.");
    	if(runForever) {
    		try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				log.error("Failed to sleep.");
				return false;
			}
    		return true;
    	}
    	return false;
		
	}

	private int getTsNumber(String tsName) throws Exception {
		Pattern pattern = Pattern.compile("\\D(\\d+)\\.ts");
		Matcher matcher = pattern.matcher(tsName);
		if (matcher.find()) {
			return Integer.valueOf(matcher.group(1));
		}
		throw new Exception("ts with name: " + tsName + " does not contain ts number");
	}

	private void onSegmentDownloadComplete(Segment segment) {
		EventsManager.get().raiseEvent(new SegmentDownloadCompleteEvent(segment));
	}
}
