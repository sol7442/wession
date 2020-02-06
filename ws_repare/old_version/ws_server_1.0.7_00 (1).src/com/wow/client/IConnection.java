package com.wow.client;

import com.wow.server.protocol.IRequest;
import com.wow.server.protocol.IResponse;
import java.io.Closeable;
import java.io.IOException;
import java.net.UnknownHostException;

public abstract interface IConnection extends Closeable
{
  public abstract void connect(String paramString, int paramInt)
    throws UnknownHostException, IOException;

  public abstract IRequest readRequest()
    throws ClassNotFoundException, IOException;

  public abstract void writeRequest(IRequest paramIRequest)
    throws IOException;

  public abstract IResponse readResponse()
    throws ClassNotFoundException, IOException;

  public abstract void writeResponse(IResponse paramIResponse)
    throws IOException;

  public abstract boolean isConnected();
}

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.client.IConnection
 * JD-Core Version:    0.5.4
 */