package com.wow.server;

import java.io.IOException;

public interface IAcceptor {
	public void listen()throws IOException;
	public void close()throws IOException;
	public String getAddress();
	public int getPort();
}
