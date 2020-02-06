package com.wow.wession.cluster;

import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wow.client.IConnection;
import com.wow.wession.log.IWessionLogger;
import com.wow.wession.pool.ConnectionHandler;

public class ConnectionPool {
	
	private static Logger syslogger = LoggerFactory.getLogger(IWessionLogger.SYSTEM_LOGGER);
	
    /** The queued items  */
    private final Vector<IConnection> items;
    private ConnectionHandler handler;
    private int count = 0;
    
    /** Main lock guarding all access */
    private final ReentrantLock lock;
    
    public ConnectionPool(String ip,int port, int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException();
        this.items = new Vector<IConnection>(capacity);
        lock = new ReentrantLock(false);        
        this.handler = new ConnectionHandler(ip, port);
    }
    
    
	public boolean release(IConnection con) {
        if (con == null) throw new NullPointerException();
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
        	if(this.items.add(con) == false){
        		try {
        			con.close();
				} catch (Exception e) {
					syslogger.error(e.getMessage());
				}
        	   	return false;
           }else{
        	   count++;
        	   	return true;
           	}
        } finally {
            lock.unlock();
        }
	}

	public IConnection poll() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
        	if(count <= 0){
        		try {
					return this.handler.createConnection();
				} catch (Exception e) {
					syslogger.error(e.getMessage());
					return null;
				} 
        	}else{
        		count--;
        		return this.items.lastElement();
        	}
        } finally {
            lock.unlock();
        }
	}

	public int size() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
	}
}
