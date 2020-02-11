<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.naming.*"%>
<%@ page import="javax.activation.*"%>
<%@page import="com.wow.wession.agent.*"%>
<%@page import="com.wow.wession.session.ISession"%>
<%@page import="com.wow.wession.session.ISessionMessage"%>
<%@page import="com.wession.*"%>
<%@page import="com.wession.model.*"%>
<%
	String testDBdata = "";
	StringBuffer sbDB = new StringBuffer();
	StringBuffer sb = new StringBuffer();
	HashMap <String, String> hmData = new HashMap <String, String>();

    InitialContext cxt = new InitialContext();
    javax.sql.DataSource ds = null;
    Connection conn = null;
        if ( cxt == null ) {
       throw new Exception("Uh oh -- no context!");
    }
    ds = (javax.sql.DataSource) cxt.lookup( "java:/comp/env/jdbc/meritzDB" );
    if ( ds == null ) {
       throw new Exception("Data source not found!");
    }
    conn = ds.getConnection();
    sbDB.append(conn.getMetaData().getDriverVersion());
    
    try {
    	String sql = "select * from T_WPOLICY_ACS1";
    	
    	Statement select = conn.createStatement();
    	ResultSet result = select.executeQuery(sql);
    	while (result.next()) { // process results one row at a time
   	        String key = result.getString(1);
   	        String val = result.getString(2);
	hmData.put(key, val);
    	}
    	select.close();
    } catch (Exception e) {
    	
    } finally {
        if (conn != null) {
            try {
              conn.close();
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
     }
    
    // Trans를 날려서 가지고 오자.
    Environment env = Environment.getInstance();
    ConfigAgent agent = env.getConfigAgent("wession_license_01");
    System.out.println(agent.getAppServiceURL());
    String agentURL = env.getAgentServiceURL_license("wession_license_01");
    
    Vector <String> a =  env.getAgentServiceURL("DEMOAPP");
    System.out.println(a.size() + ":" + a.get(0));
    
%>
<html>
<head>
<link href="admin/dist/css/wession.css" rel="stylesheet" type="text/css">
<link href="admin/dist/css/bootstrap.ko.css?v.1.1.2" rel="stylesheet"
	type="text/css">
<link href="admin/dist/css/font-awesome.min.css" rel="stylesheet"
	type="text/css">
<link href="admin/dist/css/bootstrap-table.min.css" rel="stylesheet"
	type="text/css">

</head>
<body>
	<div class="container">
		<div class="row">
			<div class="span8">
				<div class="panel panel-default">
					<div class="panel-heading">DB Connection Info</div>
					<div class="panel-body">
						<%=sbDB.toString()%>
					</div>
				</div>
			</div>
			<div class="span4">
				<div class="panel panel-default">
					<div class="panel-heading">Panel title 2</div>
					<div class="panel-body">
						GET MYSQL -
						<%=testDBdata%>
						<hr />
						Ajax를 통한 저장 TEST <br /> <br /> <input type="text" name="testid"
							id="testid" style="width: 120px;" /> <input type="button"
							name="btnSave" id="btnSave" value=" 저장 " />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="span8">
				<div class="panel panel-default">
					<div class="panel-heading">부트스트랩 테이블 쿼리를 이용한 테이블</div>
					<div class="panel-body">
						<table data-toggle="table" data-url="./dbtest_json.jsp"
							data-height="299">
							<thead>
								<tr>
									<th data-field="id">ID</th>
									<th data-field="empno">사번</th>
									<th data-field="ip">적용 IP</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
			<div class="span4">
				<div class="panel panel-default">
					<div class="panel-heading">자바스크립트로 자동생성</div>
					<div class="panel-body">

						<table id="table-javascript"></table>

					</div>
				</div>
			</div>
		</div>
	</div>
	
	<hr/>
	<%= agentURL %>

	<script src="admin/dist/js/jquery-1.9.1.min.js"></script>
	<script src="admin/dist/js/bootstrap.min.js"></script>
	<script src="admin/dist/js/bootstrap-table.min.js"></script>
	<script language="javascript">
		/*
		 $('#table-test').bootstrapTable({
		 url: './dbtest_json.jsp'
		 });
		 */

		$(function() {
			$('#table-javascript').bootstrapTable({
				method : 'post',
				url : './dbtest_json2.jsp',
				height : 400,
				striped : true,
				pagination : true,
				pageSize : 10,
				pageList : [ 10, 25, 50, 100, 200 ],
				search : true,
				showColumns : true,
				minimunCountColumns : 2,
				columns : [ {
					field : 'state',
					checkbox : true
				}, {
					field : 'EMPNO',
					title : '사번',
					align : 'center',
					valign : 'middle',
					sortable : true
				}, {
					field : 'REGUSER',
					title : '조작자사번',
					align : 'center',
					valign : 'middle',
					sortable : true
				} ]
			});
		});
	</script>
</body>
</html>