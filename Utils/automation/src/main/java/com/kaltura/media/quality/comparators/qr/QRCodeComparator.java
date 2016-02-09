package com.kaltura.media.quality.comparators.qr;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.google.zxing.NotFoundException;
import com.kaltura.media.quality.comparators.ImageComparator;
import com.kaltura.media.quality.utils.QRCodeReader;

/**
 * Created by asher.saban on 6/8/2015.
 */
public class QRCodeComparator implements ImageComparator {

	private static final Logger log = Logger.getLogger(QRCodeComparator.class);

	public QRCodeComparator(String diffImagePath) {
	}

	@Override
	public double compare(File image1, File image2) {
		return getValue(image1) - getValue(image2);
	}

	protected double qrCodeToDouble(String qrCode){
		String[] qrCodeParts = qrCode.split(" ");
		String[] timeParts = qrCodeParts[1].split(":");
		double time = 0;
		double multiplier = 1;
		for(int i = timeParts.length - 1; i >= 0; i--){
			time += (Double.valueOf(timeParts[i]) * multiplier);
			multiplier *= 60;
		}
		
		return time;
	}
	
	protected double getValue(File image){
		try {
			String qrCode = QRCodeReader.readQRCode(image);
			return qrCodeToDouble(qrCode);
		} catch (NotFoundException | IOException e) {
			log.error(e.getMessage(), e);
			return 0;
		}
	}
}
