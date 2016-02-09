package com.kaltura.media.quality.validator.logger;

import java.io.File;
import java.io.IOException;

import com.kaltura.media.quality.configurations.LoggerConfig;
import com.kaltura.media.quality.event.EventsManager;
import com.kaltura.media.quality.event.listener.IListener;
import com.kaltura.media.quality.event.listener.IQRCodeCompareResultsListener;
import com.kaltura.media.quality.model.IFrame;
import com.kaltura.media.quality.model.Segment;

public class QRCodeCompareResultsLogger extends ResultsLogger implements IQRCodeCompareResultsListener {
	private static final long serialVersionUID = -8745677651202237364L;
	private boolean deffered;

	public QRCodeCompareResultsLogger() {
		super();
	}

	public QRCodeCompareResultsLogger(String uniqueId, LoggerConfig loggerConfig) throws IOException {
		super(uniqueId, loggerConfig.getName());
		this.deffered = loggerConfig.getDeffered();
		
		register();
	}

	@Override
	public void register() {
		EventsManager.get().addListener(IQRCodeCompareResultsListener.class, this);
	}

	class QRCodeCompareResult implements IResult{
		private Segment segment;
		private File image;
		private IFrame frame;
		private double qrCode;
		
		public QRCodeCompareResult(Segment segment, File image, IFrame frame, double qrCode) {
			this.segment = segment;
			this.image = image;
			this.frame = frame;
			this.qrCode = qrCode;
		}

		@Override
		public Object[] getValues(){
			double diff = qrCode - frame.getPts();
			
			return new Object[]{
				segment.getNumber(),
				segment.getRendition().getProviderName(),
				segment.getRendition().getBandwidth(),
				frame.getDts(),
				frame.getPts(),
				frame.getKeyFrame(),
				qrCode,
				diff
			};
		}

		@Override
		public String[] getHeaders() {
			return new String[]{
				"Segment Number",
				"Provider",
				"Bitrate",
				"DTS",
				"PTS",
				"Key Frame",
				"QR Code Time",
				"Diff"
			};
		}

		public File getImage() {
			return image;
		}
	}
	
	protected void write(QRCodeCompareResult result) {
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
	public void onQRCodeCompareResult(Segment segment, File image, IFrame frame, double qrCode) {
		if(!segment.getEntryId().equals(uniqueId)){
			return;
		}

		QRCodeCompareResult result = new QRCodeCompareResult(segment, image, frame, qrCode);
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
