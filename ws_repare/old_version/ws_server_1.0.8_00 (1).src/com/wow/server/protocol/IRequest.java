package com.wow.server.protocol;

import java.io.Serializable;

public abstract interface IRequest extends Serializable
{
  public abstract String getSrc();

  public abstract void setSrc(String paramString);

  public abstract String getDest();

  public abstract void setDest(String paramString);

  public abstract int getServiceType();

  public abstract void setServiceType(int paramInt);
}

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.server.protocol.IRequest
 * JD-Core Version:    0.5.4
 */