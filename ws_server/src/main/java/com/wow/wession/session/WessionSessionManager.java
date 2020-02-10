package com.wow.wession.session;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wow.wession.cluster.ClusterManager;
import com.wow.wession.config.SessionManagerConfigDocument.SessionManagerConfig;
import com.wow.wession.journal.JournalManager;
import com.wow.wession.log.IWessionLogger;
import com.wow.wession.repository.IManagerState;
import com.wow.wession.repository.IRepository;
import com.wow.wession.repository.WessionRepository;
import com.wow.wession.server.WessionServerSession;
import com.wow.wession.util.StopWatch;

public class WessionSessionManager implements IRepository , IManagerState {
	private Logger syslogger = LoggerFactory.getLogger(IWessionLogger.SYSTEM_LOGGER);
	private Logger weslogger = LoggerFactory.getLogger(IWessionLogger.WESSION_LOGGER);

	private static WessionSessionManager instance = null;
	private WessionRepository repository = null;
	private JournalManager journal_mgr;
	private ClusterManager cluster_mgr;
	private Properties mgr_properties = new Properties();

	private int sessionExpireTime = 1000*60*30; // 기본 30분
	private ExecutorService executers = null;

	private int minThreadSize = 1;
	private int maxThreadSize = 10;
	private int idleMilliTime    = 1000*10;
	
	//private LinkedBlockingQueue<Runnable> sessionQueue = new LinkedBlockingQueue<Runnable>();
	//private SynchronousQueue<Runnable> sessionQueue = new SynchronousQueue<Runnable>(false);
	
	public static WessionSessionManager getInstance(){
		if(instance == null){
			instance = new WessionSessionManager();
		}
		return instance;
	}

	public boolean initialize(String name, SessionManagerConfig mgrConf) {
		boolean bRes = false;
		try{
			this.sessionExpireTime = mgrConf.getSessionExipreTime();
			this.minThreadSize = mgrConf.getMinThreadSize();
			this.maxThreadSize = mgrConf.getMaxThreadSize();
			this.idleMilliTime    = mgrConf.getIdleMilliTime();
			bRes = true;
		}catch(Exception e){
			syslogger.error("initialize fail : {}",e);
		}finally{
			executers = new ThreadPoolExecutor(maxThreadSize,maxThreadSize, idleMilliTime, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>() ,new RepsitoryThreadFactory());
			//executers = new ThreadPoolExecutor(minThreadSize,maxThreadSize, idleMilliTime, TimeUnit.MILLISECONDS,sessionQueue,new RepsitoryThreadFactory());
		}
		return bRes;
	}

