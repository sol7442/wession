/*     */ package com.wow.wession.security;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLEncoder;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Set;
/*     */ import sun.misc.BASE64Decoder;
/*     */ import sun.misc.BASE64Encoder;
/*     */ 
/*     */ public class seedKeyUtil
/*     */ {
/*  16 */   private static HashMap<String, String> map = null;
/*     */ 
/*     */   public seedKeyUtil() {
/*  19 */     map = seedKeySet.map;
/*     */   }
/*     */ 
/*     */   public static String getKey(String tempCode, String hashCode)
/*     */   {
/*  24 */     Set key = map.keySet();
/*  25 */     String valueName = "";
/*     */ 
/*  27 */     for (Iterator iterator = key.iterator(); iterator.hasNext(); ) {
/*  28 */       String keyName = (String)iterator.next();
/*     */       try {
/*  30 */         String keysetHashCode = encryptSHA256.work(tempCode + keyName);
/*  31 */         if (keysetHashCode.equals(hashCode)) {
/*  32 */           valueName = (String)map.get(keyName);
/*  33 */           System.out.println(keyName + " = " + valueName);
/*     */         }
/*     */       } catch (NoSuchAlgorithmException e) {
/*  36 */         e.printStackTrace();
/*     */       }
/*     */       catch (UnsupportedEncodingException e) {
/*  39 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */ 
/*  43 */     return valueName;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args) {
/*  47 */     setting();
/*  48 */     String mapkey = "1024";
/*  49 */     String tempCode = "5579";
/*  50 */     String plainText = "I love You, but you disappointed me. so, I'm sad. This is safe method. Until keyset noose.";
/*     */ 
/*  53 */     String seedkey = (String)map.get(mapkey);
/*  54 */     String enc = "";
/*     */     try {
/*  56 */       enc = encryptSEED.getSeedEncrypt(plainText, encryptSEED.getSeedRoundKey(seedkey));
/*  57 */       System.out.println("plain text : " + plainText);
/*  58 */       System.out.println("encrypt text = " + enc);
/*     */     }
/*     */     catch (Exception e1) {
/*  61 */       e1.printStackTrace();
/*     */     }
/*     */ 
/*  64 */     System.out.println("======================");
/*     */ 
/*  67 */     String hashCode = "";
/*  68 */     String sendEnc = "";
/*     */     try
/*     */     {
/*  71 */       BASE64Encoder encoder = new BASE64Encoder();
/*  72 */       hashCode = encryptSHA256.work(tempCode + mapkey);
/*  73 */       System.out.println("tempCode = " + tempCode + " and hascode = " + hashCode);
/*  74 */       sendEnc = encoder.encode(enc.getBytes());
/*  75 */       System.out.println("send encrypt text = " + URLEncoder.encode(sendEnc, "UTF-8"));
/*     */     }
/*     */     catch (NoSuchAlgorithmException e) {
/*  78 */       e.printStackTrace();
/*     */     }
/*     */     catch (UnsupportedEncodingException e)
/*     */     {
/*  82 */       e.printStackTrace();
/*     */     }
/*     */ 
/*  85 */     System.out.println("======================");
/*     */ 
/*  88 */     String seedkey2 = getKey(tempCode, hashCode);
/*  89 */     String dec = "";
/*  90 */     String recvEnc = "";
/*     */     try
/*     */     {
/*  93 */       BASE64Decoder decoder = new BASE64Decoder();
/*  94 */       recvEnc = decoder.decodeBuffer(sendEnc).toString();
/*  95 */       dec = encryptSEED.getSeedDecrypt(enc, encryptSEED.getSeedRoundKey(seedkey));
/*  96 */       System.out.println(dec);
/*     */     }
/*     */     catch (Exception e) {
/*  99 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 102 */     LinkedList list = new LinkedList();
/* 103 */     list.add("1");
/* 104 */     list.add("2");
/* 105 */     list.add("3");
/* 106 */     list.add("4");
/* 107 */     list.add("5");
/* 108 */     list.remove("1");
/*     */ 
/* 110 */     System.out.println(list.get(list.size() - 1));
/*     */   }
/*     */ 
/*     */   private static void setting()
/*     */   {
/* 122 */     map = seedKeySet.map;
/*     */   }
/*     */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.security.seedKeyUtil
 * JD-Core Version:    0.5.4
 */