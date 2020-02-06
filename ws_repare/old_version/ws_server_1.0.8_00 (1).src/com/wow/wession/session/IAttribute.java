package com.wow.wession.session;

import java.io.Serializable;

public abstract interface IAttribute extends Serializable
{
  public abstract String getKey();

  public abstract Object getValue();
}

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.session.IAttribute
 * JD-Core Version:    0.5.4
 */