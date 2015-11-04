package com.kaltura.media.quality.utils;

import java.io.File;

import com.kaltura.media.quality.configurations.TestConfig;

/**
 * Created by asher.saban on 2/16/2015.
 */
public class ImageUtils {

    public static void saveFirstFrame(File videoFile, File destination) throws Exception {
    	Process process = Runtime.getRuntime().exec(buildFirstFrameCommand(videoFile, destination));
    	process.waitFor();
    }

    private static String buildFirstFrameCommand(File videoFile, File destination) {
        //ffmpeg.exe -i {source} -vframes 1 -f image2 {dest}
        return TestConfig.get().getPathToFfmpeg() + " -y -i " + videoFile.getAbsolutePath() + " -vframes 1 -f image2 "+ destination.getAbsolutePath();
    }


}
