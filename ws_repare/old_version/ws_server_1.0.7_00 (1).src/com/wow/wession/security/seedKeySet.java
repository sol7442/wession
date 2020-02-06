/*    */ package com.wow.wession.security;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.HashMap;
/*    */ import java.util.Vector;
/*    */ 
/*    */ public class seedKeySet
/*    */ {
/*  7 */   public static final HashMap<String, String> map = new HashMap();
/*  8 */   public static final Vector<String> navi = new Vector();
/*    */ 
/*    */   static {
/* 11 */     for (int i = 0; i < 100; ++i) {
/* 12 */       String makeKey = Integer.toString(1001 + i);
/* 13 */       String makeSeedKey = "seedkey" + (20130001 + i) + "%@";
/*    */ 
/* 15 */       map.put(makeKey, makeSeedKey);
/* 16 */       navi.add(makeKey);
/*    */     }
/*    */   }
/*    */ 
/*    */   public static String findKey(int idx) {
/* 21 */     if ((idx >= 0) && (idx < 100)) {
/* 22 */       return (String)map.get(navi.get(idx));
/*    */     }
/* 24 */     System.out.println("key index is out of boundary");
/* 25 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.security.seedKeySet
 * JD-Core Version:    0.5.4
 */