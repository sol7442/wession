/*    */ package com.wow.wession.data;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Date;
/*    */ import java.util.Vector;
/*    */ 
/*    */ public abstract class AbstractEntity
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -6945596350359323336L;
/* 14 */   protected long time = -1L;
/* 15 */   protected int type = 1;
/* 16 */   protected int command = -1;
/* 17 */   protected Vector<Object> data = new Vector();
/*    */ 
/*    */   public long getTime() {
/* 20 */     return this.time;
/*    */   }
/*    */   public void setTime(long time) {
/* 23 */     this.time = time;
/*    */   }
/*    */ 
/*    */   public int getType() {
/* 27 */     return this.type;
/*    */   }
/*    */   public void setType(int type) {
/* 30 */     this.type = type;
/*    */   }
/*    */   public int getCommand() {
/* 33 */     return this.command;
/*    */   }
/*    */   public void setCommand(int command) {
/* 36 */     this.command = command;
/*    */   }
/*    */   public void addData(Object object) {
/* 39 */     this.data.add(object);
/*    */   }
/*    */   public Vector<Object> getData() {
/* 42 */     return this.data;
/*    */   }
/*    */   public void setData(Vector<Object> data) {
/* 45 */     this.data = data;
/*    */   }
/*    */   public Object getData(int index) {
/* 48 */     return this.data.get(index);
/*    */   }
/*    */   public int getDataSize() {
/* 51 */     return this.data.size();
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 55 */     StringBuffer buffer = new StringBuffer();
/* 56 */     buffer.append("[").append(new Date(this.time)).append("]");
/* 57 */     buffer.append("[").append(this.type).append("]");
/* 58 */     buffer.append("[").append(this.command).append("]");
/* 59 */     buffer.append("[").append(this.data).append("]");
/* 60 */     return buffer.toString();
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.wession.data.AbstractEntity
 * JD-Core Version:    0.5.4
 */