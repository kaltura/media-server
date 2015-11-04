package com.kaltura.media.quality.validator;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.media.quality.comparators.imagemagik.ImageMagikComparator;
import com.kaltura.media.quality.event.IListener;
import com.kaltura.media.quality.event.ISegmentsListener;
import com.kaltura.media.quality.provider.Provider;
import com.kaltura.media.quality.utils.ImageUtils;
import com.kaltura.media.quality.utils.ThreadManager;

/**
 * Compares between segments from different renditions by comparing the first frame 
 */
public class RenditionsComparator extends Validator implements ISegmentsListener {
	
	private static final Logger log = Logger.getLogger(RenditionsComparator.class);
	
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

			Thread thread = Thread.currentThread();
			thread.setPriority(Thread.MIN_PRIORITY);
			thread.setName(getClass().getSimpleName() + "-" + file1.getAbsolutePath());
			
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
	
	public RenditionsComparator(KalturaLiveEntry entry, List<Provider> providers) {
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
			log.error(e.getMessage(), e); 
		}
		return null;
	}

	@Override
	public void onSegmentsDownloadComplete(int segmentNumber, List<File> segments) {
		ThreadManager.start(new ValidationTask(segmentNumber, segments));
	}

	@Override
	public int compareTo(IListener o) {
		if(o == this){
			return 0;
		}
		return 1;
	}
}