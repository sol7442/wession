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

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.pool.AbstractConnectionPool
 * JD-Core Version:    0.5.4
 */