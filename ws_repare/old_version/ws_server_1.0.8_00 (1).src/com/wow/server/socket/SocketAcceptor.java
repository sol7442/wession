/*    */ package com.wow.server.socket;
/*    */ 
/*    */ import com.wow.server.IAcceptor;
/*    */ import com.wow.server.IServiceHandler;
/*    */ import com.wow.server.IWorker;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.net.InetAddress;
/*    */ import java.net.ServerSocket;
/*    */ import java.net.Socket;
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.LinkedBlockingQueue;
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ import java.util.concurrent.ThreadPoolExecutor;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
/*    */ 
/*    */ public class SocketAcceptor
/*    */   implements IAcceptor
/*    */ {
/*    */   private ServerSocket serverSocket;
/*    */   private IServiceHandler serviceHandler;
/*    */   private ExecutorService executorService;
/* 24 */   private AtomicBoolean serverRun = new AtomicBoolean(true);
/*    */ 
/*    */   public SocketAcceptor(IServiceHandler handler, int port, int minThread, int maxThread, int idleTime) throws IOException {
/* 27 */     this.serviceHandler = handler;
/* 28 */     this.serverSocket = new ServerSocket(port);
/* 29 */     this.executorService = new ThreadPoolExecutor(maxThread, maxThread, idleTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(1000), new WorkerThreadFactory(null));
/*    */   }
/*    */ 
/*    */   public void listen()
/*    */     throws IOException
/*    */   {
/* 40 */     while (this.serverRun.get()) {
/*    */       try {
/* 42 */         Socket socket = this.serverSocket.accept();
/* 43 */         this.serviceHandler.getSocketQueue().put(socket);
/* 44 */         IWorker worker = this.serviceHandler.getNewWorker();
/* 45 */         this.executorService.execute(worker);
/*    */       } catch (InterruptedException e) {
/* 47 */         System.out.println(e.getMessage());
/* 48 */         break label73:
/*    */       }
/*    */     }
/* 51 */     label73: System.out.println(this.serviceHandler.getName() + " : listener stop");
/*    */   }
/*    */ 
/*    */   public void close() throws IOException {
/* 55 */     this.serverRun.set(false);
/* 56 */     this.serverSocket.close();
/* 57 */     this.executorService.shutdownNow();
/*    */   }
/*    */ 
/*    */   public String getAddress()
/*    */   {
/* 62 */     String address = "";
/*    */     try {
/* 64 */       this.serverSocket.getInetAddress(); address = InetAddress.getLocalHost().getHostAddress();
/*    */     } catch (Exception localException) {
/*    */     }
/* 67 */     return address;
/*    */   }
/*    */ 
/*    */   public int getPort() {
/* 71 */     return this.serverSocket.getLocalPort();
/*    */   }
/*    */ 
/*    */   private class WorkerThreadFactory
/*    */     implements ThreadFactory
/*    */   {
/*    */     private WorkerThreadFactory()
/*    */     {
/*    */     }
/*    */ 
/*    */     public Thread newThread(Runnable worker)
/*    */     {
/* 34 */       return new Thread(worker, SocketAcceptor.this.serviceHandler.getName() + " Worker");
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.server.socket.SocketAcceptor
 * JD-Core Version:    0.5.4
 */