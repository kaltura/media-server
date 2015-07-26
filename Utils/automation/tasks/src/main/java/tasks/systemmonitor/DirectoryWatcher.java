package tasks.systemmonitor;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import tasks.StatusWatcher;

/**
 * This class watches a directory and alerts if a new file is created 
 */
public class DirectoryWatcher {

	private FileHandlerIfc fileWatcher;
    private final WatchService watcher = FileSystems.getDefault().newWatchService();
    private final Map<WatchKey,Path> keys = new HashMap<WatchKey, Path>();
	private StatusWatcher statusWatcher;
	private Pattern fileNamePattern;
    
	public DirectoryWatcher(Path dir, String regex, StatusWatcher statusWatcher, FileHandlerIfc fileWatcher) throws IOException {
		this.statusWatcher = statusWatcher;
		this.fileWatcher = fileWatcher;
		this.fileNamePattern = Pattern.compile(regex);

		registerAll(dir);
	}

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                throws IOException
            {
            	WatchKey key = dir.register(watcher, ENTRY_CREATE);
                keys.put(key, dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Process all events for keys queued to the watcher
     */
    public void processEvents() {
        do {

            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event: key.pollEvents()) {
                Kind<?> kind = event.kind();

                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);

                String fileName = child.toFile().getName();
                if(fileNamePattern.matcher(fileName).matches())
                	fileWatcher.fileCreated(child);

				// if directory is created, and watching recursively, then
				// register it and its sub-directories
				try {
					if (Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS)) 
						registerAll(child);
				} catch (IOException x) {
					// swallow
				}
			} 
            

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);
                if (keys.isEmpty()) {
                    break;
                }
            }
        } while(statusWatcher.isAlive());
    }
}