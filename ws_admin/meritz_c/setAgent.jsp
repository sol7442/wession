<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.wession.Environment"%>
<%@page import="com.wession.model.ConfigAgent"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.concurrent.ConcurrentHashMap"%>
<%
	String path = (String) System.getProperty("path");

	Environment env = Environment.getInstance();
	env.setEnvDataPath(path + "/environment.dat");
	env.initialize();
	String[][] data = {{}};
	
	String[][] data_local = { 
			{ "wession_license_01", "CMS", "wession", "127.0.0.1", "18090", "http://localhost:18090/wession/services", "WSSVR01", "http://localhost:18090/wession/services" },
			{ "wession_license_02", "HPS", "wession", "127.0.0.1", "18095", "http://localhost:18095/wession/services", "WSSVR01", "http://localhost:18095/wession/services" }
		};

	String[][] data_dev = { 
			{ "meritzfire_D_01", "INITECHSSO", "eptsvd01", "10.91.58.72", "17699", "http://10.91.58.72:17699/wession/ws_service.jsp", "ssoSoDWas101", "http://10.91.58.72:17699/meritz_app/wession/ws_monitor.jsp" },
	        { "meritzfire_D_02", "BIZDEV", "eptsvd01", "10.91.58.72", "17801", "http://10.91.58.72:17801/wession/ws_service.jsp", "eptEpDWas101", "http://10.91.58.72:17801/wession/ws_monitor.jsp" },
	        { "meritzfire_D_03", "BIZDEV", "eptsvd01", "10.91.58.72", "17802", "http://10.91.58.72:17802/wession/ws_service.jsp", "eptEpDWas102", "http://10.91.58.72:17801/wession/ws_monitor.jsp" },
	        { "meritzfire_D_04", "SALESDEV", "eptsvd01", "10.91.58.72", "18201", "http://10.91.58.72:18201/wession/ws_service.jsp", "sptSpDWas101", "http://10.91.58.72:18201/wession/ws_monitor.jsp" } };
	
	String[][] data_test = { 
	        { "meritzfire_T_01", "INITECHSSO", "eptsvt01", "10.91.59.72", "17699", "http://10.91.59.72:17699/3rdParty/wession/ws_service.jsp", "ssoSoTWas101", "http://10.91.58.72:17699/meritz_app/3rdParty/wession/ws_monitor.jsp" },
	        { "meritzfire_T_02", "BIZTEST", "eptsvt01", "10.91.59.72", "17801", "http://10.91.59.72:17801/portal/wession/services", "eptEpTWas101", "http://10.91.59.72:17801/portal/wession/services" },
	        { "meritzfire_T_03", "BIZTEST", "eptsvt01", "10.91.59.72", "17802", "http://10.91.59.72:17802/portal/wession/services", "eptEpTWas102", "http://10.91.59.72:17802/portal/wession/services" },
	        { "meritzfire_T_04", "SALESTEST", "eptsvdt01", "10.91.59.72", "18201", "http://10.91.59.72:18201/salesportal/wession/services", "sptSpTWas101", "http://10.91.59.72:18201/salesportal/wession/services" } };
	
	String[][] data_prod = { 
			{ "meritzfire_P_01", "INITECHSSO", "ssoapp01", "10.91.51.58", "18531", "http://10.91.51.58:18531/3rdParty/wession/services"     , "ssoSoPWas101", "http://10.91.51.58:18531/3rdParty/wession/services" },
	        { "meritzfire_P_02", "INITECHSSO", "ssoapp01", "10.91.51.58", "18532", "http://10.91.51.58:18532/3rdParty/wession/services"     , "ssoSoPWas102", "http://10.91.51.58:18532/3rdParty/wession/services" },
	        { "meritzfire_P_03", "INITECHSSO", "ssoapp02", "10.91.51.59", "18531", "http://10.91.51.59:18531/3rdParty/wession/services"     , "ssoSoPWas201", "http://10.91.51.59:18531/3rdParty/wession/services" },
	        { "meritzfire_P_04", "INITECHSSO", "ssoapp02", "10.91.51.59", "18532", "http://10.91.51.59:18532/3rdParty/wession/services"     , "ssoSoPWas202", "http://10.91.51.59:18532/3rdParty/wession/services" },
	        { "meritzfire_P_05", "BIZ01"     , "eptapp01", "10.91.51.72", "18511", "http://10.91.51.72:18511/portal/wession/services"       , "eptEpPWas101", "http://10.91.51.72:18511/portal/wession/services" }, 
			{ "meritzfire_P_06", "BIZ02"     , "eptapp01", "10.91.51.72", "18512", "http://10.91.51.72:18512/portal/wession/services"       , "eptEpPWas102", "http://10.91.51.72:18512/portal/wession/services" },
			{ "meritzfire_P_07", "BIZ03"     , "eptapp02", "10.91.51.73", "18511", "http://10.91.51.73:18511/portal/wession/services"       , "eptEpPWas201", "http://10.91.51.73:18511/portal/wession/services" }, 
			{ "meritzfire_P_08", "BIZ04"     , "eptapp02", "10.91.51.73", "18512", "http://10.91.51.73:18512/portal/wession/services"       , "eptEpPWas202", "http://10.91.51.73:18512/portal/wession/services" },
	        { "meritzfire_P_09", "SALES01"   , "sptapp01", "10.91.51.78", "18521", "http://10.91.51.78:18511/salesportal/wession/services"  , "sptSpPWas101", "http://10.91.51.78:18521/salesportal/wession/services" }, 
			{ "meritzfire_P_10", "SALES02"   , "sptapp01", "10.91.51.78", "18522", "http://10.91.51.78:18512/salesportal/wession/services"  , "sptSpPWas102", "http://10.91.51.78:18522/salesportal/wession/services" },
			{ "meritzfire_P_11", "SALES03"   , "sptapp02", "10.91.51.79", "18521", "http://10.91.51.79:18511/salesportal/wession/services"  , "sptSpPWas201", "http://10.91.51.79:18521/salesportal/wession/services" }, 
			{ "meritzfire_P_12", "SALES04"   , "sptapp02", "10.91.51.79", "18522", "http://10.91.51.79:18512/salesportal/wession/services"  , "sptSpPWas202", "http://10.91.51.79:18522/salesportal/wession/services" } 
		};
	
	String WAIN = System.getProperty("WAIN");
	
	System.out.println("WAIN = " + WAIN);

	if (WAIN.contains("DWas")) {
		data = data_dev;
	} else if (WAIN.contains("TWas")) {
		data = data_test;
	} else if (WAIN.contains("PWas")) {
		data = data_prod;
	} else if (WAIN.contains("WSSVR")) {
		data = data_local;
	} else {
		data = data_dev;
	}
	
	ConcurrentHashMap<String, ConfigAgent> agentList = new ConcurrentHashMap<String, ConfigAgent>();

	String command = request.getParameter("cmd")==null?"":request.getParameter("cmd");
	String result = "Browse Environment";
	
	if (command.equals("default")) {
	
	env.clearAgentList();
	
	for (int i = 0; i < data.length; i++) {
		ConfigAgent agent = new ConfigAgent();
		agent.setLicenseName(data[i][0]);
		agent.setAppCode(data[i][1]);
		agent.setAppName(data[i][1]);
		agent.setAppServerHost(data[i][2]);
		agent.setAppServerIP(data[i][3]);
		agent.setAppServerPort(data[i][4]);
		agent.setAppServiceURL(data[i][5]);
		agent.setAppInstance(data[i][6]);
		agent.setAppMonitorURL(data[i][7]);

		agent.setAgentSize(0);
		agent.setAgentVM(0);

		agent.setDummySize(0);
		agent.setCommStatus(0);
		agent.setAgentIO(0);

		env.setConfigAgent(agent);
	}

	env.writeEnv();
	
	result = "Setting Default";
	
	} else if (command.equals("manual")){
		
		env.clearAgentList();
		
		 String manual_setting = request.getParameter("manual_setting");
		 String[] data_manual = manual_setting.trim().split("\n");
		 for (int i = 0; i < data_manual.length; i++) {
			String [] datum_manual = data_manual[i].trim().split(",|\t");
			//System.out.println("datum_manual["+i+"]" + datum_manual.length );
			
			ConfigAgent agent = new ConfigAgent();
			agent.setLicenseName(datum_manual[0]);
			agent.setAppCode(datum_manual[1]);
			agent.setAppName(datum_manual[1]);
			agent.setAppServerHost(datum_manual[2]);
			agent.setAppServerIP(datum_manual[3]);
			agent.setAppServerPort(datum_manual[4]);
			agent.setAppServiceURL(datum_manual[5]);
			agent.setAppInstance(datum_manual[6]);
			agent.setAppMonitorURL(datum_manual[7]);
			
			agent.setAgentSize(0);
			agent.setAgentVM(0);
			
			agent.setDummySize(0);
			agent.setCommStatus(0);
			agent.setAgentIO(0);
			
			env.setConfigAgent(agent);
		 }
		 
		 env.writeEnv();
		 result = "Setting Manual";
	}
