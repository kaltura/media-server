package validator;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import provider.Provider;
import utils.ImageUtils;
import utils.ThreadManager;

import com.kaltura.client.types.KalturaLiveEntry;

import comparators.imagemagik.ImageMagikComparator;
import event.ISegmentsListener;

/**
 * This class is responsible for comparing TS files in Entry context. 
 */
public class EntryTsComparator extends Validator implements ISegmentsListener {
	
	private static final Logger log = Logger.getLogger(EntryTsComparator.class);
	
	private KalturaLiveEntry entry;

	class ValidationTask implements Runnable{

		private int segmentNumber;
		private List<File> segments;
		
		public ValidationTask(int segmentNumber, List<File> segments) {
			this.segmentNumber = segmentNumber;
			this.segments = segments;
		}

		@Override
		public void run() {
			File file1 = segments.get(0);
			File file2;
			
			Thread.currentThread().setName(getClass().getSimpleName() + "-" + file1.getAbsolutePath());
			
			File image1 = getFirstFrameFromFile(file1);
			File image2;

			String outputFolder = file1.getParent();
			String outputName;
			String diffPath;
			
			// Check 1-2, 2-3, 3-4, ...
			for(int i = 1 ; i < segments.size(); ++i) {
				
				if(!ThreadManager.shouldContinue()){
					break;
				}
				
				file2 = segments.get(i);
				image2 = getFirstFrameFromFile(file2);
				outputName = file2.getParentFile().getParentFile().getName();
				diffPath = outputFolder + "/diff_" + segmentNumber + "_" + outputName + ".jpg";
				
				log.info("Comparison of entry [" + entry.id + "]" + file1 + " and " + file2);
				ImageMagikComparator imComparator = new ImageMagikComparator(10.0, diffPath);
				if ((image1 == null) || (image2 == null) || (!imComparator.isSimilar(image1, image2))) {
					log.error("Comparison of " + file1 + " and " + file2 + " failed, image files: " + image1 + " , " + image2 + ", diff: " + diffPath);
				}
				
				file1 = file2;
				image1 = image2;
			}
		}
		
	}
	
	public EntryTsComparator(KalturaLiveEntry entry, List<Provider> providers) {
		super();
		
		this.entry = entry;
		for(Provider provider : providers){
			provider.addListener(ISegmentsListener.class, this);
		}
	}
	
	private File getFirstFrameFromFile(File ts) {
		try {
			File dest = new File(ts.getAbsolutePath() + ".jpeg");
			//	save first frame to file
			ImageUtils.saveFirstFrame(ts, dest);
			return dest;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onSegmentsDownloadComplete(int segmentNumber, List<File> segments) {
		ThreadManager.start(new ValidationTask(segmentNumber, segments));
	}
}