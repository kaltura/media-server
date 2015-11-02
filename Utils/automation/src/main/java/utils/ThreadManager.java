package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import configurations.TestConfig;

public class ThreadManager {
	private static ExecutorService executor  = init();
	private static Map<String, List<FutureTask<String>>> pools = new HashMap<String, List<FutureTask<String>>>();
	private static List<Future<String>> workers = Collections.synchronizedList(new ArrayList<Future<String>>());
	private static long endTime; 
	
	private static ExecutorService init(){
		TestConfig config = TestConfig.get();

		long duration = config.getTestDuration() * 1000;
		endTime = System.currentTimeMillis() + duration;
		
		int numberOfThreads = 200;
		if(config.hasOtherProperty("number-of-threads")){
			numberOfThreads = (int) config.getOtherProperty("number-of-threads");
		}
		return Executors.newFixedThreadPool(numberOfThreads);
	}
	
	public static void start(String poolName, Runnable worker){
		FutureTask<String> task = new FutureTask<String>(worker, null);
		List<FutureTask<String>> pool;
		if(pools.containsKey(poolName)){
			pool = pools.get(poolName);
		}
		else{
			pool = Collections.synchronizedList(new ArrayList<FutureTask<String>>());
			pools.put(poolName, pool);
		}
			
		pool.add(task);
		workers.add(task);
		executor.execute(task);
	}
	
	@SuppressWarnings("unchecked")
	public static void start(Runnable worker){
		if(worker instanceof Future){
			workers.add((Future<String>) worker);
		}
		executor.execute(worker);
	}

	public static boolean shouldContinue() {
		return (System.currentTimeMillis() < endTime);
	}

	public static void stop(String poolName) {
		if(!pools.containsKey(poolName)){
			return;
		}

		List<FutureTask<String>> pool = pools.get(poolName);
		for(FutureTask<String> worker : pool){
			worker.cancel(true);
		}
	}

	public static void stop() throws InterruptedException {
		for(Future<String> worker : workers){
			worker.cancel(true);
		}
		executor.shutdown();
		executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
	}

	public static boolean isAlive(String poolName) {
		if(!pools.containsKey(poolName)){
			return false;
		}

		List<FutureTask<String>> pool = pools.get(poolName);
		for(FutureTask<String> worker : pool){
			if(!worker.isDone()){
				return true;
			}
		}
		
		return false;
	}
}
