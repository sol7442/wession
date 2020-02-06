/*     */ package com.wow.wession.agent;
/*     */ 
/*     */ import com.wow.wession.session.ISessionMessage;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.methods.HttpPost;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.entity.SerializableEntity;
/*     */ import org.apache.http.impl.client.HttpClientBuilder;
/*     */ import org.apache.http.impl.client.HttpClients;
/*     */ import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
/*     */ 
/*     */ public class WessionTranser
/*     */ {
/*     */   private HttpClient httpClient;
/*  33 */   private PoolingHttpClientConnectionManager connection_mgr = new PoolingHttpClientConnectionManager();
/*     */   private ExecutorService excutors;
/*  36 */   private AtomicBoolean enable = new AtomicBoolean(true);
/*     */ 
/*     */   public ISessionMessage trans(String url, ISessionMessage message)
/*     */     throws TransException
/*     */   {
/*  47 */     if (!this.enable.get()) {
/*  48 */       throw new TransException("Object Enable false", TransException.ENABLE_FALSE);
/*     */     }
/*     */ 
/*  51 */     WessionAgentMessage res = null;
/*  52 */     HttpPost post = new HttpPost(url);
/*     */     SerializableEntity req_entity;
/*     */     try {
/*  55 */       SerializableEntity req_entity = new SerializableEntity(message, true);
/*  56 */       post.setEntity(req_entity);
/*  57 */       HttpResponse response = this.httpClient.execute(post, HttpClientContext.create());
/*     */ 
/*  59 */       HttpEntity res_entity = response.getEntity();
/*  60 */       long conents_length = res_entity.getContentLength();
/*  61 */       byte[] buffer = new byte[10240];
/*  62 */       int read_length = res_entity.getContent().read(buffer);
/*  63 */       int read_index = 0;
/*  64 */       while (buffer[read_index] == 10) {
/*  65 */         ++read_index;
/*  66 */         if (read_index > read_length) {
/*  67 */           throw new TransException("RESPONSE CONTENTS ERROR [" + conents_length + "][" + read_index + "]", TransException.PARSER_ERROR);
/*     */         }
/*     */       }
/*     */ 
/*  71 */       if (read_index > 0) {
/*  72 */         System.out.println("ws out : invalid stream header 0a0a0a0a readObject bug fixed [" + conents_length + "][" + read_length + "]");
/*     */       }
/*     */ 
/*  75 */       int contents_length = read_length - read_index;
/*  76 */       byte[] contents_buffer = new byte[contents_length];
/*  77 */       System.arraycopy(buffer, read_index, contents_buffer, 0, contents_length);
/*     */ 
/*  79 */       ByteArrayInputStream bis = new ByteArrayInputStream(contents_buffer);
/*  80 */       ObjectInputStream oi_stream = new ObjectInputStream(bis);
/*  81 */       res = (WessionAgentMessage)oi_stream.readObject();
/*     */     } catch (IOException e) {
/*  83 */       TransException ex = new TransException(e, TransException.NETWORK_ERROR);
/*  84 */       ex.setUrl(url, message);
/*  85 */       throw ex;
/*     */     } catch (ClassNotFoundException e) {
/*  87 */       TransException ex = new TransException(e, TransException.PARAMETER_MISS);
/*  88 */       ex.setUrl(url, message);
/*  89 */       throw ex;
/*     */     } finally {
/*  91 */       post.releaseConnection();
/*     */     }
/*  93 */     return res;
/*     */   }
/*     */ 
/*     */   public ISessionMessage trans(String url, ISessionMessage message, long timeout) throws TransException, InterruptedException, ExecutionException, TimeoutException {
/*  97 */     if (!this.enable.get()) {
/*  98 */       throw new TransException("Object Enable false", TransException.ENABLE_FALSE);
/*     */     }
/* 100 */     Future future = this.excutors.submit(new Callable(url, message) {
/*     */       public ISessionMessage call() throws Exception {
/* 102 */         return WessionTranser.this.trans(this.val$url, this.val$message);
/*     */       }
/*     */     });
/* 105 */     return (ISessionMessage)future.get(timeout, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */   public HttpResponse trans(HttpPost post)
/*     */     throws TransException
/*     */   {
/* 115 */     if (!this.enable.get()) {
/* 116 */       throw new TransException("Object Enable false", TransException.ENABLE_FALSE);
/*     */     }
/*     */ 
/* 119 */     HttpResponse response = null;
/*     */     try {
/* 121 */       response = this.httpClient.execute(post, HttpClientContext.create());
/*     */     } catch (IOException e) {
/* 123 */       TransException ex = new TransException(e, TransException.NETWORK_ERROR);
/* 124 */       throw ex;
/*     */     } finally {
/* 126 */       post.releaseConnection();
/*     */     }
/* 128 */     return response;
/*     */   }
/*     */   public void initialize(int connection_max_size) {
/* 131 */     this.connection_mgr.setMaxTotal(connection_max_size * 2);
/* 132 */     this.httpClient = HttpClients.custom().setConnectionManager(this.connection_mgr).build();
/* 133 */     this.excutors = new ThreadPoolExecutor(connection_max_size, connection_max_size, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), new TransThreadFactory(null));
/*     */   }
/*     */ 
/*     */   public void stop()
/*     */   {
/* 144 */     this.excutors.shutdown();
/*     */   }
/*     */ 
/*     */   public boolean isEnable() {
/* 148 */     return this.enable.get();
/*     */   }
/*     */   public void setEnable(boolean enable) {
/* 151 */     this.enable.set(enable);
/*     */   }
/*     */ 
/*     */   private class TransThreadFactory
/*     */     implements ThreadFactory
/*     */   {
/*     */     private TransThreadFactory()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Thread newThread(Runnable r)
/*     */     {
/* 138 */       Thread thread = new Thread(r);
/* 139 */       thread.setName("Trans Worker Thread");
/* 140 */       return thread;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.wession.agent.WessionTranser
 * JD-Core Version:    0.5.4
 */