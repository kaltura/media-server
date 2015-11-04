package com.kaltura.media.quality.monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.kaltura.client.KalturaClient;
import com.kaltura.media.quality.actions.StartSession;
import com.kaltura.media.quality.configurations.TestConfig;

abstract public class Monitor {
	private static final Logger log = Logger.getLogger(Monitor.class);

	protected TestConfig config;
	protected KalturaClient client;
	protected StartSession session;
	protected int partnerId;
	protected static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	
	public Monitor(String[] args) throws Exception {
		config = TestConfig.init(args);
		partnerId = Integer.valueOf(config.getPartnerId());
		session = new StartSession(partnerId,
				config.getServiceUrl(), config.getAdminSecret());
		client = session.execute();
	}

	abstract protected void execute() throws Exception;

	protected String createDefaultDownloadDir(String entryId) {
		String reportDate = DATE_FORMAT.format(new Date());
		return TestConfig.get().getDestinationFolder() + "/" + entryId + "/" + reportDate;
	}

	protected void printAllThreads(){
		Thread[] threads = getAllThreads();
		for(Thread thread : threads){
			log.warn("Thread " + thread.getName() + " is still alive: \n" + getStackTraceMessage(thread));
		}
	}

	protected String getStackTraceMessage(Thread thread) {
		String message = "";
		
        StackTraceElement[] trace = thread.getStackTrace();
        for (StackTraceElement traceElement : trace)
        	message += "\tat " + traceElement + "\n";
        
        return message;
    }
    
	protected ThreadGroup getRootThreadGroup( ) {
	    ThreadGroup tg = Thread.currentThread( ).getThreadGroup( );
	    ThreadGroup ptg;
	    while ( (ptg = tg.getParent( )) != null )
	        tg = ptg;
	    return tg;
	}
	
	protected Thread[] getAllThreads( ) {
	    final ThreadGroup root = getRootThreadGroup( );
	    final ThreadMXBean thbean = ManagementFactory.getThreadMXBean( );
	    int nAlloc = thbean.getThreadCount( );
	    int n = 0;
	    Thread[] threads;
	    do {
	        nAlloc *= 2;
	        threads = new Thread[ nAlloc ];
	        n = root.enumerate( threads, true );
	    } while ( n == nAlloc );
	    return java.util.Arrays.copyOf( threads, n );
	}
}
