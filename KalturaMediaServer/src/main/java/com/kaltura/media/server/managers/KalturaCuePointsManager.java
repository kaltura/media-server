package com.kaltura.media.server.managers;

import com.kaltura.client.types.KalturaLiveEntry;
import com.kaltura.media.server.KalturaEventsManager;
import com.kaltura.media.server.KalturaServer;
import com.kaltura.media.server.events.IKalturaEvent;
import com.kaltura.media.server.events.IKalturaEventConsumer;
import com.kaltura.media.server.events.KalturaEventType;
import org.apache.log4j.Logger;

public class KalturaCuePointsManager extends KalturaManager implements ICuePointsManager, IKalturaEventConsumer {

	protected static Logger logger = Logger.getLogger(KalturaCuePointsManager.class);
	protected ILiveManager liveManager;

	@Override
	public void init() throws KalturaManagerException {
		super.init();
		KalturaEventsManager.registerEventConsumer(this, KalturaEventType.STREAM_PUBLISHED, KalturaEventType.STREAM_UNPUBLISHED, KalturaEventType.METADATA);
		liveManager = (ILiveManager) KalturaServer.getManager(ILiveManager.class);
	}

	@Override
	public void stop() {
		logger.debug("stop::NOT IMPLEMENTED");
	}

	@Override
	public void createPeriodicSyncPoints(String liveEntryId, int interval, int duration) {
		logger.error("createPeriodicSyncPoints::NOT IMPLEMENTED");
	}

	@Override
	public void createSyncPoint(String entryId) {

		logger.error("createSyncPoint::NOT IMPLEMENTED");
	}

	@Override
	public double getEntryCurrentTime(KalturaLiveEntry liveEntry) throws KalturaManagerException {
		logger.error("getEntryCurrentTime::NOT IMPLEMENTED. returning 0");
		return 0;
	}

	@Override
	public void sendSyncPoint(String entryId, String id, double offset) throws KalturaManagerException {
		logger.error("sendSyncPoint::NOT IMPLEMENTED");
	}

	@Override
	public void onEvent(IKalturaEvent event) {
		logger.error("onEvent::NOT IMPLEMENTED");
	}
}
