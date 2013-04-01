<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="webpage/css/style.css"/>
    <script type="text/javascript" src="webpage/js/jquery-1.7.1.js"></script>
    
<link href="webpage/js/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link href="webpage/js/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet">
<link href="webpage/js/bootstrap/css/font-awesome.css" rel="stylesheet">
<link href="webpage/js/bootstrap/css/adminia.css" rel="stylesheet"> 
<link href="webpage/js/bootstrap/css/adminia-responsive.css" rel="stylesheet"> 
<link href="webpage/js/bootstrap/css/pages/login.css" rel="stylesheet"> 

    <title>XXX案管理系统</title>
</head>
<body style="background:url(webpage/css/images/index.jpg) no-repeat center; no-repeat;)">
	<div class="navbar navbar-fixed-top">
	
	<div class="navbar-inner">
		
		<div class="container">
			
			<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"> 
				<span class="icon-bar"></span> 
				<span class="icon-bar"></span> 
				<span class="icon-bar"></span> 				
			</a>
			
			<a class="brand" href="#">xxx档案管理系统</a>
			<!-- 
			<div class="nav-collapse">
			
				<ul class="nav pull-right">
					
					<li class="">
						
						<a href="javascript:;"><i class="icon-chevron-left"></i> Back to Homepage</a>
					</li>
				</ul>
				
			</div>--> <!-- /nav-collapse -->
			 
		</div> <!-- /container -->
		
	</div> <!-- /navbar-inner -->
	
</div> <!-- /navbar -->

<div id="login-container">
	<div id="login-header">
		<h3>登 录</h3>
	</div> <!-- /login-header -->
	
	<div id="login-content" class="clearfix">
		<form action="login.action" method="post">
			<fieldset>
				<div class="control-group">
					<label class="control-label" for="username">用户名</label>
					<div class="controls">
						<input type="text" id="accountcode" name="accountcode" value="admin">
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="password">密 码</label>
					<div class="controls">
						<input type="password" id="password" name="password" value="password">
					</div>
				</div>
			</fieldset>
			
			<div class="pull-right">
				<button type="submit" class="btn btn-warning btn-large">
					登录
				</button>
			</div>
		</form>
	</div> <!-- /login-content -->
	<div style="margin-top: 30px;">
		温馨提示：本系统用于浏览器IE8以上版本...
	</div>
</div> <!-- /login-wrapper -->

<script src="js/bootstrap.js"></script>
	
</body>
</html>