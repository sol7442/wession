<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	if (session.getAttribute("wessionadminid") == null) {
		response.sendRedirect("../../index.html");
		return;
	}
	String webTitle = "WESSION :: 메리츠화재";
	String adminUserName = (String) session
			.getAttribute("wessionadminname");
	String adminUserEmpNo = (String) session
			.getAttribute("wessionadminid");
	String copyright = "Copyright &copy; 2014 Wession .All rights reserved.";
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
					<a class="navbar-brand"> <i class="icon-th-large"></i> WESSION </a>
				</div>
				<ul class="nav navbar-nav">
					<li><a href="../dashboard/dashboard.jsp">dashboard</a></li>
					<li><a href="../wession/wession.jsp">wession</a></li>
					<li><a href="#">policy</a></li>
					<li><a href="../agent/agent.jsp">agent</a></li>
					<li class="active"><a href="../manager/manager.jsp">manager</a></li>
				</ul>
<jsp:include page="../inc/header_menu_right.jsp" flush="true" />
			</nav>
		</header>
		<div class="clearfix">
			<aside class="sidebar">
				<nav class="sidebar-nav">
					<ul id="menu">
						<li class="title">Manager</li>
						<li class="active"><a href="./manager.jsp"> Admin User</a></li>
						<!--  
						<li class><a href="./manager_02.jsp"> board</a></li>
						-->
					</ul>
					<div class="slider-toggle"></div>
				</nav>
			</aside>
			<section class="content">
				<ol class="breadcrumb">
					<li><i class="icon-home"></i> Home</li>
					<li>Manager</li>
					<li>Admin User</li>
				</ol>
				<div class="page">
					<div class="page-header">
						<div class="header-chart pull-right">
							<ul>
								<li></li>
							</ul>
						</div>
						<h1>
							Manager <small><i class="icon-double-angle-right"></i> Admin User</small>
						</h1>
					</div>
					<div class="page-body" style="margin-top:-25px;">
						<div id="toolbar">
							<div class="btn-group">
								<a class="btn btn-default" id="new-item" 
									data-toggle="modal" data-target="#modal-new-item" 
									rel="tooltip" data-toggle="tooltip" data-placement="top" 
									title="add Admin User" data-container="body"><i class="icon-tag"></i> Add User </a> 
									<a class="btn btn-default" id="delete"><i class="icon-remove"></i> Remove USer </a> 
							</div>
							<div id='deleteMessage' class="alert alert-info" style="display: none;">
							</div>
						</div>
						<table id="wss_tbl"></table>
					</div>
				</div>
			</section>
		</div>
		<div class="modal fade" id="modal-new-item">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
						<h4 class="modal-title">Add New Admin</h4>
					</div>
					<div class="modal-body">
						<form role="form" id="modal-form" class="form-horizontal">
							<div class="form-group form-group-sm">
								<label for="empno" class="col-xs-2 control-label">사용자 사번</label>
								<div class="col-xs-4">
									<input type="text" class="form-control" id="empno" name="empno" placeholder="Enter Admin Emp. No." />
								</div>
							</div>
							<div class="form-group form-group-sm">
								<label for="empname" class="col-xs-2 control-label">사용자 성명</label>
								<div class="col-xs-4">
									<input type="text" class="form-control" id="empname" name="empname" placeholder="Enter Admin Name" />
								</div>
							</div>
							<div class="form-group form-group-sm">
								<label for="encpassword" class="col-xs-2 control-label">비밀번호</label>
								<div class="col-xs-4">
									<input type="password" class="form-control" id="encpassword" name="encpassword" placeholder="Enter Password" />
								</div>
							</div>
							<div class="form-group form-group-sm">
								<label for="userauth" class="col-xs-2 control-label">관리자 권한</label>
								<div class="col-xs-4">
								<select class="form-control" id="userauth" name="userauth">
								  <option value="V">View</option>
								  <option value="A">Admin</option>
								</select>
								</div>
							</div>
							<!-- 
							<div class="form-group form-group-sm">
								<label for="comment" class="col-xs-2 control-label">비고사항</label>
								<div class="col-xs-10">
									<textarea class="form-control" id="comment" name="comment" placeholder="Enter Comment" rows="3"></textarea>
								</div>
							</div>
							 -->
						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">close</button>
						<button type="button" class="btn btn-danger" id="modal-btn-save">Save Change</button>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>
		<!-- /.modal -->
		
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
	<form name="admin_frm" id="admin_frm" method="post" action="../../json/wession_f.jsp" >
		<input type="hidden" id="userid" name="userid" />
	</form>
	<script src="../../dist/js/jquery-1.11.0.js"></script>
	<script src="../../dist/js/jquery-ui-1.10.4.custom.js"></script>
	<script src="../../dist/js/bootstrap.js"></script>
	<script src="../../dist/plugin/wession/wession.js"></script>
	<script src="../../dist/plugin/bootstraptable/bootstrap-table.min.js"></script>
	<script type="text/javascript">
		$(function() {
			var $table = $("#wss_tbl"), options = {
				url : '../../json/manager_adminuser.jsp',
				pagination : true,
				search : true,
				striped : true,
				pageSize : 20,
				pageList : [ 20, 50, 100 ],
				showRefresh : true,
				clickToSelect : true,
				toolbar : "#toolbar",
				idField : 'userid',
				columns : [ {
					field : 'state',
					checkbox : true
				}, {
					field : 'no',
					title : 'No'
				}, {
					field : 'userid',
					title : '사번'
				}, {
					field : 'name',
					title : '성명'
				}, {
					field : 'registdate',
					title : '등록일시'
				}, {
					field : 'modifyempno',
					title : '최종수정자'
				}, {
					field : 'remark',
					title : '비고사항'
				} ],
				queryParams : function(params) {
					return {
						limit : params.pageSize,
						offset : params.pageSize * (params.pageNumber - 1),
						search : params.searchText
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
			$("#delete").on("click", function(e) {
				// console.log('selected rows : ' + JSON.stringify($table.bootstrapTable('getSelections')));
				var rows = $table.bootstrapTable("getSelections");
				wession.removeRows(rows, function(id) {
					// console.log(id);
					// 서버에서 지우고 옴
					for (var index = 0; index < rows.length; index++) {
						//alert("wsid[" + rows[index].wsid + "]");
						if ($("#userid").val() == "")
							$("#userid").val(rows[index].userid);
						else
							$("#userid").val($("#userid").val() + ";" + rows[index].userid);
					}
					$.post("../../json/manager_data.jsp", $("#admin_frm").serialize() + "&method=delUser", function(resp) {
						var json = $.parseJSON(resp);
						alert(json.message);
						$table.bootstrapTable("refresh");
					});
				});
			});
			$("[rel=tooltip]").tooltip({
				delay : {
					show : 10,
					hide : 100
				}
			});
		});
		$("#modal-btn-save").on("click", function() {
			var empno = $.trim($("#empno").val());
			if (empno == "") {
				alert("사번은 필수 데이터 입니다.");
				$("#empno").focus();
				$("#empno").select();
				return;
			}

			var empname = $.trim($("#empname").val());
			if (empname == "") {
				alert("성명은 필수 데이터 입니다.");
				$("#empname").focus();
				$("#empname").select();
				return;
			}

			var encpassword = $.trim($("#encpassword").val());
			if (encpassword == "") {
				alert("비밀번호는 필수 데이터 입니다.");
				$("#encpassword").focus();
				$("#encpassword").select();
				return;
			}
			var frm = $("#modal-form");
			//alert("모달창의 저장버튼 클릭 \n\n폼 데이터: " + $(frm).serialize() );
			$.post("../../json/manager_data.jsp", $(frm).serialize() + "&" + $("#admin_frm").serialize() + "&method=addUser", function(resp) {
				var json = $.parseJSON(resp);
				$("#empno").val('');
				$("#empname").val('');
				$("#encpassword").val('');
				$("#comment").val('');
				alert(json.message);
				$("#wss_tbl").bootstrapTable("refresh");
			});
			$(this).parents("div.modal").modal("hide");
		});

	</script>

<jsp:include page="../inc/password_change.jsp" flush="true" />
			
</body>
</html>
