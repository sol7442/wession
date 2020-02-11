<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.wow.wession.session.ISession"%>
<%@page import="com.wow.wession.session.WessionSessionManager"%>
<%@page import="com.wow.wession.server.WessionServerSession"%>
<%@page import="com.wow.wession.agent.WessionAgentSession"%>
<%@page import="com.wession.Const"%>
<%@page import="com.wession.Common"%>
<%@page import="com.wession.model.meritz.User"%>
<%@page import="com.wession.model.meritz.SSOAuth"%>
<%@page import="com.wession.model.meritz.Customer"%>
<%@page import="com.wession.model.meritz.CustomerSession"%>
<%!
	public static void setJsonData(StringBuilder sb, int number, WessionServerSession sm) {
		// 순번 표시
		sb.append("{").append("\"no\":\"").append(number).append("\"");

		// 사용자사번 표시
		sb.append(",\"userid\":\"").append(sm.getUser()).append("\"");

		// 사용자성명 표시
		String userName = "없음";
		User user = (User) sm.getAttribute(Const.WSO_User);
		if (user != null)
			userName = user.getUserName();
		sb.append(",\"name\":\"").append(userName).append("\"");

		// 각 업무시스템별 접속시간 표시
		String homepage_AccessTime = "-";
		String mhomepage_AccessTime = "-";
		
		String direct_AccessTime = "-";
		String mdirect_AccessTime = "-";
		
		List<ISession> agent = sm.getAgentSessionList();
		for (int index = 0; index < agent.size(); index++) {
			WessionAgentSession agent_session = (WessionAgentSession) agent.get(index);
			//System.out.println("["+ index +"] "+agent_session.toString());
			if (agent_session.getAppCode() != null && (agent_session.getAppCode()).contains("HPS") ) {
				CustomerSession css = (CustomerSession) agent_session.getAttribute("CSESSION");
				homepage_AccessTime = Common.getDateFromLAT(agent_session.getCreateTime())+ "<br/>" + css.getLoginIP();
			}
			if (agent_session.getAppCode() != null && (agent_session.getAppCode()).contains("MHP") ) {
				CustomerSession css = (CustomerSession) agent_session.getAttribute("CSESSION");
				mhomepage_AccessTime = Common.getDateFromLAT(agent_session.getCreateTime())+ "<br/>" + css.getLoginIP();
			}
				
			if (agent_session.getAppCode() != null && (agent_session.getAppCode()).contains("CMS") ) {
				CustomerSession css = (CustomerSession) agent_session.getAttribute("CSESSION");
				direct_AccessTime = Common.getDateFromLAT(agent_session.getCreateTime()) + "<br/>" + css.getLoginIP();
			}
			if (agent_session.getAppCode() != null && (agent_session.getAppCode()).contains("MCM") ) {
				CustomerSession css = (CustomerSession) agent_session.getAttribute("CSESSION");
				mdirect_AccessTime = Common.getDateFromLAT(agent_session.getCreateTime())+ "<br/>" + css.getLoginIP();
			}

		}

		sb.append(",\"homepage\":\"").append(homepage_AccessTime).append("\"");
		sb.append(",\"mhomepage\":\"").append(mhomepage_AccessTime).append("\"");
		sb.append(",\"directmall\":\"").append(direct_AccessTime).append("\"");
		sb.append(",\"mdirectmall\":\"").append(mdirect_AccessTime).append("\"");
		
		// 삭제 등의 데이터 조작을 위한 wsid
		sb.append(",\"wsid\":\"").append(sm.getID()).append("\"");
		
		// 기본데이터
		sb.append(",\"remark\":\"").append("").append("\"");

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
	int numbers = s_mgr.getSize();
	int totalSize = s_mgr.getSize();

	int pages = Integer.parseInt(pLimit);
	int offset = Integer.parseInt(pOffset) + 1;
	int pageBreaker = 1;
	int i = 0;
	if (s_mgr != null) {
		if (pSearch != null && !"".equals(pSearch)) {
			List<ISession> lst = s_mgr.getBy("UserID", pSearch);
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
			while (itor.hasNext()) {
				while (itor.hasNext() && i < (offset-1)) {
					i++;
					itor.next();
					numbers--;
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
<%=totalSize%>, "repos": [
<%=sb%>
] }
