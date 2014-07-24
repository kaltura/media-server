package com.kaltura.media.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.kaltura.media.server.events.IKalturaEvent;
import com.kaltura.media.server.events.IKalturaEventConsumer;
import com.kaltura.media.server.events.IKalturaEventType;


public class KalturaEventsManager{

	protected static Logger logger = Logger.getLogger(KalturaEventsManager.class);
	static Map<IKalturaEventType, Set<IKalturaEventConsumer>> map = new HashMap<IKalturaEventType, Set<IKalturaEventConsumer>>();
	
	public static void registerEventConsumer(IKalturaEventConsumer eventConsumer, IKalturaEventType ... eventTypes){
		Set<IKalturaEventConsumer> consumersMap = null;
		
		for(IKalturaEventType eventType : eventTypes){
			logger.info("Attempting to register event consumer [" + eventConsumer.toString() + "] for event types: [" + eventType.toString() + "]");
			if(map.containsKey(eventType)){
				consumersMap = map.get(eventType);
			}
			else{
				consumersMap = new HashSet<IKalturaEventConsumer>();
				map.put(eventType, consumersMap);
			}
			consumersMap.add(eventConsumer);
		}
		
	}

	public static void raiseEvent(final IKalturaEvent event){
		logger.info("Raising event of type [" + event.getType() + "]");
		Set<IKalturaEventConsumer> consumersMap = map.get(event.getType());
		if (consumersMap == null)
			return;
			
		for(final IKalturaEventConsumer eventConsumer : consumersMap) {
			TimerTask timerTask = new TimerTask() {
				
				@Override
				public void run() {
					if(map.containsKey(event.getType())){
						eventConsumer.onEvent(event);
					}
				}
			};
			
			Timer timer = new Timer(true);
			timer.schedule(timerTask, 0);
		}
	}
}
