package com.wow.server.protocol;

import java.io.Serializable;

public abstract interface IResponse extends Serializable
{
  public abstract String getSrc();

  public abstract void setSrc(String paramString);

  public abstract String getDest();

  public abstract void setDest(String paramString);

  public abstract void setResultCode(int paramInt);

  public abstract int getResultCode();
}

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.server.protocol.IResponse
 * JD-Core Version:    0.5.4
 */