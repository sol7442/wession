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

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.journal.JournalEntity
 * JD-Core Version:    0.5.4
 */