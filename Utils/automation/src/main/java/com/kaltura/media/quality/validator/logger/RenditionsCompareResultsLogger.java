package com.kaltura.media.quality.validator.logger;

import java.io.IOException;

import com.kaltura.media.quality.configurations.LoggerConfig;
import com.kaltura.media.quality.event.EventsManager;
import com.kaltura.media.quality.event.listener.IListener;
import com.kaltura.media.quality.event.listener.ISegmentsResultsListener;
import com.kaltura.media.quality.model.Segment;

public class RenditionsCompareResultsLogger extends ResultsLogger implements ISegmentsResultsListener {
	private static final long serialVersionUID = -8745677651202237364L;
	private boolean deffered;

	public RenditionsCompareResultsLogger() {
		super();
	}

	public RenditionsCompareResultsLogger(String uniqueId, LoggerConfig loggerConfig) throws IOException {
		super(uniqueId, loggerConfig.getName());
		this.deffered = loggerConfig.getDeffered();
		EventsManager.get().addListener(ISegmentsResultsListener.class, this);
	}

	class RenditionsCompareResult implements IResult{
		private Segment segment1;
		private Segment segment2;
		private double diff;
		
		public RenditionsCompareResult(Segment segment1, Segment segment2, double diff) {
			this.segment1 = segment1;
			this.segment2 = segment2;
			this.diff = diff;
		}

		@Override
		public Object[] getValues(){
			return new Object[]{
				segment1.getNumber(),
				segment1.getRendition().getDomain(),
				segment1.getRendition().getBandwidth(),
				segment2.getRendition().getBandwidth(),
				diff
			};
		}

		@Override
		public String[] getHeaders() {
			return new String[]{
				"Segment Number",
				"Domain",
				"Bitrate 1",
				"Bitrate 2",
				"Diff"
			};
		}
	}
	
	protected void write(RenditionsCompareResult result) {
		super.write(result);
	}

	@Override
	public int compareTo(IListener o) {
		if(o == this){
			return 0;
		}
		
		return 1;
	}

	@Override
	public void onSegmentsResult(Segment segment1, Segment segment2, double diff) {
		if(!segment1.getEntryId().equals(uniqueId)){
			return;
		}

		RenditionsCompareResult result = new RenditionsCompareResult(segment1, segment2, diff);
		write(result);
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
