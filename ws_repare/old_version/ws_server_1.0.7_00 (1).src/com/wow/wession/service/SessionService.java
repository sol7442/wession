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

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.service.SessionService
 * JD-Core Version:    0.5.4
 */