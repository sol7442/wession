package com.wow.wession.session;

import java.io.Serializable;

public abstract interface IAttribute extends Serializable
{
  public abstract String getKey();

  public abstract Object getValue();
}

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.session.IAttribute
 * JD-Core Version:    0.5.4
 */