package com.kaltura.media.server.managers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.kaltura.media.server.KalturaEventsManager;
import com.kaltura.media.server.events.IKalturaEvent;
import com.kaltura.media.server.events.IKalturaEventConsumer;
import com.kaltura.media.server.events.KalturaEventType;
import com.kaltura.media.server.events.KalturaStreamEvent;

public abstract class KalturaCuePointsManager implements ICuePointsManager, IKalturaEventConsumer {

	protected static Logger logger = Logger.getLogger(KalturaCuePointsManager.class);
	
	private Map<Integer, CuePointsCreator> cuePointsCreators = new HashMap<Integer, CuePointsCreator>();
	
	@SuppressWarnings("serial")
	class CuePointsCreator extends HashMap<String, Date>{
		public Timer timer;
		public int interval;
		
		public CuePointsCreator(int interval){
			this.interval = interval;
			
			TimerTask timerTask = new TimerTask(){

				@Override
				public void run() {
					Date now = new Date();
					for(String entryId : keySet()){
						Date stopTime = get(entryId);
						if(now.after(stopTime)){
							remove(entryId);
						}
						else{
							createTimeCuePoint(entryId);
						}
					}
				}
			};

			timer = new Timer(true);
			timer.schedule(timerTask, 0, interval * 1000);
		}
	}
	
	@Override
	public void init() throws KalturaManagerException {
		KalturaEventsManager.registerEventConsumer(this, KalturaEventType.STREAM_UNPUBLISHED);
	}

	@Override
	public void onEvent(IKalturaEvent event){
		KalturaStreamEvent streamEvent;

		if(event.getType() instanceof KalturaEventType){
			switch((KalturaEventType) event.getType())
			{
				case STREAM_UNPUBLISHED:
					streamEvent = (KalturaStreamEvent) event;
					onUnPublish(streamEvent.getEntryId());
					break;
					
				default:
					break;
			}
		}
	}
	
	protected void onUnPublish(String entryId) {
		for(CuePointsCreator cuePointsCreator: cuePointsCreators.values()){
			if(cuePointsCreator.containsKey(entryId)){
				cuePointsCreator.remove(entryId);
				if(cuePointsCreator.size() == 0){
					cuePointsCreator.timer.cancel();
					cuePointsCreator.timer.purge();
					cuePointsCreators.remove(cuePointsCreator.interval);
				}
			}
		}
	}

	@Override
	public void stop() {
		for(CuePointsCreator cuePointsCreator: cuePointsCreators.values()){
			cuePointsCreator.timer.cancel();
			cuePointsCreator.timer.purge();
		}
	}

	public boolean createTimeCuePoints(String liveEntryId, int interval, int duration){

		Date stopTime = new Date();
		stopTime.setTime(stopTime.getTime() + (duration * 1000000));
		
		CuePointsCreator cuePointsCreator;
		if(cuePointsCreators.containsKey(interval)){
			cuePointsCreator = cuePointsCreators.get(interval);
		}
		else{
			cuePointsCreator = new CuePointsCreator(interval);
			cuePointsCreators.put(interval, cuePointsCreator);
		}
		cuePointsCreator.put(liveEntryId, stopTime);
		
		return true;
	}
}
