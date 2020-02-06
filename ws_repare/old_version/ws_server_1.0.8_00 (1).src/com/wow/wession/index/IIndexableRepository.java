package com.wow.wession.index;

import com.wow.wession.session.ISession;
import java.util.List;

public abstract interface IIndexableRepository
{
  public abstract IRepositoryIndexHandler getIndexHandler();

  public abstract List<ISession> getBy(String paramString);

  public abstract ISession expireBy(ISession paramISession);
}

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.index.IIndexableRepository
 * JD-Core Version:    0.5.4
 */