/*    */ package com.wow.wession.pool;
/*    */ 
/*    */ import com.wow.client.IConnection;
/*    */ import java.util.concurrent.ArrayBlockingQueue;
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ 
/*    */ public abstract class AbstractConnectionPool
/*    */   implements IConnectionPool
/*    */ {
/* 11 */   protected BlockingQueue<IConnection> queue = new ArrayBlockingQueue(100);
/*    */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.pool.AbstractConnectionPool
 * JD-Core Version:    0.5.4
 */