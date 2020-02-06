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

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.wession.session.SessionFactory
 * JD-Core Version:    0.5.4
 */