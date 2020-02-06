package com.wow.wession.session;

import java.io.Serializable;

public abstract interface ISession extends Serializable, Comparable<ISession>
{
  public abstract String getID();

  public abstract Object getAttribute(String paramString);

  public abstract void setAttribute(String paramString, Object paramObject);

  public abstract Object removeAttribute(String paramString);

  public abstract long getCreateTime();

  public abstract long getLastAccessTime();
}

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.session.ISession
 * JD-Core Version:    0.5.4
 */