%>
<html>
<head>
<title>Environment Setting</title>
</head>
<body>
<h2><%= result %></h2>
<table border=1 cellpadding=5>
	<tr>
		<th>no</th>
		<th>license</th>
		<th>app code</th>
		<th>host name</th>
		<th>server ip</th>
		<th>port</th>
		<th>service url</th>
		<th>instance name</th>
		<th>monitor url</th>
	</tr>
	<%
		Set<String> keyset = env.getAgentListSet();
		int i = 1;
		for (String license : keyset) {
			ConfigAgent agent = env.getConfigAgent(license);
	%>
	<tr>
		<td><%=i++%>
		<td><%=license%></td>
		<td><%=agent.getAppCode()%></td>
		<td><%=agent.getAppServerHost()%></td>
		<td><%=agent.getAppServerIP()%></td>
		<td><%=agent.getAppServerPort()%></td>
		<td><%=agent.getAppServiceURL()%></td>
		<td><%=agent.getAppInstance()%></td>
		<td><%=agent.getAppMonitorURL()%></td>
	</tr>
	<%
		}
	%>
</table>
<hr/>
<input type="button" name="btnShows" value=" 수동등록 UI " onclick="javascript:toggleUI()" />
<div id="manual_input" style="display:none">
    <form name="form1" method="post" action="setAgent.jsp">
    sample<br/>
    <i style="font-size:11px;"> 
    	wession_license_01, 
    	DEMOAPP, 
    	wession, 
    	127.0.0.1, 
    	9093, 
    	http://localhost:9093/meritz_app/wession/services, 
    	DEMO_INST01, 
    	http://localhost:9093/meritz_app/wession/services
    </i>
    <br/>
	<textarea name="manual_setting" style="width:80%; height:50%;"></textarea>
	<br/>
	<input type="submit" value =" 반영 " />
	<input type="hidden" name="cmd" value="manual"/>
	</form>
</div>
<script>
function toggleUI() {
	var yourUl = document.getElementById("manual_input");
	yourUl.style.display = yourUl.style.display === 'none' ? '' : 'none';
}
</script>

</body>
</html>

