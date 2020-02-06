package com.wow.server.protocol;

import java.io.Serializable;

public interface IResponse extends Serializable {
	public String getSrc();
	public void setSrc(String src);
	public String getDest();
	public void setDest(String dest);
	public void setResultCode(int code);
	public int getResultCode();
}
