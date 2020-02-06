/*    */ package com.wow.wession.repository;
/*    */ 
/*    */ import com.wow.wession.data.IEntityState;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class RepositoryMessage
/*    */   implements Cloneable, IEntityState
/*    */ {
/*  8 */   public int method = 0;
/*  9 */   public boolean clusterable = false;
/* 10 */   public ArrayList<Object> datas = new ArrayList();
/*    */ 
/*    */   public RepositoryMessage clone() throws CloneNotSupportedException
/*    */   {
/* 14 */     RepositoryMessage msg = (RepositoryMessage)super.clone();
/* 15 */     msg.method = this.method;
/* 16 */     msg.clusterable = this.clusterable;
/* 17 */     msg.datas = ((ArrayList)this.datas.clone());
/* 18 */     return msg;
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.wession.repository.RepositoryMessage
 * JD-Core Version:    0.5.4
 */