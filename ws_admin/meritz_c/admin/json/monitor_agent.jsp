<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.util.concurrent.ConcurrentHashMap"%>
<%@page import="com.wow.wession.agent.*"%>
<%@page import="com.wow.wession.session.ISessionMessage"%>
<%@page import="com.wession.Environment"%>
<%@page import="com.wession.Const"%>
<%@page import="com.wession.model.ConfigAgent"%>
<%@page import="com.wow.wession.session.WessionSessionManager"%>

<%!// 다른 서버에서 메시지를 전송할 필요가 있을 경우 
	private WessionTranser	transer;

	public void jspInit() {
		//System.out.println("jspInit count");
		transer = new WessionTranser();
		transer.initialize(20); // trans용 Thread를 10개만 가지고 있음
	}

	public void jspDestroy() {
		transer.stop();
	}
	
	public static String nvl(String str) {
		return str==null?"0":str;
	}
	%>
<%

	WessionSessionManager s_mgr = WessionSessionManager.getInstance();
	long trans_timeout = 100;
	
	StringBuffer sb = new StringBuffer();
	

	// 기본 정보를 이용하여 Server의 Property에 상태를 기록함
	Environment env = Environment.getInstance();
	Set <String>keys = env.getAgentListSet();
	ConcurrentHashMap <String, ConfigAgent> chm = env.getAgentList();
	
	String appCode = "";
	String instance = "";
	String monitor_url = "";
	for (String licnese: keys) {
		try {
			ConfigAgent agent = chm.get(licnese);
			
			monitor_url = agent.getAppMonitorURL();
			appCode = agent.getAppName();
			instance = agent.getAppInstance();
			
			WessionAgentSession iMonitor = new WessionAgentSession(session.getId(), "agent", "monitor");
			iMonitor.setAttribute(Const.CODE, Const.CODE_FAIL);
			iMonitor.setAttribute(Const.CMD, "MONITOR");
			
			//System.out.println(monitor_url + " / " + appCode + " / " + instance);
			
			ISessionMessage monitor_msg = new WessionAgentMessage(3, iMonitor);
			monitor_msg = transer.trans(monitor_url, monitor_msg, trans_timeout);

			String usedMem = (String) monitor_msg.getSession().getAttribute("VM_USED_MEMORY");
			String freeMem = (String) monitor_msg.getSession().getAttribute("VM_FREE_MEMORY");
			String agentsize = (String) monitor_msg.getSession().getAttribute("AGENT_SIZE");
			String normalsize = "0";
			String dummysize = (String) monitor_msg.getSession().getAttribute("DUMMY_SIZE");
			String smartque = (String) monitor_msg.getSession().getAttribute("AGENT_IO");
			
			int smartque_down = agent.getAgentIOS();
			
			sb.append("{").append("\"license\":\"").append(licnese).append("\"");
			sb.append(",\"app_code\":\"").append(nvl(appCode)).append("\"");
			sb.append(",\"instance\":\"").append(nvl(instance)).append("\"");
			sb.append(",\"used_mem\":\"").append(nvl(usedMem)).append("\"");
			sb.append(",\"free_mem\":\"").append(nvl(freeMem)).append("\"");
			sb.append(",\"agent_size\":\"").append(nvl(agentsize)).append("\"");
			sb.append(",\"normal_size\":\"").append(nvl(normalsize)).append("\"");
			sb.append(",\"dummy_size\":\"").append(nvl(dummysize)).append("\"");
			sb.append(",\"smart_que\":\"").append(nvl(smartque)).append("\"");
			sb.append(",\"smart_que2\":\"").append(smartque_down).append("\"");
			sb.append("},\n");

		}
		catch (Exception e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
			
			sb.append("{").append("\"license\":\"").append(licnese).append("\"");
			sb.append(",\"app_code\":\"").append(appCode).append("\"");
			sb.append(",\"instance\":\"").append("-").append("\"");
			sb.append(",\"used_mem\":\"").append("-").append("\"");
			sb.append(",\"free_mem\":\"").append("-").append("\"");
			sb.append(",\"agent_size\":\"").append("-").append("\"");
			sb.append(",\"normal_size\":\"").append("-").append("\"");
			sb.append(",\"dummy_size\":\"").append("-").append("\"");
			sb.append(",\"smart_que\":\"").append("-").append("\"");
			sb.append(",\"smart_que2\":\"").append("-").append("\"");
			sb.append("},\n");
			
		}
	}

	if (sb.length() > 2)
		sb.delete(sb.length() - 2, sb.length());
%>
[ 
<%=sb%>
]
