package com.wow.server;

import java.io.IOException;

public abstract interface IAcceptor
{
  public abstract void listen()
    throws IOException;

  public abstract void close()
    throws IOException;

  public abstract String getAddress();

  public abstract int getPort();
}

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.server.IAcceptor
 * JD-Core Version:    0.5.4
 */