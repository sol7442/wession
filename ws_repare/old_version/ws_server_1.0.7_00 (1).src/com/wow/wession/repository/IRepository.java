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

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.repository.IRepository
 * JD-Core Version:    0.5.4
 */