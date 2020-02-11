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
	
	public static void setJsonData(StringBuilder sb, int number, ConfigAgent agent) {
		// 순번 표시
		sb.append("{").append("\"no\":\"").append(number).append("\"");

		sb.append(",\"system\":\"").append(agent.getAppCode()).append("\"");
		sb.append(",\"sysname\":\"").append(agent.getAppName()).append("\"");
		sb.append(",\"host\":\"").append(agent.getAppServerHost()).append("\"");
		sb.append(",\"serverip\":\"").append(agent.getAppServerIP()).append("\"");
		sb.append(",\"instance\":\"").append(agent.getAppInstance()).append("\"");
		sb.append(",\"port\":\"").append(agent.getAppServerPort()).append("\"");
		sb.append(",\"agentsize\":\"").append(agent.getAgentSize()).append("\"");
		sb.append(",\"dummysize\":\"").append(agent.getDummySize()).append("\"");
		sb.append(",\"ioup\":\"").append(agent.getAgentIO()).append("\"");
		sb.append(",\"iodown\":\"").append(agent.getAgentIOS()).append("\"");
		sb.append(",\"license\":\"").append(agent.getLicenseName()).append("\"");
		sb.append(",\"vmstatus\":\"").append(agent.getAgentVM()).append("\"");

		sb.append("},\n");
	}%>
<%
	StringBuilder sb = new StringBuilder();
	Environment env = Environment.getInstance();
	long trans_timeout = 2000;
	//String path = (String) System.getProperty("path");
	//mon.setMonitorDataPath(path + "/monitor.dat");
	//mon.initialize();
	ConcurrentHashMap<String, ConfigAgent> agents = env.getAgentList();

	Set<String> setlic = env.getAgentListSet();
	String[] licenses = (String[]) setlic.toArray(new String[setlic.size()]);
	Arrays.sort(licenses);
	for (int i = 0; i < agents.size(); i++) {
		setJsonData(sb, i + 1, agents.get(licenses[i]));
		
		try {
			ConfigAgent agent =  agents.get(licenses[i]);
			
			String monitor_url = agent.getAppMonitorURL();
			String appCode = agent.getAppName();
			
			//monitor_url = monitor_url.replace("ws_monitor.jsp", "services");
			//monitor_url = "http://localhost:9093/meritz_app/wession/services";
						
			WessionAgentSession iMonitor = new WessionAgentSession(session.getId(), "agent", "monitor");
			iMonitor.setAttribute(Const.CODE, Const.CODE_FAIL);
			iMonitor.setAttribute(Const.CMD, "MONITOR");
			
			ISessionMessage monitor_msg = new WessionAgentMessage(3, iMonitor);
			monitor_msg = transer.trans(monitor_url, monitor_msg, trans_timeout);

			String usedMem = (String) monitor_msg.getSession().getAttribute("VM_USED_MEMORY");
			String freeMem = (String) monitor_msg.getSession().getAttribute("VM_FREE_MEMORY");
			String agentsize = (String) monitor_msg.getSession().getAttribute("AGENT_SIZE");
			String normalsize = "0";
			String dummysize = (String) monitor_msg.getSession().getAttribute("DUMMY_SIZE");
			String smartque = (String) monitor_msg.getSession().getAttribute("AGENT_IO");
			
			agent.setAgentIO(Integer.parseInt(smartque==null?"0":smartque));
			agent.setAgentVM(Integer.parseInt(usedMem==null?"0":usedMem));
			agent.setAgentSize(Integer.parseInt(agentsize==null?"0":agentsize ));
			agent.setDummySize(Integer.parseInt(dummysize==null?"0":dummysize ));
			
			agents.put(licenses[i], agent);

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	if (sb.length() > 2)
		sb.delete(sb.length() - 2, sb.length());
%>
[
<%=sb%>
]
