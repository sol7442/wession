<%@ page language="java" pageEncoding="UTF-8" 
%><%@page import="java.io.*"
%><%@page import="java.net.URL"
%><%@page import="java.net.URLConnection"
%><%@page import="org.apache.commons.io.IOUtils"
%><%@page import="java.sql.*"
%><%@page import="javax.naming.*"
%><%@page import="org.json.*"
%><%@page import="com.wession.Common"
%><%

	String type = request.getParameter("type");
	String baseURL = request.getRequestURL().toString();
	response.setHeader("Content-Disposition", "attachment; filename="+type+".csv"); 
	//response.setHeader("Content-Type", "text/csv; charset=UTF-8"); 
	response.setHeader("Content-Type", "text/csv; charset=MS949"); 
	StringBuilder sb = new StringBuilder();
	//byte[] utf8Bom = { (byte)0xEF, (byte)0xBB, (byte)0xBF};
	//String utf8String = new String(utf8Bom);
	//sb.append(utf8String);
	sb.append("WESSION - 메리츠화재");
	
	if (type.equals("agent")) {
		String body = "";
		String uri = "agent_wsstbl.jsp";
		
		try {
			uri = baseURL.replace("export.jsp", uri);
			sb.append(" - export time : ").append(Common.getDateFromLAT(System.currentTimeMillis())).append("\n");
			sb.append("AGENT LIST").append("\n");
			
			URL url = new URL(uri);
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			body = body + IOUtils.toString(in, encoding);
			
		    //JSONObject jo = new JSONObject(body);
		    //JSONArray ja = jo.getJSONArray("");
		    JSONArray ja = new JSONArray(body);
		    // 순서대로 정렬이 안되는 관계로
		    // no,system,host,serverip,instance,port,agentsize,dummysize,status,license
		    String header = "no,system,host,serverip,instance,port,agentsize,dummysize,ioup,iodown,license";
		    String [] title = header.split(",");
		    
		    sb.append(header).append("\n");
		    for (int row = 0; row < ja.length(); row++){
		    	
		        JSONObject order = ja.getJSONObject(row);
		        System.out.println("[" + row +"] " + order.toString());
		        for (int title_index=0; title_index<title.length; title_index++) {
		        	sb.append(order.getString(title[title_index])).append(",");
		        }
		        sb.deleteCharAt(sb.length()-1);
		        sb.append("\n");
		     }
		    
		}
		catch (Exception e) {
			System.out.println(e.getMessage() + " : " + uri);
		}
	} else if (type.equals("current_wession")) {
		String body = "";
		String uri = "wession_wsstbl.jsp";
		String search = request.getParameter("search");
		
		try {
			uri = baseURL.replace("export.jsp", uri);
			sb.append(" - export time : ").append(Common.getDateFromLAT(System.currentTimeMillis())).append("\n");
			sb.append("CURRENT WESSION LIST").append("\n");
			
			URL url = new URL(uri+"?limit=60000&offset=0&search=");
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			body = body + IOUtils.toString(in, encoding);
			
		    JSONObject jo = new JSONObject(body);
		    JSONArray ja = jo.getJSONArray("repos");
		    //JSONArray ja = new JSONArray(body);
		    // 순서대로 정렬이 안되는 관계로
		    // no,system,host,serverip,instance,port,agentsize,dummysize,status,license
		    String header = "no,userid,name,userip,sso,biz,sales,remark,wsid";
		    String [] title = header.split(",");
		    
		    sb.append(header).append("\n");
		    for (int row = 0; row < ja.length(); row++){
		    	
		        JSONObject order = ja.getJSONObject(row);
		        //System.out.println("[" + row +"] " + order.toString());
		        for (int title_index=0; title_index<title.length; title_index++) {
		        	sb.append(order.getString(title[title_index])).append(",");
		        }
		        sb.deleteCharAt(sb.length()-1);
		        sb.append("\n");
		     }
		    
		    
		}
		catch (Exception e) {
			System.out.println(e.getMessage() + " : " + uri);
		}
	} else if (type.equals("policy_AA") || type.equals("policy_AL") || type.equals("policy_DA") || type.equals("policy_DL")) {
		String policycode = "";
		String allowed = "DY";
		
		InitialContext cxt = new InitialContext();
		javax.sql.DataSource ds = null;
		Connection conn = null;

		String JDBC_URL = getServletConfig().getServletContext().getInitParameter("JDBC_URL");

		ds = (javax.sql.DataSource) cxt.lookup(JDBC_URL);
		if (ds == null) {
			throw new Exception("Data source not found!");
		}
		conn = ds.getConnection();
		
		sb.append(" - export time : ").append(Common.getDateFromLAT(System.currentTimeMillis())).append("\n");
		if (type.equals("policy_AA")) {
			policycode = "AA";
			sb.append("POLICY - 접속IP 적용정책 LIST").append("\n");
		} else if (type.equals("policy_AL")) {
			policycode = "AL";
			sb.append("POLICY - 접속IP 허용정책 LIST").append("\n");
		} else if (type.equals("policy_DA")) {
			policycode = "DA";
			sb.append("POLICY - 중복로그인 적용정책 LIST").append("\n");
		} else if (type.equals("policy_DL")) {
			policycode = "DL";
			sb.append("POLICY - 중복로그인 허용정책 LIST").append("\n");
		}
		
		
		try {
			String sql = "select * from T_WESSION_MATTERS where 1=1 ";
			sql = sql + " and policycode = '" + policycode + "' and allowed='" + allowed + "' order by itemcode, lastModifyTime desc, data";
			System.out.println(sql);

			Statement select = conn.createStatement();
			ResultSet result = select.executeQuery(sql);
			int i = 0;
			sb.append("\n번호, 구분코드, 정책데이터, 정책데이터2, 비고사항, 등록자, 등록일시, 적용여부, 데이터코드\n");
			while (result.next()) { // process results one row at a time
				String itemcode = result.getString("itemcode");
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
				sb.  append(++i)
					.append(",").append(itemcode)
					.append(",").append(data)
					.append(",").append(accessip)
					.append(",").append(remark == null ? "-" : remark)
					.append(",").append(username == null ? "-" : username)
					.append(",").append(result.getString("lastModifyTime"))
					.append(",").append(result.getString("memload"))
					.append(",").append(result.getString("idx"))
					.append("\n");
			}

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
	} else if (type.equals("policy_violation")) {
		String body = "";
		String excel_title = "";
		String uri = "wession_policy_violation.jsp";
		
		try {
			uri = baseURL.replace("export.jsp", uri);
			
			URL url = null;
			
			String cmd = "getPolicyLog";
			String logdate = request.getParameter("logdate");
			String logtype = request.getParameter("logtype");
			if (logtype == null) {
				logtype = "ALL";
				cmd = "getPolicyLog";
				url = new URL(uri+"?cmd=" + cmd + "&logtype=" + logtype + "&logdate=" + logdate);
				excel_title = "Policy Carryout Report ";
			} else if (logtype.equals("DUPLOGIN")) {
				logtype = "DUPLOGIN";
				cmd = "getDupLoginLog";
				url = new URL(uri+"?cmd=" + cmd + "&logtype=" + logtype + "&logdate=" + logdate);
				excel_title = "Duplication Login Violation Report";
			} else {
				url = new URL(uri+"?logdate=" + logdate);
				excel_title = "Policy Violation Report.";
			}
			
			sb.append(" - export time : ").append(Common.getDateFromLAT(System.currentTimeMillis())).append("\n");
			sb.append(excel_title).append("\n");
			
			//System.out.println(url.toString());
			
			
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			body = body + IOUtils.toString(in, encoding);
			
		    //JSONObject jo = new JSONObject(body);
		    //JSONArray ja = jo.getJSONArray("repos");
		    JSONArray ja = new JSONArray(body);
		    // 순서대로 정렬이 안되는 관계로
		    // no,system,host,serverip,instance,port,agentsize,dummysize,status,license
		    String header = "date,time,server,userid,accessip,policycode,result";
		    String [] title = header.split(",");
		    
		    sb.append(header).append("\n");
		    for (int row = 0; row < ja.length(); row++){
		    	
		        JSONObject order = ja.getJSONObject(row);
		        //System.out.println("[" + row +"] " + order.toString());
		        sb.append(logdate).append(",");
		        for (int title_index=1; title_index<title.length; title_index++) {
		        	sb.append(order.getString(title[title_index])).append(",");
		        }
		        sb.deleteCharAt(sb.length()-1);
		        sb.append("\n");
		     }
		    
		    
		}
		catch (Exception e) {
			System.out.println(e.getMessage() + " : " + uri);
		}
	}
	
	out.println(sb.toString());
%>
