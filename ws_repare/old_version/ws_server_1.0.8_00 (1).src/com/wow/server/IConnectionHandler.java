package com.wow.server;

import java.io.IOException;

public abstract interface IConnectionHandler
{
  public abstract Object readObject()
    throws ClassNotFoundException, IOException;

  public abstract void writeObject(Object paramObject)
    throws IOException;
}

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.server.IConnectionHandler
 * JD-Core Version:    0.5.4
 */