<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
				<ul class="nav navbar-nav navbar-right red" style="margin-right:10px;">
				<!-- 
					<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#"> <i class="icon-envelope"></i> <i class="caret"></i>
					</a></li>
				 -->
					<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#"> <i class="icon-user"></i> 
					<%=session.getAttribute("wessionadminname")%>(<%=session.getAttribute("wessionadminid")%>) 
					<i class="caret"></i>
					</a> <!-- user dropdown menu -->
						<ul class="dropdown-menu">
						<!-- 
							<li><a href="#"> <i class="icon-user"></i> profile
							</a></li>
							 -->
							<li><a href="#" data-toggle="modal" data-target="#modalPassword"  id="passwd_mod"> <i class="icon-cog"></i> password
							</a></li>
						
							<li class="divider"></li>
							<li><a href="../../logout.jsp"> <i class="icon-off"></i> logout
							</a></li>
						</ul></li>
				</ul>
				
