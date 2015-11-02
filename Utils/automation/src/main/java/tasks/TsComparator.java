package tasks;

import comparators.imagemagik.ImageMagikComparator;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import utils.ImageUtils;
import utils.ThreadManager;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* Created by asher.saban on 6/10/2015.
*/
public class TsComparator {

	private static final Logger log = Logger.getLogger(TsComparator.class);
	private static final int NUM_FAILED_TS_SEQUENCE = 3;

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
		final Set<Thread> threads = new ConcurrentSkipListSet<Thread>();

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
					ThreadManager.start(new Runnable() {
						@Override
						public void run() {
							threads.add(Thread.currentThread());
							//compare the pair of images
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

			log.info("Waiting for all threads to finish.");
			for(Thread thread : threads){
				thread.join();
			}

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

	private static boolean compareFiles(File folderPath, File tempDiffFolder) {
		log.info("getting files from folder: " + folderPath);
		@SuppressWarnings("rawtypes")
		Collection files = FileUtils.listFiles(folderPath, new String[]{"ts"}, true);
		@SuppressWarnings("unchecked")
		List<File> sortedFiles = getSortedFilesList(files);
		log.info("got files from folder: " + sortedFiles.size());

		return compareFiles(sortedFiles, tempDiffFolder);
	}

	public static boolean compareFiles(String folderPath, String pathToFfmpeg, String diffTempFolder) throws IOException {
		log.info("Folder: " + folderPath);
		log.info("Path to ffmpeg: " + pathToFfmpeg);
		log.info("Temp diff folder: " + diffTempFolder);

		File tempDiffFolder = new File(diffTempFolder);
		log.info("Creating temp diff folder if not exists: " + tempDiffFolder.getAbsolutePath());
		FileUtils.forceMkdir(tempDiffFolder);

		return compareFiles(new File(folderPath),tempDiffFolder);

	}

	public static void main(String[] args) throws Exception {

		if (args.length != 3) {
			System.out.println("Usage: [files destination] [path to ffmpeg] [temp diff folder]");
			System.exit(1);
		}
		compareFiles(args[0], args[1], args[2]);
	}
}

