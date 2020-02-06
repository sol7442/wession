package com.wow.wession.session;

import java.io.Serializable;

public interface IAttribute extends Serializable{
	public String getKey();
	public Object getValue();
}
