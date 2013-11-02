var ajGridconfig = new us.archive.ui.Gridconfig();
var loader = new Slick.Data.RemoteModel();
var loading_aj = new us.archive.ui.loading();

$(function() {

    ajGridconfig.tabletype = '01';
    ajGridconfig.init_grid(ajGridconfig,"#archivediv","pager_aj","inlineFilterPanel_aj");
    
    loader.onDataLoading.subscribe(function () {
    	loading_aj.show("archivediv");
    });
    

    // 创建checkbox列
//    var checkboxSelector = new Slick.CheckboxSelectColumn({
//        cssClass : "slick-cell-checkboxsel"
//    });
//    // 加入checkbox列
//    ajGridconfig.columns.push(checkboxSelector.getColumnDefinition());
//    // 加入其他列
//    for ( var i = 0; i < ajGridconfig.columns_fields.length; i++) {
//        ajGridconfig.columns.push(ajGridconfig.columns_fields[i]);
//    }

//    // 创建dataview
//    ajGridconfig.dataView = new Slick.Data.DataView({
//        inlineFilters : true
//    });
//    // 创建grid
//    ajGridconfig.grid = new Slick.Grid("#archivediv", ajGridconfig.dataView, ajGridconfig.columns, ajGridconfig.options);
//    var columnpicker = new Slick.Controls.ColumnPicker(ajGridconfig.columns, ajGridconfig.grid, ajGridconfig.options);
//    //设置录入错误时提示。例如不能为空的字段
//    ajGridconfig.grid.onValidationError.subscribe(function(e, args) {
////		alert(args.validationResults.msg);
//        us.openalert(args.validationResults.msg,
//            '系统提示',
//            'alertbody alert_Information'
//        );
//    });
//
//    // 设置grid的选择模式。行选择
//    // grid.setSelectionModel(new Slick.RowSelectionModel());
//    ajGridconfig.grid.setSelectionModel(new Slick.RowSelectionModel({
//        selectActiveRow : false
//    }));
////	ajGridconfig.grid.setSelectionModel(new Slick.CellSelectionModel());
//
////    设置键盘监听。ctrl + a 全选
//    ajGridconfig.grid.onKeyDown.subscribe(function(e) {
//        // select all rows on ctrl-a
//        if (e.which != 65 || !e.ctrlKey) {
//            return false;
//        }
//        var rows = [];
//        for ( var i = 0; i < ajGridconfig.dataView.getLength(); i++) {
//            rows.push(i);
//        }
//        ajGridconfig.grid.setSelectedRows(rows);
//        e.preventDefault();
//    });
//
//    //设置分页控件
//    var pager_aj = new Slick.Controls.Pager(ajGridconfig.dataView, ajGridconfig.grid, $("#pager_aj"));
//
//    // 注册grid的自动提示插件。只在字段内容过长时出现省略号时提示
//    ajGridconfig.grid.registerPlugin(new Slick.AutoTooltips());
//    $("#inlineFilterPanel_aj").appendTo(ajGridconfig.grid.getTopPanel()).show();

//    $("#txtSearch_aj").keyup(function(e) {
//        Slick.GlobalEditorLock.cancelCurrentEdit();
//        // clear on Esc
//        if (e.which == 27) {
//            this.value = "";
//        }
//        archiveCommon.clName = $("#selectfield_aj").val();
//        archiveCommon.searchString = this.value;
//        us.updateFilter(ajGridconfig.dataView);
//    });
//    //生成过滤选择字段
//    for (var i=0;i<ajGridconfig.columns_fields.length;i++) {
//        if (ajGridconfig.columns_fields[i].id != "rownum" && ajGridconfig.columns_fields[i].id != "isdoc" && ajGridconfig.columns_fields[i].id != "files") {
//            $("#selectfield_aj").append("<option value='"+ajGridconfig.columns_fields[i].id+"'>"+ajGridconfig.columns_fields[i].name+"</option>");
//        }
//    }
//
//
//    //grid的添加新行事件
//    ajGridconfig.grid.onAddNewRow.subscribe(function(e, args) {
//        var item = $.extend({}, ajGridconfig.newItemTemplate, args.item);
//        item.id = UUID.prototype.createUUID ();
//        item.treeid = archiveCommon.selectTreeid;
//
//        item.rownum = (ajGridconfig.dataView.getLength() + 1).toString();
//        ajGridconfig.dataView.addItem(item);
//
//        var par = "importData=[" + JSON.stringify(item) + "]&tableType=01";
//        alert(par);
//        $.post("saveImportArchive.action",par,function(data){
//                if (data != "保存完毕。") {
//                    us.openalert(data,
//                        '系统提示',
//                        'alertbody alert_Information'
//                    );
//                }
//            }
//        );
//    });
//
//
//    //grid的列值变动事件
//    ajGridconfig.grid.onCellChange.subscribe(function(e, args) {
//        var item = args.item;
//        var par = "importData=[" + JSON.stringify(item) + "]&tableType=01";
//        $.post("updateImportArchive.action",par,function(data){
//                if (data != "保存完毕。") {
//                    us.openalert(data,
//                        '系统提示',
//                        'alertbody alert_Information'
//                    );
//                }
//            }
//        );
//    });
//
//    ajGridconfig.grid.onSort.subscribe(function(e, args) {
//        archiveCommon.sortdir = args.sortAsc ? 1 : -1;
//        archiveCommon.sortcol = args.sortCol.field;
//        ajGridconfig.dataView.sort(us.comparer, args.sortAsc);
//
//    });
//
//    ajGridconfig.dataView.onRowCountChanged.subscribe(function(e, args) {
//        ajGridconfig.grid.updateRowCount();
//        ajGridconfig.grid.render();
//    });
//
//    ajGridconfig.dataView.onRowsChanged.subscribe(function(e, args) {
//        ajGridconfig.grid.invalidateRows(args.rows);
//        ajGridconfig.grid.render();
//    });
//
//    ajGridconfig.dataView.beginUpdate();
//    ajGridconfig.dataView.setItems(ajGridconfig.rows);
//    ajGridconfig.dataView.setFilterArgs({
//        searchString : archiveCommon.searchString
//    });
//    ajGridconfig.dataView.setFilter(us.myFilter);
//    ajGridconfig.dataView.endUpdate();
//
//    ajGridconfig.dataView.syncGridSelection(ajGridconfig.grid, true);
})

