/*    */ package com.wow.wession.agent;
/*    */ 
/*    */ import com.wow.wession.session.ISession;
/*    */ import com.wow.wession.session.ISessionMessage;
/*    */ 
/*    */ public class WessionAgentMessage
/*    */   implements ISessionMessage
/*    */ {
/*    */   private static final long serialVersionUID = 3503769383943015397L;
/*    */   private int eventType;
/*    */   private int messageType;
/*    */   private ISession session;
/*    */ 
/*    */   public WessionAgentMessage(int eventType, ISession session)
/*    */   {
/* 16 */     this.messageType = 3;
/* 17 */     this.eventType = eventType;
/* 18 */     this.session = session;
/*    */   }
/*    */ 
/*    */   public String getEventTypeString() {
/* 22 */     return null;
/*    */   }
/*    */ 
/*    */   public int getMessageType() {
/* 26 */     return this.messageType;
/*    */   }
/*    */ 
/*    */   public int getEventType() {
/* 30 */     return this.eventType;
/*    */   }
/*    */ 
/*    */   public ISession getSession() {
/* 34 */     return this.session;
/*    */   }
/*    */ 
/*    */   public void setSession(ISession session) {
/* 38 */     this.session = session;
/*    */   }
/*    */ 
/*    */   public String getSessionID() {
/* 42 */     return this.session.getID();
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.agent.WessionAgentMessage
 * JD-Core Version:    0.5.4
 */