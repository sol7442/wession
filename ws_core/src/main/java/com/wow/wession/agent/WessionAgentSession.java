package com.wow.wession.agent;

import com.wow.wession.session.ISessionState;
import com.wow.wession.session.WessionBaseSession;

public class WessionAgentSession extends WessionBaseSession {
	private static final long serialVersionUID = 2703693293244925688L;

	private String appCode;
	private int state  = ISessionState.CREATED;
	
	public WessionAgentSession(String id,String user, String app) {
		super(id,user);
		this.appCode = app;
	}

	public String getAppCode() {
		return appCode;
	}
	public int getState(){
		return this.state;
	}
	public void setState(int state){
		this.state = state;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(super.toString());
		buffer.append(",").append(this.appCode);
		return buffer.toString();
	}
}
