/*    */ package com.wow.wession.agent;
/*    */ 
/*    */ import com.wow.wession.session.ISessionMessage;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ public class WessionListener
/*    */ {
/*    */   private ObjectInputStream reader;
/*    */   private ObjectOutputStream writer;
/*    */ 
/*    */   public WessionListener(HttpServletRequest request, HttpServletResponse response)
/*    */   {
/*    */     try
/*    */     {
/* 18 */       this.reader = new ObjectInputStream(request.getInputStream());
/* 19 */       this.writer = new ObjectOutputStream(response.getOutputStream());
/*    */     } catch (IOException e) {
/* 21 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   public ISessionMessage read() throws IOException, ClassNotFoundException {
/* 26 */     return (ISessionMessage)this.reader.readObject();
/*    */   }
/*    */ 
/*    */   public void write(ISessionMessage session) throws IOException {
/* 30 */     this.writer.writeObject(session);
/* 31 */     this.writer.flush();
/* 32 */     this.writer.reset();
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.wession.agent.WessionListener
 * JD-Core Version:    0.5.4
 */