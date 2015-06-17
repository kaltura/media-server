package downloaders.hls;

import org.apache.commons.io.FileUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;
import utils.HttpUtils;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asher.saban on 2/17/2015.
 */
class HLSDownloaderWorker implements Runnable {

	private static final Logger log = Logger.getLogger(HLSDownloaderWorker.class);
	private static final int DEFAULT_TS_DURATION = 10;
	private final String url;
	private final String destinationPath;
	private int lastTsNumber;
	private volatile boolean stopDownloading;

	public HLSDownloaderWorker(String url, String destinationPath) {
		this.url = url;
		this.destinationPath = destinationPath;
		this.lastTsNumber = -1;
		this.stopDownloading = false;
	}

	public void stopDownload(){
		this.stopDownloading = true;
	}

	@Override
	public void run() {

		//extract base url:
		String baseUrl = url.substring(0, url.lastIndexOf("/"));
		String playlistFileName = url.substring(url.lastIndexOf("/")+1);
		String dest = destinationPath + "/" + playlistFileName;

		int counter = 0;
		while (true) {

			if (stopDownloading) {
				log.info("Shutting down downloader...");
				return;
			}

			counter++;
			log.debug("iteration: " + counter);

			//download m3u8:
			CloseableHttpClient httpClient = HttpUtils.getHttpClient();
			String content;
			try {
				content = HttpUtils.doGetRequest(httpClient, url);

				//write to file
				FileUtils.writeStringToFile(new File(dest + "/iter_" + counter),content);

			} catch (IOException e) {
				log.error("Get request failed.");
				e.printStackTrace();
				continue; //TODO
			}

			long downloadTime = System.currentTimeMillis();
			int tsDuration = 0;

			//get duration: search #EXT-X-TARGETDURATION
			String[] lines = content.split("\n");
			int numLines = lines.length;

			for (int i = 0; i < numLines; i++) {
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
					int tsNumber = -1;
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
						HttpUtils.downloadFile(baseUrl + "/" + tsName, dest + "/" + fileName);
//						if (validateSyncPoints(dest + "/" + fileName)) {
//							new File(dest + "/" + fileName).delete();
//						}
//						else {
//							System.out.println("Missing sync points: " + dest + "/" + fileName);
//						}
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

	private boolean validateSyncPoints(String s) {
		File f = new File(s);
		try {
			String tmp = FileUtils.readFileToString(f);
			if (tmp.contains("KalturaSyncPoint")) {
				log.debug(f.getAbsoluteFile() + " contains sync points");
				return true;
			}
			return false;
		} catch (IOException e) {
			log.error("failed to validate sync points in: " + f.getAbsolutePath());
			return false;
		}
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
}
