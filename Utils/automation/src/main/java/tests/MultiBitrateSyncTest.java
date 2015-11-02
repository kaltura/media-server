package tests;

import com.kaltura.client.KalturaClient;
import com.kaltura.client.types.KalturaLiveStreamEntry;

import configurations.EncoderConfig;
import configurations.EntryConfig;
import configurations.TestConfig;
import encoders.Encoder;
import kaltura.actions.CreateLiveEntry;
import kaltura.actions.StartSession;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import provider.hls.HLSDownloader;
import tasks.TsComparator;
import utils.ProcessHandler;
import utils.StringUtils;
import utils.ThreadManager;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
/**
* Created by asher.saban on 2/25/2015.
*/
public class MultiBitrateSyncTest {

    private TestConfig config;
    private String dest;
    private Encoder encoder;
    private HLSDownloader downloader;
    private KalturaClient client;
    private KalturaLiveStreamEntry entry;

    private void sleep(long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @BeforeClass
    public void initializeTest() throws Exception {
        config = TestConfig.get();

        //create client:
        int partnerId = Integer.valueOf(config.getPartnerId());
        StartSession session = new StartSession(partnerId,config.getServiceUrl(),config.getAdminSecret());
        client = session.execute();

        //create live entry:
        EntryConfig entryConf = config.getEntryDetails();
        CreateLiveEntry liveEntry = new CreateLiveEntry(client, entryConf.getEntryName(), true, entryConf.isDvr(), entryConf.isRecording(), entryConf.getConversionProfileId());
        entry = liveEntry.execute();

		comment("created test with name: " + entry.name + ", id: " + entry.id);

        //initialize encoder
        EncoderConfig encoderConfig = config.getEncoder();

        //append streams to ffmpeg command. TODO, this is a workaround!
        String ffmpegCommand = encoderConfig.getArgs();
        for (int i = 1; i <= 3; i++) {
            ffmpegCommand = ffmpegCommand.replace("{" + i + "}", entry.primaryBroadcastingUrl + "/" + entry.id + "_" + i);
        }
		System.out.println("FFMpeg command: " + ffmpegCommand);
        encoder = new Encoder(encoderConfig.getEncoderName(),encoderConfig.getPathToExecutable(),ffmpegCommand);

        //initialize stream downloader
        downloader = new HLSDownloader();
    }

    private void comment(String msg) {
        Reporter.log(msg,true);
    }

    @Test
    public void streamVideoAndSleep() throws IOException {

        comment("About to stream video");
        ThreadManager.start(encoder);
        comment("Sleeping");
        sleep(90);
        comment("Done sleeping. verifying encoder is running...");
        Assert.assertTrue(encoder.isAlive());

    }

    public URI getManifestUrl() throws URISyntaxException {
        URI base = new URI(config.getServiceUrl());
        return base.resolve(String.format("/p/%1$s/sp/%1$s00/playManifest/entryId/%2$s/format/applehttp", entry.partnerId, entry.id));
    }
    
    @Test(dependsOnMethods = "streamVideoAndSleep")
    public void downloadTsFiles() throws URISyntaxException {
        comment("Building manifest url");
        URI uri = getManifestUrl();

        dest = config.getDestinationFolder() + "/" + StringUtils.generateRandomSuffix();
        int duration = config.getTestDuration();
        comment("Manifest URL:" + uri);
        comment("Destination folder:" + dest);
        comment("Test duration:" + duration);

        try {
        	downloader = new HLSDownloader(uri, dest);
            downloader.downloadFiles();
            Thread.sleep(duration * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            comment("Stopping streaming");
            encoder.stop();
        }
    }

    @Test(dependsOnMethods = "downloadTsFiles")
    public void compareFiles() throws IOException {
        comment("Comparing files");
        Assert.assertEquals(true,TsComparator.compareFiles(dest, config.getPathToFfmpeg(), dest + "/diff_dir"));
//        Assert.assertEquals(true, TsFilesComparator.compareFiles(new File(dest), (Integer) GlobalContext.getValue("NUM_STREAMS")));

        //test passed, delete downloads
        try {
            if (config.isDeleteFiles()) {
                comment("Deleting temporary files...");
                FileUtils.forceDelete(new File(dest));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @AfterClass
    public void clearResources() {
        comment("Shutting down ProcessHandler");
        ProcessHandler.shutdown();
    }
}
