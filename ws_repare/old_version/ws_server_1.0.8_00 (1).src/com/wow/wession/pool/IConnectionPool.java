package com.wow.wession.pool;

import com.wow.client.IConnection;
import java.io.IOException;

public abstract interface IConnectionPool
{
  public abstract IConnection get()
    throws IOException;

  public abstract void release(IConnection paramIConnection);
}

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.pool.IConnectionPool
 * JD-Core Version:    0.5.4
 */