///**
// * 分页读取数据
// * @param url	
// **/
//function pageList(url,gridObject,loader_Obj){
//	loader_Obj.clear();
//	loader_Obj.setPage(0);
//	var data = [];
//	gridObject.dataView.setItems(data);
//	gridObject.grid.onViewportChanged.subscribe(function (e, args) {
//      var vp = gridObject.grid.getViewport();
//      loader_Obj.ensureData(vp.top, vp.bottom,url);
//    });
//    gridObject.grid.onSort.subscribe(function (e, args) {
////      loader_Obj.setSort(args.sortCol.field, args.sortAsc ? 1 : -1);
//      var vp = gridObject.grid.getViewport();
//      loader_Obj.ensureData(vp.top, vp.bottom,url);
//    });
//    loader_Obj.onDataLoaded.subscribe(function (e, args) {
//    	gridObject.totalRows = rowCount;
//    	var data = [];
//    	if(args.flag){
//	    	for (var i = args.from; i < (args.to+args.from); i++) {
//	    		loader_Obj.data[i].rownum = i+1;
//	    		data.push(loader_Obj.data[i]);
//	    		gridObject.dataView.addItem(loader_Obj.data[i]);
//	    	}
//	    	args.flag = false;
//	    	for (var i = args.from; i <= args.to; i++) {
//	            gridObject.grid.invalidateRow(i);
//	    	}
//	    	
//	    	gridObject.grid.updateRowCount();
//	    	gridObject.grid.render();
//
//	    	loading_aj.remove();
//    	}
//    });
//   
//    gridObject.grid.setSortColumn("date", false);
//    // load the first page
//    gridObject.grid.onViewportChanged.notify();
//    
//}



