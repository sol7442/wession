package com.wow.wession.session;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class WessionBaseSession implements ISession{
	private static final long serialVersionUID = -3524542241621912886L;

	private String id;
	private String user;

	private ConcurrentMap<String, Object> attributes = new ConcurrentHashMap<String, Object>();

	private long createTime;
	private long lastAccessTime;

	public WessionBaseSession(){
		this.createTime = System.currentTimeMillis();
	}
	public WessionBaseSession(String id,String user) {
		this.id    = id;
		this.user  = user;
		this.createTime = System.currentTimeMillis();
	}
	
	public String getUser(){
		return this.user;
	}


	public String getID() {
		return this.id;
	}

	public Object getAttribute(final String key){
		return attributes.get(key);
	}
	public Set<String> getAttributeKeySet(){
		return attributes.keySet();
	}
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}
	public Object removeAttribute(final String key){
		return attributes.remove(key);
	}
	
	public long getCreateTime() {
		return this.createTime;
	}
	public void setCreateTime(long time){
		this.createTime = time;
	}
	public long getLastAccessTime() {
		return this.lastAccessTime;
	}

	public String toString(){
		StringBuffer buffer = new StringBuffer();
		
		Date d = new Date(this.createTime);
		buffer.append(d.toString()).append(" ");
		buffer.append(this.id).append(",").append(this.user);
		synchronized (attributes) {
			Iterator<String> keys =  attributes.keySet().iterator();
			while(keys.hasNext()){
				String key    = keys.next();
				Object object = attributes.get(key);
				buffer.append("(").append(key).append(":").append(object.toString()).append(")");
			}
		}
		return buffer.toString();
	}

	public int compareTo(ISession session) {
		if(session == null) {return -1;}
		
		if(session.getID().equals(this.id)){
			return 0;
		}else{
			return -1;
		}
	}
}
