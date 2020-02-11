<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.wow.wession.agent.*"%>
<%@page import="com.wow.wession.session.ISession"%>
<%@page import="com.wow.wession.session.ISessionMessage"%>
<%@page import="com.wow.wession.session.WessionSessionManager"%>
<%@page import="com.wow.wession.server.WessionServerSession"%>
<%@page import="com.wession.Environment"%>
<%@page import="com.wession.Common"%>
<%@page import="com.wession.Const"%>
<%@ page import="org.slf4j.Logger"%>
<%@ page import="org.slf4j.LoggerFactory"%>
<%!// 다른 서버에서 메시지를 전송할 필요가 있을 경우 선
	private WessionTranser	transer;

	public void jspInit() {
		transer = new WessionTranser();
		transer.initialize(20); // trans용 Thread를 10개만 가지고 있음
	}
	
	public void jspDestroy() {
		transer.stop();
	}

%><%

	// 들어오는 파라미터를 분석하여 처리함
/*
	Map<String, String[]> parameters = request.getParameterMap();
	for (String parameter : parameters.keySet()) {
		System.out.println(parameter + ":" + request.getParameter(parameter));
	}
*/
	WessionSessionManager s_mgr = WessionSessionManager.getInstance();
	Environment env = Environment.getInstance();
	ISessionMessage trans_msg = null;
	long trans_timeout = 2000;
	Logger processLog = LoggerFactory.getLogger("process");

	String wsid = request.getParameter("wsid") + "";
	String method = request.getParameter("method") + "";
	String methodType = request.getParameter("methodType") + "";
	
	String jsonCode = "fail";
	String jsonMsg = "";

	if ("del".equals(method)) {
		if ("all".equals(methodType)) { // 전체 클리어
			s_mgr.clear();
			jsonCode = "success";
			jsonMsg = "모든 Wession을 삭제하였습니다.";
		} else {
			String[] delete_wsid = wsid.split(";");
						
			for (int i = 0; i < delete_wsid.length; i++) {
				// 연결되어 있는 것들을 모두 지운다.
				WessionServerSession d_sm = (WessionServerSession) s_mgr.get(delete_wsid[i]);
				Set<String> agentSet = d_sm.getAgentSessionKeySet();
				HashMap<String, String> agentCode = new HashMap<String, String>();
				// WessionAgentSession 목록을 가지고 옴
				List<ISession> d_agent = d_sm.getAgentSessionList();
								
				if (d_agent.size() > 0) {
					for (int index = 0; index < d_agent.size(); index++) {
						WessionAgentSession destory_session = (WessionAgentSession) d_agent.get(index);
						destory_session.setAttribute(Const.CMD, Const.STATUS_DESTORY_SWESSION);
						Vector<String> v = env.getAgentServiceURL(destory_session.getAppCode());
						//System.out.println("=======> destory_session.getAppCode() : " + destory_session.getAppCode());
						//System.out.println("\t" + destory_session.getAppCode() + ":" + v.size());
						
						
						for (String sendURL : v) {
							//System.out.println("\t" + sendURL);
							
							ISessionMessage destroy_msg = new WessionAgentMessage(3, destory_session);
							try {
								trans_msg = transer.trans(sendURL, destroy_msg, trans_timeout);
								//System.out.println(trans_msg);
								//System.out.println("Remove Agent Session \n\tsendURL : " + sendURL);
								env.addAgentSmartQue(sendURL, true);
								processLog.info("[wession_fdw.jsp] Admin Kill Server Session : receive message from " + destory_session.getAppCode() + " | " + delete_wsid[i]);
							}
							catch (TransException te) {
								te.printStackTrace();
								env.addAgentSmartQue(sendURL, false);
							}
							catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				s_mgr.expire(delete_wsid[i]);
				processLog.info(i + ") Wession Server Session Closed : " + delete_wsid[i]);
				
				jsonCode = "success";
				jsonMsg = "삭제하였습니다.";
				
				Common.addServerMonitor(Const.MNO_Wession_Expire_Count);
			}
		}
	}
%>
{ "result" : "<%= jsonCode %>", "message" : "<%= jsonMsg %>" }
