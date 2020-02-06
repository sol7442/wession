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

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.server.IServiceHandler
 * JD-Core Version:    0.5.4
 */