/*    */ package com.wow.wession.util;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
/*    */ import java.util.concurrent.locks.Condition;
/*    */ import java.util.concurrent.locks.Lock;
/*    */ import java.util.concurrent.locks.ReentrantLock;
/*    */ 
/*    */ public class SwitchLock
/*    */ {
/* 13 */   private Lock lock = new ReentrantLock();
/* 14 */   private Condition condition = this.lock.newCondition();
/* 15 */   private AtomicBoolean bswitch = new AtomicBoolean(true);
/*    */ 
/*    */   public boolean getSwitch()
/*    */     throws InterruptedException
/*    */   {
/* 23 */     ReentrantLock localLock = (ReentrantLock)this.lock;
/* 24 */     localLock.lock();
/*    */     try {
/* 26 */       while (!this.bswitch.get())
/* 27 */         this.condition.await();
/*    */     }
/*    */     finally {
/* 30 */       localLock.unlock();
/*    */     }
/* 32 */     return this.bswitch.get();
/*    */   }
/*    */ 
/*    */   public void setSwtich(boolean on)
/*    */   {
/* 41 */     ReentrantLock localLock = (ReentrantLock)this.lock;
/* 42 */     localLock.lock();
/*    */     try {
/* 44 */       this.bswitch.set(on);
/* 45 */       if (!this.bswitch.get()) break label42;
/* 46 */       label42: this.condition.signalAll();
/*    */     }
/*    */     finally {
/* 49 */       localLock.unlock();
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.util.SwitchLock
 * JD-Core Version:    0.5.4
 */