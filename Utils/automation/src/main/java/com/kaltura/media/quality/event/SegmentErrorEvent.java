package com.kaltura.media.quality.event;

import com.kaltura.media.quality.event.listener.ISegmentErrorListener;
import com.kaltura.media.quality.model.Segment;

public class SegmentErrorEvent extends Event<ISegmentErrorListener> {
	private Segment segment;
	private Exception error;
	
	public SegmentErrorEvent(Segment segment, Exception error) {
		super(ISegmentErrorListener.class);

		this.segment = segment;
		this.error = error;
	}

	@Override
	public void callListener(ISegmentErrorListener listener) {
		listener.onSegmentError(segment, error);
	}

}
