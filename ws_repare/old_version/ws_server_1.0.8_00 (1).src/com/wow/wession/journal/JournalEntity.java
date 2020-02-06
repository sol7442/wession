/*    */ package com.wow.wession.journal;
/*    */ 
/*    */ import com.wow.wession.data.AbstractEntity;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class JournalEntity extends AbstractEntity
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 3796795297513492180L;
/*    */ 
/*    */   public JournalEntity(int cmd)
/*    */   {
/* 12 */     this.type = 2;
/* 13 */     this.time = System.currentTimeMillis();
/* 14 */     this.command = cmd;
/*    */   }
/*    */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.journal.JournalEntity
 * JD-Core Version:    0.5.4
 */