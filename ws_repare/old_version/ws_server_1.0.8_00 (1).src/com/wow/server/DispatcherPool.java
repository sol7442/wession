/*    */ package com.wow.server;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class DispatcherPool
/*    */ {
/*  9 */   private int pointer = 0;
/* 10 */   private List<IDispatcher> dispatchers = new ArrayList();
/*    */ 
/* 12 */   public void addDispatcher(IDispatcher dispatcher) { this.dispatchers.add(dispatcher); }
/*    */ 
/*    */   public IDispatcher next() {
/* 15 */     IDispatcher dispatcher = null;
/* 16 */     if (this.pointer < this.dispatchers.size()) {
/* 17 */       dispatcher = (IDispatcher)this.dispatchers.get(this.pointer);
/* 18 */       this.pointer += 1;
/*    */ 
/* 20 */       if (this.pointer > this.dispatchers.size()) {
/* 21 */         this.pointer = 0;
/*    */       }
/*    */     }
/* 24 */     return dispatcher;
/*    */   }
/*    */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.server.DispatcherPool
 * JD-Core Version:    0.5.4
 */