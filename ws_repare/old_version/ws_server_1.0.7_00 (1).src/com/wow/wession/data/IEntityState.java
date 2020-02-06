package com.wow.wession.data;

public abstract interface IEntityState
{
  public static final int CREATE = 1;
  public static final int EXPIRE = 2;
  public static final int ADD = 10;
  public static final int REMOVE = 11;
  public static final int SET = 21;
  public static final int DEL = 22;
  public static final int ALL = 30;
  public static final int ACCESS = 0;
  public static final int CLEAR = 99;
}

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.data.IEntityState
 * JD-Core Version:    0.5.4
 */