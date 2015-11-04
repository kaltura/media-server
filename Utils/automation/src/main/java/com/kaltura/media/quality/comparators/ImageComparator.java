package com.kaltura.media.quality.comparators;

import java.io.File;

/**
 * Created by asher.saban on 6/8/2015.
 */
public interface ImageComparator {

	public boolean isSimilar(File image1, File image2);
}
