/*    */ package com.wow.wession.data;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class WessionMessage
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1769989765704165137L;
/*    */   private int type;
/*    */   private Object data;
/*    */ 
/*    */   public int getType()
/*    */   {
/* 12 */     return this.type;
/*    */   }
/*    */   public void setType(int type) {
/* 15 */     this.type = type;
/*    */   }
/*    */   public Object getData() {
/* 18 */     return this.data;
/*    */   }
/*    */   public void setData(Object data) {
/* 21 */     this.data = data;
/*    */   }
/*    */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.data.WessionMessage
 * JD-Core Version:    0.5.4
 */