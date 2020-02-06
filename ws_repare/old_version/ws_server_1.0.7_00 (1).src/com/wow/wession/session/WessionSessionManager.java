/*     */ package com.wow.wession.session;
/*     */ 
/*     */ import com.wow.wession.cluster.ClusterManager;
/*     */ import com.wow.wession.config.SessionManagerConfigDocument.SessionManagerConfig;
/*     */ import com.wow.wession.journal.JournalManager;
/*     */ import com.wow.wession.repository.IManagerState;
/*     */ import com.wow.wession.repository.IRepository;
/*     */ import com.wow.wession.repository.WessionRepository;
/*     */ import com.wow.wession.server.WessionServerSession;
/*     */ import com.wow.wession.util.StopWatch;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class WessionSessionManager
/*     */   implements IRepository, IManagerState
/*     */ {
/*  30 */   private Logger syslogger = LoggerFactory.getLogger("system");
/*  31 */   private Logger weslogger = LoggerFactory.getLogger("wession");
/*     */ 
/*  33 */   private static WessionSessionManager instance = null;
/*  34 */   private WessionRepository repository = null;
/*     */   private JournalManager journal_mgr;
/*     */   private ClusterManager cluster_mgr;
/*  37 */   private Properties mgr_properties = new Properties();
/*     */ 
/*  39 */   private int sessionExpireTime = 1800000;
/*  40 */   private ExecutorService executers = null;
/*     */ 
/*  42 */   private int minThreadSize = 1;
/*  43 */   private int maxThreadSize = 10;
/*  44 */   private int idleMilliTime = 10000;
/*     */ 
/*     */   public static WessionSessionManager getInstance()
/*     */   {
/*  50 */     if (instance == null) {
/*  51 */       instance = new WessionSessionManager();
/*     */     }
/*  53 */     return instance;
/*     */   }
/*     */ 
/*     */   public boolean initialize(String name, SessionManagerConfigDocument.SessionManagerConfig mgrConf) {
/*  57 */     boolean bRes = false;
/*     */     try {
/*  59 */       this.sessionExpireTime = mgrConf.getSessionExipreTime();
/*  60 */       this.minThreadSize = mgrConf.getMinThreadSize();
/*  61 */       this.maxThreadSize = mgrConf.getMaxThreadSize();
/*  62 */       this.idleMilliTime = mgrConf.getIdleMilliTime();
/*  63 */       bRes = true;
/*     */     } catch (Exception e) {
/*  65 */       this.syslogger.error("initialize fail : {}", e);
/*     */     } finally {
/*  67 */       this.executers = new ThreadPoolExecutor(this.maxThreadSize, this.maxThreadSize, this.idleMilliTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), new RepsitoryThreadFactory(null));
/*     */     }
/*     */ 
/*  70 */     return bRes;
/*     */   }
/*     */ 
/*     */   public void setWessionRepository(WessionRepository repository)
/*     */   {
/*  81 */     this.repository = repository;
/*     */   }
/*     */   public WessionRepository getWessionRepository() {
/*  84 */     return this.repository;
/*     */   }
/*     */ 
/*     */   public void setJournalManager(JournalManager j_mgr) {
/*  88 */     this.journal_mgr = j_mgr;
/*     */   }
/*     */   public JournalManager getJournalManager() {
/*  91 */     return this.journal_mgr;
/*     */   }
/*     */   public void setClusterManager(ClusterManager c_mgr) {
/*  94 */     this.cluster_mgr = c_mgr;
/*     */   }
/*     */   public ClusterManager getClusterManager() {
/*  97 */     return this.cluster_mgr;
/*     */   }
/*     */ 
/*     */   public int getSessionTimeout() {
/* 101 */     return this.sessionExpireTime;
/*     */   }
/*     */ 
/*     */   public void start() {
/* 105 */     this.cluster_mgr.start();
/* 106 */     this.journal_mgr.start();
/*     */   }
/*     */   public void stop() {
/* 109 */     this.cluster_mgr.stop();
/* 110 */     this.journal_mgr.stop();
/*     */ 
/* 112 */     this.executers.shutdown();
/* 113 */     this.syslogger.info("Stop Wession SessionManager");
/*     */   }
/*     */ 
/*     */   public int getSize()
/*     */   {
/* 120 */     return this.repository.getSize();
/*     */   }
/*     */ 
/*     */   public ISession get(String key) {
/* 124 */     return this.repository.get(key);
/*     */   }
/*     */   public List<ISession> getBy(String handle, String key) {
/* 127 */     return this.repository.getBy(handle, key);
/*     */   }
/*     */   public Iterator<ISession> iterator() {
/* 130 */     return this.repository.iterator();
/*     */   }
/*     */ 
/*     */   public Collection<ISession> getAll() {
/* 134 */     return this.repository.getAll();
/*     */   }
/*     */ 
/*     */   public void setProperty(String key, String value)
/*     */   {
/* 141 */     setProperty(key, value, true);
/*     */   }
/*     */   public void setProperty(String key, String value, boolean cluster) {
/* 144 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/*     */     try {
/* 146 */       this.mgr_properties.setProperty(key, value);
/* 147 */       this.executers.execute(new Runnable(cluster, key, value, watch)
/*     */       {
/*     */         public void run() {
/* 150 */           if (this.val$cluster) {
/* 151 */             WessionSessionManager.this.cluster_mgr.setProperty(this.val$key, this.val$value);
/*     */           }
/* 153 */           WessionSessionManager.this.weslogger.debug("setProperty : key[{}], value[{}]-{}", new Object[] { this.val$key, this.val$value, Long.valueOf(this.val$watch.stop()) });
/*     */         } } );
/*     */     }
/*     */     catch (Exception e) {
/* 157 */       this.weslogger.error("setProperty : {},{}-{}", new Object[] { key, value, e });
/*     */     }
/*     */   }
/*     */ 
/*     */   public Set<Object> getPropertyKeySet() {
/* 161 */     return this.mgr_properties.keySet();
/*     */   }
/*     */   public String getProperty(String key) {
/* 164 */     return this.mgr_properties.getProperty(key);
/*     */   }
/*     */ 
/*     */   public void create(ISession session)
/*     */   {
/* 172 */     create(session, true);
/*     */   }
/*     */ 
/*     */   public void create(ISession session, boolean cluster) {
/* 176 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/*     */     try {
/* 178 */       this.executers.execute(new Runnable(session, watch, cluster) {
/*     */         public void run() {
/*     */           try {
/* 181 */             WessionSessionManager.this.repository.create(this.val$session);
/* 182 */             WessionSessionManager.this.journal_mgr.create(this.val$session);
/* 183 */             if (!this.val$cluster) break label52;
/* 184 */             label52: WessionSessionManager.this.cluster_mgr.create(this.val$session);
/*     */           }
/*     */           finally {
/* 187 */             WessionSessionManager.this.weslogger.info("create : session[{}]-{}", this.val$session, Long.valueOf(this.val$watch.stop()));
/*     */           }
/*     */         } } );
/*     */     }
/*     */     catch (RuntimeException e) {
/* 192 */       this.weslogger.error("create : {}-{}", session, e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void expire(String key)
/*     */   {
/* 200 */     expire(key, true);
/*     */   }
/*     */   public void expire(String key, boolean cluster) {
/* 203 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/*     */     try {
/* 205 */       this.executers.execute(new Runnable(key, watch, cluster) {
/*     */         public void run() {
/*     */           try {
/* 208 */             WessionSessionManager.this.repository.expire(this.val$key);
/* 209 */             WessionSessionManager.this.journal_mgr.expire(this.val$key);
/* 210 */             if (!this.val$cluster) break label53;
/* 211 */             label53: WessionSessionManager.this.cluster_mgr.expire(this.val$key);
/*     */           }
/*     */           finally {
/* 214 */             WessionSessionManager.this.weslogger.info("expire : key[{}]-{}", this.val$key, Long.valueOf(this.val$watch.stop()));
/*     */           }
/*     */         } } );
/*     */     }
/*     */     catch (RuntimeException e) {
/* 219 */       this.weslogger.error("expire : {}-{}", key, e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void add(String parent, ISession session)
/*     */   {
/* 228 */     WessionServerSession server_session = (WessionServerSession)this.repository.get(parent);
/* 229 */     if (server_session != null)
/* 230 */       server_session.addAgentSession(session, false);
/*     */   }
/*     */ 
/*     */   public void add(String parent, ISession session, boolean cluster) {
/* 234 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/*     */     try {
/* 236 */       this.executers.execute(new Runnable(parent, session, watch, cluster) {
/*     */         public void run() {
/*     */           try {
/* 239 */             WessionSessionManager.this.journal_mgr.add(this.val$parent, this.val$session);
/* 240 */             if (!this.val$cluster) break label46;
/* 241 */             label46: WessionSessionManager.this.cluster_mgr.add(this.val$parent, this.val$session);
/*     */           }
/*     */           finally {
/* 244 */             WessionSessionManager.this.weslogger.info("add : parent[{}],session[{}]-{}", new Object[] { this.val$parent, this.val$session, Long.valueOf(this.val$watch.stop()) });
/*     */           }
/*     */         } } );
/*     */     }
/*     */     catch (RuntimeException e) {
/* 249 */       this.weslogger.error("add : {},{}-{}", new Object[] { parent, session, e });
/*     */     }
/*     */   }
/*     */ 
/*     */   public void remove(String parent, String key)
/*     */   {
/* 257 */     WessionServerSession server_session = (WessionServerSession)this.repository.get(parent);
/* 258 */     if (server_session != null)
/* 259 */       server_session.removeAgentSession(key, false);
/*     */   }
/*     */ 
/*     */   public void remove(String parent, String key, boolean cluster) {
/* 263 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/*     */     try {
/* 265 */       this.executers.execute(new Runnable(parent, key, watch, cluster) {
/*     */         public void run() {
/*     */           try {
/* 268 */             WessionSessionManager.this.journal_mgr.remove(this.val$parent, this.val$key);
/* 269 */             if (!this.val$cluster) break label46;
/* 270 */             label46: WessionSessionManager.this.cluster_mgr.remove(this.val$parent, this.val$key);
/*     */           }
/*     */           finally {
/* 273 */             WessionSessionManager.this.weslogger.info("remove : parent[{}],key[{}]-{}", new Object[] { this.val$parent, this.val$key, Long.valueOf(this.val$watch.stop()) });
/*     */           }
/*     */         } } );
/*     */     }
/*     */     catch (RuntimeException e) {
/* 278 */       this.weslogger.error("remove : {},{}-{}", new Object[] { parent, key, e });
/*     */     }
/*     */   }
/*     */ 
/*     */   public void set(String parent, String key, Object object)
/*     */   {
/* 286 */     WessionServerSession server_session = (WessionServerSession)this.repository.get(parent);
/* 287 */     if (server_session != null)
/* 288 */       server_session.setAttribute(key, object, false);
/*     */   }
/*     */ 
/*     */   public void set(String parent, String key, Object object, boolean cluster) {
/* 292 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/*     */     try {
/* 294 */       this.executers.execute(new Runnable(parent, key, object, watch, cluster) {
/*     */         public void run() {
/*     */           try {
/* 297 */             WessionSessionManager.this.journal_mgr.set(this.val$parent, this.val$key, this.val$object);
/* 298 */             if (!this.val$cluster) break label54;
/* 299 */             label54: WessionSessionManager.this.cluster_mgr.set(this.val$parent, this.val$key, this.val$object);
/*     */           }
/*     */           finally {
/* 302 */             WessionSessionManager.this.weslogger.info("set : parent[{}],key[{}],object[{}]-{}", new Object[] { this.val$parent, this.val$key, this.val$object, Long.valueOf(this.val$watch.stop()) });
/*     */           }
/*     */         } } );
/*     */     }
/*     */     catch (RuntimeException e) {
/* 307 */       this.weslogger.error("set : {},{},{}-{}", new Object[] { parent, key, object, e });
/*     */     }
/*     */   }
/*     */ 
/*     */   public void delete(String parent, String key)
/*     */   {
/* 316 */     WessionServerSession server_session = (WessionServerSession)this.repository.get(parent);
/* 317 */     if (server_session != null)
/* 318 */       server_session.removeAttribute(key, false);
/*     */   }
/*     */ 
/*     */   public void delete(String parent, String key, boolean cluster) {
/* 322 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/*     */     try {
/* 324 */       this.executers.execute(new Runnable(parent, key, watch, cluster) {
/*     */         public void run() {
/*     */           try {
/* 327 */             WessionSessionManager.this.journal_mgr.delete(this.val$parent, this.val$key);
/* 328 */             if (!this.val$cluster) break label46;
/* 329 */             label46: WessionSessionManager.this.cluster_mgr.delete(this.val$parent, this.val$key);
/*     */           }
/*     */           finally {
/* 332 */             WessionSessionManager.this.weslogger.info("delete : parent[{}],key[{}]-{}", new Object[] { this.val$parent, this.val$key, Long.valueOf(this.val$watch.stop()) });
/*     */           }
/*     */         } } );
/*     */     }
/*     */     catch (RuntimeException e) {
/* 337 */       this.weslogger.error("set : {},{}-{}", new Object[] { parent, key, e });
/*     */     }
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 346 */     clear(true);
/*     */   }
/*     */   public void clear(boolean cluster) {
/* 349 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/*     */     try {
/* 351 */       this.executers.execute(new Runnable(watch, cluster) {
/*     */         public void run() {
/*     */           try {
/* 354 */             WessionSessionManager.this.repository.clear();
/* 355 */             WessionSessionManager.this.journal_mgr.clear();
/* 356 */             if (!this.val$cluster) break label40;
/* 357 */             label40: WessionSessionManager.this.cluster_mgr.clear();
/*     */           }
/*     */           finally {
/* 360 */             WessionSessionManager.this.weslogger.info("clear:-{}", Long.valueOf(this.val$watch.stop()));
/*     */           }
/*     */         } } );
/*     */     }
/*     */     catch (RuntimeException e) {
/* 365 */       this.weslogger.error("clear : -{}", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class RepsitoryThreadFactory
/*     */     implements ThreadFactory
/*     */   {
/*     */     private RepsitoryThreadFactory()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Thread newThread(Runnable r)
/*     */     {
/*  75 */       Thread thread = new Thread(r);
/*  76 */       thread.setName("Repository Worker Thread");
/*  77 */       return thread;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.session.WessionSessionManager
 * JD-Core Version:    0.5.4
 */