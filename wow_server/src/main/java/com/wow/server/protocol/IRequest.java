package com.wow.server.protocol;

import java.io.Serializable;

public interface IRequest extends Serializable {
	public String getSrc();
	public void setSrc(String src);
	public String getDest();
	public void setDest(String dest);
	public int getServiceType();
	public void setServiceType(int type);
}
