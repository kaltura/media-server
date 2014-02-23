package com.kaltura.media.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.kaltura.media.server.events.IKalturaEvent;
import com.kaltura.media.server.events.IKalturaEventConsumer;
import com.kaltura.media.server.events.IKalturaEventType;


public class KalturaEventsManager{

	static Map<IKalturaEventType, Set<IKalturaEventConsumer>> map = new HashMap<IKalturaEventType, Set<IKalturaEventConsumer>>();
	
	public static void registerEventConsumer(IKalturaEventConsumer eventConsumer, IKalturaEventType ... eventTypes){
		Set<IKalturaEventConsumer> consumersMap = null;
		
		for(IKalturaEventType eventType : eventTypes){
			if(map.containsKey(eventType)){
				consumersMap = map.get(eventType);
			}
			else{
				consumersMap = new HashSet<IKalturaEventConsumer>();
				map.put(eventType, consumersMap);
			}
		}
		
		consumersMap.add(eventConsumer);
	}

	public static void raiseEvent(IKalturaEvent event){
		
		if(map.containsKey(event.getClass())){
			Set<IKalturaEventConsumer> consumersMap = map.get(event.getClass());
			for(IKalturaEventConsumer eventConsumer : consumersMap){
				eventConsumer.onEvent(event);
			}
		}
	}
}
