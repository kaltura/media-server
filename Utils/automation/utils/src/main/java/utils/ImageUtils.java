package utils;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by asher.saban on 2/16/2015.
 */
public class ImageUtils {

    private static final long TIME_OUT_SECONDS = 20;
    private static String pathToFfmpeg;

    public static void initializeImageUtils(String pathToFfmpeg) {
        if (pathToFfmpeg == null || pathToFfmpeg.isEmpty()) {
            throw new IllegalArgumentException("path to ffmpeg cannot be null of empty");
        }
        ImageUtils.pathToFfmpeg = pathToFfmpeg;
    }

    private static void validateImageUtilsInit() throws Exception {
        if (pathToFfmpeg == null || pathToFfmpeg.isEmpty()) {
            //TODO better exception
            throw new Exception("Image utils are not initialized. call validateImageUtilsInit() first.");
        }
    }
    public static void saveFirstFrame(File videoFile, File destination) throws Exception {
        validateImageUtilsInit();
        ProcessBuilder pb = ProcessHandler.createProcess(buildFirstFrameCommand(videoFile, destination));
        Process p = ProcessHandler.start(pb);
        ProcessHandler.waitFor(p, TIME_OUT_SECONDS, TimeUnit.SECONDS);
    }

    private static String buildFirstFrameCommand(File videoFile, File destination) {
        //ffmpeg.exe -i {source} -vframes 1 -f image2 {dest}
        return pathToFfmpeg + " -y -i " + videoFile.getAbsolutePath() + " -vframes 1 -f image2 "+ destination.getAbsolutePath();
    }


}
