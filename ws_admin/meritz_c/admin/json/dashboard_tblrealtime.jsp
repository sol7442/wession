<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.wow.wession.agent.WessionAgentSession"%>
<%@page import="com.wow.wession.session.ISession"%>
<%@page import="com.wow.wession.session.WessionSessionManager"%>
<%@page import="com.wow.wession.server.WessionServerSession"%>
<%@page import="com.wession.model.meritz.User"%>
<%@page import="com.wession.model.meritz.SSOAuth"%>
<%!public static String getDateFromLAT(String LAT) {
		String returns = "-";
		if (LAT == null || "".equals(LAT))
			return returns;
		try {
			long yourmilliseconds = new Long(LAT);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date resultdate = new Date(yourmilliseconds);
			returns = sdf.format(resultdate);
		}
		catch (Exception e) {
			//e.printStackTrace();
		}
		finally {
			return returns;
		}
	}

	public static String getDateFromLAT(Long LAT) {
		if (LAT == null) return "";
		long yourmilliseconds = LAT;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date resultdate = new Date(yourmilliseconds);
		return sdf.format(resultdate);
	}

	public static void setJsonData(StringBuilder sb, int number, WessionServerSession sm) {
		// 순번 표시
		sb.append("{").append("\"no\":").append(number);

		// 사용자사번 표시
		sb.append(",\"userid\":\"").append(sm.getUser()).append("\"");

		// 사용자성명 표시
		String userName = "없음";
		User user = (User) sm.getAttribute("User");
		if (user != null)
			userName = user.getUserName();
		sb.append(",\"name\":\"").append(userName).append("\"");

		// 접속IP 표시
		String authIP = "No Auth Token";
		SSOAuth auth = (SSOAuth) sm.getAttribute("SSOAuth");
		if (auth != null)
			authIP = auth.getAuthIP();
		sb.append(",\"userip\":\"").append(authIP).append("\"");

		// 접속시간 표시
		String accessTime = "No Auth Token";
		if (auth != null)
			accessTime = auth.getAuthTime();
		sb.append(",\"sso\":\"").append(getDateFromLAT(accessTime.substring(2))).append("\"");

		// 업무포탈 접속시간 표시
		String bizAccessTime = "-";
		String salesAccessTime = "-";
		
		String directAccessTime = "-";
		String homepageAccessTime = "-";
		
		List<ISession> agent = sm.getAgentSessionList();
		for (int index = 0; index < agent.size(); index++) {
			WessionAgentSession agent_session = (WessionAgentSession) agent.get(index);
			if (agent_session.getAppCode() != null && (agent_session.getAppCode()).contains("BIZ"))
				bizAccessTime = getDateFromLAT(agent_session.getCreateTime());
			if (agent_session.getAppCode() != null && (agent_session.getAppCode()).contains("SALES"))
				salesAccessTime = getDateFromLAT(agent_session.getCreateTime());
			
			if (agent_session.getAppCode() != null && (agent_session.getAppCode()).contains("CMS"))
				directAccessTime = getDateFromLAT(agent_session.getCreateTime());
			if (agent_session.getAppCode() != null && (agent_session.getAppCode()).contains("HPS"))
				homepageAccessTime = getDateFromLAT(agent_session.getCreateTime());
		}

		sb.append(",\"biz\":\"").append(bizAccessTime).append("\"");
		sb.append(",\"sales\":\"").append(salesAccessTime).append("\"");

		sb.append(",\"directmall\":\"").append(directAccessTime).append("\"");
		sb.append(",\"hompeage\":\"").append(homepageAccessTime).append("\"");
		
		// 삭제 등의 데이터 조작을 위한 wsid
		sb.append(",\"wsid\":\"").append(sm.getID()).append("\"");

		sb.append("},\n");
	}%>
<%
	WessionSessionManager s_mgr = WessionSessionManager.getInstance();
	int numbers = s_mgr.getSize();
	int totalSize = s_mgr.getSize();
	StringBuilder sb = new StringBuilder();
	int pages = 10;
	int pageBreaker = 0;
	int i = 0;
	if (s_mgr != null) {

		Iterator<ISession> itor = s_mgr.iterator();
		while (itor.hasNext()) {
			if (pages < pageBreaker)
				break;
			pageBreaker++;
			WessionServerSession sm = (WessionServerSession) itor.next();

			setJsonData(sb, numbers--, sm);

		}
	}
	if (sb.length() > 2)
		sb.delete(sb.length() - 2, sb.length());
%>
[
<%=sb%>
]
