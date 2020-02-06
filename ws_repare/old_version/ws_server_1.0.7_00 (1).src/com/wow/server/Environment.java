/*    */ package com.wow.server;
/*    */ 
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class Environment extends Properties
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private static Environment instance;
/*    */   public static final String HOME_PATH = "HOME_PATH";
/*    */   public static final String CONFIG_PATH = "CONFIG_PATH";
/*    */   public static final String LOG_CONFIG = "LOG_CONFIG";
/*    */   public static final String LOG_PATH = "LOG_PATH";
/*    */ 
/*    */   public static Environment getInstance()
/*    */   {
/* 16 */     if (instance == null) {
/* 17 */       instance = new Environment();
/*    */     }
/* 19 */     return instance;
/*    */   }
/*    */   public void setEnv(String[] args) {
/* 22 */     for (int i = 0; i < args.length; ++i) {
/* 23 */       String[] env = args[i].split("=");
/* 24 */       setProperty(env[0], env[1]);
/*    */     }
/*    */   }
/*    */ 
/*    */   public String getLogConfig() {
/* 29 */     System.setProperty("log.path", getProperty("HOME_PATH") + "/" + getProperty("LOG_PATH"));
/* 30 */     return getProperty("HOME_PATH") + "/" + getProperty("CONFIG_PATH") + "/" + getProperty("LOG_CONFIG");
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.server.Environment
 * JD-Core Version:    0.5.4
 */