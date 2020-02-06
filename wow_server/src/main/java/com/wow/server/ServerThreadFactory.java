package com.wow.server;

import java.util.concurrent.ThreadFactory;

public class ServerThreadFactory implements ThreadFactory{

	private IServer server;
	public Thread newThread(Runnable r) {
		server = (IServer)r;
		Thread thread = new Thread(r);
		thread.setName(server.getName());
		thread.setDaemon(true);
		return thread;
	}	
}
