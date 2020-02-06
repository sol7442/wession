package com.wow.wession.agent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.wow.wession.license.WessionLicense;
import com.wow.wession.session.ISessionMessage;
import com.wow.wession.session.ISessionState;
import com.wow.wession.session.SessionFactory;

/**
 * 서버와의 통신을 callback 방식으로 변경.
 *  - 통신구간 timeout 설정 및 dump 생성을 위한 수정
 * 
 * @author EJLEE-DELL
 * 
 */
public class WessionAgent {
	private static WessionAgent instance = null;
	
	private String DEFAULT_FUTER_URL;

	private Map<String, Object> properties = new HashMap<String, Object>();	
	private ConcurrentHashMap<String, WessionFutureSession> future_repository 	= new ConcurrentHashMap<String, WessionFutureSession>();
	private SessionFactory sessionFactory;
		
	private WessionTranser transer = new WessionTranser();
	private ExecutorService futureThreadPool;

	public static WessionAgent getInstance(){
		if(instance == null){
			instance = new WessionAgent();
		}
		return instance;
	}
	
	/**
	 * 
	 * @param server_url   : 서버 URL
	 * @param thread_size : 에이전트에서 서버와 통신할 때 사용하는 최대 thread 갯수
	 *    - 최소 thread 갯수는 1, 필요할 경우 max 수치가지 늘어난다. 
	 *    - 통신에 필요한 최대 커넥션은 max * 3
	 */
	public void initialize(String server_url, int thread_size, WessionLicense license){		
		if(license.load() == true){
			DEFAULT_FUTER_URL = server_url;
			futureThreadPool = new ThreadPoolExecutor(thread_size,thread_size,0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());		
			transer.initialize(thread_size);		
		}else{
			DEFAULT_FUTER_URL = server_url;
			futureThreadPool = new ThreadPoolExecutor(1,1,0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());		
			transer.initialize(1);		
		}
	}	
	
	public void stop(){
		this.futureThreadPool.shutdown();
	}
	public void setSessionFactory(SessionFactory factory){
		this.sessionFactory = factory;
		
		
	}
	
	/**
	 * 세션객체 비동기 저장
	 * @param session
	 */
	public void add(WessionAgentSession session){
		WessionFutureSession future =  new WessionFutureSession(session);
		future_repository.put(session.getID(),future);
	}
	
	
	/**
	 * 세션객체 비동기 삭제
	 * @param key
	 * @return
	 */
	public WessionAgentSession remove(String key){
		WessionFutureSession future = future_repository.remove(key);
		if(future != null){
			return future.get();
		}else{
			return null;
		}
	}
	
	/***[WESSION AGENT 객체 접근]*************************/
	public WessionAgentSession create(Object object){
		WessionAgentSession session = (WessionAgentSession)sessionFactory.create(object);
		WessionFutureSession future =  new WessionFutureSession(session);
		future_repository.put(session.getID(),future);
		futureThreadPool.execute(new RequestToServer(future,WessionAgentMessage.EVT_SESSION_CREATED))	; 
		return session;
	}
	/**
	 * Agent 저장소에서 세션의 객체를 삭제한다.
	 * @param key   = 세션의 키
	 * @param trans = 세션된 세션이 서버와 동기화 된다.
	 * @return
	 */
	public WessionAgentSession expire(String key, boolean trans) {
		WessionAgentSession session = null;
		WessionFutureSession future = this.future_repository.remove(key);
		if(future != null && trans){
			session = future.get();
			futureThreadPool.execute(new RequestToServer(future,WessionAgentMessage.EVT_SESSION_EXPIRED))	;
		}
		return session;
	}
	public void clear(){
		this.future_repository.clear();
	}
	/**
	 * @param key
	 * @return 
	 */
	public WessionAgentSession get(String key){
		WessionFutureSession future = future_repository.get(key);
		if(future != null){
			return  future.get();
		}else{
			return null;
		}
	}
	
	/**
	 * @param key
	 * @param time_out : mil-second
	 * @return 
	 */
	public WessionAgentSession get(String key, long time_out){
		WessionFutureSession future = future_repository.get(key);
		if(future != null){
			return  future.get(time_out);
		}else{
			return null;
		}
	}
	
	/**
	 * @return
	 *   - 모든 세션을 추출된다.
	 */
	public List<WessionAgentSession> getAgentList() {
		List<WessionAgentSession> session_list = new ArrayList<WessionAgentSession>();
		Collection<WessionFutureSession> future_list = this.future_repository.values();
		for (WessionFutureSession future : future_list) {
			session_list.add(future.get());
		}
		return session_list; 
	}
	/**
	 * @param state
	 * @return
	 *    상태 코드가 동일한 세션만 추출된단.
	 */ 
	public List<WessionAgentSession> getAgentList(int state) {
		List<WessionAgentSession> session_list = new ArrayList<WessionAgentSession>();
		Collection<WessionFutureSession> future_list = this.future_repository.values();
		for (WessionFutureSession future : future_list) {
			WessionAgentSession session = future.get();
			if(session.getState() == state){
				session_list.add(session);
			}
		}
		return session_list; 
	}
	
	/***[WESSION AGENT PROPERTIES]*************************/
	public void setProperty(String key, String value){
		this.properties.put(key, value);
	}
	public String getProperty(String key){
		return (String)this.properties.get(key);
	}	 
	public Set<String> getPropertyKeySet(){
		return this.properties.keySet();
	}
	public int getSize(){
		return this.future_repository.size();
	}
	
	/***[WESSION AGENT THREAD CONTROL]*************************/
	private class RequestToServer implements Runnable{
		private WessionFutureSession future = null;
		private int requestType  = 1;
		private String    url       = DEFAULT_FUTER_URL; 
		public RequestToServer(WessionFutureSession future, int type){
			this.future 			= future;
			this.requestType 	= type;
		}
		public void run()  {
			WessionAgentMessage request  = new WessionAgentMessage(this.requestType,this.future.get());
			try {
				WessionAgentMessage response = (WessionAgentMessage)transer.trans(url,request);
				this.future.set((WessionAgentSession)response.getSession());
			} catch (TransException e) {
				this.future.setState(ISessionState.DUMMY_FAIL, e.getUrl() + " : " + e.getMessage() + " : " + e.getCode());
			}
		}
	}
	public void setTranserEnable(boolean enable){
		this.transer.setEnable(enable);
	}
	public boolean isTranserEnable(){
		return this.transer.isEnable();
	}
	public ISessionMessage send(String url, ISessionMessage request) throws TransException{
		return transer.trans(url,request);
	}
}
