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
	String policycode = request.getParameter("policycode");
	String allowed = request.getParameter("allowed");
	String matter_allowed = "";
	String itemcode = request.getParameter("itemcode");

	// 현재의 정책상태는 서버에서 다시 확인함

	try {
		String sql = "select * from T_WESSION_MATTERS where 1=1 ";
		sql = sql + " and policycode = '" + policycode + "' and allowed='" + allowed + "' and itemcode='" + itemcode + "' order by lastModifyTime desc, data";
		//System.out.println(sql);

		out.println("[");
		Statement select = conn.createStatement();
		ResultSet result = select.executeQuery(sql);
		StringBuffer sb = new StringBuffer();
		int i = 0;
		while (result.next()) { // process results one row at a time
			String data = result.getString("data");
			String remark = result.getString("remark");
			String username = result.getString("lastModifyUser");
			String accessip = "";
			if ("AL".equals(policycode)) { // 인증적용 정책에는 ID/IP가 들어간다.
				String[] d = data.split(";");
				if (d.length > 1) {
					data = d[0];
					accessip = d[1];
				}
			}
			sb.append("  {\"serial\":\"").append(result.getString("idx"))
				.append("\",\"no\":\"").append(++i)
				.append("\",\"data\":\"").append(data.replaceAll("\"", "\\\\\""))
				.append("\",\"accessip\":\"").append(accessip)
				.append("\",\"remark\":\"").append(remark == null ? "-" : remark.replaceAll("\"", "\\\\\""))
				.append("\",\"regusername\":\"").append(username == null ? "-" : username)
				.append("\",\"regdate\":\"").append(result.getString("lastModifyTime"))
				.append("\",\"load\":\"").append(result.getString("memload"))
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