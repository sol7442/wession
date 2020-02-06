/*   */ package com.wow.server;
/*   */ 
/*   */ import java.io.Closeable;
/*   */ 
/*   */ public abstract class AbstractConnection
/*   */   implements Closeable
/*   */ {
/*   */   private String Id;
/*   */ 
/*   */   public String getId()
/*   */   {
/* 9 */     return this.Id;
/*   */   }
/*   */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.server.AbstractConnection
 * JD-Core Version:    0.5.4
 */