//read Archive data
function readData() {

//	var loading = new us.archive.ui.loading();
	
    //同步读取数据
    var par = "treeid=" + archiveCommon.selectTreeid + "&tableType=01&status=0";
    //分页查询
    var url = "listArchiveP.action?"+par;
    ajGridconfig.pageList(url,ajGridconfig,loader,"txtSearch_aj","selectfield_aj",loading_aj);
//    $.ajax({
//        async : false,
//        url : "listArchive.action?" + par,
//        type : 'post',
//        dataType : 'script',
//        success : function(data) {
//            if (data != "error") {
//                ajGridconfig.rows = rowList;
//
//                ajGridconfig.dataView.setItems(ajGridconfig.rows);

                // 创建checkbox列
                var checkboxSelector = new Slick.CheckboxSelectColumn({
                    cssClass : "slick-cell-checkboxsel"
                });
                var visibleColumns = [];//定义一个数组存放显示的列
                visibleColumns.push(checkboxSelector.getColumnDefinition());//将columns的第一列push进去

//                // 加入其他列
                for ( var i = 0; i < ajGridconfig.columns_fields.length; i++) {
                    visibleColumns.push(ajGridconfig.columns_fields[i]);
                }

                //设置grid的列
                ajGridconfig.grid.setColumns(visibleColumns);

//                ajGridconfig.dataView.beginUpdate();
//                ajGridconfig.dataView.setItems(ajGridconfig.rows);
                ajGridconfig.dataView.setFilterArgs({
                    searchString : archiveCommon.searchString
                });
                ajGridconfig.dataView.setFilter(us.myFilter);
//                ajGridconfig.dataView.endUpdate();
                // 注册grid的checkbox功能插件
                ajGridconfig.grid.registerPlugin(checkboxSelector);

                //生成过滤选择字段
                $("#selectfield_aj").empty();
                for (var i=0;i<ajGridconfig.columns_fields.length;i++) {
                    if (ajGridconfig.columns_fields[i].id != "rownum" && ajGridconfig.columns_fields[i].id != "isdoc" && ajGridconfig.columns_fields[i].id != "files") {
                        $("#selectfield_aj").append("<option value='"+ajGridconfig.columns_fields[i].id+"'>"+ajGridconfig.columns_fields[i].name+"</option>");
                    }
                }

                //声明新建行的系统默认值
//                var newItemTemplate = {
//                    isdoc	: "0",
//                    status	: "1"
//                };
                //新建行时，将系统必须的默认值与字段默认值合并
                ajGridconfig.newItemTemplate = $.extend({},ajGridconfig.newItemTemplate,ajGridconfig.fieldsDefaultValue);
                ajGridconfig.grid.invalidate();
//                loading.remove();
//            } else {
//                us.openalert('<span style="color:red">读取数据时出错.</span></br>请关闭浏览器，重新登录尝试或与管理员联系!',
//                    '系统提示',
//                    'alertbody alert_Information'
//                );
//            }
//        }
//    });
}

