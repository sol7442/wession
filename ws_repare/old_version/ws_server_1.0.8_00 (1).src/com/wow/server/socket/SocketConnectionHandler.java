/*    */ package com.wow.server.socket;
/*    */ 
/*    */ import com.wow.server.AbstractConnection;
/*    */ import com.wow.server.IConnectionHandler;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.net.Socket;
/*    */ 
/*    */ public class SocketConnectionHandler extends AbstractConnection
/*    */   implements IConnectionHandler
/*    */ {
/*    */   private Socket socket;
/*    */   private ObjectInputStream objInput;
/*    */   private ObjectOutputStream objOutput;
/*    */ 
/*    */   public SocketConnectionHandler(Socket socket)
/*    */     throws IOException
/*    */   {
/* 22 */     this.socket = socket;
/* 23 */     this.objOutput = new ObjectOutputStream(socket.getOutputStream());
/* 24 */     this.objInput = new ObjectInputStream(socket.getInputStream());
/*    */   }
/*    */   public Socket getSocket() {
/* 27 */     return this.socket;
/*    */   }
/*    */ 
/*    */   public void close() throws IOException {
/* 31 */     this.socket.close();
/*    */   }
/*    */ 
/*    */   public Object readObject() throws ClassNotFoundException, IOException {
/* 35 */     return this.objInput.readObject();
/*    */   }
/*    */ 
/*    */   public void writeObject(Object obj) throws IOException {
/* 39 */     this.objOutput.writeObject(obj);
/* 40 */     this.objOutput.flush();
/* 41 */     this.objOutput.reset();
/*    */   }
/*    */ }

/* Location:           D:\backup_201809\Dev\wession\src\ws_builder\dist\ws_server_1.0.8_00_201611032059_debug.jar
 * Qualified Name:     com.wow.server.socket.SocketConnectionHandler
 * JD-Core Version:    0.5.4
 */