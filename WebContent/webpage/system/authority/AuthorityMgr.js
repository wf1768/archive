var authorityCommon = new us.archive.Authority();
var orgid = ""; //全局的，添删修，后刷新当前账户组下账户列表
var rel = ""; //类型，文件夹(folder)Or文件(default)

var templettype = "";
$(function () {
	$("#demo").jstree({
	    //加载账户组树
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
	    orgid = node.attr("id");//选择后给全局orgid赋值
	    authorityCommon.selectNodeid = orgid; //最好把这改掉 用统一的 咱先这样
	    if(orgid!=1){
		    //账户组授权
		    $.ajax({
				url: "AuthorityList.html",
				cache: false,
				success: function(html){
					$("#authorityInfo").html(html);
				  	if(orgid!=1){ //根不可操作
					    // 设置帐户组名称
						$("#selectOrgName").html("<font color=red>" + $("#"+node.attr("id")+" a").first().text() + "</font>");
						$("#accountid").val(''); //把账户ID清空，必须的
					    $.ajax({
							async : false,
							url : "getOrgAuthorityInfo.action?orgid=" + orgid,
							type : 'post',
							dataType : 'script',
							success : function(data) {
								$("#orgOfAccount").html(accountnum);
								$("#orgOfRole").html(rolename);
							}
						});
						//组档案树orgoftree  loadOrgOfTreeData orgid orgid
						orgoftree();
				    }
				}
			});
	 	}
	 	if (node.hasClass('jstree-closed')) { return inst.open_node(node); }
	    if (node.hasClass('jstree-open')) { return inst.close_node(node); }
	});
	
	
	$('#selectField').change(function(){ 
		$("#fieldname").val($(this).children('option:selected').text());
	}) 
});

//设置角色
function setOrgOrAccountRole(roleid,rolename){
	//账户ID
	var accountid = $("#accountid").val();
	//给账户设置角色
	if(accountid!='' && accountid!=null){
		$.ajax({
		   type: "POST",
		   url: "setAccountRole.action",
		   data: "accountid="+accountid+"&roleid="+roleid,
		   success: function(msg){
				if(msg=="succ"){
					$("#accountRole").html("<font color=red>" + rolename + "</font>");
				}else{
					openalert("账户授权出错，请重新尝试或与管理员联系！");
				}
		   }
		});
	}else{
		//给组设置角色
		$.ajax({
		   type: "POST",
		   url: "setOrgRole.action",
		   data: "orgid="+orgid+"&roleid="+roleid,
		   success: function(msg){
		   		if(msg=="succ"){
					$("#orgOfRole").html("<font color=red>" + rolename + "</font>");
				}else{
					openalert("账户组授权出错，请重新尝试或与管理员联系！");
				}
		   }
		});
	}
	$('#roleTable').modal('hide');
}
//设置角色-查看角色
function showRoleList(){
	$.ajax({
		async : false,
		url : "listRole.action",
		type : 'post',
		dataType : 'script',
		success : function(data) {
			if (data != "error") {
				var content = createRoleTable(roleList);
				$("#roleList").html(content);
			} else {
				alert("error");
			}
		}
	});
	$('#roleTable').modal('show');
}
//设置角色-查看角色-创建Table
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
//设置角色-查看角色-创建Tr
function createRoleTr(roleList){
	var tr = "";
	if (roleList.length > 0) {
		for (var i=0;i<roleList.length;i++) {
			tmp = "<tr>";
			tmp += "<td>"+(i+1)+"</td>";
			tmp += "<td>"+roleList[i].rolename+"</td>";
			tmp += "<td>"+roleList[i].rolememo+"</td>";
			tmp += "<td><a href=\"javascript::\" onclick=\"setOrgOrAccountRole('"+roleList[i].roleid+"','"+roleList[i].rolename+"')\">设置</a></td>";
			tmp += "</tr>";
			tr += tmp;
		}
	}else{
		tr = "";
	}
	return tr;
}

