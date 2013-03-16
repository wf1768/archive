/*
 * 角色管理
 * guodh
 * 2013/03/12
*/
$(function(){
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
});

//创建参数设置Table
function createRoleTable(roleList){
	var table = "<table class=\"table table-striped table-bordered\">";
	table += "<thead>";
	table += "<tr>";
	table += "<th>#</th>";
	table += "<th>角色名称</th>";
	table += "<th>角色描述</th>";
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
			tmp += "<td>"+roleList[i].roleid+"</td>";
			tmp += "<td>"+roleList[i].rolename+"</td>";
			tmp += "<td>"+roleList[i].rolememo+"</td>";
			tmp += "</tr>";
			tr += tmp;
		}
	}else{
		tr = "";
	}
	return tr;
}
function modal(){
	//listRoleFunction.action?roleid=' + row.roleid 
	//C553B2B9A4200001DDA3F0601D90D250
	$.ajax({
	   type: "POST",
	   url: "listRoleFunction.action",
	   data: "roleid=C553B2B9A4200001DDA3F0601D90D250",
	   success: function(msg){
	   	$("#Rcont").html(msg);
	     //alert( "Data Saved: " + msg );
	   }
	});
	$('#myModal').modal('show');
}
//bootbox.min.js
function setRole(){
	/*var result = "sdfsafsdf";
	bootbox.prompt("设定角色操作的功能", function(result) {
		if (result == null) {
			Example.show("Prompt dismissed");
		} else {
			Example.show("sssss");
		}
	});*/
	openalert("添加成功");
	
}

 
 
 
 // /////////////////////////////////////////////
 