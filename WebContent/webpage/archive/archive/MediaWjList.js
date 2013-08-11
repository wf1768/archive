var tableMediaWjField; //表字段
var tableMediaValue; //字段值
//jQuery.fn.limit=function(){ 
//    var self = $("div[limit]"); 
//    self.each(function(){ 
//        var objString = $(this).text(); 
//        var objLength = $(this).text().length; 
//        var num = $(this).attr("limit"); 
//        if(objLength > num){ 
//		$(this).attr("title",objString); 
//            objString = $(this).text(objString.substring(0,num) + "..."); 
//        } 
//    }) 
//}
$(function(){
//    $('#grid_header_mediawj').html(archiveCommon.selectTreeName + "_多媒体管理");
//    
//	readMediaWjData();
//	uploadmmedia(); //上传控件加载
//	var tableMediaWjField = readMediaWjField();
//	
//	$(document.body).limit(); 
	
});

//同步读取当前节点的原始数据
function readMediaWjData() {
    $('#grid_header_mediawj').html(archiveCommon.selectTreeName + "_多媒体管理");
    $.ajax({
        async: false,
        url: "listForMediaWjArchive.action",
        type: 'post',
        dataType: 'json',
        data: {treeid: archiveCommon.selectTreeid,parentid:selectMediaid, tableType: '02'},
        success: function (data) {
            if (data != "error") {
                data = eval(data);
                tableMediaValue = data;
                showMediaWjData(data);
            } else {
                openalert('读取数据时出错，请尝试重新操作或与管理员联系! ');
            }
        }
    });
    uploadmmedia(); //上传控件加载
    var tableMediaWjField = readMediaWjField();

    $(document.body).limit();
}
//同步读取当前节点的字段
function readMediaWjField() {
    $.ajax({
        async: false,
        url: "getFieldList.action",
        type: 'post',
        dataType: 'json',
        data: {treeid: archiveCommon.selectTreeid, tableType: '02'},
        success: function (data) {
            if (data != "error") {
                tableMediaWjField = eval(data);
            } else {
                openalert('读取数据时出错，请尝试重新操作或与管理员联系!');
            }
        }
    });
}
//显示多媒体数据
function showMediaWjData(data) {
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
		html +='		<a class="thumbnail" href="javascript:;" >';
		html +='			<img class="imgSize" src="'+imgsrc+'">';
		html +='		</a>';
		html +='		<div class="actions">';
		html +='			<a href="javascript:;" onclick="setCoverImg(\''+data[i].SLT+'\')" title="设为封面"><i class="icon-eye-open icon-white"></i></a>';
		html +='			<a href="javascript:;" onclick="createWjInfo(\''+data[i].ID+'\')" title="修改"><i class="icon-pencil icon-white"></i></a>';
		html +='			<a href="javascript:;" onclick="deleteMediaWj(\''+data[i].ID+'\',\''+data[i].SLT+'\')" title="删除"><i class="icon-remove icon-white"></i></a>';
		html +='		</div>';
		html +='		<div limit="18" class="titleN">题名：'+data[i].TM+'</div>';
		html +='	</div>';
		html +='</li>';
	}
	$('#meidawjdiv').html(html);
}

//声明上传控件。#uploadFile，作为公共的资源，在archiveMgr.js里
function uploadmmedia(){
	$("#uploadFile").dialog({
	    autoOpen: false,
	    height: 460,
	    width: 630,
	    modal: true,
	    buttons: {
	        '关闭': function() {
	            var par = "selectRowid="+ archiveCommon.selectRowid + "&tableid="+archiveCommon.selectTableid;
	            var rowList = [];
	            //同步读取数据
	            $.ajax({
	                async : false,
	                url : "listLinkDoc.action?"+ par,
	                type : 'post',
	                dataType : 'script',
	                success : function(data) {
	                    if (data != "error") {
	                        rowList = eval(data);
	                        $("#doclist").html("");
	                        for (var i=0;i<rowList.length;i++) {
	                            $("#doclist").append(getDoclist(rowList[i]));
	                        }
	                    } else {
	                        us.openalert('读取数据时出错，请尝试重新操作或与管理员联系! ','系统提示','alertbody alert_Information');
	                    }
	                }
	            });
	            $("#docwindows").dialog('option', 'title', '电子全文列表--(共 ' + rowList.length + "个文件)");
	            $( this ).dialog( "close" );
	        }
	    }
	})
}

