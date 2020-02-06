/*     */ package com.wow.wession.agent;
/*     */ 
/*     */ import com.wow.wession.session.ISessionMessage;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.concurrent.ArrayBlockingQueue;
/*     */ import java.util.concurrent.BlockingQueue;
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
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.entity.SerializableEntity;
/*     */ import org.apache.http.impl.client.DefaultHttpClient;
/*     */ import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
/*     */ 
/*     */ public class WessionTranser
/*     */ {
/*     */   private ExecutorService excutors;
/*  33 */   private BlockingQueue<HttpClient> httpclientpool = null;
/*  34 */   private AtomicBoolean enable = new AtomicBoolean(true);
/*     */ 
/* 188 */   protected static final char[] hexArray = "0123456789ABCDEF".toCharArray();
/*     */ 
/*     */   public ISessionMessage trans(String url, ISessionMessage message)
/*     */     throws TransException
/*     */   {
/*  46 */     if (!this.enable.get()) {
/*  47 */       throw new TransException("Object Enable false", TransException.ENABLE_FALSE);
/*     */     }
/*     */ 
/*  50 */     WessionAgentMessage res = null;
/*  51 */     HttpClient http_client = null;
/*  52 */     HttpPost post = new HttpPost(url);
/*  53 */     HttpResponse response = null;
/*     */     SerializableEntity req_entity;
/*     */     try {
/*  56 */       SerializableEntity req_entity = new SerializableEntity(message, true);
/*  57 */       post.setEntity(req_entity);
/*  58 */       http_client = getHttpClient();
/*  59 */       response = http_client.execute(post);
/*  60 */       HttpEntity res_entity = response.getEntity();
/*  61 */       long conents_length = res_entity.getContentLength();
/*  62 */       byte[] buffer = new byte[10240];
/*  63 */       int read_length = res_entity.getContent().read(buffer);
/*  64 */       int read_index = 0;
/*  65 */       while (buffer[read_index] == 10) {
/*  66 */         ++read_index;
/*  67 */         if (read_index > read_length) {
/*  68 */           throw new TransException("RESPONSE CONTENTS ERROR [" + conents_length + "][" + read_index + "]", TransException.PARSER_ERROR);
/*     */         }
/*     */       }
/*     */ 
/*  72 */       if (read_index > 0) {
/*  73 */         System.out.println("ws out : invalid stream header 0a0a0a0a readObject bug fixed [" + conents_length + "][" + read_length + "]");
/*     */       }
/*     */ 
/*  77 */       int close_byte = 0;
/*  78 */       if (buffer[read_length] != 13) close_byte = 1;
/*     */ 
/*  80 */       int contents_length = read_length - read_index + close_byte;
/*  81 */       byte[] contents_buffer = new byte[contents_length];
/*  82 */       System.arraycopy(buffer, read_index, contents_buffer, 0, contents_length);
/*     */ 
/*  84 */       if (close_byte > 0) contents_buffer[contents_length] = 13;
/*     */ 
/*  86 */       ByteArrayInputStream bis = new ByteArrayInputStream(contents_buffer);
/*  87 */       ObjectInputStream oi_stream = new ObjectInputStream(bis);
/*  88 */       res = (WessionAgentMessage)oi_stream.readObject();
/*     */     } catch (IOException e) {
/*  90 */       TransException ex = new TransException(e, TransException.NETWORK_ERROR);
/*  91 */       ex.setUrl(url, message);
/*  92 */       throw ex;
/*     */     } catch (ClassNotFoundException e) {
/*  94 */       TransException ex = new TransException(e, TransException.PARAMETER_MISS);
/*  95 */       ex.setUrl(url, message);
/*  96 */       throw ex;
/*     */     } finally {
/*  98 */       if (response != null) {
/*     */         try {
/* 100 */           response.getEntity().consumeContent();
/*     */         } catch (IOException e) {
/* 102 */           e.printStackTrace();
/*     */         }
/*     */       }
/* 105 */       if (http_client != null)
/* 106 */         setHttpClient(http_client);
/*     */     }
/*     */     SerializableEntity req_entity;
/* 110 */     return res;
/*     */   }
/*     */ 
/*     */   private void setHttpClient(HttpClient client) {
/* 114 */     this.httpclientpool.offer(client);
/*     */   }
/*     */ 
/*     */   private HttpClient getHttpClient()
/*     */   {
/* 119 */     HttpClient client = (HttpClient)this.httpclientpool.poll();
/* 120 */     if (client == null) {
/* 121 */       client = new DefaultHttpClient();
/* 122 */       client = new DefaultHttpClient(new ThreadSafeClientConnManager(client.getParams(), client.getConnectionManager().getSchemeRegistry()), client.getParams());
/*     */     }
/* 124 */     return client;
/*     */   }
/*     */ 
/*     */   public ISessionMessage trans(String url, ISessionMessage message, long timeout) throws TransException, InterruptedException, ExecutionException, TimeoutException {
/* 128 */     if (!this.enable.get()) {
/* 129 */       throw new TransException("Object Enable false", TransException.ENABLE_FALSE);
/*     */     }
/* 131 */     Future future = this.excutors.submit(new Callable(url, message) {
/*     */       public ISessionMessage call() throws Exception {
/* 133 */         return WessionTranser.this.trans(this.val$url, this.val$message);
/*     */       }
/*     */     });
/* 136 */     return (ISessionMessage)future.get(timeout, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */   public void initialize(int connection_max_size)
/*     */   {
/* 166 */     this.httpclientpool = new ArrayBlockingQueue(connection_max_size);
/* 167 */     this.excutors = new ThreadPoolExecutor(connection_max_size, connection_max_size, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), new TransThreadFactory(null));
/*     */   }
/*     */ 
/*     */   public void stop()
/*     */   {
/* 178 */     this.excutors.shutdown();
/*     */   }
/*     */ 
/*     */   public boolean isEnable() {
/* 182 */     return this.enable.get();
/*     */   }
/*     */   public void setEnable(boolean enable) {
/* 185 */     this.enable.set(enable);
/*     */   }
/*     */ 
/*     */   public static String bytesToHex(byte[] bytes)
/*     */   {
/* 190 */     char[] hexChars = new char[bytes.length * 3];
/* 191 */     for (int j = 0; j < bytes.length; ++j) {
/* 192 */       int v = bytes[j] & 0xFF;
/* 193 */       hexChars[(j * 3)] = hexArray[(v >>> 4)];
/* 194 */       hexChars[(j * 3 + 1)] = hexArray[(v & 0xF)];
/* 195 */       hexChars[(j * 3 + 2)] = ' ';
/*     */     }
/* 197 */     return new String(hexChars);
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
/* 172 */       Thread thread = new Thread(r);
/* 173 */       thread.setName("Trans Worker Thread");
/* 174 */       return thread;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.wession.agent.WessionTranser
 * JD-Core Version:    0.5.4
 */