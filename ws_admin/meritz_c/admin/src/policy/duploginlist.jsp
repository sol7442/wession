<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.wession.Common" %>
<%
	if (session.getAttribute("wessionadminid") == null) {
		session.invalidate();
		System.out.println("Session Invalid");
		response.sendRedirect("../../index.html");
		return;
	}
	String webTitle = "WESSION :: 메리츠화재";
	String company_policy = "메리츠화재 ";
	String adminUserName = (String) session.getAttribute("wessionadminname");
	String adminUserEmpNo = (String) session.getAttribute("wessionadminid");
	String copyright = "Copyright &copy; 2014 Wession .All rights reserved.";
	boolean isAdmin = true;

	String today = Common.getDateFromLAT(System.currentTimeMillis()).substring(0, 10);

	// 일정시간이 지난 후에만 사용
%>
<!doctype html>
<html lang="en">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta charset="utf-8" />
<title><%=webTitle%></title>
<link rel="stylesheet" type="text/css" href="../../dist/css/metro-bootstrap.css" />
<link rel="stylesheet" type="text/css" href="../../dist/css/font-awesome.css" />
<link rel="stylesheet" type="text/css" href="../../dist/plugin/wession/wession.css" />
<link rel="stylesheet" type="text/css" href="../../dist/plugin/metismenu/metismenu.css" />
<link rel="stylesheet" type="text/css" href="../../dist/plugin/bootstraptable/bootstrap-table.min.css" />
<link rel="stylesheet" type="text/css" href="../../dist/plugin/datetimepicker/datepicker3.css" />
<!--[if lt IE 9]>
		<script src="../../dist/js/html5shiv.js"></script>
	<![endif]-->
