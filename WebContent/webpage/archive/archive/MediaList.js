var tableField; //表字段
var selectMediaid;
$(function(){
    $('#grid_header_media').html(archiveCommon.selectTreeName + "_多媒体文件管理");
	readData();
	var tableField = readField();
});

//同步读取当前节点的原始数据
function readData() {
    $.ajax({
        async: false,
        url: "listForMediaArchive.action",
        type: 'post',
        dataType: 'json',
        data: {treeid: archiveCommon.selectTreeid, tableType: '01'},
        success: function (data) {
            if (data != "error") {
                data = eval(data);
                showData(data);
            } else {
                us.openalert('读取数据时出错，请尝试重新操作或与管理员联系! ', '系统提示', 'alertbody alert_Information');
            }
        }
    });
}
//同步读取当前节点的字段
function readField() {
    $.ajax({
        async: false,
        url: "getFieldList.action",
        type: 'post',
        dataType: 'json',
        data: {treeid: archiveCommon.selectTreeid, tableType: '01'},
        success: function (data) {
            if (data != "error") {
                tableField = eval(data);
            } else {
                openalert('读取数据时出错，请尝试重新操作或与管理员联系!');
            }
        }
    });
}
//显示多媒体数据
function showData(data) {
	var html = "";
	for (var i = 0; i < data.length; i++) {
	 	var imgsrc = "";
	    if (data[i].SLT == "") {
	        imgsrc = "../../media/no_photo_135.png";
	    }else {
	        imgsrc = "../../media/" + data[i].SLT;
	    }
     	html +='<li class="span3">';
		html +='	<div class="thumbnail">';
		html +='		<a class="thumbnail" href="javascript::" onclick="showMediaWjTab(\''+data[i].ID+'\')">';
		html +='			<img class="imgSize" src="'+imgsrc+'">';
		html +='		</a>';
		html +='		<div class="actions">';
		html +='			<a href="javascript::" onclick="getMedia(\''+data[i].ID+'\')" title="修改"><i class="icon-pencil icon-white"></i></a>';
		html +='			<a href="javascript::" onclick="deleteMedia(\''+data[i].ID+'\')" title="删除"><i class="icon-remove icon-white"></i></a>';
		html +='   			<a href="javascript::" onclick="showMediaWjTab(\''+data[i].ID+'\')" title="查看"><i class="icon-search icon-white"></i></a>';
		html +='		</div>';
		html +='		<div class="titleN">题名：'+data[i].TM+'</div>';
		html +='	</div>';
		html +='</li>';
	}
	$('#meidadiv').html(html);
}

/**
 * 【添加、修改】处理动态字段，创建dialog里的html
 */
function addOrUpdInfoHtml(field) {
    var html = "<form class=\"form-horizontal\">";
    for (var i = 0; i < field.length; i++) {
        if (field[i].isgridshow == 1) {
            html += "<div class=\"control-group\" id=\"" + field[i].englishname + "div\">";
            html += "<label class=\"control-label\" for=\"" + field[i].englishname + "\">" + field[i].chinesename + "</label>";
            html += "<div class=\"controls\">";
            //弄有代码的
            if (field[i].iscode == 1) {
                var code;
                $.ajax({
                    async: false,
                    url: "getFieldCode.action",
                    type: 'post',
                    dataType: 'json',
                    data: {templetfieldid: field[i].templetfieldid},
                    success: function (data) {
                        if (data != "error") {
                            code = eval(data);
                        } else {
                        }
                    }
                });
                html += "<select id='" + field[i].englishname + "'>";
                for (var j = 0; j < code.length; j++) {
                    html += "<option value='" + code[j].columnname + "'>" + code[j].columndata + "</option>";
                }
                html += "</select>";
            }else {
                if (field[i].fieldtype == "INT") {
                    html += "<input type=\"text\" id=\"" + field[i].englishname + "\" value=\"0\">";
                }else {
                    html += "<input type=\"text\" id=\"" + field[i].englishname + "\" value=\"\" >";
                }
            }
            html += "</div>";
            html += "</div>";
        }
    }
	html += "</form>";
    return html;
}
/**
 * 【添加、修改】 相册 (案卷级)-提交参数JSON copy的还没仔细看
 */
