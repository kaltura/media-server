package com.kaltura.infra;

import java.awt.List;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.Map.Entry;

public class ConcurrentCacheMap<K,V> implements ConcurrentMap<K, V>{
	
	private class MappedValue<V>{
		public long usedTime;
		public V value;
		
		MappedValue (V v) {
			value = v;
			updateUseTime ();
		}
		
		public void updateUseTime () {
			usedTime = (new Date()).getTime();
		}
	}
	
	private class ConcurrentCacheMapEntry<K,V> implements Entry<K, V>
	{
		protected K key;
		protected V value;
		
		ConcurrentCacheMapEntry (K k, V v) {
		      key = k;
		      value = v;
		 }
		
		@Override
		public K getKey() {
			return key;
		}
		@Override
		public V getValue() {
			return value;
		}
		@Override
		public V setValue(V arg0) {
			V ret = value;
			value = arg0;
			return ret;
		}
	}
	
	private Timer clearTimer = new Timer();
	private boolean timerStarted = false;
	
	protected int timerInterval;
	protected int timerDelay;
	
	public ConcurrentHashMap<K, MappedValue<V>> map;

	ConcurrentCacheMap (int interval, int delay) {
		timerDelay = delay;
		timerInterval = interval;
	}
	
	ConcurrentCacheMap () {
		timerDelay = 60000;
		timerInterval = 4000;
	}
	
	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public boolean containsKey(Object arg0) {
		return map.containsKey(arg0);
	}

	@Override
	public boolean containsValue(Object arg0) {
		return map.containsValue(arg0);
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return null;
	}

	@Override
	public V get(Object arg0) {
		synchronized (map) {
			MappedValue<V> val = map.get(arg0);
			val.updateUseTime();
		
			map.put((K) arg0, val);
			return val.value;
		}
		
		
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		return map.keySet();
	}

	@Override
	public V put(K arg0, V arg1) {
		MappedValue<V> v = new MappedValue<V>(arg1);
		synchronized (map) {
			map.put(arg0, v);
		}
		this.initTimer ();
		
		return v.value;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> arg0) {
	
		// TODO Auto-generated method stub
		
	}

	@Override
	public V remove(Object arg0) {
		return map.remove(arg0).value;
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Collection<V> values() {
		ArrayList<V> list = new ArrayList<V>();
		for (K key : map.keySet()) {
			MappedValue<V> v = map.get(key);
			list.add(v.value);
		}
		
		return list;
	}

	@Override
	public V putIfAbsent(K arg0, V arg1) {
		synchronized (map) {
			MappedValue<V> v = new MappedValue<V>(arg1);
			return map.putIfAbsent(arg0, v).value;
		}
	}

	@Override
	public boolean remove(Object arg0, Object arg1) {
		synchronized (map) {
			if (map.containsKey(arg0) && map.get(arg0).value.equals(arg1)) {
				map.remove(arg0);
				return true;
			}
		}
		
		return false;
	}

	@Override
	public V replace(K arg0, V arg1) {
		synchronized (map) {
			MappedValue<V> v = map.get(arg0);
			v.value = arg1;
			v.updateUseTime();
			return map.replace(arg0, v).value;
		}
	}

	@Override
	public boolean replace(K arg0, V arg1, V arg2) {
		synchronized (map) {
			if (map.containsKey(arg0) && map.get(arg0).value.equals(arg1)) {
				MappedValue<V> val = map.get(arg0);
				val.value = arg2;
				val.updateUseTime();
				map.put(arg0, val);
				
				return true;
			}
		}
		
		return false;
	} 
	
	private void initTimer () {
		TimerTask clearUnused = new TimerTask() {
			
			@Override
			public void run() {
				synchronized (map) {
					for (K key : map.keySet()) {
						long lastUsed = map.get(key).usedTime;
						if (lastUsed + timerInterval > (new Date()).getTime()) {
							map.remove(key);
						}
					}
				}
				
			}
		};
		
		if (!timerStarted) {
			clearTimer.schedule(clearUnused, timerDelay, timerInterval);
			timerStarted = true;
		}
		
	}
}
