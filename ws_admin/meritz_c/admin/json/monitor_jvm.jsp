<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.wow.wession.agent.*"%>
<%@page import="com.wow.wession.session.ISessionMessage"%>
<%@page import="com.wow.wession.session.WessionSessionManager"%>
<%
	String TotalMemory = (Runtime.getRuntime().totalMemory() / 1024 / 1024) + "";
	String FreeMemory = (Runtime.getRuntime().freeMemory() / 1024 / 1024) + "";
	String UsedMemory = ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024) + "";
	String AvailableProcessors = (Runtime.getRuntime().availableProcessors()) + "";
%>
<%
	try {
		out.clear();
		pageContext.pushBody();
		
		WessionSessionManager s_mgr = WessionSessionManager.getInstance();
		String io = s_mgr.getProperty("SSO.IO");
		if (io == null) io = "0";
		System.out.println("[monitor_jvm.jsp] s_mgr.getProperty(\"SSO.IO\") : " + io);
		
		//메시지를 받는다.
		WessionListener listener = new WessionListener(request, response);
		ISessionMessage req_msg = listener.read();
		
		req_msg.getSession().setAttribute("INSTANCE", System.getProperty("WAIN"));
		req_msg.getSession().setAttribute("TotalMemory", TotalMemory);
		req_msg.getSession().setAttribute("FreeMemory", FreeMemory);
		req_msg.getSession().setAttribute("UsedMemory", UsedMemory);
		req_msg.getSession().setAttribute("AvailableProcessors", AvailableProcessors);
		req_msg.getSession().setAttribute("SSOIO", io);
		
		listener.write(req_msg);

	}
	catch (Exception e) {
		e.printStackTrace();
	}
%>