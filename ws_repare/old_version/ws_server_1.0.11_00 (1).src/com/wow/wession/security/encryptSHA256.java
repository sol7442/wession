/*    */ package com.wow.wession.security;
/*    */ 
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.security.MessageDigest;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ 
/*    */ public class encryptSHA256
/*    */ {
/*    */   public static String work(String str)
/*    */     throws NoSuchAlgorithmException, UnsupportedEncodingException
/*    */   {
/* 10 */     return encrypt(encrypt(encrypt(str)));
/*    */   }
/*    */ 
/*    */   public static String encrypt(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
/* 14 */     String SHA = "";
/*    */     try {
/* 16 */       MessageDigest sh = MessageDigest.getInstance("SHA-256");
/* 17 */       sh.update(str.getBytes());
/* 18 */       byte[] byteData = sh.digest();
/* 19 */       StringBuffer sb = new StringBuffer();
/* 20 */       for (int i = 0; i < byteData.length; ++i) {
/* 21 */         sb.append(Integer.toString((byteData[i] & 0xFF) + 256, 16).substring(1));
/*    */       }
/* 23 */       SHA = sb.toString();
/*    */     }
/*    */     catch (NoSuchAlgorithmException e) {
/* 26 */       e.printStackTrace();
/* 27 */       SHA = null;
/*    */     }
/* 29 */     return SHA;
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.wession.security.encryptSHA256
 * JD-Core Version:    0.5.4
 */