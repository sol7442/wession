/*    */ package com.wow.wession.session;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class AbstractWessionMessage
/*    */   implements ISessionMessage, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -3473793763523463511L;
/*    */   private int eventType;
/*    */   private int messageType;
/*    */   private ISession session;
/*    */ 
/*    */   public String getEventTypeString()
/*    */   {
/* 16 */     return null;
/*    */   }
/*    */ 
/*    */   public int getMessageType() {
/* 20 */     return this.messageType;
/*    */   }
/*    */ 
/*    */   public int getEventType() {
/* 24 */     return this.eventType;
/*    */   }
/*    */ 
/*    */   public ISession getSession() {
/* 28 */     return this.session;
/*    */   }
/*    */ 
/*    */   public void setSession(ISession session) {
/* 32 */     this.session = session;
/*    */   }
/*    */ 
/*    */   public String getSessionID() {
/* 36 */     return this.session.getID();
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.session.AbstractWessionMessage
 * JD-Core Version:    0.5.4
 */