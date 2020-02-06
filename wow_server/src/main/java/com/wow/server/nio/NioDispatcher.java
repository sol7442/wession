package com.wow.server.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import com.wow.server.IDispatcher;

public class NioDispatcher implements IDispatcher {

	Selector selector;
	
	public void register(SocketChannel channel) {
		 NonBlockingConnection connection = new NonBlockingConnection();
		
		 //socketHandler.getChannel().register(selector, ops, socketHandler);
		 
	}

	public void run() {
		
		while(true){
			try {
				if(selector.select() > 0){
					Set<SelectionKey> selectedEventKeys = selector.selectedKeys();
					Iterator<SelectionKey> it = selectedEventKeys.iterator();
					
					SelectionKey eventKey = it.next();
					SocketHanlder handler = (SocketHanlder) eventKey.attachment();
					//read
					handler.onReadEvent();
					//write
					handler.onWriteEvent();
					
					
				}
				
				//read
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
