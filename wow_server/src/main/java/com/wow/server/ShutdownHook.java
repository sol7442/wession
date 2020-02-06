package com.wow.server;

import java.io.Closeable;
import java.io.IOException;

public class ShutdownHook extends Thread {

	private Closeable object;
	private Runtime runtime;
	
	
	public void register(Closeable obj) {
		this.object = obj;
		runtime = Runtime.getRuntime();
		runtime.addShutdownHook(this);
	}
	
	public void run(){
		 if (object != null) {
			 try {
				object.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }		
	}
	
	public void deregister() {
		if (runtime != null) {
			try {
				runtime.removeShutdownHook(this);
			} catch (Exception e) {
					
			}
			runtime = null;
			object = null;
		}
	}
	
	
}
