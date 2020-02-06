/*    */ package com.wow.wession.index;
/*    */ 
/*    */ import com.wow.wession.session.ISession;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ 
/*    */ public class WessionMutilMap
/*    */ {
/*    */   private final ConcurrentMap<String, HashMap<String, ISession>> cache;
/*    */   private final ConcurrentMap<String, Object> locks;
/*    */ 
/*    */   public WessionMutilMap()
/*    */   {
/* 21 */     this.cache = new ConcurrentHashMap();
/* 22 */     this.locks = new ConcurrentHashMap();
/*    */   }
/*    */ 
/*    */   private Object getLock(String key) {
/* 26 */     Object object = new Object();
/* 27 */     Object lock = this.locks.putIfAbsent(key, object);
/* 28 */     if (lock == null) {
/* 29 */       lock = object;
/*    */     }
/* 31 */     return lock;
/*    */   }
/*    */ 
/*    */   public void put(String index_key, String value_key, ISession value) {
/* 35 */     synchronized (getLock(index_key)) {
/* 36 */       HashMap map = (HashMap)this.cache.get(index_key);
/* 37 */       if (map == null) {
/* 38 */         map = new HashMap();
/*    */       }
/* 40 */       map.put(value_key, value);
/* 41 */       this.cache.put(index_key, map);
/*    */     }
/*    */   }
/*    */ 
/*    */   public ISession remove(String index_key, String value_key) {
/* 46 */     ISession session = null;
/* 47 */     synchronized (getLock(index_key)) {
/* 48 */       HashMap map = (HashMap)this.cache.get(index_key);
/* 49 */       if (map != null) {
/* 50 */         session = (ISession)map.remove(value_key);
/* 51 */         if (map.size() <= 0) {
/* 52 */           this.locks.remove(index_key);
/* 53 */           this.cache.remove(index_key);
/*    */         }
/*    */       }
/* 56 */       return session;
/*    */     }
/*    */   }
/*    */ 
/*    */   public int size() {
/* 61 */     return this.cache.size();
/*    */   }
/*    */   public void clear() {
/* 64 */     this.locks.clear();
/* 65 */     this.cache.clear();
/*    */   }
/*    */   public List<ISession> get(String index_key) {
/* 68 */     List item_list = new ArrayList();
/* 69 */     synchronized (getLock(index_key)) {
/* 70 */       HashMap map = (HashMap)this.cache.get(index_key);
/* 71 */       if (map != null) {
/* 72 */         Iterator iter_key = map.keySet().iterator();
/* 73 */         while (iter_key.hasNext()) {
/* 74 */           item_list.add((ISession)map.get(iter_key.next()));
/*    */         }
/*    */       }
/* 77 */       return item_list;
/*    */     }
/*    */   }
/*    */ 
/*    */   public ISession[] getArray() {
/* 81 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.index.WessionMutilMap
 * JD-Core Version:    0.5.4
 */