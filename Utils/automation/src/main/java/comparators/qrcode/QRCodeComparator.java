package comparators.qrcode;


import comparators.ImageComparator;

import java.io.File;

/**
 * Created by asher.saban on 6/8/2015.
 */
public class QRCodeComparator implements ImageComparator {

	@Override
	public boolean isSimilar(File image1, File image2) {
		return false;
	}
}
