/*    */ package com.wow.wession.pool;
/*    */ 
/*    */ import com.wow.client.IConnection;
/*    */ import java.io.IOException;
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class BasicConnectionPool extends AbstractConnectionPool
/*    */ {
/* 13 */   private Logger syslogger = LoggerFactory.getLogger("system");
/*    */   private ConnectionHandler handler;
/*    */ 
/*    */   public BasicConnectionPool(String ip, int port)
/*    */   {
/* 17 */     this.handler = new ConnectionHandler(ip, port);
/*    */   }
/*    */   public void setHandler(ConnectionHandler handler) {
/* 20 */     this.handler = handler;
/*    */   }
/*    */   public ConnectionHandler getHandler() {
/* 23 */     return this.handler;
/*    */   }
/*    */   public IConnection get() throws IOException {
/* 26 */     IConnection conn = (IConnection)this.queue.poll();
/* 27 */     if ((conn == null) && (this.handler != null)) {
/* 28 */       conn = this.handler.createConnection();
/* 29 */       this.syslogger.info("new Conneciton : {}", conn);
/*    */     }
/* 31 */     return conn;
/*    */   }
/*    */ 
/*    */   public void release(IConnection conn) {
/* 35 */     if ((conn != null) && (conn.isConnected()))
/* 36 */       this.queue.offer(conn);
/*    */   }
/*    */ 
/*    */   public void destroy() {
/* 40 */     this.handler = null;
/* 41 */     IConnection con = null;
/* 42 */     while ((con = (IConnection)this.queue.poll()) != null) {
/*    */       try {
/* 44 */         if (con.isConnected()) {
/* 45 */           con.close();
/* 46 */           this.syslogger.info("close Conneciton : {}", con);
/*    */         }
/*    */       } catch (IOException e) {
/* 49 */         this.syslogger.info("new Conneciton : {}", e.getMessage());
/*    */       }
/*    */     }
/* 52 */     this.syslogger.info("destroy connenction pool");
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.wession.pool.BasicConnectionPool
 * JD-Core Version:    0.5.4
 */