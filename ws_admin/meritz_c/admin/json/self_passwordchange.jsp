<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.naming.*"%>
<%@ page import="org.slf4j.Logger"%>
<%@ page import="org.slf4j.LoggerFactory"%>
<%@ page import="com.wow.wession.security.*"%>
<%!
	// 이 부분을 가지고 오는 것으로
	private static final String seedkey = "123jh8hkjsf#dk9h2jd2hg";
	public static String getEncPassword(String plainText) throws Exception {
		return encryptSEED.getSeedEncrypt(plainText, encryptSEED.getSeedRoundKey(seedkey));

	}
	
	public static String getDecPassword(String encText) {
		try {
			return encryptSEED.getSeedDecrypt(encText, encryptSEED.getSeedRoundKey(seedkey));
		} catch (Exception e) {
			return null;
		}
	}

%>
<%
	if (session.getAttribute("wessionadminid") == null) {
		session.invalidate();
		System.out.println("Session Invalid");
		return;
	}

	String adminUserEmpNo = (String) session.getAttribute("wessionadminid");

	Logger processLog = LoggerFactory.getLogger("process");

	InitialContext cxt = new InitialContext();
	javax.sql.DataSource ds = null;
	Connection conn = null;

	String JDBC_URL = getServletConfig().getServletContext().getInitParameter("JDBC_URL");

	ds = (javax.sql.DataSource) cxt.lookup(JDBC_URL);
	if (ds == null) {
		throw new Exception("Data source not found!");
	}
	conn = ds.getConnection();
	String method = request.getParameter("cur_passwd");
	String userid = (String) session.getAttribute("wessionadminid");
	String cur_passwd = request.getParameter("cur_passwd");
	String new_passwd = request.getParameter("new_passwd");

	String message = "";

	try {
		// 본인 비밀번호 변경
		String selectSQL = "select * from T_WESSION_USERS where empno = ?";
		PreparedStatement pstmt = conn.prepareStatement(selectSQL);
		pstmt.setString(1, userid);
		System.out.println(pstmt);
		ResultSet result = pstmt.executeQuery();
		boolean correctUser = false;
		
		String auth = "";
		String encType = "";
		
		while (result.next()) {
			encType = result.getString("encType"); // PLAIN or V,SEED
			
			if (encType.equals("PLAIN")) { // 기존 입력방
				String dbpwd = result.getString("encpassword");
				if (cur_passwd.equals(dbpwd)) { // 정상사용자
					correctUser=true;
				} else {
					correctUser=false;
				}
			} else { // 신규 형식, V,SEED
				String [] authenc = encType.split(",");
				auth = authenc[0];
				encType = authenc[1];
				
				if ("SEED".equals(encType)) {	//기본 SEED 형식
					String inputPwd = getEncPassword(cur_passwd);
					String dbpwd = result.getString("encpassword");
					if (inputPwd.equals(dbpwd)) { // 정상사용자
						correctUser=true;
						new_passwd = getEncPassword(new_passwd);
					} else {
						correctUser=false;
					}
				}
			}

		}
		
		if (correctUser) {
			String updateSQL = "update T_WESSION_USERS set encpassword = ? where empno = ?";
			pstmt = conn.prepareStatement(updateSQL);
			pstmt.setString(1, new_passwd);
			pstmt.setString(2, userid);
			System.out.println(pstmt);
			pstmt.execute();
			message = "비밀번호를 변경하였습니다.";
			
			processLog.error("PASSWORD CHANGE USER {} ", adminUserEmpNo);
			
		} else {
			message = "현재 비밀번호가 맞지 않습니다.";
		}

		pstmt.close();

	}
	catch (Exception e) {
		e.printStackTrace();
		message = "처리 중 오류가 발생하였습니다.";
		processLog.error("PASSWORD CHANGE USER {} - {}: ", adminUserEmpNo, e.getMessage());
	}
	finally {
		if (conn != null) {
			try {
				conn.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
%>
{"code":"00", "message":"<%=message%>"}
