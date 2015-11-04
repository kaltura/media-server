package com.kaltura.media.quality.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class QRCodeReader {

    public static String readQRCode(File filePath) throws IOException, NotFoundException {

        Map<DecodeHintType, Object> hintMap = new HashMap<>();
//        hintMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        return readQRCode(filePath, hintMap);
    }

    public static String readQRCode(File filePath, Map<DecodeHintType, ?> hintMap) throws IOException, NotFoundException {
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                    new BufferedImageLuminanceSource(
                            ImageIO.read(is))));
            Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hintMap);
            return qrCodeResult.getText();
        } finally {
            //close the stream, otherwise it will lock all the jpeg files
            if (is != null) {
                is.close();
            }
        }
    }

    public static BufferedImage cropImage(BufferedImage src, Rectangle rect) {
        BufferedImage dest = src.getSubimage(0, 0, rect.width, rect.height);
        return dest;
    }
}
