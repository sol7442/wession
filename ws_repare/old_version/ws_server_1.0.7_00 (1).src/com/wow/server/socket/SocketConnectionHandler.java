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

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.7_00 (1).jar
 * Qualified Name:     com.wow.server.socket.SocketConnectionHandler
 * JD-Core Version:    0.5.4
 */