</head>
<body>
	<div id="wrapper">
		<header class="header-wrapper">
			<nav class="navbar navbar-default navbar-static-top">
				<div class="navbar-header">
					<a class="navbar-brand"> <i class="icon-th-large"></i> WESSION
					</a>
				</div>
				<ul class="nav navbar-nav">
					<li><a href="../dashboard/dashboard.jsp">dashboard</a></li>
					<li class=""><a href="../wession/wession.jsp">wession</a></li>
					<li class="active"><a href="../policy/authip_adopt.jsp">policy</a></li>
					<li><a href="../agent/agent.jsp">agent</a></li>
					<li><a href="../manager/manager.jsp">manager</a></li>
				</ul>
				<jsp:include page="../inc/header_menu_right.jsp" flush="true" />
			</nav>
		</header>
		<div class="clearfix">
			<aside class="sidebar">
				<nav class="sidebar-nav">
					<ul id="menu">
						<li class="title">Policy</li>
						<li class=""><a href="#"> <%= company_policy %> 인증정책<i class="icon-angle-down icon-1x pull-right"></i></a>
							<ul class="submenu collapse">
								<li class=""><a href="./authip_adopt.jsp"><i class="menu-icon icon-caret-right "></i> 접속IP 적용정책</a></li>
								<li class=""><a href="./authip_allow.jsp"><i class="menu-icon icon-caret-right "></i> 접속IP 허용정책</a></li>
								<li class=""><a href="./duplogin_adopt.jsp"><i class="menu-icon icon-caret-right "></i> 중복로그인 적용정책</a></li>
								<li class=""><a href="./duplogin_allow.jsp"><i class="menu-icon icon-caret-right "></i> 중복로그인 허용정책</a></li>
							</ul></li>
							<li class=""><a href="#"> 정책수행결과 조회<i class="icon-angle-down icon-1x pull-right"></i></a>
							<ul class="submenu collapse">
								<li class=""><a href="./policyviolation.jsp?policycode=AA"><i class="menu-icon icon-caret-right "></i> 접속IP 적용정책</a></li>
								<li class=""><a href="./policyviolation.jsp?policycode=AL"><i class="menu-icon icon-caret-right "></i> 접속IP 허용정책</a></li>
								<li class=""><a href="./policyviolation.jsp?policycode=DA"><i class="menu-icon icon-caret-right "></i> 중복로그인 적용정책</a></li>
								<li class=""><a href="./policyviolation.jsp?policycode=DL"><i class="menu-icon icon-caret-right "></i> 중복로그인 허용정책</a></li>
							</ul></li>
						<li class="active"><a href="./duploginlist.jsp"> 중복로그인 현황</a></li>
						<li class=""><a href="./simulator.jsp"> Simulator</a></li>
					</ul>
					<div class="slider-toggle"></div>
				</nav>
			</aside>
			<section class="content">
				<ol class="breadcrumb">
					<li><i class="icon-home"></i> Home</li>
					<li>Policy Manager</li>
					<li>중복로그인 현황</li>
				</ol>
				<div class="page">
					<div class="page-header">
						<div class="header-chart pull-right">
							<ul>
								<li></li>
							</ul>
						</div>
						<h1>
							중복로그인 현황 <small><i class="icon-double-angle-right"></i> </small>
						</h1>
					</div>
					<div class="page-body" style="margin-top:-30px;">	
						<div id="toolbar">
							<div class="input-group" style="width: 200px;">
								<input class="form-control" type="text" name="logdate" id="logdate" value="<%= today %>" style="display: inline;" />
								<span class="input-group-btn">
								<button class="btn color color-asbestos pull-left" type="button" name="btn_move" id="btn_move">
						 			<i class="icon-search"></i> Move
								</button>
								<button class="btn color color-asbestos" type="button" name="btn_export" id="btn_export" style="margin-left: 3px;">
					 			<i class="icon-download"></i> Export
							</button>
								</span>
							</div>
						</div>
						<table id="wss_tbl" ></table>
					</div>
				</div>
			</section>
		</div>
		<footer class="footer-wrapper">
			<!-- 
     <div class="row" style="display:none;">
      <div class="col-xs-3 col-xs-offset-1">
        <h3><i class="icon-th-large"></i> WESSION</h3>
        <h4>sub description</h4>
        <p>
          wession은 중복 로그인 방지를 위한 솔루션으로 ..
        </p>
      </div>
      <div class="col-xs-7 col-xs-offset-1">
      <h4 class="hidden-phone">Wession is solution ...</h4>
        <div class="row">
          <div class="col-xs-3">
            <ul class="list-unstyled footer-nav">
              <li><i class="caret"></i> <a href="#">wession</a></li>
              <li><i class="caret"></i> <a href="#">policy</a></li>
              <li><i class="caret"></i> <a href="#">agent</a></li>
              <li><i class="caret"></i> <a href="#">manager</a></li>
            </ul>
          </div>
          <div class="col-xs-3">
            <ul class="stat list-unstyled">
              <li>Server Specification </li><span> 5,105 kb</span>
              <li>now pageroup </li><span> no.1</span>
              <li>Total page </li><span> 13 .pg</span>
            </ul>
          </div>
          <div>
            <ul class="share list-unstyled">
              <li><i class="icon-gears"></i> <a href="#">business system 01</a></li>
              <li><i class="icon-gears"></i> <a href="#">business system 01</a></li>
              <li><i class="icon-gears"></i> <a href="#">business system 01</a></li>
              <li><i class="icon-gears"></i> <a href="#">business system 01</a></li>
            </ul>
          </div>
        </div>
      </div>
     </div>
 -->
			<div class="footer-bottom-wrapper">
				<div class="row">
					<div class="col-xs-6">
						<p><%=copyright%></p>
					</div>
					<div class="col-xs-6">
						<ul class="list-inline pull-right">
							<li><a href="#" rel="tooltip" title="tooltip" data-placement="top"></a></li>
						</ul>
					</div>
				</div>
			</div>
		</footer>
	</div>


	<script src="../../dist/js/jquery-1.11.0.js"></script>
	<script src="../../dist/js/jquery-ui-1.10.4.custom.js"></script>
	<script src="../../dist/js/bootstrap.js"></script>
	<script src="../../dist/plugin/wession/wession.js"></script>
	<script src="../../dist/plugin/bootstraptable/bootstrap-table.min.js"></script>
	<script src="../../dist/plugin/metismenu/metismenu.min.js"></script>
	<script src="../../dist/plugin/datetimepicker/bootstrap-datepicker.js"></script>
	<script type="text/javascript">
		$(function() {
			$("#logdate").datepicker({
				format: "yyyy-mm-dd",
				todayBtn: 'linked'
			})
			var $table = $("#wss_tbl"), 
				options = {
				url : "../../json/wession_policy_violation.jsp?logtype=DUPLOGIN&logdate="+$("#logdate").val(),
				pagination : true,
				search : true,
				striped : true,
				pageSize : 20,
				pageList : [ 20, 50, 100 ],
				showRefresh : true,
				toolbar : "#toolbar",
				sortName: "time",
				sortOrder: "desc",
				columns : [ {
					field : 'time',
					title : '시간',
					sortable : true,
					order: 'asc'
				}, {					
					field : 'server',
					title : '수행서버',
					sortable : true
				}, {
					field : 'userid',
					title : '사번',
					sortable : true
				}, {
					field : 'orgin_ip',
					title : '차단된IP'
				}, {
					field : 'access_ip',
					title : '로그인IP'
				}, {
					field : 'result',
					title : '비고'
				} ],
				formatLoadingMessage : function() {
					return 'Loading ...';
				},
				formatNoMatches : function() {
					return 'Not found Records';
				}
			};
			$table.bootstrapTable(options);
			$("[rel=tooltip]").tooltip({});
			$("#btn_move").on("click", function(e) {
				$.getJSON("../../json/wession_policy_violation.jsp?logtype=DUPLOGIN&logdate="+$("#logdate").val()).done(function(data) {
					$table.bootstrapTable("load", data);
				});
			});

		});
		$("#btn_export").on("click", function(e) {
			window.open("../../json/export.jsp?type=policy_violation&logtype=DUPLOGIN&logdate="+$("#logdate").val());
		});
		
		function policyFormatter(value, row, index) {
			if (value=="AA") {
				return "접속IP 적용정책";
			} else if (value=="AL") {
				return "접속IP 허용정책";
			} else if (value=="DA") {
				return "중복로그인 적용정책";
			} else if (value=="DL") {
				return "중복로그인 허용정책";
			}
		}
	
		
	</script>
	<script type="text/javascript">
		$(function() {
			$("#menu").metisMenu();
			$("#btn_verification").on("click", function() {
				var frm = $("#verification-form");
				frm.submit();
			});
			$("[rel=tooltip]").tooltip();
		});
	</script>
<jsp:include page="../inc/password_change.jsp" flush="true" />

</body>
</html>
