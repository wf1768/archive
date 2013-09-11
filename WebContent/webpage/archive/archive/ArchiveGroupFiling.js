var filingGridconfig = new us.archive.ui.Gridconfig();

$(function() {

    filingGridconfig.tabletype = '01';
    filingGridconfig.init_grid(filingGridconfig,"#archiveGroupFilingDiv","","");

  //档案
	var resizeTimer = null;
	$('#dan').click(function(){
		var treeid = archiveCommon.selectTreeid;
		if(treeid!=''){
			if (resizeTimer) clearTimeout(resizeTimer);
			//需要延迟一会，要不然数据不显示
	        resizeTimer = setTimeout("show_archive_list()", 100);
		}
	});
	
})



//读取档案数据
function readArchiveData() {
    var par = "treeid=" + archiveCommon.selectTreeid + "&tableType=01"+"&status=0";
    $.ajax({
        async : false,
        url : "listArchiveF.action?" + par,
        type : 'post',
        dataType : 'script',
        success : function(data) {
            if (data != "error") {
                filingGridconfig.rows = rowList;

                filingGridconfig.dataView.setItems(filingGridconfig.rows);

                // 创建checkbox列
                var checkboxSelector = new Slick.CheckboxSelectColumn({
                    cssClass : "slick-cell-checkboxsel"
                });
                var visibleColumns = [];//定义一个数组存放显示的列
                visibleColumns.push(checkboxSelector.getColumnDefinition());//将columns的第一列push进去

//                // 加入其他列
                for ( var i = 0; i < filingGridconfig.columns_fields.length; i++) {
                    visibleColumns.push(filingGridconfig.columns_fields[i]);
                }

                //设置grid的列
                filingGridconfig.grid.setColumns(visibleColumns);

//                filingGridconfig.dataView.beginUpdate();
//                filingGridconfig.dataView.setItems(filingGridconfig.rows);
                filingGridconfig.dataView.setFilterArgs({
                    searchString : archiveCommon.searchString
                });
                filingGridconfig.dataView.setFilter(us.myFilter);
//                filingGridconfig.dataView.endUpdate();
                // 注册grid的checkbox功能插件
                filingGridconfig.grid.registerPlugin(checkboxSelector);


                //新建行时，将系统必须的默认值与字段默认值合并
                filingGridconfig.newItemTemplate = $.extend({},filingGridconfig.newItemTemplate,filingGridconfig.fieldsDefaultValue);
                filingGridconfig.grid.invalidate();
            } else {
                us.openalert('<span style="color:red">读取数据时出错.</span></br>请关闭浏览器，重新登录尝试或与管理员联系!',
                    '系统提示',
                    'alertbody alert_Information'
                );
            }
        }
    });
}

//档案
function show_archive_list() {
    //同步读取字段
    var par = "treeid=" + archiveCommon.selectTreeid + "&tableType=01&importType=0";
    $.ajax({
        async : false,
        url : "getField.action?" + par,
        type : 'post',
        dataType : 'script',
        success : function(data) {
            if (data != "error") {
                filingGridconfig.columns_fields = fields;
                filingGridconfig.fieldsDefaultValue = fieldsDefaultValue;
                filingGridconfig.selectTableid = tableid;
            } else {
                us.openalert('<span style="color:red">读取字段信息时出错.</span></br>请关闭浏览器，重新登录尝试或与管理员联系!',
                    '系统提示',
                    'alertbody alert_Information'
                );
            }
        }
    });
    readArchiveData();

    $("#grid_header_aj").html('<h3>'+archiveCommon.selectTreeName + '_案卷档案列表'+'</h3>');

}

