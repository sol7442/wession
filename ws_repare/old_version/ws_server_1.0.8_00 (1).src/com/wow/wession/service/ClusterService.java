/*     */ package com.wow.wession.service;
/*     */ 
/*     */ import com.wow.client.socket.SocketConnection;
/*     */ import com.wow.server.IAcceptor;
/*     */ import com.wow.server.IServiceHandler;
/*     */ import com.wow.server.IWorker;
/*     */ import com.wow.server.socket.SocketAcceptor;
/*     */ import com.wow.wession.cluster.ClusterEntity;
/*     */ import com.wow.wession.cluster.ClusterManager;
/*     */ import com.wow.wession.config.ClusterServerConfigDocument.ClusterServerConfig;
/*     */ import com.wow.wession.config.WSConfigurationManager;
/*     */ import com.wow.wession.config.WessionConfigurationDocument.WessionConfiguration;
/*     */ import com.wow.wession.data.AbstractEntity;
/*     */ import com.wow.wession.repository.IManagerState;
/*     */ import com.wow.wession.repository.IRepository;
/*     */ import com.wow.wession.service.impl.WessionRequest;
/*     */ import com.wow.wession.service.impl.WessionResponse;
/*     */ import com.wow.wession.session.ISession;
/*     */ import com.wow.wession.session.WessionSessionManager;
/*     */ import com.wow.wession.util.StopWatch;
/*     */ import java.io.IOException;
/*     */ import java.net.Socket;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class ClusterService
/*     */   implements IServiceHandler, IRepository, IManagerState
/*     */ {
/*  37 */   private Logger syslogger = LoggerFactory.getLogger("system");
/*     */   private String name;
/*     */   private IAcceptor acceptor;
/*  41 */   private BlockingQueue<Socket> queue = null;
/*  42 */   private Set<SocketConnection> connections = new HashSet();
/*     */ 
/*     */   public boolean initialize() {
/*  45 */     boolean bRes = false;
/*     */     try {
/*  47 */       ClusterServerConfigDocument.ClusterServerConfig cluster_config = WSConfigurationManager.getInstance().get().getClusterServerConfig();
/*     */ 
/*  49 */       this.syslogger.info("Cluster Server Configuration \n{}", WSConfigurationManager.getInstance().getText(cluster_config));
/*     */ 
/*  51 */       this.name = (cluster_config.getName() + " Cluster");
/*  52 */       this.acceptor = 
/*  57 */         new SocketAcceptor(this, 
/*  54 */         cluster_config.getPort(), 
/*  55 */         cluster_config.getMinThreadSize(), 
/*  56 */         cluster_config.getMaxThreadSize(), 
/*  57 */         cluster_config.getIdleMilliTime());
/*     */ 
/*  59 */       bRes = true;
/*     */     } catch (IOException e) {
/*  61 */       this.syslogger.error("ClusterService initialize fail", e);
/*     */     }
/*     */ 
/*  64 */     return bRes;
/*     */   }
/*     */ 
/*     */   public void close() {
/*     */     try {
/*  69 */       this.acceptor.close();
/*  70 */       Iterator iter = this.connections.iterator();
/*  71 */       while (iter.hasNext()) {
/*  72 */         ((SocketConnection)iter.next()).close();
/*  73 */         iter.remove();
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/*  77 */       this.syslogger.error("Service Close Error : {}", getName());
/*     */     }
/*  79 */     this.syslogger.info("Service Closed : {}", getName());
/*     */   }
/*     */   public IAcceptor getAcceptor() {
/*  82 */     return this.acceptor;
/*     */   }
/*     */ 
/*     */   public BlockingQueue<Socket> getSocketQueue() {
/*  86 */     if (this.queue == null) {
/*  87 */       this.queue = new LinkedBlockingQueue();
/*     */     }
/*  89 */     return this.queue;
/*     */   }
/*     */ 
/*     */   public IWorker getNewWorker() {
/*  93 */     return new ClusterWorker();
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  97 */     return this.name;
/*     */   }
/*     */ 
/*     */   private WessionResponse OnNodeRegister(AbstractEntity entity)
/*     */   {
/* 193 */     WessionResponse response = new WessionResponse();
/*     */     try {
/* 195 */       if (entity.getDataSize() == 1) {
/* 196 */         String node_name = (String)entity.getData(0);
/* 197 */         WessionSessionManager.getInstance().getClusterManager().register(node_name);
/* 198 */         response.setResultCode(1);
/*     */       } else {
/* 200 */         response.setResultCode(301);
/*     */       }
/*     */     } catch (Exception e) {
/* 203 */       response.setResultCode(500);
/*     */     }
/* 205 */     return response;
/*     */   }
/*     */ 
/*     */   private WessionResponse setProperty(ClusterEntity entity)
/*     */   {
/* 211 */     WessionResponse response = new WessionResponse();
/* 212 */     if ((entity != null) && (entity.getDataSize() > 1)) {
/* 213 */       String key = (String)entity.getData(0);
/* 214 */       String value = (String)entity.getData(1);
/* 215 */       setProperty(key, value);
/* 216 */       response.setResultCode(1);
/*     */     } else {
/* 218 */       response.setResultCode(301);
/*     */     }
/* 220 */     return response;
/*     */   }
/*     */   public void setProperty(String key, String value) {
/* 223 */     WessionSessionManager.getInstance().setProperty(key, value, false);
/*     */   }
/*     */ 
/*     */   private WessionResponse create(ClusterEntity entity)
/*     */   {
/* 228 */     WessionResponse response = new WessionResponse();
/* 229 */     if ((entity != null) && (entity.getDataSize() == 1)) {
/* 230 */       create((ISession)entity.getData(0));
/* 231 */       response.setResultCode(1);
/*     */     } else {
/* 233 */       response.setResultCode(301);
/*     */     }
/* 235 */     return response;
/*     */   }
/*     */   public void create(ISession session) {
/* 238 */     WessionSessionManager.getInstance().create(session, false);
/*     */   }
/*     */ 
/*     */   private WessionResponse expire(ClusterEntity entity)
/*     */   {
/* 243 */     WessionResponse response = new WessionResponse();
/* 244 */     if ((entity != null) && (entity.getDataSize() == 1)) {
/* 245 */       expire((String)entity.getData(0));
/* 246 */       response.setResultCode(1);
/*     */     } else {
/* 248 */       response.setResultCode(301);
/*     */     }
/* 250 */     return response;
/*     */   }
/*     */   public void expire(String key) {
/* 253 */     WessionSessionManager.getInstance().expire(key, false);
/*     */   }
/*     */ 
/*     */   private WessionResponse add(ClusterEntity entity) {
/* 257 */     WessionResponse response = new WessionResponse();
/* 258 */     if ((entity != null) && (entity.getDataSize() == 2)) {
/* 259 */       String parent = (String)entity.getData(0);
/* 260 */       ISession session = (ISession)entity.getData(1);
/* 261 */       add(parent, session);
/* 262 */       response.setResultCode(1);
/*     */     } else {
/* 264 */       response.setResultCode(301);
/*     */     }
/* 266 */     return response;
/*     */   }
/*     */   public void add(String parent, ISession session) {
/* 269 */     WessionSessionManager.getInstance().add(parent, session);
/*     */   }
/*     */   private WessionResponse remove(ClusterEntity entity) {
/* 272 */     WessionResponse response = new WessionResponse();
/* 273 */     if ((entity != null) && (entity.getDataSize() == 2)) {
/* 274 */       String parent = (String)entity.getData(0);
/* 275 */       String key = (String)entity.getData(1);
/* 276 */       remove(parent, key);
/* 277 */       response.setResultCode(1);
/*     */     } else {
/* 279 */       response.setResultCode(301);
/*     */     }
/* 281 */     return response;
/*     */   }
/*     */   public void remove(String parent, String key) {
/* 284 */     WessionSessionManager.getInstance().remove(parent, key);
/*     */   }
/*     */ 
/*     */   private WessionResponse set(ClusterEntity entity) {
/* 288 */     WessionResponse response = new WessionResponse();
/* 289 */     if ((entity != null) && (entity.getDataSize() == 3)) {
/* 290 */       String parent = (String)entity.getData(0);
/* 291 */       String key = (String)entity.getData(1);
/* 292 */       Object object = entity.getData(2);
/* 293 */       set(parent, key, object);
/* 294 */       response.setResultCode(1);
/*     */     } else {
/* 296 */       response.setResultCode(301);
/*     */     }
/* 298 */     return response;
/*     */   }
/*     */ 
/*     */   public void set(String parent, String key, Object object) {
/* 302 */     WessionSessionManager.getInstance().set(parent, key, object);
/*     */   }
/*     */ 
/*     */   private WessionResponse delete(ClusterEntity entity) {
/* 306 */     WessionResponse response = new WessionResponse();
/* 307 */     if ((entity != null) && (entity.getDataSize() == 2)) {
/* 308 */       String parent = (String)entity.getData(0);
/* 309 */       String key = (String)entity.getData(1);
/* 310 */       delete(parent, key);
/* 311 */       response.setResultCode(1);
/*     */     } else {
/* 313 */       response.setResultCode(301);
/*     */     }
/* 315 */     return response;
/*     */   }
/*     */   public void delete(String parent, String key) {
/* 318 */     WessionSessionManager.getInstance().delete(parent, key);
/*     */   }
/*     */   private WessionResponse getAll(ClusterEntity entity) {
/* 321 */     WessionResponse response = new WessionResponse();
/* 322 */     if ((entity != null) && (entity.getDataSize() == 0)) {
/* 323 */       Collection all_session = WessionSessionManager.getInstance().getAll();
/* 324 */       ClusterEntity res_entiry = new ClusterEntity(entity.getCommand());
/* 325 */       for (ISession session : all_session) {
/* 326 */         res_entiry.addData(session);
/*     */       }
/* 328 */       this.syslogger.debug("getall : {}", Integer.valueOf(res_entiry.getDataSize()));
/* 329 */       response.setData(res_entiry);
/* 330 */       response.setResultCode(1);
/*     */     } else {
/* 332 */       response.setResultCode(301);
/*     */     }
/* 334 */     return response;
/*     */   }
/*     */ 
/*     */   private WessionResponse clear(ClusterEntity entity) {
/* 338 */     WessionResponse response = new WessionResponse();
/* 339 */     if ((entity != null) && (entity.getDataSize() == 0)) {
/* 340 */       clear();
/* 341 */       response.setResultCode(1);
/*     */     } else {
/* 343 */       response.setResultCode(301);
/*     */     }
/* 345 */     return response;
/*     */   }
/*     */ 
/*     */   public void clear() {
/* 349 */     WessionSessionManager.getInstance().clear(false);
/*     */   }
/*     */ 
/*     */   public class ClusterWorker
/*     */     implements IWorker
/*     */   {
/*     */     public ClusterWorker()
/*     */     {
/*     */     }
/*     */ 
/*     */     public String getWorkerName()
/*     */     {
/* 103 */       return "ClusterWorker";
/*     */     }
/*     */     public void run() {
/* 106 */       SocketConnection connection = null;
/*     */       try {
/* 108 */         connection = new SocketConnection((Socket)ClusterService.this.queue.take());
/* 109 */         StopWatch watch = new StopWatch(TimeUnit.NANOSECONDS);
/*     */ 
/* 111 */         ClusterService.this.connections.add(connection);
/*     */         do {
/* 113 */           WessionRequest request = null;
/* 114 */           WessionResponse response = null;
/*     */           try {
/* 116 */             request = (WessionRequest)connection.readRequest();
/* 117 */             ClusterEntity entity = (ClusterEntity)request.getData();
/* 118 */             watch.reset();
/*     */ 
/* 120 */             connection.setKeepAlive(true);
/* 121 */             switch (entity.getCommand())
/*     */             {
/*     */             case 1:
/* 123 */               response = ClusterService.this.create(entity);
/* 124 */               break;
/*     */             case 2:
/* 126 */               response = ClusterService.this.expire(entity);
/* 127 */               break;
/*     */             case 10:
/* 129 */               response = ClusterService.this.add(entity);
/* 130 */               break;
/*     */             case 11:
/* 132 */               response = ClusterService.this.remove(entity);
/* 133 */               break;
/*     */             case 21:
/* 135 */               response = ClusterService.this.set(entity);
/* 136 */               break;
/*     */             case 22:
/* 138 */               response = ClusterService.this.delete(entity);
/* 139 */               break;
/*     */             case 30:
/* 141 */               response = ClusterService.this.getAll(entity);
/* 142 */               break;
/*     */             case 99:
/* 144 */               response = ClusterService.this.clear(entity);
/* 145 */               break;
/*     */             case 104:
/* 147 */               response = ClusterService.this.setProperty(entity);
/* 148 */               break;
/*     */             case 101:
/*     */             case 103:
/* 151 */               response = ClusterService.this.OnNodeRegister(entity);
/* 152 */               label480: break label480:
/*     */             case 102:
/*     */             }
/*     */ 
/*     */           }
/*     */           catch (ClassNotFoundException e)
/*     */           {
/* 161 */             ClusterService.this.syslogger.error("WessionRequest Read Error : {}", e);
/* 162 */             connection.setKeepAlive(false);
/*     */           } finally {
/* 164 */             if (request != null) {
/* 165 */               ClusterService.this.syslogger.debug("{} - {} : {}", new Object[] { request.getData(), response, Long.valueOf(watch.stop()) });
/* 166 */               connection.writeResponse(response);
/*     */             } else {
/* 168 */               connection.close();
/*     */             }
/*     */           }
/* 171 */           label552: if (!connection.isKeepAlive()) break label552; 
/* 171 */         }while (connection.isConnected());
/*     */       }
/*     */       catch (IOException e) {
/* 174 */         ClusterService.this.syslogger.error("ServerSocket Connection Error : {}", e);
/*     */       } catch (InterruptedException e) {
/* 176 */         ClusterService.this.syslogger.error("ServerSocket Connection Error : {}", e);
/*     */       } finally {
/* 178 */         ClusterService.this.syslogger.error("ServerSocket Close : {}", connection);
/* 179 */         if (connection != null)
/*     */           try {
/* 181 */             ClusterService.this.connections.remove(connection);
/* 182 */             connection.close();
/*     */           } catch (Exception e) {
/* 184 */             ClusterService.this.syslogger.error("ServerSocket Connection Error : {}", e);
/*     */           }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.service.ClusterService
 * JD-Core Version:    0.5.4
 */