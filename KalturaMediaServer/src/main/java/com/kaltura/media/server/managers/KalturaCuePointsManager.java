package com.kaltura.media.server.managers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.enums.KalturaCuePointOrderBy;
import com.kaltura.client.enums.KalturaCuePointStatus;
import com.kaltura.client.types.KalturaCuePoint;
import com.kaltura.client.types.KalturaCuePointFilter;
import com.kaltura.client.types.KalturaCuePointListResponse;
import com.kaltura.client.types.KalturaFilterPager;
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

	protected final static int CUE_POINTS_LIST_MAX_ENTRIES = 30;
	protected final static int INFINITE_SYNC_POINT_DURATION = -1;
	
	protected static Logger logger = Logger.getLogger(KalturaCuePointsManager.class);

	private ConcurrentHashMap<Integer, CuePointsCreator> cuePointsCreators = new ConcurrentHashMap<Integer, CuePointsCreator>();
	private List<CuePointsLoader> cuePointsLoaders = new ArrayList<CuePointsLoader>();
	private CuePointsLoader currentCuePointsLoader;

	protected ILiveManager liveManager;


	/**
	 * Periodically check if additional pre-defined cue-points created
	 */
	@SuppressWarnings("serial")
	class CuePointsLoader extends ArrayList<String>{

		private int lastUpdatedCuePoint = 0;
		public Timer timer;
		
		public CuePointsLoader(){
			
			TimerTask timerTask = new TimerTask() {
				@Override
				public void run() {
					load(getEntryIds(), true);
				}
			};
			
			timer = new Timer(true);
			timer.schedule(timerTask, 60000, 60000);
		}
		
		public List<String> getEntryIds() {
			synchronized(this){
				return this;
			}
		}
		
		public boolean isFull() {
			return size() >= KalturaCuePointsManager.CUE_POINTS_LIST_MAX_ENTRIES;
		}

		public void add(KalturaLiveEntry liveEntry) {
			add(liveEntry.id);
			
			List<String> entryIds = new ArrayList<String>();
			entryIds.add(liveEntry.id);
			load(entryIds, false);
		}

		public void load(final List<String> entryIds, boolean periodic) {
			boolean setLastUpdatedAt = periodic || lastUpdatedCuePoint == 0;
			
			// list all cue-points that should be triggered by absolute time
			KalturaCuePointFilter filter = new KalturaCuePointFilter();
			filter.entryIdEqual = StringUtils.join(entryIds);
			filter.statusEqual = KalturaCuePointStatus.READY;
			filter.triggeredAtGreaterThanOrEqual = 0;
			if(periodic){
				filter.updatedAtGreaterThanOrEqual = lastUpdatedCuePoint;
			}
			
			filter.orderBy = KalturaCuePointOrderBy.UPDATED_AT_ASC.hashCode;

			KalturaFilterPager pager = new KalturaFilterPager();
			pager.pageIndex = 1;
			pager.pageSize = 100;

			Timer timer;
			TimerTask timerTask;
			Date date = new Date();
			
			try{
				while(true){
					KalturaCuePointListResponse CuePointsList = client.getCuePointService().list(filter , pager);
					if(CuePointsList.objects.size() == 0){
						break;
					}

					for(final KalturaCuePoint cuePoint : CuePointsList.objects){
						if(setLastUpdatedAt){
							lastUpdatedCuePoint = Math.max(lastUpdatedCuePoint, cuePoint.updatedAt);
						}
						
						// create sync-point 30 seconds before the trigger time
						date.setTime((cuePoint.triggeredAt / 1000) - 30000);

						timerTask = new TimerTask(){
							@Override
							public void run() {
								createSyncPoint(cuePoint.entryId);
							}
						};
						
						timer = new Timer(true);
						timer.schedule(timerTask, date);
					}
				}
			} catch (KalturaApiException e) {
				logger.error("Failed to list entries cue-points: " + e.getMessage());
			}
		}
	}
	
	@SuppressWarnings("serial")
	class CuePointsCreator extends HashMap<String, Date>{
		public Timer timer;
		public int interval;
		
		public CuePointsCreator(int interval){
			this.interval = interval;
			
			TimerTask timerTask = new TimerTask(){

				@Override
				public void run() {
					try {
						logger.info("Running sync point creator timer");
						Date now = new Date();
						for(String entryId : keySet()){
							Date stopTime = get(entryId);
							if(stopTime != null && now.after(stopTime)){
								logger.info("Stop time reached or exceeded");
								remove(entryId);
							}
							else{
								createSyncPoint(entryId);
							
							}
						}
					}
					catch (Exception e) {
						logger.error("An error occured while running the sync-point creator timer: [" + e.getMessage() + "]");
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
		KalturaEventsManager.registerEventConsumer(this, KalturaEventType.STREAM_PUBLISHED, KalturaEventType.STREAM_UNPUBLISHED, KalturaEventType.METADATA);
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
	
				case STREAM_PUBLISHED:
					onPublish(((KalturaStreamEvent) event).getEntry());
					break;

				case METADATA:
					onMetadata(((KalturaMetadataEvent) event).getEntry(), ((KalturaMetadataEvent) event).getObject());
					break;
					
				default:
					break;
			}
		}
	}

	private void onPublish(final KalturaLiveEntry liveEntry) {
		synchronized(cuePointsLoaders){
			if(currentCuePointsLoader == null || currentCuePointsLoader.isFull()){
				currentCuePointsLoader = new CuePointsLoader();
				cuePointsLoaders.add(currentCuePointsLoader);
			}
			synchronized(cuePointsLoaders){
				currentCuePointsLoader.add(liveEntry);
			}
		}
	}

	protected void onMetadata(KalturaLiveEntry entry, KalturaObjectBase object) {
		
		if(object instanceof KalturaCuePoint){
			onCuePoint(entry, (KalturaCuePoint) object);
		}
	}

	protected void onCuePoint(KalturaLiveEntry entry, KalturaCuePoint cuePoint) {
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
				synchronized (cuePointsCreator) {
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
		synchronized (cuePointsLoaders) {
			for(CuePointsLoader cuePointsLoader: cuePointsLoaders){
				synchronized(cuePointsLoader){
					if(cuePointsLoader.contains(entryId)){
						cuePointsLoader.remove(entryId);
						if(cuePointsLoader.size() == 0){
							cuePointsLoader.timer.cancel();
							cuePointsLoader.timer.purge();
							cuePointsLoaders.remove(cuePointsLoader);
							
							if(cuePointsLoader == currentCuePointsLoader){
								currentCuePointsLoader = null;
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void stop() {
		try {
			synchronized (cuePointsCreators) {
				for(CuePointsCreator cuePointsCreator: cuePointsCreators.values()){
					synchronized (cuePointsCreator) {
						cuePointsCreator.timer.cancel();
						cuePointsCreator.timer.purge();
						cuePointsCreators.remove(cuePointsCreator.interval);
					}
				}
			}
			synchronized (cuePointsLoaders) {
				for(CuePointsLoader cuePointsLoader: cuePointsLoaders){
					synchronized (cuePointsLoader) {
						cuePointsLoader.timer.cancel();
						cuePointsLoader.timer.purge();
						cuePointsLoaders.remove(cuePointsLoader);
					}
				}
			}
		} catch (Exception e ) {
			logger.error("Error occurred: " + e.getMessage());
		}
	}

	/**
	 * To enable sync points without specific end time -1 needs to be passed as the value of duration 
	 */
	public void createPeriodicSyncPoints(String liveEntryId, int interval, int duration){
		createSyncPoint(liveEntryId);
		
		Date stopTime = new Date();
		stopTime.setTime(stopTime.getTime() + (duration * 1000));
		
		CuePointsCreator cuePointsCreator;
		synchronized (cuePointsCreators) {
			if(cuePointsCreators.containsKey(interval)){
				cuePointsCreator = cuePointsCreators.get(interval);
			}
			else{
				cuePointsCreator = new CuePointsCreator(interval);
				cuePointsCreators.put(interval, cuePointsCreator);
			}
			
			if(duration == KalturaCuePointsManager.INFINITE_SYNC_POINT_DURATION){
				cuePointsCreator.put(liveEntryId, null);
			}
			else{
				cuePointsCreator.put(liveEntryId, stopTime);
			}
		}
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
