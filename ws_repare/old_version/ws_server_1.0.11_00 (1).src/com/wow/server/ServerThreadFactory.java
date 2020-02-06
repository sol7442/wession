/*    */ package com.wow.server;
/*    */ 
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ 
/*    */ public class ServerThreadFactory
/*    */   implements ThreadFactory
/*    */ {
/*    */   private IServer server;
/*    */ 
/*    */   public Thread newThread(Runnable r)
/*    */   {
/*  9 */     this.server = ((IServer)r);
/* 10 */     Thread thread = new Thread(r);
/* 11 */     thread.setName(this.server.getName());
/* 12 */     thread.setDaemon(true);
/* 13 */     return thread;
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.server.ServerThreadFactory
 * JD-Core Version:    0.5.4
 */