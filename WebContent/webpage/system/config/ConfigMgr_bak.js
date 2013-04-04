var listCommon = null;

function openTab(name,ursl,icons) {
   	$.ajax({
		async : false,
		url : "listConfig.action",
		type : 'post',
		dataType : 'script',
		success : function(data) {
			if (data != "error") {
				var content = createSysConfigTable(sysList);
				$("#title").html(name);
				$("#configInfo").html(content);
				$("#paramset").addClass("active");
			} else {
				alert("error");
			}
		}
	});
}

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
			tmp += "<td>"+configList[i].configid+"</td>";
			tmp += "<td>"+configList[i].configname+"</td>";
			tmp += "<td>"+configList[i].configvalue+"</td>";
			tmp += "<td>"+configList[i].configmemo+"</td>";
			tmp += "</tr>";
			tr += tmp;
		}
	}else{
		tr = "";
	}
	return tr;
}
