<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="webpage/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="webpage/js/bootstrap/css/bootstrap.css" />
    <link rel="stylesheet" type="text/css" href="webpage/js/bootstrap/css/bootstrap-responsive.min.css" />
    <link rel="stylesheet" href="webpage/js/login/css/typica-login.css">
    <script type="text/javascript" src="webpage/js/jquery-1.7.1.js"></script>
    <script type="text/javascript" src="webpage/js/login/js/backstretch.min.js"></script>
    <script type="text/javascript" src="webpage/js/login/js/typica-login.js"></script>
    <script type="text/javascript" src="webpage/js/bootstrap/js/bootstrap.js"></script>
    <title>中粮集团综合档案管理系统</title>
    <!--
    <script language="javascript">
        $(function() {
            $.ajaxSetup({
                contentType : "application/x-www-form-urlencoded;charset=utf-8",
                timeout : pageTimeout,
                cache : false,
                complete : function(XHR, TS) {
                    alert("dd");
                    var resText = XHR.responseText;
                    if (resText == "{sessionState:0}") {
                        var nav = judgeNavigator();
                        if (nav.indexOf("IE:6") > -1) {
                            window.opener = null;
                            window.close();
                            window.open(jsContextPath + '/login.jsp', '');
                        } else {
                            window.open(jsContextPath + '/login.jsp', '_top');
                        }
                    }
                }
            });
        })
    </script>
     -->
</head>
<body>
<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <a class="brand" href="login.jsp"><img src="webpage/js/login/logo.png" alt=""></a>
        </div>
    </div>
</div>

<div class="container">
    <div id="login-wraper">
        <form class="form login-form" action="login.action" method="post">
            <legend>登录到 <span class="blue">中粮集团综合档案管理平台</span></legend>
            <div class="body">
                <label>帐户名</label>
                <input type="text" id="accountcode" name="accountcode" value="admin">

                <label>密码</label>
                <input type="password" id="password" name="password" value="password">
                <div>${requestScope.str }</div>
            </div>

            <div class="footer">
                <%--<label class="checkbox inline">--%>
                    <%--<input type="checkbox" id="inlineCheckbox1" value="option1"> 记住我--%>
                <%--</label>--%>
                <button type="submit" class="btn btn-success">Login</button>
            </div>
        </form>
    </div>
</div>

<footer class="white navbar-fixed-bottom">
    中粮集团版权所有 2013-2018
    <%--Don't have an account yet? <a href="register.html" class="btn btn-black">Register</a>--%>
</footer>

<%--<form action="login.action" method="post">--%>
    <%--用户名：<input type="text" id="accountcode" name="accountcode"--%>
               <%--value="admin"/> 密码:<input type="password" id="password"--%>
                                         <%--name="password" value="password"/> <input type="submit"--%>
                                                                                   <%--value="submit"/>--%>
<%--</form>--%>
<%--<br> ${requestScope.str }--%>
</body>
</html>