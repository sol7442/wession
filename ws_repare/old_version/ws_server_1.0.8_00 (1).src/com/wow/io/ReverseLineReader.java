/*    */ package com.wow.io;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.RandomAccessFile;
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.channels.FileChannel;
/*    */ import java.nio.channels.FileChannel.MapMode;
/*    */ 
/*    */ public class ReverseLineReader
/*    */ {
/*    */   private static final int BUFFER_SIZE = 8192;
/*    */   private final RandomAccessFile raf;
/*    */   private final FileChannel channel;
/*    */   private final String encoding;
/*    */   private long filePos;
/*    */   private ByteBuffer buf;
/* 19 */   private int bufPos = 0;
/* 20 */   private byte lastLineBreak = 10;
/* 21 */   private ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*    */ 
/*    */   public ReverseLineReader(File file, String encoding) throws IOException {
/* 24 */     this.raf = new RandomAccessFile(file, "r");
/* 25 */     this.channel = this.raf.getChannel();
/* 26 */     this.filePos = this.raf.length();
/* 27 */     this.encoding = encoding;
/*    */   }
/*    */   public void close() throws IOException {
/* 30 */     if (this.raf != null)
/* 31 */       this.raf.close();
/*    */   }
/*    */ 
/*    */   public String readLine()
/*    */     throws IOException
/*    */   {
/* 56 */     for (; ; this.bufPos-- > 0)
/* 37 */       if (this.bufPos < 0) {
/* 38 */         if (this.filePos == 0L) {
/* 39 */           if (this.baos == null) {
/* 40 */             return null;
/*    */           }
/* 42 */           String line = bufToString();
/* 43 */           this.baos = null;
/* 44 */           return line;
/*    */         }
/*    */ 
/* 47 */         long start = Math.max(this.filePos - 8192L, 0L);
/* 48 */         long end = this.filePos;
/* 49 */         long len = end - start;
/*    */ 
/* 51 */         this.buf = this.channel.map(FileChannel.MapMode.READ_ONLY, start, len);
/* 52 */         this.bufPos = (int)len;
/* 53 */         this.filePos = start;
/*    */ 
/* 56 */         continue;
/* 57 */         byte c = this.buf.get(this.bufPos);
/* 58 */         if ((c == 13) || (c == 10)) {
/* 59 */           if (c != this.lastLineBreak) {
/* 60 */             this.lastLineBreak = c;
/* 61 */             continue;
/*    */           }
/* 63 */           this.lastLineBreak = c;
/* 64 */           return bufToString();
/*    */         }
/* 66 */         this.baos.write(c);
/*    */       }
/*    */   }
/*    */ 
/*    */   private String bufToString() throws UnsupportedEncodingException
/*    */   {
/* 72 */     if (this.baos.size() == 0) {
/* 73 */       return "";
/*    */     }
/*    */ 
/* 76 */     byte[] bytes = this.baos.toByteArray();
/* 77 */     for (int i = 0; i < bytes.length / 2; ++i) {
/* 78 */       byte t = bytes[i];
/* 79 */       bytes[i] = bytes[(bytes.length - i - 1)];
/* 80 */       bytes[(bytes.length - i - 1)] = t;
/*    */     }
/*    */ 
/* 83 */     this.baos.reset();
/*    */ 
/* 85 */     return new String(bytes, this.encoding) + "\n";
/*    */   }
/*    */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.io.ReverseLineReader
 * JD-Core Version:    0.5.4
 */