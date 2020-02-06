/*    */ package com.wow.server.nio;
/*    */ 
/*    */ import com.wow.server.DispatcherPool;
/*    */ import com.wow.server.IAcceptor;
/*    */ import com.wow.server.IDispatcher;
/*    */ import com.wow.server.IServiceHandler;
/*    */ import java.io.IOException;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.ServerSocket;
/*    */ import java.nio.channels.ServerSocketChannel;
/*    */ import java.nio.channels.SocketChannel;
/*    */ 
/*    */ public class NioAcceptor
/*    */   implements IAcceptor
/*    */ {
/*    */   private DispatcherPool dispatcherPool;
/*    */   private ServerSocketChannel serverChannel;
/*    */   private IServiceHandler callback;
/*    */ 
/*    */   public NioAcceptor(IServiceHandler handler, InetSocketAddress address)
/*    */   {
/*    */     try
/*    */     {
/* 21 */       this.callback = handler;
/*    */ 
/* 24 */       this.serverChannel = ServerSocketChannel.open();
/* 25 */       this.serverChannel.configureBlocking(true);
/*    */ 
/* 28 */       this.serverChannel.socket().setSoTimeout(0);
/* 29 */       this.serverChannel.socket().setReuseAddress(true);
/*    */ 
/* 32 */       this.serverChannel.socket().bind(address, 0);
/*    */ 
/* 35 */       this.dispatcherPool = new NioDispatcherPool();
/*    */     }
/*    */     catch (IOException e)
/*    */     {
/* 39 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   public void listen() throws IOException
/*    */   {
/*    */     while (true) {
/* 46 */       SocketChannel channel = this.serverChannel.accept();
/*    */ 
/* 48 */       IDispatcher localIDispatcher = this.dispatcherPool.next();
/*    */     }
/*    */   }
/*    */ 
/*    */   public void close()
/*    */     throws IOException
/*    */   {
/*    */   }
/*    */ 
/*    */   public String getAddress()
/*    */   {
/* 61 */     return null;
/*    */   }
/*    */ 
/*    */   public int getPort()
/*    */   {
/* 66 */     return 0;
/*    */   }
/*    */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.server.nio.NioAcceptor
 * JD-Core Version:    0.5.4
 */