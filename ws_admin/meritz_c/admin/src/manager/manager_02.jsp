<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String webTitle = "WESSION :: 메리츠화재";
	String adminUserName = "사용자명";
	String adminUserEmpNo = "109201234";
	String copyright = "Copyright &copy; 2014 Wession .All rights reserved.";
	boolean isAdmin = true;
%>
<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title><%=webTitle%></title>

<link rel="stylesheet" type="text/css" href="../../dist/css/metro-bootstrap.css" />
<link rel="stylesheet" type="text/css" href="../../dist/css/font-awesome.css" />
<link rel="stylesheet" type="text/css" href="../../dist/plugin/wession/wession.css" />
<link rel="stylesheet" type="text/css" href="../../dist/plugin/metismenu/metismenu.css" />
<link rel="stylesheet" type="text/css" href="../../dist/plugin/bootstraptable/bootstrap-table.min.css" />
<!--[if lt IE 9]>
		<script src="/dist/js/html5shiv.js"></script>
	<![endif]-->
</head>
<body>
	<div id="wrapper">
		<header class="header-wrapper">
			<nav class="navbar navbar-default navbar-static-top">
				<div class="navbar-header">
					<a class="navbar-brand" href="../dashboard.jsp"> <i class="icon-th-large"></i> WESSION
					</a>
				</div>
				<ul class="nav navbar-nav">
					<li><a href="../dashboard/dashboard.jsp">dashboard</a></li>
					<li><a href="../wession/wession.jsp">wession</a></li>
					<li><a href="../policy/duplogin_adopt.jsp">policy</a></li>
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
		          <li class><a href="./manager.jsp"> manager</a></li>
		          <li class="active"><a href="./manager_02.jsp"> board</a></li>
		        </ul>
		        <div class="slider-toggle"></div>
		      </nav>
		    </aside>
		    <section class="content">
		      <ol class="breadcrumb">
		        <li><i class="icon-home"></i> Home</li>
		        <li>Manager</li>
		        <li>board</li>
		      </ol>
		      <div class="page">
		        <div class="page-header">
		          <div class="header-chart pull-right">
		            <ul>
		              <li></li>
		            </ul>
		          </div>
		          <h1>Board <small><i class="icon-double-angle-right"></i> </small></h1>
		        </div>
		        <div class="page-body">
	          		<div id="toolbar">
	              		<button class="btn color color-asbestos" type="button" name="btn_new" id="btn_new" data-toggle="modal" data-target="#modal-board"><i class="icon-tag" data-toggle="tooltip" rel="tooltip" data-placement="top" title="New Write"></i></button>
						<div id='deleteMessage' class="alert alert-info" style="display: none;">
							<a href="#" class="close" data-dismiss="alert">&times;</a> <strong>삭제</strong> 해당 Wession 객체를 하였습니다.
						</div>
	          		</div>
					<table id="wss_tbl"></table>
		        </div>
		      </div>
		    </section>
		</div>


		<div class="modal fade" id="modal-board">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		        <h4 class="modal-title">New Comment</h4>
		      </div>
		      <div class="modal-body">
		        <form role="form" id="modal-form" class="form-horizontal">
		        	<div class="form-group form-group-sm">
		        		<label for="userno" class="col-xs-2 control-label">User NO.</label>
		        		<div class="col-xs-10">
		        			<p class="form-control-static"> <%=adminUserName%>(<%=adminUserEmpNo%>) </p>
		        		</div>
		        	</div>
		        	<div class="form-group form-group-sm">
		        		<label for="title" class="col-xs-2 control-label">Title.</label>
		        		<div class="col-xs-10">
		        				<input type="text" class="form-control" id="title" name="title" placeholder="Enter TItle" />
		        		</div>
		        	</div>
		        	<div class="form-group form-group-sm">
		        		<label for="userno" class="col-xs-2 control-label">Comment.</label>
		        		<div class="col-xs-10">
		        				<textarea class="form-control" id="comment" name="comment" placeholder="Enter Comment" style="height: 105px;"></textarea> 
		        		</div>
		        	</div>
		        </form>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">close</button>
		        <button type="button" class="btn btn-primary" id="modal-btn-save">Save Change</button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div><!-- /.modal -->

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
	<form name="wss_frm" id="wss_frm" method="post" action="../../json/wession_f.jsp" />
	<input type="hidden" id="wsid" name="wsid" />
	<input type="hidden" id="method" name="method" />
	<input type="hidden" id="methodType" name="methodType" />
	</form>
	<script src="../../dist/js/jquery-1.11.0.js"></script>
	<script src="../../dist/js/jquery-ui-1.10.4.custom.js"></script>
	<script src="../../dist/js/bootstrap.js"></script>
	<script src="../../dist/plugin/wession/wession.js"></script>
	<script src="../../dist/plugin/bootstraptable/bootstrap-table.min.js"></script>
	<script type="text/javascript">
		$(function() {
			var $table = $("#wss_tbl"), 
					frm = $("#model-form"),
					options = {
						url : '../../json/wession_wsstbl.jsp',
						sidePagination : 'server',
						pagination : true,
						search : false,
						striped : true,
						pageSize : 20,
						pageList : [ 20, 50, 100 ],
						showRefresh : false,
						toolbar : "#toolbar",
						clickToSelect : true,
						columns : [{
							field : 'wsid',
							title : 'ID',
							visible : false
						}, {
							field : 'no',
							title : 'No'
						}, {
							field : 'title',
							title : 'Title'
						}, {
							field : 'regdate',
							title : '등록일자'
						}, {
							field : 'reguser',
							title : '작성자'
						}, {
							field : null,
							title : '',
							formatter: 'operateFormatter',
							events: 'operateEvents',
							align : 'center',
							width : '80'
						} ],
						queryParams : function(params) {
							return {
								limit : params.pageSize,
								offset : params.pageSize * (params.pageNumber - 1),
								search : params.searchText
							}
						},
						responseHandler : function(res) {
							return {
								rows : res.repos,
								total : res.total
							}
						},
						formatLoadingMessage : function() {
							return 'Loading ...';
						},
						formatNoMatches : function() {
							return 'Not found Records'
						}
					};
			$table.bootstrapTable(options);
			$(".search input").tooltip({
				title : "Enter User ID for Search"
			});
			$("#modal-btn-save").on("click", function(){
				var frm = $("#modal-form");
				alert("모달창의 저장버튼 클릭 \n\n폼 데이터: " + $(frm).serialize() );
				$(this).parents("div.modal").modal("hide");			

			});
			$("[rel=tooltip]").tooltip({
				container: 'body'
			});
		});

	  function operateFormatter(value, row, index) {
		  return [
		    '<a href="#" class="edit" title="Edit" style="margin-right: 10px;" data-toggle="modal" data-target="#modal-board"">',
		        '<i class="icon-edit"></i>',
		    '</a>',
		    '<a href="#" class="remove" title="Remove" rel="tooltip" data-placement="top" >',
		        '<i class="icon-remove"></i>',
		    '</a>'
		  ].join('');
			$("[rel=tooltip]").tooltip({
				container: 'body'
			});
		};


		window.operateEvents = {
		  'click .edit': function (e, value, row, index) {
		 		e.preventDefault();
		    alert('You click edit icon, row: ' + JSON.stringify(row));
		    $("#title").val(row.name);
		    $("#comment").val(row.wsid);
		  },
		  'click .remove': function (e, value, row, index) {
		 		e.preventDefault();
		    alert('You click remove icon, row: ' + JSON.stringify(row));
		    var no = row.no;
		  }
		};
		
		function deleteWessesion(type) {
			if ('all' == type) {
				if (confirm("모든 세션을 삭제하시겠습니까?")) {
					$("input[name=method]").val('del');
					$("input[name=methodType]").val('all');
					$.post("../../json/wession_fdw.jsp", $("#wss_frm").serialize(), function(resp) {
						var json = $.parseJSON(resp);
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
					var json = $.parseJSON(resp);
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
</body>
</html>
