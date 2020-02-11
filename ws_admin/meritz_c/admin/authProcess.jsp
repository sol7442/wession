<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.naming.*"%>
<%@ page import="java.sql.PreparedStatement"%>
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
	String id = request.getParameter("user_id");
	String pwd = request.getParameter("user_passwd");

	String testDBdata = "";
	StringBuffer sbDB = new StringBuffer();
	StringBuffer sb = new StringBuffer();
	HashMap<String, String> hmData = new HashMap<String, String>();

	InitialContext cxt = new InitialContext();
	javax.sql.DataSource ds = null;
	Connection conn = null;
	PreparedStatement preparedStatement = null;

	ds = (javax.sql.DataSource) cxt.lookup(getServletConfig().getServletContext().getInitParameter("JDBC_URL"));
	if (ds == null) {
		throw new Exception("Data source not found!");
	}
	conn = ds.getConnection();

	String encType = "";
	String encPassword = "";
	String encSalt = "";
	String empName = "";
	
	//System.out.println("userid = " + id);
	//System.out.println("password = " + pwd);

	try {
		String sql = "select * from T_WESSION_USERS where empNo=?";

		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setString(1, id);
		ResultSet result = preparedStatement.executeQuery();

		while (result.next()) { // process results one row at a time
			encType = result.getString("encType");
			encPassword = result.getString("encPassword");
			encSalt = result.getString("encSalt");
			empName = result.getString("empName");
		}
		preparedStatement.close();

	}
	catch (Exception e) {

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

	if ("".equals(encPassword)) {
		//System.out.println("No User");
		response.sendRedirect("./index.html");
		return;
	}

	// 암호화 처리를 수행함
	if ("PLAIN".equals(encType)) {
		if (pwd.equals(encPassword)) {
			System.out.println("Authentication Success.");
			session.setAttribute("wessionadminid", id);
			session.setAttribute("wessionadminname", empName);
			
			response.sendRedirect("./src/dashboard/dashboard.jsp");
			return;
		} else {
			System.out.println("Wrong Password");
			response.sendRedirect("./index.html");
			return;
		}
	} else {
		// 권한이 들어간 모습.
		String [] types = encType.split(",");
		String auth = types[0];
		encType = types[1];
		if (encType.equals("PLAIN")) {
			
		} else if (encType.equals("SEED")) {
			pwd = encryptSEED.getSeedEncrypt(pwd, encryptSEED.getSeedRoundKey(seedkey));
			
		}
		
		String inputPwd = pwd;
		String registPwd = encPassword;
		
		if (pwd.equals(encPassword)) {
			System.out.println("Authentication Success.");
			session.setAttribute("wessionadminid", id);
			session.setAttribute("wessionadminname", empName);
			
			if ("V".equals(auth)) {
				session.setAttribute("wessionauth", "view");
				response.sendRedirect("./src/dashboard/dashboard_view.jsp");
				return;
			} else {
				session.setAttribute("wessionauth", "admin");
				response.sendRedirect("./src/dashboard/dashboard.jsp");
				return;
			}
			
		} else {
			System.out.println("Wrong Password");
			response.sendRedirect("./index.html");
			return;
		}
	
	}
%>