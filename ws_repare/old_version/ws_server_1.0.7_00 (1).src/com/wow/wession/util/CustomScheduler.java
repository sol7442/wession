/*    */ package com.wow.wession.util;
/*    */ 
/*    */ import java.util.concurrent.ScheduledExecutorService;
/*    */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ 
/*    */ public class CustomScheduler
/*    */ {
/*  9 */   private ScheduledExecutorService scheduler = null;
/* 10 */   private String name = "";
/* 11 */   private Runnable action = null;
/*    */ 
/* 13 */   public CustomScheduler(String name, Runnable action) { this.name = name;
/* 14 */     this.action = action; }
/*    */ 
/*    */   public void start(int delay, int period)
/*    */   {
/* 18 */     if (this.scheduler == null) {
/* 19 */       create(this.name);
/*    */     }
/*    */ 
/* 22 */     this.scheduler.scheduleAtFixedRate(this.action, delay, period, TimeUnit.MILLISECONDS);
/*    */   }
/*    */   public void stop() {
/* 25 */     this.scheduler.shutdown();
/* 26 */     this.scheduler = null;
/*    */   }
/*    */   private void create(String name) {
/* 29 */     if (this.scheduler == null)
/* 30 */       this.scheduler = new ScheduledThreadPoolExecutor(1, new ThreadFactory(name) {
/*    */         public Thread newThread(Runnable r) {
/* 32 */           return new Thread(r, this.val$name);
/*    */         }
/*    */       });
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.util.CustomScheduler
 * JD-Core Version:    0.5.4
 */