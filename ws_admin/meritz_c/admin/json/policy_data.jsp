<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.naming.*"%>
<%@ page import="org.slf4j.Logger"%>
<%@ page import="org.slf4j.LoggerFactory"%>
<%
	if (session.getAttribute("wessionadminid") == null) {
		session.invalidate();
		System.out.println("Session Invalid");
		out.println("{\"code\":\"90\", \"message\":\"세션이 만료되었습니다.\"}");

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
	String policycode = request.getParameter("policycode");
	String allowed = request.getParameter("allowed");
	String itemcode = request.getParameter("itemcode");
	String data = request.getParameter("data");
	String accessip = request.getParameter("accessip");
	String policyserial = request.getParameter("policySerial");
	String comment = request.getParameter("comment") + "";

	String message = "";

	if ("AL".equals(policycode) && "MP".equals(itemcode)) {
		data = data + ";" + accessip;
	}
	// 현재의 정책상태는 서버에서 다시 확인함
	try {
		// 기존에 있는 데이터 인지 확인함
		if ("add".equals(method)) {
			String sql = "select count(*) from T_WESSION_MATTERS where 1=1 ";
			sql = sql + " and policycode = '" + policycode + "' and allowed='" + allowed + "' and itemcode='" + itemcode + "' ";
			sql = sql + " and data = '" + data + "'";

			Statement select = conn.createStatement();
			ResultSet result = select.executeQuery(sql);
			while (result.next()) {
				int count = result.getInt(1);
				if (count == 0) { // 이러면 데이터가 없는 것 adminUserEmpNo
					
					Statement select_index = conn.createStatement();
					ResultSet result_index = select_index.executeQuery("select max(idx) from T_WESSION_MATTERS");
					int newIndex = 1;
					while (result_index.next()) {
						newIndex = result_index.getInt(1) + 1;
					}
					result_index.close();
					select_index.close();
					
					String insertSQL = "insert into T_WESSION_MATTERS(policyCode, allowed, itemCode, metaCode, data, remark, lastModifyUser, idx) " + "values(?, ?, ?, ?, ?, ?, ?, ?)";
					PreparedStatement pstmt = conn.prepareStatement(insertSQL);
					pstmt.setString(1, policycode);
					pstmt.setString(2, allowed);
					pstmt.setString(3, itemcode);
					pstmt.setString(4, "NR");
					pstmt.setString(5, data);
					pstmt.setString(6, comment);
					pstmt.setString(7, adminUserEmpNo);
					pstmt.setInt(8, newIndex);

					System.out.println(pstmt.toString());

					if (pstmt.execute()) {
						message = "정책을 추가 중 오류가 발생하였습니다.";
					} else {
						message = "정책을 추가하였습니다.";
						policyLog.info("POLICY INSERT : policycode : {} / allowed : {} / itemcode : {} / data : {} / comment : {} / registered : {}"
								       , policycode, allowed, itemcode, data, comment, adminUserEmpNo);
					}

					pstmt.close();
				} else {
					message = "기존에 있는 정책입니다.";
				}
			}
			select.close();
		} else if ("del".equals(method)) { // 삭제합니다.
			
			String deleteSQL = "delete from T_WESSION_MATTERS where idx=?";
			String selectSQL = "select * from T_WESSION_MATTERS where idx=?";
			
			PreparedStatement pstmt = conn.prepareStatement(deleteSQL);
			PreparedStatement pstmt1 = conn.prepareStatement(selectSQL);

			String[] delete_policy = policyserial.split(";");
			
			for (int i = 0; i < delete_policy.length; i++) {
				// 삭제하기 전에 데이터를 뽑아서 로그로 남겨둔다.
				pstmt1.setInt(1, Integer.parseInt(delete_policy[i]));
				ResultSet rs = pstmt1.executeQuery();
				while (rs.next()) {
					policyLog.info("POLICY DELETE : policycode : {} / allowed : {} / itemcode : {} / data : {} / comment : {} / registered : {}"
						       , rs.getString("policyCode")
						       , rs.getString("allowed")
						       , rs.getString("itemCode")
						       , rs.getString("data")
						       , rs.getString("remark")
						       , rs.getString("lastModifyUser"));
				}
				rs.close();	
				
				pstmt.setInt(1, Integer.parseInt(delete_policy[i]));
				pstmt.execute();
				
			}
			
			message="삭제하였습니다.";
			pstmt.close();
			
		} else if ("changedefault".equals(method)) { // 기본 정책을 변경합니다.
			String updateSQL = "update T_WESSION_POLICIES set allowed=? where policyCode=?";
			
			PreparedStatement pstmt = conn.prepareStatement(updateSQL);
			pstmt.setString(1, allowed);
			pstmt.setString(2, policycode);
					
			if (pstmt.execute()) {
				message = "기본 정책 변경 중 오류가 발생하였습니다.";
			} else {
				message = "기본 정책을 변경하였습니다.";
				policyLog.info("DEFALUT POLICY CHANGED :  policycode : {} / allowed : {}", policycode, allowed);
			}
			
			
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
