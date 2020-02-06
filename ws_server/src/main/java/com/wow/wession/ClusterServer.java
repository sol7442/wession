package com.wow.wession;

import com.wow.server.socket.SocketServer;

public class ClusterServer extends SocketServer  {
	public ClusterServer(String name) {
		super(name);
	}

//	public void setRepository(WessionRepository repository) {
//		ClusterService cluster_handler = (ClusterService)this.getHandler();
//		cluster_handler.setSessionRepository(repository);
//	}

}
