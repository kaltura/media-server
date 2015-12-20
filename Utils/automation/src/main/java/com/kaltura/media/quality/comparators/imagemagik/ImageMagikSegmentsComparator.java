package com.kaltura.media.quality.comparators.imagemagik;

import org.apache.log4j.Logger;

import com.kaltura.media.quality.comparators.SegmentsComparator;
import com.kaltura.media.quality.configurations.SegmentsComparatorConfig;
import com.kaltura.media.quality.event.Event;
import com.kaltura.media.quality.event.EventsManager;
import com.kaltura.media.quality.event.SegmentErrorEvent;
import com.kaltura.media.quality.event.listener.ISegmentFirstFrameImageListener;
import com.kaltura.media.quality.event.listener.ISegmentsResultsListener;
import com.kaltura.media.quality.model.Segment;
import com.kaltura.media.quality.utils.ImageUtils;
import com.kaltura.media.quality.utils.ThreadManager;

import java.io.File;
import java.util.List;

public class ImageMagikSegmentsComparator implements SegmentsComparator {
	private static final Logger log = Logger.getLogger(ImageMagikSegmentsComparator.class);

	class SegmentFirstFrameImageEvent extends Event<ISegmentFirstFrameImageListener> {
		private static final long serialVersionUID = -1970944510668478236L;
		private Segment segment;
		private File frame;
	
		public SegmentFirstFrameImageEvent(Segment segment, File frame) {
			super(ISegmentFirstFrameImageListener.class);
			
			this.segment = segment;
			this.frame = frame;
		}

		@Override
		protected void callListener(ISegmentFirstFrameImageListener listener) {
			listener.onSegmentFirstFrameCapture(segment, frame);
		}
	
		@Override
		protected String getTitle() {
			String title = segment.getEntryId();
			title += "-" + segment.getRendition().getDomainHash();
			title += "-" + segment.getRendition().getBandwidth();
			title += "-" + segment.getNumber();
			return title;
		}
	
	}

	class SegmentsResultsEvent extends Event<ISegmentsResultsListener>{
		private static final long serialVersionUID = -8330040440848338116L;
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
		protected void callListener(ISegmentsResultsListener listener) {
			listener.onSegmentsResult(segment1, segment2, diff);
		}

		@Override
		protected String getTitle() {
			String title = segment1.getEntryId();
			title += "-" + segment1.getRendition().getDomainHash();
			title += "-" + segment1.getRendition().getBandwidth();
			title += "-" + segment1.getNumber();
			title += "-" + segment2.getNumber();
			return title;
		}
		
	}
	
	public ImageMagikSegmentsComparator(SegmentsComparatorConfig comparatorConfig){
	}

	private File getFirstFrameFromFile(Segment segment) throws Exception {
		File ts = segment.getFile();
		File dest = new File(ts.getAbsolutePath() + ".jpeg");
		if(!dest.exists()){
			//	save first frame to file
			ImageUtils.saveFirstFrame(ts, dest);
			EventsManager.get().raiseEvent(new SegmentFirstFrameImageEvent(segment, dest));
		}
		
		return dest;
	}
	
	@Override
	public void compare(List<Segment> segments) {
		Segment sourceSegment = null;
		for(Segment segment: segments){
			if(sourceSegment == null || sourceSegment.getRendition().getBandwidth() < segment.getRendition().getBandwidth()){
				sourceSegment = segment;
			}	
		}
		File sourceFile = sourceSegment.getFile();
		File compareFile;

		Thread thread = Thread.currentThread();
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.setName(getClass().getSimpleName() + "-" + sourceFile.getAbsolutePath());
		
		File sourceImage;
		try {
			sourceImage = getFirstFrameFromFile(sourceSegment);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			EventsManager.get().raiseEvent(new SegmentErrorEvent(sourceSegment, e));
			return;
		}
		File compareImage;

		String outputFolder = sourceFile.getParent();
		String outputName;
		String diffPath;
		double diff;
		
		// Check 1-2, 2-3, 3-4, ...
		for(Segment compareSegment: segments){
			
			if(!ThreadManager.shouldContinue()){
				break;
			}
			
			if(compareSegment == sourceSegment){
				EventsManager.get().raiseEvent(new SegmentsResultsEvent(sourceSegment, compareSegment, 0));
				continue;
			}
			
			compareFile = compareSegment.getFile();
			try {
				compareImage = getFirstFrameFromFile(compareSegment);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				EventsManager.get().raiseEvent(new SegmentErrorEvent(compareSegment, e));
				continue;
			}
			outputName = compareFile.getParentFile().getParentFile().getName();
			diffPath = outputFolder + "/diff_" + sourceSegment.getNumber() + "_" + outputName + ".jpg";
			
			log.info("Comparison of entry [" + sourceSegment.getEntryId() + "]" + sourceFile + " and " + compareFile);
			ImageMagikImageComparator imComparator = new ImageMagikImageComparator(diffPath);
			diff = imComparator.compare(sourceImage, compareImage);
			EventsManager.get().raiseEvent(new SegmentsResultsEvent(sourceSegment, compareSegment, diff));
		}
	}
}
