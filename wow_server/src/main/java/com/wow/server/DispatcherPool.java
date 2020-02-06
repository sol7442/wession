package com.wow.server;

import java.util.ArrayList;
import java.util.List;


public class DispatcherPool {

	private int pointer = 0;
	private List<IDispatcher> dispatchers = new ArrayList<IDispatcher>();
	public void addDispatcher(IDispatcher dispatcher){
		this.dispatchers.add(dispatcher);
	}
	public  IDispatcher next() {
		IDispatcher dispatcher = null;
		if(pointer<dispatchers.size()){
			dispatcher = dispatchers.get(pointer);
			pointer++;
			
			if(pointer > dispatchers.size()){
				pointer = 0;
			}
		}
		return dispatcher;
	}

}
