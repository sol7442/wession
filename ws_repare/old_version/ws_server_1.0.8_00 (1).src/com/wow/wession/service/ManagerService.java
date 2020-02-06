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

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.service.ManagerService
 * JD-Core Version:    0.5.4
 */