var orgid = ""; //全局的，添删修，后刷新当前账户组下账户列表
var newParentid =""; //全局的移动组的组节点ID
function funcheck(){
	var flag = true;
	if(orgid==""){
		openalert("请选择一个节点！");
		flag = false;
	}
	if(orgid==1){
		openalert("不能对根节点进行操作！");
      		flag = false;
	}
	return flag;
}
$(function () {
//http://blog.csdn.net/kdiller/article/details/6059700
//http://blog.csdn.net/xushichang/article/details/5795532
//http://hi.baidu.com/danghj/item/b3c123a99f172e706cd455bd
//http://www.jstree.com/demo
	
	$("#demo").bind("before.jstree", function (e, data) {
		if (data.func == "remove") {
			if(funcheck()){
				if(orgid==2){
			        openalert("不能对默认组进行删除！");
	        		return false;
	        	}
        	}else{
        		return false;
        	}
		}
		if (data.func == "rename") {
			if(funcheck()){
				if(orgid==2){
			        openalert("不能对默认组进行重命名！");
	        		return false;
	        	}
        	}else{
        		return false;
        	}
		}
	})
	.jstree({
	    //tree settings
	    "json_data" : {
			"ajax" : {
				"url" : "orgtreeAction.action",
				"data" : function (n) { 
					return { nodeId : n.attr ? n.attr("id") : 0 };
				},
				error:function(n){
					window.location.href="../../common/logout.jsp";
				}
			}
		},
		"plugins" : [ "themes","json_data","ui","crrm"],
		"core" : {
			"initially_open" : [ "1" ]
		}
	}).bind("select_node.jstree",function(e,data) { 
	    var node = data.rslt.obj, inst = data.inst;
	    //账户列表
	    orgid = node.attr("id")//选择后给全局orgid赋值
	    accountListInfo(orgid);
	    if(orgid!=1){
	    	$("#funmenu").css("display","block"); //显示添删改
	    }else{
	    	$("#funmenu").css("display","none"); //显示添删改
	    }
	    if (node.hasClass('jstree-closed')) { return inst.open_node(node); }
	    if (node.hasClass('jstree-open')) { return inst.close_node(node); }
	}).bind("create.jstree", function (e, data) {
        $.post(
            "addOrg.action",
            {
                "operation" : "create_node",
                "parentid" : data.rslt.parent.attr("id"),
                "position" : data.rslt.position,
                "orgname" : data.rslt.name,
                "type" : data.rslt.obj.attr("rel")
            },
            function (r) {
                if(r!="error") {
                    $(data.rslt.obj).attr("id", r); //r.id :r为后台直接返回的roid
                }else {
                    $.jstree.rollback(data.rlbk);
                }
            }
        );
    }).bind("remove.jstree", function (e, data) {
        data.rslt.obj.each(function () {
        	bootbox.confirm("删除组后，该组下的账户会到默认组下，确定要删除吗？", function(result) {
	            if(result){
	                 $.ajax({
		                async : false,
		                type: 'POST',
		                url: "delOrg.action",
		                data : {
		                    "operation" : "remove_node",
		                    "orgid" : data.rslt.obj.attr("id")
		                },
		                success : function (r) {
		                    if(r=="succ") {
		                        openalert("删除成功！");
		                    }else{
		                    	$.jstree.rollback(data.rlbk);
		                    	openalert("删除组出错，请重新尝试或与管理员联系。")
		                    }
		                }
		            });
	            }else{
	            	$.jstree.rollback(data.rlbk);
	            }
	        });// bootbox end
        });
    })
    .bind("rename.jstree", function (e, data) {
        $.post(
            "editOrg.action",
            {
                "operation" : "rename_node",
                "orgid" : data.rslt.obj.attr("id"),
                "orgname" : data.rslt.new_name
            },
            function (r) {
                if(r!="succ") {
                    $.jstree.rollback(data.rlbk);
                }
            }
        );
    })
   /* .bind("move_node.jstree", function (e, data) {
        data.rslt.o.each(function (i) {
        	alert(data.rslt.cr === -1 ? 1 : data.rslt.np.attr("id"));
            $.ajax({
                async : false,
                type: 'POST',
                url: "moveOrg.action",
                data : {
                    "operation" : "move_node",
                    "orgid" : $(this).attr("id"),
                    "ref" : data.rslt.cr === -1 ? 1 : data.rslt.np.attr("id"),
                    "position" : data.rslt.cp + i,
                    "title" : data.rslt.name,
                    "copy" : data.rslt.cy ? 1 : 0
                },
                success : function (r) {
                    if(!r.status) {
                        $.jstree.rollback(data.rlbk);
                    }
                    else {
                        $(data.rslt.oc).attr("id", r.id);
                        if(data.rslt.cy && $(data.rslt.oc).children("UL").length) {
                            data.inst.refresh(data.inst._get_parent(data.rslt.oc));
                        }
                    }
                    $("#analyze").click();
                }
            });
        });
    });*/
    
	//账户组操作
	$("#mmenu a").click(function () {
		switch(this.id) {
			case "add_default":
			case "add_folder":
				$("#demo").jstree("create", null, "last", { "attr" : { "rel" : this.id.toString().replace("add_", "") } });
				break;
			case "search":
				$("#demo").jstree("search", document.getElementById("text").value);
				break;
			case "text": break;
			default:
				$("#demo").jstree(this.id);
				break;
		}
	});
	
	//添加
	$("#addAcc").click(function(){
		$("#account").val(""); //清空accountid，用于修改，添加判断
		$("#accountForm").clearForm(); //表单清除
		$("#pwd").css("display", "none"); //密码不可见
		$("#state").css("display","none");//状态不可见
		$("#ad_up_account").modal({backdrop:'static'});
	});
	//删除
	$("#delAcc").click(function(){
		var f_str = "";
		$("input[name='account']").each(function(){
		   if($(this).attr("checked")=="checked"){
		    f_str += $(this).attr("value")+",";
		   }
		});
		if(f_str==""){
			openalert("请选择要删除的数据！");
		}else{
			bootbox.confirm("确定要删除选择的数据吗？", function(result) {
	            if(result){
	                f_str = f_str.substring(0,f_str.length-1);
	                $.ajax({
	                    type:"post",
	                    url:"delAccount.action",
	                    data: "par=" + f_str,
	                    success: function(data){
	                        if (data=="succ") {
	                           	openalert("删除成功！");
	                           	accountListInfo(orgid);//刷新列表
	                            //window.location.reload();
	                        }else {
	                            openalert("删除账户出错，请重新尝试或与管理员联系。");
	                        }
	                    },
	                    error: function() {
	                        openalert("执行操作出错，请重新尝试或与管理员联系。");
	                    }
	                });
	            }
	        });
		}
	});
	
});

