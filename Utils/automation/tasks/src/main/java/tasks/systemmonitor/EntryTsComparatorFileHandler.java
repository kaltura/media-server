package tasks.systemmonitor;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import utils.ImageUtils;
import comparators.imagemagik.ImageMagikComparator;

/**
 * This class is responsible for comparing TS files in Entry context. 
 */
public class EntryTsComparatorFileHandler implements FileHandlerIfc {
	
	private static final Logger log = Logger.getLogger(EntryTsComparatorFileHandler.class);
	
	// Thread pools
	private static ExecutorService executor = Executors.newCachedThreadPool();
	private static ScheduledThreadPoolExecutor schedulerExecutor = new ScheduledThreadPoolExecutor(20);

	private String entryId;
	private String outputFolder;
	private int nStreams;
	
	// This map maps between the ts-number to the files that match that ts number
	private Map<Integer, List<Path>> filesPerIdx = new HashMap<Integer, List<Path>>();
	// This list contains all ids of failed indexes
	private List<Integer> failedIdx = new ArrayList<Integer>();

	public EntryTsComparatorFileHandler(String entryId, String ffmpegPath, String outputFolder, int nStreams) {
		this.entryId = entryId;
		this.outputFolder = outputFolder;
		this.nStreams = nStreams;
		
		// Set verify failures to run automatically every VERIFY_PERIOD.
		schedulerExecutor.scheduleAtFixedRate(new VerifyComparisonResults(), VerifyComparisonResults.VERIFY_PERIOD, VerifyComparisonResults.VERIFY_PERIOD, TimeUnit.SECONDS);
		ImageUtils.initializeImageUtils(ffmpegPath);
	}
	
	/**
	 * This class is responsible for cleaning after a TS
	 * (as an entry may live for ever, (theoretically) we'd like to clean beforehand. 
	 */
	protected class TsCleaner implements Runnable {
		
		protected static final String THREAD_NAME = "Index-ttl-";
		protected static final int TS_TIME_TO_LIVE = 5;
		
		private Integer key;

		public TsCleaner(Integer key) {
			this.key = key;
		}

		@Override
		public void run() {
			Thread.currentThread().setName(THREAD_NAME + key);
			synchronized (filesPerIdx) {
				if(filesPerIdx.containsKey(key)) 
					filesPerIdx.remove(key);
			}
		
			synchronized (failedIdx) {
				if(failedIdx.contains(key)) 
					failedIdx.remove(key);
			}
		}
	}
	
	/**
	 * This class is responsible for the actual comparison of a group of TSes that have the same ts number 
	 */
	protected class TsCompareTask implements Runnable {

		protected static final String THREAD_NAME = "Ts-Compare-";
		private Integer tsNumber;
		private List<Path> tsPaths;

		public TsCompareTask(Integer tsNumber, List<Path> paths) {
			this.tsPaths = paths;
			this.tsNumber = tsNumber;
		}
		
		private File getFirstFrameFromFile(File ts) {
			try {
				File dest = new File(ts.getAbsolutePath() + ".jpeg");
				//	save first frame to file
				ImageUtils.saveFirstFrame(ts, dest);
				return dest;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public void run() {
			Thread.currentThread().setName(THREAD_NAME + "_" +entryId + "_" + tsNumber);
			
			File file1 = tsPaths.get(0).toFile();
			File image1 = getFirstFrameFromFile(file1);
			
			// Check 1-2, 2-3, 3-4, ...
			for(int i = 1 ; i < tsPaths.size(); ++i) {
				
				File file2 = tsPaths.get(i).toFile();
				File image2 = getFirstFrameFromFile(file2);
				
				System.out.println("Comparison of entry [" + entryId + "]" + file1 + " and " + file2);
				
				ImageMagikComparator imComparator = new ImageMagikComparator(10.0,outputFolder + "/diff_" + tsNumber + ".jpg");
				if ((image1 == null) || (image2 == null) || (!imComparator.isSimilar(image1, image2))) {
					synchronized (failedIdx) {
						System.out.println("Comparison of " + file1 + " and " + file2 + " failed. "
								+ "Image files: " + image1 + " , " + image2);
						failedIdx.add(tsNumber);
						break;
					}
				}
				
				file1 = file2;
				image1 = image2;
			}
		}
	}
	
	/**
	 * This thread is responsible to verify comparison results, and alert in a case of error
	 */
	protected class VerifyComparisonResults implements Runnable {
		
		protected static final String THREAD_NAME = "Periodic-compare-results";
		protected static final int VERIFY_PERIOD = 60;
		protected static final int NUM_FAILED_TS_SEQUENCE = 3;
		
		@Override
		public void run() {

			Thread.currentThread().setName(THREAD_NAME + "_" +entryId);
			if(failedIdx.isEmpty())
				return;
			
			List<Integer> indices = new ArrayList<>(failedIdx);
			Collections.sort(indices);
			Integer first = indices.get(0);
			Integer last = indices.get(0);
			int total = 0;
			
			for (Integer cur : indices) {
				if(cur == last + 1) {
					total++;
				} else {
					checkStrike(first, total);
					total = 0;
					first = cur;
				}
				last = cur;
			}
			
			checkStrike(first, total);
		}

		private void checkStrike(Integer first, int total) {
			if(total > NUM_FAILED_TS_SEQUENCE)
				log.info("Entry Id [" + entryId + "] Ts files in range: " + first + " to " + (first + total) + " are different (The sequence may be longer)");
			
			synchronized (failedIdx) {
				for(Integer i = first; i < first + total; ++i)
					failedIdx.remove(i);
			}
		}
		
		
	}

	/**
	 * This function is triggered any time the downloaders gets a new file,
	 */
	public void fileCreated(Path file) {
		Integer tsNumber = extractTsNumber(file.getFileName().toString());
		List<Path> paths = null;
		
		synchronized (filesPerIdx) {
			
			// If this is the first TS matching this TS number
			if(!filesPerIdx.containsKey(tsNumber)){
				filesPerIdx.put(tsNumber, new ArrayList<Path>());
				schedulerExecutor.schedule(new TsCleaner(tsNumber), TsCleaner.TS_TIME_TO_LIVE, TimeUnit.MINUTES);
			}

			filesPerIdx.get(tsNumber).add(file);
			
			if(filesPerIdx.get(tsNumber).size() == nStreams) {
				paths = filesPerIdx.remove(tsNumber);
			}
		}
		
		if(paths != null)
			executor.execute(new TsCompareTask(tsNumber, paths));
	}
	
	private static Integer extractTsNumber(String tsName) {
		Pattern pattern = Pattern.compile("\\D(\\d+)\\.ts");
		Matcher matcher = pattern.matcher(tsName);
		if (matcher.find()) {
			return Integer.valueOf(matcher.group(1));
		}
		return null;
	}
}
