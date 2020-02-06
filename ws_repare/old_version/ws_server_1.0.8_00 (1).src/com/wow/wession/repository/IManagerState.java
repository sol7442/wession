package com.wow.wession.repository;

import com.wow.wession.data.IEntityState;

public abstract interface IManagerState extends IEntityState
{
  public static final int NODE_REGISTRY = 101;
  public static final int NODE_UNREGISTRY = 102;
  public static final int NODE_CHECK = 103;
  public static final int SET_PROPERTY = 104;

  public abstract void setProperty(String paramString1, String paramString2);
}

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.repository.IManagerState
 * JD-Core Version:    0.5.4
 */