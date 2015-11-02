package provider.hls;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;

import provider.Provider;
import utils.HttpUtils;
import utils.ThreadManager;
import event.ISegmentListener;

/**
 * Created by asher.saban on 2/17/2015.
 */
class HLSStreamDownloader extends Provider {

	private static final Logger log = Logger.getLogger(HLSStreamDownloader.class);
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	private static final int DEFAULT_TS_DURATION = 10;
	private final URI url;
	private final String destinationPath;
	private String domainHash;
	private int lastTsNumber;
	private boolean runForever = false;
	private String tempPath;

	public HLSStreamDownloader(URI url, String destinationPath, String domainHash) {
		this.url = url;
		this.destinationPath = destinationPath;
		this.domainHash = domainHash;
		this.lastTsNumber = -1;
		this.tempPath = dateFormat.format(new Date());
	}

	@Override
	public void run() {
		Thread.currentThread().setName(getClass().getSimpleName() + "-" + url);
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

			} catch (IOException e) {
				log.error("Get request failed.");
				e.printStackTrace();
				continue; //TODO
			}

            if(content == null) {
            	if(canHandleNullContent()) {
            		continue;
            	} else {
            		return;
            	}
            }
			
			long downloadTime = System.currentTimeMillis();
			int tsDuration = 0;

			//get duration: search #EXT-X-TARGETDURATION
			String[] lines = content.split("\n");
			int numLines = lines.length;

			for (int i = 0; i < numLines; i++) {
				if(!ThreadManager.shouldContinue()){
					return;
				}
				String line = lines[i];
				if (line.startsWith("#EXT-X-TARGETDURATION:")) {
					log.debug(line);
					tsDuration = parseStringToInt(line.substring("#EXT-X-TARGETDURATION:".length()).trim());
				}
				//a .ts file
				else if (line.startsWith("#EXTINF:")) {
					log.debug(line);
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
						e.printStackTrace();
						continue;
					}

					//skip .ts files we already downloaded in previous iterations
					if (tsNumber <= lastTsNumber) {
						continue;
					}
					lastTsNumber = tsNumber;

					//download ts:
					try {
						onSegmentDownloadStart(lastTsNumber);
						ts = HttpUtils.downloadFile(url.resolve(tsName).toString(), destinationPath + "/" + tempPath + "/" + fileName);
						onSegmentDownloadComplete(lastTsNumber, ts);
					} catch (IOException e) {
						e.printStackTrace();
						continue;
					}
					i = j;
				}
			}

			//sleep for at least the maximal duration of the .ts that is specified in the m3u8 file - #EXT-X-TARGETDURATION
			long endDownloadTime = System.currentTimeMillis();
			log.info("download took: " + (endDownloadTime - downloadTime) + " ms. max duration: " + tsDuration * 1000);

			if ((endDownloadTime - downloadTime) < (tsDuration * 1000)) {
				try {
					log.info("sleeping for: " + ((tsDuration * 1000) - (endDownloadTime - downloadTime)));
					Thread.sleep((tsDuration * 1000) - (endDownloadTime - downloadTime));
				} catch (InterruptedException e) {
					e.printStackTrace();
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

	private int parseStringToInt(String str) {
		try {
			return Integer.valueOf(str);
		} catch (NumberFormatException e) {
			log.error("#EXT-X-TARGETDURATION was not found in m3u8 file.");
			return DEFAULT_TS_DURATION; //TODO throw exception?
		}
	}

	public void addSegmentListener(ISegmentListener listener) {
		addListener(ISegmentListener.class, listener);
	}

	public void removeSegmentListener(ISegmentListener listener) {
		removeListener(ISegmentListener.class, listener);
	}

	private void onSegmentDownloadComplete(int segmentNumber, File segment) {
		for(ISegmentListener listener : getListeners(ISegmentListener.class)){
			listener.onSegmentDownloadComplete(segmentNumber, domainHash, segment);
		}
	}

	private void onSegmentDownloadStart(int segmentNumber) {
		for(ISegmentListener listener : getListeners(ISegmentListener.class)){
			listener.onSegmentDownloadStart(segmentNumber, domainHash);
		}
	}
}
