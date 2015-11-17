package com.kaltura.media.quality.validator.logger;

import java.io.IOException;

import com.kaltura.media.quality.configurations.LoggerConfig;
import com.kaltura.media.quality.event.EventsManager;
import com.kaltura.media.quality.event.listener.IListener;
import com.kaltura.media.quality.event.listener.ISegmentsResultsListener;

public class RenditionsCompareResultsLogger extends ResultsLogger implements ISegmentsResultsListener {

	public RenditionsCompareResultsLogger(String uniqueId, LoggerConfig loggerConfig) throws IOException {
		super(uniqueId, loggerConfig.getName());
		EventsManager.get().addListener(ISegmentsResultsListener.class, this);
	}

	class RenditionsCompareResult implements IResult{
		private int segmentNumber;
		private int bitrate1;
		private int bitrate2;
		private double diff;
		
		public RenditionsCompareResult(int segmentNumber, int bitrate1, int bitrate2, double diff) {
			this.segmentNumber = segmentNumber;
			this.bitrate1 = bitrate1;
			this.bitrate2 = bitrate2;
			this.diff = diff;
		}

		@Override
		public Object[] getValues(){
			return new Object[]{
				segmentNumber,
				bitrate1,
				bitrate2,
				diff
			};
		}

		@Override
		public String[] getHeaders() {
			return new String[]{
					"Segment Number",
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
	public void onSegmentsResult(String entryId, int segmentNumber, int bitrate1, int bitrate2, double diff) {
		if(!entryId.equals(uniqueId)){
			return;
		}

		RenditionsCompareResult result = new RenditionsCompareResult(segmentNumber, bitrate1, bitrate2, diff);
		write(result);
	}
}
