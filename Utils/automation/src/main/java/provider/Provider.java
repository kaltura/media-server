package provider;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;





import utils.ThreadManager;
import configurations.TestConfig;
import event.IListener;

abstract public class Provider implements Runnable, Comparable<Provider> {
	private static Map<Class<? extends IListener>, Set<? extends IListener>> listeners = new ConcurrentHashMap<Class<? extends IListener>, Set<? extends IListener>>();

	protected static TestConfig config;

	public Provider() {
		config = TestConfig.get();
	}
	
	@Override
	public int compareTo(Provider provider){
		if(provider == this){
			return 0;
		}
			
		return 1;
	}
	
	public void start() {
		ThreadManager.start(this);
	}

	@SuppressWarnings("unchecked")
	public <T extends IListener> void addListener(Class<T> clazz, T listener) {
		Set<T> set;
		if(!listeners.containsKey(clazz)){
			set = new ConcurrentSkipListSet<T>();
			listeners.put(clazz, set);
		}
		else{
			set = (Set<T>) listeners.get(clazz);
		}
		set.add(listener);
	}

	public <T extends IListener> void removeListener(Class<T> clazz, IListener listener) {
		if(listeners.containsKey(clazz)){
			@SuppressWarnings("unchecked")
			Set<T> set = (Set<T>) listeners.get(clazz);
			if(set.contains(listener)){
				set.remove(listener);
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected <T extends IListener> Set<T> getListeners(Class<T> clazz) {
		if(listeners.containsKey(clazz)){
			return (Set<T>) listeners.get(clazz);
		}
		
		return new ConcurrentSkipListSet<T>();
	}
}
