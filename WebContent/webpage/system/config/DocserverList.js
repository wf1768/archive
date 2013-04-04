$(function(){
	//列表
	docserverList();
   	
	//添加
	$("#addDocS").click(function(){
		$("#docserverList").html(docform(Object));
		$('#docform').validation(); //表单验证
		$('#docform').clearForm(); //表单清除
		// 绑定表单提交事件处理器
		$('#docform').submit(function() {
		    // 提交表单
		    var options = {
			    url: 'saveDocserver.action',
			    type:'post',
			    success: function(data) {
			      if(data=="succ"){
			      	openalert("添加成功！");
			      	docserverList();//刷新列表
			      }else{
			      	openalert("添加失败！");
			      }
			    } 
			};
		    $(this).ajaxSubmit(options);
		    // 为了防止普通浏览器进行表单提交和产生页面导航（防止页面刷新？）返回false
		    return false;
	   	});
		
		/*
		   // 将options传给ajaxForm  有问题，每次只能执行一次，要刷新一下才行，不知为什么
		$('#docform').ajaxForm(options);
		*/
	});
	
	//删除
	$("#delDocS").click(function(){
		var f_str = "";
		$("input[name='docserverid']").each(function(){
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
	                    url:"delDocserver.action",
	                    data: "par=" + f_str,
	                    success: function(data){
	                        if (data=="succ") {
	                           	openalert("删除成功！");
	                           	docserverList();//刷新列表
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
//文件服务器列表
function docserverList(){
	$.ajax({
		async : false,
		url : "listDocserver.action",
		type : 'post',
		dataType : 'script',
		success : function(data) {
			if (data != "error") {
				var content = createDocserverTable(docList);
				$("#docserverList").html(content);
			} else {
				alert("error");
			}
		}
	});
}
//修改-得到一个docserver 修改
function getDocserver(docserverid){
	$.ajax({
		async : false,
		url : "getDocserver.action?docserverid="+docserverid,
		type : 'post',
		dataType : 'script',
		success : function(data) {
			if (data != "error") {
				docserver = eval(sysDocserver);//sysDocserver返回的对象
				dochtml = docform(docserver); 
				$("#docserverList").html(dochtml);
				$('#docform').validation(); //表单验证
			} else {
				alert("error");
			}
		}
	});
	$('#docform').submit(function() {
		var options = {
		    url: 'updDocserver.action',
		    type:'post',
		    success: function(data) {
		      if(data=="succ"){
		      	docserverList();//刷新列表
		      	openalert("修改成功！");
		      }else{
		      	openalert("修改失败！");
		      }
		    }
		};
	 	$(this).ajaxSubmit(options);
	    // 为了防止普通浏览器进行表单提交和产生页面导航（防止页面刷新？）返回false
	    return false;
	 });
}

//创建文件服务器Table
function createDocserverTable(docList){
	var table = "<table class=\"table table-striped table-bordered\">";
	table += "<thead>";
	table += "<tr>";
	table += "<th>#</th>";
	table += "<th>服务器名称</th>";
	table += "<th>服务器IP</th>";
	table += "<th>Ftp账户名称</th>";
	table += "<th>Ftp密码</th>";
	table += "<th>Ftp端口</th>";
	table += "<th>服务器路径</th>";
	table += "<th>类型</th>";
	table += "<th>状态</th>";
	table += "<th>服务器描述</th>";
	table += "<th>操作</th>";
	table += "</tr>";
	table += "</thead>";
	tr = createDocserver(docList);
	table += tr;
	table += "</table>";
	return table;
}
function createDocserver(docList){
	var tr = "";
	if (docList.length > 0) {
		for (var i=0;i<docList.length;i++) {
			tmp = "<tr>";
			tmp += "<td><input name=\"docserverid\" type=\"checkbox\" value=\""+docList[i].docserverid+"\"/></td>";
			tmp += "<td>"+docList[i].servername+"</td>";
			tmp += "<td>"+docList[i].serverip+"</td>";
			tmp += "<td>"+docList[i].ftpuser+"</td>";
			tmp += "<td>"+docList[i].ftppassword+"</td>";
			tmp += "<td>"+docList[i].serverport+"</td>";
			tmp += "<td>"+docList[i].serverpath+"</td>";
			tmp += "<td>"+docList[i].servertype+"</td>";
			tmp += "<td><a href=\"javascript::\" onclick=\"serverState('"+docList[i].docserverid+"')\">"+docList[i].serverstate+"</a></td>";
			tmp += "<td>"+docList[i].servermemo+"</td>";
			tmp += "<td><a href=\"javascript::\" onclick=\"getDocserver('"+docList[i].docserverid+"')\">修改</a>&nbsp;<a href=\"javascript::\" onclick=\"checkFtp('"+docList[i].docserverid+"')\">测试</a></td>";
			tmp += "</tr>";
			tr += tmp;
		}
	}else{
		tr = "";
	}
	return tr;
}
//测试服务器连接
function checkFtp(docserverid){
	$.ajax({
        type:"post",
        url:"testFtp.action",
        data: "docserverid=" + docserverid,
        success: function(data){
          		openalert(data);
        },
        error: function() {
            openalert("执行操作出错，请重新尝试或与管理员联系。");
        }
    });
}

//更改服务器状态(是否启用)serverState
function serverState(docserverid){
	$("#docserverid").val(docserverid);
	$("#serverState").modal('show');
}
function startDocServer(){
	var docserverid = $("#docserverid").val();
	$("#serverState").modal('hide');
	$.ajax({
	   type: "POST",
	   url: "setDocserverState.action",
	   data: "docserverid="+docserverid,
	   success: function(msg){
			if("succ"==msg){
				openalert("已成功启动！");
				docserverList();//刷新
			}else{
				openalert("启动失败！");
			}
	   }
	});
}

//添加、修改form
function docform(sysdocser){
	var html = '<form id="docform" class="form-horizontal">'+
   		'<legend>添加文件服务器</legend>'+
		'<input type="hidden" name="docserver.docserverid" value="'+sysdocser.docserverid+'">'+
		'<div class="control-group">'+
	        '<label class="control-label" for="servername">服务器名称</label>'+
	        '<div class="controls">'+
	            '<input type="text" name="docserver.servername" value="'+sysdocser.servername+'" check-type="required" required-message="请填写服务器名称！" >'+
	        '</div>'+
	    '</div>'+
	   	'<div class="control-group">'+
	      '<label for="serverip" class="control-label">服务器IP</label>'+
	      '<div class="controls">'+
	        '<input type="text" name="docserver.serverip" value="'+sysdocser.serverip+'">'+
	      '</div>'+
	    '</div>'+
	    '<div class="control-group">'+
	      '<label for="ftpuser" class="control-label">Ftp账户名称</label>'+
	      '<div class="controls">'+
	        '<input type="text" name="docserver.ftpuser" value="'+sysdocser.ftpuser+'">'+
	      '</div>'+
	    '</div>'+
	    '<div class="control-group">'+
	      '<label for="ftppassword" class="control-label">Ftp密码</label>'+
	      '<div class="controls">'+
	        '<input type="text" name="docserver.ftppassword" value="'+sysdocser.ftppassword+'">'+
	      '</div>'+
	    '</div>'+
	    '<div class="control-group">'+
	      '<label for="serverport" class="control-label">Ftp端口</label>'+
	      '<div class="controls">'+
	        '<input type="text" name="docserver.serverport" value="'+sysdocser.serverport+'">'+
	      '</div>'+
	    '</div>'+
	    '<div class="control-group">'+
	      '<label for="serverpath" class="control-label">服务器路径</label>'+
	      '<div class="controls">'+
	        '<input type="text" name="docserver.serverpath" value="'+sysdocser.serverpath+'">'+
	      '</div>'+
	    '</div>'+
	    '<div class="control-group">'+
	      '<label for="servertype" class="control-label">类型</label>'+
	      '<div class="controls">'+
	        '<input type="text" name="docserver.servertype" value="'+sysdocser.servertype+'">'+
	      '</div>'+
	    '</div>'+
	    '<div class="control-group">'+
	      '<label for="serverstate" class="control-label">状态</label>'+
	      '<div class="controls">'+
	        '<input type="text" name="docserver.serverstate" value="'+sysdocser.serverstate+'">'+
	      '</div>'+
	    '</div>'+
	    '<div class="control-group">'+
	      '<label for="servermemo" class="control-label">服务器描述</label>'+
	      '<div class="controls">'+
	        '<input type="text" name="docserver.servermemo" value="'+sysdocser.servermemo+'">'+
	      '</div>'+
	    '</div>'+
	    '<div class="control-group">'+
			'<div class="controls">'+
				'<button class="btn" type="reset">取 消</button>&nbsp;&nbsp;&nbsp;&nbsp;'+
				'<button class="btn btn-primary" type="submit">保 存</button>'+
			'</div>'+
		'</div>'+
   	'</form>';
   	return html;
}