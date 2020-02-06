/*    */ package com.wow.server.nio;
/*    */ 
/*    */ import com.wow.server.IAcceptor;
/*    */ import com.wow.server.IServer;
/*    */ import com.wow.server.IServiceHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public abstract class NioServer
/*    */   implements IServer
/*    */ {
/*    */   public final String instanceName;
/* 15 */   private IAcceptor acceptor = null;
/* 16 */   private IServiceHandler handler = null;
/*    */ 
/*    */   public NioServer(String name) {
/* 19 */     this.instanceName = name;
/*    */   }
/*    */ 
/*    */   public boolean initialize(IServiceHandler handler)
/*    */   {
/* 24 */     setHandler(handler);
/*    */ 
/* 30 */     return true;
/*    */   }
/*    */ 
/*    */   public void close()
/*    */     throws IOException
/*    */   {
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/*    */     try
/*    */     {
/* 45 */       this.acceptor.listen();
/*    */     } catch (IOException e) {
/* 47 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   public IServiceHandler getHandler()
/*    */   {
/* 54 */     return this.handler;
/*    */   }
/*    */ 
/*    */   public void setHandler(IServiceHandler handler)
/*    */   {
/* 59 */     this.handler = handler;
/*    */   }
/*    */ 
/*    */   public void onConnectionAccepted()
/*    */   {
/* 64 */     NonBlockingConnection conn = new NonBlockingConnection();
/*    */   }
/*    */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.server.nio.NioServer
 * JD-Core Version:    0.5.4
 */