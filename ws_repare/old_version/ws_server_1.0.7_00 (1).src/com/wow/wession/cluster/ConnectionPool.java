/*    */ package com.wow.wession.cluster;
/*    */ 
/*    */ import com.wow.client.IConnection;
/*    */ import com.wow.wession.pool.ConnectionHandler;
/*    */ import java.util.Vector;
/*    */ import java.util.concurrent.locks.ReentrantLock;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class ConnectionPool
/*    */ {
/* 15 */   private static Logger syslogger = LoggerFactory.getLogger("system");
/*    */   private final Vector<IConnection> items;
/*    */   private ConnectionHandler handler;
/* 20 */   private int count = 0;
/*    */   private final ReentrantLock lock;
/*    */ 
/*    */   public ConnectionPool(String ip, int port, int capacity)
/*    */   {
/* 26 */     if (capacity <= 0)
/* 27 */       throw new IllegalArgumentException();
/* 28 */     this.items = new Vector(capacity);
/* 29 */     this.lock = new ReentrantLock(false);
/* 30 */     this.handler = new ConnectionHandler(ip, port);
/*    */   }
/*    */ 
/*    */   public boolean release(IConnection con)
/*    */   {
/* 35 */     if (con == null) throw new NullPointerException();
/* 36 */     ReentrantLock lock = this.lock;
/* 37 */     lock.lock();
/*    */     try {
/* 39 */       if (!this.items.add(con)) {
/*    */         try {
/* 41 */           con.close();
/*    */         } catch (Exception e) {
/* 43 */           syslogger.error(e.getMessage());
/*    */         }
/*    */         return false;
/*    */       }
/* 47 */       this.count += 1;
/* 48 */       return true;
/*    */     }
/*    */     finally {
/* 51 */       lock.unlock();
/*    */     }
/*    */   }
/*    */ 
/*    */   public IConnection poll() {
/* 56 */     ReentrantLock lock = this.lock;
/* 57 */     lock.lock();
/*    */     try
/*    */     {
/*    */       IConnection localIConnection;
/* 59 */       if (this.count <= 0) {
/*    */         try {
/* 61 */           localIConnection = this.handler.createConnection();
/*    */ 
/* 71 */           lock.unlock();
/*    */ 
/* 61 */           return localIConnection;
/*    */         } catch (Exception e) {
/* 63 */           syslogger.error(e.getMessage());
/*    */ 
/* 71 */           lock.unlock();
/*    */ 
/* 64 */           return null;
/*    */         }
/*    */       }
/* 67 */       this.count -= 1;
/* 68 */       return (IConnection)this.items.lastElement();
/*    */     }
/*    */     finally {
/* 71 */       lock.unlock();
/*    */     }
/*    */   }
/*    */ 
/*    */   public int size() {
/* 76 */     ReentrantLock lock = this.lock;
/* 77 */     lock.lock();
/*    */     try {
/* 79 */       return this.count;
/*    */     } finally {
/* 81 */       lock.unlock();
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.cluster.ConnectionPool
 * JD-Core Version:    0.5.4
 */