/*    */ package com.wow.wession.service.impl;
/*    */ 
/*    */ import com.wow.server.protocol.IRequest;
/*    */ 
/*    */ public class WessionRequest
/*    */   implements IRequest, IWessionServiceType
/*    */ {
/*    */   private static final long serialVersionUID = 1550075010031569565L;
/* 12 */   private transient long startTime = 0L;
/* 13 */   private String src = null;
/* 14 */   private String dest = null;
/* 15 */   private int serviceType = 0;
/* 16 */   private int method = 0;
/* 17 */   private Object object = null;
/*    */ 
/*    */   public WessionRequest() {
/*    */   }
/* 21 */   public WessionRequest(String senderName, String receiverName) { this.src = senderName;
/* 22 */     this.dest = receiverName; }
/*    */ 
/*    */   public String getSrc()
/*    */   {
/* 26 */     return this.src;
/*    */   }
/*    */ 
/*    */   public void setSrc(String src) {
/* 30 */     this.src = src;
/*    */   }
/*    */ 
/*    */   public String getDest() {
/* 34 */     return this.dest;
/*    */   }
/*    */ 
/*    */   public void setDest(String dest) {
/* 38 */     this.dest = dest;
/*    */   }
/*    */ 
/*    */   public int getServiceType() {
/* 42 */     return this.serviceType;
/*    */   }
/*    */ 
/*    */   public void setServiceType(int type) {
/* 46 */     this.serviceType = type;
/*    */   }
/*    */ 
/*    */   public void setMethod(int method) {
/* 50 */     this.method = method;
/*    */   }
/*    */   public int getMethod() {
/* 53 */     return this.method;
/*    */   }
/*    */   public void setData(Object object) {
/* 56 */     this.object = object;
/*    */   }
/*    */   public Object getData() {
/* 59 */     return this.object;
/*    */   }
/*    */   public long getStartTime() {
/* 62 */     return this.startTime;
/*    */   }
/*    */   public void setStartTime() {
/* 65 */     this.startTime = System.nanoTime();
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.wession.service.impl.WessionRequest
 * JD-Core Version:    0.5.4
 */