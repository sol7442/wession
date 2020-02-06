package com.wow.wession.data;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

public abstract class AbstractEntity implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -6945596350359323336L;

	protected long time     = -1;
	protected int type        = 1;
	protected int command = -1;
	protected Vector<Object> data = new Vector<Object>();

	public long getTime(){
		return this.time;
	}
	public void setTime(long time){
		this.time = time;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getCommand() {
		return command;
	}
	public void setCommand(int command) {
		this.command = command;
	}
	public void addData(Object object){
		this.data.add(object);
	}
	public Vector<Object> getData() {
		return data;
	}
	public void setData(Vector<Object> data) {
		this.data = data;
	}
	public Object getData(int index){
		return this.data.get(index);
	}
	public int getDataSize(){
		return this.data.size();
	}

	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("[").append(new Date(time)).append("]");
		buffer.append("[").append(type).append("]");
		buffer.append("[").append(command).append("]");
		buffer.append("[").append(this.data).append("]");
		return buffer.toString();
	}
//
//	private void writeObject(java.io.ObjectOutputStream stream)  throws IOException {
//	 	stream.writeLong(this.time);
//	 	stream.writeInt(this.type);
//	    stream.writeInt(this.command);
//	    stream.writeInt(getDataSize());
//	    for(int i=0; i<this.data.size();i++){
//	    	stream.writeObject(data.get(i));
//	    }
//	}
//	private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
//	 	this.time  = stream.readLong();
//	 	this.type  = stream.readInt();
//	    this.command = stream.readInt();
//	    int dataSize   = stream.readInt();
//	    this.data = new Vector<Object>();
//	    for(int i=0; i<dataSize;i++){
//	    	this.data.add(stream.readObject());
//	    }
//	}
}
