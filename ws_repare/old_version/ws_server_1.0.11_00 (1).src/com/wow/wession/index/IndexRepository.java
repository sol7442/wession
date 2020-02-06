/*    */ package com.wow.wession.index;
/*    */ 
/*    */ import com.wow.wession.server.WessionServerSession;
/*    */ import com.wow.wession.session.ISession;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ public class IndexRepository
/*    */   implements IIndexableRepository
/*    */ {
/*    */   public static final String IDX_USERID = "UserID";
/* 13 */   public static IndexRepository UserIDIndexRepository = new IndexRepository("UserID", new IRepositoryIndexHandler() {
/*    */     public String getIndexKey(ISession session) {
/* 15 */       if (session instanceof WessionServerSession) {
/* 16 */         WessionServerSession srv_session = (WessionServerSession)session;
/* 17 */         return srv_session.getUser();
/*    */       }
/* 19 */       return null;
/*    */     }
/*    */   });
/*    */   private String name;
/* 24 */   private WessionMutilMap multimap = new WessionMutilMap();
/*    */   private IRepositoryIndexHandler handler;
/*    */ 
/*    */   public IndexRepository(String name, IRepositoryIndexHandler handler)
/*    */   {
/* 28 */     this.name = name;
/* 29 */     this.handler = handler;
/*    */   }
/*    */ 
/*    */   public void setName(String name) {
/* 33 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 37 */     return this.name;
/*    */   }
/*    */ 
/*    */   public IRepositoryIndexHandler getIndexHandler() {
/* 41 */     return this.handler;
/*    */   }
/*    */ 
/*    */   public void clear() {
/* 45 */     this.multimap.clear();
/*    */   }
/*    */ 
/*    */   public ISession[] getArray(int start, int end) {
/* 49 */     return null;
/*    */   }
/*    */ 
/*    */   public ISession[] getArray() {
/* 53 */     return this.multimap.getArray();
/*    */   }
/*    */   public Iterator<ISession> iterator() {
/* 56 */     return null;
/*    */   }
/*    */ 
/*    */   public void create(ISession value)
/*    */   {
/* 61 */     String key = this.handler.getIndexKey(value);
/* 62 */     if (key != null)
/* 63 */       this.multimap.put(this.handler.getIndexKey(value), value.getID(), value);
/*    */   }
/*    */ 
/*    */   public List<ISession> getBy(String key) {
/* 67 */     return this.multimap.get(key);
/*    */   }
/*    */ 
/*    */   public ISession expireBy(ISession value) {
/* 71 */     return this.multimap.remove(this.handler.getIndexKey(value), value.getID());
/*    */   }
/*    */   public void expire(String key) {
/*    */   }
/*    */   public void add(String parent, ISession session) {  }
/*    */ 
/*    */   public void remove(String parent, String key) {  }
/*    */ 
/*    */   public void set(String parent, String key, Object object) {  }
/*    */ 
/*    */   public void del(String parent, String key) {  }
/*    */ 
/* 79 */   public int getSize() { return 0; } 
/* 80 */   public ISession get(String key) { return null; }
/*    */ 
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.wession.index.IndexRepository
 * JD-Core Version:    0.5.4
 */