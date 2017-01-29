package com.kaltura.media_server.services;

/**
 * Created by lilach.maliniak on 29/01/2017.
 */
import org.apache.log4j.Logger;

import java.util.WeakHashMap;

public class LiveWeakHashMap<K, V> extends WeakHashMap<K,V> {

	private static final Logger logger = Logger.getLogger(LiveWeakHashMap.class);

	protected void finalize() {
		logger.debug("finalize called");
	}
}
