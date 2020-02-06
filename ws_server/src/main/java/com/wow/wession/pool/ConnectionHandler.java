package com.wow.wession.pool;


import java.io.IOException;
import java.net.UnknownHostException;
import com.wow.client.IConnection;
import com.wow.client.socket.SocketConnection;
public class ConnectionHandler {
	private String ip;
	private int port;
	
	public ConnectionHandler(String ip, int port){
		setIp(ip);
		setPort(port);
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public IConnection createConnection() throws UnknownHostException, IOException{
		IConnection conn = null;
		conn = new SocketConnection(ip, port);
		return conn;
	}
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("Connection : ").append(this.ip).append(",").append(this.port);
		return buffer.toString();
	}
}
