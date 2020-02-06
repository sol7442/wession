package com.wow.server.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.wow.server.IAcceptor;
import com.wow.server.IServiceHandler;
import com.wow.server.IWorker;

public class SocketAcceptor implements IAcceptor {

	private ServerSocket serverSocket;
	private IServiceHandler serviceHandler;
	private ExecutorService executorService; 
	private AtomicBoolean serverRun = new AtomicBoolean(true);
	
	public SocketAcceptor(IServiceHandler handler, int port, int minThread,int maxThread,int idleTime) throws IOException {
		this.serviceHandler = handler;
		serverSocket = new ServerSocket(port);		
		executorService = new ThreadPoolExecutor(maxThread,maxThread, idleTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1000),new WorkerThreadFactory());
	}

	private class WorkerThreadFactory implements ThreadFactory{
		public Thread newThread(Runnable worker) {
			return new Thread(worker,serviceHandler.getName() + " Worker");
		}
	}
	
	public void listen() throws IOException {
		
		while(serverRun.get()){
			try {
				Socket socket = serverSocket.accept();
				this.serviceHandler.getSocketQueue().put(socket);
				IWorker worker =  this.serviceHandler.getNewWorker();
				executorService.execute(worker);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
				break;
			}
		}
		System.out.println(this.serviceHandler.getName() + " : listener stop");
	}

	public void close() throws IOException {
		serverRun.set(false);
		this.serverSocket.close();		
		this.executorService.shutdownNow();//.shutdown();
	}

	@SuppressWarnings("static-access")
	public String getAddress() {
		String address = "";
		try{
			address = this.serverSocket.getInetAddress().getLocalHost().getHostAddress();
		}catch(Exception e){
		}
		return address;
	}

	public int getPort() {
		return this.serverSocket.getLocalPort();
	}

}
