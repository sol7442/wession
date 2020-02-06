package com.wow.wession.journal;

import java.io.Serializable;

import com.wow.wession.data.AbstractEntity;
import com.wow.wession.service.ServiceContext;

public class JournalEntity extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 3796795297513492180L;

	public JournalEntity(int cmd){
		this.type 			= ServiceContext.JOUNAL;
		this.time 			= System.currentTimeMillis();
		this.command 	= cmd;
	}
}
