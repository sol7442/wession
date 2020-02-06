/*    */ package com.wow.server.socket;
/*    */ 
/*    */ import com.wow.server.IAcceptor;
/*    */ import com.wow.server.IServer;
/*    */ import com.wow.server.IServiceHandler;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public abstract class SocketServer
/*    */   implements IServer
/*    */ {
/*    */   private String name;
/*    */   private IAcceptor acceptor;
/*    */   private IServiceHandler handler;
/*    */ 
/*    */   public String getName()
/*    */   {
/* 17 */     return this.name;
/*    */   }
/*    */   public void setName(String name) {
/* 20 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public SocketServer(String name) {
/* 24 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public IServiceHandler getHandler() {
/* 28 */     return this.handler;
/*    */   }
/*    */ 
/*    */   public void run() {
/*    */     try {
/* 33 */       this.acceptor.listen();
/*    */     } catch (IOException e) {
/* 35 */       System.out.println("Socket Acceptor Close : " + e.getMessage());
/*    */     } finally {
/* 37 */       System.out.println("SocketServer Listener Closed");
/*    */     }
/* 39 */     System.out.println("SocketServer Closed");
/*    */   }
/*    */   public void close() throws IOException {
/* 42 */     this.handler.close();
/*    */   }
/*    */ 
/*    */   public boolean initialize(IServiceHandler handler) {
/* 46 */     this.handler = handler;
/* 47 */     if (handler.initialize()) {
/* 48 */       this.name = (this.name + "[" + handler.getName() + "]");
/* 49 */       this.acceptor = handler.getAcceptor();
/* 50 */       return true;
/*    */     }
/* 52 */     return false;
/*    */   }
/*    */ 
/*    */   public String getAddress()
/*    */   {
/* 57 */     return this.acceptor.getAddress();
/*    */   }
/*    */ 
/*    */   public int getPort() {
/* 61 */     return this.acceptor.getPort();
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.server.socket.SocketServer
 * JD-Core Version:    0.5.4
 */