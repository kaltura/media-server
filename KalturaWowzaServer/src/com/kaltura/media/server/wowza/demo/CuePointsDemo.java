package com.kaltura.media.server.wowza.demo;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.kaltura.infra.StringUtils;
import com.kaltura.media.server.managers.KalturaCuePointsManager;
import com.kaltura.media.server.managers.KalturaManagerException;
import com.kaltura.media.server.wowza.CuePointsManager;
import com.wowza.wms.application.IApplicationInstance;

public class CuePointsDemo extends CuePointsManager {

	protected static Logger logger = Logger.getLogger(KalturaCuePointsManager.class);
	
	private Timer timer = null;

	@Override
	protected void onAppStart(final IApplicationInstance applicationInstance) {
		
		if(timer == null){
			final Date start = new Date();
			
			TimerTask timerTask = new TimerTask() {
				
				@Override
				public void run() {
	
					logger.info("Running");
					
					Date now = new Date();
					double offset = now.getTime() - start.getTime();
					String id = StringUtils.getUniqueId();
						
					synchronized (streams) {
						for(String entryId : streams.keySet()){
							try {
								sendSyncPoint(entryId, id, offset);
							} catch (KalturaManagerException e) {
								logger.error(e.getMessage());
							}
						}
					}
				}
			};
			
			timer = new Timer(true);
			timer.schedule(timerTask, 3000, 3000);
			
			logger.info("Started");
		}

		super.onAppStart(applicationInstance);
	}
}
