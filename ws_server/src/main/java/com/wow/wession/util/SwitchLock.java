package com.wow.wession.util;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author user
 *
 */
public class SwitchLock {
	private Lock lock = new ReentrantLock();	
	private Condition condition = lock.newCondition();
	private AtomicBoolean bswitch = new AtomicBoolean(true);
	
	
	/**
	 * @return
	 * @throws InterruptedException
	 */
	public boolean getSwitch() throws InterruptedException{
		ReentrantLock localLock = (ReentrantLock) this.lock;
		localLock.lock();
		try{
			while(bswitch.get()==false){
				this.condition.await();
			}
		}finally{
			localLock.unlock();
		}
		return bswitch.get();
	}
	
	/**
	 * true  = 스위치 온
	 * false = 스위치 오프 
	 * @param ready
	 */
	public void setSwtich(boolean on){
		ReentrantLock localLock = (ReentrantLock) this.lock;
		localLock.lock();
		try{
			this.bswitch.set(on);
			if(this.bswitch.get() == true){
				this.condition.signalAll();
			}
		}finally{
			localLock.unlock();
		}		
	}	
}
