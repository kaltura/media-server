package tasks;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import utils.ImageUtils;
import utils.MultiBitrateResults;
import utils.QRCodeReader;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asher.saban on 2/25/2015.
 */
public class TsFilesComparator {

    private static final Logger log = Logger.getLogger(TsFilesComparator.class);
    private static final long THRESHOLD_IN_MS = 500;

    private static long convertToMs(String code) {
        Pattern pattern = Pattern.compile("(\\d\\d):(\\d\\d):(\\d\\d\\.\\d*)$");
        Matcher matcher = pattern.matcher(code);
        if (matcher.find()) {
            Integer hours = Integer.valueOf(matcher.group(1));
            Integer minutes = Integer.valueOf(matcher.group(2));
            Double milliseconds = Double.valueOf(matcher.group(3)) * 1000;
            return (long) (milliseconds + minutes * 60 * 1000 + hours * 60 * 60 * 1000);
        }

        //TODO error handling
        return 0;
    }

    private static long getQRCodeFromFile(File tsFile, String jpegFile) throws Exception {
        File f = new File(jpegFile);

        //save first frame to file
        ImageUtils.saveFirstFrame(tsFile, f);   //TODO, be consistent with File objects

        //take QR code:
        String text = QRCodeReader.readQRCode(f);
        return convertToMs(text);
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

    private static List<MultiBitrateResults> buildTestResults(List<File> sortedFiles) {

        List<MultiBitrateResults> results = new ArrayList<>();

        //initialize first
        File prevTsFile = sortedFiles.get(0);  //TODO, index out of bounds
        int prevTsNumber = extractTsNumber(prevTsFile.getName());
        MultiBitrateResults r = new MultiBitrateResults(prevTsNumber);

        for (File currentTsFile : sortedFiles) {
            int currentTsNumber = extractTsNumber(currentTsFile.getName());

            //finished with previous ts file, save it to map and start a new result
            if (currentTsNumber != prevTsNumber) {

                results.add(r);
                r = new MultiBitrateResults(currentTsNumber);
                prevTsNumber = currentTsNumber;
            }
            String jpegName = currentTsFile.getAbsolutePath() + ".jpeg";

            //delete file if exists
//            File jpegFile = new File(jpegName); /*boolean isDeleted = */jpegFile.delete();  //TODO, be consistent and work only with File obj. what if false? DEADLOCK. solved with -y in ffmpeg
            try {
                long code = getQRCodeFromFile(currentTsFile, jpegName);
                r.updateValues(code);
            } catch (Exception e) {
                log.error(jpegName + ". failed to get qr code. not updating");
                e.printStackTrace();
            }
        }

        //add the last ones
        results.add(r);
        return results;
    }

    @SuppressWarnings("unchecked")
    public static boolean compareFiles(File folderPath, int numStreams) {
        boolean success = true;
        @SuppressWarnings("rawtypes")
		Collection files = FileUtils.listFiles(folderPath, new String[]{"ts"}, true);
        List<File> sortedFiles = getSortedFilesList(files);
        List<MultiBitrateResults> results = buildTestResults(sortedFiles);

        //analyze results:
        for (int i = 0; i < results.size(); i++) {
            MultiBitrateResults r = results.get(i);
            long diff = r.getMaxValue() - r.getMinValue();
            log.info("ts with id: " + r.getTsNumber() + ", diff: " + diff + ", min: " + r.getMinValue() + ", max: " + r.getMaxValue() + " . num comparisons: " + r.getNumComparisons());

            //TODO, solve missing ts problem
//            boolean failOnMissingFiles = false;
//            String message = "missing ts files";
//            if (r.getNumComparisons() < numStreams) {
//                log.warn(message);
//            }
            //fail the test on missing ts files only if there were missing file at the middle.
//            else if (failOnMissingFiles) {
//                log.error(message);
//                success = false;
//            }
            if (diff > THRESHOLD_IN_MS) {
                success = false;
                log.error("Ts files are not in sync");
            }
        }
        return success;
    }
}
