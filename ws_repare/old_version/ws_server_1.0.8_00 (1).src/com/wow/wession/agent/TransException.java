/*    */ package com.wow.wession.agent;
/*    */ 
/*    */ import com.wow.wession.session.ISessionMessage;
/*    */ 
/*    */ public class TransException extends Exception
/*    */ {
/*  7 */   public static int ENABLE_FALSE = 1;
/*  8 */   public static int NOT_INITIALIZE = 2;
/*  9 */   public static int PARAMETER_MISS = 3;
/* 10 */   public static int PARSER_ERROR = 4;
/* 11 */   public static int NETWORK_ERROR = 5;
/*    */   private String url;
/*    */   private ISessionMessage msg;
/* 15 */   private int code = 0;
/*    */   private static final long serialVersionUID = -3673395140733838489L;
/*    */ 
/*    */   public TransException(Exception e, int code)
/*    */   {
/* 19 */     super(e.getMessage());
/* 20 */     this.code = code;
/*    */   }
/*    */   public TransException(String msg, int code) {
/* 23 */     super(msg);
/* 24 */     this.code = code;
/*    */   }
/*    */ 
/*    */   public void setUrl(String url, ISessionMessage message)
/*    */   {
/* 31 */     this.url = url;
/* 32 */     this.msg = message;
/*    */   }
/*    */ 
/*    */   public String getUrl() {
/* 36 */     return this.url;
/*    */   }
/*    */   public int getCode() {
/* 39 */     return this.code;
/*    */   }
/*    */   public ISessionMessage getMsg() {
/* 42 */     return this.msg;
/*    */   }
/*    */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.agent.TransException
 * JD-Core Version:    0.5.4
 */