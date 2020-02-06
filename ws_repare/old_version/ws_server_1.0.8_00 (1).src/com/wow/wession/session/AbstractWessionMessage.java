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

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.session.AbstractWessionMessage
 * JD-Core Version:    0.5.4
 */