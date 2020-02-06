package com.wow.wession.session;

public abstract interface ISessionState
{
  public static final int CREATED = 0;
  public static final int VALID_SUCEESS = 10;
  public static final int VALID_FAIL = 11;
  public static final int DUMMY_DUP = 12;
  public static final int DUMMY_FAIL = 13;
  public static final int EXPIRED = 99;
}

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.session.ISessionState
 * JD-Core Version:    0.5.4
 */