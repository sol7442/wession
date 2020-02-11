<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.wession.Environment"%>
<%@page import="com.wession.Const"%>
<%@page import="com.wession.model.ConfigAgent"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.concurrent.ConcurrentHashMap"%>
<%@page import="com.wow.wession.agent.*"%>
<%@page import="com.wow.wession.session.ISessionMessage"%>
<%!// 다른 서버에서 메시지를 전송할 필요가 있을 경우 
	private WessionTranser	transer;

	public void jspInit() {
		transer = new WessionTranser();
		transer.initialize(20); // trans용 Thread를 10개만 가지고 있음
	}

	public void jspDestroy() {
		transer.stop();
	}
%>
<%
	// Agent로 명령을 보내 세션을 삭제함
	// input parameter - license : agent에 있는 라이센스명
	//                   type : all(전체 세션삭제), dummy(더미세션 삭제)
	// COMMAND : REMOVE_ALL_SESSION, REMOVE_DUMMY
	// SESSION ATTRIBUTE : REMAIN_MINUTES
	// RETURN ATTRIBUTE : START_COUNT, REMOVE_COUNT, RESULT, CODE
	
	String license = request.getParameter("license");
	String type = request.getParameter("type");
	String COMMAND = "";
	
	StringBuilder sb = new StringBuilder();
	System.out.println("===========" + license + " / " + type);
	
	long trans_timeout = 2000;
	
	
	if ("all".equals(type)) {
		COMMAND = "REMOVE_ALL_SESSION";
	} else if ("dummy".equals(type)) {
		COMMAND = "REMOVE_DUMMY";
	}
	
	// license 이름으로 보낼 곳을 찾음
	Environment env = Environment.getInstance();
	ConfigAgent agent = env.getConfigAgent(license);
	
	String monitor_url = agent.getAppMonitorURL();
	String appCode = agent.getAppName();
	String instance = agent.getAppInstance();
	
	String return_code = "";
	String remove_count = "";
	
	try {
		WessionAgentSession iMonitor = new WessionAgentSession(session.getId(), "agent", "command");
		iMonitor.setAttribute(Const.CODE, Const.CODE_FAIL);
		iMonitor.setAttribute(Const.CMD, COMMAND);
		iMonitor.setAttribute("REMAIN_MINUTES", "60000");

		System.out.println("TRANS URL : " + monitor_url);

		ISessionMessage monitor_msg = new WessionAgentMessage(3, iMonitor);
		monitor_msg = transer.trans(monitor_url, monitor_msg, trans_timeout);

	 	return_code = (String) monitor_msg.getSession().getAttribute("CODE");
		remove_count = (String) monitor_msg.getSession().getAttribute("REMOVE_COUNT");

		System.out.println("\treturn_code : " + return_code);
		System.out.println("\tremove_count : " + remove_count);
		System.out.println("\tSession : " + monitor_msg.getSession());
	}
	catch (Exception e) {
		e.printStackTrace();
	}
%>
{"code":"<%= return_code %>", "message":"에이전트 세션을 삭제하였습니다.", "remove_count":"<%= remove_count %>"}