//账户组移动
function openMoveOrg(){
	if(funcheck()){
		newParentid = "";//清除
		$("#orgOfaccountTree").jstree({
		    "json_data" : {
				"ajax" : {
					"url" : "orgtreeAction.action",
					"data" : function (n) { 
						return { nodeId : n.attr ? n.attr("id") : 0 };
					}
				}
			},
			"plugins" : [ "themes","json_data","ui"],
			"core" : {
				"initially_open" : [ "1" ]
			}
		}).bind("select_node.jstree",function(e,data) { 
		    var node = data.rslt.obj, inst = data.inst;
		    newParentid = node.attr("id"); //
		   	
		});
		$("#orgTree").modal('show');	
	}
	
	
	$("#demo4").jstree({ 
		"crrm" : {
			"move" : {
				"default_position" : "first",
				"check_move" : function (m) { return (m.o[0].id === "thtml_1") ? false : true;  }
			}
		},
		"ui" : {
			"initially_select" : [ "thtml_2" ]
		},
		"core" : { "initially_open" : [ "thtml_1" ] },
		"plugins" : [ "themes", "html_data", "ui", "crrm" ]
	});
	
}
function saveMoveOrg(){

	$.ajax({
	   type: "POST",
	   url: "moveOrg.action",
	   data: "orgid="+orgid+"&newParentid="+newParentid,
	   success: function(msg){
	   		$("#orgTree").modal('hide');
	   		if("succ"==msg){
	   			openalert("移动成功！");
	   			$("#demo").jstree("move_node","#"+orgid,"#"+newParentid);
	   			//accountListInfo(orgid);//刷新列表
	   		}else{
	   			openalert("移动失败！");
	   		}
	   }
	});
}

//用户列表
function accountListInfo(orgid){
	$.ajax({
		async : false,
		url : "listAccount.action?orgid="+orgid,
		type : 'post',
		dataType : 'script',
		success : function(data) {
			if (data != "error") {
				var content = createAccountTable(accountList);
				$("#accountList").html(content);
			} else {
				alert("error");
			}
		},error:function(){
			var table = "<table class=\"table table-striped table-bordered\">";
				table += "<thead>";
				table += "<tr>";
				table += "<th width=\"5%\">#</th>";
				table += "<th>账户名称</th>";
				table += "<th>状态</th>";
				table += "<th>描述</th>";
				table += "<th>操作</th>";
				table += "</tr>";
				table += "</thead>";
				table += "</table>";
			$("#accountList").html(table);
		}
	});
}

