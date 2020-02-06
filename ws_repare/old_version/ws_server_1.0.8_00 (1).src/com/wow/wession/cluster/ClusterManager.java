/*     */ package com.wow.wession.cluster;
/*     */ 
/*     */ import com.wow.wession.config.ClusterManagerConfigDocument.ClusterManagerConfig;
/*     */ import com.wow.wession.config.ClusterServerConfigDocument.ClusterServerConfig;
/*     */ import com.wow.wession.config.ClusterServerConfigDocument.ClusterServerConfig.ClusterNodeConfig;
/*     */ import com.wow.wession.config.WSConfigurationManager;
/*     */ import com.wow.wession.config.WessionConfigurationDocument.WessionConfiguration;
/*     */ import com.wow.wession.repository.IManagerState;
/*     */ import com.wow.wession.repository.IRepository;
/*     */ import com.wow.wession.service.impl.WessionResponse;
/*     */ import com.wow.wession.session.ISession;
/*     */ import com.wow.wession.util.StopWatch;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class ClusterManager
/*     */   implements IRepository, IManagerState
/*     */ {
/*  34 */   private Logger syslogger = LoggerFactory.getLogger("system");
/*  35 */   private Logger weslogger = LoggerFactory.getLogger("wession");
/*     */ 
/*  37 */   private ConcurrentHashMap<String, ClusterNode> clusterNodes = new ConcurrentHashMap();
/*     */   private String serviceName;
/*  39 */   private int checkStartTime = 1000;
/*  40 */   private int checkPeriodTime = 1000;
/*     */ 
/*  42 */   private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
/*     */ 
/*     */   public boolean initialize(String service_name, ClusterManagerConfigDocument.ClusterManagerConfig mgr_conf, ClusterServerConfigDocument.ClusterServerConfig server_conf) {
/*  45 */     this.syslogger.info("******** Cluster Manager Node Regist ********");
/*     */ 
/*  47 */     boolean bRes = false;
/*     */     try {
/*  49 */       this.serviceName = service_name;
/*     */ 
/*  51 */       ClusterServerConfigDocument.ClusterServerConfig.ClusterNodeConfig[] cluster_node_array = server_conf.getClusterNodeConfigArray();
/*  52 */       for (int i = 0; i < cluster_node_array.length; ++i) {
/*  53 */         ClusterNode node = new ClusterNode();
/*  54 */         node.initialize(server_conf.getName(), 
/*  55 */           cluster_node_array[i].getName(), 
/*  56 */           cluster_node_array[i].getAddress(), 
/*  57 */           cluster_node_array[i].getPort());
/*  58 */         register(node, true);
/*     */       }
/*  60 */       this.syslogger.info("******** Cluster Manager Node Regist ********");
/*     */ 
/*  62 */       this.checkPeriodTime = mgr_conf.getCheckPeriodTime();
/*  63 */       this.checkStartTime = mgr_conf.getCheckStartTime();
/*  64 */       bRes = true;
/*     */     } catch (Exception e) {
/*  66 */       this.syslogger.error("initialize fail {}", e);
/*     */     } finally {
/*  68 */       this.scheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
/*     */         public Thread newThread(Runnable r) {
/*  70 */           return new Thread(r, "Cluster NodeCheck Thread");
/*     */         }
/*     */       });
/*     */     }
/*  74 */     return bRes;
/*     */   }
/*     */ 
/*     */   public boolean register(String node_name) {
/*  78 */     return register(findClusterNode(node_name), false);
/*     */   }
/*     */   public boolean register(ClusterNode node, boolean send) {
/*  81 */     boolean result = false;
/*  82 */     if (send) {
/*  83 */       ClusterEntity entity = new ClusterEntity(101);
/*  84 */       entity.addData(this.serviceName);
/*  85 */       WessionResponse res = node.send(entity);
/*  86 */       if ((res != null) && (res.getResultCode() == 1))
/*  87 */         result = true;
/*     */     }
/*     */     else {
/*  90 */       result = true;
/*     */     }
/*     */ 
/*  93 */     if (result) {
/*  94 */       ClusterNode ref_node = (ClusterNode)this.clusterNodes.putIfAbsent(node.getReceiverName(), node);
/*  95 */       if (ref_node == null)
/*  96 */         this.syslogger.info("cluster node registry success : {}-{}", this.serviceName, node.getReceiverName());
/*     */     }
/*     */     else {
/*  99 */       this.syslogger.info("cluster node registry failed   : {}-{}", this.serviceName, node.getReceiverName());
/*     */     }
/*     */ 
/* 102 */     return result;
/*     */   }
/*     */   public void unRegister(String name) {
/* 105 */     this.clusterNodes.remove(name);
/*     */   }
/*     */ 
/*     */   private ClusterNode findClusterNode(String nodeName) {
/* 109 */     ClusterNode node = null;
/*     */     try {
/* 111 */       ClusterServerConfigDocument.ClusterServerConfig cluster_config = WSConfigurationManager.getInstance().get().getClusterServerConfig();
/* 112 */       ClusterServerConfigDocument.ClusterServerConfig.ClusterNodeConfig[] cluster_node_array = cluster_config.getClusterNodeConfigArray();
/*     */ 
/* 114 */       for (int i = 0; i < cluster_node_array.length; ++i) {
/* 115 */         if (nodeName.equals(cluster_node_array[i].getName())) {
/* 116 */           node = new ClusterNode();
/* 117 */           node.initialize(cluster_config.getName(), 
/* 118 */             cluster_node_array[i].getName(), 
/* 119 */             cluster_node_array[i].getAddress(), 
/* 120 */             cluster_node_array[i].getPort());
/* 121 */           node.setState(0);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 127 */       this.syslogger.error("cluster node register Exception : ", e);
/*     */     }
/*     */ 
/* 130 */     return node;
/*     */   }
/*     */ 
/*     */   public boolean checkNode(ClusterNode node) {
/* 134 */     ClusterEntity entity = new ClusterEntity(103);
/* 135 */     entity.addData(this.serviceName);
/* 136 */     WessionResponse res = node.send(entity);
/*     */ 
/* 138 */     return (res != null) && (res.getResultCode() == 1);
/*     */   }
/*     */ 
/*     */   public Collection<ISession> getAll()
/*     */   {
/* 144 */     Collection all_session = new TreeSet();
/* 145 */     Iterator node_iter = this.clusterNodes.keySet().iterator();
/* 146 */     if (node_iter.hasNext()) {
/* 147 */       ClusterNode node = (ClusterNode)this.clusterNodes.get(node_iter.next());
/* 148 */       ClusterEntity entity = new ClusterEntity(30);
/* 149 */       WessionResponse res = node.send(entity);
/* 150 */       if ((res != null) && (res.getResultCode() == 1)) {
/* 151 */         ClusterEntity res_entity = (ClusterEntity)res.getData();
/* 152 */         for (int i = 0; i < res_entity.getDataSize(); ++i) {
/* 153 */           all_session.add((ISession)res_entity.getData(i));
/*     */         }
/* 155 */         this.syslogger.info("getAll-{} ", Integer.valueOf(res_entity.getDataSize()));
/*     */       }
/*     */     }
/* 158 */     return all_session;
/*     */   }
/*     */ 
/*     */   public void start() {
/* 162 */     this.scheduler.scheduleAtFixedRate(new Runnable()
/*     */     {
/*     */       public void run() {
/*     */         try {
/* 166 */           Iterator cluster_node_iter = ClusterManager.this.clusterNodes.values().iterator();
/* 167 */           while (cluster_node_iter.hasNext()) {
/* 168 */             ClusterNode node = (ClusterNode)cluster_node_iter.next();
/* 169 */             if (!ClusterManager.this.checkNode(node)) {
/* 170 */               cluster_node_iter.remove();
/* 171 */               node.close();
/* 172 */               ClusterManager.this.syslogger.info("cluster node unregistry  {} - {}", ClusterManager.this.serviceName, node.getReceiverName());
/*     */             }
/*     */           }
/*     */         } catch (Exception e) {
/* 176 */           ClusterManager.this.syslogger.debug("check {} - {}", ClusterManager.this.serviceName, e);
/*     */         }
/*     */       }
/*     */     }
/*     */     , this.checkStartTime, this.checkPeriodTime, TimeUnit.MILLISECONDS);
/*     */ 
/* 181 */     this.syslogger.info("Cluster Manager Star *************");
/*     */   }
/*     */   public void stop() {
/* 184 */     this.scheduler.shutdown();
/* 185 */     Iterator iter = this.clusterNodes.values().iterator();
/* 186 */     while (iter.hasNext()) {
/* 187 */       ClusterNode node = (ClusterNode)iter.next();
/* 188 */       node.close();
/* 189 */       iter.remove();
/*     */     }
/* 191 */     this.syslogger.info("Cluster Manager Stop ************");
/*     */   }
/*     */ 
/*     */   private List<ClusterNode> getNodeList() {
/* 195 */     List array_list = new ArrayList();
/* 196 */     array_list.addAll(this.clusterNodes.values());
/* 197 */     return array_list;
/*     */   }
/*     */ 
/*     */   public void setProperty(String key, String value) {
/* 201 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/*     */ 
/* 203 */     ClusterEntity entity = new ClusterEntity(104);
/* 204 */     entity.addData(key);
/* 205 */     entity.addData(value);
/*     */ 
/* 207 */     List cluster_list = getNodeList();
/* 208 */     for (ClusterNode node : cluster_list) {
/* 209 */       node.send(entity);
/*     */     }
/*     */ 
/* 212 */     this.weslogger.debug("setProperty : key[{}], value[{}]-{} ({})", new Object[] { key, value, Long.valueOf(watch.stop()), Integer.valueOf(cluster_list.size()) });
/*     */   }
/*     */   public void create(ISession session) {
/* 215 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/* 216 */     ClusterEntity entity = new ClusterEntity(1);
/* 217 */     entity.addData(session);
/*     */ 
/* 219 */     List cluster_list = getNodeList();
/* 220 */     for (ClusterNode node : cluster_list) {
/* 221 */       node.send(entity);
/*     */     }
/* 223 */     this.weslogger.debug("create : session[{}]-{} ({}) ", new Object[] { session, Long.valueOf(watch.stop()), Integer.valueOf(cluster_list.size()) });
/*     */   }
/*     */   public void expire(String key) {
/* 226 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/*     */ 
/* 228 */     ClusterEntity entity = new ClusterEntity(2);
/* 229 */     entity.addData(key);
/*     */ 
/* 231 */     List cluster_list = getNodeList();
/* 232 */     for (ClusterNode node : cluster_list) {
/* 233 */       node.send(entity);
/*     */     }
/* 235 */     this.weslogger.debug("expire : key[{}]-{} ({})", new Object[] { key, Long.valueOf(watch.stop()), Integer.valueOf(cluster_list.size()) });
/*     */   }
/*     */   public void add(String parent, ISession session) {
/* 238 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/*     */ 
/* 240 */     ClusterEntity entity = new ClusterEntity(10);
/* 241 */     entity.addData(parent);
/* 242 */     entity.addData(session);
/*     */ 
/* 244 */     List cluster_list = getNodeList();
/* 245 */     for (ClusterNode node : cluster_list) {
/* 246 */       node.send(entity);
/*     */     }
/* 248 */     this.weslogger.debug("add : parent[{}],session[{}]-{} ({})", new Object[] { parent, session, Long.valueOf(watch.stop()), Integer.valueOf(cluster_list.size()) });
/*     */   }
/*     */   public void remove(String parent, String key) {
/* 251 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/*     */ 
/* 253 */     ClusterEntity entity = new ClusterEntity(11);
/* 254 */     entity.addData(parent);
/* 255 */     entity.addData(key);
/*     */ 
/* 257 */     List cluster_list = getNodeList();
/* 258 */     for (ClusterNode node : cluster_list) {
/* 259 */       node.send(entity);
/*     */     }
/* 261 */     this.weslogger.debug("remove : parent[{}],key[{}]-{} ({})", new Object[] { parent, key, Long.valueOf(watch.stop()), Integer.valueOf(cluster_list.size()) });
/*     */   }
/*     */ 
/*     */   public void set(String parent, String key, Object object) {
/* 265 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/*     */ 
/* 267 */     ClusterEntity entity = new ClusterEntity(21);
/* 268 */     entity.addData(parent);
/* 269 */     entity.addData(key);
/* 270 */     entity.addData(object);
/*     */ 
/* 272 */     List cluster_list = getNodeList();
/* 273 */     for (ClusterNode node : cluster_list) {
/* 274 */       node.send(entity);
/*     */     }
/* 276 */     this.weslogger.debug("set : parent[{}],key[{}],object[{}]-{} ({})", new Object[] { parent, key, object, Long.valueOf(watch.stop()), Integer.valueOf(cluster_list.size()) });
/*     */   }
/*     */ 
/*     */   public void delete(String parent, String key) {
/* 280 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/*     */ 
/* 282 */     ClusterEntity entity = new ClusterEntity(22);
/* 283 */     entity.addData(parent);
/* 284 */     entity.addData(key);
/*     */ 
/* 286 */     List cluster_list = getNodeList();
/* 287 */     for (ClusterNode node : cluster_list) {
/* 288 */       node.send(entity);
/*     */     }
/* 290 */     this.weslogger.debug("delete : parent[{}],key[{}]-{} ({})", new Object[] { parent, key, Long.valueOf(watch.stop()), Integer.valueOf(cluster_list.size()) });
/*     */   }
/*     */ 
/*     */   public void clear() {
/* 294 */     StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/*     */ 
/* 296 */     ClusterEntity entity = new ClusterEntity(99);
/*     */ 
/* 298 */     List cluster_list = getNodeList();
/* 299 */     for (ClusterNode node : cluster_list) {
/* 300 */       node.send(entity);
/*     */     }
/* 302 */     this.weslogger.debug("clear:-{} ({})", Long.valueOf(watch.stop()), Integer.valueOf(cluster_list.size()));
/*     */   }
/*     */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.cluster.ClusterManager
 * JD-Core Version:    0.5.4
 */