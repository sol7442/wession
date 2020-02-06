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

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.data.IEntityState
 * JD-Core Version:    0.5.4
 */