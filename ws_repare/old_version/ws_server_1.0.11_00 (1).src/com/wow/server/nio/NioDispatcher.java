/*    */ package com.wow.server.nio;
/*    */ 
/*    */ import com.wow.server.IDispatcher;
/*    */ import java.io.IOException;
/*    */ import java.nio.channels.SelectionKey;
/*    */ import java.nio.channels.Selector;
/*    */ import java.nio.channels.SocketChannel;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class NioDispatcher
/*    */   implements IDispatcher
/*    */ {
/*    */   Selector selector;
/*    */ 
/*    */   public void register(SocketChannel channel)
/*    */   {
/* 17 */     NonBlockingConnection connection = new NonBlockingConnection();
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/*    */     try
/*    */     {
/* 27 */       if (this.selector.select() > 0);
/* 28 */       Set selectedEventKeys = this.selector.selectedKeys();
/* 29 */       Iterator it = selectedEventKeys.iterator();
/*    */ 
/* 31 */       SelectionKey eventKey = (SelectionKey)it.next();
/* 32 */       SocketHanlder handler = (SocketHanlder)eventKey.attachment();
/*    */ 
/* 34 */       handler.onReadEvent();
/*    */ 
/* 36 */       handler.onWriteEvent();
/*    */     }
/*    */     catch (IOException e)
/*    */     {
/* 44 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.server.nio.NioDispatcher
 * JD-Core Version:    0.5.4
 */