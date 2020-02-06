package com.wow.wession.service.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Vector;

import com.wow.server.protocol.IResponse;
import com.wow.wession.session.ISession;

public class WessionResponse implements IResponse, IWessionResult{

	/**
	 *
	 */
	private static final long serialVersionUID = -2964341350782529017L;

	transient private long startTime;
	transient private long endTime;

	private String src = null;
	private String dest = null;
	private int resultCode = 0;
	private Object object = null;

	//private Vector<Serializable> data = null;//new Vector<Serializable>();

	public WessionResponse(){
	}
	public WessionResponse(WessionRequest request) {
		this.startTime = request.getStartTime();
		this.src  = request.getDest();
		this.dest = request.getSrc();
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

	public void setResultCode(int code) {
		this.resultCode = code;
	}

	public int getResultCode() {
		return this.resultCode;
	}
	public long getStartTime() {
		return startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime() {
		this.endTime = System.nanoTime();
	}
	public void setData(Object object){
		this.object = object;
	}
	public Object getData(){
		return this.object;
	}

	private void writeObject(java.io.ObjectOutputStream stream)  throws IOException {
	 	stream.writeObject(this.src);
	 	stream.writeObject(this.dest);
        stream.writeInt(this.resultCode);
        stream.writeObject(this.object);
    }

	private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
	 	this.src  			= (String)stream.readObject();
	 	this.dest 			= (String)stream.readObject();
	    this.resultCode 	= stream.readInt();
	    this.object       	= stream.readObject();
	}
}
