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

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.wession.repository.IManagerState
 * JD-Core Version:    0.5.4
 */