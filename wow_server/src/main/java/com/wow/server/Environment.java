package com.wow.server;

import java.util.Properties;

public class Environment extends Properties {
	private static final long serialVersionUID = 1L;
	private static Environment instance;	
	
	
	public static final String HOME_PATH 	= "HOME_PATH";
	public static final String CONFIG_PATH 	= "CONFIG_PATH";
	public static final String LOG_CONFIG 	= "LOG_CONFIG";
	public static final String LOG_PATH		= "LOG_PATH";
	
	public static Environment getInstance(){
		if(instance ==null){
			instance = new Environment();
		}
		return instance;
	}
	public void setEnv(String[] args) {
		for(int i=0; i<args.length; i++){
			String[] env = args[i].split("=");
			setProperty(env[0],env[1]);
		}
	}
	
	public String getLogConfig(){
		System.setProperty("log.path", getProperty(HOME_PATH) + "/" + getProperty(LOG_PATH));
		return getProperty(HOME_PATH) + "/"+ getProperty(CONFIG_PATH) + "/" +getProperty(LOG_CONFIG);
	}
}
