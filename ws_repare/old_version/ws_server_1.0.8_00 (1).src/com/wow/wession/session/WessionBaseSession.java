/*    */ package com.wow.wession.session;
/*    */ 
/*    */ import java.util.Date;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ 
/*    */ public abstract class WessionBaseSession
/*    */   implements ISession
/*    */ {
/*    */   private static final long serialVersionUID = -3524542241621912886L;
/*    */   private String id;
/*    */   private String user;
/* 15 */   private ConcurrentMap<String, Object> attributes = new ConcurrentHashMap();
/*    */   private long createTime;
/*    */   private long lastAccessTime;
/*    */ 
/*    */   public WessionBaseSession()
/*    */   {
/* 21 */     this.createTime = System.currentTimeMillis();
/*    */   }
/*    */   public WessionBaseSession(String id, String user) {
/* 24 */     this.id = id;
/* 25 */     this.user = user;
/* 26 */     this.createTime = System.currentTimeMillis();
/*    */   }
/*    */ 
/*    */   public String getUser() {
/* 30 */     return this.user;
/*    */   }
/*    */ 
/*    */   public String getID()
/*    */   {
/* 35 */     return this.id;
/*    */   }
/*    */ 
/*    */   public Object getAttribute(String key) {
/* 39 */     return this.attributes.get(key);
/*    */   }
/*    */   public Set<String> getAttributeKeySet() {
/* 42 */     return this.attributes.keySet();
/*    */   }
/*    */   public void setAttribute(String key, Object value) {
/* 45 */     this.attributes.put(key, value);
/*    */   }
/*    */   public Object removeAttribute(String key) {
/* 48 */     return this.attributes.remove(key);
/*    */   }
/*    */ 
/*    */   public long getCreateTime() {
/* 52 */     return this.createTime;
/*    */   }
/*    */   public void setCreateTime(long time) {
/* 55 */     this.createTime = time;
/*    */   }
/*    */   public long getLastAccessTime() {
/* 58 */     return this.lastAccessTime;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 62 */     StringBuffer buffer = new StringBuffer();
/*    */ 
/* 64 */     Date d = new Date(this.createTime);
/* 65 */     buffer.append(d.toString()).append(" ");
/* 66 */     buffer.append(this.id).append(",").append(this.user);
/* 67 */     synchronized (this.attributes) {
/* 68 */       Iterator keys = this.attributes.keySet().iterator();
/* 69 */       while (keys.hasNext()) {
/* 70 */         String key = (String)keys.next();
/* 71 */         Object object = this.attributes.get(key);
/* 72 */         buffer.append("(").append(key).append(":").append(object.toString()).append(")");
/*    */       }
/*    */     }
/* 75 */     return buffer.toString();
/*    */   }
/*    */ 
/*    */   public int compareTo(ISession session) {
/* 79 */     if (session == null) return -1;
/*    */ 
/* 81 */     if (session.getID().equals(this.id)) {
/* 82 */       return 0;
/*    */     }
/* 84 */     return -1;
/*    */   }
/*    */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.session.WessionBaseSession
 * JD-Core Version:    0.5.4
 */