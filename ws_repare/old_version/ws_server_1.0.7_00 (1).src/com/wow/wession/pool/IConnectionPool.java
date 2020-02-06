package com.wow.wession.pool;

import com.wow.client.IConnection;
import java.io.IOException;

public abstract interface IConnectionPool
{
  public abstract IConnection get()
    throws IOException;

  public abstract void release(IConnection paramIConnection);
}

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.pool.IConnectionPool
 * JD-Core Version:    0.5.4
 */