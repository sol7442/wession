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

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.wession.service.impl.IWessionResult
 * JD-Core Version:    0.5.4
 */