package tasks.monitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import tasks.StatusWatcher;
import utils.GlobalContext;
import configurations.TestConfig;

/**
 * This runnable object is responsible for listening on the entry directory compare it results
 */
public class EntryComparator implements Runnable {
	
	private static final int STREAMS_RETRIVAL_INTERVAL = 10 * 1000;
	private static final int STEAMS_RETRIVAL_RETRIES = 10;
	private TestConfig config;
	private String entryId;
	private StatusWatcher statusWatcher;
	private String directoryPath;
	
	/**
	 * Constructor
	 * @param config The test configuration
	 * @param statusWatcher whatever tells us to stop watching the directory.
	 * @param entryId The entry we monitor
	 */
	public EntryComparator(StatusWatcher statusWatcher, String entryId, String directoryPath) {
		this.config = TestConfig.get();
		this.statusWatcher = statusWatcher;
		this.entryId = entryId;
		this.directoryPath = directoryPath;
	}

	public void run() {
		
		// Get number of streams (actually validates that the entry is alive)
		Integer nStreams = getNStreams();
		if(nStreams == null) {
			System.err.println("failed to identify number of streams for entry: " + entryId);
			return;
		}
		
		// Register root dir for files creation
		String outputDirStr = directoryPath + "/diff";
		File outputDir = new File(outputDirStr);
		if(!outputDir.exists()) {
			outputDir.mkdir();
		}
		
		FileHandlerIfc watcher = new EntryTsComparatorFileHandler(entryId, config.getPathToFfmpeg(), outputDirStr,  nStreams);
		Path pathToWatch = Paths.get(directoryPath);
		
		try {
			DirectoryWatcher watch = new DirectoryWatcher(pathToWatch, ".*\\.ts$", statusWatcher, watcher);
			
			// Process the results
			watch.processEvents();
		} catch (IOException e) {
			System.err.println("Couldn't process events while testing: " + entryId);
		}
	}

	private Integer getNStreams() {
		int cnt = 0;
		Integer nStreams = null;
		while(nStreams == null) {
			if (cnt++ == STEAMS_RETRIVAL_RETRIES) {
				System.err.println("Tried too many times...");
				return null;
			}
			
			try {
				nStreams = (Integer)GlobalContext.getValue("NUM_STREAMS");
			} catch (IllegalArgumentException e) {
				try {
					Thread.sleep(STREAMS_RETRIVAL_INTERVAL);
				} catch (InterruptedException e1) {
					System.err.println("Failed while initializing comparator for entry: " + entryId);
					return null;
				}
			}
		}
		
		return nStreams;
	}
	
}