package com.wow.wession.session;

import java.io.Serializable;


public interface ISessionMessage extends Serializable
{
	  public static final int EVT_SESSION_CREATED 	= 1;
	  public static final int EVT_SESSION_EXPIRED 	= 2;
	  public static final int EVT_SESSION_ACCESSED 	= 3;
	  public static final int EVT_GET_ALL_SESSIONS 	= 4;
	  public static final int EVT_SESSION_DELTA 	= 13;
	  public static final int EVT_ALL_SESSION_DATA 	= 12;
	  public static final int EVT_ALL_SESSION_TRANSFERCOMPLETE = 14;
	  public static final int EVT_CHANGE_SESSION_ID = 15;
	  public static final int EVT_ALL_SESSION_NOCONTEXTMANAGER = 16;

	  public static final int MSG_SESSION_CLUSTER   = 1;
	  public static final int MSG_SESSION_JOURNAL   = 2;
	  public static final int MSG_SESSION_AUTHEN    = 3;
	  
	  public abstract String getEventTypeString();
	  public abstract int getMessageType();
	  public abstract int getEventType();
	  public abstract ISession getSession();
	  public abstract void setSession(ISession session);
	  public abstract String getSessionID();
	  
	  //public abstract ISessionMessage readObject(ObjectInput objInput)throws IOException,ClassNotFoundException;
	  //public abstract void writeObject(ObjectOutput objOutput)throws IOException;
}
