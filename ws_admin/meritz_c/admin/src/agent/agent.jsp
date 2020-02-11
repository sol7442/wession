<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	if (session.getAttribute("wessionadminid") == null) {
		response.sendRedirect("../../index.html");
		return;
	}
	String webTitle = "WESSION :: 메리츠화재";
	String adminUserName = (String) session.getAttribute("wessionadminname");
	String adminUserEmpNo = (String) session.getAttribute("wessionadminid");
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
					<a class="navbar-brand"> <i class="icon-th-large"></i> WESSION
					</a>
				</div>
				<ul class="nav navbar-nav">
					<li><a href="../dashboard/dashboard.jsp">dashboard</a></li>
					<li><a href="../wession/wession.jsp">wession</a></li>
					<li><a href="#">policy</a></li>
					<li class="active"><a href="../agent/agent.jsp">agent</a></li>
					<li><a href="../manager/manager.jsp">manager</a></li>
				</ul>
			<jsp:include page="../inc/header_menu_right.jsp" flush="true" />
			</nav>
		</header>
	  <div class="clearfix">
	    <aside class="sidebar">
	      <nav class="sidebar-nav">
	        <ul id="menu">
	          <li class="title">Agent</li>
		        <li class="active"><a href="agent.jsp"> Agent List </a></li>
	        </ul>
	        <div class="slider-toggle"></div>
	      </nav>
	    </aside>
	    <section class="content">
	      <ol class="breadcrumb">
	        <li><i class="icon-home"></i> Home</li>
	        <li>Agent</li>
	        <li>agent list</li>
	      </ol>
	      <div class="page">
	        <div class="page-header">
	          <div class="header-chart pull-right">
	            <ul>
	              <li></li>
	            </ul>
	          </div>
	          <h1>Agent List <small><i class="icon-double-angle-right"></i> </small></h1>
	        </div>
	        <div class="page-body" style="margin-top:-30px;">
	        	<div id="toolbar">
					<button class="btn color color-asbestos" type="button" name="btn_export" id="btn_export">
					 <i class="icon-download"></i> Export
					</button>
				</div>
	          <table id="wss_tbl" ></table>
	        </div>
	  <div>
	  	IO(UP) : Agent에서 Server로 보내는 통신(Agent Session의 생성, 삭제, 조회)의 성공비율(최근 100회 기준)<br/>
	  	IO(DOWN) : Server에서 Agent로 보내는 통신(Agent Session 삭제요청-중복로그인, 관리자 삭제)의 성공비율(최근 100회 기준)
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
	<script src="../../dist/plugin/metismenu/metismenu.min.js"></script>
	<script type="text/javascript">
		$(function() {
			var $table = $("#wss_tbl"), 
				options = {
				url: "../../json/agent_wsstbl.jsp?",
				striped : true,
				showRefresh : true,
				toolbar : "#toolbar",
				columns : [ {
					field : 'no',
					title : 'No'
				}, {
					field : 'system',
					title : '업무시스템'
				}, {
					field : 'host',
					title : 'Host'
				}, {
					field : 'serverip',
					title : 'IP'
				}, {
					field : 'instance',
					title : 'Instance'
				}, {
					field : 'port',
					title : 'Port'
				}, {
					field : 'agentsize',
					title : 'Agent Size',
					formatter: 'localeUnitFormatter'
				},{
					field : 'dummysize',
					title : 'Dummy Size',
					formatter: 'localeUnitFormatter'
				},{
					field : 'ioup',
					title : 'IO(UP)',
					formatter : 'pctFormatter'
				},{
					field : 'iodown',
					title : 'IO(DOWN)',
					formatter : 'pctFormatter'
				}, {
					field : 'license',
					title : 'LIcense'
				
                }, {
                    field: 'operate',
                    title: 'Send Msg',
                    align: 'center',
                    valign: 'middle',
                    clickToSelect: false,
                    formatter: operateFormatter,
                    events: operateEvents
                }
				],
				formatLoadingMessage : function() {
					return 'Loading ...';
				},
				formatNoMatches : function() {
					return 'Not found Records';
				}
			};
			$table.bootstrapTable(options);
			setInterval(function(){
				$.getJSON("../../json/agent_wsstbl.jsp").done(function(data){$table.bootstrapTable("load", data)});
			}, 1000);



			$("[rel=tooltip]").tooltip({});
		});
		$("#btn_export").on("click", function(e) {

			window.open("../../json/export.jsp?type=agent");
		});
		
		function pctFormatter(val) {
			return val + '%';
		}
		function localeUnitFormatter(val) {
			return val.toLocaleString().split(".")[0];
		}
		
		function operateFormatter(value, row, index) {
	        return [
	            '<a class="dummy" href="javascript:void(0)" title="Dummy Session Remove" style="margin-right:10px;">',
	                '<i class="glyphicon glyphicon-flash"></i>',
	            '</a>',
	            '<a class="all" href="javascript:void(0)" title="All Session Remove">',
                	'<i class="glyphicon glyphicon-trash"></i>',
            	'</a>'
	        ].join('');
	    }
		function sendAgent(license, type) {
			$.post("../../json/agent_control.jsp", "license="+license+"&type="+type, function(resp) {
				var json = $.parseJSON(resp);
				
				alert(json.message);
				$("#wss_tbl").bootstrapTable("refresh");
			});
		}

	    window.operateEvents = {
	        'click .all': function (e, value, row, index) {
	            //alert('You click like icon, row: ' + JSON.stringify(row));
	            console.log(row.license, 'all');
	            sendAgent(row.license, 'all');
	        },
	        'click .dummy': function (e, value, row, index) {
	            //alert('You click edit icon, row: ' + JSON.stringify(row));
	            console.log(row.license, 'dummy');
	            sendAgent(row.license, 'dummy');
	        }
	       
	    };

	</script> 
	
<jsp:include page="../inc/password_change.jsp" flush="true" />

</body>
</html>
