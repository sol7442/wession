package com.wow.wession.util;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("rawtypes")
public class ConcurrentMultiMap<K extends Comparable, V extends Comparable> {
	private final ConcurrentMap<K, Set<V>> cache;
	private final ConcurrentMap<K, Object> locks;

	public ConcurrentMultiMap(){
		cache = new ConcurrentHashMap<K, Set<V>>();
		locks = new ConcurrentHashMap<K, Object>();
	}

	private Object getLock(final K key){
		final Object object=new Object();
	    Object lock=locks.putIfAbsent(key, object);
	    if(lock == null){
	      lock=object;
	    }
	    return lock;
	}

	public void put(final K key, final V value) {
		synchronized(getLock(key)){
			Set<V> set=cache.get(key);
			if(set == null){
				set = new HashSet<V>();
				cache.put(key, set);
			}
			set.add(value);
		}
	}

	public Set<V> remove(final K key){
		synchronized(getLock(key)){
			locks.remove(key);
			return cache.remove(key);
		}
	}

	public int size() {
	    return cache.size();
	}

	public Set<V> get(final K key){
		synchronized(getLock(key)){
			return cache.get(key);
		}
	}
}
