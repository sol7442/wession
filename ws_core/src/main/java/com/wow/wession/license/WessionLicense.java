package com.wow.wession.license;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
public class WessionLicense implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4997533753450411255L;
	private transient File directory  = null;
	private transient File licenseFile = null;
	private String siteName;
	private String licenseUrl;
	private String wessionUrl;
	private String applicationName; 
	public WessionLicense(String file_path){
		directory = new File(file_path);
	}
	public WessionLicense(File file){
		this.licenseFile = file;
	}
	public void setSiteName(String site_name) {
		this.siteName = site_name;
	}
	public void setLicenseUrl(String license_url) {
		this.licenseUrl = license_url;
	}
	public void setWessionUrl(String wession_url) {
		this.wessionUrl = wession_url;
	}
	public void setApplicationName(String app_name) {
		this.applicationName = app_name;
	}
	public String getSiteName() {
		return siteName;
	}
	public String getLicenseUrl() {
		return licenseUrl;
	}
	public String getWessionUrl() {
		return wessionUrl;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public File getFile(){
		return licenseFile;
	}
	public void save() {
		licenseFile = new File(directory.getPath(),getSiteName()+"_"+getApplicationName()+".lic");
		try {
			ObjectOutputStream outstream = new ObjectOutputStream(new FileOutputStream(licenseFile));
			outstream.writeObject(this);
			outstream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean load() {
		boolean bRes  = false;
		try {
			ObjectInputStream instream  = new ObjectInputStream(new FileInputStream(licenseFile));
			WessionLicense temp = (WessionLicense) instream.readObject();
			setSiteName(temp.getSiteName());
			setLicenseUrl(temp.getLicenseUrl());
			setWessionUrl(temp.getWessionUrl());
			setApplicationName(temp.getApplicationName());
			instream.close();
			
			printLicense();
			
			bRes = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return bRes;
	}
	private void printLicense() {
		System.out.println("***********************************************************************************");
		System.out.println("   * SITE NAME       : " + siteName);
		System.out.println("   * LICENSE URL     : " + licenseUrl);
		System.out.println("   * SERVICE URL     : " + wessionUrl);
		System.out.println("   * APPLICATION    : " + applicationName);
		System.out.println("***********************************************************************************");
		System.out.println(" This Copyright License Agreement is made effective as of ["+siteName+"] . ");	
		System.out.println("***********************************************************************************");
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("SITE NAME    : ").append(siteName).append("\n\r");
		buffer.append("LICENSE URL  : ").append(licenseUrl).append("\n\r");
		buffer.append("SERVICE URL  : ").append(wessionUrl).append("\n\r");
		buffer.append("APPLICATION : ").append(applicationName).append("\n\r");
		return buffer.toString();
	}
	
	
	
	
	
}
