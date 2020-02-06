package com.wow.wession;


import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wow.wession.cluster.ClusterManager;
import com.wow.wession.config.ClusterManagerConfigDocument.ClusterManagerConfig;
import com.wow.wession.config.ClusterServerConfigDocument.ClusterServerConfig;
import com.wow.wession.config.JournalManagerConfigDocument.JournalManagerConfig;
import com.wow.wession.config.SessionManagerConfigDocument.SessionManagerConfig;
import com.wow.wession.config.WSConfigurationManager;
import com.wow.wession.journal.JournalManager;
import com.wow.wession.log.IWessionLogger;
import com.wow.wession.repository.WessionRepository;
import com.wow.wession.service.ClusterService;
import com.wow.wession.service.ManagerService;
import com.wow.wession.service.SessionService;
import com.wow.wession.session.WessionSessionManager;
import com.wow.wession.util.Time;

public class WessionDaemon {
	private static Logger syslogger;

	public final String name;
	public static final String SYS_INSTANCE = "WS_INS";
	public static final String SYS_LOGPATH  = "WS_LOG";


	private ClusterServer c_srv;
	private ManagerServer m_srv;
	private SessionServer s_srv;
	private ExecutorService excutor;

	/**
	 * Wession Main Daemon
	 * @param
	 */
	public static void main(String[] args) {
		System.out.println(System.getProperty(SYS_INSTANCE));
		System.out.println(System.getProperty(SYS_LOGPATH));


	}

	public WessionDaemon(String instance_name, String logfile_path){
		this.name = instance_name;
		System.setProperty(SYS_INSTANCE, instance_name);
		System.setProperty(SYS_LOGPATH, logfile_path);
	}


	/**
	 * @param conf_path
	 * @param log_path
	 */
	public boolean initialize(String ws_config_file,String log4j_config_file){
		setLoggerConfigure(log4j_config_file);

		printStartLog(true);
		if(loadConfigure(ws_config_file) == false){
			syslogger.error("Wession Confuration load fail : {}", ws_config_file);
			return false;
		}

		if(initializeServer() == false){
			syslogger.error("Wession Server Open fail : {}", ws_config_file);
			return false;
		}
		
		if(initializeComponent() == false){
			syslogger.error("Wession Component Initialize fail : {}", ws_config_file);
			return false;
		}
		
		return true;
	}


	private boolean initializeComponent(){
		WessionSessionManager s_mgr = WessionSessionManager.getInstance();
		JournalManager j_mgr = new JournalManager();
		ClusterManager c_mgr = new ClusterManager();
		WessionRepository repository = new WessionRepository();
		
		SessionManagerConfig s_mgr_conf = WSConfigurationManager.getInstance().get().getSessionManagerConfig();
		ClusterServerConfig c_srv_config = WSConfigurationManager.getInstance().get().getClusterServerConfig();
		
		JournalManagerConfig j_mgr_conf = s_mgr_conf.getJournalManagerConfig();
		ClusterManagerConfig c_mgr_conf = s_mgr_conf.getClusterManagerConfig();
			
		if(s_mgr.initialize(this.name,s_mgr_conf) == false){
			syslogger.info("SessionManager.initialize fail: \n{}",WSConfigurationManager.getInstance().getText(s_mgr_conf));
		}
		
		if(j_mgr.initialize(this.name,s_mgr_conf.getSessionExipreTime(),j_mgr_conf) == false){
			syslogger.info("JournalManager.initialize fail: \n{}",WSConfigurationManager.getInstance().getText(j_mgr_conf));
		}
		
		if(c_mgr.initialize(this.name,c_mgr_conf,c_srv_config) == false){
			syslogger.info("ClusterManager.initialize fail: \n{} \n{}",WSConfigurationManager.getInstance().getText(c_mgr_conf),WSConfigurationManager.getInstance().getText(c_srv_config));
		}
		
		
		s_mgr.setWessionRepository(repository);
		s_mgr.setJournalManager(j_mgr);
		s_mgr.setClusterManager(c_mgr);

		return true;
	}
	private boolean initializeServer(){
		c_srv 	= new ClusterServer("Wession Cluster Server");
		m_srv = new ManagerServer("Wession Manager Server");
		s_srv 	= new SessionServer("Wession Session Server");

		syslogger.info("Wession Service initialize ****************");
		syslogger.info("{} initializeServer : {}"  , c_srv.getName(), c_srv.initialize(new ClusterService()));
		syslogger.info("{} initializeServer : {}"  , m_srv.getName(), m_srv.initialize(new ManagerService()));
		syslogger.info("{} initializeServer : {}"  , s_srv.getName(), s_srv.initialize(new SessionService()));

		excutor = new ThreadPoolExecutor(3,3, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),new ServiceThreadFactory());
		return true;
	}

	private class ServiceThreadFactory implements ThreadFactory{
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r);
			thread.setName("Wession Service");
			return thread;
		}
	}

	private boolean loadConfigure(String path) {
		WSConfigurationManager conf_mgr = WSConfigurationManager.getInstance();
		return conf_mgr.load(path);
	}


	private boolean setLoggerConfigure(String path) {
		DOMConfigurator.configureAndWatch(path);
		syslogger = LoggerFactory.getLogger(IWessionLogger.SYSTEM_LOGGER);
        return true;
	}

	public void start(){
		WessionSessionManager s_mgr = WessionSessionManager.getInstance();
		s_mgr.start();

		excutor.execute(this.c_srv);
		printStartLog(false);

	}
	public void stop(){
		try {
			printStopLog(true);
			this.c_srv.close();
			syslogger.info("{} Service closed",this.c_srv.getName());

			WessionSessionManager.getInstance().stop();
			excutor.shutdown();
			syslogger.info("Wession Daemon Stoped");
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			printStopLog(false);
		}
	}


	private static void printStartLog(boolean before) {
		syslogger.info("***********************************************************************");
		if(before){
			syslogger.info("System Start ["+ System.getProperty("WS_INS") +"] : " + Time.getTime() );
			syslogger.info("***********************************************************************");
		}
	}

	private static void printStopLog(boolean before) {
		syslogger.info("***********************************************************************");
		if(before){
			syslogger.info("System Stop ["+ System.getProperty("WS_INS") +"] : " + Time.getTime() );
			syslogger.info("***********************************************************************");
		}
	}

}
