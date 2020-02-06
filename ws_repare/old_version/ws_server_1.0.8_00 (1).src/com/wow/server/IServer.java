package com.wow.server;

import java.io.Closeable;

public abstract interface IServer extends Runnable, Closeable
{
  public abstract boolean initialize(IServiceHandler paramIServiceHandler);

  public abstract String getName();

  public abstract String getAddress();

  public abstract int getPort();
}

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.server.IServer
 * JD-Core Version:    0.5.4
 */