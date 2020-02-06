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

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.util.Time
 * JD-Core Version:    0.5.4
 */