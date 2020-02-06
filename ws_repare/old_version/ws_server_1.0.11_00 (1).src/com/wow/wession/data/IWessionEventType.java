package com.wow.wession.data;

public abstract interface IWessionEventType
{
  public static final int EVT_SESSION_CREATE = 0;
  public static final int EVT_SESSION_EXPIRE = 1;
  public static final int EVT_SESSION_ACCESS = 2;
  public static final int EVT_LINK_APPEND = 10;
  public static final int EVT_LINK_REMOVE = 11;
  public static final int EVT_ATTRIBUTE_SET = 20;
  public static final int EVT_ATTRIBUTE_DEL = 21;
  public static final int EVT_ATTRIBUTE_GET = 22;
}

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.wession.data.IWessionEventType
 * JD-Core Version:    0.5.4
 */