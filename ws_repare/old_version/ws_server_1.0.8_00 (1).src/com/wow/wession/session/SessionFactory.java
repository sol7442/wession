/*    */ package com.wow.wession.session;
/*    */ 
/*    */ public abstract class SessionFactory
/*    */ {
/*    */   private String code;
/*    */   private int type;
/*    */ 
/*    */   public abstract ISession create(Object paramObject);
/*    */ 
/*    */   public abstract ISession create(String paramString1, String paramString2);
/*    */ 
/*    */   public void setCode(String code)
/*    */   {
/* 16 */     this.code = code;
/*    */   }
/*    */   public void setSessionType(int type) {
/* 19 */     this.type = type;
/*    */   }
/*    */ 
/*    */   public String getAppCode() {
/* 23 */     return this.code;
/*    */   }
/*    */ 
/*    */   public int getSessionType() {
/* 27 */     return this.type;
/*    */   }
/*    */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.session.SessionFactory
 * JD-Core Version:    0.5.4
 */