/*    */ package com.wow.wession.index;
/*    */ 
/*    */ import com.wow.wession.server.WessionServerSession;
/*    */ import com.wow.wession.session.ISession;
/*    */ 
/*    */ public class UserIDIndexHandler
/*    */   implements IRepositoryIndexHandler
/*    */ {
/*    */   public String getIndexKey(ISession session)
/*    */   {
/*  9 */     if (session instanceof WessionServerSession) {
/* 10 */       WessionServerSession server_session = (WessionServerSession)session;
/* 11 */       return server_session.getUser();
/*    */     }
/* 13 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.index.UserIDIndexHandler
 * JD-Core Version:    0.5.4
 */