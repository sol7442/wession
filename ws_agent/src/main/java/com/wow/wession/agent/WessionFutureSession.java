package com.wow.wession.agent;

import java.util.concurrent.atomic.AtomicBoolean;

import com.wow.wession.session.ISessionState;

/** FUTURE OBJECT
 * @author EJLEE-DELL
 *
 */
public class WessionFutureSession{
	private AtomicBoolean ready = new AtomicBoolean(false);
	private WessionAgentSession session = null;
		
	public WessionFutureSession(WessionAgentSession session){
		this.session = session;
	}
	public String getID(){
		return this.session.getID();
	}
	
	synchronized public void setState(int state, String msg) {
		//System.out.println("STATE CHANGE :  " + this.session.getState() + " -> " + state + " : " + msg);
		this.session.setState(state);		
		ready.set(true);
		notifyAll();		
	}
	synchronized public void set(WessionAgentSession session){
		///System.out.println("STATE CHANGE :  " + this.session.getState() + " -> " + session.getState());
		this.session = session;		
		ready.set(true);
		notifyAll();	
	}

	/**
	 * @return 
	 *    - 서버 검증 전 : 원본 세션을 반환한다.
	 *    - 서버 검증 후 : 변경된 세션을 반환한다. 
	 */
	synchronized public WessionAgentSession get(){
		return this.session;
	}

	/**
	 * @param timeout 
	 * @return
	 *    - 서버 검증이 될 때 까지 기다린다. 
	 *    - 서버 검증에 대한 응답이 없거나 에러가 발생할 경우, 세션 상태를 검증 불가로 만든다.  
	 */   
	synchronized public WessionAgentSession get(long timeout) {
		if(ready.get() == false){
			try {
				wait(timeout);
				if(ready.get() == false){
					setState(ISessionState.DUMMY_FAIL, "TIME OUT");
				}
			} catch (InterruptedException e) {
				setState(ISessionState.DUMMY_FAIL, e.getMessage());
			}
		}
		return this.session;
	}
}
