package utils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import configurations.TestConfig;

/**
 * Created by asher.saban on 2/16/2015.
 */
public class ImageUtils {

    private static final long TIME_OUT_SECONDS = 20;

    public static void saveFirstFrame(File videoFile, File destination) throws Exception {
        ProcessBuilder pb = ProcessHandler.createProcess(buildFirstFrameCommand(videoFile, destination));
        Process p = ProcessHandler.start(pb);
        ProcessHandler.waitFor(p, TIME_OUT_SECONDS, TimeUnit.SECONDS);
    }

    private static String buildFirstFrameCommand(File videoFile, File destination) {
        //ffmpeg.exe -i {source} -vframes 1 -f image2 {dest}
        return TestConfig.get().getPathToFfmpeg() + " -y -i " + videoFile.getAbsolutePath() + " -vframes 1 -f image2 "+ destination.getAbsolutePath();
    }


}
