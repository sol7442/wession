package com.wow.wession.session;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

public class AbstractWessionMessage implements ISessionMessage, Serializable{
	private static final long serialVersionUID = -3473793763523463511L;

	private int eventType;
	private int messageType;
	private ISession session;
	
	public String getEventTypeString() {
		return null;
	}

	public int getMessageType() {
		return this.messageType;
	}

	public int getEventType() {
		return this.eventType;
	}

	public ISession getSession() {
		return this.session;
	}

	public void setSession(ISession session) {
		this.session = session;
	}

	public String getSessionID() {
		return this.session.getID();
	}

}