////insert a new row
//function add() {
//    filingGridconfig.grid.setOptions({
//        autoEdit : true
//    });
//    filingGridconfig.grid.gotoCell(filingGridconfig.dataView.getLength(),4,true);
//}
////open update mode
//function update() {
//    filingGridconfig.grid.setOptions({
//        autoEdit : true
//    });
//}
////end update mode
//function endupdate() {
//    filingGridconfig.grid.setOptions({
//        autoEdit : false
//    });
//}
////delete rows
//function del() {
//
//    var selectRows = filingGridconfig.grid.getSelectedRows();
//    selectRows.sort(function compare(a, b) {
//        return b - a;
//    });
//
//    var deleteRows = [];
//
//    for ( var i = 0; i < selectRows.length; i++) {
//        var item = filingGridconfig.dataView.getItem(selectRows[i]);
//        if (item) {
//            deleteRows.push(item);
//        }
//    }
//
//    if (deleteRows.length > 0) {
//        bootbox.confirm("确定要删除选中的 <span style='color:red'>"+deleteRows.length+"</span> 条案卷记录吗?<br> <font color='red'>" +
//            "注意：删除案卷记录，将同时删除案卷及案卷下所有文件数据、电子全文，请谨慎操作！</font> ", function(result) {
//            if(result){
//                var par = "par=" + JSON.stringify(deleteRows) + "&tableType=01";
//
//                $.post("deleteArchive.action",par,function(data){
//                        if (data == "SUCCESS") {
//                            for ( var i = 0; i < selectRows.length; i++) {
//                                var item = filingGridconfig.dataView.getItem(selectRows[i]);
//                                filingGridconfig.dataView.deleteItem(item.id);
//                                filingGridconfig.grid.invalidate();
//                                filingGridconfig.grid.render();
//                            };
//                            readData();
////                            filingGridconfig.grid.invalidate();
////                            filingGridconfig.grid.render();
//                            us.openalert('删除成功。 ','系统提示','alertbody alert_Information');
////                            var data = [];
////                            readData();
////                            grid.invalidate();
////                            filingGridconfig.dataView.setItems(data);
////                            filingGridconfig.dataView.setItems(filingGridconfig.rows);
//                        }
//                        else {
//                            us.openalert(data,'系统提示','alertbody alert_Information');
//                        }
//                    }
//                );
//            }
//        })
//    }
//    else {
//        us.openalert('请选择要删除的数据。 ','系统提示','alertbody alert_Information');
//    }
//}
////open batchupdate dialog
//function batchupdate() {
//    var selectRows = filingGridconfig.grid.getSelectedRows();
//    selectRows.sort(function compare(a, b) {
//        return b - a;
//    });
//    if (selectRows.length > 0) {
//        $("#selectfield").empty();
//        for (var i=0;i<filingGridconfig.columns_fields.length;i++) {
//            if (filingGridconfig.columns_fields[i].id != "rownum" && filingGridconfig.columns_fields[i].id != "isdoc" && filingGridconfig.columns_fields[i].id != "files") {
//                $("#selectfield").append("<option value='"+filingGridconfig.columns_fields[i].id+"'>"+filingGridconfig.columns_fields[i].name+"</option>");
//            }
//        }
//        $("#updatetxt").val("");
//        //取消绑定保存按钮的click事情
//        $("#save_batch").unbind("click");
//        $('#save_batch').click(function() {
//            us.batchUpdate(filingGridconfig.grid,filingGridconfig.dataView,true,'01');
//        });
//        $('#batchwindows').modal('show');
//    }
//    else {
//        us.openalert('请选择要修改的数据.');
//    }
//}
////open import data dialog
//function importdata() {
////    showArchiveImportTab('01');
//    openImportWindows('01');
//}
////show filter div
//function filter() {
//    filingGridconfig.grid.setTopPanelVisibility(!filingGridconfig.grid.getOptions().showTopPanel);
////    if ($(filingGridconfig.grid.getTopPanel()).is(":visible")) {
////        filingGridconfig.grid.hideTopPanel();
////    } else {
////        filingGridconfig.grid.showTopPanel();
////    }
//}
////show allwj tab
//function allwj() {
//    showWjTab("","1");
//}
////open batch att tab
//function batchatt() {
//    var selectRows = filingGridconfig.grid.getSelectedRows();
//    if (selectRows.length > 0) {
//        selectRows.sort(function compare(a, b) {
//            return a - b;
//        });
//        archiveCommon.showBatchAttachment(filingGridconfig,'01',selectRows);
//    }
//    else {
//        us.openalert('请选择要批量挂接的档案记录! ',
//            '系统提示',
//            'alertbody alert_Information'
//        );
//    }
//}
////Single archive link files
//function linkfile() {
//    var selectRows = filingGridconfig.grid.getSelectedRows();
//    if (selectRows.length == 0) {
//        us.openalert('请选择要挂接的档案记录! ',
//            '系统提示',
//            'alertbody alert_Information'
//        );
//        return ;
//    }
//    else if (selectRows.length > 1) {
//        us.openalert('只能选择一条要挂接的档案记录! ',
//            '系统提示',
//            'alertbody alert_Information'
//        );
//        return ;
//    }
//    var item = filingGridconfig.dataView.getItem(selectRows[0]);
////	alert(filingGridconfig.selectTableid);
//    showDocwindow(item.id,filingGridconfig.selectTableid);
//}
////refresh data
//function refresh() {
//    var data = [];
//    readData();
//    filingGridconfig.dataView.setItems(data);
//    filingGridconfig.dataView.setItems(filingGridconfig.rows);
//}
//
//// 打开电子全文windows
//function showDocwindow(id, tableid) {
//    archiveCommon.selectRowid = id;
//    archiveCommon.selectTableid = tableid;
//
//    showdoc();
//
////    var par = "selectRowid="+ id + "&tableid="+tableid+"&treeid="+archiveCommon.selectTreeid;
////    var rowList = [];
////    //同步读取数据
////    $.ajax({
////        async : false,
////        url : "listLinkDoc.action?"+ par,
////        type : 'post',
////        dataType : 'script',
////        success : function(data) {
////            if (data != "error") {
////                rowList = eval(docList);
////                $("#doclist").html("");
////
////                for (var i=0;i<rowList.length;i++) {
////                    $("#doclist").append(getDoclist(rowList[i]));
////                }
////            } else {
////                us.openalert('读取数据时出错，请尝试重新操作或与管理员联系! ','系统提示','alertbody alert_Information');
////            }
////        }
////    });
//
//    $('#upload-doc-dialog').modal('show');
//
////    $("#docwindows").dialog('option', 'title', '电子全文列表--(共 ' + rowList.length + "个文件)");
////    $("#docwindows").dialog( "open" );
//}
//
////显示当前数据的电子文件
//function showdoc() {
//    var par = "selectRowid="+ archiveCommon.selectRowid + "&tableid="+archiveCommon.selectTableid+"&treeid="+archiveCommon.selectTreeid;
//    var rowList = [];
//    //同步读取数据
//    $.ajax({
//        async : false,
//        url : "listLinkDoc.action?"+ par,
//        type : 'post',
//        dataType : 'script',
//        success : function(data) {
//    	 alert(data);
//            if (data != "error") {
//                rowList = eval(docList);
//                $("#doclist").html("");
//
//                for (var i=0;i<rowList.length;i++) {
//                    $("#doclist").append(getDoclist(rowList[i]));
//                }
//            } else {
//                us.openalert('读取数据时出错，请尝试重新操作或与管理员联系! ','系统提示','alertbody alert_Information');
//            }
//        }
//    });
//}
//
///*
// * 生成doc现实的列表
// *
// */
//function getDoclist(row) {
//    var str = "<li class=\"docli\" onMouseOut=\"showDocDelectButton(false,'"+row.docid+"')\" onMouseOver=\"showDocDelectButton(true,'"+row.docid+"')\">";
//    str += "<div class=\"docdiv\"><a href=\"#\" onClick=\"fileDown('"+row.docid+"','"+archiveCommon.selectTreeid+"')\">";
//    var docType = row.docext;
//    var typeCss = "";
//    if (docType == "DOC" || docType == "XLS" || docType=="PPT" || docType=="DOCX" || docType=="XLSX") {
//        typeCss = "file-icon-office";
//    }
//    else if (docType == "TXT" || docType == "INI") {
//        typeCss = "file-icon-text";
//    }
//    else if (docType == "JPG" || docType == "GIF" || docType=="BMP" || docType=="JPEG" || docType=="TIF") {
//        typeCss = "file-icon-image";
//    }
//    else if (docType == "PDF") {
//        typeCss = "file-icon-pdf";
//    }
//    else if (docType == "ZIP") {
//        typeCss = "file-icon-zip";
//    }
//    else if (docType == "RAR") {
//        typeCss = "file-icon-rar";
//    }
//    else if (docType == "FLASH") {
//        typeCss = "file-icon-flash";
//    }
//    else {
//
//    }
//    str += "<img class=\"file-icon "+typeCss+" \">";
//    str += "</a></div>";
//    var name = row.docoldname;
////	if (row.docoldname.length > 8) {
////		name = row.docoldname.substr(0,8) + "..." + "." + docType;
////	}
//    str += "<div title=\""+row.docoldname+"\"><div class=\"docfilename\"><a href=\"#\" onClick=\"fileDown('"+row.docid+"','"+archiveCommon.selectTreeid+"')\">" + name + "</a></div></div>";
//    str += "<div><button type=\"button\" id=\""+row.docid+"\" style=\"display:none\" onClick=\"delectDoc('"+row.docid+"')\" class=\"btn btn-danger btn-small\">删除</button></div>";
//    str += "</li>";
//    return str;
//}
//
//function uploadFile() {
//    var url = 'docUpload.action?fileid=' +archiveCommon.selectRowid + '&tableid=' + archiveCommon.selectTableid + '&treeid=' + archiveCommon.selectTreeid;
//    us.upload_multi(url,'showdoc');
////    $("#uploader").pluploadQueue({
////        // General settings
////        runtimes : 'flash,html5,html4',
////        url : 'docUpload.action?fileid=' +archiveCommon.selectRowid + '&tableid=' + archiveCommon.selectTableid + '&treeid=' + archiveCommon.selectTreeid,
////        max_file_size : '200mb',
////        //缩略图形式。
//////        resize : {width :32, height : 32, quality : 90},
//////        unique_names : true,
////        chunk_size: '2mb',
////        // Flash settings
////        flash_swf_url : '../../js/plupload/js/plupload.flash.swf'
////        // Silverlight settings
//////        silverlight_xap_url : '/example/plupload/js/plupload.silverlight.xap'
////    });
////    $('#formId').submit(function(e) {
////        var uploader = $('#uploader').pluploadQueue();
////        if (uploader.files.length > 0) {
////            // When all files are uploaded submit form
////            uploader.bind('StateChanged', function() {
////                if (uploader.files.length === (uploader.total.uploaded + uploader.total.failed)) {
////                    $('#formId')[0].submit();
////                }
////            });
////            uploader.start();
////        } else {
////            us.openalert('请先上传数据文件! ','系统提示','alertbody alert_Information');
////        }
////        return false;
////    });
////    $( "#uploadFile" ).dialog( "open");
//}
//
///**
// * 显示和隐藏电子全文删除按钮
// * @param b		true false 是否隐藏
// * @param id	按钮ID
// */
//function showDocDelectButton(b,id) {
//    if (b) {
//        $("#" + id).css("display","inline-block");
//    }
//    else {
//        $("#" + id).css("display","none");
//    }
//}
//
////下载
//function fileDown(docId,treeid){
//    $.ajax({
//        async : false,
//        url : "isDownDoc.action",
//        type : 'post',
//        dataType : 'text',
//        data:"treeid="+treeid,
//        success : function(data) {
//            if (data == "1") {
//                window.location.href="downDoc.action?docId="+docId+"&treeid="+treeid;
//            } else {
//                openalert("对不起，您没有权限下载此文件！");
//            }
//        }
//    });
//}
//
//function delectDoc(id) {
//    bootbox.confirm("确定要<span style='color:red'>删除</span> 选中的电子全文吗？<br>" +
//        "<span style='color:red'>注意：将彻底删除电子全文，请谨慎操作！</span> ", function(result) {
//        if(result){
//            //同步读取数据
//            $.ajax({
//                async : false,
//                url : "docDelete.action?docId="+ id,
//                type : 'post',
//                dataType : 'script',
//                success : function(data) {
//                    if (data != "error") {
//                        us.openalert('删除文件完毕! ');
//                    } else {
//                        us.openalert('删除文件时出错，请尝试重新操作或与管理员联系! ');
//                    }
//                }
//            });
//            //重新读取全文列表
//            showdoc();
//        }
//    })
//}