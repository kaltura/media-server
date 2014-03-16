package com.kaltura.media.server.managers;

import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.types.KalturaCuePoint;
import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.infra.StringUtils;
import com.kaltura.media.server.KalturaEventsManager;
import com.kaltura.media.server.KalturaServer;
import com.kaltura.media.server.events.IKalturaEvent;
import com.kaltura.media.server.events.IKalturaEventConsumer;
import com.kaltura.media.server.events.KalturaEventType;
import com.kaltura.media.server.events.KalturaMetadataEvent;
import com.kaltura.media.server.events.KalturaStreamEvent;

public abstract class KalturaCuePointsManager extends KalturaManager implements ICuePointsManager, IKalturaEventConsumer {

	protected static Logger logger = Logger.getLogger(KalturaCuePointsManager.class);
	
	private ConcurrentHashMap<Integer, CuePointsCreator> cuePointsCreators = new ConcurrentHashMap<Integer, CuePointsCreator>();

	protected ILiveManager liveManager;
	
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
							createSyncPoint(entryId);
						}
					}
				}
			};

			timer = new Timer(true);
			timer.schedule(timerTask, interval * 1000, interval * 1000);
		}
	}
	
	@Override
	public void init() throws KalturaManagerException {
		super.init();
		KalturaEventsManager.registerEventConsumer(this, KalturaEventType.STREAM_UNPUBLISHED, KalturaEventType.METADATA);
		liveManager = (ILiveManager) KalturaServer.getManager(ILiveManager.class);
	}

	@Override
	public void onEvent(IKalturaEvent event){

		if(event.getType() instanceof KalturaEventType){
			switch((KalturaEventType) event.getType())
			{
				case STREAM_UNPUBLISHED:
					onUnPublish(((KalturaStreamEvent) event).getEntryId());
					break;

				case METADATA:
					onMetadata(((KalturaMetadataEvent) event).getEntry(), ((KalturaMetadataEvent) event).getObject());
					break;
					
				default:
					break;
			}
		}
	}

	private void onMetadata(KalturaLiveEntry entry, KalturaObjectBase object) {
		
		if(object instanceof KalturaCuePoint){
			onCuePoint(entry, (KalturaCuePoint) object);
		}
	}

	private void onCuePoint(KalturaLiveEntry entry, KalturaCuePoint cuePoint) {
		KalturaClient impersonateClient = impersonate(entry.partnerId);
		
		cuePoint.entryId = entry.id;
		try {
			impersonateClient.getCuePointService().add(cuePoint);
		} catch (KalturaApiException e) {
			logger.equals("Failed adding cue-point to entry [" + entry.id + "]: " + e.getMessage());
		}
		
		impersonateClient = null;
	}

	protected void onUnPublish(String entryId) {
		synchronized (cuePointsCreators) {
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
	}

	@Override
	public void stop() {
		synchronized (cuePointsCreators) {
			for(CuePointsCreator cuePointsCreator: cuePointsCreators.values()){
				cuePointsCreator.timer.cancel();
				cuePointsCreator.timer.purge();
				cuePointsCreators.remove(cuePointsCreator.interval);
			}
		}
	}

	public void createPeriodicSyncPoints(String liveEntryId, int interval, int duration){
		createSyncPoint(liveEntryId);
		
		Date stopTime = new Date();
		stopTime.setTime(stopTime.getTime() + (duration * 1000000));
		
		CuePointsCreator cuePointsCreator;
		synchronized (cuePointsCreators) {
			if(cuePointsCreators.containsKey(interval)){
				cuePointsCreator = cuePointsCreators.get(interval);
			}
			else{
				cuePointsCreator = new CuePointsCreator(interval);
				cuePointsCreators.put(interval, cuePointsCreator);
			}
		}
		cuePointsCreator.put(liveEntryId, stopTime);
	}

	@Override
	public void createSyncPoint(String entryId) {

		KalturaLiveEntry liveEntry = liveManager.get(entryId);
		String id = StringUtils.getUniqueId();
			
		try{
			sendSyncPoint(entryId, id, getEntryCurrentTime(liveEntry));
		} catch (KalturaManagerException e) {
			logger.error("Failed sending sync-point [" + id + "] to entry [" + entryId + "]: " + e.getMessage());
		}
	}
}
