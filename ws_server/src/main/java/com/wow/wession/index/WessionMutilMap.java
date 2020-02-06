package com.wow.wession.index;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.wow.wession.session.ISession;


public class WessionMutilMap {
	private final ConcurrentMap<String, HashMap<String,ISession>> cache;
	private final ConcurrentMap<String, Object> locks;

	public WessionMutilMap(){
		cache 		= new ConcurrentHashMap<String, HashMap<String, ISession>>();
		locks 		= new ConcurrentHashMap<String, Object>();
	}

	private Object getLock(final String key){
		final Object object=new Object();
	    Object lock=locks.putIfAbsent(key, object);
	    if(lock == null){
	      lock=object;
	    }
	    return lock;
	}

	public void put(final String index_key, final String value_key, final ISession value) {
		synchronized(getLock(index_key)){
			HashMap<String,ISession> map=cache.get(index_key);
			if(map == null){
				map = new HashMap<String,ISession>();
			}
			map.put(value_key, value);
			cache.put(index_key, map);
		}
	}

	public ISession remove(final String index_key, final String value_key){
		ISession session = null;
		synchronized(getLock(index_key)){			
			HashMap<String,ISession> map = cache.get(index_key);
			if(map !=null){
				session = map.remove(value_key);
				if(map.size() <= 0){
					locks.remove(index_key);
					cache.remove(index_key);
				}
			}
			return session;
		}
	}

	public int size() {
	    return cache.size();
	}
	public void clear(){
		this.locks.clear();
		this.cache.clear();
	}
	public List<ISession> get(final String index_key){
		List<ISession> item_list = new ArrayList<ISession>();
		synchronized(getLock(index_key)){
			HashMap<String,ISession> map = cache.get(index_key);
			if(map !=null){
				Iterator<String> iter_key = map.keySet().iterator();
				while(iter_key.hasNext()){
					item_list.add(map.get(iter_key.next()));
				}
			}
			return item_list;
		}
	}
	public ISession[] getArray(){
		return null;
	}
}
