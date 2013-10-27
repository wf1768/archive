<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="webpage/css/style.css"/>
<link rel="stylesheet" type="text/css" href="webpage/images/images.css"/>
<link rel="stylesheet" type="text/css" href="webpage/js/jquery.layout/layout-default-latest.css"/>


<script type="text/javascript" src="webpage/js/jquery-1.7.1.js"></script>

<link href="webpage/js/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link href="webpage/js/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet">
<%--<script type="text/javascript" src="webpage/js/bootstrap/js/bootstrap.js"></script>--%>
<script type="text/javascript" src="webpage/js/bootstrap/js/bootbox.min.js"></script>
<link href="webpage/js/bootstrap/css/font-awesome.css" rel="stylesheet">
<link href="webpage/js/bootstrap/css/adminia.css" rel="stylesheet">
<link href="webpage/js/bootstrap/css/adminia-responsive.css" rel="stylesheet">

<link rel="stylesheet" type="text/css" href="webpage/js/jquery-ui/css/custom-theme/jquery-ui-1.8.16.custom.css"/>
<script type="text/javascript" src="webpage/js/jquery-ui/jquery-ui-1.8.16.custom.min.js"></script>

<script type="text/javascript" src="webpage/js/jquery.layout/jquery.layout-latest.js"></script>
<script type="text/javascript" src="webpage/js/us.archive.util.js"></script>



<style type="text/css">
    .ifr {
        left: 0;
        margin: 0;
        overflow: auto;
        padding: 0;
        position: absolute;
        right: 0;
        top: 40px;
        z-index: 1;
    }
    .navbar-inner {
    	background-color: #23538E;
	    background: none repeat scroll 0 0 #23538E;
	    /* background-color: #2C2C2C;
	    background-image: -moz-linear-gradient(center top , #333333, #222222); */
	    background-repeat: repeat-x;
	    border-radius: 4px 4px 4px 4px;
	    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.25), 0 -1px 0 rgba(0, 0, 0, 0.1) inset;
	    padding-left: 20px;
	    padding-right: 20px;
	    filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#23538E', endColorstr='#23538E', GradientType=0);
	}
	.navbar .nav > li > a {
	    color: #FFFFFF;
	    float: none;
	    line-height: 19px;
	    padding: 10px 10px 11px;
	    text-decoration: none;
	    text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.25);
	}
	.navbar .divider-vertical {
	    background-color: #23538E;
	    border-right: 1px solid #215A95;
	    height: 40px;
	    margin: 0 9px;
	    overflow: hidden;
	    width: 1px;
	}
