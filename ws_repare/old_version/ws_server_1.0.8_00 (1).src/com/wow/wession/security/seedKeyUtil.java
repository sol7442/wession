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
/*     */ 
/*     */ public class seedKeyUtil
/*     */ {
/*  12 */   private static HashMap<String, String> map = null;
/*     */ 
/*     */   public seedKeyUtil() {
/*  15 */     map = seedKeySet.map;
/*     */   }
/*     */ 
/*     */   public static String getKey(String tempCode, String hashCode)
/*     */   {
/*  20 */     Set key = map.keySet();
/*  21 */     String valueName = "";
/*     */ 
/*  23 */     for (Iterator iterator = key.iterator(); iterator.hasNext(); ) {
/*  24 */       String keyName = (String)iterator.next();
/*     */       try {
/*  26 */         String keysetHashCode = encryptSHA256.work(tempCode + keyName);
/*  27 */         if (keysetHashCode.equals(hashCode)) {
/*  28 */           valueName = (String)map.get(keyName);
/*  29 */           System.out.println(keyName + " = " + valueName);
/*     */         }
/*     */       } catch (NoSuchAlgorithmException e) {
/*  32 */         e.printStackTrace();
/*     */       }
/*     */       catch (UnsupportedEncodingException e) {
/*  35 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */ 
/*  39 */     return valueName;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args) {
/*  43 */     setting();
/*  44 */     String mapkey = "1024";
/*  45 */     String tempCode = "5579";
/*  46 */     String plainText = "I love You, but you disappointed me. so, I'm sad. This is safe method. Until keyset noose.";
/*     */ 
/*  49 */     String seedkey = (String)map.get(mapkey);
/*  50 */     String enc = "";
/*     */     try {
/*  52 */       enc = encryptSEED.getSeedEncrypt(plainText, encryptSEED.getSeedRoundKey(seedkey));
/*  53 */       System.out.println("plain text : " + plainText);
/*  54 */       System.out.println("encrypt text = " + enc);
/*     */     }
/*     */     catch (Exception e1) {
/*  57 */       e1.printStackTrace();
/*     */     }
/*     */ 
/*  60 */     System.out.println("======================");
/*     */ 
/*  63 */     String hashCode = "";
/*  64 */     String sendEnc = "";
/*     */     try
/*     */     {
/*  67 */       Base64 encoder = new Base64();
/*  68 */       hashCode = encryptSHA256.work(tempCode + mapkey);
/*  69 */       System.out.println("tempCode = " + tempCode + " and hascode = " + hashCode);
/*  70 */       sendEnc = Base64.encode(enc.getBytes());
/*  71 */       System.out.println("send encrypt text = " + URLEncoder.encode(sendEnc, "UTF-8"));
/*     */     }
/*     */     catch (NoSuchAlgorithmException e) {
/*  74 */       e.printStackTrace();
/*     */     }
/*     */     catch (UnsupportedEncodingException e)
/*     */     {
/*  78 */       e.printStackTrace();
/*     */     }
/*     */ 
/*  81 */     System.out.println("======================");
/*     */ 
/*  84 */     String seedkey2 = getKey(tempCode, hashCode);
/*  85 */     String dec = "";
/*  86 */     String recvEnc = "";
/*     */     try
/*     */     {
/*  89 */       Base64 decoder = new Base64();
/*  90 */       recvEnc = Base64.decodeBuffer(sendEnc).toString();
/*  91 */       dec = encryptSEED.getSeedDecrypt(enc, encryptSEED.getSeedRoundKey(seedkey));
/*  92 */       System.out.println(dec);
/*     */     }
/*     */     catch (Exception e) {
/*  95 */       e.printStackTrace();
/*     */     }
/*     */ 
/*  98 */     LinkedList list = new LinkedList();
/*  99 */     list.add("1");
/* 100 */     list.add("2");
/* 101 */     list.add("3");
/* 102 */     list.add("4");
/* 103 */     list.add("5");
/* 104 */     list.remove("1");
/*     */ 
/* 106 */     System.out.println(list.get(list.size() - 1));
/*     */   }
/*     */ 
/*     */   private static void setting()
/*     */   {
/* 118 */     map = seedKeySet.map;
/*     */   }
/*     */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.security.seedKeyUtil
 * JD-Core Version:    0.5.4
 */