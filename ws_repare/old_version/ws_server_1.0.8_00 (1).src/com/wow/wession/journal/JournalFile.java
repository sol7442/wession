/*     */ package com.wow.wession.journal;
/*     */ 
/*     */ import com.wow.wession.server.WessionServerSession;
/*     */ import com.wow.wession.session.ISession;
/*     */ import java.io.EOFException;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class JournalFile
/*     */ {
/*  33 */   private Logger syslogger = LoggerFactory.getLogger("system");
/*     */   private File A;
/*     */   private File B;
/*     */   private File D;
/*     */   private final String dataPath;
/*     */   private final long rollTime;
/*     */   private final long sessionTime;
/*  43 */   private long fileModifyTime = 0L;
/*     */   private ObjectOutputStream appendstream;
/*  45 */   private Lock appendLock = new ReentrantLock();
/*     */ 
/*     */   public JournalFile(String path, long session_time, long roll_time) {
/*  48 */     this.dataPath = path;
/*  49 */     this.sessionTime = session_time;
/*  50 */     this.rollTime = roll_time;
/*     */   }
/*     */ 
/*     */   public void initialize(String name) {
/*  54 */     createNewPaht();
/*  55 */     this.A = new File(this.dataPath, name + "_A");
/*  56 */     this.B = new File(this.dataPath, name + "_B");
/*  57 */     this.D = new File(this.dataPath, name + "_D");
/*     */   }
/*     */   public void append(JournalEntity entity) {
/*  60 */     this.appendLock.lock();
/*     */     try
/*     */     {
/*  63 */       ObjectOutputStream outstream = getOutputSteam(entity.getTime());
/*  64 */       outstream.writeObject(entity);
/*  65 */       outstream.flush();
/*     */ 
/*  67 */       this.syslogger.debug("Journal Append : {}", entity);
/*     */     }
/*     */     catch (IOException e) {
/*  70 */       this.syslogger.error("Journal File append Error : {}", e);
/*     */     }
/*     */     finally {
/*  73 */       this.appendLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   private ObjectOutputStream getOutputSteam(long time) {
/*  78 */     if ((this.appendstream == null) || (isRollOver(time))) {
/*     */       try {
/*  80 */         this.fileModifyTime = System.currentTimeMillis();
/*  81 */         this.appendstream = new ObjectOutputStream(new FileOutputStream(this.A));
/*     */       } catch (FileNotFoundException e) {
/*  83 */         e.printStackTrace();
/*     */       } catch (IOException e) {
/*  85 */         e.printStackTrace();
/*     */       }
/*     */     }
/*  88 */     return this.appendstream;
/*     */   }
/*     */ 
/*     */   private boolean isRollOver(long time) {
/*  92 */     boolean rollover = false;
/*  93 */     if (time - this.fileModifyTime > this.rollTime) {
/*  94 */       rollover = true;
/*  95 */       this.syslogger.debug("Journal File HandOver {}-{}", new Date(this.fileModifyTime), new Date(time));
/*     */       try {
/*  97 */         this.appendstream.close();
/*  98 */         this.appendstream = null;
/*  99 */         if (this.B.exists()) {
/* 100 */           this.B.delete();
/*     */         }
/* 102 */         this.A.renameTo(this.B);
/*     */       } catch (IOException e) {
/* 104 */         this.syslogger.error("Journal File HandOver Error : {} ", e);
/*     */       }
/*     */     }
/* 107 */     return rollover;
/*     */   }
/*     */   public int createDumpFile(Collection<ISession> all_session) {
/* 110 */     if (this.D.exists())
/* 111 */       this.D.delete();
/*     */     try
/*     */     {
/* 114 */       ObjectOutputStream outstream = new ObjectOutputStream(new FileOutputStream(this.D));
/* 115 */       long alive_time = System.currentTimeMillis() - this.sessionTime;
/* 116 */       for (ISession session : all_session) {
/* 117 */         if (session.getCreateTime() > alive_time) {
/* 118 */           outstream.writeObject(session);
/*     */         }
/*     */       }
/* 121 */       outstream.close();
/*     */     } catch (FileNotFoundException e) {
/* 123 */       e.printStackTrace();
/*     */     } catch (IOException e) {
/* 125 */       e.printStackTrace();
/*     */     }
/* 127 */     return 0;
/*     */   }
/*     */ 
/*     */   private File createNewPaht() {
/* 131 */     File data_path = new File(this.dataPath);
/* 132 */     if ((!data_path.exists()) || (!data_path.isDirectory())) {
/* 133 */       data_path.mkdir();
/* 134 */       this.syslogger.info("make journal path {}", data_path);
/*     */     }
/* 136 */     return data_path;
/*     */   }
/*     */ 
/*     */   public void close() {
/* 140 */     this.appendLock.lock();
/*     */     try {
/* 142 */       this.appendstream.close();
/*     */     } catch (IOException e) {
/* 144 */       this.syslogger.error("Journal File Close Error : {}", e);
/*     */     } finally {
/* 146 */       this.appendLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Collection<ISession> getAll()
/*     */   {
/* 152 */     long alive_time = System.currentTimeMillis() - this.sessionTime;
/*     */ 
/* 154 */     ConcurrentHashMap dump_repository = readDumpFile(alive_time);
/*     */ 
/* 156 */     Set entity_set = new TreeSet(new Comparator() {
/*     */       public int compare(JournalEntity o1, JournalEntity o2) {
/* 158 */         return (o1.getTime() < o2.getTime()) ? -1 : 1;
/*     */       }
/*     */     });
/* 162 */     entity_set.addAll(readJournalEntity(this.B, alive_time));
/* 163 */     entity_set.addAll(readJournalEntity(this.A, alive_time));
/*     */ 
/* 165 */     int after_size = dump_repository.size();
/* 166 */     Iterator entiry_iter = entity_set.iterator();
/* 167 */     while (entiry_iter.hasNext()) {
/* 168 */       JournalEntity entity = (JournalEntity)entiry_iter.next();
/* 169 */       this.syslogger.debug("JournalEntity  : {} ", entity);
/* 170 */       WessionServerSession server_session = null;
/* 171 */       switch (entity.getCommand())
/*     */       {
/*     */       case 1:
/* 173 */         dump_repository.put(((ISession)entity.getData(0)).getID(), (ISession)entity.getData(0));
/* 174 */         break;
/*     */       case 2:
/* 176 */         dump_repository.remove(entity.getData(0));
/* 177 */         break;
/*     */       case 10:
/* 179 */         server_session = (WessionServerSession)dump_repository.get(entity.getData(0));
/* 180 */         if (server_session != null) {
/* 181 */           server_session.addAgentSession((ISession)entity.getData(1), false);
/*     */         }
/* 183 */         break;
/*     */       case 11:
/* 185 */         server_session = (WessionServerSession)dump_repository.get(entity.getData(0));
/* 186 */         if (server_session != null) {
/* 187 */           server_session.removeAgentSession((String)entity.getData(1), false);
/*     */         }
/* 189 */         break;
/*     */       case 21:
/* 191 */         server_session = (WessionServerSession)dump_repository.get(entity.getData(0));
/* 192 */         if (server_session != null) {
/* 193 */           server_session.setAttribute((String)entity.getData(1), entity.getData(2), false);
/*     */         }
/* 195 */         break;
/*     */       case 22:
/* 197 */         server_session = (WessionServerSession)dump_repository.get(entity.getData(0));
/* 198 */         if (server_session != null) {
/* 199 */           server_session.removeAgentSession((String)entity.getData(1), false);
/*     */         }
/* 201 */         break;
/*     */       case 99:
/* 203 */         dump_repository.clear();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 208 */     this.syslogger.info("dump repository size : {} - {}", Integer.valueOf(after_size), Integer.valueOf(dump_repository.size()));
/* 209 */     return dump_repository.values();
/*     */   }
/*     */ 
/*     */   private Set<JournalEntity> readJournalEntity(File read_file, long alive_time)
/*     */   {
/* 214 */     Set entity_set = new HashSet();
/*     */     try {
/* 216 */       ObjectInputStream instream = new ObjectInputStream(new FileInputStream(read_file));
/* 217 */       JournalEntity entity = null;
/*     */       try {
/* 219 */         while ((entity = (JournalEntity)instream.readObject()) != null)
/* 220 */           if (entity.getTime() > alive_time) {
/* 221 */             entity_set.add(entity);
/* 222 */             this.syslogger.debug("read file in-time {} - {}", read_file.getName(), entity);
/*     */           } else {
/* 224 */             this.syslogger.debug("read file out-time {} - {}", read_file.getName(), entity);
/*     */           }
/*     */       }
/*     */       catch (EOFException e) {
/* 228 */         this.syslogger.debug("read file end-line {}", read_file.getName());
/*     */       } finally {
/* 230 */         instream.close();
/*     */       }
/*     */     } catch (FileNotFoundException e) {
/* 233 */       this.syslogger.warn("read error : {} ", read_file.getName());
/*     */     } catch (IOException e) {
/* 235 */       this.syslogger.error("read error : {} - {}", read_file.getName(), e);
/*     */     } catch (ClassNotFoundException e) {
/* 237 */       this.syslogger.error("read error : {} - {}", read_file.getName(), e);
/*     */     }
/* 239 */     return entity_set;
/*     */   }
/*     */ 
/*     */   private ConcurrentHashMap<String, ISession> readDumpFile(long alive_time) {
/* 243 */     ConcurrentHashMap dump_repository = new ConcurrentHashMap();
/*     */ 
/* 245 */     File dump_file = new File(new File(this.dataPath), "D");
/* 246 */     if (dump_file.exists()) {
/*     */       try {
/* 248 */         ObjectInputStream instreadm = new ObjectInputStream(new FileInputStream(dump_file));
/*     */         try {
/* 250 */           ISession session = null;
/* 251 */           while ((session = (ISession)instreadm.readObject()) != null)
/* 252 */             if (session.getCreateTime() > alive_time) {
/* 253 */               dump_repository.put(session.getID(), session);
/* 254 */               this.syslogger.debug("demp file in-time {} - {}", dump_file.getName(), session);
/*     */             } else {
/* 256 */               this.syslogger.debug("demp file out-time {} - {}", dump_file.getName(), session);
/*     */             }
/*     */         }
/*     */         catch (EOFException e) {
/* 260 */           this.syslogger.debug("demp file end-line {}", dump_file.getName());
/*     */         } finally {
/* 262 */           instreadm.close();
/*     */         }
/*     */       } catch (FileNotFoundException e) {
/* 265 */         e.printStackTrace();
/*     */       } catch (IOException e) {
/* 267 */         e.printStackTrace();
/*     */       } catch (ClassNotFoundException e) {
/* 269 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 272 */     return dump_repository;
/*     */   }
/*     */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.journal.JournalFile
 * JD-Core Version:    0.5.4
 */