//设置权限
function getAccount(accountid,accountcode){
	$("#accountTable").modal('hide');
	authorityCommon.accountid = accountid;
	$.ajax({
		url: "AccountAuthorityList.html",
		cache: false,
		success: function(html){
			$("#authorityInfo").html(html);
		    // 设置帐户组名称
			$("#accountName").html("<font color=red>"+accountcode+"</font>");
			$("#accountid").val(accountid);//设置角色用
		    //查看该账户是否已选择角色
			$.ajax({
				async : false,
				url : "getAccountAuthorityInfo.action?accountid=" + accountid,
				type : 'post',
				dataType : 'script',
				success : function(data) {
					$("#accountRole").html(role);
				}
			});
			
			//账户档案树accountoftree  loadAccountOfTreeData  accountid
			accountoftree(accountid);
		}
	});
	
}

//查看账户
function showAccountList(){
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
	$("#accountTable").modal('show');
}
//查看账户-账户列表-创建Table
function createAccountTable(accountList){
	var table = "<table class=\"table table-striped table-bordered\">";
	table += "<thead>";
	table += "<tr>";
	table += "<th width=\"5%\">#</th>";
	table += "<th width=\"21%\">账户名称</th>";
	table += "<th width=\"10%\">状态</th>";
	table += "<th width=\"37%\">描述</th>";
	table += "<th width=\"13%\">操作</th>";
	table += "</tr>";
	table += "</thead>";
	tr = createAccountTr(accountList);
	table += tr;
	table += "</table>";
	return table;
}
//查看账户-账户列表-创建Tr
function createAccountTr(accountList){
	var tr = "";
	if (accountList.length > 0) {
		for (var i=0;i<accountList.length;i++) {
			tmp = "<tr>";
			tmp += "<td>"+(i+1)+"</td>";
			tmp += "<td>"+accountList[i].accountcode+"</td>";
			tmp += "<td>"+accountList[i].accountstate+"</td>";
			tmp += "<td>"+accountList[i].accountmemo+"</td>";
			tmp += "<td><a href=\"javascript::\" onclick=\"getAccount('"+accountList[i].accountid+"','"+accountList[i].accountcode+"')\">设置权限</a></td>";
			tmp += "</tr>";
			tr += tmp;
		}
	}else{
		tr = "";
	}
	return tr;
}

