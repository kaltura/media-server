package com.kaltura.media.quality.validator;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.google.zxing.NotFoundException;
import com.kaltura.media.quality.configurations.DataValidator;
import com.kaltura.media.quality.event.Event;
import com.kaltura.media.quality.event.EventsManager;
import com.kaltura.media.quality.event.listener.IListener;
import com.kaltura.media.quality.event.listener.IQRCodeCompareResultsListener;
import com.kaltura.media.quality.event.listener.ISegmentFirstFrameImageListener;
import com.kaltura.media.quality.event.listener.ISegmentInfoListener;
import com.kaltura.media.quality.model.IFrame;
import com.kaltura.media.quality.model.Segment;
import com.kaltura.media.quality.utils.QRCodeReader;
import com.kaltura.media.quality.utils.ThreadManager;
import com.kaltura.media.quality.validator.info.FFPROBE;
import com.kaltura.media.quality.validator.info.MediaInfoBase;

/**
 * Compares between segment first frame pts and first frame QR code 
 */
public class QRCodeComparator extends Validator implements ISegmentFirstFrameImageListener, ISegmentInfoListener {
	private static final long serialVersionUID = -9056394196720364433L;
	private static final Logger log = Logger.getLogger(QRCodeComparator.class);
	
	private String uniqueId;
	private boolean deffered;

	private Map<String, ValidationTask> validationTasks = new ConcurrentHashMap<String, ValidationTask>();

	class QRCodeCompareResultsEvent extends Event<IQRCodeCompareResultsListener>{
		private static final long serialVersionUID = -8861129318344410220L;
		
		private Segment segment;
		private File image;
		private IFrame frame;
		private double qrCode;

		public QRCodeCompareResultsEvent(Segment segment, File image, IFrame frame, double qrCode) {
			super(IQRCodeCompareResultsListener.class);

			this.segment = segment;
			this.image = image;
			this.frame = frame;
			this.qrCode = qrCode;
		}

		@Override
		protected void callListener(IQRCodeCompareResultsListener listener) {
			listener.onQRCodeCompareResult(segment, image, frame, qrCode);
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
	
	class ValidationTask implements Runnable{

		private Segment segment;
		private File image;
		private IFrame frame;
		
		public ValidationTask(Segment segment, File image) {
			this.segment = segment;
			this.image = image;
		}

		public ValidationTask(Segment segment, IFrame frame) {
			this.segment = segment;
			this.frame = frame;
		}

		@Override
		public void run() {
			compare(segment, image, frame);
		}

		public void setFrame(IFrame frame) {
			this.frame = frame;
		}

		public void setImage(File image) {
			this.image = image;
		}
	}

	public QRCodeComparator() {
		super();
	}
	

	public QRCodeComparator(String uniqueId, DataValidator dataValidator) {
		this();
		
		this.uniqueId = uniqueId;
		this.deffered = dataValidator.getDeffered();
		
		register();
	}

	@Override
	public void register() {
		EventsManager eventsManager = EventsManager.get();
		eventsManager.addListener(ISegmentFirstFrameImageListener.class, this);
		eventsManager.addListener(ISegmentInfoListener.class, this);
	}
	
	@Override
	public void onSegmentFirstFrameCapture(Segment segment, File image) {
		if(!segment.getEntryId().equals(uniqueId)){
			return;
		}

		ValidationTask validationTask;
		String validationTaskKey = segment.getFile().getAbsolutePath();
		if(validationTasks.containsKey(validationTaskKey)){
			validationTask = validationTasks.get(validationTaskKey);
			validationTask.setImage(image);
		}
		else{
			validationTask = new ValidationTask(segment, image);
			validationTasks.put(validationTaskKey, validationTask);
			return;
		}
		
		if(isDeffered() || EventsManager.isConsumingDefferedEvents()){
			validationTask.run();
		}
		else{
			ThreadManager.start(validationTask);
		}
	}

	@Override
	public void onSegmentInfo(Segment segment, List<MediaInfoBase.Info> infos) {
		if(!segment.getEntryId().equals(uniqueId)){
			return;
		}
		
		IFrame frame = null;
		for(MediaInfoBase.Info info : infos){
			if(info instanceof FFPROBE.FFPROBEInfo){
				frame = ((FFPROBE.FFPROBEInfo) info).getFirstFrame();
				break;
			}
		}
		if(frame == null){
			return;
		}
		
		ValidationTask validationTask;
		String validationTaskKey = segment.getFile().getAbsolutePath();
		if(validationTasks.containsKey(validationTaskKey)){
			validationTask = validationTasks.get(validationTaskKey);
			validationTask.setFrame(frame);
		}
		else{
			validationTask = new ValidationTask(segment, frame);
			validationTasks.put(validationTaskKey, validationTask);
			return;
		}
		
		if(isDeffered() || EventsManager.isConsumingDefferedEvents()){
			validationTask.run();
		}
		else{
			ThreadManager.start(validationTask);
		}
	}
	
	protected double qrCodeToDouble(String qrCode){
		String[] qrCodeParts = qrCode.split(" ");
		String[] timeParts = qrCodeParts[1].split(":");
		double time = 0;
		double multiplier = 1;
		for(int i = timeParts.length - 1; i >= 0; i--){
			time += (Double.valueOf(timeParts[i]) * multiplier);
			multiplier *= 60;
		}
		
		return time;
	}
	
	protected void compare(Segment segment, File image, IFrame frame){
		String qrCode;
		try {
			qrCode = QRCodeReader.readQRCode(image);
		} catch (NotFoundException | IOException e) {
			log.error(e.getMessage(), e);
			return;
		}
		
		double timestamp = qrCodeToDouble(qrCode); 
		EventsManager.get().raiseEvent(new QRCodeCompareResultsEvent(segment, image, frame, timestamp));
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