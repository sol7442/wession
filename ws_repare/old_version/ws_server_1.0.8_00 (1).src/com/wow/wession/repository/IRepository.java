package com.wow.wession.repository;

import com.wow.wession.data.IEntityState;
import com.wow.wession.session.ISession;

public abstract interface IRepository extends IEntityState
{
  public abstract void create(ISession paramISession);

  public abstract void expire(String paramString);

  public abstract void add(String paramString, ISession paramISession);

  public abstract void remove(String paramString1, String paramString2);

  public abstract void set(String paramString1, String paramString2, Object paramObject);

  public abstract void delete(String paramString1, String paramString2);

  public abstract void clear();
}

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.repository.IRepository
 * JD-Core Version:    0.5.4
 */