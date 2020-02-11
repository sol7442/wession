<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.wow.wession.agent.WessionTranser"%>
<%@page import="com.wow.wession.agent.WessionListener"%>
<%@page import="com.wow.wession.agent.WessionAgentSession"%>
<%@page import="com.wow.wession.agent.WessionAgentMessage"%>
<%@page import="com.wow.wession.agent.TransException"%>
<%@page import="com.wow.wession.server.WessionServerSession"%>
<%@page import="com.wow.wession.session.ISession"%>
<%@page import="com.wow.wession.session.ISessionMessage"%>
<%@page import="com.wow.wession.session.WessionSessionManager"%>
<%@page import="com.wession.Const"%>
<%@page import="com.wession.DBUtil"%>
<%@page import="com.wession.Environment"%>
<%@page import="com.wession.Common"%>
<%@page import="com.wession.policy.AdoptPolicy"%>
<%@page import="com.wession.policy.Matter"%>
<%@page import="com.wession.policy.meritz.AA"%>
<%@page import="com.wession.policy.meritz.AL"%>
<%@page import="com.wession.policy.meritz.DA"%>
<%@page import="com.wession.policy.meritz.DL"%>
<%@page import="com.wession.policy.meritz.SL"%>
<%@page import="com.wession.model.meritz.User"%>
<%@page import="com.wession.model.meritz.SSOAuth"%>
<%@page import="com.wession.model.meritz.*"%>
<%@ page import="org.slf4j.Logger"%>
<%@ page import="org.slf4j.LoggerFactory"%>
<%!// 다른 서버에서 메시지를 전송할 필요가 있을 경우 사용함
	private WessionTranser	transer;

	public void jspInit() {
		transer = new WessionTranser();
		transer.initialize(20); // trans용 Thread를 20개만 가지고 있음
	}

	public void jspDestroy() {
		transer.stop();
	}%>
