package com.wow.server;

import java.io.Closeable;
import java.io.IOException;

public interface IServer extends Runnable, Closeable {

	public boolean initialize(IServiceHandler handler);
	public String getName();
	public String getAddress();
	public int getPort();
}
