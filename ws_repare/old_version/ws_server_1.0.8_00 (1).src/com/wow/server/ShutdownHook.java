/*    */ package com.wow.server;
/*    */ 
/*    */ import java.io.Closeable;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class ShutdownHook extends Thread
/*    */ {
/*    */   private Closeable object;
/*    */   private Runtime runtime;
/*    */ 
/*    */   public void register(Closeable obj)
/*    */   {
/* 13 */     this.object = obj;
/* 14 */     this.runtime = Runtime.getRuntime();
/* 15 */     this.runtime.addShutdownHook(this);
/*    */   }
/*    */ 
/*    */   public void run() {
/* 19 */     if (this.object == null) return;
/*    */     try {
/* 21 */       this.object.close();
/*    */     } catch (IOException e) {
/* 23 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   public void deregister()
/*    */   {
/* 29 */     if (this.runtime == null) return;
/*    */     try {
/* 31 */       this.runtime.removeShutdownHook(this);
/*    */     }
/*    */     catch (Exception localException) {
/*    */     }
/* 35 */     this.runtime = null;
/* 36 */     this.object = null;
/*    */   }
/*    */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.server.ShutdownHook
 * JD-Core Version:    0.5.4
 */