package utils;

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

    private BufferedImage cropImage(BufferedImage src, Rectangle rect) {
        BufferedImage dest = src.getSubimage(0, 0, rect.width, rect.height);
        return dest;
    }

    public static void main(String[] args) throws IOException, NotFoundException {

        File f = new File("C:\\ASHER\\capture22.jpeg");
//        File f = new File("C:\\live-automation-resources\\downloads\\150312_181227\\flavor_0\\chunklist_b987136.m3u8\\media-uyhyatobd_b987136_0.ts.jpeg");
//        File f = new File("C:\\live-automation-resources\\downloads\\150312_181227\\flavor_1\\chunklist_b475136.m3u8\\media-uopd6lkpm_b475136_16.ts.jpeg");
//        BufferedImage i = ImageIO.read(f);
//        System.out.println(i.getHeight());
//        System.out.println(i.getWidth());
//
//        int size = i.getHeight() / 4;
//        BufferedImage i1 = i.getSubimage(i.getWidth() - size, 0, size, size);
//        File d = new File("C:/ASHER/capture22.jpeg");
//        ImageIO.write(i1, "jpeg", d);
        String a = readQRCode(f.getAbsoluteFile());
//
        System.out.println(a);
    }
}
