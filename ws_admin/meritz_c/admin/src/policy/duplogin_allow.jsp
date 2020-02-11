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

	DL policy = DL.getInstance();
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
					<a class="navbar-brand"> <i class="icon-th-large"></i> WESSION </a>
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
								<li class=""><a href="./authip_allow.jsp"><i class="menu-icon icon-caret-right "></i> 접속IP 허용정책</a></li>
								<li class=""><a href="./duplogin_adopt.jsp"><i class="menu-icon icon-caret-right "></i> 중복로그인 적용정책</a></li>
								<li class="active"><a href="./duplogin_allow.jsp"><i class="menu-icon icon-caret-right "></i> 중복로그인 허용정책</a></li>
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
					<li>중복로그인 허용정책</li>
				</ol>
				<div class="page">
					<div class="page-header">
						<div class="header-chart pull-right">
							<ul>
								<li></li>
							</ul>
						</div>
						<h1>
							<%= company_policy %> 인증정책 <small><i class="icon-double-angle-right"></i> 중복로그인 허용정책 </small> 
							<% // String data="12345\""; out.println(data.replaceAll("\"", "\\\\\"")); %>
						</h1>
					</div>
					<div class="alert alert-warning" style="margin-bottom: 0;">
						<div class="btn-group">
							<div class="form-inline">
								<div class="input-group">
									<select class="form-control " id="default-policy">
										<option value="AW" <%= default_policy.equals("allow")?"selected":"" %>>지정한 사용자를 적용하지 않음</option>
										<option value="DY" <%= default_policy.equals("deny")?"selected":"" %>>지정한 사용자를 적용함</option>
									</select> <span class="input-group-btn"> <a class="btn color color-asbestos " id="save-policy" title="정책실행" data-placement="top" data-toggle="tooltip" rel="tooltip"><i class="icon-signin icon-1x"></i></a>
									</span>
								</div>
							</div>
						</div>
					</div>
					<div class="page-body">
						<div id="toolbar">
							<div class="btn-group">
								<a class="btn btn-default" id="datatype" style="width: 90px; text-align: left;">사번</a> <a class="btn color color-asbestos dropdown-toggle" data-toggle="dropdown"> <i class="icon-angle-down"></i>
								</a>
								<ul class="dropdown-menu" role="menu" id="datatype-dropdown-menu">
									<li><a href="">사번</a></li>
									<li><a href="">부서코드</a></li>
									<li><a href="">접속IP</a></li>
									<li><a href="">역할코드</a></li>
								</ul>
							</div>
							<div class="btn-group">
								<a class="btn btn-default" id="new-item" data-toggle="modal" data-target="#modal-new-item" rel="tooltip" data-toggle="tooltip" data-placement="top" title="new item" data-container="body"><i class="icon-tag"></i> New </a>
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
						<form role="form" id="modal-form" class="form-horizontal">
						<!-- 추가 수정 -->
							<div class="form-group form-group">
								<label for="data" class="col-xs-2 control-label" id="data1_label">User No</label>
								<div class="col-xs-7">
									<input type="text" class="form-control input-sm" name="data" id="data"/>
								</div>
								<button type="button" class="btn btn-sm" id="btn-search">Search</button>
							</div>
							<div class="form-group form-group">
								<label for="userno" class="col-xs-2 control-label">Comment.</label>
								<div class="col-xs-10">
									<textarea class="form-control input-sm" id="comment" name="comment" placeholder="Enter Comment" rows="3" style="height: 76px"></textarea>
								</div>
							</div>
						<!-- // end -->
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
	<form name="policy_frm" id="policy_frm" method="post" action="../../json/policy_save.jsp" >
		<input type="hidden" id="policySerial" name="policySerial" />
		<input type="hidden" id="policycode" name="policycode" value="DL" />
		<input type="hidden" id="allowed" name="allowed" value="DY" />
		<input type="hidden" id="itemcode" name="itemcode" value="ID" />
	</form>
	<script src="../../dist/js/jquery-1.11.0.js"></script>
	<script src="../../dist/js/jquery-ui-1.10.4.custom.js"></script>
	<script src="../../dist/js/bootstrap.js"></script>
	<script src="../../dist/plugin/wession/wession.js"></script>
	<script src="../../dist/plugin/bootstraptable/bootstrap-table.min.js"></script>
	<script src="../../dist/plugin/metismenu/metismenu.min.js"></script>
	<script type="text/javascript">
		$(function() {
			var 	$table = $("#tbl_policymatter"), 
				$table1 = $("#modal_table"), // 추가 코드
				options = {
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
						//console.log(row.data)
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
					height: 350,
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
				}, // 추가
				option3 = {
					url :'../../json/policy_search.jsp',
					height: 400,
					sidePagination: 'client',
					pagenation : false,
					search: true,
					striped: true,
					clickToSelect: true,
					rowStyle: rowStyle,
					toolbar: '#modal-toolbar',
					columns: [
						{
							field: 'no',
							title: '번호'
						}, {
							field: 'pri-deptnm',
							title: '상위부서'
						}, {
							field: 'deptnm',
							title: '부서명 '
						}, {
							field: 'deptcode',
							title: '부서코드'
						}
					],
					onClickRow: function(row, tr) {
						if (row) {
							$("#data").val(row.deptcode);
						}
					}
				};
			// 추가 끝

			$("[rel=tooltip]").tooltip({
				delay : {
					show : 10,
					hide : 100
				}
			});
			$("#menu").metisMenu();
			$("#datatype-dropdown-menu").on("click", "a", function(e) {
				e.preventDefault();

				var holder = $(this).parents("div.btn-group").find("a:eq(0)");
				holder.text(this.outerText);
				$table.find("th:eq(2) div:first-child").text(this.outerText);
				if (this.outerText == "사번") {
					$('#itemcode').val("ID");
				} else if (this.outerText == "부서코드") {
					$('#itemcode').val("DV");
				} else if (this.outerText == "접속IP") {
					$('#itemcode').val("IP");
				} else if (this.outerText == "역할코드") {
					$('#itemcode').val("EM");
				}
				$table.bootstrapTable("refresh");
			});
			$("#launch").on("click", function() {
				//alert("Excute button called");
				if (confirm("접속IP적용 정책을 로딩 하시겠습니까?") == false)
				    return;

				$.post("../../json/policy_set.jsp", $("#policy_frm").serialize(), function(resp) {
					var json = $.parseJSON(resp);
					alert(json.message);
					$("#tbl_policymatter").bootstrapTable("refresh");
				});
			});
			$("#export").on("click", function() {
				window.open("../../json/export.jsp?type=policy_DL");
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
					$.post("../../json/policy_data.jsp", $("#policy_frm").serialize() + "&method=del", function(resp) {
						var json = $.parseJSON(resp);
						alert(json.message);
						$("#tbl_policymatter").bootstrapTable("refresh");
					});
				});
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
				case  "ID":
					input_data1.attr("placeholder","Enter User NO"); 
					data1_label.text("User No");
					btn_search.attr("style", "");
					modal_search.html('<legend>Search</legend><div id="modal-toolbar" class="form-inline" role="form"><label class="control-label">사번</label> </span> <input type="text" class="form-control input-sm" name="userno" style="width:120px;" /><label class="control-label">성명</label> </span> <input type="text" class="form-control input-sm"  name="usernm"  style="width:100px;"/><label class="control-label">팀명</label></span> <input type="text" class="form-control input-sm" name="deptnm"  style="width:100px;"/><button class="btn btn-default btn-sm" type="button" id="data-search"><i class="icon icon-search"></i></button></div></div><table id="modal_table"></table>');
					$("#modal_table").bootstrapTable(option2);
					break;
				case "DV":
					input_data1.attr("placeholder","Enter Dept Code");
					data1_label.text("Dept Code");
					btn_search.attr("style", "");
					modal_search.html('<legend>Search</legend><div id="modal-toolbar" class="form-inline" role="form"><label class="control-label">팀코드</label> </span> <input type="text" class="form-control input-sm" name="deptcode" style="width:120px;" /><label class="control-label">팀명</label> </span> <input type="text" class="form-control input-sm"  name="deptnm"  style="width:100px;"/><button class="btn btn-default btn-sm" type="button" id="data-search"><i class="icon icon-search"></i></button></div></div><table id="modal_table"></table>');
					$("#modal_table").bootstrapTable(option3);
					break;
				case "IP":
					input_data1.attr("placeholder","Enter Connect IP");
					data1_label.text("Connect IP");
					btn_search.attr("style", "display:none;");
					modal_search.attr("style","display:none");
					break;
				case "EM":
					input_data1.attr("placeholder","Enter Role Code");
					data1_label.text("Role Code");
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
					getRows();
					//$("#modal_table").bootstrapTable("load", getRows());
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
			var rows = [];
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
			
			var data = $.trim($("#data").val());
			if (data == "") {
				alert("입력 데이터가 없습니다.");
				$("#data").focus();
				$("#data").select();
				return;
			}
			
			//alert("모달창의 저장버튼 클릭 \n\n폼 데이터: " + $(frm).serialize() );
			$.post("../../json/policy_data.jsp", $(frm).serialize() + "&" + $("#policy_frm").serialize() + "&method=add", function(resp) {
				var json = $.parseJSON(resp);
				$("#data").val('');
				$("#comment").val('');
				alert(json.message);
				$("#tbl_policymatter").bootstrapTable("refresh");
			});
			$(this).parents("div.modal").modal("hide");
		});
		$("#save-policy").on("click", function() {
			//e.preventDefault();
			var frm = $("#modal-form");
			$("#allowed").val($("#default-policy").val());
			$.post("../../json/policy_data.jsp", $(frm).serialize() + "&" + $("#policy_frm").serialize() + "&method=changedefault", function(resp) {
				var json = $.parseJSON(resp);
				$("#data").val('');
				$("#comment").val('');
				alert(json.message + "\n" + "기본 정책 변경 후에는 반드시 Execute를 실행하여 정책 반영을 하여야 합니다.");
				$("#tbl_policymatter").bootstrapTable("refresh");
			});
		});
	</script>
	
<jsp:include page="../inc/password_change.jsp" flush="true" />

</body>
</html>