<%
	WessionSessionManager s_mgr = WessionSessionManager.getInstance();
	Environment env = Environment.getInstance();
	Logger policyLog = LoggerFactory.getLogger("policy");
	Logger processLog = LoggerFactory.getLogger("process");

	WessionListener listener = null;
	ISessionMessage req_msg = null;
	String receiveAppCode = "";
	String command = "";
	String sessionid = "";

	long trans_timeout = 2000;

	ISessionMessage trans_msg = null;
	WessionServerSession sm = null;

	String instanceID = System.getProperty("WAIN");

	try {
		out.clear();
		pageContext.pushBody();

		listener = new WessionListener(request, response);
		req_msg = listener.read();

		processLog.debug("[c_service.jsp] req_msg.getEventType() : " + req_msg.getEventType() + " : " + req_msg.getEventTypeString());
		processLog.debug("[c_service.jsp] req_msg.getMessageType() : " + req_msg.getMessageType() + " : " + req_msg.getSessionID());

		sessionid = (String) req_msg.getSession().getID();
		CustomerSession css = (CustomerSession) req_msg.getSession().getAttribute("CustomerSession");

		System.out.println("CustomerSession INFO : " + css);

		receiveAppCode = ((WessionAgentSession) req_msg.getSession()).getAppCode();
		if (receiveAppCode == null) {
			receiveAppCode = Const.STATUS_NO_APPCODE;
			processLog.error("NO APPCODE Session : {}", req_msg);
		}

		command = (String) ((WessionAgentSession) req_msg.getSession()).getAttribute(Const.CMD);
		if (command == null) {
			command = Const.STATUS_NO_CMD;
			processLog.error("NO COMMAND Session From {} : {}", receiveAppCode, req_msg);
		}

		Common.addServerMonitor("WSS_REQ_CNT");

	}
	catch (Exception e) {
		e.printStackTrace();
		receiveAppCode = Const.STATUS_NO_APPCODE;
		command = Const.STATUS_NO_CMD;
	}

	// 홈페이지나, 다이렉트몰에서 인증세션이 만들어졌음
	// 기존 사용자코드가 있다면 해당 업무의 WessionAgent 정보를 제공하고 COMMAND = GET_WESSION
	// 없다면 신규로 생성하고 WessionAgent를 생성하여 제공함 = SET_WESSION

	System.out.println("SERVER MANAGER SIZE : " + s_mgr.getSize() + " / COMMNAD : " + command);

	int s_mgr_getSize = s_mgr.getSize();


	if (command.equals("AGENT_SET_WESSION")) {
		// 기존 사용자를 확인함
		String customerID = (String) ((WessionAgentSession) req_msg.getSession()).getUser();
		Customer customer = (Customer) req_msg.getSession().getAttribute("USER"); // 이건 만들고 지워야 함
		CustomerSession css = (CustomerSession) req_msg.getSession().getAttribute("CSESSION");
		//System.out.println("customer.getName() : " + customer.getName());
		//User user = new User(customer.getID(), customer.getName());

		List<ISession> ServerSession = s_mgr.getBy("UserID", customerID);

		System.out.println(customerID + " : " + sessionid);

		if ((ServerSession != null) && (ServerSession.size() >0)) { // 기존 로그인 된 것이 있음
			System.out.println("기존에 로그인된 세션이 있음 : " + ServerSession.size() + " - " + ServerSession);
			ISession destorySession = null;
			for (ISession l_session : ServerSession) {
				WessionServerSession d_sm = (WessionServerSession) s_mgr.get(l_session.getID());
				Set<String> agentSet = d_sm.getAgentSessionKeySet();
				// 아무것도 없을 수 있음
				if (agentSet != null && agentSet.size() > 0) {
					HashMap<String, String> agentCode = new HashMap<String, String>();
					boolean hasAgent = false;
					for (String agentid : agentSet) {
						WessionAgentSession agent_session = (WessionAgentSession) d_sm.getAgentSession(agentid);
						String AppCode = agent_session.getAppCode();
						if (AppCode.equals(receiveAppCode)) { // 받은 세션과 동일한 APPCODE가 있는 경우
							req_msg.getSession().setAttribute("APPCODE", AppCode); // 있음 
							CustomerSession css2 = (CustomerSession) agent_session.getAttribute("CSESSION");
							req_msg.getSession().setAttribute("CSESSION", css2);
							hasAgent = true;
						}

						// 없다면 새로 기존 값에 추가해야 함
						if (!hasAgent) {
							d_sm.addAgentSession((WessionAgentSession) req_msg.getSession(), true);
						}

					}

				} else {
					req_msg.getSession().setAttribute("APPCODE", "NO"); // 있음 
				}
			}
			req_msg.getSession().setAttribute(Const.CMD, "AGENT_GET_WESSION"); // 있음 
			req_msg.getSession().setAttribute("CODE", "20"); // 있음 

		} else {
			System.out.println("NEW WESSION CREATE START");
			SSOAuth sm_auth = new SSOAuth(customerID);
			sm_auth.setAuthTime(System.currentTimeMillis());
			sm_auth.setType("N");
			if (css == null) {
				css = new CustomerSession();
				System.out.println("CustomerSession Not Received");
			} else {
				System.out.println("CustomerSession : " + css.getLoginIP());
			}
			sm_auth.setAuthIP(css.getLoginIP());
			sm = new WessionServerSession(sessionid, customerID);
			sm.setAttribute("SSOAuth", sm_auth);
			//ssm.setAttribute("User", user);
			sm.addAgentSession((WessionAgentSession) req_msg.getSession(), true);
	
			// 실제 생성함
			s_mgr.create(sm);
			Common.addServerMonitor(Const.MNO_Wession_Create_Count);
			req_msg.getSession().setAttribute("CODE", "00");

		}

		//req_msg.getSession().removeAttribute("USER");
		listener.write(req_msg);
		
		
		
	} else if (command.equals("AGENT_GET_WESSION")) {
		String customerID = (String) ((WessionAgentSession) req_msg.getSession()).getUser();
		WessionAgentSession return_session = null;
		
		if (customerID == null) {	// 이런경우는 자체 세션이 없어진 경우 
			req_msg.getSession().setAttribute("MESSAGE", "NO_SESSION");
			customerID = "";
		}
		
		List<ISession> ServerSession = s_mgr.getBy("UserID", customerID);

		System.out.println("AGENT_GET_WESSION [getUserID] " + customerID);

		if (ServerSession != null) { // 하나일 가능성이 매우 높지만, 
			System.out.println("ServerSession count " + ServerSession.size());
			ISession destorySession = null;
			for (ISession l_session : ServerSession) {
				WessionServerSession e_sm = (WessionServerSession) s_mgr.get(l_session.getID());
				Set<String> agentSet = e_sm.getAgentSessionKeySet();
				// 아무것도 없을 수 있음
				if (agentSet != null && agentSet.size() > 0) {
					for (String agentid : agentSet) {
						WessionAgentSession agent_session = (WessionAgentSession) e_sm.getAgentSession(agentid);
						String AppCode = agent_session.getAppCode();
						if (receiveAppCode.equals(AppCode)) { // AppCode가 같다면 해당 세션을 넘겨준다.
							return_session = agent_session;
							return_session.setAttribute("MESSAGE", "FIND_SESSION");
							return_session.setAttribute("COMMAND", "FIND_SESSION");
							System.out.println("We get a wession : " + agentid);
							req_msg.setSession(return_session);
						}
					}
				} else { // agent에 없다.
					req_msg.getSession().setAttribute("MESSAGE", "NO_SESSION");
				}
			}

		} else { // 서버에 없다.
			req_msg.getSession().setAttribute("MESSAGE", "NO_SESSION");

		}

		req_msg.getSession().setAttribute("CODE", "00");
		listener.write(req_msg);
		
	} else if (command.equals("AGENT_GET_LOGIN")) {
		String customerID = (String) ((WessionAgentSession) req_msg.getSession()).getAttribute("REQ_USERNO");
		WessionAgentSession return_session = null;
		
		if (customerID == null) {	// 이런경우는 자체 세션이 없어진 경우 
			req_msg.getSession().setAttribute("MESSAGE", "NO_SESSION");
			customerID = "";
		}
		
		List<ISession> ServerSession = s_mgr.getBy("UserID", customerID);

		System.out.println("AGENT_GET_LOGIN [getUserID] " + customerID);

		if (ServerSession != null) { // 하나일 가능성이 매우 높지만, 
			System.out.println("ServerSession count " + ServerSession.size());
			ISession destorySession = null;
			req_msg.getSession().setAttribute("COMMAND", "NO_LOGINUSER");
			req_msg.getSession().setAttribute("MESSAGE", "NO_LOGINUSER");
			
			for (ISession l_session : ServerSession) {
				WessionServerSession e_sm = (WessionServerSession) s_mgr.get(l_session.getID());
				Set<String> agentSet = e_sm.getAgentSessionKeySet();
				// 아무것도 없을 수 있음
				if (agentSet != null && agentSet.size() > 0) {
					for (String agentid : agentSet) {
						WessionAgentSession agent_session = (WessionAgentSession) e_sm.getAgentSession(agentid);
						req_msg.setSession(agent_session);
					}
					req_msg.getSession().setAttribute("MESSAGE", "FIND_LOGINUSER");
					req_msg.getSession().setAttribute("COMMAND", "FIND_LOGINUSER");
					
				}
			}

		} else { // 서버에 없다.
			req_msg.getSession().setAttribute("COMMAND", "NO_LOGINUSER");
			req_msg.getSession().setAttribute("MESSAGE", "NO_LOGINUSER");

		}

		req_msg.getSession().setAttribute("CODE", "00");
		listener.write(req_msg);

	} else if (command.equals(Const.CMD_REMOVE_AWESSION)) { // WAS Timeout을 통해 날아옴
		String userid = (String) ((WessionAgentSession) req_msg.getSession()).getUser();
		List<ISession> ServerSessionList = s_mgr.getBy(Const.GetBy_ID, userid);

		processLog.debug("============= REMOVE_AGENT_SESSION ========== \n\t" + userid);

		// 하나만 있어야 함 여러개가 있다면 그것도 문제임
		if (ServerSessionList == null) {
			req_msg.getSession().setAttribute(Const.CMD, Const.STATUS_AGENT_SESSION_NOT_FOUND);

		} else if (ServerSessionList.size() == 0) {
			req_msg.getSession().setAttribute(Const.CMD, Const.STATUS_AGENT_SESSION_NOT_FOUND);

		} else {

			boolean deleteWession = false;
			String serverSessionID = "";
			for (ISession yoursession : ServerSessionList) {
				WessionServerSession ism = (WessionServerSession) yoursession;
				ism.removeAgentSession(sessionid, true); // 요청이 들어온 세션만 삭제함

				// 만약 더 이상연결된 에이전트가 없다면, Server Session도 삭제해야 함
				if (ism.getAgentSize() == 0) {
					deleteWession = true;
					serverSessionID = ism.getID();
				}
			}

			if (deleteWession) {
				s_mgr.expire(serverSessionID);
				Common.addServerMonitor(Const.MNO_Wession_Expire_Count);
				processLog.info("[c_service.jsp] Remove Empty Server Session  : " + userid + "[" + serverSessionID + "]");

			}

		}

		req_msg.getSession().setAttribute(Const.CODE, Const.CODE_SUCCESS);
		listener.write(req_msg);
		return;

	} else if (command.equals("AGENT_REMOVE_WESSION")) {
		// 기존 사용자를 확인함
		String customerID = (String) ((WessionAgentSession) req_msg.getSession()).getUser();
		List<ISession> ServerSession = s_mgr.getBy("UserID", customerID);

		System.out.println(customerID + " : " + sessionid);

		if (ServerSession != null) { // 기존 로그인 된 것이 있음
			System.out.println("## AGENT_REMOVE_WESSION PROCESS [" + ServerSession.size() + "]");

			ISession destorySession = null;
			for (ISession l_session : ServerSession) {
				WessionServerSession d_sm = (WessionServerSession) s_mgr.get(l_session.getID());
				Set<String> agentSet = d_sm.getAgentSessionKeySet();
				// 아무것도 없을 수 있음
				if (agentSet != null && agentSet.size() > 0) {

					HashMap<String, String> agentCode = new HashMap<String, String>();

					for (String agentid : agentSet) {
						WessionAgentSession destory_session = (WessionAgentSession) d_sm.getAgentSession(agentid);
						String AppCode = destory_session.getAppCode();
						if (receiveAppCode.equals(AppCode)) {
							destory_session.setAttribute("COMMAND", "SERVER_SESSION_DESTROY");
							//  여기서 어플리케이션 코드가 하나라도 여러개의 서버로 발송을 할 수 있다.
							Vector<String> v = env.getAgentServiceURL(receiveAppCode);
							for (String url : v) {
								ISessionMessage destroy_msg = new WessionAgentMessage(3, destory_session);
								trans_msg = transer.trans(url, destroy_msg, trans_timeout);
								System.out.println(url + " ( " + destory_session.getID() + " ) " +trans_msg);
								processLog.info("[c_service.jsp] Destory Agent Sessions : receive message from " + AppCode + " | " + sessionid);
							}
							d_sm.removeAgentSession(destory_session.getID(), false);
						}
					}

					// 지우기전에 남은 에이전트 세션이 있는지 확인해야함
					// 없으면 그때 지워야 함
					
					if (d_sm.getAgentSize() == 0)  {
						s_mgr.expire(l_session.getID());
						Common.addServerMonitor(Const.MNO_Wession_Expire_Count);
					}
					//processLog.info("[ws_service.jsp] Remove Server Session with Agent : " + d_sm.getUser() + "[" + l_session.getID() + "]");

				} else {
					s_mgr.expire(l_session.getID());
					Common.addServerMonitor(Const.MNO_Wession_Expire_Count);
					//processLog.info("[ws_service.jsp] Remove Server Session alone : " + d_sm.getUser() + "[" + l_session.getID() + "]");
				}
			}

		}
	} else {
		req_msg.getSession().setAttribute("COMMAND", "ERR_NO_COMMAND");
		req_msg.getSession().setAttribute("CODE", "99");
		listener.write(req_msg);
	}
%>