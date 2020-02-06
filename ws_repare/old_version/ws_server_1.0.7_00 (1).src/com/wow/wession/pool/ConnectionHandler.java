/*    */ package com.wow.wession.pool;
/*    */ 
/*    */ import com.wow.client.IConnection;
/*    */ import com.wow.client.socket.SocketConnection;
/*    */ import java.io.IOException;
/*    */ import java.net.UnknownHostException;
/*    */ 
/*    */ public class ConnectionHandler
/*    */ {
/*    */   private String ip;
/*    */   private int port;
/*    */ 
/*    */   public ConnectionHandler(String ip, int port)
/*    */   {
/* 13 */     setIp(ip);
/* 14 */     setPort(port);
/*    */   }
/*    */ 
/*    */   public void setIp(String ip) {
/* 18 */     this.ip = ip;
/*    */   }
/*    */ 
/*    */   public void setPort(int port) {
/* 22 */     this.port = port;
/*    */   }
/*    */ 
/*    */   public IConnection createConnection() throws UnknownHostException, IOException {
/* 26 */     IConnection conn = null;
/* 27 */     conn = new SocketConnection(this.ip, this.port);
/* 28 */     return conn;
/*    */   }
/*    */   public String toString() {
/* 31 */     StringBuffer buffer = new StringBuffer();
/* 32 */     buffer.append("Connection : ").append(this.ip).append(",").append(this.port);
/* 33 */     return buffer.toString();
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.pool.ConnectionHandler
 * JD-Core Version:    0.5.4
 */