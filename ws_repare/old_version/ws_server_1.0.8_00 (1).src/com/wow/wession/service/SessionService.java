/*    */ package com.wow.wession.service;
/*    */ 
/*    */ import com.wow.server.IAcceptor;
/*    */ import com.wow.server.IServiceHandler;
/*    */ import com.wow.server.IWorker;
/*    */ import java.net.Socket;
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class SessionService
/*    */   implements IServiceHandler
/*    */ {
/* 15 */   private Logger logger = LoggerFactory.getLogger("syslog");
/*    */ 
/*    */   public boolean initialize() {
/* 18 */     return true;
/*    */   }
/*    */   public void close() {
/*    */   }
/*    */   public BlockingQueue<Socket> getSocketQueue() {
/* 23 */     return null;
/*    */   }
/*    */ 
/*    */   public IWorker getNewWorker()
/*    */   {
/* 28 */     return null;
/*    */   }
/*    */ 
/*    */   public IAcceptor getAcceptor()
/*    */   {
/* 33 */     return null;
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 38 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.service.SessionService
 * JD-Core Version:    0.5.4
 */