<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.wession.Environment"%>
<%@page import="com.wession.model.ConfigAgent"%>
<%@page import="java.util.concurrent.ConcurrentHashMap"%>
<%@page import="java.util.Set"%>
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
	
	Environment env = Environment.getInstance();
	ConcurrentHashMap<String, ConfigAgent> agents = env.getAgentList();
	Set<String> setlic = env.getAgentListSet();
	String[] licenses = (String[]) setlic.toArray(new String[setlic.size()]);
	
	String SSO_I[] = {"-", "-", "-", "-"};
	String SSO_O[] = {"-", "-", "-", "-"};
	String SSO_IO[] = {"none", "none", "none", "none"};
	
	String BIZ_I[] = {"-", "-", "-", "-"};
	String BIZ_O[] = {"-", "-", "-", "-"};
	String BIZ_IO[] = {"none", "none", "none", "none"};
	
	String SALES_I[] = {"-", "-", "-", "-"};
	String SALES_O[] = {"-", "-", "-", "-"};
	String SALES_IO[] = {"none", "none", "none", "none"};
	
	for (int i = 0; i < agents.size(); i++) {
		ConfigAgent agent =  agents.get(licenses[i]);
		String appCode = agent.getAppName();
		String instance = agent.getAppInstance();
		
		//System.out.println(licenses[i] + " - " + appCode + " / " + instance);
		if (appCode.contains("SSO")) {
			if (instance.contains("101") || instance.contains("Adm") || instance.contains("INST01") || instance.contains("WSSVR01")) {
				SSO_I[0] = "-";
				SSO_O[0] = "-";
				SSO_IO[0] = "";
			} else if (instance.contains("102") || instance.contains("INST02")) {
				SSO_I[1] = "-";
				SSO_O[1] = "-";
				SSO_IO[1] = "";
			} else if (instance.contains("201")) {
				SSO_I[2] = "-";
				SSO_O[2] = "-";
				SSO_IO[2] = "";
			} else if (instance.contains("202")) {
				SSO_I[3] = "-";
				SSO_O[3] = "-";
				SSO_IO[3] = "";
			}
		} else if (appCode.contains("BIZ") || appCode.contains("DEMOAPP")) {
			if (instance.contains("101") || instance.contains("INST01") || instance.contains("WSSVR01") ) {
				BIZ_I[0] = "-";
				BIZ_O[0] = "-";
				BIZ_IO[0] = "";
			} else if (instance.contains("102") || instance.contains("INST02")) {
				BIZ_I[1] = "-";
				BIZ_O[1] = "-";
				BIZ_IO[1] = "";

			} else if (instance.contains("201")) {
				BIZ_I[2] = "-";
				BIZ_O[2] = "-";
				BIZ_IO[2] = "";

			} else if (instance.contains("202")) {
				BIZ_I[3] = "-";
				BIZ_O[3] = "-";
				BIZ_IO[3] = "";

			}
		} else if (appCode.contains("SALES")) {
			if (instance.contains("101") || instance.contains("INST01")) {
				SALES_I[0] = "-";
				SALES_O[0] = "-";
				SALES_IO[0] = "";
			} else if (instance.contains("102") || instance.contains("INST02")) {
				SALES_I[1] = "-";
				SALES_O[1] = "-";
				SALES_IO[1] = "";
			} else if (instance.contains("201")) {
				SALES_I[2] = "-";
				SALES_O[2] = "-";
				SALES_IO[2] = "";
			} else if (instance.contains("202")) {
				SALES_I[3] = "-";
				SALES_O[3] = "-";
				SALES_IO[3] = "";
			}
		}
	}
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
<link rel="stylesheet" type="text/css" href="../../dist/plugin/flot/flot.css" />
<style type="text/css">
.placeholder {
	width: 95%;
	height: 250px;
	margin: auto;
}

.labelFormatter {
	font-size: 8pt;
	text-align: center;
	padding: 2px;
	color: #fff;
}

