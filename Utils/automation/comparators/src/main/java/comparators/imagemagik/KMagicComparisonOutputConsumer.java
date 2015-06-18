package comparators.imagemagik;

import org.apache.commons.io.IOUtils;
import org.im4java.process.ErrorConsumer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * Created by asher.saban on 6/8/2015.
 */
class KMagicComparisonOutputConsumer implements ErrorConsumer {
	private double differenceScore = 100;

	@Override
	public void consumeError(InputStream inputStream) throws IOException {
		StringWriter writer = new StringWriter();
		IOUtils.copy(inputStream, writer);
		differenceScore = Double.parseDouble(writer.toString());
	}

	public double getDifferenceScore() {
		return differenceScore;
	}
}