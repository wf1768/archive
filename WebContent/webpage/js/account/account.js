/**
 * 账户信息
 * guodh
 * 2013/03/12
*/
$(function(){
	//获得父页中的账户
	var account = $('#account_code', window.parent.document);
	$.ajax({
	  url: "../../js/account/account.html",
	  cache: false,
	  success: function(html){
	    $("#account").append(html);
	    $("#uid").html(account.val());
	  }
	});
	$.ajax({
	  url: "../../js/account/account_info.html",
	  cache: false,
	  success: function(html){
	    $("#accountInfo").html(html);
	  }
	});
	
});

//退出
function quit() {
   us.openconfirm("真的要退出系统吗?", "系统提示", function () {
       window.location.href = "../../common/logout.jsp";
   });
}

//账户信息 - 修改密码
function openAccountInfo() {
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
	});//end dialog-form
    $("#oldpassword").val("");
    $("#newpassword").val("");
    $("#confirmpassword").val("");
    $("#dialog-form").dialog("open");
}


// 通用alert
openalert = function(text) {
    var html = $(
        '<div id="dialog-message" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">' +
            '<div class="modal-header">' +
                '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>' +
                '<h3 id="myModalLabel">系统提示</h3>' +
            '</div>' +
            '<div class="modal-body">' +
                '<p id="continfo">' + text + '</p>' +
            '</div>' +
            '<div class="modal-footer">' +
                '<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>' +
            '</div>' +
        '</div>');
    return html.modal()
}
//
function modalCommon(text){
	var html = $(
        '<div id="updMedia" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">' +
            '<div class="modal-header">' +
                '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>' +
                '<h3 id="myModalLabel">修改多媒体库</h3>' +
            '</div>' +
            '<div class="modal-body">' +
                '' + text + '' +
            '</div>' +
            '<div class="modal-footer">' +
                '<button class="btn" data-dismiss="modal">取 消</button>'+
				'<button class="btn btn-primary" onclick="update();">保 存</button>'+
            '</div>' +
        '</div>');
  	return html.modal();
}