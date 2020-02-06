package com.wow.wession.repository;

import com.wow.wession.data.IEntityState;
import com.wow.wession.session.ISession;


public interface IRepository extends IEntityState{
	public void create(ISession session);
	public void expire(String key );
	public void add(String parent, ISession session);
	public void remove(String parent, String key);
	public void set(String parent,String key, Object object);
	public void delete(String parent, String key);
	public void clear();
}
