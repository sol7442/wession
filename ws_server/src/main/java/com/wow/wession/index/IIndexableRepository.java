package com.wow.wession.index;


import java.util.List;
import com.wow.wession.session.ISession;

public interface IIndexableRepository{
	public IRepositoryIndexHandler getIndexHandler();
	public List<ISession> getBy(String key);
	public ISession expireBy(ISession value);
}
