package com.wow.server;

import java.io.IOException;


public interface IConnectionHandler {

	Object readObject()throws ClassNotFoundException, IOException;
	void writeObject(Object obj)throws IOException;
}
