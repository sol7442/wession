/*    */ package com.wow.wession.cluster;
/*    */ 
/*    */ import com.wow.wession.data.AbstractEntity;
/*    */ 
/*    */ public class ClusterEntity extends AbstractEntity
/*    */ {
/*    */   private static final long serialVersionUID = 5495745623877520034L;
/*    */ 
/*    */   public ClusterEntity(int cmd)
/*    */   {
/* 11 */     this.type = 1;
/* 12 */     this.time = System.currentTimeMillis();
/* 13 */     this.command = cmd;
/*    */   }
/*    */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.cluster.ClusterEntity
 * JD-Core Version:    0.5.4
 */