function show_aj_archive_list() {
    $('#ajtab').click();
    $("#allwj").css('display','block');
    $('#wjtab').show();
    $('#ajtab').text('案卷');
    //同步读取字段
    var par = "treeid=" + archiveCommon.selectTreeid + "&tableType=01&importType=0";
    $.ajax({
        async : false,
        url : "getField.action?" + par,
        type : 'post',
        dataType : 'script',
        success : function(data) {
            if (data != "error") {
                ajGridconfig.columns_fields = fields;
                ajGridconfig.fieldsDefaultValue = fieldsDefaultValue;
                ajGridconfig.selectTableid = tableid;
//                var temptype = templettype;
                archiveCommon.templettype = templettype
                if (archiveCommon.templettype == "F") {
                	$("#allwj").css('display','none');
                	$('#wjtab').hide();
                	$('#ajtab').text('文件');
                }
            } else {
                us.openalert('<span style="color:red">读取字段信息时出错.</span></br>请关闭浏览器，重新登录尝试或与管理员联系!',
                    '系统提示',
                    'alertbody alert_Information'
                );
            }
        }
    });
    readData();

    if (archiveCommon.templettype == "F") {
    	$("#grid_header_aj").html('<h3>'+archiveCommon.selectTreeName + '_文件档案列表'+'</h3>');
    }
    else {
    	$("#grid_header_aj").html('<h3>'+archiveCommon.selectTreeName + '_案卷档案列表'+'</h3>');
    }
    

}

