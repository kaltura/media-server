package com.kaltura.media.quality.event;

import com.kaltura.media.quality.event.listener.IListener;

public abstract class Event<T extends IListener> {

    private final Class<T> consumerType;

    public Event(Class<T> consumerType) {
         this.consumerType = consumerType;
    }

    public Class<T> getConsumerType() {
        return consumerType;
    }
    
    public abstract void callListener(T listener);
}
