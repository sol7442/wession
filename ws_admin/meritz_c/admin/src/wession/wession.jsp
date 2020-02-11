<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	if (request.getProtocol().equals("HTTP/1.1"))
		response.setHeader("Cache-Control", "no-cache");

	if (session.getAttribute("wessionadminid") == null) {
		session.invalidate();
		System.out.println("Session Invalid");
		response.sendRedirect("../../index.html");
		return;
	}

	String webTitle = "WESSION :: 메리츠화재";
	String copyright = "Copyright &copy; 2015 Wession .All rights reserved.";
	
	String adminUserName = (String) session.getAttribute("wessionadminname");
	String adminUserEmpNo = (String) session.getAttribute("wessionadminid");
	boolean isAdmin = true;
	
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
					<li class="active"><a href="../wession/wession.jsp">wession</a></li>
					<li><a href="#">policy</a></li>
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
						<li class="title">Wession</li>
						<li class="active"><a href="wession.jsp"> Current Wession List </a></li>
						<li class=""><a href="./anticipatedproblems.jsp"> Anticipated Problems </a></li>
						<!--  
						<li class=""><a href="./policyviolation.jsp" > Policy Violation </a></li>
						 -->
					</ul>
					<div class="slider-toggle"></div>
				</nav>
			</aside>
			<section class="content">
				<ol class="breadcrumb">
					<li><i class="icon-home"></i> Home</li>
					<li>Wession</li>
					<li>wession list</li>
				</ol>
				<div class="page">
					<div class="page-header">
						<div class="header-chart pull-right">
							<ul>
								<li></li>
							</ul>
						</div>
						<h1>
							Wession List <small><i class="icon-double-angle-right"></i> Current Wession List</small>
						</h1>
					</div>
					<div class="page-body">
						<div id="toolbar">
							<button class="btn color color-asbestos" type="button" name="btn_remove" id="btn_remove">
								<i class="icon-remove-sign"></i> Remove Session
							</button>
							<button class="btn color color-asbestos" type="button" name="btn_session_manager_clear" id="btn_session_manager_clear">
								<i class="icon-remove-circle"></i> Session Manager Clear
							</button>
							<button class="btn color color-asbestos" type="button" name="btn_export" id="btn_export">
					 			<i class="icon-download"></i> Export
							</button>
							<div id='deleteMessage' class="alert alert-info" style="display: none;">
								<a href="#" class="close" data-dismiss="alert">&times;</a> <strong>삭제</strong> 해당 Wession 객체를 하였습니다.
							</div>
						</div>
						<table id="wss_tbl"></table>
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
	<form name="wss_frm" id="wss_frm" method="post" action="../../json/wession_f.jsp" >
		<input type="hidden" id="wsid" name="wsid" />
		<input type="hidden" id="method" name="method" />
		<input type="hidden" id="methodType" name="methodType" />
	</form>
	<script src="../../dist/js/jquery-1.11.0.js"></script>
	<script src="../../dist/js/jquery-ui-1.10.4.custom.js"></script>
	<script src="../../dist/js/bootstrap.js"></script>
	<script src="../../dist/plugin/wession/wession.js"></script>
	<script src="../../dist/plugin/bootstraptable/bootstrap-table.min.js"></script>
	<script src="../../dist/plugin/metismenu/metismenu.min.js"></script>
	<script type="text/javascript">
		$(function() {
			var $table = $("#wss_tbl"), options = {
				url : '../../json/wession_wsstbl.jsp',
				sidePagination : 'server',
				pagination : true,
				search : true,
				striped : true,
				pageSize : 20,
				pageList : [ 20, 50, 100 ],
				showRefresh : true,
				toolbar : "#toolbar",
				clickToSelect : true,
				idField : 'wsid',
				columns : [ {
					field : 'state',
					checkbox : true
				}, {
					field : 'wsid',
					title : 'ID',
					visible : false
				}, {
					field : 'no',
					title : 'No'
				}, {
					field : 'userid',
					title : 'UserID'
				}, {
					field : 'name',
					title : 'Name'
				}, {
					field : 'homepage',
					title : 'HOMEPAGE'
				}, {
					field : 'mhomepage',
					title : 'M.HOMEPAGE'
				}, {
					field : 'directmall',
					title : 'DIRECTMALL'
				}, {
					field : 'mdirectmall',
					title : 'M.DIRECTMALL'
				}, {
					field : 'remark',
					title : 'Remark'
				} ],
				queryParams : function(params) {
					return {
						limit : params.pageSize,
						offset : params.pageSize * (params.pageNumber - 1),
						search : params.searchText
					};
				},
				responseHandler : function(res) {
					return {
						rows : res.repos,
						total : res.total
					};
				},
				onClickRow : function(row, tr) {
					/*console.log("selected row index : " + $("tr").index(tr));
					console.log("selected row data : " + JSON.stringify(row));
					console.log("selected row id value : " + row.id);*/
				},
				formatLoadingMessage : function() {
					return 'Loading ...';
				},
				formatNoMatches : function() {
					return 'Not found Records';
				}
			};
			$table.bootstrapTable(options);
			$(".search input").tooltip({
				title : "Enter User ID for Search"
			});
			$("#btn_remove").on("click", function(e) {
				// console.log('selected rows : ' + JSON.stringify($table.bootstrapTable('getSelections')));
				var rows = $table.bootstrapTable("getSelections");
				wession.removeRows(rows, function(id) {
					// console.log(id);
					// 서버에서 지우고 옴
					for (var index = 0; index < rows.length; index++) {
						//alert("wsid[" + rows[index].wsid + "]");
						if ($("#wsid").val() == "")
							$("#wsid").val(rows[index].wsid);
						else
							$("#wsid").val($("#wsid").val() + ";" + rows[index].wsid);
					}
					deleteWessesion('del');
				});
			});
			$("#btn_session_manager_clear").on("click", function(e) {
				deleteWessesion('all');
			});
			$("[rel=tooltip]").tooltip({
				delay : {
					show : 10,
					hide : 100
				}
			});
		});
		$("#btn_export").on("click", function(e) {

			window.open("../../json/export.jsp?type=current_wession");
		});
		
		function deleteWessesion(type) {
			if ('all' == type) {
				if (confirm("모든 세션을 삭제하시겠습니까?")) {
					$("input[name=method]").val('del');
					$("input[name=methodType]").val('all');
					$.post("../../json/wession_fdw.jsp", $("#wss_frm").serialize(), function(resp) {
						//var json = $.parseJSON(resp);
						$("#deleteMessage").show();
						window.setTimeout(function() {
							$("#deleteMessage").hide();
						}, 2000);
						$("#wsid").val('');
						$("#wss_tbl").bootstrapTable("refresh");
					});
				}
			} else {
				$("input[name=method]").val('del');
				$("input[name=methodType]").val('multi');
				$.post("../../json/wession_fdw.jsp", $("#wss_frm").serialize(), function(resp) {
					//var json = $.parseJSON(resp);
					$("#deleteMessage").show();
					window.setTimeout(function() {
						$("#deleteMessage").hide();
					}, 2000);
					$("#wsid").val('');
					$("#wss_tbl").bootstrapTable("refresh");
				});
			}
		}
	</script>
	
<jsp:include page="../inc/password_change.jsp" flush="true" />
	
</body>
</html>
