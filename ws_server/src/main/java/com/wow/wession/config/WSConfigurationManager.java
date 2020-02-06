package com.wow.wession.config;

import java.io.File;
import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.wow.wession.config.WessionConfigurationDocument.WessionConfiguration;


public class WSConfigurationManager{

	static private WSConfigurationManager instance;
	public static final String DEFAULT_CONFIG = "com/wow/wession/config/default_log4j.xml";
	private WessionConfigurationDocument conf_doc;
	private String fileName;
	
	public static WSConfigurationManager getInstance(){
		if(instance == null){
			instance = new WSConfigurationManager();
		}
		return instance;
	}
	
	public boolean load(String path){
		boolean bRes = false;
		try {
			this.fileName = path;
			File file = new File(path);
			conf_doc = WessionConfigurationDocument.Factory.parse(file);
			bRes = true;
		} catch (XmlException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bRes;
	}
	
	public void save(String path) throws IOException{
		this.fileName = path;
		File file = new File(path);
		conf_doc.save(file, getOption());
	}
	
	public WessionConfiguration get(){
		if(conf_doc == null){
			conf_doc = WessionConfigurationDocument.Factory.newInstance();
		}
		
		WessionConfiguration wconf = conf_doc.getWessionConfiguration();;
		if(wconf == null){
			wconf = conf_doc.addNewWessionConfiguration();
		}
		return wconf;
	}

	public String getText(){
		return conf_doc.xmlText(getOption());
	}
	public String getText(XmlObject object){
		if(object == null){
			return "xml is null";
		}
		try{
			return object.xmlText(getOption());
		}catch(Exception e){
			return e.getMessage();
		}
	}
	private XmlOptions getOption(){
		XmlOptions opt = new XmlOptions();
		opt.setSavePrettyPrint();
		opt.setSavePrettyPrintIndent(4);
		return opt;
	}
	
	public String getFileName() {
		return this.fileName;
	}

}
