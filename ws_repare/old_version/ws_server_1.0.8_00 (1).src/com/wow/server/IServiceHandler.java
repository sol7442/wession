package com.wow.server;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public abstract interface IServiceHandler
{
  public abstract BlockingQueue<Socket> getSocketQueue();

  public abstract IWorker getNewWorker();

  public abstract boolean initialize();

  public abstract void close();

  public abstract IAcceptor getAcceptor();

  public abstract String getName();
}

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.server.IServiceHandler
 * JD-Core Version:    0.5.4
 */