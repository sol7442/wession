package com.wow.wession.repository;

import java.util.ArrayList;

import com.wow.wession.data.IEntityState;

public class RepositoryMessage implements Cloneable, IEntityState{
	public int method              = ACCESS;
	public boolean clusterable     = false;
	public ArrayList<Object> datas = new ArrayList<Object>();
	
	@SuppressWarnings("unchecked")
	public RepositoryMessage clone() throws CloneNotSupportedException{
		RepositoryMessage msg = (RepositoryMessage)super.clone();
		msg.method 		= this.method;
		msg.clusterable = this.clusterable;
		msg.datas 		= (ArrayList<Object>)this.datas.clone();
		return msg;
	}
}