//insert a new row
function add() {
//    ajGridconfig.grid.setOptions({
//        autoEdit : true
//    });
//    var item = {
//    		id		: "112",
//            isdoc	: "0",
//            status	: "0"
//        }
    ajGridconfig.addItem(ajGridconfig);
    readData();
//    ajGridconfig.grid.gotoCell(0,4,true);
//    ajGridconfig.dataView.insertItem(0,item);
//    ajGridconfig.grid.gotoCell(0,4,true);
//    ajGridconfig.grid.gotoCell(ajGridconfig.dataView.getLength(),4,true);
}
//open update mode
function update() {
    ajGridconfig.grid.setOptions({
        autoEdit : true
    });
}
//end update mode
function endupdate() {
    ajGridconfig.grid.setOptions({
        autoEdit : false
    });
}
//delete rows
function del() {

    var selectRows = ajGridconfig.grid.getSelectedRows();
    selectRows.sort(function compare(a, b) {
        return b - a;
    });

    var deleteRows = [];

    for ( var i = 0; i < selectRows.length; i++) {
        var item = ajGridconfig.dataView.getItem(selectRows[i]);
        if (item) {
            deleteRows.push(item);
        }
    }

    if (deleteRows.length > 100) {
        us.openalert('选择删除的数据过大，请选择100条以内，分多次删除。 ','系统提示','alertbody alert_Information');
        return;
    }

    if (deleteRows.length > 0) {
    	var str = "确定要删除选中的 <span style='color:red'>"+deleteRows.length+"</span> 条案卷记录吗?<br> <font color='red'>" +
        "注意：删除案卷记录，将同时删除案卷及案卷下所有文件数据、电子全文，请谨慎操作！</font> ";
    	if (archiveCommon.templettype == "F") {
    		var str = "确定要删除选中的 <span style='color:red'>"+deleteRows.length+"</span> 条文件记录吗?<br> <font color='red'>" +
            "注意：删除记录，将不能恢复，请谨慎操作！</font> ";
    	}
        bootbox.confirm(str, function(result) {
            if(result){
            	
            	var jsonString = JSON.stringify(deleteRows);
        		jsonString = jsonString.replace(/%/g,"%25");
        		jsonString = jsonString.replace(/\&/g,"%26");
        		jsonString = jsonString.replace(/\+/g,"%2B");
        		
                var par = "par=" + jsonString + "&tableType=01";

                $.post("deleteArchive.action",par,function(data){
                        if (data == "SUCCESS") {
                            for ( var i = 0; i < selectRows.length; i++) {
                                var item = ajGridconfig.dataView.getItem(selectRows[i]);
                                ajGridconfig.dataView.deleteItem(item.id);
                                ajGridconfig.grid.invalidate();
                                ajGridconfig.grid.render();
                            };
                            readData();
//                            ajGridconfig.grid.invalidate();
//                            ajGridconfig.grid.render();
                            us.openalert('删除成功。 ','系统提示','alertbody alert_Information');
//                            var data = [];
//                            readData();
//                            grid.invalidate();
//                            ajGridconfig.dataView.setItems(data);
//                            ajGridconfig.dataView.setItems(ajGridconfig.rows);
                        }
                        else {
                            us.openalert(data,'系统提示','alertbody alert_Information');
                        }
                    }
                );
            }
        })
    }
    else {
        us.openalert('请选择要删除的数据。 ','系统提示','alertbody alert_Information');
    }
}
//open batchupdate dialog
function batchupdate() {
    var selectRows = ajGridconfig.grid.getSelectedRows();
    selectRows.sort(function compare(a, b) {
        return b - a;
    });
    if (selectRows.length > 0) {
        $("#selectfield").empty();
        for (var i=0;i<ajGridconfig.columns_fields.length;i++) {
            if (ajGridconfig.columns_fields[i].id != "rownum" && ajGridconfig.columns_fields[i].id != "isdoc" && ajGridconfig.columns_fields[i].id != "files") {
                $("#selectfield").append("<option value='"+ajGridconfig.columns_fields[i].id+"'>"+ajGridconfig.columns_fields[i].name+"</option>");
            }
        }
        $("#updatetxt").val("");
        //取消绑定保存按钮的click事情
        $("#save_batch").unbind("click");
        $('#save_batch').click(function() {
            us.batchUpdate(ajGridconfig.grid,ajGridconfig.dataView,true,'01');
        });
        $('#batchwindows').modal('show');
    }
    else {
        us.openalert('请选择要修改的数据.');
    }
}
//open import data dialog
function importdata() {
//    showArchiveImportTab('01');
    openImportWindows('01');
}
//show filter div
function filter() {
    ajGridconfig.grid.setTopPanelVisibility(!ajGridconfig.grid.getOptions().showTopPanel);
//    if ($(ajGridconfig.grid.getTopPanel()).is(":visible")) {
//        ajGridconfig.grid.hideTopPanel();
//    } else {
//        ajGridconfig.grid.showTopPanel();
//    }
}
//show allwj tab
function allwj() {
    showWjTab("","1");
}
//open batch att tab
function batchatt() {
    var selectRows = ajGridconfig.grid.getSelectedRows();
    if (selectRows.length > 0) {
        selectRows.sort(function compare(a, b) {
            return a - b;
        });
        archiveCommon.showBatchAttachment(ajGridconfig,'01',selectRows);
    }
    else {
        us.openalert('请选择要批量挂接的档案记录! ',
            '系统提示',
            'alertbody alert_Information'
        );
    }
}
//Single archive link files
function linkfile() {
    var selectRows = ajGridconfig.grid.getSelectedRows();
    if (selectRows.length == 0) {
        us.openalert('请选择要挂接的档案记录! ',
            '系统提示',
            'alertbody alert_Information'
        );
        return ;
    }
    else if (selectRows.length > 1) {
        us.openalert('只能选择一条要挂接的档案记录! ',
            '系统提示',
            'alertbody alert_Information'
        );
        return ;
    }
    var item = ajGridconfig.dataView.getItem(selectRows[0]);
//	alert(ajGridconfig.selectTableid);
    showDocwindow(item.id,ajGridconfig.selectTableid);
}
//refresh data
function refresh() {
    var data = [];
    readData();
//    ajGridconfig.dataView.setItems(data);
//    ajGridconfig.dataView.setItems(ajGridconfig.rows);
}

// 打开电子全文windows
function showDocwindow(id, tableid) {
    archiveCommon.selectRowid = id;
    archiveCommon.selectTableid = tableid;

    showdoc();

//    var par = "selectRowid="+ id + "&tableid="+tableid+"&treeid="+archiveCommon.selectTreeid;
//    var rowList = [];
//    //同步读取数据
//    $.ajax({
//        async : false,
//        url : "listLinkDoc.action?"+ par,
//        type : 'post',
//        dataType : 'script',
//        success : function(data) {
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

//    $('#upload-doc-dialog').modal('show');

//    $("#docwindows").dialog('option', 'title', '电子全文列表--(共 ' + rowList.length + "个文件)");
//    $("#docwindows").dialog( "open" );
}

