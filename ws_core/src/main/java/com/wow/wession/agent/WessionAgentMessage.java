package com.wow.wession.agent;

import com.wow.wession.session.ISession;
import com.wow.wession.session.ISessionMessage;

public class WessionAgentMessage implements ISessionMessage {
	private static final long serialVersionUID = 3503769383943015397L;

	private int eventType;
	private int messageType;
	private ISession session;
	
	
	
	public WessionAgentMessage(int eventType,ISession session){
		this.messageType = MSG_SESSION_AUTHEN;
		this.eventType   = eventType;
		this.session     = session;
	}
	
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
