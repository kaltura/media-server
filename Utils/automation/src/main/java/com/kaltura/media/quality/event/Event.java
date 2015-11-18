package com.kaltura.media.quality.event;

import com.kaltura.media.quality.event.listener.IListener;

public abstract class Event<T extends IListener> extends Persistable {
	private static final long serialVersionUID = 7801683306534178171L;
	private final Class<T> consumerType;
	private long time;

    public Event(Class<T> consumerType) {
        this.time = System.nanoTime();
        this.consumerType = consumerType;
    }

    public Class<T> getConsumerType() {
        return consumerType;
    }

    public boolean consume(T listener){
    	if(!EventsManager.isConsumingDefferedEvents() && listener.isDeffered()){
    		return true;
    	}
    	
    	callListener(listener);
    	return false;
    }
    
    public void consumeDeffered(T listener) {
    	callListener(listener);
	}
    
	protected abstract void callListener(T listener);

	public long getTime() {
		return time;
	}

	@Override
	protected String getPath() {
		return EventsManager.getDefferedEventsPath();
	}

	@Override
	protected String getExtension() {
		return "event";
	}
    
}
