package com.wow.server;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public interface IServiceHandler {
	public BlockingQueue<Socket> getSocketQueue();
	public IWorker getNewWorker();
	public boolean initialize();
	public void close();
	public IAcceptor getAcceptor();
	public String getName();
}
