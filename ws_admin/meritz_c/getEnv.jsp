<%@page import="java.util.Collections"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.wow.wession.agent.WessionAgentSession"%>
<%@page import="com.wow.wession.session.ISession"%>
<%@page import="com.wow.wession.session.WessionSessionManager"%>
<%@page import="com.wow.wession.server.WessionServerSession"%>
<%@page import="com.wession.Environment"%>
<%@page import="com.wession.model.ConfigAgent"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.concurrent.ConcurrentHashMap"%>
<%
	Environment obj = Environment.getInstance();
	//mon.setMonitorDataPath("/Users/eunjun/Documents/workspace/meritz/WebContent/WEB-INF/monitor.dat");
	String instanceID = System.getProperty("WAIN");
	String path = "";
	String localPath = "/Users/eunjun/Documents/workspace/meritz/WebContent/WEB-INF/monitor.dat";
	String serverPath = "/storage/wession/wessiondemo/Server/WEB-INF/environment.dat";
	
	if (instanceID.contains("TEST")) 
		path = localPath;
	else 
		path = serverPath;
	
	obj.setEnvDataPath(path);
	obj.initialize();
	
	out.println(obj.getEnvironmentSize());	
	
	Vector <String> v = obj.getAgentServiceURL("SSO");
	
	//out.println(obj.getAgentServiceURL("SSO"));
	//obj.writeEnv();
%>