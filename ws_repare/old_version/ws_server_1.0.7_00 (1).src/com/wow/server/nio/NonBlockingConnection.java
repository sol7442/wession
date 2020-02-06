package com.wow.server.nio;

import com.wow.server.AbstractConnection;
import java.io.IOException;
import java.nio.channels.SocketChannel;

public class NonBlockingConnection extends AbstractConnection
{
  public void initialize(SocketChannel channel)
  {
  }

  public void close()
    throws IOException
  {
  }
}

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.server.nio.NonBlockingConnection
 * JD-Core Version:    0.5.4
 */