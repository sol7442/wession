package com.wow.wession.data;

import java.io.Serializable;

public class WessionMessage implements Serializable {
	private static final long serialVersionUID = 1769989765704165137L;

	private int 	type;
	private Object 	data;

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
