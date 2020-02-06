package com.wow.server;

import java.io.Closeable;

public abstract interface IServer extends Runnable, Closeable
{
  public abstract boolean initialize(IServiceHandler paramIServiceHandler);

  public abstract String getName();

  public abstract String getAddress();

  public abstract int getPort();
}

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.server.IServer
 * JD-Core Version:    0.5.4
 */