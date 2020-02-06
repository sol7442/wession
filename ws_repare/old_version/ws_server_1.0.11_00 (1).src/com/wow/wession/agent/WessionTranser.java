/*     */ package com.wow.wession.agent;
/*     */ 
/*     */ import com.wow.wession.session.ISessionMessage;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
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
/*     */ import org.apache.http.client.CookieStore;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.config.RequestConfig.Builder;
/*     */ import org.apache.http.client.methods.HttpPost;
/*     */ import org.apache.http.config.Registry;
/*     */ import org.apache.http.config.RegistryBuilder;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.entity.SerializableEntity;
/*     */ import org.apache.http.impl.client.BasicCookieStore;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.apache.http.impl.client.DefaultHttpClient;
/*     */ import org.apache.http.impl.client.HttpClientBuilder;
/*     */ import org.apache.http.impl.client.HttpClients;
/*     */ import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
/*     */ 
/*     */ public class WessionTranser
/*     */ {
/*     */   private ExecutorService excutors;
/*  63 */   private BlockingQueue<HttpClient> httpclientpool = null;
/*  64 */   private AtomicBoolean enable = new AtomicBoolean(true);
/*     */ 
/* 255 */   protected static final char[] hexArray = "0123456789ABCDEF".toCharArray();
/*     */ 
/*     */   public ISessionMessage trans(String url, ISessionMessage message)
/*     */     throws TransException
/*     */   {
/*  80 */     if (!this.enable.get()) {
/*  81 */       throw new TransException("Object Enable false", TransException.ENABLE_FALSE);
/*     */     }
/*     */ 
/*  85 */     Registry r = RegistryBuilder.create()
/*  86 */       .register("easy", new EasySpecProvider())
/*  87 */       .build();
/*     */ 
/*  89 */     CookieStore cookieStore = new BasicCookieStore();
/*     */ 
/*  91 */     RequestConfig requestConfig = RequestConfig.custom()
/*  92 */       .setCookieSpec("easy")
/*  93 */       .build();
/*     */ 
/*  95 */     CloseableHttpClient http_client = HttpClients.custom()
/*  96 */       .setDefaultCookieStore(cookieStore)
/*  97 */       .setDefaultCookieSpecRegistry(r)
/*  98 */       .setDefaultRequestConfig(requestConfig)
/*  99 */       .build();
/*     */ 
/* 102 */     WessionAgentMessage res = null;
/*     */ 
/* 104 */     HttpPost post = new HttpPost(url);
/* 105 */     HttpResponse response = null;
/*     */     SerializableEntity req_entity;
/*     */     try
/*     */     {
/* 108 */       SerializableEntity req_entity = new SerializableEntity(message, true);
/* 109 */       req_entity.setChunked(true);
/* 110 */       post.setEntity(req_entity);
/*     */ 
/* 113 */       response = http_client.execute(post);
/*     */ 
/* 116 */       HttpEntity entity = response.getEntity();
/*     */ 
/* 118 */       byte[] buffer = null;
/* 119 */       long conents_length = entity.getContentLength();
/* 120 */       int read_length = 0;
/*     */ 
/* 122 */       if (entity != null) {
/* 123 */         ByteArrayOutputStream os = new ByteArrayOutputStream();
/*     */         try {
/* 125 */           entity.writeTo(os);
/* 126 */           read_length = os.size();
/* 127 */           buffer = os.toByteArray();
/*     */         } catch (IOException e1) {
/* 129 */           e1.printStackTrace();
/*     */         }
/*     */       }
/*     */ 
/* 133 */       int read_index = 0;
/* 134 */       while (buffer[read_index] == 10) {
/* 135 */         ++read_index;
/* 136 */         if (read_index > read_length) {
/* 137 */           throw new TransException("RESPONSE CONTENTS ERROR [" + conents_length + "][" + read_index + "]", TransException.PARSER_ERROR);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 142 */       if (read_index > 0) {
/* 143 */         System.out.println("ws out : invalid stream header 0a0a0a0a readObject bug fixed [" + conents_length + "][" + read_length + "]");
/*     */       }
/*     */ 
/* 146 */       int close_byte = 0;
/*     */ 
/* 148 */       int contents_length = read_length - read_index + close_byte;
/* 149 */       byte[] contents_buffer = new byte[contents_length];
/*     */ 
/* 151 */       System.arraycopy(buffer, read_index, contents_buffer, 0, contents_length - close_byte);
/*     */ 
/* 153 */       ByteArrayInputStream bis = new ByteArrayInputStream(contents_buffer);
/* 154 */       ObjectInputStream oi_stream = new ObjectInputStream(bis);
/* 155 */       res = (WessionAgentMessage)oi_stream.readObject();
/*     */     } catch (IOException e) {
/* 157 */       TransException ex = new TransException(e, TransException.NETWORK_ERROR);
/* 158 */       ex.setUrl(url, message);
/* 159 */       throw ex;
/*     */     } catch (ClassNotFoundException e) {
/* 161 */       TransException ex = new TransException(e, TransException.PARAMETER_MISS);
/* 162 */       ex.setUrl(url, message);
/* 163 */       throw ex;
/*     */     } finally {
/* 165 */       if (response != null) {
/*     */         try {
/* 167 */           response.getEntity().consumeContent();
/*     */         } catch (IOException e) {
/* 169 */           e.printStackTrace();
/*     */         }
/*     */       }
/* 172 */       if (http_client != null)
/* 173 */         setHttpClient(http_client);
/*     */     }
/*     */     SerializableEntity req_entity;
/* 177 */     return res;
/*     */   }
/*     */ 
/*     */   private void setHttpClient(HttpClient client) {
/* 181 */     this.httpclientpool.offer(client);
/*     */   }
/*     */ 
/*     */   private HttpClient getHttpClient()
/*     */   {
/* 186 */     HttpClient client = (HttpClient)this.httpclientpool.poll();
/* 187 */     if (client == null) {
/* 188 */       client = new DefaultHttpClient();
/* 189 */       client = new DefaultHttpClient(new ThreadSafeClientConnManager(client.getParams(), client.getConnectionManager().getSchemeRegistry()), client.getParams());
/*     */     }
/* 191 */     return client;
/*     */   }
/*     */ 
/*     */   public ISessionMessage trans(String url, ISessionMessage message, long timeout) throws TransException, InterruptedException, ExecutionException, TimeoutException {
/* 195 */     if (!this.enable.get()) {
/* 196 */       throw new TransException("Object Enable false", TransException.ENABLE_FALSE);
/*     */     }
/* 198 */     Future future = this.excutors.submit(new Callable(url, message) {
/*     */       public ISessionMessage call() throws Exception {
/* 200 */         return WessionTranser.this.trans(this.val$url, this.val$message);
/*     */       }
/*     */     });
/* 203 */     return (ISessionMessage)future.get(timeout, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */   public void initialize(int connection_max_size)
/*     */   {
/* 233 */     this.httpclientpool = new ArrayBlockingQueue(connection_max_size);
/* 234 */     this.excutors = new ThreadPoolExecutor(connection_max_size, connection_max_size, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), new TransThreadFactory(null));
/*     */   }
/*     */ 
/*     */   public void stop()
/*     */   {
/* 245 */     this.excutors.shutdown();
/*     */   }
/*     */ 
/*     */   public boolean isEnable() {
/* 249 */     return this.enable.get();
/*     */   }
/*     */   public void setEnable(boolean enable) {
/* 252 */     this.enable.set(enable);
/*     */   }
/*     */ 
/*     */   public static String bytesToHex(byte[] bytes)
/*     */   {
/* 257 */     char[] hexChars = new char[bytes.length * 3];
/* 258 */     for (int j = 0; j < bytes.length; ++j) {
/* 259 */       int v = bytes[j] & 0xFF;
/* 260 */       hexChars[(j * 3)] = hexArray[(v >>> 4)];
/* 261 */       hexChars[(j * 3 + 1)] = hexArray[(v & 0xF)];
/* 262 */       hexChars[(j * 3 + 2)] = ' ';
/*     */     }
/* 264 */     return new String(hexChars);
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
/* 239 */       Thread thread = new Thread(r);
/* 240 */       thread.setName("Trans Worker Thread");
/* 241 */       return thread;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.wession.agent.WessionTranser
 * JD-Core Version:    0.5.4
 */