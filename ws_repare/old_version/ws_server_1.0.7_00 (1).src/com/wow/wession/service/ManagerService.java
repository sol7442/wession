/*    */ package com.wow.wession.service;
/*    */ 
/*    */ import com.wow.server.IAcceptor;
/*    */ import com.wow.server.IServiceHandler;
/*    */ import com.wow.server.IWorker;
/*    */ import java.net.Socket;
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ 
/*    */ public class ManagerService
/*    */   implements IServiceHandler
/*    */ {
/*    */   public BlockingQueue<Socket> getSocketQueue()
/*    */   {
/* 14 */     return null;
/*    */   }
/*    */ 
/*    */   public IWorker getNewWorker()
/*    */   {
/* 19 */     return null;
/*    */   }
/*    */ 
/*    */   public boolean initialize() {
/* 23 */     return true;
/*    */   }
/*    */   public void close() {
/*    */   }
/*    */   public IAcceptor getAcceptor() {
/* 28 */     return null;
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 33 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.service.ManagerService
 * JD-Core Version:    0.5.4
 */