div.legend td.legendLabel {
	padding-left: 5px !important;
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
					<li class="active"><a href="dashboard_view.jsp">dashboard</a></li>
					<li><a href="#">wession</a></li>
					<li><a href="#">policy</a></li>
					<li><a href="#">agent</a></li>
					<li><a href="#">manager</a></li>
				</ul>
				<jsp:include page="../inc/header_menu_right.jsp" flush="true" />
			</nav>
		</header>
		<div class="container-wrapper container" style="margin-top: 16px; width: 95%;">
			<div class="row">
				<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
					<div class="panel panel-default">
						<div class="panel-heading">Statistics</div>
						<div class="panel-body">
							<h3 style="margin-top:2px; text-align:center">Wession Count : <span id="wession_count"></span> </h3>
							<hr />
							<div style="text-align:center;font-size:18px;"><span id='currentTime'></span></div>
							<br /> 
							Wession 요청 건수 : <span id="wession_request_count">0</span> 건 <br /> __ 생성 건수 : <span id="wession_create_count">0</span> 건 <br /> __ 삭제 건수 : <span id="wession_delete_count">0</span> 건 <br />
						</div>
					</div>
				</div>
				<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
					<div class="panel panel-default">
						<div class="panel-heading">정책 수행 통계</div>
						<div class="panel-body">
							IP제어정책 수행 : <span id="policy_AA_count"></span> 건<br /> 
							IP제어정책 위반 : <span id="policy_AL_count"></span> 건<br /> 
							중복정책 수행 : <span id="policy_DA_count"></span> 건<br /> 
							중복정책 위반 : <span id="policy_DL_count"></span> 건<br />
							<hr/>
							중복세션삭제 : <span id="duplogin_count"></span> 건<br />
						</div>
					</div>
				</div>
				<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
					<div class="panel panel-default">
						<div class="panel-heading">Agent Monitoring</div>
						<div class="panel-body">
							<table class="table">
								<tr>
									<th width="20%">구분</th>
									<th width="20%">1</th>
									<th width="20%">2</th>
									<th width="20%">3</th>
									<th width="20%">4</th>
								</tr>
								<tr>
									<td>SSO</td>
									<%
									String agentType = "SSO";
									for (int i=0;i<4;i++) { %>
									<td style="font-size: 11px;">
										<div id="<%= agentType %>_IO_<%=i%>" style=";display:<%= SSO_IO[i]%>">
											<span class="glyphicon glyphicon-circle-arrow-up"></span><span id="<%= agentType %>_I_<%=i%>"><%=SSO_I[i]%></span>% 
											<br/>
											<span class="glyphicon glyphicon-circle-arrow-down"></span><span id="<%= agentType %>_O_<%=i%>"><%=SSO_O[i]%></span>%
										</div>
									</td>
									<% } %>
									</tr>
								<tr>
									<td>BIZ</td>
									<%
									agentType = "BIZ";
									for (int i=0;i<4;i++) { %>
									<td style="font-size: 11px">
										<div id="<%= agentType %>_IO_<%=i%>" style=";display:<%= BIZ_IO[i]%>">
											<span class="glyphicon glyphicon-circle-arrow-up"></span><span id="<%= agentType %>_I_<%=i%>"><%=BIZ_I[i]%></span>% 
											<br/>
											<span class="glyphicon glyphicon-circle-arrow-down"></span><span id="<%= agentType %>_O_<%=i%>"><%=BIZ_O[i]%></span>%
										</div>
									</td>
									<% } %>
								</tr>
								<tr>
									<td>SALES</td>
									<%
									agentType = "SALES";
									for (int i=0;i<4;i++) { %>
									<td style="font-size: 11px;">
										<div id="<%= agentType %>_IO_<%=i%>" style=";display:<%= SALES_IO[i]%>">
											<span class="glyphicon glyphicon-circle-arrow-up"></span><span id="<%= agentType %>_I_<%=i%>"><%=SALES_I[i]%></span>% 
											<br/>
											<span class="glyphicon glyphicon-circle-arrow-down"></span><span id="<%= agentType %>_O_<%=i%>"><%=SALES_O[i]%></span>%
										</div>
									</td>
									<% } %>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
					<div class="panel panel-default panel-primary">
						<div class="panel-heading">실시간 세션 정보</div>
						<div class="panel-body" style="min-height: 35em;">
							<table class="table table-striped" id="tbl_realtime">
							</table>
						</div>
					</div>
				</div>
				<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
					<div class="panel panel-default panel-primary">
						<div class="panel-heading">실시간 세션 차트</div>
						<div class="panel-body">
							<div id="realtime_wession" class="placeholder"></div>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-3 col-xs-12" style="min-width: 20em; max-width: 25em;">
					<div class="panel panel-default">
						<div class="panel-heading">Wession #1 Server</div>
						<div class="panel-body">
							<div id="pie_vm_01" class="placeholder"></div>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-3 col-xs-12" style="min-width: 20em; max-width: 25em;">
					<div class="panel panel-default">
						<div class="panel-heading">Wession #2 Server</div>
						<div class="panel-body">
							<div id="pie_vm_02" class="placeholder"></div>
						</div>
					</div>
				</div>
				<div class="pull-right" style="width: 0px;"></div>
			</div>
		</div>
		<!-- /container -->
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
						<p><%= copyright %></p>
					</div>
					<div class="col-xs-6">
						<ul class="list-inline pull-right">
							<li><a href="#" rel="tooltip" title="tooltip" data-placement="top"></a></li>
						</ul>
					</div>
				</div>
			</div>
		</footer>
		<script src="../../dist/js/jquery-1.11.0.js"></script>
		<script src="../../dist/js/jquery-ui-1.10.4.custom.js"></script>
		<script src="../../dist/js/jquery.number.min.js"></script>
		<script src="../../dist/js/bootstrap.js"></script>
		<script src="../../dist/plugin/wession/wession.js"></script>
		<script src="../../dist/plugin/bootstraptable/bootstrap-table.min.js"></script>
		<script src="../../dist/plugin/flot/jquery.flot.min.js"></script>
		<script src="../../dist/plugin/flot/jquery.flot.time.min.js"></script>
		<script src="../../dist/plugin/flot/jquery.flot.pie.min.js"></script>
		<script>
			$(function() {
				//webWorker();
				var $table = $("#tbl_realtime"), options = {
					url : '../../json/dashboard_tblrealtime.jsp',
					pagination : false,
					search : false,
					striped : true,
					pageSize : 10,
					idField : 'wsid',
					columns : [ {
						field : 'wsid',
						title : 'ID',
						visible : false
					}, {
						field : 'no',
						title : 'No'
					}, {
						field : 'userid',
						title : '사번',
					}, {
						field : 'name',
						title : '성명'
					}, {
						field : 'userip',
						title : '접속IP'
					}, {
						field : 'sso',
						title : '접속시간'
					} ],
					formatLoadingMessage : function() {
						return 'Loading ...';
					},
					formatNoMatches : function() {
						return 'Not found Records'
					}
				};
				$table.bootstrapTable(options);

				setInterval(function() {
					$.getJSON("../../json/dashboard_tblrealtime.jsp").done(function(data) {
						$table.bootstrapTable("load", data)
					})
				}, 1000);

				var jvm = [];
				// flot pie chart
				function getPieData() {
					var res = [];
					$.ajax({
						url : "../../json/monitor_server.jsp",
						async : false,
						success : function(resp) {
							jvm = $.parseJSON(resp);
							// 다른 span도 여기서 넣는다.
							$("#wession_count").text($.number(jvm.wession_count));
							$("#wession_request_count").text($.number(Number(jvm.wession_request_count_1) + Number(jvm.wession_request_count_2)));
							$("#wession_create_count").text($.number(Number(jvm.wession_create_count_1) + Number(jvm.wession_create_count_2)));
							$("#wession_delete_count").text($.number(Number(jvm.wession_delete_count_1) + Number(jvm.wession_delete_count_2)));
							$("#duplogin_count").text($.number(Number(jvm.duplogin_count_1) + Number(jvm.duplogin_count_2)));

							$("#policy_AA_count").text($.number(Number(jvm.policy_AA_count_1) + Number(jvm.policy_AA_count_2)));
							$("#policy_AL_count").text($.number(Number(jvm.policy_AL_count_1) + Number(jvm.policy_AL_count_2)));
							$("#policy_DA_count").text($.number(Number(jvm.policy_DA_count_1) + Number(jvm.policy_DA_count_2)));
							$("#policy_DL_count").text($.number(Number(jvm.policy_DL_count_1) + Number(jvm.policy_DL_count_2)));

						}
					});
					return jvm;
				}
				dataset = [], options = {
					series : {
						pie : {
							show : true,
							innerRadius : 0.3,
							radius : 0.8,
							label : {
								show : true,
								radius : 0.7,
								formatter : function(label, series) {
									//return "<div class='labelFormatter'>" + series.data + "KByte" + Math.round(series.percent) + "%</div>";
									return "<div class='labelFormatter'>" + ((series.data)[0])[1] + " MB</div>";
								},
								threshold : 0.1,
								background : {
									opacity : 0.5,
									color : "#000000"
								}
							}
						}
					},
					grid : {
						hoverable : false
					},
					legend : {
						show : true,
						noColumns : 1,
						position : "nw"
					}
				};
				getPieData();
				var $pie_plot1 = $.plot("#pie_vm_01", jvm.server1_jvm, options);
				var $pie_plot2 = $.plot("#pie_vm_02", jvm.server2_jvm, options);

				setInterval(function() {
					var jvm = getPieData();
					$pie_plot1.setData(jvm.server1_jvm);
					$pie_plot1.draw();

					$pie_plot2.setData(jvm.server2_jvm);
					$pie_plot2.draw();
				}, 1000);

				// real time
				var placeholder_03 = $("#realtime_wession");
				var maximum = placeholder_03.outerWidth() / 2 || 120; // 한번에 보여질 데이터 갯수

				var data03 = [];

				function getRandomData() {

					if (data03.length) {
						data03 = data03.slice(1);
					}

					while (data03.length < maximum) {
						var previous = data03.length ? data03[data03.length - 1] : 50;
						var y = previous + Math.random() * 10 - 5;
						data03.push(y < 0 ? 0 : y > 100 ? 100 : y);
					}

					// zip the generated y values with the x values

					var res = [];
					for (var i = 0; i < data03.length; ++i) {
						res.push([ i, data03[i] ]);
					}

					return res;
				}

				var data04 = [];
				var ws_chart = [];
				var step = 400;

				//  이 데이터가 표시데이터
				function getRandomData2() {
					if (data04.length) {
						data04 = data04.slice(1);
					}

					while (data04.length < maximum) {
						var previous = data04.length ? data04[data04.length - 1] : 0;
						var y = previous;
						data04.push(y < 0 ? 0 : y > 100 ? 100 : y);
					}
					data04.push(jvm.wession_count);

					var res = [];

					var d = new Date().getTime() - (data04.length - 1) * step;

					for (var i = 1; i < data04.length; ++i) {
						res.push([ d + (i * step), data04[i] ]);
					}

					//console.log(ws_chart.length + "/" + maximum);

					if (ws_chart.length < maximum - 1) {
						var d1 = new Date().getTime() - (maximum - 1) * step;
						for (var i = 1; i < maximum; ++i) {
							ws_chart.push([ d1 + (i * step), 0 ]);
						}
					}

					ws_chart.push([ new Date().getTime(), jvm.wession_count ]);
					ws_chart.shift();

					res = ws_chart;
					//console.log(data04.length);

					return res;
				}

				//

				series = [ {
					data : null,
					lines : {
						fill : true
					}
				}, {
					data : getRandomData2(),
					lines : {
						fill : true
					}
				} ];

				var $plot = $.plot(placeholder_03, series, {
					grid : {
						borderWidth : 1,
						minBorderMargin : 20,
						labelMargin : 10,
						backgroundColor : {
							colors : [ "#fff", "#e4f4f4" ]
						},
						margin : 20,
						markings : function(axes) {
							var markings = [];
							var xaxis = axes.xaxis;
							for (var x = Math.floor(xaxis.min); x < xaxis.max; x += xaxis.tickSize * 2) {
								markings.push({
									xaxis : {
										from : x,
										to : x + xaxis.tickSize
									},
									color : "rgba(232, 232, 255, 0.2)"
								});
							}
							return markings;
						}

					},
					xaxis : {
						mode : "time",
						timeformat : "%H:%M:%S",
						timezone : "browser",
						tickSize : [ step / 25, "second" ]

					},
					legend : {
						show : true
					}
				});

				setInterval(function updateRandom() {
					series[0].data = null;
					series[1].data = getRandomData2();
					$plot.setData(series);
					$plot.setupGrid();
					$plot.draw();
				}, step);
	
				clock();

			});
			
			function clock() {
				   var now = new Date();
				   var week = new Array('일', '월', '화', '수', '목', '금', '토');
				   var outDateString = now.getFullYear() + '/' + twolength((now.getMonth()+1)) + '/' + twolength(now.getDate());
				   var outDayString = week[now.getDay()] + '요일';
				   var outTimeString = twolength(now.getHours())+':'+twolength(now.getMinutes())+':'+twolength(now.getSeconds());
				   document.getElementById('currentTime').innerHTML=outDateString + " " + outDayString + ". " + outTimeString;
				   setTimeout('clock()',1000);
				}
			
			function twolength(n) {
				  return (n < 10 ? '0' : '') + n
			}
			
			$(function() {
				var m_agent = [];
				//매핑된 dashboard 데이터
				var mapping_agent=[{"":""}];
				setInterval(function() {
					$.ajax({
						url : "../../json/monitor_agent.jsp",
						async : false,
						success : function(resp) {
							m_agent = $.parseJSON(resp);
							// 다른 span도 여기서 넣는다.
							//alert(m_agent);
							//alert(m_agent.length);
							for (var i=0; i<m_agent.length; i++) {
								var appcode = "X";
								var instance = "X";
								var up = "X";
								var down = "X";
								
								appcode = m_agent[i].app_code;
								instance = m_agent[i].instance;
								up = m_agent[i].smart_que;
								down =  m_agent[i].smart_que2;
								
								console.log(appcode + "(" + instance+ ") : " + up + " : " + down);
								
								if ( appcode.indexOf("SSO") >= 0 ) {
									if ( instance.indexOf("101") > 0 || instance.indexOf("Adm") > 0 || instance.indexOf("INST01") > 0 || instance.indexOf("SVR01") > 0 ) {
										$("#SSO_I_0").html(up);
										$("#SSO_O_0").html(down);
									} else if ( instance.indexOf("102") > 0 || instance.indexOf("INST02") > 0 ) {
										$("#SSO_I_1").html(up);
										$("#SSO_O_1").html(down);
									} else if ( instance.indexOf("201") > 0 ) {
										$("#SSO_I_2").html(up);
										$("#SSO_O_2").html(down);
									} else if ( instance.indexOf("202") > 0 ) {
										$("#SSO_I_3").html(up);
										$("#SSO_O_3").html(down);
									} else {
										$("#SSO_I_0").html(up);
										$("#SSO_O_0").html(down);
									}	
								} else if ( appcode.indexOf("BIZ") >= 0 || appcode == "DEMOAPP") {
									if ( instance.indexOf("101") > 0 || instance.indexOf("INST01") > 0 || instance.indexOf("SVR01") > 0) {
										$("#BIZ_I_0").html(up);
										$("#BIZ_O_0").html(down);
									} else if ( instance.indexOf("102") > 0 || instance.indexOf("INST02") > 0 ) {
										$("#BIZ_I_1").html(up);
										$("#BIZ_O_1").html(down);
									} else if ( instance.indexOf("201") > 0 ) {
										$("#BIZ_I_2").html(up);
										$("#BIZ_O_2").html(down);
									} else if ( instance.indexOf("202") > 0 ) {
										$("#BIZ_I_3").html(up);
										$("#BIZ_O_3").html(down);
									} else {
										$("#BIZ_I_0").html(up);
										$("#BIZ_O_0").html(down);
									}	
								} else if ( appcode.indexOf("SALES") >= 0 ) {
									if ( instance.indexOf("101") > 0 ) {
										$("#SALES_I_0").html(up);
										$("#SALES_O_0").html(down);
									} else if ( instance.indexOf("102") > 0 ) {
										$("#SALES_I_1").html(up);
										$("#SALES_O_1").html(down);
									} else if ( instance.indexOf("201") > 0 ) {
										$("#SALES_I_2").html(up);
										$("#SALES_O_2").html(down);
									} else if ( instance.indexOf("202") > 0 ) {
										$("#SALES_I_3").html(up);
										$("#SALES_O_3").html(down);
									} else {
										$("#SALES_I_0").html(up);
										$("#SALES_O_0").html(down);
									}	
								}
							}
	
						}
					});
				}, 1000);
			});
			
		</script>
		
<jsp:include page="../inc/password_change.jsp" flush="true" />
	
</body>
</html>
