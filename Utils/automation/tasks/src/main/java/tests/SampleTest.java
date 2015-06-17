package tests;

import com.kaltura.client.KalturaClient;
import com.kaltura.client.types.KalturaLiveStreamEntry;
import configurations.ConfigurationReader;
import configurations.EncoderConfig;
import configurations.EntryConfig;
import configurations.TestConfig;
import downloaders.StreamDownloader;
import downloaders.StreamDownloaderFactory;
import encoders.Encoder;
import kaltura.actions.CreateLiveEntry;
import kaltura.actions.StartSession;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import tasks.TsFilesComparator;
import utils.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
/**
 * Created by asher.saban on 2/25/2015.
 */
public class SampleTest {

	@BeforeClass
	public void initializeTest() throws Exception {
		Reporter.log("BEFORE 1-----------------------", true);
		System.out.println("BEFORE -----------------------");
	}

	@Test
	public void compareFiles() {
		Reporter.log("TEST 1-----------------------",true);
		System.out.println("TEST 1 -----------------------");
		Assert.assertTrue(true);

	}

	@AfterClass
	public void clearResources() {
		Reporter.log("AFTER 1 -----------------------",true);
		System.out.println("AFTER -----------------------");

	}
}
