/*     */ package com.wow.wession;
/*     */ 
/*     */ import com.wow.wession.cluster.ClusterManager;
/*     */ import com.wow.wession.config.ClusterManagerConfigDocument.ClusterManagerConfig;
/*     */ import com.wow.wession.config.ClusterServerConfigDocument.ClusterServerConfig;
/*     */ import com.wow.wession.config.JournalManagerConfigDocument.JournalManagerConfig;
/*     */ import com.wow.wession.config.SessionManagerConfigDocument.SessionManagerConfig;
/*     */ import com.wow.wession.config.WSConfigurationManager;
/*     */ import com.wow.wession.config.WessionConfigurationDocument.WessionConfiguration;
/*     */ import com.wow.wession.journal.JournalManager;
/*     */ import com.wow.wession.repository.WessionRepository;
/*     */ import com.wow.wession.service.ClusterService;
/*     */ import com.wow.wession.service.ManagerService;
/*     */ import com.wow.wession.service.SessionService;
/*     */ import com.wow.wession.session.WessionSessionManager;
/*     */ import com.wow.wession.util.Time;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.log4j.xml.DOMConfigurator;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class WessionDaemon
/*     */ {
/*     */   private static Logger syslogger;
/*     */   public final String name;
/*     */   public static final String SYS_INSTANCE = "WS_INS";
/*     */   public static final String SYS_LOGPATH = "WS_LOG";
/*     */   private ClusterServer c_srv;
/*     */   private ManagerServer m_srv;
/*     */   private SessionServer s_srv;
/*     */   private ExecutorService excutor;
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  48 */     System.out.println(System.getProperty("WS_INS"));
/*  49 */     System.out.println(System.getProperty("WS_LOG"));
/*     */   }
/*     */ 
/*     */   public WessionDaemon(String instance_name, String logfile_path)
/*     */   {
/*  55 */     this.name = instance_name;
/*  56 */     System.setProperty("WS_INS", instance_name);
/*  57 */     System.setProperty("WS_LOG", logfile_path);
/*     */   }
/*     */ 
/*     */   public boolean initialize(String ws_config_file, String log4j_config_file)
/*     */   {
/*  66 */     setLoggerConfigure(log4j_config_file);
/*     */ 
/*  68 */     printStartLog(true);
/*  69 */     if (!loadConfigure(ws_config_file)) {
/*  70 */       syslogger.error("Wession Confuration load fail : {}", ws_config_file);
/*  71 */       return false;
/*     */     }
/*     */ 
/*  74 */     if (!initializeServer()) {
/*  75 */       syslogger.error("Wession Server Open fail : {}", ws_config_file);
/*  76 */       return false;
/*     */     }
/*     */ 
/*  79 */     if (!initializeComponent()) {
/*  80 */       syslogger.error("Wession Component Initialize fail : {}", ws_config_file);
/*  81 */       return false;
/*     */     }
/*     */ 
/*  84 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean initializeComponent()
/*     */   {
/*  89 */     WessionSessionManager s_mgr = WessionSessionManager.getInstance();
/*  90 */     JournalManager j_mgr = new JournalManager();
/*  91 */     ClusterManager c_mgr = new ClusterManager();
/*  92 */     WessionRepository repository = new WessionRepository();
/*     */ 
/*  94 */     SessionManagerConfigDocument.SessionManagerConfig s_mgr_conf = WSConfigurationManager.getInstance().get().getSessionManagerConfig();
/*  95 */     ClusterServerConfigDocument.ClusterServerConfig c_srv_config = WSConfigurationManager.getInstance().get().getClusterServerConfig();
/*     */ 
/*  97 */     JournalManagerConfigDocument.JournalManagerConfig j_mgr_conf = s_mgr_conf.getJournalManagerConfig();
/*  98 */     ClusterManagerConfigDocument.ClusterManagerConfig c_mgr_conf = s_mgr_conf.getClusterManagerConfig();
/*     */ 
/* 100 */     if (!s_mgr.initialize(this.name, s_mgr_conf)) {
/* 101 */       syslogger.info("SessionManager.initialize fail: \n{}", WSConfigurationManager.getInstance().getText(s_mgr_conf));
/*     */     }
/*     */ 
/* 104 */     if (!j_mgr.initialize(this.name, s_mgr_conf.getSessionExipreTime(), j_mgr_conf)) {
/* 105 */       syslogger.info("JournalManager.initialize fail: \n{}", WSConfigurationManager.getInstance().getText(j_mgr_conf));
/*     */     }
/*     */ 
/* 108 */     if (!c_mgr.initialize(this.name, c_mgr_conf, c_srv_config)) {
/* 109 */       syslogger.info("ClusterManager.initialize fail: \n{} \n{}", WSConfigurationManager.getInstance().getText(c_mgr_conf), WSConfigurationManager.getInstance().getText(c_srv_config));
/*     */     }
/*     */ 
/* 113 */     s_mgr.setWessionRepository(repository);
/* 114 */     s_mgr.setJournalManager(j_mgr);
/* 115 */     s_mgr.setClusterManager(c_mgr);
/*     */ 
/* 117 */     return true;
/*     */   }
/*     */   private boolean initializeServer() {
/* 120 */     this.c_srv = new ClusterServer("Wession Cluster Server");
/* 121 */     this.m_srv = new ManagerServer("Wession Manager Server");
/* 122 */     this.s_srv = new SessionServer("Wession Session Server");
/*     */ 
/* 124 */     syslogger.info("Wession Service initialize ****************");
/* 125 */     syslogger.info("{} initializeServer : {}", this.c_srv.getName(), Boolean.valueOf(this.c_srv.initialize(new ClusterService())));
/* 126 */     syslogger.info("{} initializeServer : {}", this.m_srv.getName(), Boolean.valueOf(this.m_srv.initialize(new ManagerService())));
/* 127 */     syslogger.info("{} initializeServer : {}", this.s_srv.getName(), Boolean.valueOf(this.s_srv.initialize(new SessionService())));
/*     */ 
/* 129 */     this.excutor = new ThreadPoolExecutor(3, 3, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), new ServiceThreadFactory(null));
/* 130 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean loadConfigure(String path)
/*     */   {
/* 142 */     WSConfigurationManager conf_mgr = WSConfigurationManager.getInstance();
/* 143 */     return conf_mgr.load(path);
/*     */   }
/*     */ 
/*     */   private boolean setLoggerConfigure(String path)
/*     */   {
/* 148 */     DOMConfigurator.configureAndWatch(path);
/* 149 */     syslogger = LoggerFactory.getLogger("system");
/* 150 */     return true;
/*     */   }
/*     */ 
/*     */   public void start() {
/* 154 */     WessionSessionManager s_mgr = WessionSessionManager.getInstance();
/* 155 */     s_mgr.start();
/*     */ 
/* 157 */     this.excutor.execute(this.c_srv);
/* 158 */     printStartLog(false);
/*     */   }
/*     */ 
/*     */   public void stop() {
/*     */     try {
/* 163 */       printStopLog(true);
/* 164 */       this.c_srv.close();
/* 165 */       syslogger.info("{} Service closed", this.c_srv.getName());
/*     */ 
/* 167 */       WessionSessionManager.getInstance().stop();
/* 168 */       this.excutor.shutdown();
/* 169 */       syslogger.info("Wession Daemon Stoped");
/*     */     } catch (IOException e) {
/* 171 */       e.printStackTrace();
/*     */     } finally {
/* 173 */       printStopLog(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void printStartLog(boolean before)
/*     */   {
/* 179 */     syslogger.info("***********************************************************************");
/* 180 */     if (before) {
/* 181 */       syslogger.info("System Start [" + System.getProperty("WS_INS") + "] : " + Time.getTime());
/* 182 */       syslogger.info("***********************************************************************");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void printStopLog(boolean before) {
/* 187 */     syslogger.info("***********************************************************************");
/* 188 */     if (before) {
/* 189 */       syslogger.info("System Stop [" + System.getProperty("WS_INS") + "] : " + Time.getTime());
/* 190 */       syslogger.info("***********************************************************************");
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ServiceThreadFactory
/*     */     implements ThreadFactory
/*     */   {
/*     */     private ServiceThreadFactory()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Thread newThread(Runnable r)
/*     */     {
/* 135 */       Thread thread = new Thread(r);
/* 136 */       thread.setName("Wession Service");
/* 137 */       return thread;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.WessionDaemon
 * JD-Core Version:    0.5.4
 */