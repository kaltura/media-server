package com.kaltura.media.quality.validator;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.media.quality.comparators.imagemagik.ImageMagikComparator;
import com.kaltura.media.quality.event.Event;
import com.kaltura.media.quality.event.EventsManager;
import com.kaltura.media.quality.event.SegmentErrorEvent;
import com.kaltura.media.quality.event.listener.IListener;
import com.kaltura.media.quality.event.listener.ISegmentsListener;
import com.kaltura.media.quality.event.listener.ISegmentsResultsListener;
import com.kaltura.media.quality.model.Segment;
import com.kaltura.media.quality.utils.ImageUtils;
import com.kaltura.media.quality.utils.ThreadManager;

/**
 * Compares between segments from different renditions by comparing the first frame 
 */
public class RenditionsComparator extends Validator implements ISegmentsListener {
	
	private static final Logger log = Logger.getLogger(RenditionsComparator.class);
	
	private KalturaLiveEntry entry;

	class SegmentsResultsEvent extends Event<ISegmentsResultsListener>{

		private Segment segment1;
		private Segment segment2;
		private double diff;

		public SegmentsResultsEvent(Segment segment1, Segment segment2, double diff) {
			super(ISegmentsResultsListener.class);
			
			this.segment1 = segment1;
			this.segment2 = segment2;
			this.diff = diff;
		}

		@Override
		public void callListener(ISegmentsResultsListener listener) {
			listener.onSegmentsResult(segment1.getEntryId(), segment1.getNumber(), segment1.getRendition().getBandwidth(), segment2.getRendition().getBandwidth(), diff);
		}
		
	}
	
	class ValidationTask implements Runnable{

		private List<Segment> segments;
		
		public ValidationTask(List<Segment> segments) {
			this.segments = segments;
		}

		@Override
		public void run() {
			Segment segment1 = segments.get(0);
			File file1 = segment1.getFile();
			File file2;

			Thread thread = Thread.currentThread();
			thread.setPriority(Thread.MIN_PRIORITY);
			thread.setName(getClass().getSimpleName() + "-" + file1.getAbsolutePath());
			
			File image1;
			try {
				image1 = getFirstFrameFromFile(file1);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				EventsManager.get().raiseEvent(new SegmentErrorEvent(segment1, e));
				return;
			}
			File image2;

			String outputFolder = file1.getParent();
			String outputName;
			String diffPath;
			double diff;
			
			// Check 1-2, 2-3, 3-4, ...
			for(int i = 1 ; i < segments.size(); ++i) {
				
				if(!ThreadManager.shouldContinue()){
					break;
				}
				
				Segment segment2 = segments.get(i);
				file2 = segment2.getFile();
				try {
					image2 = getFirstFrameFromFile(file2);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					EventsManager.get().raiseEvent(new SegmentErrorEvent(segment2, e));
					continue;
				}
				outputName = file2.getParentFile().getParentFile().getName();
				diffPath = outputFolder + "/diff_" + segment1.getNumber() + "_" + outputName + ".jpg";
				
				log.info("Comparison of entry [" + entry.id + "]" + file1 + " and " + file2);
				ImageMagikComparator imComparator = new ImageMagikComparator(diffPath);
				diff = imComparator.compare(image1, image2);
				EventsManager.get().raiseEvent(new SegmentsResultsEvent(segment1, segment2, diff));
				
				file1 = file2;
				image1 = image2;
			}
		}
		
	}
	
	public RenditionsComparator(KalturaLiveEntry entry) {
		super();
		
		this.entry = entry;
		EventsManager.get().addListener(ISegmentsListener.class, this);
	}
	
	private File getFirstFrameFromFile(File ts) throws Exception {
		File dest = new File(ts.getAbsolutePath() + ".jpeg");
		//	save first frame to file
		ImageUtils.saveFirstFrame(ts, dest);
		return dest;
	}

	@Override
	public void onSegmentsDownloadComplete(List<Segment> segments) {
		if(!segments.get(0).getEntryId().equals(entry.id)){
			return;
		}

		ThreadManager.start(new ValidationTask(segments));
	}

	@Override
	public int compareTo(IListener o) {
		if(o == this){
			return 0;
		}
		return 1;
	}
}