/*    */ package com.wow.wession.util;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
/*    */ 
/*    */ public class StopWatch
/*    */ {
/*    */   private long start_time;
/*    */   private final TimeUnit time_unit;
/*    */ 
/*    */   public StopWatch(TimeUnit unit)
/*    */   {
/* 10 */     this.time_unit = unit;
/* 11 */     this.start_time = getNowTime();
/*    */   }
/*    */ 
/*    */   public void start() {
/* 15 */     this.start_time = getNowTime();
/*    */   }
/*    */ 
/*    */   public long stop() {
/* 19 */     return getNowTime() - this.start_time;
/*    */   }
/*    */ 
/*    */   public void reset() {
/* 23 */     this.start_time = getNowTime();
/*    */   }
/*    */ 
/*    */   private long getNowTime() {
/* 27 */     long time = 0L;
/* 28 */     switch ($SWITCH_TABLE$java$util$concurrent$TimeUnit()[this.time_unit.ordinal()]) {
/*    */     case 4:
/* 30 */       time = System.currentTimeMillis() / 1000L;
/* 31 */       break;
/*    */     case 3:
/* 33 */       time = System.currentTimeMillis();
/* 34 */       break;
/*    */     case 1:
/* 36 */       time = System.nanoTime();
/*    */     case 2:
/*    */     }
/* 39 */     return time;
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.util.StopWatch
 * JD-Core Version:    0.5.4
 */