package com.kaltura.media.quality.provider;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.media.quality.configurations.DataProvider;
import com.kaltura.media.quality.event.Event;
import com.kaltura.media.quality.event.EventsManager;
import com.kaltura.media.quality.event.listener.IListener;
import com.kaltura.media.quality.event.listener.ISegmentInfoListener;
import com.kaltura.media.quality.event.listener.ISegmentListener;
import com.kaltura.media.quality.model.Segment;
import com.kaltura.media.quality.utils.ThreadManager;
import com.kaltura.media.quality.validator.info.MediaInfoBase;
import com.kaltura.media.quality.validator.info.MediaInfoBase.Info;

/**
 * Check media info on each downloaded segment
 */
public class MediaInfoParser extends Provider implements ISegmentListener {

	private static final Logger log = Logger.getLogger(MediaInfoParser.class);

	Queue<Segment> queue = new ConcurrentLinkedQueue<Segment>();
	private String uniqueId;
	private List<String> mediaParsers;

	@SuppressWarnings("unchecked")
	public MediaInfoParser(String uniqueId, DataProvider providerConfig) {
		super();

		this.uniqueId = uniqueId;	
		this.mediaParsers = (List<String>) providerConfig.getOtherProperty("mediaParsers");		
		EventsManager.get().addListener(ISegmentListener.class, this);
	}

	public MediaInfoParser (KalturaLiveEntry entry, DataProvider providerConfig) {
		this(entry.id, providerConfig);
	}

	class SegmentInfoEvent extends Event<ISegmentInfoListener>{
		private Segment segment;
		private List<Info> infos;
		
		public SegmentInfoEvent(Segment segment, List<Info> infos) {
			super(ISegmentInfoListener.class);

			this.segment = segment;
			this.infos = infos;
		}

		@Override
		public void callListener(ISegmentInfoListener listener) {
			listener.onSegmentInfo(segment, infos);
		}
		
	}
	
	@Override
	public void run() {
		Thread thread = Thread.currentThread();
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.setName(getClass().getSimpleName() + "-" + uniqueId);

		while (ThreadManager.shouldContinue()) {
			while(ThreadManager.shouldContinue() && !queue.isEmpty()){
				Segment segment = queue.poll();
				try {
					handleSegment(segment);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}
	
	private void handleSegment(Segment segment) throws Exception {
		List<MediaInfoBase.Info> infos = new ArrayList<MediaInfoBase.Info>();
		
		for(String mediaParserClassName : mediaParsers){
			@SuppressWarnings("unchecked")
			Class<MediaInfoBase> mediaParserClass = (Class<MediaInfoBase>) Class.forName(mediaParserClassName);
			Constructor<MediaInfoBase> constructor = mediaParserClass.getConstructor(File.class);
			MediaInfoBase mediaParser = constructor.newInstance(segment.getFile());
			infos.add(mediaParser.getInfo());
		}
		EventsManager.get().raiseEvent(new SegmentInfoEvent(segment, infos));
	}

	@Override
	public int compareTo(IListener o) {
		if(o == this){
			return 0;
		}
		return 1;
	}

	@Override
	public void onSegmentDownloadStart(Segment segment) {
	}

	@Override
	public void onSegmentDownloadComplete(Segment segment) {
		if(!segment.getEntryId().equals(uniqueId)){
			return;
		}
		
		queue.offer(segment);
	}
}
