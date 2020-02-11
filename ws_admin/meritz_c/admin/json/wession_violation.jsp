<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.wow.wession.agent.WessionAgentSession"%>
<%@page import="com.wow.wession.session.ISession"%>
<%@page import="com.wow.wession.session.WessionSessionManager"%>
<%@page import="com.wow.wession.server.WessionServerSession"%>
<%@page import="com.wession.Const"%>
<%@page import="com.wession.Common"%>
<%@page import="com.wession.model.meritz.User"%>
<%@page import="com.wession.model.meritz.SSOAuth"%>
<%!
	public static void setJsonData(StringBuilder sb, int number, WessionServerSession sm) {
		// 순번 표시
		sb.append("{").append("\"no\":").append(number);

		// 사용자사번 표시
		sb.append(",\"userid\":\"").append(sm.getUser()).append("\"");

		// 사용자성명 표시
		String userName = "없음";
		User user = (User) sm.getAttribute(Const.WSO_User);
		if (user != null)
			userName = user.getUserName();
		sb.append(",\"name\":\"").append(userName).append("\"");

		// 접속IP 표시
		String authIP = "No Auth Token";
		SSOAuth auth = (SSOAuth) sm.getAttribute(Const.WSO_Auth);
		if (auth != null)
			authIP = auth.getAuthIP();
		sb.append(",\"userip\":\"").append(authIP).append("\"");

		// 접속시간 표시
		String accessTime = "No Auth Token";
		if (auth != null)
			accessTime = auth.getAuthTime();
		sb.append(",\"sso\":\"").append(Common.getDateFromLAT(accessTime.substring(2))).append("\"");

		// 업무포탈 접속시간 표시
		String bizAccessTime = "-";
		String salesAccessTime = "-";
		List<ISession> agent = sm.getAgentSessionList();
		for (int index = 0; index < agent.size(); index++) {
			WessionAgentSession agent_session = (WessionAgentSession) agent.get(index);
			if (agent_session.getAppCode() != null && (agent_session.getAppCode()).contains("BIZ") )
				bizAccessTime = Common.getDateFromLAT(agent_session.getCreateTime());
			if (agent_session.getAppCode() != null && (agent_session.getAppCode()).contains("SALES") )
				salesAccessTime = Common.getDateFromLAT(agent_session.getCreateTime());
			if (agent_session.getAppCode() != null && (agent_session.getAppCode()).contains("DEMOAPP") )
				bizAccessTime = Common.getDateFromLAT(agent_session.getCreateTime());
			if (agent_session.getAppCode() != null && (agent_session.getAppCode()).contains("DEMOAPP") )
				salesAccessTime = Common.getDateFromLAT(agent_session.getCreateTime());
		}

		sb.append(",\"biz\":\"").append(bizAccessTime).append("\"");
		sb.append(",\"sales\":\"").append(salesAccessTime).append("\"");
		
		// 삭제 등의 데이터 조작을 위한 wsid
		sb.append(",\"wsid\":\"").append(sm.getID()).append("\"");

		sb.append("},\n");
	}%>
<%
	// 들어오는 파라미터를 분석하여 처리함
	/*
	Map<String, String[]> parameters = request.getParameterMap();
	for (String parameter : parameters.keySet()) {
		//System.out.println(parameter + ":" + request.getParameter(parameter));
	}
	*/
	

	String pLimit = request.getParameter("limit");
	String pOffset = request.getParameter("offset");
	String pSearch = request.getParameter("search");
	StringBuilder sb = new StringBuilder();
	
	WessionSessionManager s_mgr = WessionSessionManager.getInstance();
	
	String policy_minutes = s_mgr.getProperty("ANTICIPATED_MIN");
	int back_minutes = 0;
	if (policy_minutes == null || policy_minutes.equals("")) {
		back_minutes = 60;
	} else {
		back_minutes = Integer.parseInt(policy_minutes);
	}
		
			
	int numbers = s_mgr.getSize();
	int totalSize = s_mgr.getSize();

	int pages = Integer.parseInt(pLimit);
	int offset = Integer.parseInt(pOffset) + 1;
	int pageBreaker = 1;
	int i = 0;
	int cutover = 0;
	if (s_mgr != null) {
		if (pSearch != null && !"".equals(pSearch)) {
			List<ISession> lst = s_mgr.getBy(Const.GetBy_ID, pSearch);
			if (lst != null) {
				totalSize = lst.size();
				numbers = totalSize;
				for (int index = 0; index < lst.size(); index++) {
					WessionServerSession sm = (WessionServerSession) lst.get(index);
					setJsonData(sb, numbers--, sm);
				}
			} else {
				
			}
		} else {
			Iterator<ISession> itor = s_mgr.iterator();
			// 기본 시간까지는 무조건 후퇴함
			
			long now =  System.currentTimeMillis();
			long due = back_minutes * 60* 1000;
			while (itor.hasNext()) {
				WessionServerSession sm = (WessionServerSession) itor.next();
				SSOAuth auth = (SSOAuth) sm.getAttribute(Const.WSO_Auth);
				String [] a1 = (auth.getAuthTime()).split(",");
				String a = a1[1];
				long aa = 0l;
				if (a == null || a.equals("")) {
					aa = 0l;
				} else {
					aa = Long.parseLong(a);
				}
				//System.out.println(now - aa);
				//System.out.println((now-aa) + " : " + due + " - " + ((now - aa) - due));
				if ((now - aa) > due) {
					cutover++;
					//numbers--;
					//setJsonData(sb, numbers--, sm);
					break;
				}
				cutover++;
			}
			
			numbers = numbers - cutover;
			while (itor.hasNext()) {
				//System.out.println(offset-1);
				while (itor.hasNext() && i < (offset-1)) {
					i++;
					WessionServerSession sm = (WessionServerSession) itor.next();
					numbers--;
					if (i > (offset-1)) {
						setJsonData(sb, numbers--, sm);
					}
				}
				if (pages < (pageBreaker))
					break;
				pageBreaker++;
				WessionServerSession sm = (WessionServerSession) itor.next();

				setJsonData(sb, numbers--, sm);

			}
		}
	}
	if (sb.length() > 2)
		sb.delete(sb.length() - 2, sb.length());
%>
{ "total" :
<%=totalSize - cutover%>, "repos": [
<%=sb%>
] }
