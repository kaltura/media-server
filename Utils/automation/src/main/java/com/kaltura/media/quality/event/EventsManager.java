package com.kaltura.media.quality.event;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.log4j.Logger;

import com.kaltura.media.quality.configurations.TestConfig;
import com.kaltura.media.quality.event.listener.IListener;
import com.kaltura.media.quality.event.listener.Listener;

public class EventsManager {
	private static final Logger log = Logger.getLogger(EventsManager.class);
	private static TestConfig config = null;
	private static EventsManager instance = new EventsManager();
	private static boolean isConsumingDefferedEvents = false;

	private Map<Class<? extends IListener>, Set<? extends IListener>> listeners = new ConcurrentHashMap<Class<? extends IListener>, Set<? extends IListener>>();

	protected EventsManager() {
		Runtime.getRuntime().addShutdownHook(new RegisterConsumers());
	}

	public static EventsManager get() {
		return instance;
	}
	
	class RegisterConsumers extends Thread{
		@Override
		public void run(){
			Set<IListener> deffered = new TreeSet<IListener>();
			for(Class<? extends IListener> clazz : listeners.keySet()){
				Set<? extends IListener> set = listeners.get(clazz);
				for(IListener listener : set){
					if(!isConsumingDefferedEvents && listener.isDeffered() && listener instanceof Listener && !deffered.contains(listener)){
						deffered.add(listener);
						((Listener) listener).save();
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IListener> void addListener(Class<T> clazz, T listener) {
		Set<T> set;
		if (!listeners.containsKey(clazz)) {
			set = new ConcurrentSkipListSet<T>();
			listeners.put(clazz, set);
		} else {
			set = (Set<T>) listeners.get(clazz);
		}
		set.add(listener);
	}

	public <T extends IListener> void removeListener(Class<T> clazz, IListener listener) {
		if (listeners.containsKey(clazz)) {
			@SuppressWarnings("unchecked")
			Set<T> set = (Set<T>) listeners.get(clazz);
			if (set.contains(listener)) {
				set.remove(listener);
			}
		}
	}

	public <T extends IListener> void raiseEvent(Event<T> event) {
		boolean deffer = false;
		for (T listener : getListeners(event.getConsumerType())) {
			log.info("Consuming event [" + listener.getClass().getSimpleName() + "]: " + listener.getTitle());
			deffer = deffer || event.consume(listener);
		}
		
		if(deffer){
			event.save();
		}
	}

	@SuppressWarnings("unchecked")
	protected <T extends IListener> Set<T> getListeners(Class<T> clazz) {
		if (listeners.containsKey(clazz)) {
			return (Set<T>) listeners.get(clazz);
		}

		return new ConcurrentSkipListSet<T>();
	}

	public static String getDefferedEventsPath() {
		if(config == null){
			config = TestConfig.get();
		}
		
		String path = config.getDestinationFolder() + "/events";
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir.getAbsolutePath();
	}

	public static String getDefferedConsumersPath() {
		if(config == null){
			config = TestConfig.get();
		}
		
		String path = config.getDestinationFolder() + "/consumers";
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir.getAbsolutePath();
	}

	public static <T extends Persistable> T unserialize(Class<T> clazz, File file) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
		@SuppressWarnings("unchecked")
		T persistable  = (T) objectInputStream.readObject();
		objectInputStream.close();
		
		return persistable;
	}

	public static void registerDefferedConsumers() {
		File dir = new File(getDefferedConsumersPath());
		if (!dir.exists() || dir.listFiles() == null) {
			log.info("No deffered consumers found");
			return;
		}

		File[] children = dir.listFiles();
		for (File file : children) {
			try {
				Listener defferedConsumer = unserialize(Listener.class, file);
				defferedConsumer.register();
				file.delete();
			} catch (IOException | ClassNotFoundException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	public static void consumeDefferedEvents() {
		isConsumingDefferedEvents = true;
		registerDefferedConsumers();
		
		File dir = new File(getDefferedEventsPath());
		if (!dir.exists() || dir.listFiles() == null) {
			log.info("No deffered events found");
			return;
		}

		File[] children = dir.listFiles();
		for (File file : children) {
			try {
				Event<?> event = unserialize(Event.class, file);
				log.info("Raising event [" + event.getClass().getSimpleName() + "]: " + event.getTitle());
				instance.raiseEvent(event);
				file.delete();
			} catch (IOException | ClassNotFoundException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	public static boolean isConsumingDefferedEvents() {
		return isConsumingDefferedEvents ;
	}
}
