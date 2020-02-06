package com.wow.wession.cluster;

import java.io.Serializable;

import com.wow.wession.data.AbstractEntity;
import com.wow.wession.service.ServiceContext;

public class ClusterEntity extends AbstractEntity {
	private static final long serialVersionUID = 5495745623877520034L;
	public ClusterEntity(int cmd){
		this.type 			= ServiceContext.CLUSTER;
		this.time        	= System.currentTimeMillis();
		this.command 	= cmd;
	}
}
