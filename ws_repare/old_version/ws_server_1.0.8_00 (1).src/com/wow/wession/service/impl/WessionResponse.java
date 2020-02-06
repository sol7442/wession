/*    */ package com.wow.wession.service.impl;
/*    */ 
/*    */ import com.wow.server.protocol.IResponse;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ 
/*    */ public class WessionResponse
/*    */   implements IResponse, IWessionResult
/*    */ {
/*    */   private static final long serialVersionUID = -2964341350782529017L;
/*    */   private transient long startTime;
/*    */   private transient long endTime;
/* 21 */   private String src = null;
/* 22 */   private String dest = null;
/* 23 */   private int resultCode = 0;
/* 24 */   private Object object = null;
/*    */ 
/*    */   public WessionResponse()
/*    */   {
/*    */   }
/*    */ 
/*    */   public WessionResponse(WessionRequest request) {
/* 31 */     this.startTime = request.getStartTime();
/* 32 */     this.src = request.getDest();
/* 33 */     this.dest = request.getSrc();
/*    */   }
/*    */ 
/*    */   public String getSrc() {
/* 37 */     return this.src;
/*    */   }
/*    */ 
/*    */   public void setSrc(String src) {
/* 41 */     this.src = src;
/*    */   }
/*    */ 
/*    */   public String getDest() {
/* 45 */     return this.dest;
/*    */   }
/*    */ 
/*    */   public void setDest(String dest) {
/* 49 */     this.dest = dest;
/*    */   }
/*    */ 
/*    */   public void setResultCode(int code) {
/* 53 */     this.resultCode = code;
/*    */   }
/*    */ 
/*    */   public int getResultCode() {
/* 57 */     return this.resultCode;
/*    */   }
/*    */   public long getStartTime() {
/* 60 */     return this.startTime;
/*    */   }
/*    */   public long getEndTime() {
/* 63 */     return this.endTime;
/*    */   }
/*    */   public void setEndTime() {
/* 66 */     this.endTime = System.nanoTime();
/*    */   }
/*    */   public void setData(Object object) {
/* 69 */     this.object = object;
/*    */   }
/*    */   public Object getData() {
/* 72 */     return this.object;
/*    */   }
/*    */ 
/*    */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 76 */     stream.writeObject(this.src);
/* 77 */     stream.writeObject(this.dest);
/* 78 */     stream.writeInt(this.resultCode);
/* 79 */     stream.writeObject(this.object);
/*    */   }
/*    */ 
/*    */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 83 */     this.src = ((String)stream.readObject());
/* 84 */     this.dest = ((String)stream.readObject());
/* 85 */     this.resultCode = stream.readInt();
/* 86 */     this.object = stream.readObject();
/*    */   }
/*    */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.service.impl.WessionResponse
 * JD-Core Version:    0.5.4
 */