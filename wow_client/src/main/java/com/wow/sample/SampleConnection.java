package com.wow.sample;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class SampleConnection {
	private String ip;
	private int port;
	private Socket socket;

	public SampleConnection(){}
	public SampleConnection(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	public void connect() throws IOException{
		this.socket = new Socket(this.ip, this.port);
		System.out.println("connect : " + this.socket.isConnected());
	}
	public void close() throws IOException {
		if(this.socket != null && !this.socket.isClosed()){
			this.socket.close();
		}
	}
	public void connect(String ip, int port) throws IOException {
		this.ip = ip;
		this.port = port;
		connect();
	}
	public String getIPAddress(){
		return this.ip;
	}
	public int getPort(){
		return this.port;
	}
	public InputStream getInputStream() throws IOException{
		return this.socket.getInputStream();
	}
	public OutputStream getOutputStream() throws IOException{
		return this.socket.getOutputStream();
	}
}
