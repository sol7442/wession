<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.naming.*"%>
<%@ page import="org.slf4j.Logger"%>
<%@ page import="org.slf4j.LoggerFactory"%>
<%
	InitialContext cxt = new InitialContext();
	javax.sql.DataSource ds = null;
	Connection conn = null;

	String JDBC_URL = getServletConfig().getServletContext().getInitParameter("JDBC_URL");

	ds = (javax.sql.DataSource) cxt.lookup(JDBC_URL);
	if (ds == null) {
		throw new Exception("Data source not found!");
	}

	StringBuilder sb = new StringBuilder();

	String search_item = request.getParameter("itemcode");
	String search_id = request.getParameter("userno");
	String search_name = request.getParameter("usernm");
	String search_deptname = request.getParameter("deptnm");
	String search_deptcode = request.getParameter("deptcode");

	// 들어오는 파라미터를 분석하여 처리함

	Map<String, String[]> parameters = request.getParameterMap();
	for (String parameter : parameters.keySet()) {
		System.out.println(parameter + ":" + request.getParameter(parameter));
	}

	String user_query = "select * from COM_USER where user_id like ? and user_name like ? and org_name like ?";
	String dept_query = "select * from COM_ORG where org_cd like ? and org_name like ?";

	search_item = search_item == null ? "" : search_item.trim();

	search_id = search_id == null ? "" : search_id.trim();
	search_name = search_name == null ? "" : search_name.trim();
	search_deptname = search_deptname == null ? "" : search_deptname.trim();
	search_deptcode = search_deptcode == null ? "" : search_deptcode.trim();

	StringBuilder sb_empno = new StringBuilder();

	if (search_item.equals("ID") || search_item.equals("MP")) {

		String searchText = search_id + search_name + search_deptname;
		if (searchText.length() < 2) {
			sb.append("{\"empnm\":\"").append("검색 조건을 2자 이상 입력하십시오.").append("\"");
			sb.append("},\n");
		} else {
			try {
				conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(user_query);

				pstmt.setString(1, search_id + "%");
				pstmt.setString(2, "%" + search_name + "%");
				pstmt.setString(3, "%" + search_deptname + "%");

				//System.out.println(pstmt);

				ResultSet result = pstmt.executeQuery();
				int i = 0;
				while (result.next()) {
					i++;
					sb.append("{").append("\"no\":\"").append(i).append("\"");
					sb.append(",\"empno\":\"").append(result.getString("user_id")).append("\"");
					sb.append(",\"empnm\":\"").append(result.getString("user_name")).append("\"");
					sb.append(",\"deptcode\":\"").append(result.getString("org_cd")).append("\"");
					sb.append(",\"deptnm\":\"").append(result.getString("org_name")).append("\"");
					sb.append(",\"pri-deptcode\":\"").append("-").append("\"");
					sb.append(",\"pri-deptnm\":\"").append("-").append("\"");
					sb.append(",\"search-data\":\"").append("-").append("\"");
					sb.append("},\n");

				}
				pstmt.close();

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
		}
	} else if (search_item.equals("DV")) {
		try {
			conn = ds.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(dept_query);

			pstmt.setString(1, "%" + search_deptcode + "%");
			pstmt.setString(2, "%" + search_deptname + "%");

			//System.out.println(pstmt);

			ResultSet result = pstmt.executeQuery();
			int i = 0;
			while (result.next()) {
				i++;
				sb.append("{").append("\"no\":\"").append(i).append("\"");
				sb.append(",\"empno\":\"").append("").append("\"");
				sb.append(",\"empnm\":\"").append("").append("\"");
				sb.append(",\"deptcode\":\"").append(result.getString("org_cd")).append("\"");
				sb.append(",\"deptnm\":\"").append(result.getString("org_name")).append("\"");
				sb.append(",\"pri-deptcode\":\"").append(result.getString("up_org_cd")).append("\"");
				sb.append(",\"pri-deptnm\":\"").append(result.getString("up_org_name")).append("\"");
				sb.append(",\"search-data\":\"").append("").append("\"");
				sb.append("},\n");
			}
			pstmt.close();

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
	} else {
		sb.append("{}");
	}

	if (sb.length() > 2)
		sb.delete(sb.length() - 2, sb.length());
%>
[
<%=sb.toString()%>
]