//创建Table
function createAccountTable(accountList){
	var table = "<table class=\"table table-striped table-bordered\">";
	table += "<thead>";
	table += "<tr>";
	table += "<th width=\"5%\">#</th>";
	table += "<th width=\"25%\">账户名称</th>";
	table += "<th width=\"10%\">状态</th>";
	table += "<th width=\"52%\">描述</th>";
	table += "<th width=\"8%\">操作</th>";
	table += "</tr>";
	table += "</thead>";
	tr = createAccountTr(accountList);
	table += tr;
	table += "</table>";
	return table;
}
function createAccountTr(accountList){
	var tr = "";
	if (accountList.length > 0) {
		for (var i=0;i<accountList.length;i++) {
			tmp = "<tr>";
			tmp += "<td><input name=\"account\" type=\"checkbox\" value=\""+accountList[i].accountid+"\"/></td>";
			tmp += "<td>"+accountList[i].accountcode+"</td>";
			tmp += "<td>"+accountList[i].accountstate+"</td>";
			tmp += "<td>"+accountList[i].accountmemo+"</td>";
			tmp += "<td><a href=\"javascript::\" onclick=\"getAccount('"+accountList[i].accountid+"')\">修改</a></td>";
			tmp += "</tr>";
			tr += tmp;
		}
	}else{
		tr = "";
	}
	return tr;
}

//save
function save(){
	var accountid = $("#accountid").val();
	if(accountid==""){
		saveAccount();//添加
	}else{
		updateAccount();//修改
	}
}

//保存-添加账户
function saveAccount(){
	var accountcode = $("#accountcode").val();
	var accountmemo = $("#accountmemo").val();
	if(checkInfo(accountcode,accountmemo)){
		$.ajax({
		   type: "POST",
		   url: "saveAccount.action",
		   data: "sysAccount.accountcode="+accountcode+"&sysAccount.accountmemo="+accountmemo+"&orgid="+orgid,
		   success: function(msg){
		   		if("succ"==msg){
		   			$("#ad_up_account").modal('hide');
		   			openalert("添加成功！");
		   			accountListInfo(orgid);//刷新列表
		   		}else{
		   			openalert("添加失败！");
		   		}
		   }
		});
	}
}
//修改-获得一个role对象
function getAccount(accountid){
	$('#accountForm').clearForm(); //表单清除
	$("#pwd").css("display", "block"); //密码可见
	$("#state").css("display","block");//状态可见
	//修改账户弹出框
	$("#ad_up_account").modal('show');
	$.ajax({
		async : false,
		url : "getAccount.action?accountid="+accountid,
		type : 'post',
		dataType : 'script',
		success : function(data) {
			if (data != "error") {
				acc = eval(account);
				$("#accountid").val(acc.accountid);
				$("#accountcode").val(acc.accountcode);
				$("#accountmemo").val(acc.accountmemo);
			} else {
				openalert("读取数据时出错,请重新登录尝试或与管理员联系!");
			}
		}
	});
}
//修改
function updateAccount(){
	var accountid = $("#accountid").val();
	var accountcode = $("#accountcode").val();
	var accountmemo = $("#accountmemo").val();
	var accountstate = $("#accountstate").val();
	var password = $("#password").val();
	
	if(checkInfo(accountcode,accountmemo)){
		$.ajax({
		   type: "POST",
		   url: "updAccount.action",
		   data: "sysAccount.accountid="+accountid+"&sysAccount.accountcode="+accountcode+"&sysAccount.accountmemo="+accountmemo+"&sysAccount.accountstate="+accountstate+"&password="+password,
		   success: function(msg){
		   		if("succ"==msg){
		   			$("#ad_up_account").modal('hide');
		   			openalert("修改成功！");
		   			accountListInfo(orgid);//刷新列表
		   		}else{
		   			openalert("修改失败，请重新登录尝试或与管理员联系!");
		   		}
		   }
		});
	}
}

//JS验证，不是很好
function checkInfo(accountcode,accountmemo){
	if(accountcode==""){
		$("#codeInfo").removeClass('info').addClass('error');
		return false;
	}
	if(accountmemo==""){
		$("#memoInfo").removeClass('info').addClass('error');
		return false;
	}
	return true;
}
//设置状态
function setState(value){
	$("#accountstate").val(value);
}