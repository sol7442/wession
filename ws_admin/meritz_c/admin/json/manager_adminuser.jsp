<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.naming.*"%>
<%
	InitialContext cxt = new InitialContext();
	javax.sql.DataSource ds = null;
	Connection conn = null;
	String JDBC_URL = getServletConfig().getServletContext().getInitParameter("JDBC_URL");

	ds = (javax.sql.DataSource) cxt.lookup(JDBC_URL);
	if (ds == null) {
		throw new Exception("Data source not found!");
	}
	conn = ds.getConnection();

	try {
		String sql = "select * from T_WESSION_USERS ";

		out.println("[");
		Statement select = conn.createStatement();
		ResultSet result = select.executeQuery(sql);
		StringBuffer sb = new StringBuffer();
		int i = 0;
		while (result.next()) { // process results one row at a time
			sb.append("  {\"userid\":\"").append(result.getString("empNo"))
			  .append("\",\"no\":\"").append(++i)
			  .append("\",\"name\":\"").append((result.getString("empName")).replaceAll("\"", "\\\\\\\""))
			  .append("\",\"registdate\":\"").append(result.getString("lastModifyTime"))
			  .append("\",\"modifyempno\":\"").append(result.getString("lastModifyUser"))
			  .append("\"},\n");
		}

		if (sb.length() < 2)
			sb.append("{}");
		sb.delete(sb.length() - 2, sb.length());
		out.println(sb.toString());
		out.println("]");
		select.close();
	}
	catch (Exception e) {
		e.printStackTrace();

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