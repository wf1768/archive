/*
 * 参数设置
 * guodh
 * 2013/03/12
*/
$(function(){
	configList();
});
//config列表
function configList(){
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
			tmp += "<td><a href=\"javascript::\" onclick=\"upConfig('"+configList[i].configid+"','"+configList[i].configvalue+"')\">"+configList[i].configvalue+"</a></td>";
			tmp += "<td>"+configList[i].configmemo+"</td>";
			tmp += "</tr>";
			tr += tmp;
		}
	}else{
		tr = "";
	}
	return tr;
}
//修改属性值
function upConfig(id,value){
	$('#configid').val(id);
	$('#configvalue').val(value);
	$('#upInfo').modal('show');
}
function upConf(){
	var configid = $('#configid').val();
	var configvalue = $('#configvalue').val();
	$.ajax({
	   type: "POST",
	   url: "saveConfig.action",
	   data: "sysConfig.configid="+configid+"&sysConfig.configvalue="+configvalue,
	   success: function(msg){
			if("succ"==msg){
				$('#upInfo').modal('hide');
				openalert("修改成功！");
				configList();//刷新
			}else{
				openalert("修改失败！");
			}
	   }
	});
}
