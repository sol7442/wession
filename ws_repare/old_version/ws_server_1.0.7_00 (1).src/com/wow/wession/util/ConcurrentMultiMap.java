/*    */ package com.wow.wession.util;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ 
/*    */ public class ConcurrentMultiMap<K extends Comparable, V extends Comparable>
/*    */ {
/*    */   private final ConcurrentMap<K, Set<V>> cache;
/*    */   private final ConcurrentMap<K, Object> locks;
/*    */ 
/*    */   public ConcurrentMultiMap()
/*    */   {
/* 14 */     this.cache = new ConcurrentHashMap();
/* 15 */     this.locks = new ConcurrentHashMap();
/*    */   }
/*    */ 
/*    */   private Object getLock(K key) {
/* 19 */     Object object = new Object();
/* 20 */     Object lock = this.locks.putIfAbsent(key, object);
/* 21 */     if (lock == null) {
/* 22 */       lock = object;
/*    */     }
/* 24 */     return lock;
/*    */   }
/*    */ 
/*    */   public void put(K key, V value) {
/* 28 */     synchronized (getLock(key)) {
/* 29 */       Set set = (Set)this.cache.get(key);
/* 30 */       if (set == null) {
/* 31 */         set = new HashSet();
/* 32 */         this.cache.put(key, set);
/*    */       }
/* 34 */       set.add(value);
/*    */     }
/*    */   }
/*    */ 
/*    */   public Set<V> remove(K key) {
/* 39 */     synchronized (getLock(key)) {
/* 40 */       this.locks.remove(key);
/* 41 */       return (Set)this.cache.remove(key);
/*    */     }
/*    */   }
/*    */ 
/*    */   public int size() {
/* 46 */     return this.cache.size();
/*    */   }
/*    */ 
/*    */   public Set<V> get(K key) {
/* 50 */     synchronized (getLock(key)) {
/* 51 */       return (Set)this.cache.get(key);
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.util.ConcurrentMultiMap
 * JD-Core Version:    0.5.4
 */