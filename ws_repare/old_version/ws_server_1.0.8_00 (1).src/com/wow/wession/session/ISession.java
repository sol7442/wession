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

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.session.ISession
 * JD-Core Version:    0.5.4
 */