package com.wow.server.socket;

import java.io.IOException;

import com.wow.server.IAcceptor;
import com.wow.server.IServer;
import com.wow.server.IServiceHandler;

public abstract class SocketServer implements IServer {

	private String name;

	private IAcceptor acceptor;
	private IServiceHandler handler;

	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}

	public SocketServer(String name) {
		this.name = name;
	}

	public IServiceHandler getHandler(){
		return this.handler;
	}

	public void run() {
		try {
			acceptor.listen();
		} catch (IOException e) {
			System.out.println("Socket Acceptor Close : " + e.getMessage());
		} finally{
			System.out.println("SocketServer Listener Closed");
		}
		System.out.println("SocketServer Closed");
	}
	public void close() throws IOException {
		handler.close();
	}

	public boolean initialize(IServiceHandler handler){
		this.handler = handler;
		if(handler.initialize()){
			this.name = this.name + "["+handler.getName()+"]";
			this.acceptor = handler.getAcceptor();
			return true;
		}else{
			return false;
		}
	}

	public String getAddress() {
		return this.acceptor.getAddress();
	}

	public int getPort() {
		return this.acceptor.getPort();
	}

}
