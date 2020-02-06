package com.wow.wession.service.impl;

public abstract interface IWessionResult
{
  public static final int FAIL = 0;
  public static final int SUCCESS = 1;
  public static final int CLIENT_ERROR = 300;
  public static final int PARAM_ERROR = 301;
  public static final int TRANCE_ERROR = 400;
  public static final int UNDEFINED_TRANCE_OBJECT = 401;
  public static final int SERVER_ERROR = 500;
}

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.service.impl.IWessionResult
 * JD-Core Version:    0.5.4
 */