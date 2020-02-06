package com.wow.wession.session;

public abstract class SessionFactory {
	
	private  String code;
	private  int type;
	
	public SessionFactory(){
	}
	
	//abstract public String getID();
	abstract public ISession create(Object obj);
	abstract public ISession create(String id, String user);
	
	public void setCode(String code){
		this.code = code;
	}
	public void setSessionType(int type){
		this.type = type;
	}
	
	public  String getAppCode() {
		return code;
	}
	
	public  int getSessionType(){
		return type;
	}
}
