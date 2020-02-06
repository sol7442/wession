package com.wow.wession.pool;

import java.io.IOException;

import com.wow.client.IConnection;


public interface IConnectionPool {
	public IConnection get()throws IOException;
	public void release(IConnection conn);
}
