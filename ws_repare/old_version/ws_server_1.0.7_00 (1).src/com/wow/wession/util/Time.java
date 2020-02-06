/*    */ package com.wow.wession.util;
/*    */ 
/*    */ import java.text.DateFormat;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.TimeZone;
/*    */ 
/*    */ public class Time
/*    */ {
/*  9 */   private static DateFormat simple_df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss (z Z)");
/* 10 */   private static TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
/*    */ 
/* 12 */   static { simple_df.setTimeZone(tz); }
/*    */ 
/*    */   public static String getTime()
/*    */   {
/* 16 */     return simple_df.format(new Date());
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.util.Time
 * JD-Core Version:    0.5.4
 */