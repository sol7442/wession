package com.wow.server.protocol;

import java.io.Serializable;

public abstract interface IRequest extends Serializable
{
  public abstract String getSrc();

  public abstract void setSrc(String paramString);

  public abstract String getDest();

  public abstract void setDest(String paramString);

  public abstract int getServiceType();

  public abstract void setServiceType(int paramInt);
}

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.server.protocol.IRequest
 * JD-Core Version:    0.5.4
 */