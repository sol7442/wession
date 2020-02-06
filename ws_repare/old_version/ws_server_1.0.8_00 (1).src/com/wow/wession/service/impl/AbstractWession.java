/*    */ package com.wow.wession.service.impl;
/*    */ 
/*    */ import com.wow.server.protocol.IRequest;
/*    */ 
/*    */ public class AbstractWession
/*    */   implements IRequest
/*    */ {
/*    */   private static final long serialVersionUID = -7651569374699149987L;
/*    */   public static final int UNDEFINED = 0;
/*    */   public static final int INSERT = 1;
/*    */   public static final int REMOVE = 2;
/*    */   public static final int MODIFY = 3;
/*    */   public static final int GET = 4;
/*    */   public static final int GETBY = 5;
/*    */   public static final int CLUSTER_IN = 11;
/*    */   public static final int CLUSTER_OUT = 12;
/*    */   public static final int JOURNAL_IN = 13;
/*    */   public static final int JOURNAL_OUT = 14;
/*    */   public static final int CLUSTER = 1;
/*    */   public static final int JOURNAL = 2;
/*    */   public static final int SESSION = 3;
/*    */   public static final int MANAGER = 4;
/*    */   public static final int CONTROL = 5;
/* 30 */   private String src = "";
/* 31 */   private String dest = "";
/* 32 */   private int sericeType = 0;
/*    */ 
/*    */   public String getSrc() {
/* 35 */     return this.src;
/*    */   }
/*    */   public void setSrc(String src) {
/* 38 */     this.src = src;
/*    */   }
/*    */   public String getDest() {
/* 41 */     return this.dest;
/*    */   }
/*    */   public void setDest(String dest) {
/* 44 */     this.dest = dest;
/*    */   }
/*    */   public int getServiceType() {
/* 47 */     return this.sericeType;
/*    */   }
/*    */   public void setServiceType(int type) {
/* 50 */     this.sericeType = type;
/*    */   }
/*    */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.service.impl.AbstractWession
 * JD-Core Version:    0.5.4
 */