//显示当前数据的电子文件
function showdoc() {
    var par = "selectRowid="+ archiveCommon.selectRowid + "&tableid="+archiveCommon.selectTableid+"&treeid="+archiveCommon.selectTreeid;
    var rowList = [];
    //同步读取数据
    $.ajax({
        async : false,
        url : "listLinkDoc.action?"+ par,
        type : 'post',
        dataType : 'script',
        success : function(data) {
        	var isAuth = isNotAuth;

        	if (isAuth == 1) {
//        		rowList = eval(docList);
                $("#doclist").html(getDocTable(docList));
                $('#upload-doc-dialog').modal('show');
        	}
        	else {
        		us.openalert('没有浏览电子全文权限，或读取数据时出错，请尝试重新操作或与管理员联系! ','系统提示','alertbody alert_Information');
        	}
        	
        	
//            if (data != "error") {
//                rowList = eval(docList);
//                $("#doclist").html(getDocTable(docList));
//                $('#upload-doc-dialog').modal('show');
//
////                for (var i=0;i<rowList.length;i++) {
////                    $("#doclist").append(getDoclist(rowList[i]));
////                }
//            } else {
//                us.openalert('没有浏览电子全文权限，或读取数据时出错，请尝试重新操作或与管理员联系! ','系统提示','alertbody alert_Information');
//            }
        }
    });
}

function getDocTable(list) {
	var content = "";
	content += "<table class=\"table table-bordered table-condensed\" width=\"100%\">";
	content += "<thead>";
	content += "<tr>";
	content += "<th>序号</th>";
	content += "<th>文件名</th>";
	content += "<th>类型</th>";
	content += "<th>大小</th>";
	content += "<th>操作</th>";
	content += "</tr>";
	content += "</thead>";
	content += "<tbody>";
	
	for (var i=0;i<list.length;i++) {
		var doc = list[i];
		var num = i+1;
		content += "<tr>";
		content += "<td>"+num+"</td>";
		content += "<td>"+doc.docoldname+"</td>";
		content += "<td>"+doc.docext+"</td>";
		content += "<td>"+doc.doclength+"</td>";
		content += "<td><button type='button' class='btn btn-primary' onclick=\"fileDown('"+doc.docid+"','"+archiveCommon.selectTreeid+"')\">下载</button><button type='button' class='btn btn-danger' style='margin-left: 10px;' onclick=\"delectDoc('"+doc.docid+"')\">删除</button></td>";
		content += "</tr>";
	}
	content += "</tbody>";
	content += "</table>";
	return content;
}

/*
 * 生成doc现实的列表
 *
 */
function getDoclist(row) {
    var str = "<li class=\"docli\" onMouseOut=\"showDocDelectButton(false,'"+row.docid+"')\" onMouseOver=\"showDocDelectButton(true,'"+row.docid+"')\">";
    str += "<div class=\"docdiv\"><a href=\"#\" onClick=\"fileDown('"+row.docid+"','"+archiveCommon.selectTreeid+"')\">";
    var docType = row.docext;
    var typeCss = "";
    if (docType == "DOC" || docType == "XLS" || docType=="PPT" || docType=="DOCX" || docType=="XLSX") {
        typeCss = "file-icon-office";
    }
    else if (docType == "TXT" || docType == "INI") {
        typeCss = "file-icon-text";
    }
    else if (docType == "JPG" || docType == "GIF" || docType=="BMP" || docType=="JPEG" || docType=="TIF") {
        typeCss = "file-icon-image";
    }
    else if (docType == "PDF") {
        typeCss = "file-icon-pdf";
    }
    else if (docType == "ZIP") {
        typeCss = "file-icon-zip";
    }
    else if (docType == "RAR") {
        typeCss = "file-icon-rar";
    }
    else if (docType == "FLASH") {
        typeCss = "file-icon-flash";
    }
    else {

    }
    str += "<img class=\"file-icon "+typeCss+" \">";
    str += "</a></div>";
    var name = row.docoldname;
//	if (row.docoldname.length > 8) {
//		name = row.docoldname.substr(0,8) + "..." + "." + docType;
//	}
    str += "<div title=\""+row.docoldname+"\"><div class=\"docfilename\"><a href=\"#\" onClick=\"fileDown('"+row.docid+"','"+archiveCommon.selectTreeid+"')\">" + name + "</a></div></div>";
    str += "<div><button type=\"button\" id=\""+row.docid+"\" style=\"display:none\" onClick=\"delectDoc('"+row.docid+"')\" class=\"btn btn-danger btn-small\">删除</button></div>";
    str += "</li>";
    return str;
}

