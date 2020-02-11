<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.wession.*"%>
<%@page import="com.wession.policy.*"%>
<%@page import="com.wession.policy.meritz.*"%>
<%@ page import="org.slf4j.Logger"%>
<%@ page import="org.slf4j.LoggerFactory"%>
<%!
	public String matter(Matter m, String idx, String part) {
		String str = "";
		String allow = "-";
		String except = "-";
		HashMap <String, Vector> hm = m.getWhite();
		Vector <String> v = hm.get(idx);
		if (v == null) {
			str = "-";
		} else {
			allow = "deny";
			for (int i=0; i<v.size(); i++) {
				if ((v.get(i)).contains("allow")) {
					allow = "allow";
				}
				if ((v.get(i)).contains("Exception")) {
					except = "yes";
				}
			}
		}
		return (part.equals("allow"))?allow:except;
	}
%>
<%
	if (session.getAttribute("wessionadminid") == null) {
		response.sendRedirect("../../index.html");
		return;
	}
	String webTitle = "WESSION :: 메리츠화재";
	String company_policy = "메리츠화재 ";
	String copyright = "Copyright &copy; 2014 Wession .All rights reserved.";
	
	String adminUserName = (String) session.getAttribute("wessionadminname");
	String adminUserEmpNo = (String) session.getAttribute("wessionadminid");
	
	boolean isAdmin = true;
	
	Logger policyLog = LoggerFactory.getLogger("policy");
		
	String userno = request.getParameter("userno")==null?"":request.getParameter("userno");
	String deptcode = request.getParameter("deptcode")==null?"":request.getParameter("deptcode");
	String connectip = request.getParameter("connectip")==null?"":request.getParameter("connectip");
	String rolecode = request.getParameter("rolecode")==null?"":request.getParameter("rolecode");
			
	AA policyAccessAdopt = AA.getInstance();
	AL policyAccessAllow = AL.getInstance();
	DA policyDuploginAdopt = DA.getInstance();
	DL policyDuploginAllow = DL.getInstance();
	
	policyAccessAdopt.setLogger(policyLog);
	policyAccessAllow.setLogger(policyLog);
	policyDuploginAdopt.setLogger(policyLog);
	policyDuploginAllow.setLogger(policyLog);
	
	Matter m = new Matter();
	m.setItem(Const.MTO_User_ID, userno);
	m.setItem(Const.MTO_Division_Code, deptcode);
	m.setItem(Const.MTO_Acess_IP, connectip);
	m.setItem(Const.MTO_IDIP_Mapping, userno+";"+connectip);
	
	Vector <String> ve = new Vector <String>();
	if (rolecode.contains(";")) {
		String [] rolecodes = rolecode.split(";");
		for (int i=0; i<rolecodes.length; i++) {
			ve.add(rolecodes[i]);
		}
	} else {
		ve.add(rolecode);
	}
	m.setItem(Const.MTO_EAM_Code, ve);
	
	Matter m1 = m.clone();
	Matter m2 = m.clone();
	Matter m3 = m.clone();
	Matter m4 = m.clone();
	
	if (!"".equals(userno) || !"".equals(deptcode) || !"".equals(connectip) || !"".equals(rolecode)) {
	
	policyAccessAdopt.carryOut(m1);
	policyAccessAllow.carryOut(m2);
	policyDuploginAdopt.carryOut(m3);
	policyDuploginAllow.carryOut(m4);

	}
	
	/*
	System.out.println("=========================");
	System.out.println("userid : " + m3.getItem("userid"));
	System.out.println("eamCode : " + m3.getItem("eamCode"));
	System.out.println("divCode : " + m3.getItem("divCode"));
	System.out.println("accessIP : " + m3.getItem("accessIP"));
	System.out.println("mapping : " + m3.getItem("mapping"));
	System.out.println(m3.getReport());
	System.out.println(m3.getWhite().toString());
	System.out.println("=========================");
	*/
	
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
						<li class=""><a href="./duploginlist.jsp"> 중복로그인 현황</a></li>
						<li class="active"><a href="./simulator.jsp"> Simulator</a></li>
					</ul>
					<div class="slider-toggle"></div>
				</nav>
			</aside>
			<section class="content">
				<ol class="breadcrumb">
					<li><i class="icon-home"></i> Home</li>
					<li>Policy Manager</li>
					<li>Simulator</li>
				</ol>
				<div class="page">
					<div class="page-header">
						<div class="header-chart pull-right">
							<ul>
								<li></li>
							</ul>
						</div>
						<h1>
							Simulator <small><i class="icon-double-angle-right"></i> </small>
						</h1>
					</div>
					<div class="page-body">
						<div id="toolbar" style="padding-top: 10px; padding-bottom: 10px;">
							<form id="verification-form" class="form-inline" method="post" action="./simulator.jsp">
								<div class="form-group">
									<input type="text" class="form-control" id="userno" name="userno" placeholder="Enter UserNO" data-toggle="tooltip" data-placement="top" rel="tooltip" title="Enter User NO" value="<%= userno %>" />
								</div>
								<div class="form-group">
									<input type="text" class="form-control" id="deptcode" name="deptcode" placeholder="Enter Dept Code" data-toggle="tooltip" data-placement="top" rel="tooltip" title="Enter Department Code" value="<%= deptcode %>" />
								</div>
								<div class="form-group">
									<input type="text" class="form-control" id="connectip" name="connectip" placeholder="Enter Connect IP" data-toggle="tooltip" data-placement="top" rel="tooltip" title="Enter Connect IP" value="<%= connectip %>" />
								</div>
								<div class="form-group">
									<input type="text" class="form-control" id="rolecode" name="rolecode" placeholder="Enter Role Code" data-toggle="tooltip" data-placement="top" rel="tooltip" title="Enter Role Code" value="<%= rolecode %>" />
								</div>
								<button class="btn color color-asbestos" type="button" name="btn_verification" id="btn_verification">
									<i class="icon-ok-sign"></i> 검증하기
								</button>
							</form>
						</div>
						<table class="table table-bordered">
							<thead>
								<tr>
									<th colspan='3' >
										<div class="th-inner"></div>
										<div class="fht-cell"></div>
									</th>
									<th >
										<div class="th-inner">기본정책</div>
										<div class="fht-cell"></div>
									</th>
									<th >
										<div class="th-inner">예외정책</div>
										<div class="fht-cell"></div>
									</th>
									<th >
										<div class="th-inner">결과</div>
										<div class="fht-cell"></div>
									</th>
								</tr>
							</thead>
							<tbody>
								<tr class="alert-success hd-wid-5">
									<td colspan='3'><strong><i class="icon-quote-left icon-border"></i> 테스트결과</strong></td>
									<td><strong>-</strong></td>
									<td><strong>-</strong></td>
									<td><strong>-</strong></td>
								</tr>
								<tr>
									<td rowspan='9'>접속허용IP</td>
									<td rowspan='5'>적용정책</td>
									<td class="alert-warning"><strong>적용결과</strong></td>
									<td class="alert-warning"><strong><%= policyAccessAdopt.getDefaultPolicy() %></strong></td>
									<td class="alert-warning"><strong>-</strong></td>
									<td class="alert-warning"><strong><%= m1.getReport() %></strong></td>
								</tr>
								<tr>
									<td>사용자</td>
									<td>-</td>
									<td><%= matter(m1, "userid", "exception") %></td>
									<td><%= matter(m1, "userid", "allow") %></td>
								</tr>
								<tr>
									<td>부서코드</td>
									<td>-</td>
									<td><%= matter(m1, "divCode", "exception") %></td>
									<td><%= matter(m1, "divCode", "allow") %></td>
								</tr>
								<tr>
									<td>접속IP</td>
									<td>-</td>
									<td><%= matter(m1, "accessIP", "exception") %></td>
									<td><%= matter(m1, "accessIP", "allow") %></td>
								</tr>
								<tr>
									<td>역할코드</td>
									<td>-</td>
									<td><%= matter(m1, "eamCode", "exception") %></td>
									<td><%= matter(m1, "eamCode", "allow") %></td>
								</tr>
								<tr>
									<td rowspan='4'>허용정책</td>
									<td class="alert-info"><strong>적용결과 (OR 연산)</strong></td>
									<td class="alert-warning"><strong><%= policyAccessAllow.getDefaultPolicy() %></strong></td>
									<td class="alert-warning"><strong>-</strong></td>
									<td class="alert-warning"><strong><%= m2.getReport() %></strong></td>
								</tr>
								<tr>
									<td>ID-IP 매핑</td>
									<td>-</td>
									<td><%= matter(m2, "mapping", "exception") %></td>
									<td><%= matter(m2, "mapping", "allow") %></td>
								</tr>
								<tr>
									<td>공용PC</td>
									<td>-</td>
									<td><%= matter(m2, "accessIP", "exception") %></td>
									<td><%= matter(m2, "accessIP", "allow") %></td>
								</tr>
								<tr>
									<td>특수사용자</td>
									<td>-</td>
									<td><%= matter(m2, "userid", "exception") %></td>
									<td><%= matter(m2, "userid", "allow") %></td>
								</tr>
								<tr>
									<td rowspan='10'>중복로그인</td>
									<td rowspan='5'>적용정책</td>
									<td class="alert-warning"><strong>적용결과</strong></td>
									<td class="alert-warning"><strong><%= policyDuploginAdopt.getDefaultPolicy() %></strong></td>
									<td class="alert-warning"><strong>-</strong></td>
									<td class="alert-warning"><strong><%= m3.getReport() %></strong></td>
								</tr>
								<tr>
									<td>사용자</td>
									<td>-</td>
									<td><%= matter(m3, "userid", "exception") %></td>
									<td><%= matter(m3, "userid", "allow") %></td>
								</tr>
								<tr>
									<td>부서코드</td>
									<td>-</td>
									<td><%= matter(m3, "divCode", "exception") %></td>
									<td><%= matter(m3, "divCode", "allow") %></td>
								</tr>
								<tr>
									<td>접속IP</td>
									<td>-</td>
									<td><%= matter(m3, "accessIP", "exception") %></td>
									<td><%= matter(m3, "accessIP", "allow") %></td>
								</tr>
								<tr>
									<td>역할코드</td>
									<td>-</td>
									<td><%= matter(m3, "eamCode", "exception") %></td>
									<td><%= matter(m3, "eamCode", "allow") %></td>
								</tr>
								<tr>
									<td rowspan='5'>허용정책</td>
									<td class="alert-warning"><strong>적용결과</strong></td>
									<td class="alert-warning"><strong><%= policyDuploginAllow.getDefaultPolicy() %></strong></td>
									<td class="alert-warning"><strong>-</strong></td>
									<td class="alert-warning"><strong><%= m4.getReport() %></strong></td>
								</tr>
								<tr>
									<td>사용자</td>
									<td>-</td>
									<td><%= matter(m4, "userid", "exception") %></td>
									<td><%= matter(m4, "userid", "allow") %></td>
								</tr>
								<tr>
									<td>부서코드</td>
									<td>-</td>
									<td><%= matter(m4, "divCode", "exception") %></td>
									<td><%= matter(m4, "divCode", "allow") %></td>
								</tr>
								<tr>
									<td>접속IP</td>
									<td>-</td>
									<td><%= matter(m4, "accessIP", "exception") %></td>
									<td><%= matter(m4, "accessIP", "allow") %></td>
								</tr>
								<tr>
									<td>역할코드</td>
									<td>-</td>
									<td><%= matter(m4, "eamCode", "exception") %></td>
									<td><%= matter(m4, "eamCode", "allow") %></td>
								</tr>
							</tbody>
						</table>
					</div>
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
	
	<form name="policy_frm" id="policy_frm" method="post" action="../../json/policy_save.jsp" >
		<input type="hidden" id="policySerial" name="policySerial" />
		<input type="hidden" id="policycode" name="policycode" value="DA" />
		<input type="hidden" id="allowed" name="allowed" value="DY" />
		<input type="hidden" id="itemcode" name="itemcode" value="ID" />
	</form>
	<script src="../../dist/js/jquery-1.11.0.js"></script>
	<script src="../../dist/js/jquery-ui-1.10.4.custom.js"></script>
	<script src="../../dist/js/bootstrap.js"></script>
	<script src="../../dist/plugin/wession/wession.js"></script>
	<script src="../../dist/plugin/metismenu/metismenu.min.js"></script>
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
