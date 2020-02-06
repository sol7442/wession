/*     */ package com.wow.wession.security;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ 
/*     */ public class Base64
/*     */ {
/*     */   public static String encode(byte[] data)
/*     */   {
/*   9 */     char[] tbl = { 
/*  10 */       'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 
/*  11 */       'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 
/*  12 */       'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 
/*  13 */       'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
/*     */ 
/*  15 */     StringBuilder buffer = new StringBuilder();
/*  16 */     int pad = 0;
/*  17 */     for (int i = 0; i < data.length; i += 3)
/*     */     {
/*  19 */       int b = (data[i] & 0xFF) << 16 & 0xFFFFFF;
/*  20 */       if (i + 1 < data.length)
/*  21 */         b |= (data[(i + 1)] & 0xFF) << 8;
/*     */       else {
/*  23 */         ++pad;
/*     */       }
/*  25 */       if (i + 2 < data.length)
/*  26 */         b |= data[(i + 2)] & 0xFF;
/*     */       else {
/*  28 */         ++pad;
/*     */       }
/*     */ 
/*  31 */       for (int j = 0; j < 4 - pad; ++j) {
/*  32 */         int c = (b & 0xFC0000) >> 18;
/*  33 */         buffer.append(tbl[c]);
/*  34 */         b <<= 6;
/*     */       }
/*     */     }
/*  37 */     for (int j = 0; j < pad; ++j) {
/*  38 */       buffer.append("=");
/*     */     }
/*     */ 
/*  41 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   public static byte[] decodeBuffer(String data) {
/*  45 */     return decode(data);
/*     */   }
/*     */ 
/*     */   public static byte[] decode(String data)
/*     */   {
/*  50 */     int[] tbl = { 
/*  51 */       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
/*  52 */       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
/*  53 */       -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 
/*  54 */       55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 
/*  55 */       3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 
/*  56 */       20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 
/*  57 */       31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 
/*  58 */       48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
/*  59 */       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
/*  60 */       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
/*  61 */       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
/*  62 */       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
/*  63 */       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
/*  64 */       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
/*  65 */       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
/*  66 */     byte[] bytes = data.getBytes();
/*  67 */     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/*  68 */     for (int i = 0; i < bytes.length; ) {
/*  69 */       int b = 0;
/*  70 */       if (tbl[bytes[i]] != -1) {
/*  71 */         b = (tbl[bytes[i]] & 0xFF) << 18;
/*     */       }
/*     */       else
/*     */       {
/*  75 */         ++i;
/*  76 */         continue;
/*     */       }
/*     */ 
/*  79 */       int num = 0;
/*  80 */       if ((i + 1 < bytes.length) && (tbl[bytes[(i + 1)]] != -1)) {
/*  81 */         b |= (tbl[bytes[(i + 1)]] & 0xFF) << 12;
/*  82 */         ++num;
/*     */       }
/*  84 */       if ((i + 2 < bytes.length) && (tbl[bytes[(i + 2)]] != -1)) {
/*  85 */         b |= (tbl[bytes[(i + 2)]] & 0xFF) << 6;
/*  86 */         ++num;
/*     */       }
/*  88 */       if ((i + 3 < bytes.length) && (tbl[bytes[(i + 3)]] != -1)) {
/*  89 */         b |= tbl[bytes[(i + 3)]] & 0xFF;
/*  90 */         ++num;
/*     */       }
/*     */ 
/*  93 */       while (num > 0) {
/*  94 */         int c = (b & 0xFF0000) >> 16;
/*  95 */         buffer.write((char)c);
/*  96 */         b <<= 8;
/*  97 */         --num;
/*     */       }
/*  99 */       i += 4;
/*     */     }
/* 101 */     return buffer.toByteArray();
/*     */   }
/*     */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.wession.security.Base64
 * JD-Core Version:    0.5.4
 */