package com.wow.wession.agent;

import com.wow.wession.session.ISessionMessage;

public class TransException extends Exception  {
	
	public static int ENABLE_FALSE 		= 1;
	public static int NOT_INITIALIZE 	= 2 ;
	public static int PARAMETER_MISS 	= 3;
	public static int PARSER_ERROR 	= 4;
	public static int NETWORK_ERROR 	= 5;
	
	private String url;
	private ISessionMessage msg;
	private int code = 0;
	
	
	public TransException(Exception e,int code) {
		super(e.getMessage());
		this.code = code;
	}
	public TransException(String msg,int code) {
		super(msg);
		this.code = code;
	}

	private static final long serialVersionUID = -3673395140733838489L;


	public void setUrl(String url, ISessionMessage message) {
		this.url = url;
		this.msg = message;
	}
	
	public String getUrl(){
		return this.url;
	}
	public int getCode(){
		return this.code;
	}
	public ISessionMessage getMsg(){
		return this.msg;
	}

}
