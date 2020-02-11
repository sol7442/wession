<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.wow.wession.agent.*"%>
<%@page import="com.wow.wession.session.WessionSessionManager"%>
<%@page import="java.io.*"%>
<%@page import="java.net.URL"%>
<%@page import="java.net.URLConnection"%>
<%@page import="org.apache.commons.io.IOUtils"%>
<%!// 다른 서버에서 메시지를 전송할 필요가 있을 경우 
	private WessionTranser	transer;

	public void jspInit() {
		//System.out.println("jspInit count");
		transer = new WessionTranser();
		transer.initialize(20); // trans용 Thread를 10개만 가지고 있음
	}

	public void jspDestroy() {
		transer.stop();
	}%>
<%
	WessionSessionManager s_mgr = WessionSessionManager.getInstance();

	StringBuilder sb = new StringBuilder();
	String WAIN = System.getProperty("WAIN");
	String logPath = s_mgr.getProperty("logPath");
	String server = "";
	String daemon = "";
	int trans_timeout = 2000;

	if (WAIN.contains("01")) {
		server = "Server1";
		daemon = "MERITZ1";
	} else if (WAIN.contains("02")) {
		server = "Server2";
		daemon = "MERITZ2";
	}

	/*
	List <String> lst = AdoptPolicy.getViolationList(logPath + "/" + daemon + "_Policy.log");

	if (lst != null) {
		for (String s:lst) {
			sb.append("{");
			sb.append("\"server\":\"").append(server).append("\",");
			sb.append(s).append("},\n");
		}
	}

	if (sb.length()>2) sb.delete(sb.length()-2, sb.length());
	 */

	String body = "";
	String uri = "";
	String cmd = "getPolicyLog";
	String logdate = request.getParameter("logdate");
	String logtype = request.getParameter("logtype");
	if (logtype == null) {
		logtype = "ALL";
		cmd = "getPolicyLog";
	} else if (logtype.equals("DUPLOGIN")) {
		logtype = "ALL";
		cmd = "getDupLoginLog";
	}
	
	
	try {
		String instance = s_mgr.getProperty("instance.1");
		String monitorURL = s_mgr.getProperty("monitorURL" + "." + instance);
		uri = monitorURL + "?cmd=" + cmd + "&policycode=" + logtype + "&logdate="+logdate;
		URL url = new URL(uri);
		URLConnection con = url.openConnection();
		InputStream in = con.getInputStream();
		String encoding = con.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		
		InputStreamReader reader = new InputStreamReader(in);
		BufferedReader line_reader = new BufferedReader(new InputStreamReader(in));
		
		StringBuffer str_buf = new StringBuffer();
		String line = "";
		while((line = line_reader.readLine()) != null){
			str_buf.append(line);
		}		
			
		//String body2 = IOUtils.toString(in, encoding);
		
		body = str_buf.toString();
	}
	catch (Exception e) {
		System.out.println(e.getMessage() + " : " + uri);
	}
	
	try {
		String instance = s_mgr.getProperty("instance.2");
		String monitorURL = s_mgr.getProperty("monitorURL" + "." + instance);
		uri = monitorURL + "?cmd=" + cmd + "&policycode=" + logtype + "&logdate="+logdate;
		URL url = new URL(uri);
		URLConnection con = url.openConnection();
		
		InputStream in = con.getInputStream();
		String encoding = con.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		
		
		InputStreamReader reader = new InputStreamReader(in);
		BufferedReader line_reader = new BufferedReader(new InputStreamReader(in));
		
		StringBuffer str_buf = new StringBuffer();
		String line = "";
		while((line = line_reader.readLine()) != null){
			str_buf.append(line);
		}		
			
		//String body2 = IOUtils.toString(in, encoding);
		//String body2 = str_buf.toString();
		
		if (body.length()>10 && str_buf.length()>10) { // 2개 다 있는 경우
			body = body + ",\n\n";
			body = body + str_buf.toString();
		} else if (body.length()>10 && str_buf.length()<10) { // 1번만 있는 경우
			// 아무것도 안함
		} else {
			body = str_buf.toString();
		}
	}
	catch (Exception e) {
		System.out.println(e.getMessage() + " : " + uri);
	}
	
	
	//System.out.println(body); 

	/*
	try {
		String monitor_url = s_mgr.getProperty("monitorURL."+WAIN);
		WessionAgentSession iMonitor = new WessionAgentSession(session.getId(), "agent", "monitor");
		iMonitor.setAttribute(Const.CODE, Const.CODE_FAIL);
		iMonitor.setAttribute(Const.CMD, "GET_POLICY_LOG");
		ISessionMessage monitor_msg = new WessionAgentMessage(3, iMonitor);
		//monitor_msg = transer.trans(monitor_url, monitor_msg, trans_timeout);
		
		System.out.println(monitor_msg.getSession());
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	 */
%>
[
<%=body%>
]