	private class RepsitoryThreadFactory implements ThreadFactory{
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r);
			thread.setName("Repository Worker Thread");
			return thread;
		}
	}
	public void setWessionRepository(WessionRepository repository){
		this.repository = repository;
	}
	public WessionRepository getWessionRepository(){
		return this.repository;
	}

	public void setJournalManager(JournalManager j_mgr) {
		this.journal_mgr = j_mgr;
	}
	public JournalManager getJournalManager(){
		return this.journal_mgr;
	}
	public void setClusterManager(ClusterManager c_mgr) {
		this.cluster_mgr = c_mgr;
	}
	public ClusterManager getClusterManager(){
		return this.cluster_mgr;
	}

	public int getSessionTimeout(){
		return this.sessionExpireTime;
	}
	
	public void start() {
		this.cluster_mgr.start();
		this.journal_mgr.start();
	}
	public void stop(){
		this.cluster_mgr.stop();
		this.journal_mgr.stop();

		this.executers.shutdown();
		syslogger.info("Stop Wession SessionManager");
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public int getSize() {
		return this.repository.getSize();
	}

	public ISession get(String key) {
		return this.repository.get(key);
	}
	public List<ISession> getBy(String handle, String key){
		return repository.getBy(handle,key);
	}
	public Iterator<ISession> iterator(){
		return repository.iterator();
	}

	public Collection<ISession> getAll(){
		return repository.getAll();
	}
	/*********************************************************************************************************
	 * 세션 메니저의 속성 정보를 관리한다.
	 * 속성을 삭제할 때는 value 값에 공백 또는 널 값을 설정한다.
	 *********************************************************************************************************/
	public void setProperty(final String key, final String value){
		setProperty(key, value,true);
	}
	public void setProperty(final String key, final String value,final boolean cluster){
		final StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);
		try{
			mgr_properties.setProperty(key, value);
			this.executers.execute(new Runnable() {
				public void run() {
					//journal_mgr.setProperty(key, value);
					if(cluster){
						cluster_mgr.setProperty(key, value);
					}
					weslogger.debug("setProperty : key[{}], value[{}]-{}",key,value,watch.stop());
				}
			});
		}catch(Exception e){
			weslogger.error("setProperty : {},{}-{}",key,value,e);
		}
	}
	public Set<Object> getPropertyKeySet(){
		return this.mgr_properties.keySet();
	}
	public String getProperty(String key){
		return mgr_properties.getProperty(key);
	}

	/*********************************************************************************************************
	 * 신규 서버 세션을 저장한다.
	 * .
	 *********************************************************************************************************/
	public void create(final ISession session){
		create(session,true);
	}

	public void create(final ISession session, final boolean cluster){
		final StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);
		try{
			this.executers.execute(new Runnable() {
				public void run() {
					try{
						repository.create(session);
						journal_mgr.create(session);
						if(cluster){
							cluster_mgr.create(session);
						}
					}finally{
						weslogger.info("create : session[{}]-{}",session,watch.stop());
					}
				}
			});
		}catch (RuntimeException e) {
			weslogger.error("create : {}-{}",session,e);
		}
	}
	/*********************************************************************************************************
	 *  서버 세션을 삭제한다.
	 * .
	 *********************************************************************************************************/
	public void expire(String key){
		expire(key,true);
	}
	public void expire(final String key,final boolean cluster) {
		final StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);
		try{
			this.executers.execute(new Runnable() {
				public void run() {
					try{
						repository.expire(key);
						journal_mgr.expire(key);
						if(cluster ){
							cluster_mgr.expire(key);
						}
					}finally{
						weslogger.info("expire : key[{}]-{}",key,watch.stop());
					}
				}
			});
		}catch (RuntimeException e) {
			weslogger.error("expire : {}-{}",key,e);
		}
	}

	/*********************************************************************************************************
	 *  에이전트 세션을 추가한다.
	 * .
	 *********************************************************************************************************/
	public void add(String parent, ISession session){
		WessionServerSession server_session =  (WessionServerSession)repository.get(parent);
		if(server_session != null){
			server_session.addAgentSession(session, false);
		}
	}
	public void add(final String parent, final ISession session,final boolean cluster) {
		final StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);
		try{
			this.executers.execute(new Runnable() {
				public void run() {
					try{
						journal_mgr.add(parent, session);
						if(cluster){
							cluster_mgr.add(parent, session);
						}
					}finally{
						weslogger.info("add : parent[{}],session[{}]-{}",parent,session,watch.stop());
					}
				}
			});
		}catch (RuntimeException e) {
			weslogger.error("add : {},{}-{}",parent,session,e);
		}
	}
	/*********************************************************************************************************
	 *  에이전트 세션을 삭제한다.
	 * .
	 *********************************************************************************************************/
	public void remove(String parent, String key){
		WessionServerSession server_session =  (WessionServerSession)repository.get(parent);
		if(server_session != null){
			server_session.removeAgentSession(key,false);
		}
	}
	public void remove(final String parent, final String key, final boolean cluster) {
		final StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);
		try{
			this.executers.execute(new Runnable() {
				public void run() {
					try{
						journal_mgr.remove(parent,key);
						if(cluster){
							cluster_mgr.remove(parent, key);
						}
					}finally{
						weslogger.info("remove : parent[{}],key[{}]-{}",parent,key,watch.stop());
					}
				}
			});
		}catch (RuntimeException e) {
			weslogger.error("remove : {},{}-{}",parent,key,e);
		}
	}
	/*********************************************************************************************************
	 *  세션의 속성을 설정한다.
	 * .
	 *********************************************************************************************************/
	public void set(String parent,String key,Object object){
		WessionServerSession server_session =  (WessionServerSession)repository.get(parent);
		if(server_session != null){
			server_session.setAttribute(key, object,false);
		}
	}
	public void set(final String parent,final String key,final Object object,final boolean cluster) {
		final StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);
		try{
			this.executers.execute(new Runnable() {
				public void run() {
					try{
						journal_mgr.set(parent, key, object);
						if(cluster){
							cluster_mgr.set(parent, key, object);
						}
					}finally{
						weslogger.info("set : parent[{}],key[{}],object[{}]-{}",parent,key,object,watch.stop());
					}
				}
			});
		}catch (RuntimeException e) {
			weslogger.error("set : {},{},{}-{}",parent,key,object,e);
		}
	}

	/*********************************************************************************************************
	 *  세션의 속성을 삭제한다..
	 * .
	 *********************************************************************************************************/
	public void delete(String parent,String key){
		WessionServerSession server_session =  (WessionServerSession)repository.get(parent);
		if(server_session != null){
			server_session.removeAttribute(key,false);
		}
	}
	public void delete(final String parent, final String key, final boolean cluster) {
		final StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);
		try{
			this.executers.execute(new Runnable() {
				public void run() {
					try{
						journal_mgr.delete(parent,key);
						if(cluster){
							cluster_mgr.delete(parent, key);
						}
					}finally{
						weslogger.info("delete : parent[{}],key[{}]-{}",parent,key,watch.stop());
					}
				}
			});
		}catch (RuntimeException e) {
			weslogger.error("set : {},{}-{}",parent,key,e);
		}
	}

	/*********************************************************************************************************
	 *  세션 저장소를 초기화한다.
	 * .
	 *********************************************************************************************************/
	public void clear() {
		clear(true);
	}
	public void clear(final boolean cluster) {
		final StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);
		try{
			this.executers.execute(new Runnable() {
				public void run() {
					try{
						repository.clear();
						journal_mgr.clear();
						if(cluster){
							cluster_mgr.clear();
						}
					}finally{
						weslogger.info("clear:-{}",watch.stop());
					}
				}
			});
		}catch (RuntimeException e) {
			weslogger.error("clear : -{}",e);
		}
	}


}
