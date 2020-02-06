package com.wow.wession.journal;

import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wow.wession.config.JournalManagerConfigDocument.JournalManagerConfig;
import com.wow.wession.config.WSConfigurationManager;
import com.wow.wession.config.ClusterManagerConfigDocument.ClusterManagerConfig;
import com.wow.wession.log.IWessionLogger;
import com.wow.wession.repository.IManagerState;
import com.wow.wession.repository.IRepository;
import com.wow.wession.session.ISession;
import com.wow.wession.session.WessionSessionManager;
import com.wow.wession.util.StopWatch;

public class JournalManager implements IRepository, IManagerState {

	private Logger syslogger = LoggerFactory.getLogger(IWessionLogger.SYSTEM_LOGGER) ;
	private Logger weslogger = LoggerFactory.getLogger(IWessionLogger.WESSION_LOGGER);

	//private WessionRepository repository;

	private JournalFile journalFile;
	private ScheduledExecutorService scheduler ;

	private int backPeriodTime = 1000*60*10;
	private int backDelayTime  = 1000*60*1;
	private boolean useable    = false;
	
	
	public boolean initialize(String instance_name, int expire_time,JournalManagerConfig mgrConf) {
		String data_path = "./data";		
		try{
			this.backPeriodTime  	= mgrConf.getBackupPeriodTime();
			this.backDelayTime   	= mgrConf.getBackupDelayTime();		
			data_path         		= mgrConf.getDataDirectory();
			useable 					= mgrConf.getUseable();;
		}catch(Exception e){
			syslogger.error("initialize fail {}",e);
		}finally{
			if(useable){
				journalFile = new JournalFile(data_path,expire_time,backPeriodTime);
				journalFile.initialize(instance_name);
				
				scheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
					public Thread newThread(Runnable r) {
						return new Thread(r,"Journal Dump Thread");
					}
				});
			}
		}
		return useable;
	}

	public void start() {
		if(!useable){return;}
		
		syslogger.info("Start Wession Journal manager");
		
		syslogger.info("journal start ------------------------>");
		if( jounralByNetwork() == 0){
			 jounralByFile();
		}
		syslogger.info("journal end  ------------------------>");
		
		// ���꼸留� �뜡�봽 �뒪耳�以꾨윭 �떆�옉
		scheduler.scheduleAtFixedRate(new Runnable() {
			public void run() {
				dump();
			}
		}, backDelayTime, backPeriodTime,TimeUnit.MILLISECONDS);

	}

	public void stop() {
		if(!useable){return;}
		journalFile.close();
		scheduler.shutdown();
		syslogger.info("Stop Wession Journal manager");
	}
	public int jounralByNetwork(){
		StopWatch watch = new StopWatch(TimeUnit.SECONDS);
		Collection<ISession> dump_repository = WessionSessionManager.getInstance().getClusterManager().getAll();
		for (ISession session : dump_repository) {
			WessionSessionManager.getInstance().create(session, false);
		}
		syslogger.info("Jurnal {} : {}",dump_repository.size(), watch.stop());
		return dump_repository.size();
	}
	public int jounralByFile(){
		StopWatch watch = new StopWatch(TimeUnit.SECONDS);
		Collection<ISession> dump_repository = this.journalFile.getAll();
		for (ISession session : dump_repository) {
			WessionSessionManager.getInstance().create(session, false);
		}
		syslogger.info("Jurnal {} : {}",dump_repository.size(),  watch.stop());
		return dump_repository.size();
	}
	
	private void dump(){
		this.journalFile.createDumpFile( WessionSessionManager.getInstance().getAll());
	}

	//******************************************************************************************//
	public void setProperty(final String key,final String value) {
		if(!useable){return;}
		
		StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);
		JournalEntity entity = new JournalEntity(SET_PROPERTY);
		entity.addData(key);
		entity.addData(value);
		this.journalFile.append(entity);
		weslogger.debug("setProperty : key[{}], value[{}]-{}",key,value,watch.stop());
	}
	public void create(final ISession session){
		if(!useable){return;}
		
		StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);
		JournalEntity entity = new JournalEntity(CREATE);
		entity.addData(session);
		this.journalFile.append(entity);
		weslogger.debug("create : session[{}]-{}",session,watch.stop());
	}
	public void expire(String key) {
		if(!useable){return;}
		
		StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);
		JournalEntity entity = new JournalEntity(EXPIRE);
		entity.addData(key);
		this.journalFile.append(entity);
		weslogger.debug("expire : key[{}]-{}",key,watch.stop());

	}
	
	public void remove(String key) {
		if(!useable){return;}
		
		StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);
		JournalEntity entity = new JournalEntity(EXPIRE);
		entity.addData(key);
		this.journalFile.append(entity);
		weslogger.debug("remove : key[{}]-{}",key,watch.stop());
	}
	
	public void add(final String parent,final ISession session) {
		if(!useable){return;}
		
		StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);
		JournalEntity entity = new JournalEntity(ADD);
		entity.addData(parent);
		entity.addData(session);
		this.journalFile.append(entity);
		weslogger.debug("add : parent[{}],session[{}]-{}",parent,session,watch.stop());
	}
	public void remove(final String parent,final String key) {
		if(!useable){return;}
		
		StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);
		JournalEntity entity = new JournalEntity(REMOVE);
		entity.addData(parent);
		entity.addData(key);
		this.journalFile.append(entity);
		weslogger.debug("remove : parent[{}],key[{}]-{}",parent,key,watch.stop());
	}
	public void set(String parent, String key, Object object) {
		if(!useable){return;}
		
		StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);
		JournalEntity entity = new JournalEntity(SET);
		entity.addData(parent);
		entity.addData(key);
		entity.addData(object);
		this.journalFile.append(entity);
		weslogger.debug("set : parent[{}],key[{}],object[{}]-{}",parent,key,object,watch.stop());
	}
	public void delete(String parent, String key) {
		if(!useable){return;}
		
		StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);
		JournalEntity entity = new JournalEntity(DEL);
		entity.addData(parent);
		entity.addData(key);
		this.journalFile.append(entity);
		weslogger.debug("delete : parent[{}],key[{}]-{}",parent,key,watch.stop());
	}
	public void clear() {
		if(!useable){return;}
		
		StopWatch  watch = new StopWatch(TimeUnit.NANOSECONDS);
		JournalEntity entity = new JournalEntity(CLEAR);
		this.journalFile.append(entity);
		weslogger.debug("clear:-{}",watch.stop());
	}


}
