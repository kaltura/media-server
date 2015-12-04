package com.kaltura.media.quality.validator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Logger;

import com.kaltura.media.quality.comparators.SegmentsComparator;
import com.kaltura.media.quality.configurations.DataValidator;
import com.kaltura.media.quality.configurations.SegmentsComparatorConfig;
import com.kaltura.media.quality.event.EventsManager;
import com.kaltura.media.quality.event.listener.IListener;
import com.kaltura.media.quality.event.listener.ISegmentsListener;
import com.kaltura.media.quality.model.Segment;
import com.kaltura.media.quality.utils.ThreadManager;

/**
 * Compares between segments from different renditions by comparing the first frame 
 */
public class RenditionsComparator extends Validator implements ISegmentsListener {
	private static final long serialVersionUID = -8200877988082291537L;
	private static final Logger log = Logger.getLogger(RenditionsComparator.class);
	
	private String uniqueId;
	private boolean deffered;
	private List<SegmentsComparatorConfig> comparators;

	class ValidationTask implements Runnable{

		private List<Segment> segments;
		
		public ValidationTask(List<Segment> segments) {
			this.segments = segments;
		}

		@Override
		public void run() {
			try {
				handleSegments(segments);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	public RenditionsComparator() {
		super();
	}
	
	public RenditionsComparator(String uniqueId, DataValidator dataValidator) {
		this();
		
		this.uniqueId = uniqueId;
		this.deffered = dataValidator.getDeffered();
		this.comparators = dataValidator.getComparators();
		
		register();
	}

	@Override
	public void register() {
		EventsManager.get().addListener(ISegmentsListener.class, this);
	}

	@Override
	public void onSegmentsDownloadComplete(List<Segment> segments) {
		if(!segments.get(0).getEntryId().equals(uniqueId)){
			return;
		}

		if(!isDeffered() && !EventsManager.isConsumingDefferedEvents()){
			ThreadManager.start(new ValidationTask(segments));
			return;
		}
		
		try {
			handleSegments(segments);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	protected void handleSegments(List<Segment> segments) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		for(SegmentsComparatorConfig comparatorConfig : comparators){
			log.info("Create segments-comparator [" + comparatorConfig.getType().getName() + "]");
			Constructor<SegmentsComparator> constructor = comparatorConfig.getType().getConstructor(SegmentsComparatorConfig.class);
			SegmentsComparator comparator = constructor.newInstance(comparatorConfig);
			comparator.compare(segments);
		}
	}

	@Override
	public int compareTo(IListener o) {
		if(o == this){
			return 0;
		}
		return 1;
	}

	@Override
	public boolean isDeffered() {
		return deffered;
	}

	@Override
	public String getTitle() {
		return uniqueId;
	}
}