<%@page import="com.wow.wession.server.WessionServerSession"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.wow.wession.session.WessionSessionManager"%>
<%@page import="com.wow.wession.session.ISession"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>WessionDemo Sample</title>
</head>
<body>
<%
	WessionSessionManager s_mgr = WessionSessionManager.getInstance();
	List <ISession> sm2 =  s_mgr.getBy("UserID","lej0718");
	out.println(sm2);
	out.println("<br/>");
	if(sm2 != null){
		Iterator<ISession> iter = sm2.iterator();
		int i = 0;
		while(iter.hasNext()){
			i++;
			ISession ss = iter.next();
			WessionServerSession ss2 = (WessionServerSession) ss;
			out.println("userid : lej0718 - [" + i + "]" + ss2.getID() + "<br/>");
		}
	}
	
	WessionServerSession sm22 = (WessionServerSession) s_mgr.get("ssojsid_14567801");
	String sm2_userid = null;
	if(sm22 != null){
	  sm2_userid = sm22.getUser();
	}
	 
	
	out.println("===>" + sm2_userid);

%>
<hr/>
 / <%= s_mgr.getSize() %>
</body>
</html>