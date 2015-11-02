package event;

import java.io.File;
import java.util.List;

public interface ISegmentsListener extends IListener {
	void onSegmentsDownloadComplete(int segmentNumber, List<File> segments);
}
