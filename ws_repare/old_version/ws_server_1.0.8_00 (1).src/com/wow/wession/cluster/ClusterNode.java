/*    */ package com.wow.wession.cluster;
/*    */ 
/*    */ import com.wow.client.IConnection;
/*    */ import com.wow.wession.pool.BasicConnectionPool;
/*    */ import com.wow.wession.service.impl.WessionRequest;
/*    */ import com.wow.wession.service.impl.WessionResponse;
/*    */ import com.wow.wession.util.StopWatch;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class ClusterNode
/*    */ {
/*    */   public static final int STATE_CREATE = 0;
/*    */   public static final int STATE_REGISTER = 1;
/*    */   public static final int STATE_SYN = 2;
/*    */   public static final int STATE_JOURNA = 3;
/*    */   public static final int STATE_EXIT = 4;
/* 25 */   private static Logger syslogger = LoggerFactory.getLogger("system");
/*    */   private String receiverName;
/*    */   private String senderName;
/* 30 */   private AtomicInteger state = new AtomicInteger(0);
/*    */   private BasicConnectionPool connection_pool;
/*    */ 
/*    */   public void initialize(String sender, String receiver, String ip, int port)
/*    */   {
/* 33 */     this.senderName = sender;
/* 34 */     this.receiverName = receiver;
/* 35 */     this.connection_pool = new BasicConnectionPool(ip, port);
/*    */   }
/*    */   public String getReceiverName() {
/* 38 */     return this.receiverName;
/*    */   }
/*    */ 
/*    */   public void setState(int state) {
/* 42 */     this.state.set(state);
/*    */   }
/*    */   public int getState() {
/* 45 */     return this.state.get();
/*    */   }
/*    */ 
/*    */   public void close() {
/* 49 */     this.connection_pool.destroy();
/* 50 */     syslogger.info("{} Cluster Node Closed", this.receiverName);
/*    */   }
/*    */ 
/*    */   public WessionResponse send(ClusterEntity entity) {
/* 54 */     WessionRequest request = new WessionRequest(this.senderName, this.receiverName);
/* 55 */     WessionResponse response = null;
/*    */ 
/* 57 */     StopWatch watch = new StopWatch(TimeUnit.MICROSECONDS);
/* 58 */     IConnection conn = null;
/*    */     try {
/* 60 */       request.setData(entity);
/* 61 */       conn = this.connection_pool.get();
/* 62 */       if (conn == null) break label122;
/* 63 */       conn.writeRequest(request);
/* 64 */       response = (WessionResponse)conn.readResponse();
/* 65 */       if (response == null) break label122;
/* 66 */       label122: syslogger.debug("Cluster Send {} : {} - {} - {}", new Object[] { this.receiverName, entity, Integer.valueOf(response.getResultCode()), Long.valueOf(watch.stop()) });
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 70 */       syslogger.error("Cluster Node Send Error : {}", e);
/*    */     } finally {
/* 72 */       if (conn != null) {
/* 73 */         this.connection_pool.release(conn);
/*    */       }
/*    */     }
/* 76 */     return response;
/*    */   }
/*    */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.cluster.ClusterNode
 * JD-Core Version:    0.5.4
 */