</style>
<script language="javascript">
	function setit(){
		if(document.all){
			document.getElementById("content").attachEvent("onfocus",dothis);
		}else{
		 	document.getElementById("content").contentWindow.addEventListener("focus",dothis,false);
		}
	}
	function dothis(){
		$("li").removeClass("open");
	}
	
    $(function () {
        //$("#ifr").focus(function(){
          //  $("li").removeClass("open");
        //});
        
        $("#dialog-form").dialog({
            autoOpen: false,
            height: 360,
            width: 480,
            modal: true,
            resizable: false,
            buttons: {
                "提交": function () {
                    var oldpass = $("#oldpassword");
                    var newpass = $("#newpassword");
                    var confirmpass = $("#confirmpassword");
                    if (oldpass.val() == "") {
                        $("#oldpassdiv").addClass("error");
                        $("#oldpasserror").html("请输入原密码。");
                        return;
                    }
                    if (newpass.val() == "") {
                        $("#newpassdiv").addClass("error");
                        $("#newpasserror").html("请输入新密码。");
                        return;
                    }
                    if (confirmpass.val() == "") {
                        $("#confirmpassdiv").addClass("error");
                        $("#confirmpasserror").html("请输入重复密码。");
                        return;
                    }

                    if (newpass.val() != confirmpass.val()) {
                        $("#newpassdiv").addClass("error");
                        $("#newpasserror").html("新密码2次输入不一致。请重新输入。");
                        return;
                    }

                    var par = {
                        oldpassword: "",
                        newpassword: ""
                    };
                    par.oldpassword = oldpass.val();
                    par.newpassword = newpass.val();
                    var aa = "par=" + JSON.stringify(par);
                    $.post("updatePass.action", aa, function (data) {
                                us.openalert(data, "系统提示", "alertbody alert_Information");
                            }
                    );
                },
                "关闭": function () {
                    $(this).dialog("close");
                }
            },
            close: function () {

            }
        });

        $.ajax({
            async: false,
            url: "menu.action",
            type: 'post',
            dataType: 'script',
            success: function (data) {
                if (data != "error") {
                    $("#sysname").html(sysname); //系统名称
                    document.title = sysname;	 //title
                    $("#account_code").val(account);
                    var funList = eval(functionList);
                    var funStr = "<li><a href=\"#\" onclick=\"javascript:$(window.parent.document).find('#content').attr('src','index.html')\">首页</a></li>";
                    for (var i = 0; i < funList.length; i++) {
                        var fun = funList[i];
                        if (fun.funparent == 0) {
                            //先得到是否有子节点
                            var childFun = "";
                            var tmp = "";
                            for (var j = 0; j < funList.length; j++) {
                                child = funList[j];
                                if (child.funparent == fun.functionid) {
                                    tmp += "<li><a href=\"javascript:void(0)\" onclick=\"javascript:$(window.parent.document).find('#content').attr('src','"
                                            + child.funpath
                                            + "')\">"
                                            + child.funchinesename
                                            + "</a></li>";
                                }
                            }
                            if (tmp != "") {
                                childFun = "<ul class=\"dropdown-menu\">";
                                childFun += tmp + "</ul>";
                            }

                            if (childFun != "") {
                                funStr += "<li class=\"divider-vertical\"></li><li class=\"dropdown\"><a href=\"javascript:void(0)\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">"
                                        + fun.funchinesename
                                        + " <b class=\"caret\"></b></a>";
                                funStr += childFun + "</li>";
                            } else {
                                funStr += "<li><a href=\"javascript:void(0)\" onclick=\"javascript:$(window.parent.document).find('#content').attr('src','"
                                        + child.funpath
                                        + "')\">"
                                        + fun.funchinesename + "</a>";
                                funStr += "</li>";
                            }
                        }
                    }
                    funStr += "<li><a href=\"#\" onclick=\"javascript:$(window.parent.document).find('#content').attr('src','help/index.html')\">帮助</a></li>";
                    funStr += "<li class=\"divider-vertical\"></li><li class=\"dropdown\">";
                    funStr += "<a href=\"account.html#\" class=\"dropdown-toggle \" data-toggle=\"dropdown\">";
                    funStr += account+" <b class=\"caret\"></b></a>";
                    funStr += "<ul class=\"dropdown-menu\"><li><a href=\"#\" onClick=\"openAccountInfo();\"><i class=\"icon-user\"></i> 账户信息  </a></li>";
                    funStr += "<li class=\"divider\"></li><li><a href=\"#\" onclick=\"quit()\"><i class=\"icon-off\"></i> 退 出</a></li></ul></li>";
                    $("#fun").html(funStr);

                } else {
                    us.openalert(
                            '<span style="color:red">读取功能信息时出错.</span></br>请关闭浏览器，重新登录尝试或与管理员联系!',
                            '系统提示',
                            'alertbody alert_Information');
                }
            }
        });
        //创建页面布局
        $('body').layout({
            applyDefaultStyles: true,
            zIndexes: {
                pane_normal: 3
            },
            panes: {
                cssDemo: {
                    padding: "0px"
                }
            },
            north: {
                size: "40",
                spacing_open: 0,
                closable: false,
                resizable: true
            },
            south: {
                size: "28",
                spacing_open: 0,
                closable: false,
                resizable: true
            }
        });
        //解决菜单被遮挡的问题
        $("#content").css("z-index", "1");
        //解决iframe内pading过大的问题
        //$("#ifr").css("padding","0");
        //$("#ifr").addClass("ifr");
    });
    function quit() {
        bootbox.confirm("真的要退出系统吗?", function(result) {
            if(result){
                window.location.href = "webpage/common/logout.jsp";
            }
        })
//        us.openconfirm("真的要退出系统吗?", "系统提示", function () {
//            window.location.href = "webpage/common/logout.jsp";
//        });
    }
    function openAccountInfo() {
        $("#oldpassword").val("");
        $("#newpassword").val("");
        $("#confirmpassword").val("");
        $("#dialog-form").dialog("open");
    }
    function ifrHeight() {
        var h = pageHeight();
        $("#content").height(
                h - $("#desktopFooter").height() - $("#menu").height() - 6);
    }
    function pageHeight() {
        if ($.browser.msie) {
            return document.compatMode == "CSS1Compat" ? document.documentElement.clientHeight
                    : document.body.clientHeight;
        } else {
            return self.innerHeight;
        }
    };

    function updatePassMouseOut(who) {
        if ($("#" + who + "password").val() != "") {
            $("#" + who + "passdiv").removeClass("error");
            $("#" + who + "passerror").html("");
        }
    }
