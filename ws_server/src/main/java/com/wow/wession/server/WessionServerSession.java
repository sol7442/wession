package com.wow.wession.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.wow.wession.session.ISession;
import com.wow.wession.session.WessionBaseSession;
import com.wow.wession.session.WessionSessionManager;


public class WessionServerSession extends WessionBaseSession {
	private static final long serialVersionUID = -4279165365119671062L;
	private ConcurrentMap<String, ISession> agents = new ConcurrentHashMap<String, ISession>();

	public WessionServerSession(String id,String user){
		super(id,user);
	}

	public void setAttribute(String key, Object value) {
		setAttribute(key, value,true);
	}

	public void setAttribute(String key, Object value, boolean cluster) {
		super.setAttribute(key, value);
		if(WessionSessionManager.getInstance().get(getID()) != null){
			WessionSessionManager.getInstance().set(getID(),key,value,cluster);
		}
	}
	public Object removeAttribute(String key){
		return removeAttribute(key,true);
	}
	public Object removeAttribute(String key,boolean cluster){
		Object object =  super.removeAttribute(key);
		if(WessionSessionManager.getInstance().get(getID()) != null){
			WessionSessionManager.getInstance().delete(getID(),key,cluster);
		}
		return object;
	}

	public void addAgentSession(ISession session){
		addAgentSession(session,true);
	}
	public void removeAgentSession(String key){
		removeAgentSession(key,true);
	}

	public void addAgentSession(ISession session,boolean cluster){
		agents.put(session.getID(), session);
		if(WessionSessionManager.getInstance().get(getID()) != null){
			WessionSessionManager.getInstance().add(getID(), session,cluster);
		}

	}
	public void removeAgentSession(String key, boolean cluster){
		agents.remove(key);
		if(WessionSessionManager.getInstance().get(getID()) != null){
			WessionSessionManager.getInstance().remove(getID(), key,cluster);
		}
	}

	public Set<String> getAgentSessionKeySet(){
		return agents.keySet();
	}
	public List<ISession> getAgentSessionList(){
		List<ISession> item_list = new ArrayList<ISession>();
		item_list.addAll(agents.values());
		return item_list;
	}

	public ISession getAgentSession(String key){
		return agents.get(key);
	}
	public int getAgentSize(){
		return agents.size();
	}

	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(super.toString());
		synchronized (agents) {
			Iterator<String> keys =  agents.keySet().iterator();
			while(keys.hasNext()){
				buffer.append("[").append(agents.get(keys.next()).toString()).append("]");
			}
		}
		return buffer.toString();
	}
}