function uploadFile() {
    var url = 'docUpload.action?fileid=' +archiveCommon.selectRowid + '&tableid=' + archiveCommon.selectTableid + '&treeid=' + archiveCommon.selectTreeid;
    us.upload_multi(url,'showdoc');
//    $("#uploader").pluploadQueue({
//        // General settings
//        runtimes : 'flash,html5,html4',
//        url : 'docUpload.action?fileid=' +archiveCommon.selectRowid + '&tableid=' + archiveCommon.selectTableid + '&treeid=' + archiveCommon.selectTreeid,
//        max_file_size : '200mb',
//        //缩略图形式。
////        resize : {width :32, height : 32, quality : 90},
////        unique_names : true,
//        chunk_size: '2mb',
//        // Flash settings
//        flash_swf_url : '../../js/plupload/js/plupload.flash.swf'
//        // Silverlight settings
////        silverlight_xap_url : '/example/plupload/js/plupload.silverlight.xap'
//    });
//    $('#formId').submit(function(e) {
//        var uploader = $('#uploader').pluploadQueue();
//        if (uploader.files.length > 0) {
//            // When all files are uploaded submit form
//            uploader.bind('StateChanged', function() {
//                if (uploader.files.length === (uploader.total.uploaded + uploader.total.failed)) {
//                    $('#formId')[0].submit();
//                }
//            });
//            uploader.start();
//        } else {
//            us.openalert('请先上传数据文件! ','系统提示','alertbody alert_Information');
//        }
//        return false;
//    });
//    $( "#uploadFile" ).dialog( "open");
}

/**
 * 显示和隐藏电子全文删除按钮
 * @param b		true false 是否隐藏
 * @param id	按钮ID
 */
function showDocDelectButton(b,id) {
    if (b) {
        $("#" + id).css("display","inline-block");
    }
    else {
        $("#" + id).css("display","none");
    }
}

//下载
function fileDown(docId,treeid){
    $.ajax({
        async : false,
        url : "isDownDoc.action",
        type : 'post',
        dataType : 'text',
        data:"treeid="+treeid,
        success : function(data) {
            if (data == "1") {
                window.location.href="downDoc.action?docId="+docId+"&treeid="+treeid;
            } else {
                openalert("对不起，您没有权限下载此文件！");
            }
        }
    });
}

function delectDoc(id) {
    bootbox.confirm("确定要<span style='color:red'>删除</span> 选中的电子全文吗？<br>" +
        "<span style='color:red'>注意：将彻底删除电子全文，请谨慎操作！</span> ", function(result) {
        if(result){
            //同步读取数据
            $.ajax({
                async : false,
                url : "docDelete.action?docId="+ id,
                type : 'post',
                dataType : 'script',
                success : function(data) {
                    if (data != "error") {
                        us.openalert('删除文件完毕! ');
                    } else {
                        us.openalert('删除文件时出错，请尝试重新操作或与管理员联系! ');
                    }
                }
            });
            //重新读取全文列表
            showdoc();
        }
    })
}