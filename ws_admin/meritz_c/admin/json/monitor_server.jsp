<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.wow.wession.agent.*"%>
<%@page import="com.wow.wession.session.ISessionMessage"%>
<%@page import="com.wow.wession.session.WessionSessionManager"%>
<%@page import="com.wession.*"%>
<%@page import="java.util.*"%>
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
	WessionSessionManager s_mgr = WessionSessionManager.getInstance();
	Environment env = Environment.getInstance();
	
	String instanceID = System.getProperty("WAIN");
	String appCode = Const.STATUS_NO_APPCODE;
	String UsedMemory_1 = "0";
	String FreeMemory_1 = "0";
	
	String UsedMemory_2 = "0";
	String FreeMemory_2 = "0";
	
	String ssoio_1 = "0";
	String ssoio_2 = "0";
	
	long trans_timeout = 100;
	int agentsize = 0;

	String CREATE_CNT_1 = "0";
	String CREATE_CNT_2 = "0";
	
	String WSS_REQ_CNT_1 = "0";
	String WSS_REQ_CNT_2 = "0";

	String REMOVE_CNT_1 = "0";
	String REMOVE_CNT_2 = "0";
	
	String DUPLOG_CNT_1 = "0";
	String DUPLOG_CNT_2 = "0";
	
	String AA_CNT_1 = "0";
	String AL_CNT_1 = "0";
	String DA_CNT_1 = "0";
	String DL_CNT_1 = "0";

	String AA_CNT_2 = "0";
	String AL_CNT_2 = "0";
	String DA_CNT_2 = "0";
	String DL_CNT_2 = "0";
	
	try {
		
		String instance = s_mgr.getProperty("instance.1");
		String url = s_mgr.getProperty("monitorURL" + "." + instance);
		
		AA_CNT_1 = nvl(s_mgr.getProperty(Common.getPropName("AA_CNT", instance)));
		AL_CNT_1 = nvl(s_mgr.getProperty(Common.getPropName("AL_CNT", instance)));
		DA_CNT_1 = nvl(s_mgr.getProperty(Common.getPropName("DA_CNT", instance)));
		DL_CNT_1 = nvl(s_mgr.getProperty(Common.getPropName("DL_CNT", instance)));
		
		WSS_REQ_CNT_1 = nvl(s_mgr.getProperty(Common.getPropName("WSS_REQ_CNT", instance)));
		CREATE_CNT_1 = nvl(s_mgr.getProperty(Common.getPropName(Const.MNO_Wession_Create_Count, instance)));
		REMOVE_CNT_1 = nvl(s_mgr.getProperty(Common.getPropName(Const.MNO_Wession_Expire_Count, instance)));
		DUPLOG_CNT_1 = nvl(s_mgr.getProperty(Common.getPropName("DUPLOG_CNT", instance)));
		
		if (url == null) {
			System.out.println("   ====> monitorURL." + instance + " URL Not Setting.");
		} else {
			WessionAgentSession iMonitor = new WessionAgentSession(session.getId(), "server", "monitor");
			iMonitor.setAttribute(Const.CODE, Const.CODE_FAIL);
			ISessionMessage monitor_msg = new WessionAgentMessage(3, iMonitor);
			monitor_msg = transer.trans(url, monitor_msg, trans_timeout);
			
			if (monitor_msg != null) {
				UsedMemory_1 = (String) monitor_msg.getSession().getAttribute("UsedMemory");
				FreeMemory_1 = (String) monitor_msg.getSession().getAttribute("FreeMemory");
				//ssoio_1 = (String) monitor_msg.getSession().getAttribute("SSOIO");
			}
		}
		
	}
	catch (Exception e) {
		UsedMemory_1 = "50";
		FreeMemory_1 = "50";
		//e.printStackTrace();
	}
	
	try {
		String instance = s_mgr.getProperty("instance.2");
		String url = s_mgr.getProperty("monitorURL" + "." + instance);
		
		AA_CNT_2 = nvl(s_mgr.getProperty(Common.getPropName("AA_CNT", instance)));
		AL_CNT_2 = nvl(s_mgr.getProperty(Common.getPropName("AL_CNT", instance)));
		DA_CNT_2 = nvl(s_mgr.getProperty(Common.getPropName("DA_CNT", instance)));
		DL_CNT_2 = nvl(s_mgr.getProperty(Common.getPropName("DL_CNT", instance)));
		
		WSS_REQ_CNT_2 = nvl(s_mgr.getProperty(Common.getPropName("WSS_REQ_CNT", instance)));
		CREATE_CNT_2 = nvl(s_mgr.getProperty(Common.getPropName(Const.MNO_Wession_Create_Count, instance)));
		REMOVE_CNT_2 = nvl(s_mgr.getProperty(Common.getPropName(Const.MNO_Wession_Expire_Count, instance)));
		DUPLOG_CNT_2 = nvl(s_mgr.getProperty(Common.getPropName("DUPLOG_CNT", instance)));

		if (url == null) {
			System.out.println("   ====> monitorURL." + instance + " URL Not Setting.");
		} else {
			WessionAgentSession iMonitor = new WessionAgentSession(session.getId(), "server", "monitor");
			iMonitor.setAttribute(Const.CODE, Const.CODE_FAIL);
			ISessionMessage monitor_msg = new WessionAgentMessage(3, iMonitor);
			monitor_msg = transer.trans(url, monitor_msg, trans_timeout);
			if (monitor_msg != null) {
				UsedMemory_2 = (String) monitor_msg.getSession().getAttribute("UsedMemory");
				FreeMemory_2 = (String) monitor_msg.getSession().getAttribute("FreeMemory");
				//ssoio_1 = (String) monitor_msg.getSession().getAttribute("SSOIO");
			}
		}
	}
	catch (Exception e) {
		UsedMemory_2 = "50";
		FreeMemory_2 = "50";
		//e.printStackTrace();
	}
	
	HashMap <String, String> hm = new HashMap <String, String> ();
	Set <Object> keyset = s_mgr.getPropertyKeySet();

	int i = 0;

	for (Object key : keyset) {
		i++;
		hm.put((String) key, s_mgr.getProperty((String) key));
		//System.out.println("\t(" + i + ")" + (String) key + ":" + s_mgr.getProperty((String) key));
	}

