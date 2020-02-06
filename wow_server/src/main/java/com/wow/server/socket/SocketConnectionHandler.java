package com.wow.server.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.wow.server.AbstractConnection;
import com.wow.server.IConnectionHandler;

public class SocketConnectionHandler extends AbstractConnection implements IConnectionHandler {

	private Socket socket;


	//private BufferedInputStream input;
	//private BufferedOutputStream output;
	private ObjectInputStream objInput;
	private ObjectOutputStream objOutput;

	public SocketConnectionHandler(Socket socket) throws IOException {
		this.socket = socket;
		objOutput = new ObjectOutputStream(socket.getOutputStream());
		objInput = new ObjectInputStream(socket.getInputStream());
	}
	public Socket getSocket() {
		return socket;
	}

	public void close() throws IOException {
		this.socket.close();
	}

	public Object readObject() throws ClassNotFoundException, IOException {
		return this.objInput.readObject();
	}

	public void writeObject(Object obj) throws IOException {
		this.objOutput.writeObject(obj);
		this.objOutput.flush();
		this.objOutput.reset();
	}
}
