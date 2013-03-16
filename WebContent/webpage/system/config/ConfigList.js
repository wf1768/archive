/*
 * 参数设置
 * guodh
 * 2013/03/12
*/
$(function(){
	$.ajax({
		async : false,
		url : "listConfig.action",
		type : 'post',
		dataType : 'script',
		success : function(data) {
			if (data != "error") {
				var content = createSysConfigTable(sysList);
				$("#configInfo").html(content);
			} else {
				alert("error");
			}
		},
		error:function(){
			window.location.href="../../common/logout.jsp";
		}
	});
	
	//修改
	$("#upd_conf").click(function(){
		var checks = "";
		$("[name='conf_box']").each(function(){
	        if($(this).attr("checked") == "checked"){
	            checks += $(this).val() + "";
	            alert($(this).val());
	        }
	    });
	    
	    if(checks == ""){
	    	openalert("请选择一条记录！");
	    }else{
	    	$.ajax({
				url : "getConfig.action",
				type : 'post',
				data: "par="+checks,
				success : function(data) {
					if (data != "error") {
						alert(data);						
					} else {
						alert("error");
					}
				}
			});
	    }
	});
});

//创建参数设置Table
function createSysConfigTable(configList){
	var table = "<table class=\"table table-striped table-bordered\">";
	table += "<thead>";
	table += "<tr>";
	table += "<th>#</th>";
	table += "<th>属性名称</th>";
	table += "<th>属性值</th>";
	table += "<th>属性描述</th>";
	table += "</tr>";
	table += "</thead>";
	tr = createSysConfig(configList);
	table += tr;
	table += "</table>";
	return table;
}
function createSysConfig(configList){
	var tr = "";
	if (configList.length > 0) {
		for (var i=0;i<configList.length;i++) {
			//alert(configList[i].configid+'/'+sysList[i].configname);
			tmp = "<tr>";
			//tmp += "<td><input name=\"conf_box\" type=\"checkbox\" value=\""+configList[i].configid+"\"/></td>";
			tmp += "<td>"+configList[i].configid+"</td>";
			tmp += "<td>"+configList[i].configname+"</td>";
			tmp += "<td><a href=\"\" id=>"+configList[i].configvalue+"</a></td>";
			tmp += "<td>"+configList[i].configmemo+"</td>";
			tmp += "</tr>";
			tr += tmp;
		}
	}else{
		tr = "";
	}
	return tr;
}


