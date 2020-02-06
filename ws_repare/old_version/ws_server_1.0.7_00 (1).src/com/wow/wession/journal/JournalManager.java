/*     */ package com.wow.wession.journal;
/*     */ 
/*     */ import com.wow.wession.cluster.ClusterManager;
/*     */ import com.wow.wession.config.JournalManagerConfigDocument.JournalManagerConfig;
/*     */ import com.wow.wession.repository.IManagerState;
/*     */ import com.wow.wession.repository.IRepository;
/*     */ import com.wow.wession.session.ISession;
/*     */ import com.wow.wession.session.WessionSessionManager;
/*     */ import com.wow.wession.util.StopWatch;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class JournalManager
/*     */   implements IRepository, IManagerState
/*     */ {
/*  24 */   private Logger syslogger = LoggerFactory.getLogger("system");
/*  25 */   private Logger weslogger = LoggerFactory.getLogger("wession");
/*     */   private JournalFile journalFile;
/*     */   private ScheduledExecutorService scheduler;
/*  32 */   private int backPeriodTime = 600000;
/*  33 */   private int backDelayTime = 60000;
/*  34 */   private boolean useable = false;
/*     */ 
/*     */   public boolean initialize(String instance_name, int expire_time, JournalManagerConfigDocument.JournalManagerConfig mgrConf)
/*     */   {
/*  38 */     String data_path = "./data";
/*     */     try {
/*  40 */       this.backPeriodTime = mgrConf.getBackupPeriodTime();
/*  41 */       this.backDelayTime = mgrConf.getBackupDelayTime();
/*  42 */       data_path = mgrConf.getDataDirectory();
/*  43 */       this.useable = mgrConf.getUseable();
/*     */     } catch (Exception e) {
/*  45 */       this.syslogger.error("initialize fail {}", e);
/*     */     } finally {
/*  47 */       if (this.useable) {
/*  48 */         this.journalFile = new JournalFile(data_path, expire_time, this.backPeriodTime);
/*  49 */         this.journalFile.initialize(instance_name);
/*     */ 
/*  51 */         this.scheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
/*     */           public Thread newThread(Runnable r) {
/*  53 */             return new Thread(r, "Journal Dump Thread");
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/*  58 */     return this.useable;
/*     */   }
/*     */ 
/*     */   public void start() {
/*  62 */     if (!this.useable) return;
/*     */ 
/*  64 */     this.syslogger.info("Start Wession Journal manager");
/*     */ 
/*  66 */     this.syslogger.info("journal start ------------------------>");
/*  67 */     if (jounralByNetwork() == 0) {
/*  68 */       jounralByFile();
/*     */     }
/*  70 */     this.syslogger.info("journal end  ------------------------>");
/*     */ 
/*  73 */     this.scheduler.scheduleAtFixedRate(new Runnable() {
/*     */       public void run() {
/*  75 */         JournalManager.this.dump();
/*     */       }
/*     */     }
/*     */     , this.backDelayTime, this.backPeriodTime, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */   public void stop()
/*     */   {
/*  82 */     if (!this.useable) return;
/*  83 */     this.journalFile.close();
/*  84 */     this.scheduler.shutdown();
/*  85 */     this.syslogger.info("Stop Wession Journal manager");
/*     */   }
/*     */   public int jounralByNetwork() {
/*  88 */     StopWatch watch = new StopWatch(TimeUnit.SECONDS);
/*  89 */     Collection dump_repository = WessionSessionManager.getInstance().getClusterManager().getAll();
/*  90 */     for (ISession session : dump_repository) {
/*  91 */       WessionSessionManager.getInstance().create(session, false);
/*     */     }
/*  93 */     this.syslogger.info("Jurnal {} : {}", Integer.valueOf(dump_repository.size()), Long.valueOf(watch.stop()));
/*  94 */     return dump_repository.size();
/*     */   }
/*     */   public int jounralByFile() {
/*  97 */     StopWatch watch = new StopWatch(TimeUnit.SECONDS);
/*  98 */     Collection dump_repository = this.journalFile.getAll();
/*  99 */     for (ISession session : dump_repository) {
/* 100 */       WessionSessionManager.getInstance().create(session, false);
/*     */     }
/* 102 */     this.syslogger.info("Jurnal {} : {}", Integer.valueOf(dump_repository.size()), Long.valueOf(watch.stop()));
/* 103 */     return dump_repository.size();
/*     */   }
/*     */ 
/*     */   private void dump() {
/* 107 */     this.journalFile.createDumpFile(WessionSessionManager.getInstance().getAll());
/*     */   }
/*     */ 
/*     */   public void setProperty(String key, String value)
/*     */   {
/* 112 */     if (!this.useable) return;
/*     */ 
/* 114 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/* 115 */     JournalEntity entity = new JournalEntity(104);
/* 116 */     entity.addData(key);
/* 117 */     entity.addData(value);
/* 118 */     this.journalFile.append(entity);
/* 119 */     this.weslogger.debug("setProperty : key[{}], value[{}]-{}", new Object[] { key, value, Long.valueOf(watch.stop()) });
/*     */   }
/*     */   public void create(ISession session) {
/* 122 */     if (!this.useable) return;
/*     */ 
/* 124 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/* 125 */     JournalEntity entity = new JournalEntity(1);
/* 126 */     entity.addData(session);
/* 127 */     this.journalFile.append(entity);
/* 128 */     this.weslogger.debug("create : session[{}]-{}", session, Long.valueOf(watch.stop()));
/*     */   }
/*     */   public void expire(String key) {
/* 131 */     if (!this.useable) return;
/*     */ 
/* 133 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/* 134 */     JournalEntity entity = new JournalEntity(2);
/* 135 */     entity.addData(key);
/* 136 */     this.journalFile.append(entity);
/* 137 */     this.weslogger.debug("expire : key[{}]-{}", key, Long.valueOf(watch.stop()));
/*     */   }
/*     */ 
/*     */   public void add(String parent, ISession session) {
/* 141 */     if (!this.useable) return;
/*     */ 
/* 143 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/* 144 */     JournalEntity entity = new JournalEntity(10);
/* 145 */     entity.addData(parent);
/* 146 */     entity.addData(session);
/* 147 */     this.journalFile.append(entity);
/* 148 */     this.weslogger.debug("add : parent[{}],session[{}]-{}", new Object[] { parent, session, Long.valueOf(watch.stop()) });
/*     */   }
/*     */   public void remove(String parent, String key) {
/* 151 */     if (!this.useable) return;
/*     */ 
/* 153 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/* 154 */     JournalEntity entity = new JournalEntity(11);
/* 155 */     entity.addData(parent);
/* 156 */     entity.addData(key);
/* 157 */     this.journalFile.append(entity);
/* 158 */     this.weslogger.debug("remove : parent[{}],key[{}]-{}", new Object[] { parent, key, Long.valueOf(watch.stop()) });
/*     */   }
/*     */   public void set(String parent, String key, Object object) {
/* 161 */     if (!this.useable) return;
/*     */ 
/* 163 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/* 164 */     JournalEntity entity = new JournalEntity(21);
/* 165 */     entity.addData(parent);
/* 166 */     entity.addData(key);
/* 167 */     entity.addData(object);
/* 168 */     this.journalFile.append(entity);
/* 169 */     this.weslogger.debug("set : parent[{}],key[{}],object[{}]-{}", new Object[] { parent, key, object, Long.valueOf(watch.stop()) });
/*     */   }
/*     */   public void delete(String parent, String key) {
/* 172 */     if (!this.useable) return;
/*     */ 
/* 174 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/* 175 */     JournalEntity entity = new JournalEntity(22);
/* 176 */     entity.addData(parent);
/* 177 */     entity.addData(key);
/* 178 */     this.journalFile.append(entity);
/* 179 */     this.weslogger.debug("delete : parent[{}],key[{}]-{}", new Object[] { parent, key, Long.valueOf(watch.stop()) });
/*     */   }
/*     */   public void clear() {
/* 182 */     if (!this.useable) return;
/*     */ 
/* 184 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/* 185 */     JournalEntity entity = new JournalEntity(99);
/* 186 */     this.journalFile.append(entity);
/* 187 */     this.weslogger.debug("clear:-{}", Long.valueOf(watch.stop()));
/*     */   }
/*     */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.journal.JournalManager
 * JD-Core Version:    0.5.4
 */