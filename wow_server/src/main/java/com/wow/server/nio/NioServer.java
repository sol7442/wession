package com.wow.server.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import com.wow.server.IAcceptor;
import com.wow.server.IServer;
import com.wow.server.IServiceHandler;

public abstract class NioServer  implements IServer {

	public final String instanceName;
	
	private IAcceptor acceptor = null;
	private IServiceHandler handler = null;
	
	public NioServer(String name){
		instanceName = name; 		
	}
	
	
	public boolean initialize(IServiceHandler handler){
		setHandler(handler);
		//set configuration
		//create worker pool
		//create acceptor
		//InetSocketAddress address = new InetSocketAddress(InetAddress.getLocalHost(), 5000);
		//acceptor = new NioAcceptor(handler, address);
		return true;
	}

	
	
	
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}


	public void run() {
		
		try {
			acceptor.listen();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
		}
	}


	public IServiceHandler getHandler() {
		return handler;
	}


	public void setHandler(IServiceHandler handler) {
		this.handler = handler;
	}
	
	public void onConnectionAccepted(){
		// create NIOConnection;
		NonBlockingConnection conn = new NonBlockingConnection();
	}
}
