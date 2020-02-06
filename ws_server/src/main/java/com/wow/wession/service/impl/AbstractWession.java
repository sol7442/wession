package com.wow.wession.service.impl;

import com.wow.server.protocol.IRequest;
import com.wow.server.protocol.IResponse;
import com.wow.wession.session.ISession;

public class AbstractWession implements IRequest {
	private static final long serialVersionUID = -7651569374699149987L;
	
	public static final int UNDEFINED = 0;

	public static final int INSERT 	= 1;
	public static final int REMOVE 	= 2;
	public static final int MODIFY 	= 3;
	public static final int GET 	= 4;
	public static final int GETBY 	= 5;

	public static final int CLUSTER_IN 	= 11;
	public static final int CLUSTER_OUT = 12;
	public static final int JOURNAL_IN 	= 13;
	public static final int JOURNAL_OUT = 14;

	public static final int CLUSTER = 1;
	public static final int JOURNAL = 2;
	public static final int SESSION = 3;
	public static final int MANAGER = 4;
	public static final int CONTROL = 5;


	private String src			="";
	private String dest			="";
	private int sericeType		= UNDEFINED;

	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getDest() {
		return dest;
	}
	public void setDest(String dest) {
		this.dest = dest;
	}
	public int getServiceType() {
		return this.sericeType;
	}
	public void setServiceType(int type) {
		this.sericeType = type;
	}
}
