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

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.data.IWessionEventType
 * JD-Core Version:    0.5.4
 */