/*    */ package com.wow.wession.agent;
/*    */ 
/*    */ import org.apache.http.cookie.CookieSpec;
/*    */ import org.apache.http.cookie.CookieSpecProvider;
/*    */ import org.apache.http.protocol.HttpContext;
/*    */ 
/*    */ class EasySpecProvider
/*    */   implements CookieSpecProvider
/*    */ {
/*    */   public CookieSpec create(HttpContext context)
/*    */   {
/* 55 */     return new EasyCookieSpec();
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.wession.agent.EasySpecProvider
 * JD-Core Version:    0.5.4
 */