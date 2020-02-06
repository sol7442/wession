package com.wow.wession.session;

import java.io.Serializable;

public abstract interface ISessionMessage extends Serializable
{
  public static final int EVT_SESSION_CREATED = 1;
  public static final int EVT_SESSION_EXPIRED = 2;
  public static final int EVT_SESSION_ACCESSED = 3;
  public static final int EVT_GET_ALL_SESSIONS = 4;
  public static final int EVT_SESSION_DELTA = 13;
  public static final int EVT_ALL_SESSION_DATA = 12;
  public static final int EVT_ALL_SESSION_TRANSFERCOMPLETE = 14;
  public static final int EVT_CHANGE_SESSION_ID = 15;
  public static final int EVT_ALL_SESSION_NOCONTEXTMANAGER = 16;
  public static final int MSG_SESSION_CLUSTER = 1;
  public static final int MSG_SESSION_JOURNAL = 2;
  public static final int MSG_SESSION_AUTHEN = 3;

  public abstract String getEventTypeString();

  public abstract int getMessageType();

  public abstract int getEventType();

  public abstract ISession getSession();

  public abstract void setSession(ISession paramISession);

  public abstract String getSessionID();
}

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.session.ISessionMessage
 * JD-Core Version:    0.5.4
 */