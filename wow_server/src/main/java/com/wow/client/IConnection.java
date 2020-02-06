package com.wow.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.UnknownHostException;

import com.wow.server.protocol.IRequest;
import com.wow.server.protocol.IResponse;


public interface IConnection extends Closeable {
	void connect(String ip,int port) throws UnknownHostException, IOException;
	public IRequest readRequest() throws ClassNotFoundException, IOException ;
	public void writeRequest(IRequest request) throws IOException ;
	public IResponse readResponse() throws ClassNotFoundException, IOException ;
	public void writeResponse(IResponse response) throws IOException ;
	
	public boolean isConnected();
	//public void setConnected(boolean yesno);
}