function save(id) {
    var items = [];
    var item = {};
    item.id = id;
    item.isdoc = 0;
    item.treeid = archiveCommon.selectTreeid;
    item.slt = '';//no_photo_135.png
    for (var i = 0; i < tableField.length; i++) {
        var field = tableField[i];
        if (field.isgridshow == 1) {
            $('#' + field.englishname + "div").removeClass("error");
            if (null != $('#' + field.englishname).val() || "" != $('#' + field.englishname).val()) {
                if (field.fieldtype == "INT") {
                    //判断值是否正常
                    var isnum = isNaN($('#' + field.englishname).val());
                    if (isnum) {
                        us.openalert('请填写正确的数字类型! ', '系统提示', 'alertbody alert_Information');
                        $('#' + field.englishname).focus();
                        $('#' + field.englishname + "div").addClass("error");
                        return;
                    }
                }
                item[field.englishname.toLowerCase()] = $('#' + field.englishname).val();
            }else {
                if (field.fieldtype == "INT") {
                    item[field.englishname.toLowerCase()] = '0';
                }else {
                    item[field.englishname.toLowerCase()] = '';
                }
            }
        }
    }
    items.push(item);
    return items;
}

/**
 * 添加 相册 (案卷级)
 */
function addMedia(){
	var html = addOrUpdInfoHtml(tableField);
	$("#addMediaDialog").html(html);
	$("#center").css("z-index",""); //解决modal弹出和jquery.layout的冲突 费劲啊
	$("#addMedia").modal('show');
}
/**
 * 添加 相册 (案卷级)-保存
 */
function insert(){
	var id = UUID.prototype.createUUID();
	var items = save(id);
	$.ajax({
	     async: false,
	     url: "saveImportArchive.action",
	     type: 'post',
	     dataType: 'text',
	     data: {importData: JSON.stringify(items), tableType: '01'},
	     success: function (data) {
	     	$("#addMedia").modal('hide');
        	openalert(data);
        	readData();
	     }
	});
}
/**
 * 删除 相册 (案卷级)
 */
function deleteMedia(id){
	bootbox.confirm("确定要删除吗？<div style=\"color: red;\">注意：删除案卷将会把该案卷下的所有文件一同删除，本操作不可恢复，请谨慎操作！</div>", function(result) {
	    if(result){
	    	$.ajax({
			    async: false,
			    url: "deleteImportArchive.action",
			    type: 'post',
			    dataType: 'text',
			    data: {treeid: archiveCommon.selectTreeid,id:id, tableType: '01'},
			    success: function (data) {
			        if (data == "succ") {
			           openalert("删除成功!");
			           readData();
			        } else {
			           openalert('删除失败，请尝试重新操作或与管理员联系! ');
			        }
			    }
			});
	    }
	});
}
/**
 * 修改 相册 (案卷级)-查找
 */
function getMedia(id){
	$.ajax({
	    async: false,
	    url: "getImportArchive.action",
	    type: 'post',
	    dataType: 'json',
	    data: {treeid: archiveCommon.selectTreeid,id:id, tableType: '01'},
	    success: function (data) {
	        if (data != "error") {
	            tableFieldValue = eval(data);
	            var html = addOrUpdInfoHtml(tableField);
	            $("#addMediaDialog").html("");//把添加的内容清空，否则ID有冲突
	            $("#updMediaDialog").html(html);
	            $("#ID").val(id); //用于Update
	            for(var i=0;i<tableField.length;i++){ 
	            	var v = tableField[i].englishname;
	            	$("#"+v).val(tableFieldValue[0][v]);//赋值
	            }
				$("#center").css("z-index",""); //解决modal弹出和jquery.layout的冲突 费劲啊
				$("#updMedia").modal('show');
	        } else {
	            us.openalert('读取数据时出错，请尝试重新操作或与管理员联系! ', '系统提示', 'alertbody alert_Information');
	        }
	    }
	});
}
/**
 * 修改 相册 (案卷级)-保存
 */
function update(){
	var id = $("#ID").val();
	var items = save(id);
	$.ajax({
	     async: false,
	     url: "updateImportArchive.action",
	     type: 'post',
	     dataType: 'text',
	     data: {importData: JSON.stringify(items), tableType: '01'},
	     success: function (data) {
	     	$("#updMedia").modal('hide');
        	openalert(data);
        	readData();
	     }
	});
}
/**
 * 查看 文件
 */
function showMediaWjTab(id) {
    selectMediaid = id;
    url = "dispatch.action?page=/webpage/archive/archive/MediaWjList.html";
    us.addtab($('#mediawj'), '多媒体--文件管理', 'ajax', url);
}