%>
<%!
	public static String nvl(String str) {
		return (str==null?"0":str);
	}
%>
{
	"server1_jvm" : [
		{"label": "UsedMemory",	"data": "<%= UsedMemory_1 %>"},
		{"label": "FreeMemory",	"data": "<%= FreeMemory_1 %>"}
		]
	,
	"server2_jvm" : [
		{"label": "UsedMemory",	"data": "<%= UsedMemory_2 %>"},
		{"label": "FreeMemory",	"data": "<%= FreeMemory_2 %>"}
		]
	,
	"wession_count" : "<%= s_mgr.getSize() %>"
	,
	"sso_io_1" : "<%= ssoio_1 %>", "sso_io_2" : "<%= ssoio_2 %>"
	,
	"wession_create_count_1" : "<%= CREATE_CNT_1 %>", "wession_create_count_2" : "<%= CREATE_CNT_2 %>"
	,
	"wession_delete_count_1" : "<%= REMOVE_CNT_1 %>", "wession_delete_count_2" : "<%= REMOVE_CNT_2 %>"
	,
	"policy_AA_count_1" : "<%= AA_CNT_1 %>", "policy_AA_count_2" : "<%= AA_CNT_2 %>"
	,
	"policy_AL_count_1" : "<%= AL_CNT_1 %>", "policy_AL_count_2" : "<%= AL_CNT_2 %>"
	,
	"policy_DA_count_1" : "<%= DA_CNT_1 %>", "policy_DA_count_2" : "<%= DA_CNT_2 %>"
	,
	"policy_DL_count_1" : "<%= DL_CNT_1 %>", "policy_DL_count_2" : "<%= DL_CNT_2 %>"
	,
	"wession_request_count_1" : "<%= WSS_REQ_CNT_1 %>", "wession_request_count_2" : "<%= WSS_REQ_CNT_2 %>"
	,
	"duplogin_count_1" : "<%= DUPLOG_CNT_1 %>", "duplogin_count_2" : "<%= DUPLOG_CNT_2 %>"
}
