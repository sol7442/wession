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

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.session.ISessionState
 * JD-Core Version:    0.5.4
 */