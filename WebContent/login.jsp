<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link type="text/css" rel="stylesheet" href="webpage/css/base.css" />
    <link type="text/css" rel="stylesheet" href="webpage/css/main.css" />

    <link href="webpage/js/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <script type="text/javascript" src="webpage/js/jquery-1.7.1.js"></script>
    
    <title>中粮集团档案管理系统</title>
</head>
<body>
<div id="container">
    <div id="main">
        <div class="login">
            <div class="lgn_main">
                <h3 style="border:none; padding:0px; padding-left:50px;">中粮集团档案管理系统</h3>
                <p class="top_line"><img src="webpage/css/images/login_top.jpg" width="913" height="33" /></p>
                <p class="left_pic"><img src="webpage/css/images/left_pic.jpg" width="257" height="286" /></p>
                <form action="login.action" method="post">
                    <div class="login_right">
                        <p><img src="webpage/css/images/logo.jpg" width="177" height="83" /></p>
                        <ul>
                            <li>
                                <label><span></span> 用户名：</label>
                                <input type="text" name="accountcode" id="accountcode" value = "wangf"/>
                                <strong class="" id="userpio_ico"></strong>
                            </li>
                            <li>
                                <label><span></span>密　码： </label>
                                <input type="password" name="password" id="password" value="password" />
                                <strong class="" id="passpio_ico"></strong>
                                <span class="prompt" id="passpio_msg"></span>
                            </li>
                            <%--<li>--%>
                                <%--<a href="#" style="width:60px;">忘记密码？</a><div class="login_msg "><input type="checkbox" class="cur" id="treaty" name="treaty" checked="checked" style="margin-left:10px; width:16px; height:16px; border:0; float:left;" value="1"> <span class="msg5">下次自动登录</span></div>--%>
                            <%--</li>--%>
                            <li>
                                <div class="butt1"><input type="submit" id="button" class="btn btn-success" value="登录" style="margin-left:100px;width: 220px;" /></div>
                            </li>
                        </ul>
                    </div>
                </form>

                <p style="float:left; margin-top:48px;"><img src="webpage/css/images/login_mid.jpg" width="1" height="241" /></p>
            </div>
        </div>
    </div>
</div>

</body>
</html>