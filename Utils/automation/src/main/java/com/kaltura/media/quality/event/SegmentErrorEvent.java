package com.kaltura.media.quality.event;

import com.kaltura.media.quality.event.listener.ISegmentErrorListener;
import com.kaltura.media.quality.model.Segment;

public class SegmentErrorEvent extends Event<ISegmentErrorListener> {
	private static final long serialVersionUID = -1925190826326234148L;
	private Segment segment;
	private Exception error;
	
	public SegmentErrorEvent(Segment segment, Exception error) {
		super(ISegmentErrorListener.class);

		this.segment = segment;
		this.error = error;
	}

	@Override
	protected void callListener(ISegmentErrorListener listener) {
		listener.onSegmentError(segment, error);
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
