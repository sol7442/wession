package com.wow.wession.util;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class CustomScheduler {
	private ScheduledExecutorService scheduler = null;
	private String     name  = "";
	private Runnable action = null;
	public CustomScheduler(String name, Runnable action){
		this.name  = name;
		this.action = action;
	}
	
	public void start(int delay, int period){
		if(this.scheduler == null){
			create(name);
		}
		
		this.scheduler.scheduleAtFixedRate(this.action, delay, period, TimeUnit.MILLISECONDS);
	}
	public void stop(){
		this.scheduler.shutdown();
		this.scheduler = null;
	}
	private void create(final String name){
		if(this.scheduler == null){
			this.scheduler = new  ScheduledThreadPoolExecutor(1, new ThreadFactory() {
				public Thread newThread(Runnable r) {
					return new Thread(r,name);
				}
			});
		}
	}
}
