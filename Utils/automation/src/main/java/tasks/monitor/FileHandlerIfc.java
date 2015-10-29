package tasks.monitor;

import java.nio.file.Path;

public interface FileHandlerIfc {

	public void fileCreated(Path file);
}