//档案节点-【帐户组】访问权限
function setOrgTree(){
	var checkeds = getAuthorityParam("orgoftree"); //已选中的节点ID
	var par = getOrgAuthority(checkeds);
	$.ajax({
	   type: "POST",
	   url: "setOrgTree.action",
	   data: "orgid="+orgid+"&par="+par,
	   success: function(msg){
	   		if("succ"==msg){
	   			openalert("授权成功！");
	   			//组档案树orgoftree  loadOrgOfTreeData orgid orgid
				orgoftree();//刷新
	   		}else{
	   			openalert("授权失败，请重新登录尝试或与管理员联系!");
	   		}
	   }
	});
}
//档案节点-【帐户】访问权限
function setAccountTree(){
	//账户ID
	var accountid = $("#accountid").val();
	var checkeds = getAuthorityParam("accountoftree"); //已选中的节点ID
	var par = getAccountAuthority(checkeds);
	$.ajax({
	   type: "POST",
	   url: "setAccountTree.action",
	   data: "accountid="+accountid+"&par="+par,
	   success: function(msg){
	   		if("succ"==msg){
	   			openalert("授权成功！");
	   			//账户档案树accountoftree  loadAccountOfTreeData  accountid
				accountoftree(accountid);//刷新
	   		}else{
	   			openalert("授权失败，请重新登录尝试或与管理员联系!");
	   		}
	   }
	});
}
/**
* 获得【账户组、账户】已选中档案树节点的IDs
* treeId 档案树ID【账户组，账户】
*/
function getAuthorityParam(treeId){
	//得到父节点和部分选中节点的ID，全选中什么也得不到
	var checked_ids = [];
	$("#"+treeId).find(".jstree-undetermined").each(function(i,element){     
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
	$("#"+treeId).jstree("get_checked",null,true).each(function(i,node){ //null，true 全选父，子节点都获得,部分节点选中得不到父节点
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
	//var par = getArcAuthority(checked_ids);
	return checked_ids;
}
/**
* 获取【账户组】已选中的档案节点已有的，浏览、下载、打印权限
* checked_ids 已选中的节点
*/
function getOrgAuthority(checked_ids){
	authorityCommon.getOrgTree();
	var str = "[";
	var d = "";
	var temp = "";
	for (var i=0;i<checked_ids.length;i++) {
		d += "{'orgTreeId':'','orgid':'";
		d += authorityCommon.selectNodeid;
		d += "','treeid':'";
		d += checked_ids[i];
		if (authorityCommon.orgOfTree.length > 0) {
			for (var j=0;j<authorityCommon.orgOfTree.length;j++) {
				if (authorityCommon.orgOfTree[j].treeid == checked_ids[i]) {
					temp = "','filescan':" + authorityCommon.orgOfTree[j].filescan;
					temp += ",'filedown':" + authorityCommon.orgOfTree[j].filedown;
					temp += ",'fileprint':" + authorityCommon.orgOfTree[j].fileprint;
					break;
				}
			}
		}
		if (temp == "") {
			temp = "','filescan':0";
			temp += ",'filedown':0";
			temp += ",'fileprint':0";
		}
		d += temp;
		temp = "";
		d += "}";
		//str.push(d);
		str += d + ",";
		d = "";
	}
	if (str.length > 1) {
		str = str.substring(0, str.length -1) + "]";
	}
	else {
		str = "";
	}
	return str;
}
/**
* 获取【账户】已选中的档案节点已有的，浏览、下载、打印权限
* checked_ids 已选中的节点
*/
function getAccountAuthority(checked_ids){
	authorityCommon.getAccountTree();
	var str = "[";
	var d = "";
	var temp = "";
	for (var i=0;i<checked_ids.length;i++) {
		d += "{'accountTreeId':'','accountid':'";
		d += authorityCommon.accountid;
		d += "','treeid':'";
		d += checked_ids[i];
		if (authorityCommon.accountTree.length > 0) {
			for (var j=0;j<authorityCommon.accountTree.length;j++) {
				if (authorityCommon.accountTree[j].treeid == checked_ids[i]) {
					temp = "','filescan':" + authorityCommon.accountTree[j].filescan;
					temp += ",'filedown':" + authorityCommon.accountTree[j].filedown;
					temp += ",'fileprint':" + authorityCommon.accountTree[j].fileprint;
					break;
				}
			}
		}
		if (temp == "") {
			temp = "','filescan':0";
			temp += ",'filedown':0";
			temp += ",'fileprint':0";
		}
		d += temp;
		temp = "";
		d += "}";
		str += d + ",";
		d = "";
	}
	if (str.length > 1) {
		str = str.substring(0, str.length -1) + "]";
	}else {
		str = "";
	}
	return str;
}
/**
* 档案节点的【帐户组】访问权限 / 档案节点电子全文的[帐户组]权限
**/
function orgoftree(){
	authorityCommon.getOrgTree();
	$("#orgoftree").jstree({
	    "json_data" : {
			"ajax" : {
				"url" : "loadOrgOfTreeData.action?orgid="+orgid,
				"data" : function (n) { 
					return { parentid : n.attr ? n.attr("id") : 0 };
				}
			}
		},
		"plugins" : [ "themes","json_data","ui","crrm","checkbox","types"],
		"core" : {
			"initially_open" : [ "2" ]
		},
		"types" : {
			"valid_children" : [ "root" ],
			"types" : {
				//设置rel=root的参数
				"root" : {
					//图标
					"icon" : { 
						"image" : "../../images/icons/house.png"
					},
					"valid_children" : [ "folder","default" ],
					"max_depth" : 2,
					"hover_node" : false,
					"select_node" : function () {return false;}
	
				},
				"default" : {
					"valid_children" : [ "none" ],
					"icon" : { 
						"image" : "../../images/icons/page.png" 
					}
				},
				"folder" : {
					"valid_children" : [ "default", "folder" ],
					"icon" : {
						"image" : "../../images/icons/folder.png"
					}
				}
			}
		}
		
	})
	.bind("select_node.jstree",function(e,data) { 
	    var node = data.rslt.obj, inst = data.inst;
	    rel = node.attr('rel'); //用于判断是文件夹还是文件
	    authorityCommon.orgTreeid = "";
       	$("#filescan").attr("disabled", true);
		$("#filedown").attr("disabled", true);
		$("#fileprint").attr("disabled", true);
		$("#filescan").attr("checked", false); 
		$("#filedown").attr("checked", false); 
		$("#fileprint").attr("checked", false);
		$(".bu").addClass("disabled");
		$(".bu").attr("disabled","true"); 
       	if (authorityCommon.orgOfTree.length > 0 ) {  //&& rel != "folder"
       		for (var i=0;i<authorityCommon.orgOfTree.length;i++) {
       			if (authorityCommon.orgOfTree[i].treeid == node.attr("id") && authorityCommon.orgOfTree[i].orgid) {
       				authorityCommon.orgTreeid = authorityCommon.orgOfTree[i].orgTreeId;
       				authorityCommon.selectTree = authorityCommon.orgOfTree[i];
       				authorityCommon.isSetAccount = 2;
       				$("#filescan").removeAttr("disabled");
       				$("#filedown").removeAttr("disabled");
       				$("#fileprint").removeAttr("disabled");
       				$(".bu").removeClass("disabled");
       				$(".bu").removeAttr("disabled"); 
       				if (authorityCommon.orgOfTree[i].filescan == 1) {
       					$("#filescan").attr("checked",'true');
       				}
       				if (authorityCommon.orgOfTree[i].filedown == 1) {
       					$("#filedown").attr("checked",'true');
       				}
       				if (authorityCommon.orgOfTree[i].fileprint == 1) {
       					$("#fileprint").attr("checked",'true');
       				}
       				
       				//获取选择的树节点档案类型，是A还是F／P
       				$.ajax({
       			        async: false,
       			        url: "getTempletType.action",
       			        type: 'post',
       			        dataType: 'text',
       			        data: {treeid: authorityCommon.selectTree.treeid},
       			        success: function (data) {
       			            if (data != "error") {
       			            	templettype = data;
       			            	if (templettype == 'F') {
       			            		$("#ajDataAuth").hide();
       			            	}
       			            	else {
       			            		$("#ajDataAuth").show();
       			            	}
       			            	
       			            	showDataAuth(authorityCommon.selectTree.filter);
       			            } else {
       			                openalert('读取数据时出错，请尝试重新操作或与管理员联系!');
       			            }
       			        }
       			    });
       				
//       				getOrgTreeDataAuth(authorityCommon.orgOfTree[i].filter);
       				break;
       			}
       		}
       	}
       	if (node.hasClass('jstree-closed')) { return inst.open_node(node); }
	    if (node.hasClass('jstree-open')) { return inst.close_node(node); }
    })
}


//设置数据权限范围
function showSetDataAuthWindow(tabletype){
//	alert(authorityCommon.selectTree);
	
	if (authorityCommon.selectTree == null) {
		alert("请选中一个档案节点，再编辑权限！");
		return;
	}
	
	readField(authorityCommon.selectTree.treeid,tabletype);
	$("#tableType").val(tabletype);
	$("#fieldname").val("");
	$("#fieldname").val($("#selectField").children('option:selected').text());
	$('#setDataAuth').modal('show');
}
//保存数据权限范围
function saveDataAuth() {
	//获取权限内容
	var dataAuthValue = $("#dataAuthValue").val();
	var selectField = $("#selectField").val();
	var oper = $("#oper").val();
	var fieldname = $("#fieldname").val();
	
	//创建对象
	var dataAuth = {};
	dataAuth.dataAuthValue = dataAuthValue;
	dataAuth.selectField = selectField;
	dataAuth.fieldname = fieldname;
	dataAuth.oper = oper;
	
	var tmpid="";
	var url = "";
	if (authorityCommon.isSetAccount != 1) {
		tmpid = authorityCommon.orgTreeid;
		url = "setDataAuth.action";
	}
	else {
		tmpid = authorityCommon.accountTreeid;
		url = "setAccountDataAuth.action";
	}
	var tableType = $("#tableType").val();
	//保存到服务器
	$.ajax({
        async: false,
        url: url,
        type: 'post',
        dataType: 'script',
        data: {orgTreeid: tmpid, tableType: tableType,filter:JSON.stringify(dataAuth)},
        success: function (data) {
            if (data != "error") {
            	//保存成功后，提示
            	openalert("授权成功！");
            	if (authorityCommon.isSetAccount != 1) {
            		authorityCommon.getOrgTree(); //刷新
            	}
            	else {
            		authorityCommon.getAccountTree(); //刷新
            	}
	   			
            	//保存成功后，显示在页面上。
	   			if (templettype != "F") {
	   				if (tableType == '01') {
	   					createDataAuthTable('ajAuth',JSON.stringify(dataAuth));
	   				}
	   				else if (tableType == '02') {
	   					createDataAuthTable('wjAuth',JSON.stringify(dataAuth));
	   				}
	   			}
	   			else {
	   				createDataAuthTable('wjAuth',JSON.stringify(dataAuth));
	   			}
	   			
//                field = eval(fields);
//                talbeField(field); //字段
            } else {
                openalert('读取数据时出错，请尝试重新操作或与管理员联系!');
            }
        }
    });
}
//显示数据访问权限
function showDataAuth(filter) {
	$("#ajAuth").html('');
	$("#wjAuth").html('');
	//如果当前树节点的档案类型是标准档案或图片档案
	if (templettype != "F") {
		//获取条件对象
		var array = JSON.parse(filter);
		
		for(var i=0;i<array.length;i++) {
			if (array[i].tableType == '01') {
				createDataAuthTable('ajAuth',JSON.stringify(array[i]));
			}
			else {
				createDataAuthTable('wjAuth',JSON.stringify(array[i]));
			}
		}
		
	}
	else {
		//获取条件对象
		var array = JSON.parse(filter);
		for(var i=0;i<array.length;i++) {
			createDataAuthTable('wjAuth',JSON.stringify(array[i]));
		}
	}
}
//创建数据访问权限页面显示
function createDataAuthTable(who,dataAuth) {
	if (dataAuth == "") {
		return;
	}
	var html = $("#"+who).html();
	var auth = JSON.parse(dataAuth);
	var tmp = '<tr id="'+auth.id+'">';
	tmp += '<td><input name="authid" type="checkbox" value="'+auth.id+'"/></td>'
	tmp += '<td>'+auth.fieldname+'</td>';
	tmp += '<td>'+auth.oper+'</td>';
	tmp += '<td>'+auth.dataAuthValue+'</td>';
	tmp += '</tr>';
	html += tmp;
	$("#"+who).html(html);
}

//删除组的数据访问权限
function removeDataAuth(who) {
	
	var f_str = "";
	$("#"+who+" :checkbox:checked").each(function(){
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
                
        		if (authorityCommon.isSetAccount != 1) {
        			tmpid = authorityCommon.orgTreeid;
        			url = "removeDataAuth.action";
        		}
        		else {
        			tmpid = authorityCommon.accountTreeid;
        			url = "removeAccountDataAuth.action";
        		}
        		
                $.ajax({
                    type:"post",
                    url:url,
                    data: {par:f_str,orgTreeid: tmpid},
                    success: function(data){
                        if (data=="succ") {
                           	openalert("删除成功！");
                           	if (authorityCommon.isSetAccount != 1) {
                        		authorityCommon.getOrgTree(); //刷新
                        	}
                        	else {
                        		authorityCommon.getAccountTree(); //刷新
                        	}
                           	
                           	var ids = f_str.split(",");
                           	for (var i=0;i<ids.length;i++) {
                           		//移除
                               	$("#"+ids[i]).remove();
                           	}
                           	
                        }else {
                            openalert("删除出错，请重新尝试或与管理员联系。");
                        }
                    },
                    error: function() {
                        openalert("执行操作出错，请重新尝试或与管理员联系。");
                    }
                });
            }
        });
	}
}


//同步读取当前节点的字段
function readField(treeid,tableType) {
    $.ajax({
        async: false,
        url: "getFieldAdvanced.action",
        type: 'post',
        dataType: 'script',
        data: {treeid: treeid, tableType: tableType},
        success: function (data) {
            if (data != "error") {
                field = eval(fields);
                talbeField(field); //字段
            } else {
                openalert('读取数据时出错，请尝试重新操作或与管理员联系!');
            }
        }
    });
}

//表字段
function talbeField(tableField){
	var html = "";
	if (tableField.length > 0) {
        for (var i=0;i<tableField.length;i++) {
        	html += "<option value=\""+tableField[i].englishname+"\">"+tableField[i].chinesename+"</option>";
        }
    }
    $("#selectField").html(html);
}

/**
* 档案节点的【帐户】访问权限 / 档案节点电子全文的[帐户]权限
*/    
function accountoftree(accountid){
	authorityCommon.getAccountTree();
	$("#accountoftree").jstree({
	    "json_data" : {
			"ajax" : {
				"url" : "loadAccountOfTreeData.action?accountid="+accountid,
				"data" : function (n) { 
					return { parentid : n.attr ? n.attr("id") : 0 };
				}
			}
		},
		"plugins" : [ "themes","json_data","ui","crrm","checkbox","types"],
		"core" : {
			"initially_open" : [ "2" ]
		},
		"types" : {
			"valid_children" : [ "root" ],
			"types" : {
				//设置rel=root的参数
				"root" : {
					//图标
					"icon" : { 
						"image" : "../../images/icons/house.png"
					},
					"valid_children" : [ "folder","default" ],
					"max_depth" : 2,
					"hover_node" : false,
					"select_node" : function () {return false;}
	
				},
				"default" : {
					"valid_children" : [ "none" ],
					"icon" : { 
						"image" : "../../images/icons/page.png" 
					}
				},
				"folder" : {
					"valid_children" : [ "default", "folder" ],
					"icon" : {
						"image" : "../../images/icons/folder.png"
					}
				}
			}
		}
		
	})
	.bind("select_node.jstree",function(e,data) { 
	    var node = data.rslt.obj, inst = data.inst;
	    rel = node.attr('rel');//判断文件还是文件夹
	    authorityCommon.accountTreeid = "";
       	$("#account_filescan").attr("disabled", true);
		$("#account_filedown").attr("disabled", true);
		$("#account_fileprint").attr("disabled", true);
		$("#account_filescan").attr("checked", false); 
		$("#account_filedown").attr("checked", false); 
		$("#account_fileprint").attr("checked", false);
		$(".bu").addClass("disabled");
		$(".bu").attr("disabled","true"); 
       	if (authorityCommon.accountTree.length > 0) {
       		for (var i=0;i<authorityCommon.accountTree.length;i++) {
       			if (authorityCommon.accountTree[i].treeid == node.attr('id') && authorityCommon.accountTree[i].accountid == authorityCommon.accountid) {
       				authorityCommon.accountTreeid = authorityCommon.accountTree[i].accountTreeId;
       				authorityCommon.selectTree = authorityCommon.accountTree[i];
       				authorityCommon.isSetAccount = 1;
       				$("#account_filescan").removeAttr("disabled");
       				$("#account_filedown").removeAttr("disabled");
       				$("#account_fileprint").removeAttr("disabled");
       				$(".bu").removeClass("disabled");
       				$(".bu").removeAttr("disabled"); 
       				if (authorityCommon.accountTree[i].filescan == 1) {
       					$("#account_filescan").attr("checked",'true');
       				}
       				if (authorityCommon.accountTree[i].filedown == 1) {
       					$("#account_filedown").attr("checked",'true');
       				}
       				if (authorityCommon.accountTree[i].fileprint == 1) {
       					$("#account_fileprint").attr("checked",'true');
       				}
       				
       				//获取选择的树节点档案类型，是A还是F／P
       				$.ajax({
       			        async: false,
       			        url: "getTempletType.action",
       			        type: 'post',
       			        dataType: 'text',
       			        data: {treeid: authorityCommon.selectTree.treeid},
       			        success: function (data) {
       			            if (data != "error") {
       			            	templettype = data;
       			            	if (templettype == 'F') {
       			            		$("#ajDataAuth").hide();
       			            	}
       			            	else {
       			            		$("#ajDataAuth").show();
       			            	}
       			            	
       			            	showDataAuth(authorityCommon.selectTree.filter);
       			            } else {
       			                openalert('读取数据时出错，请尝试重新操作或与管理员联系!');
       			            }
       			        }
       			    });
       				
       				break;
       			}
       		}
       	}
       	if (node.hasClass('jstree-closed')) { return inst.open_node(node); }
	    if (node.hasClass('jstree-open')) { return inst.close_node(node); }
	})
}
/**
*档案节点电子全文的[帐户组]权限 保存
*
*/
function updateOrgTree(){
	if (authorityCommon.orgTreeid == "") {
		return;
	}
	var filescan = 0;
	var filedown = 0;
	var fileprint = 0;
	if($("#filescan").attr("checked")== "checked") {
		filescan = 1;
	}
	if($("#filedown").attr("checked")== "checked") {
		filedown = 1;
	}
	if($("#fileprint").attr("checked")== "checked") {
		fileprint = 1;
	}
	//判断选中的树节点，是夹还是叶子。是叶子就只为叶子本身赋予全文权限。如果是夹，询问是否把夹下面所有有权限的都赋予相同的电子文件权限
	var prompt = '您选择的树节点是【文件夹】，确定要对该【文件夹】赋予相同的电子全文权限吗? <br><span style="color:red">说明：对【文件夹】设置电子全文权限，将赋予该【文件夹】下的所有有权限的节点都赋予相同的电子全文权限。</span>';
	if(rel=="folder"){ //文件夹
		bootbox.confirm(prompt, function(result) {
            if(result){
                var par = "orgTreeid=" + authorityCommon.orgTreeid + "&filescan=" + filescan + "&filedown=" + filedown + "&fileprint=" + fileprint;
				$.ajax({
				   type: "POST",
				   url: "updateOrgTree.action?"+par,
				   success: function(msg){
				   		if("succ"==msg){
				   			openalert("授权成功！");
				   			authorityCommon.getOrgTree(); //刷新
				   			//orgoftree(); //刷新
				   		}else{
				   			openalert("授权失败，请重新登录尝试或与管理员联系!");
				   		}
				   }
				});
            }
        });
	}else{
		var par = "orgTreeid=" + authorityCommon.orgTreeid + "&filescan=" + filescan + "&filedown=" + filedown + "&fileprint=" + fileprint;
		$.ajax({
		   type: "POST",
		   url: "updateOrgTree.action?"+par,
		   success: function(msg){
		   		if("succ"==msg){
		   			openalert("授权成功！");
		   			authorityCommon.getOrgTree(); //刷新
		   			//orgoftree(); //刷新
		   		}else{
		   			openalert("授权失败，请重新登录尝试或与管理员联系!");
		   		}
		   }
		});
	}
}
/**
*档案节点电子全文的[账户]权限 保存
*
*/
function updateAccountTree(){
	if (authorityCommon.accountTreeid == "") {
		return;
	}
	var account_filescan = 0;
	var account_filedown = 0;
	var account_fileprint = 0;
	if($("#account_filescan").attr("checked")== "checked") {
		account_filescan = 1;
	}
	if($("#account_filedown").attr("checked")== "checked") {
		account_filedown = 1;
	}
	if($("#account_fileprint").attr("checked")== "checked") {
		account_fileprint = 1;
	}
	//判断选中的树节点，是夹还是叶子。是叶子就只为叶子本身赋予全文权限。如果是夹，询问是否把夹下面所有有权限的都赋予相同的电子文件权限
	var prompt = '您选择的树节点是【文件夹】，确定要对该【文件夹】赋予相同的电子全文权限吗? <br><span style="color:red">说明：对【文件夹】设置电子全文权限，将赋予该【文件夹】下的所有有权限的节点都赋予相同的电子全文权限。</span>';
	if(rel=="folder"){ //文件夹
		bootbox.confirm(prompt, function(result) {
            if(result){
                var par = "accountTreeid=" + authorityCommon.accountTreeid + "&filescan=" + account_filescan + "&filedown=" + account_filedown + "&fileprint=" + account_fileprint;
				$.ajax({
				   type: "POST",
				   url: "updateAccountTree.action?"+par,
				   success: function(msg){
				   		if("succ"==msg){
				   			openalert("授权成功！");
				   			authorityCommon.getAccountTree(); //刷新
				   		}else{
				   			openalert("授权失败，请重新登录尝试或与管理员联系!");
				   		}
				   }
				});
            }
        });
	}else{
		var par = "accountTreeid=" + authorityCommon.accountTreeid + "&filescan=" + account_filescan + "&filedown=" + account_filedown + "&fileprint=" + account_fileprint;
		$.ajax({
		   type: "POST",
		   url: "updateAccountTree.action?"+par,
		   success: function(msg){
		   		if("succ"==msg){
		   			openalert("授权成功！");
		   			authorityCommon.getAccountTree(); //刷新
		   		}else{
		   			openalert("授权失败，请重新登录尝试或与管理员联系!");
		   		}
		   }
		});
	}
}