package com.wow.wession.index;

import java.util.Iterator;
import java.util.List;

import com.wow.wession.server.WessionServerSession;
import com.wow.wession.session.ISession;

public class IndexRepository implements IIndexableRepository {

	public static final String IDX_USERID = "UserID";
	//public static final String IDX_APPSID = "AppSID";
	public static IndexRepository UserIDIndexRepository = new IndexRepository(IDX_USERID, new IRepositoryIndexHandler() {
		public String getIndexKey(ISession session) {
			if (session instanceof WessionServerSession) {
				WessionServerSession srv_session = (WessionServerSession) session;
				return srv_session.getUser();
			}else{
				return null;
			}
		}
	});
	private String name;
	private WessionMutilMap multimap = new WessionMutilMap();
	private IRepositoryIndexHandler handler;

	public IndexRepository(String name,IRepositoryIndexHandler handler){
		this.name    = name;
		this.handler = handler;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public IRepositoryIndexHandler getIndexHandler() {
		return this.handler;
	}
	
	public void clear() {
		multimap.clear();
	}

	public ISession[] getArray(int start, int end){
		return null;
	}
	
	public ISession[] getArray(){
		return multimap.getArray();
	}
	public Iterator<ISession> iterator(){
		return null;
	}
	

	public void create(ISession value) {
		String key = handler.getIndexKey(value);
		if(key != null){
			multimap.put(handler.getIndexKey(value), value.getID(), value);
		}
	}
	public List<ISession> getBy(String key) {
		return this.multimap.get(key);
	}

	public ISession expireBy(ISession value) {
		return multimap.remove(handler.getIndexKey(value),value.getID());
	}

	public void expire(String key) {}
	public void add(String parent, ISession session) {}
	public void remove(String parent, String key) {}
	public void set(String parent, String key, Object object) {}
	public void del(String parent, String key) {}
	public int getSize() {return 0;}
	public ISession get(String key) {return null;}

}
