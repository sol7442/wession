package com.wow.server.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import com.wow.server.DispatcherPool;
import com.wow.server.IAcceptor;
import com.wow.server.IDispatcher;
import com.wow.server.IServiceHandler;

public class NioAcceptor implements IAcceptor{

	private DispatcherPool dispatcherPool;
	private ServerSocketChannel serverChannel;
	private IServiceHandler callback;
	
	public NioAcceptor(IServiceHandler handler, InetSocketAddress address) {
		try {
			callback = handler;
			
			// create server channel.
			serverChannel = ServerSocketChannel.open();
			serverChannel.configureBlocking(true);
			
			// set socket option
	        serverChannel.socket().setSoTimeout(0);
	        serverChannel.socket().setReuseAddress(true); 
			
	        // socket binding
	        serverChannel.socket().bind(address, 0);
			
	        // create DispatcherPool
	        dispatcherPool = new NioDispatcherPool();
			
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void listen() throws IOException{

		while(true){
			SocketChannel channel = serverChannel.accept();
			
			IDispatcher dispatcher = dispatcherPool.next();
			
			//dispatcher.register(channel);
		}
	}

	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

	public String getAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getPort() {
		// TODO Auto-generated method stub
		return 0;
	}

}
