package com.wow.wession.service;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wow.server.IAcceptor;
import com.wow.server.IServiceHandler;
import com.wow.server.IWorker;

public class SessionService implements IServiceHandler {

	private Logger logger = LoggerFactory.getLogger("syslog");	
	
	public boolean initialize() {
		return true;
	}
	public void close(){}
	public BlockingQueue<Socket> getSocketQueue() {
		// TODO Auto-generated method stub
		return null;
	}

	public IWorker getNewWorker() {
		// TODO Auto-generated method stub
		return null;
	}

	public IAcceptor getAcceptor() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}



}
