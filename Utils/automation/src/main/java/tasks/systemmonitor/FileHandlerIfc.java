package tasks.systemmonitor;

import java.nio.file.Path;

public interface FileHandlerIfc {

	public void fileCreated(Path file);
}