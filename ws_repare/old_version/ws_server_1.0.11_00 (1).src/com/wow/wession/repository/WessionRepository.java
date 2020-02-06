/*    */ package com.wow.wession.repository;
/*    */ 
/*    */ import com.wow.wession.index.IndexRepository;
/*    */ import com.wow.wession.session.ISession;
/*    */ import java.util.Collection;
/*    */ import java.util.Comparator;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.TreeSet;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ 
/*    */ public class WessionRepository
/*    */ {
/* 17 */   private ConcurrentHashMap<String, ISession> memoryRepository = new ConcurrentHashMap();
/* 18 */   private IndexRepository indexRepository = IndexRepository.UserIDIndexRepository;
/*    */ 
/*    */   public void create(ISession session)
/*    */   {
/* 22 */     ISession ref_session = (ISession)this.memoryRepository.putIfAbsent(session.getID(), session);
/* 23 */     if (ref_session == null)
/* 24 */       this.indexRepository.create(session);
/*    */   }
/*    */ 
/*    */   public ISession expire(String key)
/*    */   {
/* 30 */     ISession ref_session = (ISession)this.memoryRepository.remove(key);
/* 31 */     if (ref_session != null) {
/* 32 */       this.indexRepository.expireBy(ref_session);
/*    */     }
/* 34 */     return ref_session;
/*    */   }
/*    */ 
/*    */   public void remove(String key) {
/* 38 */     expire(key);
/*    */   }
/*    */ 
/*    */   public void clear() {
/* 42 */     this.memoryRepository.clear();
/* 43 */     this.indexRepository.clear();
/*    */   }
/*    */ 
/*    */   public int getSize() {
/* 47 */     return this.memoryRepository.size();
/*    */   }
/*    */ 
/*    */   public ISession get(String key) {
/* 51 */     return (ISession)this.memoryRepository.get(key);
/*    */   }
/*    */ 
/*    */   public List<ISession> getBy(String handle, String key) {
/* 55 */     return this.indexRepository.getBy(key);
/*    */   }
/*    */   public Iterator<ISession> iterator() {
/* 58 */     return getAll().iterator();
/*    */   }
/*    */   public Collection<ISession> getAll() {
/* 61 */     Set sort_set = new TreeSet(new Comparator() {
/*    */       public int compare(ISession o1, ISession o2) {
/* 63 */         return (o1.getCreateTime() > o2.getCreateTime()) ? -1 : 1;
/*    */       }
/*    */     });
/* 66 */     sort_set.addAll(this.memoryRepository.values());
/* 67 */     return sort_set;
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.wession.repository.WessionRepository
 * JD-Core Version:    0.5.4
 */