package com.wow.wession.service;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import com.wow.server.IAcceptor;
import com.wow.server.IServiceHandler;
import com.wow.server.IWorker;

public class ManagerService implements IServiceHandler {

	public BlockingQueue<Socket> getSocketQueue() {
		// TODO Auto-generated method stub
		return null;
	}

	public IWorker getNewWorker() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean initialize() {
		return true;
	}
	public void close(){}
	public IAcceptor getAcceptor() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}


}