function openUploadMedia() {
    var url = 'uploadMedia.action?parentid=' + selectMediaid + '&tableType=02&treeid=' + archiveCommon.selectTreeid;
    us.upload_multi(url,'readMediaWjField');
}

function openUploadMedia2() {
     $("#uploader").pluploadQueue({
         // General settings
         runtimes : 'flash,html5,html4',
         url : 'uploadMedia.action?parentid=' + selectMediaid + '&tableType=02&treeid=' + archiveCommon.selectTreeid,
         max_file_size : '200mb',
         //缩略图形式。
     	//resize : {width :32, height : 32, quality : 90},
     	//unique_names : true,
         chunk_size: '2mb',
         filters : [
             {title : "Image files", extensions : "jpg,gif,png,bmp,tif"},
             {title : "Video files", extensions : "mp4,avi,flv"}
         ],
         // Flash settings
         flash_swf_url : '../../js/plupload/js/plupload.flash.swf'
         // Silverlight settings
		//silverlight_xap_url : '/example/plupload/js/plupload.silverlight.xap'
     });
     $('#formId').submit(function(e) {
         var uploader = $('#uploader').pluploadQueue();
         if (uploader.files.length > 0) {
             // When all files are uploaded submit form
             uploader.bind('StateChanged', function() {
                 if (uploader.files.length === (uploader.total.uploaded + uploader.total.failed)) {
                     $('#formId')[0].submit();
                 }
             });
             uploader.start();
         } else {
             openalert('请先上传数据文件! ');
         }
         return false;
     });
     $( "#uploadFile" ).dialog( "open");
}


/**
 * 修改 相册 (文件)
 */
var editRowid = "";
var editRow = {};
function createWjInfo(id) {
    var row;
    var num = -1;
    if (id != "") {
        for (var i=0;i<tableMediaValue.length;i++) {
            if (id == tableMediaValue[i].ID) {
                editRow = tableMediaValue[i];
                row = editRow;
                num = i;
                editRowid = row.ID;
                break;
            }
        }
    }else {
        row = "";
        num = "";
    }
    var html = "<form class=\"form-horizontal\">";
    //处理动态字段，创建dialog里的添加html
    if (tableMediaWjField.length > 0) {
        for (var i=0;i<tableMediaWjField.length;i++) {
            if (tableMediaWjField[i].isgridshow == 1) {
                html += "<div class=\"control-group\" id=\""+tableMediaWjField[i].englishname+"div\">";
                html += "<label class=\"control-label\"' for=\""+tableMediaWjField[i].englishname+"\">"+tableMediaWjField[i].chinesename+"</label>";
                html += "<div class=\"controls\"'>";
                //弄有代码的
                if (tableMediaWjField[i].iscode == 1) {
                    var code;
                    $.ajax({
                        async : false,
                        url : "getFieldCode.action",
                        type : 'post',
                        dataType : 'json',
                        data:{templetfieldid:tableMediaWjField[i].templetfieldid},
                        success : function(data) {
                            if (data != "error") {
                                code = eval(data);
                            } else {
                            }
                        }
                    });
                    html += "<select id='" + tableMediaWjField[i].englishname +"'>";
                    for (var j=0;j<code.length;j++) {
                        html += "<option value='"+code[j].columnname+"'>"+code[j].columndata+"</option>";
                    }
                    html += "</select>";
                }
                else {
                    if (tableMediaWjField[i].fieldtype == "INT") {
                        html += "<input type=\"text\" id=\""+tableMediaWjField[i].englishname+"\" value=\"0\">";
                    }else {
                    	if(tableMediaWjField[i].englishname == "GDRQ"){
                    		html += '<input type="text" readonly="readonly" value=\""+row[tableMediaWjField[i].englishname]+"\" id="GDRQ">';
	                	}else{
	                    	html += "<input type=\"text\" id=\""+tableMediaWjField[i].englishname+"\" value=\""+row[tableMediaWjField[i].englishname]+"\">";
	                    }
                    }

                }

                html += "</div>";
                html += "</div>";
            }
        }
    }
    html += "</form>";
    $("#updMediaDialog").html("");//把修改的内容清空，否则ID有冲突 静态的就是费劲啊
    $("#updMediaWjDialog").html(html);
    $("#center").css("z-index",""); //解决modal弹出和jquery.layout的冲突 费劲啊
	$("#updMediaWj").modal('show');
	
	//时间控件
	$('#GDRQ').datepicker().on('changeDate', function(ev) {
        $(this).datepicker('hide')
    });
    $('#GDRQ').datepicker().on('show', function(ev) {
        $('.modal').css('z-index','9999');
        $('.datepicker').css('z-index','99999');
    });    
}

