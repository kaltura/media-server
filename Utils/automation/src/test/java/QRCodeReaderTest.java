import com.google.zxing.NotFoundException;
import com.kaltura.media.quality.utils.QRCodeReader;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by asher.saban on 2/23/2015.
 */
public class QRCodeReaderTest {

    @Test()
    public void shouldReadQRCode() throws IOException, NotFoundException, URISyntaxException {
//        String testResource =
        URL outputFile = QRCodeReaderTest.class.getResource("/qrCodeTest/qrCodeSampleOutput.txt");
        String output = new String(Files.readAllBytes(Paths.get(outputFile.toURI())));
        URL pngFile = QRCodeReaderTest.class.getResource("/qrCodeTest/qrCodeSample.png");
        String code = QRCodeReader.readQRCode(new File(pngFile.getFile()));
        Assert.assertEquals(code,output);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void shouldFailToReadQRCode() throws IOException, NotFoundException {
        URL pngFile = QRCodeReaderTest.class.getResource("/qrCodeTest/missingQrCode.png");
        QRCodeReader.readQRCode(new File(pngFile.getFile()));
    }
}