</script>
<title></title>
</head>
<body>
<div><input type="hidden" id="account_code" value="" /></div>
<div class="navbar navbar-fixed-top">
    <div id="menu" class="navbar-inner">
        <div class="container">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"> 
				<span class="icon-bar"></span> 
				<span class="icon-bar"></span> 
				<span class="icon-bar"></span> 				
			</a>
			
			<a id="sysname" class="brand" href="javascript:;">Adminia Admin</a>
			<div class="nav-collapse">    
                <ul class="nav pull-right" id="fun">
                </ul>
             </div>   
        </div>
    </div>
</div>
<!-- onload="ifrHeight()" -->
<iframe id="content" class="ui-layout-center" style="padding-top: 70px;"
        name="ifr" frameborder="no" border="0" marginwidth="0"
        marginheight="0" scrolling="auto" allowtransparency="yes"
        src="index.html"></iframe>
<!-- 
<div id="desktopFooter" class="ui-layout-south">
    &copy; 2011-2015
    <!-- <a target="_blank" href="#">亚普软件（北京）有限公司</a> - <a id="licenseLink" href="#">www.upsoft.com</a> -->
<!-- 
</div>
 -->
<div id="dialog-form" title="帐户信息" style="display:none">
    <form class="form-horizontal" style="margin-top:40px;">
        <fieldset>
            <div class="control-group" id="oldpassdiv">
                <label for="oldpassword" style="font-size:13px" class="control-label">原密码</label>

                <div class="controls">
                    <input type="password" id="oldpassword" class="span2" onMouseOut="updatePassMouseOut('old')">
                    <span class="help-inline" id="oldpasserror"></span>
                </div>
            </div>
            <div class="control-group" id="newpassdiv">
                <label for="newpassword" style="font-size:13px" class="control-label">更改密码</label>

                <div class="controls">
                    <input type="password" id="newpassword" class="span2" onMouseOut="updatePassMouseOut('new')">
                    <span class="help-inline" id="newpasserror"></span>
                </div>
            </div>
            <div class="control-group">
                <label for="confirmpassword" style="font-size:13px" class="control-label">重复密码</label>

                <div class="controls">
                    <input type="password" id="confirmpassword" class="span2"
                           onMouseOut="updatePassMouseOut('confirm')">
                    <span class="help-inline" id="confirmpasserror"></span>
                </div>
            </div>
        </fieldset>
    </form>
</div>
<script type="text/javascript">
	$(document).ready(function(){
	  // 修改iframe的样式
		$("#content").css("padding-top", "40px");
		setit();
	});
</script>
<script src="webpage/js/bootstrap/js/bootstrap.js"></script>
</body>
</html>