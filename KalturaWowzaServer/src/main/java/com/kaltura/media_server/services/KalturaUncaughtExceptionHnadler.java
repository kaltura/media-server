package com.kaltura.media_server.services;

import org.apache.log4j.Logger;

/**
 * Created by asher.saban on 4/14/2015.
 */
public class KalturaUncaughtExceptionHnadler implements Thread.UncaughtExceptionHandler{
		private static Logger log = Logger.getLogger(KalturaUncaughtExceptionHnadler.class);

		public void uncaughtException(Thread t, Throwable ex) {
			log.error("Uncaught exception in thread: " + t.getName(), ex);
		}
}
