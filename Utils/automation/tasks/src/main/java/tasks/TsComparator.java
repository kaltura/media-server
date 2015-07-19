package tasks;

import comparators.imagemagik.ImageMagikComparator;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import utils.ImageUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* Created by asher.saban on 6/10/2015.
*/
public class TsComparator {

	private static final Logger log = Logger.getLogger(TsComparator.class);
	private static final int NUM_FAILED_TS_SEQUENCE = 3;
	private static ExecutorService executor = Executors.newFixedThreadPool(20);
	private static final Object errorMsgsLock = new Object();

	private static File getFirstFrameFromFile(File ts) throws Exception {

		File dest = new File(ts.getAbsolutePath() + ".jpeg");

		//save first frame to file
		ImageUtils.saveFirstFrame(ts, dest);

		return dest;
	}
	private static boolean compareFiles(List<File> sortedFiles, final File tempDiffFolder) {

		if (sortedFiles.size() == 0) {
			log.error("Sorted list is empty");
			return true;
		}
		File first = sortedFiles.get(0);
		File last = sortedFiles.get(sortedFiles.size() - 1);
		final int firstTsIndex = extractTsNumber(first.getName());
		int lastTsIndex = extractTsNumber(last.getName());

		//create a boolean array that will hold the result of that ts
		final boolean[] results = new boolean[lastTsIndex - firstTsIndex + 1];
		final Map<Integer, List<String>> errorMsgs = new ConcurrentHashMap<>();

		try {
			File firstImage = getFirstFrameFromFile(first);
			for (int i = 1; i < sortedFiles.size(); i++) {

				//get first frame from second file
				final File second = sortedFiles.get(i);
				final File secondImage = getFirstFrameFromFile(second);

				//extract ts numbers
				final int firstNum = extractTsNumber(first.getName());
				int secondNum = extractTsNumber(second.getName());

				if (firstNum == secondNum) {
					final File finalFirstImage = firstImage;
					final File finalFirst = first;
					executor.submit(new Runnable() {
						@Override
						public void run() {
							//compare the pair of images
							log.info("--: " + tempDiffFolder.getName() + " " + tempDiffFolder.getName() + "diff: " + "/diff"+firstNum+".jpg");
							ImageMagikComparator imComparator = new ImageMagikComparator(10.0,tempDiffFolder.getAbsolutePath() + "/diff"+firstNum+".jpg");
							if (!imComparator.isSimilar(finalFirstImage, secondImage)) {
								results[firstNum - firstTsIndex] = true;
							}
						}
					});
				}
				first = second;
				firstImage = secondImage;
			}

			executor.shutdown();
			log.info("Waiting for all threads to finish. 5min timeout");
			executor.awaitTermination(5, TimeUnit.MINUTES);
			log.info("All threads finished, analyzing results");
			return verifyResults(results, errorMsgs, firstTsIndex);

		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}

	private static boolean verifyResults(boolean[] results, Map<Integer, List<String>> errorMsgs, int tsOffset) {
		boolean success = true;
		int counter = 0;
		int i;
		for (i = 0; i < results.length - 1; i++) {
			if (results[i] && results[i + 1]) {
				counter++;
			}
			else {
				if (counter > NUM_FAILED_TS_SEQUENCE) {
					success = false;
					printErrorMsg(i, counter, tsOffset);
					counter = 0;
				}
			}
		}
		//handle the last sequence of printing
		if (counter > NUM_FAILED_TS_SEQUENCE) {
			success = false;
			printErrorMsg(i, counter, tsOffset);
		}
		return success;
	}

	private static void printErrorMsg(int i, int counter, int tsOffset) {
		log.info("Ts files in range: " + (i-1-counter+tsOffset) + " to " + (i-1+tsOffset) + " are different");
	}

	private static Integer extractTsNumber(String tsName) {
		Pattern pattern = Pattern.compile("\\D(\\d+)\\.ts");
		Matcher matcher = pattern.matcher(tsName);
		if (matcher.find()) {
			return Integer.valueOf(matcher.group(1));
		}
		return null;
	}

	private static List<File> getSortedFilesList(Collection<File> files) {
		List<File> newList = new ArrayList<>(files);
		Collections.sort(newList, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				Integer num1 = extractTsNumber(o1.getName());
				Integer num2 = extractTsNumber(o2.getName());
				//TODO, NullPointerException
				return num1.compareTo(num2);
			}
		});
		return newList;
	}

	public static boolean compareFiles(File folderPath, File tempDiffFolder) {
		Collection files = FileUtils.listFiles(folderPath, new String[]{"ts"}, true);
		List<File> sortedFiles = getSortedFilesList(files);
		return compareFiles(sortedFiles, tempDiffFolder);
	}

	public static void main(String[] args) throws Exception {

		if (args.length != 3) {
			System.out.println("Usage: [files destination] [path to ffmpeg] [temp diff folder]");
			System.exit(1);
		}

		log.info("Folder: " + args[0]);
		log.info("Path to ffmpeg: " + args[1]);
		log.info("Temp diff folder: " + args[2]);

		File tempDiffFolder = new File(args[2]);
		log.info("Creating temp diff folder if not exists: " + tempDiffFolder.getAbsolutePath());
		FileUtils.forceMkdir(tempDiffFolder);

		ImageUtils.initializeImageUtils(args[1]);
		compareFiles(new File(args[0]),tempDiffFolder);
	}
}

