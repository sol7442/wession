<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.wession.*"%>
<%@page import="com.wession.policy.meritz.*"%>
<%@page import="com.wow.wession.agent.*"%>
<%@page import="com.wow.wession.session.ISessionMessage"%>
<%@page import="com.wow.wession.session.WessionSessionManager"%>
<%@ page import="org.slf4j.Logger"%>
<%@ page import="org.slf4j.LoggerFactory"%>
<%!// 다른 서버에서 메시지를 전송할 필요가 있을 경우 
	private WessionTranser	transer;

	public void jspInit() {
		//System.out.println("jspInit count");
		transer = new WessionTranser();
		transer.initialize(20); // trans용 Thread를 10개만 가지고 있음
	}

	public void jspDestroy() {
		transer.stop();
	}%>
<%
	WessionSessionManager s_mgr = WessionSessionManager.getInstance();
	Environment env = Environment.getInstance();

	String instanceID = System.getProperty("WAIN");
	String appCode = Const.STATUS_NO_APPCODE;

	long trans_timeout = 2000;

	if (session.getAttribute("wessionadminid") == null) {
		session.invalidate();
		System.out.println("Session Invalid");
		out.println("{\"code\":\"90\", \"message\":\"세션이 만료되었습니다.\"}");
		return;
	}

	Logger policyLog = LoggerFactory.getLogger("policy");
	String adminUserEmpNo = (String) session.getAttribute("wessionadminid");

	// 들어오는 파라미터를 분석하여 처리함
	/*
	Map<String, String[]> parameters = request.getParameterMap();
	for (String parameter : parameters.keySet()) {
		//System.out.println(parameter + ":" + request.getParameter(parameter));
	}
	 */

	String policycode = request.getParameter("policycode");

	try {
		String instance = s_mgr.getProperty("instance.1");
		String url = s_mgr.getProperty(Const.SERVICE_URL + "." + instance);
		if (url == null) {
			policyLog.error("   Empty seriveURL." + instance);
		} else {
			url = url.replaceAll("/m_service.jsp", "/wession/policy_adopt");
			WessionAgentSession iPolicy = new WessionAgentSession(session.getId(), "server", "SSO_POLICY");
			ISessionMessage policy_msg = new WessionAgentMessage(3, iPolicy);

			policy_msg.getSession().setAttribute(Const.CMD, "POLICY_RELOAD");
			policy_msg.getSession().setAttribute("POLICY_CODE", policycode);

			policy_msg = transer.trans(url, policy_msg, trans_timeout);
			String POLICYSET = (String) policy_msg.getSession().getAttribute("POLICYSET");
			String code = (String) policy_msg.getSession().getAttribute(Const.CODE);
			
			policyLog.info(" POLICY RELOADED WESSION_SERVER_1 [{}] : {}", POLICYSET, code);
		}
	}
	catch (Exception e) {
		//e.printStackTrace();
		System.out.println(" POLICY RELOADED FAIL WESSION_SERVER_1 " + e.getMessage());
		policyLog.error(" POLICY RELOADED FAIL WESSION_SERVER_1 : {}", "99");
	}

	try {
		String instance = s_mgr.getProperty("instance.2");
		String url = s_mgr.getProperty(Const.SERVICE_URL + "." + instance);
		if (url == null) {
			policyLog.error("   Empty seriveURL." + instance);
		} else {
			url = url.replaceAll("/m_service.jsp", "/wession/policy_adopt");
			WessionAgentSession iPolicy = new WessionAgentSession(session.getId(), "server", "SSO_POLICY");
			ISessionMessage policy_msg = new WessionAgentMessage(3, iPolicy);

			policy_msg.getSession().setAttribute(Const.CMD, "POLICY_RELOAD");
			policy_msg.getSession().setAttribute("POLICY_CODE", policycode);

			policy_msg = transer.trans(url, policy_msg, trans_timeout);
			String POLICYSET = (String) policy_msg.getSession().getAttribute("POLICYSET");
			String code = (String) policy_msg.getSession().getAttribute(Const.CODE);
			
			policyLog.info(" POLICY RELOADED WESSION_SERVER_2 [{}] : {}", POLICYSET, code);
		}
	}
	catch (Exception e) {
		//e.printStackTrace();
		System.out.println(" POLICY RELOADED FAIL WESSION_SERVER_2 " + e.getMessage());
		policyLog.error(" POLICY RELOADED FAIL WESSION_SERVER_2 : {}", "99");
	}
	
	int count = 0;

	//Policy 재로딩
	if ("AA".equals(policycode)) { // AA 인증적용정책 목록 세팅
		AA policyAccessAdopt = AA.getInstance();
	/*
		String sql = "select * from T_WESSION_POLICIES where policyCode='AA'";
		Vector<HashMap<String, Object>> rows = DBUtil.getResultSet(sql);
		String policyAccessAdopt_defalutPolicy = "";
		for (HashMap<String, Object> data : rows) {
			policyAccessAdopt_defalutPolicy = (String) data.get("allowed");
		}
		policyAccessAdopt.setDefaultPolicy(policyAccessAdopt_defalutPolicy.equals("AW") ? "allow" : "deny");

		sql = "select * from T_WESSION_MATTERS where policyCode='AA' " + " and allowed='" + policyAccessAdopt_defalutPolicy + "'" + " and metaCode='NR'";
		Vector<HashMap<String, Object>> rowsAA = DBUtil.getResultSet(sql);
		Vector<String> vUserid = new Vector<String>();
		Vector<String> vDivCode = new Vector<String>();
		Vector<String> vAccessIP = new Vector<String>();
		Vector<String> vEamCode = new Vector<String>();
		for (HashMap<String, Object> data : rowsAA) {
			if ("ID".equals((String) data.get("itemcode"))) {
				vUserid.add((String) data.get("data"));
			} else if ("DV".equals((String) data.get("itemcode"))) {
				vDivCode.add((String) data.get("data"));
			} else if ("IP".equals((String) data.get("itemcode"))) {
				vAccessIP.add((String) data.get("data"));
			} else if ("EM".equals((String) data.get("itemcode"))) {
				vEamCode.add((String) data.get("data"));
			}
		}
		policyAccessAdopt.setPolicy("userid", vUserid);
		policyAccessAdopt.setPolicy("divCode", vDivCode);
		policyAccessAdopt.setPolicy("accessIP", vAccessIP);
		policyAccessAdopt.setPolicy("eamCode", vEamCode);
*/
		count = policyAccessAdopt.getSize("userid") + policyAccessAdopt.getSize("divCode") + policyAccessAdopt.getSize("accessIP") + policyAccessAdopt.getSize("eamCode");
	}

	if ("AL".equals(policycode)) { // AL 인증허용정책 목록 세팅
		AL policyAccessAllow = AL.getInstance();
	/*
		String sql = "select * from T_WESSION_POLICIES where policyCode='AL'";
		Vector<HashMap<String, Object>> rows = DBUtil.getResultSet(sql);
		String policyAccessAllow_defalutPolicy = "";
		for (HashMap<String, Object> data : rows) {
			policyAccessAllow_defalutPolicy = (String) data.get("allowed");
		}
		policyAccessAllow.setDefaultPolicy(policyAccessAllow_defalutPolicy.equals("AW") ? "allow" : "deny");

		sql = "select * from T_WESSION_MATTERS where policyCode='AL' " + " and allowed='" + policyAccessAllow_defalutPolicy + "'" + " and metaCode='NR'";
		rows = DBUtil.getResultSet(sql);
		System.out.println("[policy_set]" + sql);
		Vector<String> mapping = new Vector<String>();

		for (HashMap<String, Object> data : rows) {
			if ("ID".equals((String) data.get("itemcode"))) {

				// mapping 부분은 나누어서 처리해야 함
				String mappingString = (String) data.get("data");
				String[] ips = mappingString.split(";");
				if (ips.length > 0) {
					String id = ips[0];
					String ip = ips[1];
					// ip에 대한 * 검사 처리 후 *가 있다면 1~255까지 집어넣는다.
					if (ip.contains("*")) {
						for (int i = 1; i <= 255; i++) {
							mapping.add(id + ";" + ip.subSequence(0, ip.length() - 1) + i);
						}
					} else {
						mapping.add((String) data.get("data"));
					}
				}
			}
		}

		policyAccessAllow.setPolicy("mapping", mapping);
		*/
		
		count = policyAccessAllow.getSize("mapping");
	}

	//Policy 재로딩
	if ("DA".equals(policycode)) { // DA 중복로그인적용정책 목록 세팅

		DA policyDuploginAdopt = DA.getInstance();
	/*
		String sql = "select * from T_WESSION_POLICIES where policyCode='DA'";
		Vector<HashMap<String, Object>> rows = DBUtil.getResultSet(sql);
		String policyDuploginAdopt_defalutPolicy = "";
		for (HashMap<String, Object> data : rows) {
			policyDuploginAdopt_defalutPolicy = (String) data.get("allowed");
		}
		policyDuploginAdopt.setDefaultPolicy(policyDuploginAdopt_defalutPolicy.equals("AW") ? "allow" : "deny");
		//System.out.println(policyDuploginAdopt.getDefaultPolicy() + "/" + policyDuploginAdopt_defalutPolicy);

		sql = "select * from T_WESSION_MATTERS where policyCode='DA' " + " and allowed='" + policyDuploginAdopt_defalutPolicy + "'" + " and metaCode='NR'";
		//System.out.println(sql);
		Vector<HashMap<String, Object>> rowsAA = DBUtil.getResultSet(sql);
		Vector<String> vUserid = new Vector<String>();
		Vector<String> vDivCode = new Vector<String>();
		Vector<String> vAccessIP = new Vector<String>();
		Vector<String> vEamCode = new Vector<String>();
		for (HashMap<String, Object> data : rowsAA) {
			if ("ID".equals((String) data.get("itemcode"))) {
				vUserid.add((String) data.get("data"));
			} else if ("DV".equals((String) data.get("itemcode"))) {
				vDivCode.add((String) data.get("data"));
			} else if ("IP".equals((String) data.get("itemcode"))) {
				vAccessIP.add((String) data.get("data"));
			} else if ("EM".equals((String) data.get("itemcode"))) {
				vEamCode.add((String) data.get("data"));
			}
		}
		policyDuploginAdopt.setPolicy("userid", vUserid);
		policyDuploginAdopt.setPolicy("divCode", vDivCode);
		policyDuploginAdopt.setPolicy("accessIP", vAccessIP);
		policyDuploginAdopt.setPolicy("eamCode", vEamCode);
*/

		count = policyDuploginAdopt.getSize("userid") + policyDuploginAdopt.getSize("divCode") + policyDuploginAdopt.getSize("accessIP") + policyDuploginAdopt.getSize("eamCode");
	}

	//Policy 재로딩
	if ("DL".equals(policycode)) { // DA 중복로그인적용정책 목록 세팅

		DL policyDuploginAllow = DL.getInstance();
	
	/*
		String sql = "select * from T_WESSION_POLICIES where policyCode='DL'";
		Vector<HashMap<String, Object>> rows = DBUtil.getResultSet(sql);
		String policyDuploginAllow_defalutPolicy = "";
		for (HashMap<String, Object> data : rows) {
			policyDuploginAllow_defalutPolicy = (String) data.get("allowed");
		}
		policyDuploginAllow.setDefaultPolicy(policyDuploginAllow_defalutPolicy.equals("AW") ? "allow" : "deny");

		sql = "select * from T_WESSION_MATTERS where policyCode='DL' " + " and allowed='" + policyDuploginAllow_defalutPolicy + "'" + " and metaCode='NR'";
		Vector<HashMap<String, Object>> rowsAA = DBUtil.getResultSet(sql);
		Vector<String> vUserid = new Vector<String>();
		Vector<String> vDivCode = new Vector<String>();
		Vector<String> vAccessIP = new Vector<String>();
		Vector<String> vEamCode = new Vector<String>();
		for (HashMap<String, Object> data : rowsAA) {
			if ("ID".equals((String) data.get("itemcode"))) {
				vUserid.add((String) data.get("data"));
			} else if ("DV".equals((String) data.get("itemcode"))) {
				vDivCode.add((String) data.get("data"));
			} else if ("IP".equals((String) data.get("itemcode"))) {
				vAccessIP.add((String) data.get("data"));
			} else if ("EM".equals((String) data.get("itemcode"))) {
				vEamCode.add((String) data.get("data"));
			}
		}
		policyDuploginAllow.setPolicy("userid", vUserid);
		policyDuploginAllow.setPolicy("divCode", vDivCode);
		policyDuploginAllow.setPolicy("accessIP", vAccessIP);
		policyDuploginAllow.setPolicy("eamCode", vEamCode);

		*/
		count = policyDuploginAllow.getSize("userid") + policyDuploginAllow.getSize("divCode") + policyDuploginAllow.getSize("accessIP") + policyDuploginAllow.getSize("eamCode");
	}

	policyLog.info("POLICY RELOAD - {} / count : {}", policycode, count);
%>
{"code":"00", "message":"<%=count%>건의 정책을 로딩하였습니다."}
