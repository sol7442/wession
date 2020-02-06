/*     */ package com.wow.wession.license;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class WessionLicense
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -4997533753450411255L;
/*  17 */   private transient File directory = null;
/*  18 */   private transient File licenseFile = null;
/*     */   private String siteName;
/*     */   private String licenseUrl;
/*     */   private String wessionUrl;
/*     */   private String applicationName;
/*     */ 
/*     */   public WessionLicense(String file_path)
/*     */   {
/*  24 */     this.directory = new File(file_path);
/*     */   }
/*     */   public WessionLicense(File file) {
/*  27 */     this.licenseFile = file;
/*     */   }
/*     */   public void setSiteName(String site_name) {
/*  30 */     this.siteName = site_name;
/*     */   }
/*     */   public void setLicenseUrl(String license_url) {
/*  33 */     this.licenseUrl = license_url;
/*     */   }
/*     */   public void setWessionUrl(String wession_url) {
/*  36 */     this.wessionUrl = wession_url;
/*     */   }
/*     */   public void setApplicationName(String app_name) {
/*  39 */     this.applicationName = app_name;
/*     */   }
/*     */   public String getSiteName() {
/*  42 */     return this.siteName;
/*     */   }
/*     */   public String getLicenseUrl() {
/*  45 */     return this.licenseUrl;
/*     */   }
/*     */   public String getWessionUrl() {
/*  48 */     return this.wessionUrl;
/*     */   }
/*     */   public String getApplicationName() {
/*  51 */     return this.applicationName;
/*     */   }
/*     */   public File getFile() {
/*  54 */     return this.licenseFile;
/*     */   }
/*     */   public void save() {
/*  57 */     this.licenseFile = new File(this.directory.getPath(), getSiteName() + "_" + getApplicationName() + ".lic");
/*     */     try {
/*  59 */       ObjectOutputStream outstream = new ObjectOutputStream(new FileOutputStream(this.licenseFile));
/*  60 */       outstream.writeObject(this);
/*  61 */       outstream.close();
/*     */     } catch (FileNotFoundException e) {
/*  63 */       e.printStackTrace();
/*     */     } catch (IOException e) {
/*  65 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean load() {
/*  70 */     boolean bRes = false;
/*     */     try {
/*  72 */       ObjectInputStream instream = new ObjectInputStream(new FileInputStream(this.licenseFile));
/*  73 */       WessionLicense temp = (WessionLicense)instream.readObject();
/*  74 */       setSiteName(temp.getSiteName());
/*  75 */       setLicenseUrl(temp.getLicenseUrl());
/*  76 */       setWessionUrl(temp.getWessionUrl());
/*  77 */       setApplicationName(temp.getApplicationName());
/*  78 */       instream.close();
/*     */ 
/*  80 */       printLicense();
/*     */ 
/*  82 */       bRes = true;
/*     */     } catch (FileNotFoundException e) {
/*  84 */       e.printStackTrace();
/*     */     } catch (IOException e) {
/*  86 */       e.printStackTrace();
/*     */     } catch (ClassNotFoundException e) {
/*  88 */       e.printStackTrace();
/*     */     }
/*  90 */     return bRes;
/*     */   }
/*     */   private void printLicense() {
/*  93 */     System.out.println("***********************************************************************************");
/*  94 */     System.out.println("   * SITE NAME       : " + this.siteName);
/*  95 */     System.out.println("   * LICENSE URL     : " + this.licenseUrl);
/*  96 */     System.out.println("   * SERVICE URL     : " + this.wessionUrl);
/*  97 */     System.out.println("   * APPLICATION    : " + this.applicationName);
/*  98 */     System.out.println("***********************************************************************************");
/*  99 */     System.out.println(" This Copyright License Agreement is made effective as of [" + this.siteName + "] . ");
/* 100 */     System.out.println("***********************************************************************************");
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 104 */     StringBuffer buffer = new StringBuffer();
/* 105 */     buffer.append("SITE NAME    : ").append(this.siteName).append("\n\r");
/* 106 */     buffer.append("LICENSE URL  : ").append(this.licenseUrl).append("\n\r");
/* 107 */     buffer.append("SERVICE URL  : ").append(this.wessionUrl).append("\n\r");
/* 108 */     buffer.append("APPLICATION : ").append(this.applicationName).append("\n\r");
/* 109 */     return buffer.toString();
/*     */   }
/*     */ }

/* Location:           D:\Work\기술지원\메리츠화재보험\20191029 메모리누수\ws_server_1.0.11_00.jar
 * Qualified Name:     com.wow.wession.license.WessionLicense
 * JD-Core Version:    0.5.4
 */