function updateMediaWj() {
	$("#updMediaDialog").html("");//把修改的内容清空，否则ID有冲突 静态的就是费劲啊
    var items = [];
    var item = {};
    item.id= editRow.ID;
    item.isdoc = 0;
    item.treeid = editRow.TREEID;
    item.slt = editRow.SLT;
    item.parentid = editRow.PARENTID;
    item.IMAGEPATH = "";
    for (var i=0;i<tableMediaWjField.length;i++) {
        var field = tableMediaWjField[i];
        if (field.isgridshow == 1) {
            $('#' + field.englishname + "div").removeClass("error");
            if (null != $('#' + field.englishname).val() || "" != $('#' + field.englishname).val()) {
                if (field.fieldtype == "INT") {
                    //判断值是否正常
                    var isnum = isNaN($('#' + field.englishname).val());
                    if (isnum) {
                        us.openalert('请填写正确的数字类型! ','系统提示','alertbody alert_Information');
                        $('#' + field.englishname).focus();
                        $('#' + field.englishname + "div").addClass("error");
                        return;
                    }
                }
                item[field.englishname.toLowerCase()] = $('#' + field.englishname).val();
            }
            else {
                if (field.fieldtype == "INT") {
                    item[field.englishname.toLowerCase()] = '0';
                }
                else {
                    item[field.englishname.toLowerCase()] = '';
                }

            }
        }
    }
    items.push(item);
    $.ajax({
        async : false,
        url : "updateImportArchive.action",
        type : 'post',
        dataType : 'text',
        data:{importData:JSON.stringify(items),tableType:'02'},
        success : function(data) {
        	$("#updMediaWj").modal('hide'); 
        	readMediaWjData();
            openalert(data);
        }
    });
}
/**
 * 删除
 */
function deleteMediaWj(id,slt){
	bootbox.confirm("确定要删除吗？<div style=\"color: red;\">注意：删除将会把该文件的图片一同删除，本操作不可恢复，请谨慎操作！</div>", function(result) {
	    if(result){
	    	$.ajax({
			    async: false,
			    url: "deleteImportArchiveF.action",
			    type: 'post',
			    dataType: 'text',
			    data: {treeid: archiveCommon.selectTreeid,id:id,sltPic:slt, tableType: '02'},
			    success: function (data) {
			        if (data == "succ") {
			           openalert("删除成功!");
			           readMediaWjData();
			        } else {
			           openalert('删除失败，请尝试重新操作或与管理员联系! ');
			        }
			    }
			});
	    }
	});
}

/**
 * 设置封页图片
 */
function setCoverImg(slt){
	$.ajax({
	     async: false,
	     url: "setCoverPic.action",
	     type: 'post',
	     dataType: 'text',
	     data: {treeid: archiveCommon.selectTreeid,sltPic:slt,id:selectMediaid ,tableType: '01'},
	     success: function (data) {
	     	if(data=="succ"){
	     		openalert("封面设置成功！");
	     	}else{
	     		openalert("设置失败！");
	     	}
	     }
	});
}
/**
 * 预览
 */
function openMediaImg() {
	//初始化多媒体文件信息dialog
	$("#showMediaImg").dialog({
	    title:'多媒体文件浏览',
	    autoOpen: false,
	    height: 470,
	    width: 860,
	    modal: true
	});
	$("#selectTreeid").val(archiveCommon.selectTreeid);;
	$("#selectid").val(selectMediaid);
	$('#ifr').attr('src','slideshow/test.html');
	$( "#showMediaImg" ).dialog( "open");
}
/**
 * 刷新
 */
function refreshWj(){
	readMediaWjData();
}