package com.wow.wession.service.impl;

import com.wow.server.protocol.IRequest;


public class WessionRequest implements IRequest,IWessionServiceType{
	/**
	 *
	 */
	private static final long serialVersionUID = 1550075010031569565L;

	transient private long startTime = 0;
	private String src = null;
	private String dest = null;
	private int serviceType = 0;
	private int method = 0;
	private Object object = null;

	public WessionRequest(){}
	public WessionRequest(String senderName, String receiverName) {
		this.src  = senderName;
		this.dest = receiverName;
	}

	public String getSrc() {
		return this.src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getDest() {
		return this.dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public int getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(int type) {
		this.serviceType = type;
	}

	public void setMethod(int method){
		this.method = method;
	}
	public int getMethod(){
		return this.method;
	}
	public void setData(Object object){
		this.object = object;
	}
	public Object getData(){
		return this.object;
	}
	public long getStartTime(){
		return this.startTime;
	}
	public void setStartTime() {
		this.startTime = System.nanoTime();
	}
}
