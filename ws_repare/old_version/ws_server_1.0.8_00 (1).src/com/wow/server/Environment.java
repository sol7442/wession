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

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.server.Environment
 * JD-Core Version:    0.5.4
 */