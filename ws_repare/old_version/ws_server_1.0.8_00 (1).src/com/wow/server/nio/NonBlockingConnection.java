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

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.server.nio.NonBlockingConnection
 * JD-Core Version:    0.5.4
 */