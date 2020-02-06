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

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.server.IAcceptor
 * JD-Core Version:    0.5.4
 */