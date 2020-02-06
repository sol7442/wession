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

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.index.UserIDIndexHandler
 * JD-Core Version:    0.5.4
 */