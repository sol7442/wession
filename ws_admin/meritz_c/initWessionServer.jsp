<%@page import="com.wession.Const"%>
<%@page import="com.wow.wession.server.WessionServerSession"%>
<%@page import="com.wow.wession.session.ISession"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.wow.wession.util.CustomScheduler"%>
<%@page import="com.wow.wession.session.WessionSessionManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.wow.wession.WessionDaemon"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Vector"%>
<%@page import="com.wession.Environment"%>
<%@page import="com.wession.DBUtil"%>
<%@page import="com.wession.Common"%>

<%@ page import="org.slf4j.Logger"%>
<%@ page import="org.slf4j.LoggerFactory"%>
<%!private WessionDaemon	deamon	 = null;
	private static String instanceID	= null;
	private static String JDBC_URL = "";
	private CustomScheduler schedule_old_delete = null;
	
	public void jspInit() {
		instanceID = System.getProperty("WAIN");
		String DaemonName = getServletConfig().getInitParameter("daemon.name." + instanceID);
		String configFileName = getServletConfig().getInitParameter("server.config." + instanceID);
		String path = getServletConfig().getInitParameter("path." + instanceID);
		String logPath = getServletConfig().getInitParameter("log.path." + instanceID);
		
		String instance1 = getServletConfig().getInitParameter("instance.1");
		String instance2 = getServletConfig().getInitParameter("instance.2");
		
		String policy_mins = getServletConfig().getInitParameter("anticipated.min");
		
		JDBC_URL = getServletConfig().getServletContext().getInitParameter("JDBC_URL");
		
		System.out.println("=================================================================================");
		System.out.println("    we get instanceID-" + instanceID + " and DaemonName is " + DaemonName);
		System.out.println("           path = " + path);
		System.out.println("           JDBC_URL = " + JDBC_URL);
		System.out.println("=================================================================================");

		// 환경설정 초기화
		System.setProperty("path", path);
		System.setProperty("JDBC_URL", JDBC_URL);

		System.out.println("=================================================================================");
		System.out.println("    Setting Environment ");
		Environment env = Environment.getInstance();
		env.setEnvDataPath(path + "/environment.dat");
		env.setEnvironment();
		System.out.println("         agent List : " + env.getAgentList().size());
		System.out.println("=================================================================================");
		String envpath = "";
		
		// =========================================================================================
		// ============   DAEMON SETTING
		// =========================================================================================

		deamon = new WessionDaemon(DaemonName, logPath);
		
		System.out.println(path);
		
		if (deamon.initialize(path + configFileName, path + "/config/log4j.xml")) {
			deamon.start();
		} else {
			System.out.println("Wession System Start Exception");
			System.exit(0);
		}
		
		WessionSessionManager s_mgr = WessionSessionManager.getInstance();
		
		String serviceURL = getServletConfig().getInitParameter("serviceURL." + instanceID);
		String monitorURL = getServletConfig().getInitParameter("monitorURL." + instanceID);
		
		s_mgr.setProperty("instance.1", instance1, false);
		s_mgr.setProperty("instance.2", instance2, false);
		
		s_mgr.setProperty("serviceURL." + instance1, getServletConfig().getInitParameter("serviceURL." + instance1), false);
		s_mgr.setProperty("monitorURL." + instance1, getServletConfig().getInitParameter("monitorURL." + instance1), false);

		s_mgr.setProperty("serviceURL." + instance2, getServletConfig().getInitParameter("serviceURL." + instance2), false);
		s_mgr.setProperty("monitorURL." + instance2, getServletConfig().getInitParameter("monitorURL." + instance2), false);
		
		
		s_mgr.setProperty("ANTICIPATED_MIN", policy_mins, true);
		s_mgr.setProperty("logPath", logPath, false);

		try{
			//int delay_time = 10 * 60 * 1000;    // 기동후 10분후에 첫번째 배치 실행
			//int duration = 12 * 60 * 60 * 1000; // 12 시간에 1번씩 실행
			
			int delay_time 	= 0;    		// 기동 후  즉시 실행
			int duration 	= 60 * 1000; 	// 1분에 1번씩 실행
			
			if (schedule_old_delete == null) {
				
				schedule_old_delete = new CustomScheduler("Delete_Old_Wession", new Runnable() {
					int count = 0;
					int i = 0;
					
					public void run() {
						WessionSessionManager s_mgr = WessionSessionManager.getInstance();

						int removecount = 0;
						
						// 얼마가 지난 것을 지울 것인가 (분단위로 입력)
						String policy_minutes = s_mgr.getProperty("ANTICIPATED_MIN"); 
						int back_minutes = 0;
						if (policy_minutes == null || policy_minutes.equals("")) {
							back_minutes = 60;
						} else {
							back_minutes = Integer.parseInt(policy_minutes);
						}
						// 소스레벨에서 관리한다면 이곳을 이용함 
						// back_minutes = 2 * 24 * 60; // 2일이 지난것들은 지움
						
						if (s_mgr != null) {
							
							Iterator<ISession> itor = s_mgr.iterator();
							// 기본 시간까지는 무조건 후퇴함
							long now =  System.currentTimeMillis();
							long due = back_minutes * 60 * 1000;
							
							while (itor.hasNext()) {
								WessionServerSession sm = (WessionServerSession) itor.next();
								long aa = sm.getCreateTime();
								//System.out.println(sm);
								if ((now - aa) > due) {
									//System.out.println("Remove Wession : " + sm.getID());
									String _appcode = (String) sm.getAttribute(Const.APP_CODE);

									// 양쪽서버에서 모두 배치가 동작하므로 그냥 지워야 함. 클라이언트로 보낼 필요 없음
									//s_mgr.expire(sm.getID());
									s_mgr.remove(sm.getID(),true); 
									removecount++;	
								}
							}
							
							System.out.println("Total Wession Count : " + s_mgr.getSize());
							System.out.println("Remove Wession Count : " + removecount);
						}
						
						System.out.println(count++);
					}
				});
				schedule_old_delete.start(delay_time, duration);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}

		System.out.println("=================================================");
		System.out.println("====== DONE INIIALIZE WESSION SERVER " + instanceID);
		System.out.println("======      SERVICE URL :  " + serviceURL);
		System.out.println("======      MONITOR URL :  " + monitorURL);
		System.out.println("=================================================");

	}

	public void jspDestroy() {
		if (deamon != null) {
			deamon.stop();
			System.out.println("=================================================");
			System.out.println("====== STOPPED WESSION SERVER " + instanceID);
			System.out.println("=================================================");
		}
		if( schedule_old_delete != null){
			schedule_old_delete.stop();
		}
	}

%>