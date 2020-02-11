<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.wession.policy.meritz.*"%>
<%
	if (session.getAttribute("wessionadminid") == null) {
		response.sendRedirect("../../index.html");
		return;
	}
	String webTitle = "WESSION :: 메리츠화재";
	String company_policy = "메리츠화재 ";
	String adminUserName = (String) session.getAttribute("wessionadminname");
	String adminUserEmpNo = (String) session.getAttribute("wessionadminid");
	String copyright = "Copyright &copy; 2014 Wession .All rights reserved.";
	boolean isAdmin = true;
	
	AL policy = AL.getInstance();
	String default_policy = policy.getDefaultPolicy();
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
<style>
	#modal-search-wrapper {
		margin-bottom: 20px;
	}
	.modal .search input {
		display: none;
	}
	#modal-search-wrapper label {
		margin-left: 5px;
	}
	
	.cursorhand {
		cursor:pointer;
	}
</style>
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
					<li><a href="../wession/wession.jsp">wession</a></li>
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
						<li class="active"><a href="#"> <%= company_policy %> 인증정책<i class="icon-angle-down icon-1x pull-right"></i></a>
							<ul class="submenu collapse in">
								<li class=""><a href="./authip_adopt.jsp"><i class="menu-icon icon-caret-right "></i> 접속IP 적용정책</a></li>
								<li class="active"><a href="./authip_allow.jsp"><i class="menu-icon icon-caret-right "></i> 접속IP 허용정책</a></li>
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
						<li class=""><a href="./duploginlist.jsp"> 중복로그인 현황</a></li>
						<li class=""><a href="./simulator.jsp"> Simulator</a></li>
					</ul>
					<div class="slider-toggle"></div>
				</nav>
			</aside>
			<section class="content">
				<ol class="breadcrumb">
					<li><i class="icon-home"></i> Home</li>
					<li>Policy Manager</li>
					<li><%= company_policy %> 인증정책</li>
					<li>접속IP 허용정책</li>
				</ol>
				<div class="page">
					<div class="page-header">
						<div class="header-chart pull-right">
							<ul>
							</ul>
						</div>
						<h1>
							<%= company_policy %> 인증정책 <small><i class="icon-double-angle-right"></i> 접속IP 허용정책</small>
						</h1>
					</div>
					<div class="alert alert-warning" style="margin-bottom: 0;">
						<h4>
							<i class="icon-warning-sign"></i> <strong>지정한 사용자를 적용합니다.</strong>
						</h4>
						<p>아래 설정된 사번과 접속아이피를 기준으로 접속을 허가하는 정책입니다. 정책에 거부되는 사용자 목록은 policy.log 에 기록 됩니다.</p>
						<!-- 
	          <p><button type="button" class="btn btn-default"> 정책변경 </button></p>
	           -->
					</div>
					<div class="page-body">
						<div id="toolbar">
							<div class="btn-group">
								<a class="btn btn-default" id="datatype" style="width: 90px; text-align: left;"> <span class="data-type">ID-IP 매핑</span>
								</a> <a class="btn color color-asbestos dropdown-toggle" data-toggle="dropdown"> <i class="icon-angle-down"></i>
								</a>
								<ul class="dropdown-menu" role="menu" id="datatype-dropdown-menu">
									<li><a href="">ID-IP 매핑</a></li>
									<li><a href="">공용 PC IP</a></li>
									<li><a href="">특수 사용자</a></li>
									<!-- 
	          			<li><a href="">부서코드</a></li>
	          			<li><a href="">접속IP</a></li>
	          			<li><a href="">역할코드</a></li>
	          			 -->
								</ul>
							</div>
							<div class="btn-group">
								<a class="btn btn-default" id="new-item" data-toggle="modal" data-target="#modal-new-item" rel="tooltip" data-placement="top" title="new item" data-container="body"><i class="icon-tag"></i> New</a> 
								<a class="btn btn-default" id="delete"><i class="icon-remove"></i> Remove</a> 
								<a class="btn btn-default" id="launch"><i class="icon-cog"></i> Execute</a>
								<a class="btn btn-default" id="export"><i class="icon-download"></i> Export</a>
							</div>
						</div>
						<table id="tbl_policymatter"></table>
					</div>
				</div>
			</section>
		</div>
		<div class="modal fade" id="modal-new-item">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
						<h4 class="modal-title">New Item</h4>
					</div>
					<div class="modal-body">
						<form name="form" id="modal-form" class="form-horizontal">
							<div class="form-group form-group-sm" id="modal-form-input-1">
								<label for="userno" class="col-xs-2 control-label"><span id="modal-form-input-1-title">User ID</span></label>
								<div class="col-xs-7">
									<input type="text" class="form-control" id="data" name="data" placeholder="Enter User ID" />
								</div>
								<button type="button" class="btn btn-sm" id="btn-search">Search</button>
							</div>
							<div class="form-group form-group-sm" id="modal-form-input-2">
								<label for="userno" class="col-xs-2 control-label">Access IP</label>
								<div class="col-xs-7">
									<input type="text" class="form-control" id="accessip" name="accessip" placeholder="Enter IP" />
								</div>
							</div>
							<div class="form-group form-group-sm">
								<label for="userno" class="col-xs-2 control-label">Comment.</label>
								<div class="col-xs-10">
									<textarea class="form-control" id="comment" name="comment" placeholder="Enter Comment" rows="3" style="height: 76px"></textarea>
								</div>
							</div>
						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">close</button>
						<button type="button" class="btn btn-primary" id="modal-btn-save">Save Change</button>
					</div>
					<div class="modal-body" id="modal-search-wrapper">
						<!-- 추가 코드 -->
						<div class="modal_search" style="display:none;">
						<legend>Search</legend>
						<table id="modal_table"></table>
						</div>
						<!-- 추가 코드 // end -->
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
	<form name="policy_frm" id="policy_frm" method="post" action="" >
		<input type="hidden" id="policySerial" name="policySerial" />
		<input type="hidden" id="policycode" name="policycode" value="AL" />
		<input type="hidden" id="allowed" name="allowed" value="DY" />
		<input type="hidden" id="itemcode" name="itemcode" value="MP" />
	</form>
	<script src="../../dist/js/jquery-1.11.0.js"></script>
	<script src="../../dist/js/jquery-ui-1.10.4.custom.js"></script>
	<script src="../../dist/js/bootstrap.js"></script>
	<script src="../../dist/plugin/bootstraptable/bootstrap-table.min.js"></script>
	<script src="../../dist/plugin/metismenu/metismenu.min.js"></script>
	<script src="../../dist/plugin/wession/wession.js"></script>
	<script type="text/javascript">
		$(function() {
			var $table = $("#tbl_policymatter"), options = {
				url : '../../json/authip_adopt_policymatter.jsp',
				sidePagination : 'client',
				pagination : true,
				search : true,
				striped : true,
				pageSize : 20,
				pageList : [ 20, 50, 100 ],
				showRefresh : true,
				toolbar : "#toolbar",
				clickToSelect : true,
				idField : 'serial',
				columns : [ {
					field : 'state',
					checkbox : true
				}, {
					field : 'serial',
					title : 'serial',
					visible : false
				}, {
					field : 'no',
					title : '번호'
				}, {
					field : 'data',
					title : '사번'
				}, {
					field : 'accessip',
					title : '접속아이피'
				}, {
					field : 'regdate',
					title : '등록일시'
				}, {
					field : 'remark',
					title : '비고'
				}, {
					field : 'load',
					title : '적용여부'
				} ],
				queryParams : function(params) {
					return {
						limit : params.pageSize,
						offset : params.pageSize * (params.pageNumber - 1),
						search : params.searchText,
						policycode : $('#policycode').val(),
						allowed : $('#allowed').val(),
						itemcode : $('#itemcode').val()
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
			}, 

				// 추가 
				option2 = {
					url :'../../json/policy_search.jsp',
					height: 410,
					sidePagination: 'client',
					pagenation : false,
					striped: true,
					clickToSelect: true,
					rowStyle: rowStyle,
					toolbar: '#modal-toolbar',
					columns: [
						{
							field: 'no',
							title: '번호'
						}, {
							field: 'empno',
							title: '사번'
						}, {
							field: 'empnm',
							title: '사원명'
						}, {
							field: 'deptnm',
							title: '부서명'
						}
					],
					onClickRow: function(row, tr) {
						if (row) {
							$("#data").val(row.empno);
						}
					}
				};
			$("#menu").metisMenu();
			$("#datatype-dropdown-menu").on("click", "a", function(e) {
				e.preventDefault();
				var holder = $(this).parents("div.btn-group").find("a:eq(0)");
				holder.text(this.outerText);
				//$table.find("th:eq(2) div:first-child").text(this.outerText);
				if (this.outerText == "ID-IP 매핑") {
					$('#itemcode').val("MP");
					$table.find("th:eq(2) div:first-child").text("사번");
					$table.find("th:eq(3) div:first-child").text("접속아이피");
				} else if (this.outerText == "특수 사용자") {
					$('#itemcode').val("ID");
					$table.find("th:eq(2) div:first-child").text("사번");
					$table.find("th:eq(3) div:first-child").text("-");
				} else if (this.outerText == "공용 PC IP") {
					$('#itemcode').val("IP");
					$table.find("th:eq(2) div:first-child").text("접속아이피");
					$table.find("th:eq(3) div:first-child").text("-");
				} else if (this.outerText == "역할코드") {
					$('#itemcode').val("EM");
				}
				$table.bootstrapTable("refresh");
				;
			});
			$("#launch").on("click", function() {
				if (confirm("접속IP허용 정책을 로딩 하시겠습니까?") == false)
				    return;
				
				$.post("../../json/policy_set.jsp", $("#policy_frm").serialize(), function(resp) {
					var json = $.parseJSON(resp);
					alert(json.message);
					$("#tbl_policymatter").bootstrapTable("refresh");
				});
			});
			$table.bootstrapTable(options);
			$(".search input").tooltip({
				title : "Enter Data Search"
			});
			$("#delete").on("click", function(e) {
				// console.log('selected rows : ' + JSON.stringify($table.bootstrapTable('getSelections')));
				var rows = $table.bootstrapTable("getSelections");
				wession.removeRows(rows, function(id) {
					// console.log(id);
					// 서버에서 지우고 옴
					for (var index = 0; index < rows.length; index++) {
						//alert("wsid[" + rows[index].wsid + "]");
						if ($("#policySerial").val() == "")
							$("#policySerial").val(rows[index].serial);
						else
							$("#policySerial").val($("#policySerial").val() + ";" + rows[index].serial);
					}
					//alert('del');
					$.post("../../json/policy_data.jsp", $("#policy_frm").serialize() + "&method=del", function(resp) {
						var json = $.parseJSON(resp);
						alert(json.message);
						$("#tbl_policymatter").bootstrapTable("refresh");
					});
				});
			});
			$("#export").on("click", function() {
				window.open("../../json/export.jsp?type=policy_AL");
			});
			$("[rel=tooltip]").tooltip({
				delay : {
					show : 10,
					hide : 100
				}
			});
			$("#new-item").on("click", function() {
				var choose = $('#itemcode').val();
				if (choose == "MP") {
					$('#modal-form-input-1').show();
					$('#modal-form-input-2').show();
					$('#modal-form-input-1-title').text('USER ID');
					$('#data').attr('placeholder', 'Enter USER ID');
					
				} else if (choose == "ID") {
					$('#modal-form-input-1').show();
					$('#modal-form-input-2').hide();
					$('#modal-form-input-1-title').text('USER ID');
					$('#data').attr('placeholder', 'Enter Special USER ID');
					
				} else if (choose == "IP") {
					$('#modal-form-input-1').show();
					$('#modal-form-input-2').hide();
					$('#modal-form-input-1-title').text('PC IP');
					$('#data').attr('placeholder', 'Enter PC IP');
				}
				//alert(choose);
			});


			/* 검색 조건 추가 */
			$("#modal-new-item").on("show.bs.modal", function(){
				var 	itemcode = $('#itemcode').val(),
					input_data1 = $("#data"),
					data1_label = $("#data1_label"),
					btn_search = $("#btn-search"),
					modal_search = $(".modal_search");
					modal_search.attr("style","display:none");
					input_data1.val('');
				switch (itemcode) {
				case  "MP":
				case "ID" :
					input_data1.attr("placeholder","Enter User NO"); 
					data1_label.text("User No");
					btn_search.attr("style", "");
					modal_search.html('<legend>Search</legend><div id="modal-toolbar" class="form-inline" role="form"><label class="control-label">사번</label> </span> <input type="text" class="form-control input-sm" name="userno" style="width:120px;" /><label class="control-label">성명</label> </span> <input type="text" class="form-control input-sm"  name="usernm"  style="width:100px;"/><label class="control-label">팀명</label></span> <input type="text" class="form-control input-sm" name="deptnm"  style="width:100px;"/><button class="btn btn-default btn-sm" type="button" id="data-search"><i class="icon icon-search"></i></button></div></div><table id="modal_table"></table>');
					$("#modal_table").bootstrapTable(option2);
					break;
				case "IP":
					input_data1.attr("placeholder","Enter Connect IP");
					data1_label.text("Connect IP");
					btn_search.attr("style", "display:none;");
					modal_search.attr("style","display:none");
					break;
				default:
					input_data1.attr("placeholder","Enter User NO"); 
					data1_label.text("User No");
					btn_search.attr("style", "");
					modal_search.html('<legend>Search</legend><div id="modal-toolbar" class="form-inline" role="form"><label class="control-label">사번</label> </span> <input type="text" class="form-control input-sm" name="userno" style="width:120px;" /><label class="control-label">성명</label> </span> <input type="text" class="form-control input-sm"  name="usernm"  style="width:100px;"/><label class="control-label">팀명</label></span> <input type="text" class="form-control input-sm" name="deptnm"  style="width:100px;"/><button class="btn btn-default btn-sm" type="button" id="data-search"><i class="icon icon-search"></i></button></div></div><table id="modal_table"></table>');
					$("#modal_table").bootstrapTable(option2);
					break;
				}
				$("#modal-toolbar").on("click", "#data-search",function(){
					//$("#modal_table").bootstrapTable("load", getRows());
					getRows();
				});
			});
			/* 검색 조건 추가 // end */
			$("#btn-search").on("click", function(){
				$(".modal_search").attr("style","");
			});
		});
		var rowStyle = function() {
			return {
				classes: 'cursorhand'
			};
		};
		var getRows = function() {
			var param="itemcode=" + $('#itemcode').val() + "&userno=" + $('input[name=userno]').val() + "&usernm=" + $('input[name=usernm]').val() +"&deptnm=" + $('input[name=deptnm]').val()+"&deptcode=" + $('input[name=deptcode]').val();
			$.ajax({
				type: 'post',
				url: '../../json/policy_search.jsp',
				data: param,
				success: function(resp) {
					//return response;
					rows =  $.parseJSON(resp);
					$("#modal_table").bootstrapTable("load", rows);
				}
			});
		};
		
		$("#modal-btn-save").on("click", function() {
			var frm = $("#modal-form");
			//alert("모달창의 저장버튼 클릭 \n\n폼 데이터: " + $(frm).serialize() );
			var data = $.trim($("#data").val());
			if (data == "") {
				alert("입력 데이터가 없습니다.");
				$("#data").focus();
				$("#data").select();
				return;
			}
			
			var accessip = $.trim($("#accessip").val());
			if (accessip == "" && $('#itemcode').val() == "MP") {
				alert("입력 데이터가 없습니다.");
				$("#accessip").focus();
				$("#accessip").select();
				return;
			}
			
			$.post("../../json/policy_data.jsp", $(frm).serialize() + "&" + $("#policy_frm").serialize() + "&method=add", function(resp) {
				var json = $.parseJSON(resp);
				/*
				$("#deleteMessage").show();
				window.setTimeout(function() {
					$("#deleteMessage").hide();
				}, 2000);
				 */
				$("#data").val('');
				$("#comment").val('');
				$("#accessip").val('');
				alert(json.message);
				$("#tbl_policymatter").bootstrapTable("refresh");
			});
			$(this).parents("div.modal").modal("hide");
		});
	</script>
	
<jsp:include page="../inc/password_change.jsp" flush="true" />

</body>
</html>
