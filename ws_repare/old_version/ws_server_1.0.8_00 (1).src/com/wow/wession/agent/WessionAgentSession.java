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

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.agent.WessionAgentSession
 * JD-Core Version:    0.5.4
 */