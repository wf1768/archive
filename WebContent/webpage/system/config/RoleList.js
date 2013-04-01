/*
 * 角色管理
 * guodh
 * 2013/03/12
*/
$(function(){
	roleList_show();
	//添加
	$("#addRole").click(function(){
		$("#roleid").val(""); //清空roleid，用于修改，添加判断
		$("#ad_up_role").modal({backdrop:'static'});
	});
	//删除
	$("#delRole").click(function(){
		var f_str = "";
		$("input[name='role']").each(function(){
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
	                    url:"delRole.action",
	                    data: "par=" + f_str,
	                    success: function(data){
	                        if (data=="succ") {
	                           	openalert("删除成功！");
	                           	roleList_show();//刷新列表
	                            //window.location.reload();
	                        }else {
	                            openalert("删除采购单出错，请重新尝试或与管理员联系。");
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
//角色列表
function roleList_show(){
	$.ajax({
		async : false,
		url : "listRole.action",
		type : 'post',
		dataType : 'script',
		success : function(data) {
			if (data != "error") {
				var content = createRoleTable(roleList);
				$("#roleInfo").html(content);
			} else {
				alert("error");
			}
		}
	});
}

//创建参数设置Table
function createRoleTable(roleList){
	var table = "<table class=\"table table-striped table-bordered\">";
	table += "<thead>";
	table += "<tr>";
	table += "<th>#</th>";
	table += "<th>角色名称</th>";
	table += "<th>角色描述</th>";
	table += "<th>操作</th>";
	table += "</tr>";
	table += "</thead>";
	tr = createRoleTr(roleList);
	table += tr;
	table += "</table>";
	return table;
}
function createRoleTr(roleList){
	var tr = "";
	if (roleList.length > 0) {
		for (var i=0;i<roleList.length;i++) {
			tmp = "<tr>";
			tmp += "<td><input name=\"role\" type=\"checkbox\" value=\""+roleList[i].roleid+"\"/></td>";
			tmp += "<td>"+roleList[i].rolename+"</td>";
			tmp += "<td>"+roleList[i].rolememo+"</td>";
			tmp += "<td><a href=\"javascript::\" onclick=\"getRole('"+roleList[i].roleid+"')\">修改</a>&nbsp;<a href=\"javascript::\" onclick=\"setRoleFun('"+roleList[i].roleid+"')\">权限</a></td>";
            
            /*"<a href=\"javascript::\" class=\"btn btn-info\" onclick=\"getRole('"+roleList[i].roleid+"')\">"+
				"<i class=\"icon-edit icon-white\"></i>"+                                            
			"</a>"+
			"<a class=\"btn btn-info\" href=\"javascript::\" onclick=\"setRoleFun('"+roleList[i].roleid+"')\">权限</a>"+
			"</td>";*/
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
	var roleid = $("#roleid").val();
	if(roleid==""){
		saveRole();//添加
	}else{
		updateRole();//修改
	}
}

//保存
function saveRole(){
	var rolename = $("#rolename").val();
	var rolememo = $("#rolememo").val();
	if(checkInfo(rolename,rolememo)){
		$.ajax({
		   type: "POST",
		   url: "saveRole.action",
		   data: "sysRole.rolename="+rolename+"&sysRole.rolememo="+rolememo,
		   success: function(msg){
		   		if("succ"==msg){
		   			$("#ad_up_role").modal('hide');
		   			openalert("添加成功！");
		   			roleList_show();//刷新列表
		   		}else{
		   			openalert("添加失败！");
		   		}
		   }
		});
	}
}
//获得一个role对象
function getRole(roleid){
	$("#ad_up_role").modal('show');
	$.ajax({
		async : false,
		url : "getRole.action?roleid="+roleid,
		type : 'post',
		dataType : 'script',
		success : function(data) {
			if (data != "error") {
				rol = eval(role);
				$("#roleid").val(rol.roleid);
				$("#rolename").val(rol.rolename);
				$("#rolememo").val(rol.rolememo);
			} else {
				openalert("读取数据时出错,请重新登录尝试或与管理员联系!");
			}
		}
	});
}
//修改
function updateRole(){
	var roleid = $("#roleid").val();
	var rolename = $("#rolename").val();
	var rolememo = $("#rolememo").val();
	if(checkInfo(rolename,rolememo)){
		$.ajax({
		   type: "POST",
		   url: "updRole.action",
		   data: "sysRole.roleid="+roleid+"&sysRole.rolename="+rolename+"&sysRole.rolememo="+rolememo,
		   success: function(msg){
		   		if("succ"==msg){
		   			$("#ad_up_role").modal('hide');
		   			openalert("修改成功！");
		   			roleList_show();//刷新列表
		   		}else{
		   			openalert("修改失败，请重新登录尝试或与管理员联系!");
		   		}
		   }
		});
	}
}
//
function checkInfo(rolename,rolememo){
	if(rolename==""){
		$("#nameInfo").removeClass('info').addClass('error');
		return false;
	}
	if(rolememo==""){
		$("#memoInfo").removeClass('info').addClass('error');
		return false;
	}
	return true;
}
//给角色授权
function setRoleFun(roleid){
	$("#functionList").jstree({
	    //tree settings
	    "json_data" : {
	    	//data:[{ "attr": { "id" : "2950179"},"state": "closed","children" : [{ "attr": { "id" : "2950181"},"data": "用户查看","state": "closed"},{ "attr": { "id" : "2950183"},"data": "用户增加"},{ "attr": { "id" : "2950185","class":"jstree-checked"},"data": "用户删除"}],"data": "用户管理"},{"attr": { "id": "2950187"},"state": "closed",children : [{ "attr": { "id": "2950189"},"data": "角色查看"},{ "attr": { "id": "2950191"},"data": "角色增加"},{ "attr": { "id": "2950193"},"data": "角色删除"}],"data": "角色管理"}],//json格式，加载静态数据  
			//"ajax" : {//使用ajax加载数据，如果和data同时使用则只在打开未加载的子节点时起作用
				//"url" : "listRoleFunction.action?roleid=C553B2B9A4200001DDA3F0601D90D250",
				//async:false  
			//}
			"ajax" : {
				"url" : "listRoleFunction.action?roleid="+roleid,
				 async:false, 
				"data" : function (n) { 
					return { functionid : n.attr ? n.attr("id") : 0 };
				}
			}
		},
		"plugins" : [ "themes", "json_data","checkbox","ui"]
	})
	.bind("loaded.jstree", function(e,data){  
		data.inst.open_all(-1); //全部展开
	})
	/*.bind('click.jstree', function(event) { //绑定click事件  
            $("#demo").jstree("get_checked").each(function(i,node){
            	alert(node.id);
            });//查看每个被选中节点的id属性值  
    }); */
	$('#roleid').val(roleid); //把roleid带过去，用于保存
	$('#roleFunction').modal('show');
}
//保存该角色拥有的功能
function saveRoleFun(){
	//得到父节点和部分选中节点的ID，全选中什么也得不到
	var checked_ids = [];
	$("#functionList").find(".jstree-undetermined").each(function(i,element){     
	    checked_ids.push($(element).attr("id"));
	    if ($(this).find(".jstree-undetermined").length == 0) {  
	        $(this).find(".jstree-checked").each(function(i, element){          
	            checked_ids.push($(element).attr("id"));
	        });         
	    }
	    //alert(checked_ids);
	});
	//查看每个被选中节点的id属性值 ,如果该节点的子节点全都选择那么ID值为父节点ID
	//var functionids = [];
	$("#functionList").jstree("get_checked",null,true).each(function(i,node){ //null，true 全选父，子节点都获得,部分节点选中得不到父节点
		//处理重复的节点ID 
		var flag = true;
		$.each(checked_ids,function(key,value){
			if(value==node.id){
				flag = false;
				return false;
			}
		});
		if(flag){checked_ids.push(node.id);}
	});
	var roleid = $('#roleid').val();//角色ID
	$.ajax({
	   type: "POST",
	   url: "saveRoleFun.action",
	   data: "roleid="+roleid+"&functionids="+checked_ids,
	   success: function(msg){
	   		if("succ"==msg){
	   			$("#roleFunction").modal('hide');
	   			openalert("授权成功！");
	   		}else{
	   			$("#roleFunction").modal('hide');
	   			openalert("授权失败，请重新登录尝试或与管理员联系!");
	   		}
	   }
	});
}


////////////////////////////////////////////////////////////////////////////////
function modal(){
//http://blog.csdn.net/sunbiao0526/article/details/8176706
//http://stackoverflow.com/questions/4128607/jstree-checkbox-checked-on-load
	//listRoleFunction.action?roleid=' + row.roleid 
	//C553B2B9A4200001DDA3F0601D90D250
	$("#demo").jstree({
	    //tree settings
	    "json_data" : {
	    	//data:[{ "attr": { "id" : "2950179"},"state": "closed","children" : [{ "attr": { "id" : "2950181"},"data": "用户查看","state": "closed"},{ "attr": { "id" : "2950183"},"data": "用户增加"},{ "attr": { "id" : "2950185","class":"jstree-checked"},"data": "用户删除"}],"data": "用户管理"},{"attr": { "id": "2950187"},"state": "closed",children : [{ "attr": { "id": "2950189"},"data": "角色查看"},{ "attr": { "id": "2950191"},"data": "角色增加"},{ "attr": { "id": "2950193"},"data": "角色删除"}],"data": "角色管理"}],//json格式，加载静态数据  
			//"ajax" : {//使用ajax加载数据，如果和data同时使用则只在打开未加载的子节点时起作用
				//"url" : "listRoleFunction.action?roleid=C553B2B9A4200001DDA3F0601D90D250",
				//async:false  
			//}
			"ajax" : {
				"url" : "listRoleFunction.action?roleid=C553B2B9A4200001DDA3F0601D90D250",
				 async:false, 
				"data" : function (n) { 
					return { functionid : n.attr ? n.attr("id") : 0 };
				}
			}
		},
		"plugins" : [ "themes", "json_data","checkbox","ui"]
	})
	.bind("loaded.jstree", function(e,data){  
		data.inst.open_all(-1); //全部展开
	})
	/*.bind('click.jstree', function(event) { //绑定click事件  
            $("#demo").jstree("get_checked").each(function(i,node){
            	alert(node.id);
            });//查看每个被选中节点的id属性值  
    }); */
	$('#myModal').modal('show');
}
