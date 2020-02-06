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

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.server.ServerThreadFactory
 * JD-Core Version:    0.5.4
 */