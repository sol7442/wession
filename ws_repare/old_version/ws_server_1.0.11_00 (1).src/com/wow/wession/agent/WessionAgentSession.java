/*    */ package com.wow.wession.agent;
/*    */ 
/*    */ import com.wow.wession.session.WessionBaseSession;
/*    */ 
/*    */ public class WessionAgentSession extends WessionBaseSession
/*    */ {
/*    */   private static final long serialVersionUID = 2703693293244925688L;
/*    */   private String appCode;
/* 10 */   private int state = 0;
/*    */ 
/*    */   public WessionAgentSession(String id, String user, String app) {
/* 13 */     super(id, user);
/* 14 */     this.appCode = app;
/*    */   }
/*    */ 
/*    */   public String getAppCode() {
/* 18 */     return this.appCode;
/*    */   }
/*    */   public int getState() {
/* 21 */     return this.state;
/*    */   }
/*    */   public void setState(int state) {
/* 24 */     this.state = state;
/*    */   }
/*    */   public void setAppCode(String appCode) {
/* 27 */     this.appCode = appCode;
/*    */   }
/*    */   public String toString() {
/* 30 */     StringBuffer buffer = new StringBuffer();
/* 31 */     buffer.append(super.toString());
/* 32 */     buffer.append(",").append(this.appCode);
/* 33 */     return buffer.toString();
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.wession.agent.WessionAgentSession
 * JD-Core Version:    0.5.4
 */