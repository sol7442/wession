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
/*     */       } catch (UnsupportedEncodingException e) {
/*  34 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */ 
/*  38 */     return valueName;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args) {
/*  42 */     setting();
/*  43 */     String mapkey = "1024";
/*  44 */     String tempCode = "5579";
/*  45 */     String plainText = "I love You, but you disappointed me. so, I'm sad. This is safe method. Until keyset noose.";
/*     */ 
/*  48 */     String seedkey = (String)map.get(mapkey);
/*  49 */     String enc = "";
/*     */     try {
/*  51 */       enc = encryptSEED.getSeedEncrypt(plainText, encryptSEED.getSeedRoundKey(seedkey));
/*  52 */       System.out.println("plain text : " + plainText);
/*  53 */       System.out.println("encrypt text = " + enc);
/*     */     } catch (Exception e1) {
/*  55 */       e1.printStackTrace();
/*     */     }
/*     */ 
/*  58 */     System.out.println("======================");
/*     */ 
/*  61 */     String hashCode = "";
/*  62 */     String sendEnc = "";
/*     */     try
/*     */     {
/*  65 */       Base64 encoder = new Base64();
/*  66 */       hashCode = encryptSHA256.work(tempCode + mapkey);
/*  67 */       System.out.println("tempCode = " + tempCode + " and hascode = " + hashCode);
/*  68 */       sendEnc = Base64.encode(enc.getBytes());
/*  69 */       System.out.println("send encrypt text = " + URLEncoder.encode(sendEnc, "UTF-8"));
/*     */     }
/*     */     catch (NoSuchAlgorithmException e) {
/*  72 */       e.printStackTrace();
/*     */     } catch (UnsupportedEncodingException e) {
/*  74 */       e.printStackTrace();
/*     */     }
/*     */ 
/*  77 */     System.out.println("======================");
/*     */ 
/*  80 */     String seedkey2 = getKey(tempCode, hashCode);
/*  81 */     String dec = "";
/*  82 */     String recvEnc = "";
/*     */     try
/*     */     {
/*  85 */       Base64 decoder = new Base64();
/*  86 */       recvEnc = Base64.decodeBuffer(sendEnc).toString();
/*  87 */       dec = encryptSEED.getSeedDecrypt(enc, encryptSEED.getSeedRoundKey(seedkey));
/*  88 */       System.out.println(dec);
/*     */     } catch (Exception e) {
/*  90 */       e.printStackTrace();
/*     */     }
/*     */ 
/*  93 */     LinkedList list = new LinkedList();
/*  94 */     list.add("1");
/*  95 */     list.add("2");
/*  96 */     list.add("3");
/*  97 */     list.add("4");
/*  98 */     list.add("5");
/*  99 */     list.remove("1");
/*     */ 
/* 101 */     System.out.println(list.get(list.size() - 1));
/*     */   }
/*     */ 
/*     */   private static void setting()
/*     */   {
/* 112 */     map = seedKeySet.map;
/*     */   }
/*     */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.wession.security.seedKeyUtil
 * JD-Core Version:    0.5.4
 */