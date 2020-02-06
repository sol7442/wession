/*    */ package com.wow.client.socket;
/*    */ 
/*    */ import com.wow.client.IConnection;
/*    */ import com.wow.server.protocol.IRequest;
/*    */ import com.wow.server.protocol.IResponse;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.net.InetAddress;
/*    */ import java.net.Socket;
/*    */ import java.net.UnknownHostException;
/*    */ 
/*    */ public class SocketConnection
/*    */   implements IConnection
/*    */ {
/*    */   private Socket socket;
/*    */   private ObjectInputStream objInput;
/*    */   private ObjectOutputStream objOutput;
/* 17 */   private boolean keepAlive = false;
/*    */ 
/*    */   public SocketConnection(String ip, int port) throws UnknownHostException, IOException {
/* 20 */     connect(ip, port);
/*    */   }
/*    */ 
/*    */   public void setKeepAlive(boolean value) {
/* 24 */     this.keepAlive = value;
/*    */   }
/*    */   public boolean isKeepAlive() {
/* 27 */     return this.keepAlive;
/*    */   }
/*    */   public SocketConnection(Socket socket) throws IOException {
/* 30 */     this.socket = socket;
/* 31 */     ServerSocketInitialize();
/*    */   }
/*    */ 
/*    */   public String getIPAddress() {
/* 35 */     return this.socket.getInetAddress().getHostAddress();
/*    */   }
/*    */ 
/*    */   private void ServerSocketInitialize() throws IOException {
/* 39 */     this.objOutput = new ObjectOutputStream(this.socket.getOutputStream());
/* 40 */     this.objInput = new ObjectInputStream(this.socket.getInputStream());
/*    */   }
/*    */ 
/*    */   private void ClientSocketInitialize() throws IOException {
/* 44 */     this.objInput = new ObjectInputStream(this.socket.getInputStream());
/* 45 */     this.objOutput = new ObjectOutputStream(this.socket.getOutputStream());
/*    */   }
/*    */ 
/*    */   public void close() throws IOException {
/* 49 */     if (this.socket != null) {
/* 50 */       this.objInput.close();
/* 51 */       this.objOutput.close();
/* 52 */       this.socket.close();
/*    */     }
/*    */   }
/*    */ 
/*    */   public void connect(String ip, int port) throws UnknownHostException, IOException {
/* 57 */     this.socket = new Socket(ip, port);
/* 58 */     ClientSocketInitialize();
/*    */   }
/*    */   public boolean isConnected() {
/* 61 */     boolean bRes = false;
/* 62 */     if (this.socket != null) {
/* 63 */       bRes = this.socket.isConnected();
/*    */     }
/* 65 */     return bRes;
/*    */   }
/*    */ 
/*    */   public IRequest readRequest() throws ClassNotFoundException, IOException {
/* 69 */     return (IRequest)this.objInput.readObject();
/*    */   }
/*    */ 
/*    */   public void writeRequest(IRequest request) throws IOException {
/* 73 */     this.objOutput.writeObject(request);
/* 74 */     this.objOutput.flush();
/* 75 */     this.objOutput.reset();
/*    */   }
/*    */ 
/*    */   public IResponse readResponse() throws ClassNotFoundException, IOException {
/* 79 */     return (IResponse)this.objInput.readObject();
/*    */   }
/*    */ 
/*    */   public void writeResponse(IResponse response) throws IOException {
/* 83 */     this.objOutput.writeObject(response);
/* 84 */     this.objOutput.flush();
/* 85 */     this.objOutput.reset();
/*    */   }
/*    */ 
/*    */   public int getPortNumber() {
/* 89 */     return this.socket.getPort();
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 93 */     if (this.socket != null) {
/* 94 */       return this.socket.toString();
/*    */     }
/* 96 */     return "socket is null";
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.client.socket.SocketConnection
 * JD-Core Version:    0.5.4
 */