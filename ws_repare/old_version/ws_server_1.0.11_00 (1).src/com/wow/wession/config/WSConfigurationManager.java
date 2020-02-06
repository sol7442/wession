/*    */ package com.wow.wession.config;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import org.apache.xmlbeans.XmlException;
/*    */ import org.apache.xmlbeans.XmlObject;
/*    */ import org.apache.xmlbeans.XmlOptions;
/*    */ 
/*    */ public class WSConfigurationManager
/*    */ {
/*    */   private static WSConfigurationManager instance;
/*    */   public static final String DEFAULT_CONFIG = "com/wow/wession/config/default_log4j.xml";
/*    */   private WessionConfigurationDocument conf_doc;
/*    */   private String fileName;
/*    */ 
/*    */   public static WSConfigurationManager getInstance()
/*    */   {
/* 21 */     if (instance == null) {
/* 22 */       instance = new WSConfigurationManager();
/*    */     }
/* 24 */     return instance;
/*    */   }
/*    */ 
/*    */   public boolean load(String path) {
/* 28 */     boolean bRes = false;
/*    */     try {
/* 30 */       this.fileName = path;
/* 31 */       File file = new File(path);
/* 32 */       this.conf_doc = WessionConfigurationDocument.Factory.parse(file);
/* 33 */       bRes = true;
/*    */     } catch (XmlException e) {
/* 35 */       e.printStackTrace();
/*    */     } catch (IOException e) {
/* 37 */       e.printStackTrace();
/*    */     }
/* 39 */     return bRes;
/*    */   }
/*    */ 
/*    */   public void save(String path) throws IOException {
/* 43 */     this.fileName = path;
/* 44 */     File file = new File(path);
/* 45 */     this.conf_doc.save(file, getOption());
/*    */   }
/*    */ 
/*    */   public WessionConfigurationDocument.WessionConfiguration get() {
/* 49 */     if (this.conf_doc == null) {
/* 50 */       this.conf_doc = WessionConfigurationDocument.Factory.newInstance();
/*    */     }
/*    */ 
/* 53 */     WessionConfigurationDocument.WessionConfiguration wconf = this.conf_doc.getWessionConfiguration();
/* 54 */     if (wconf == null) {
/* 55 */       wconf = this.conf_doc.addNewWessionConfiguration();
/*    */     }
/* 57 */     return wconf;
/*    */   }
/*    */ 
/*    */   public String getText() {
/* 61 */     return this.conf_doc.xmlText(getOption());
/*    */   }
/*    */   public String getText(XmlObject object) {
/* 64 */     if (object == null)
/* 65 */       return "xml is null";
/*    */     try
/*    */     {
/* 68 */       return object.xmlText(getOption()); } catch (Exception e) {
/*    */     }
/* 70 */     return e.getMessage();
/*    */   }
/*    */ 
/*    */   private XmlOptions getOption() {
/* 74 */     XmlOptions opt = new XmlOptions();
/* 75 */     opt.setSavePrettyPrint();
/* 76 */     opt.setSavePrettyPrintIndent(4);
/* 77 */     return opt;
/*    */   }
/*    */ 
/*    */   public String getFileName() {
/* 81 */     return this.fileName;
/*    */   }
/*    */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.wession.config.WSConfigurationManager
 * JD-Core Version:    0.5.4
 */