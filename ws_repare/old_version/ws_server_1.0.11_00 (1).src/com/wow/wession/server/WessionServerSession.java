/*    */ package com.wow.wession.server;
/*    */ 
/*    */ import com.wow.wession.session.ISession;
/*    */ import com.wow.wession.session.WessionBaseSession;
/*    */ import com.wow.wession.session.WessionSessionManager;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ 
/*    */ public class WessionServerSession extends WessionBaseSession
/*    */ {
/*    */   private static final long serialVersionUID = -4279165365119671062L;
/* 17 */   private ConcurrentMap<String, ISession> agents = new ConcurrentHashMap();
/*    */ 
/*    */   public WessionServerSession(String id, String user) {
/* 20 */     super(id, user);
/*    */   }
/*    */ 
/*    */   public void setAttribute(String key, Object value) {
/* 24 */     setAttribute(key, value, true);
/*    */   }
/*    */ 
/*    */   public void setAttribute(String key, Object value, boolean cluster) {
/* 28 */     super.setAttribute(key, value);
/* 29 */     if (WessionSessionManager.getInstance().get(getID()) != null)
/* 30 */       WessionSessionManager.getInstance().set(getID(), key, value, cluster);
/*    */   }
/*    */ 
/*    */   public Object removeAttribute(String key) {
/* 34 */     return removeAttribute(key, true);
/*    */   }
/*    */   public Object removeAttribute(String key, boolean cluster) {
/* 37 */     Object object = super.removeAttribute(key);
/* 38 */     if (WessionSessionManager.getInstance().get(getID()) != null) {
/* 39 */       WessionSessionManager.getInstance().delete(getID(), key, cluster);
/*    */     }
/* 41 */     return object;
/*    */   }
/*    */ 
/*    */   public void addAgentSession(ISession session) {
/* 45 */     addAgentSession(session, true);
/*    */   }
/*    */   public void removeAgentSession(String key) {
/* 48 */     removeAgentSession(key, true);
/*    */   }
/*    */ 
/*    */   public void addAgentSession(ISession session, boolean cluster) {
/* 52 */     this.agents.put(session.getID(), session);
/* 53 */     if (WessionSessionManager.getInstance().get(getID()) != null)
/* 54 */       WessionSessionManager.getInstance().add(getID(), session, cluster);
/*    */   }
/*    */ 
/*    */   public void removeAgentSession(String key, boolean cluster)
/*    */   {
/* 59 */     this.agents.remove(key);
/* 60 */     if (WessionSessionManager.getInstance().get(getID()) != null)
/* 61 */       WessionSessionManager.getInstance().remove(getID(), key, cluster);
/*    */   }
/*    */ 
/*    */   public Set<String> getAgentSessionKeySet()
/*    */   {
/* 66 */     return this.agents.keySet();
/*    */   }
/*    */   public List<ISession> getAgentSessionList() {
/* 69 */     List item_list = new ArrayList();
/* 70 */     item_list.addAll(this.agents.values());
/* 71 */     return item_list;
/*    */   }
/*    */ 
/*    */   public ISession getAgentSession(String key) {
/* 75 */     return (ISession)this.agents.get(key);
/*    */   }
/*    */   public int getAgentSize() {
/* 78 */     return this.agents.size();
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 82 */     StringBuffer buffer = new StringBuffer();
/* 83 */     buffer.append(super.toString());
/* 84 */     synchronized (this.agents) {
/* 85 */       Iterator keys = this.agents.keySet().iterator();
/* 86 */       while (keys.hasNext()) {
/* 87 */         buffer.append("[").append(((ISession)this.agents.get(keys.next())).toString()).append("]");
/*    */       }
/*    */     }
/* 90 */     return buffer.toString();
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.wession.server.WessionServerSession
 * JD-Core Version:    0.5.4
 */