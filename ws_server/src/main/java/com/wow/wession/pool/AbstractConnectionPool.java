package com.wow.wession.pool;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.wow.client.IConnection;


public abstract class AbstractConnectionPool implements IConnectionPool {
	protected BlockingQueue<IConnection> queue = new ArrayBlockingQueue<IConnection>(100);
}
