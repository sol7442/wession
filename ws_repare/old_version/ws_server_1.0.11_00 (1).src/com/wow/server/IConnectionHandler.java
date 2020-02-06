package com.wow.server;

import java.io.IOException;

public abstract interface IConnectionHandler
{
  public abstract Object readObject()
    throws ClassNotFoundException, IOException;

  public abstract void writeObject(Object paramObject)
    throws IOException;
}

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.server.IConnectionHandler
 * JD-Core Version:    0.5.4
 */