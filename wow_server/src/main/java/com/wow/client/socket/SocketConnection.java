package com.wow.client.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.wow.client.IConnection;
import com.wow.server.protocol.IRequest;
import com.wow.server.protocol.IResponse;

public class SocketConnection implements IConnection{
	private Socket socket;
	private ObjectInputStream objInput;
	private ObjectOutputStream objOutput;
	private boolean keepAlive = false;

	public SocketConnection(String ip, int port) throws UnknownHostException, IOException{
		connect(ip, port);
	}

	public void setKeepAlive(boolean value){
		this.keepAlive = value;
	}
	public boolean isKeepAlive(){
		return this.keepAlive;
	}
	public SocketConnection(Socket socket) throws IOException {
		this.socket = socket;
		ServerSocketInitialize();
	}

	public String getIPAddress(){
		return this.socket.getInetAddress().getHostAddress();
	}

	private void ServerSocketInitialize() throws IOException{
		 objOutput = new ObjectOutputStream(socket.getOutputStream());
		 objInput = new ObjectInputStream(socket.getInputStream());
	}

	private void ClientSocketInitialize() throws IOException{
		 objInput = new ObjectInputStream(socket.getInputStream());
		 objOutput = new ObjectOutputStream(socket.getOutputStream());
	}

	public void close() throws IOException {
		if(this.socket != null){
			this.objInput.close();
			this.objOutput.close();
			this.socket.close();
		}
	}

	public void connect(String ip, int port) throws UnknownHostException,IOException {
		 this.socket = new Socket(ip, port);
		 ClientSocketInitialize();
	}
	public boolean isConnected() {
		boolean bRes = false;
		if(socket != null){
			bRes = socket.isConnected();
		}
		return bRes;
	}

	public IRequest readRequest() throws ClassNotFoundException, IOException {
		return (IRequest)this.objInput.readObject();
	}

	public void writeRequest(IRequest request) throws IOException {
		this.objOutput.writeObject(request);
		this.objOutput.flush();
		this.objOutput.reset();
	}

	public IResponse readResponse() throws ClassNotFoundException, IOException {
		return (IResponse)this.objInput.readObject();
	}

	public void writeResponse(IResponse response) throws IOException {
		this.objOutput.writeObject(response);
		this.objOutput.flush();
		this.objOutput.reset();
	}

	public int getPortNumber() {
		return this.socket.getPort();
	}

	public String toString(){
		if(socket != null){
			return socket.toString();
		}else{
			return "socket is null";
		}
	}

}
