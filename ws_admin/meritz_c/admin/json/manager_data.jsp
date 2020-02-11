<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.naming.*"%>
<%@ page import="com.wession.Common"%>
<%@ page import="org.slf4j.Logger"%>
<%@ page import="org.slf4j.LoggerFactory"%>
<%@ page import="com.wow.wession.security.*"%>
<%!
	// 이 부분을 가지고 오는 것으로
	private static final String seedkey = "123jh8hkjsf#dk9h2jd2hg";
	public static String getEncPassword(String plainText) {
		try {
			return encryptSEED.getSeedEncrypt(plainText, encryptSEED.getSeedRoundKey(seedkey));
		} catch (Exception e) {
			return null;
		}
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

	Logger policyLog = LoggerFactory.getLogger("policy");

	InitialContext cxt = new InitialContext();
	javax.sql.DataSource ds = null;
	Connection conn = null;
	
	String JDBC_URL = getServletConfig().getServletContext().getInitParameter("JDBC_URL");

	ds = (javax.sql.DataSource) cxt.lookup(JDBC_URL);
	if (ds == null) {
		throw new Exception("Data source not found!");
	}
	conn = ds.getConnection();
	String method = request.getParameter("method");
	String userid = request.getParameter("userid"); // 삭제
	String empno = request.getParameter("empno");
	String empname = request.getParameter("empname");
	String encpassword = request.getParameter("encpassword");
	String comment = request.getParameter("comment") + "";
	
	String userauth = request.getParameter("userauth") + "";

	String message = "";
	
	String encType = "SEED";
	String encpassword2 = getEncPassword(encpassword);
	encType = userauth + "," + encType;
	
	System.out.println("encType : " + encType);
	System.out.println("password : " + encpassword);
	System.out.println("enc with SEED : " + encpassword2);
	
	

	try {
		// 관리자 추가
		if ("addUser".equals(method)) {
			String sql = "select count(*) from T_WESSION_USERS where 1=1 ";
			sql = sql + " and empNo = '" + empno + "'";

			Statement select = conn.createStatement();
			ResultSet result = select.executeQuery(sql);
			while (result.next()) {
				int count = result.getInt(1);
				if (count == 0) { // 이러면 데이터가 없는 것 adminUserEmpNo
					
					String insertSQL = "insert into T_WESSION_USERS(empNo, empName, encType, encPassword, encSalt, lastModifyUser, lastModifyTime) " + "values(?, ?, ?, ?, ?, ?, ?)";
					PreparedStatement pstmt = conn.prepareStatement(insertSQL);
					pstmt.setString(1, empno);
					pstmt.setString(2, empname);
					pstmt.setString(3, encType);
					pstmt.setString(4, encpassword2);
					pstmt.setString(5, "");
					pstmt.setString(6, adminUserEmpNo);
					pstmt.setString(7, Common.getDateFromLAT(System.currentTimeMillis()));

					System.out.println(pstmt.toString());

					if (pstmt.execute()) {
						message = "관리자 추가 중 오류가 발생하였습니다.";
					} else {
						message = "관리자를 추가하였습니다.";
						policyLog.info("ADMIN INSERT : empno : {} / empname : {}" , empno, empname);
					}

					pstmt.close();
				} else {
					message = "기존에 있는 관리자 입니다.";
				}
			}
			select.close();
		} else if ("delUser".equals(method)) { // 삭제합니다.
			
			String deleteSQL = "delete from T_WESSION_USERS where empno=?";
			
			PreparedStatement pstmt = conn.prepareStatement(deleteSQL);

			String[] deleteUser = userid.split(";");
			
			for (int i = 0; i < deleteUser.length; i++) {
				// 삭제하기 전에 데이터를 뽑아서 로그로 남겨둔다.
				pstmt.setString(1, deleteUser[i]);
				pstmt.execute();
				policyLog.info("ADMIN DELETE : empno : {}" , deleteUser[i]);
				
			}
			
			message="관리자를 삭제하였습니다.";
			pstmt.close();
			
		} 
	}
	catch (Exception e) {
		e.printStackTrace();
		message = "처리 중 오류가 발생하였습니다.";
		policyLog.error("WORK USER {} - {}: ", adminUserEmpNo, e.getMessage());
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
