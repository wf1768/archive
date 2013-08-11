var wjGridconfig = new us.archive.ui.Gridconfig();
var ajGridconfig = new us.archive.ui.Gridconfig();

$(function(){
	
	wjGridconfig.tabletype = '02';
	wjGridconfig.init_grid(wjGridconfig,"#wjdiv","pager_wj","inlineFilterPanel_wj");
	    
	$("#txtSearch_wj").keyup(function(e) {
		Slick.GlobalEditorLock.cancelCurrentEdit();
		// clear on Esc
		if (e.which == 27) {
			this.value = "";
		}
		archiveCommon.clName = $("#selectfield_wj").val();
		archiveCommon.searchString = this.value;
		us.updateFilter(wjGridconfig.dataView);
	});
	
	//声明新建行的系统默认值
	wjGridconfig.newItemTemplate = {
			treeid	: archiveCommon.selectTreeid,
			parentid: archiveCommon.selectAid,
			isdoc	: "0",
			status	: "2"
	    };
	
	//文件类型 判断文件类型
	if(archiveCommon.archiveType == "F"){
		//纯文件级
		wjGridconfig.tabletype = "01";
	}
	
});

/**
 * 显示文件列表
 * @param tableType 01:纯文件/02:文件 
 * */
function show_wj_list(tableType){
	$('#zlwj').click();
	wjGridconfig.tabletype = tableType;
	var par = "treeid=" + archiveCommon.selectTreeid + "&tableType="+tableType+"&importType=0";
	$.ajax({
		async: false,
		url: "getField.action?" + par,
		type:'post',
		dataType: 'script',
        success: function(data) {
			if (data != "error") {
				wjGridconfig.columns_fields = fields;
				wjGridconfig.fieldsDefaultValue= fieldsDefaultValue;
				wjGridconfig.selectTableid = tableid;
			}
			else {
				us.openalert('<span style="color:red">读取字段信息时出错!<span></br>请关闭浏览器，重新登录尝试或与管理员联系。',
						'系统提示',
						'alertbody alert_Information'
				);
			}
        }
	});
	//读取文件数据
	readwjdata(tableType);
	
	
}

/**
 * 读取文件数据
 * @param tableType 01：纯文件/02：文件 
 * */
function readwjdata(tableType) {
	//同步读取数据
	var par = "treeid=" + archiveCommon.selectTreeid + "&tableType="+tableType+"&isAllWj=false"+"&status=2";// + archiveCommon.isAllWj ;//+"&selectAid=" + archiveCommon.selectAid;
	$.ajax({
		async : false,
		url : "listArchiveF.action?" + par,
		type : 'post',
		dataType : 'script',
		success : function(data) {
			if (data != "error") {
				wjGridconfig.rows = rowList;
				wjGridconfig.dataView.setItems(wjGridconfig.rows);
				
				// 创建checkbox列
				var checkboxSelector = new Slick.CheckboxSelectColumn({
					cssClass : "slick-cell-checkboxsel"
				});
				var visibleColumns = [];//定义一个数组存放显示的列
                visibleColumns.push(checkboxSelector.getColumnDefinition());//将columns的第一列push进去

	            // 加入其他列
                for ( var i = 0; i < wjGridconfig.columns_fields.length; i++) {
                    visibleColumns.push(wjGridconfig.columns_fields[i]);
                }
                //设置grid的列
                wjGridconfig.grid.setColumns(visibleColumns);

//                wjGridconfig.dataView.beginUpdate();
//                wjGridconfig.dataView.setItems(wjGridconfig.rows);
                wjGridconfig.dataView.setFilterArgs({
                    searchString : archiveCommon.searchString
                });
                wjGridconfig.dataView.setFilter(us.myFilter);
//                wjGridconfig.dataView.endUpdate();

                // 注册grid的checkbox功能插件
                wjGridconfig.grid.registerPlugin(checkboxSelector);

                //生成过滤选择字段
                $("#selectfield_wj").empty();
				for (var i=0;i<wjGridconfig.columns_fields.length;i++) {
					if (wjGridconfig.columns_fields[i].id != "rownum" && wjGridconfig.columns_fields[i].id != "isdoc" && wjGridconfig.columns_fields[i].id != "files") {
						$("#selectfield_wj").append("<option value='"+wjGridconfig.columns_fields[i].id+"'>"+wjGridconfig.columns_fields[i].name+"</option>");
					}
				}
				
				//新建行时，将系统必须的默认值与字段默认值合并
				wjGridconfig.newItemTemplate = $.extend({},wjGridconfig.newItemTemplate,wjGridconfig.fieldsDefaultValue);
				wjGridconfig.grid.invalidate();
                
			} else {
				us.openalert('<span style="color:red">读取数据时出错!<span></br>请尝试重新操作或与管理员联系。',
						'系统提示',
						'alertbody alert_Information'
				);
			}
		}
	});
}


//添加一行
function wjadd() {
	wjGridconfig.grid.setOptions({
		autoEdit : true
	});
	wjGridconfig.grid.gotoCell(wjGridconfig.dataView.getLength(),3,true);
}
//删除
function wjdelete() {
	var selectRows = wjGridconfig.grid.getSelectedRows();
	selectRows.sort(function compare(a, b) {
		return b - a;
	});
	
	if (selectRows.length > 0) {
		var prompt='确定要删除选中的 <span style="color:red">'+selectRows.length+'</span>' + 
		' 条记录吗? <br><span style="color:red">注意：删除文件记录，将同时删除文件及文件下所有数据、电子全文，请谨慎操作！</span> ';
		bootbox.confirm(prompt, function(result) {
            if(result){
            	var deleteRows = [];
        		for ( var i = 0; i < selectRows.length; i++) {
					var item = wjGridconfig.dataView.getItem(selectRows[i]);
					deleteRows.push(item);
				}
        		var tableType = "tableType=02"; //默认为文件级
        		//文件类型
        		if(archiveCommon.archiveType == "F"){
        			//纯文件级
        			tableType = "tableType=01";
        		}
        		var par = "par=" + JSON.stringify(deleteRows) + "&"+tableType;//"&tableType=02";
        		$.post("deleteArchive.action",par,function(data){
    				if (data == "SUCCESS") {
    					for ( var i = 0; i < selectRows.length; i++) {
        					var item = wjGridconfig.dataView.getItem(selectRows[i]);
        					wjGridconfig.dataView.deleteItem(item.id);
        				};
        				us.openalert('删除成功。 ','系统提示','alertbody alert_Information');
    				}else {
    					us.openalert(data,'系统提示','alertbody alert_Information');
    				}
        		});
            }
        });
	}else {
		us.openalert("请选择要删除的数据",'系统提示','alertbody alert_Information');
	}
}
//open wj grid update mode
function wjupdate() {
	wjGridconfig.grid.setOptions({
		autoEdit : true
	});
}
//close wj grid update mode
function wjendupdate() {
	wjGridconfig.grid.setOptions({
		autoEdit : false
	});
}
//批量修改
function wjbatchupdate() {
	var selectRows = wjGridconfig.grid.getSelectedRows();
	selectRows.sort(function compare(a, b) {
		return b - a;
	});
	if (selectRows.length > 0) {
		$("#selectfield").empty();
		for (var i=0;i<wjGridconfig.columns_fields.length;i++) {
			if (wjGridconfig.columns_fields[i].id != "rownum" && wjGridconfig.columns_fields[i].id != "isdoc" && wjGridconfig.columns_fields[i].id != "files") {
				$("#selectfield").append("<option value='"+wjGridconfig.columns_fields[i].id+"'>"+wjGridconfig.columns_fields[i].name+"</option>");
			}
		}
		
		$("#updatetxt").val("");
		//取消绑定保存按钮的click事情
        $("#save_batch").unbind("click");
        $('#save_batch').click(function() {
        	$('#batchwindows').modal('hide');
        	us.batchUpdate(wjGridconfig.grid,wjGridconfig.dataView,true,'02');;
        });
        $('#batchwindows').modal('show');
		
	}else {
		us.openalert('请选择要修改的数据。 ','系统提示','alertbody alert_Information');
	}
}

//刷新
function wjrefresh() {
	if(archiveCommon.archiveType == "F"){
		//纯文件级
		wjGridconfig.tabletype = "01";
	}else{
		wjGridconfig.tabletype = '02';
	}
	var data = [];
	readwjdata(wjGridconfig.tabletype);
	wjGridconfig.dataView.setItems(data);
	wjGridconfig.dataView.setItems(wjGridconfig.rows);
}

//过滤
function wjfilter() {
	wjGridconfig.grid.setTopPanelVisibility(!wjGridconfig.grid.getOptions().showTopPanel);
}

//单文件挂接
function wjlinkfile() {
    var selectRows = wjGridconfig.grid.getSelectedRows();
    if (selectRows.length == 0) {
        us.openalert('请选择要挂接的记录! ',
            '系统提示',
            'alertbody alert_Information'
        );
        return ;
    }
    else if (selectRows.length > 1) {
        us.openalert('只能选择一条要挂接的记录! ',
            '系统提示',
            'alertbody alert_Information'
        );
        return ;
    }
    var item = wjGridconfig.dataView.getItem(selectRows[0]);
    showDocwindow(item.id,wjGridconfig.selectTableid);
}
//打开电子全文windows
function showDocwindow(id, tableid) {
    archiveCommon.selectRowid = id;
    archiveCommon.selectTableid = tableid;

    showdoc();
    $('#upload-doc-dialog').modal('show');
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
            if (data != "error") {
                rowList = eval(docList);
                $("#doclist").html("");

                for (var i=0;i<rowList.length;i++) {
                    $("#doclist").append(getDoclist(rowList[i]));
                }
            } else {
                us.openalert('读取数据时出错，请尝试重新操作或与管理员联系! ','系统提示','alertbody alert_Information');
            }
        }
    });
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
    str += "<div title=\""+row.docoldname+"\"><div class=\"docfilename\"><a href=\"#\" onClick=\"fileDown('"+row.docid+"','"+archiveCommon.selectTreeid+"')\">" + name + "</a></div></div>";
    str += "<div><button type=\"button\" id=\""+row.docid+"\" style=\"display:none\" onClick=\"delectDoc('"+row.docid+"')\" class=\"btn btn-danger btn-small\">删除</button></div>";
    str += "</li>";
    return str;
}

function uploadFile() {
    var url = 'docUpload.action?fileid=' +archiveCommon.selectRowid + '&tableid=' + archiveCommon.selectTableid + '&treeid=' + archiveCommon.selectTreeid;
    us.upload_multi(url,'showdoc');
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

//导入
function wjimport() {
    openImportWindows('02');
}


/**
 *  读取案卷字段
 *  组卷条件
 * */
function read_aj_rows(){
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
            } else {
                us.openalert('<span style="color:red">读取字段信息时出错.</span></br>请关闭浏览器，重新登录尝试或与管理员联系!',
                    '系统提示',
                    'alertbody alert_Information'
                );
            }
        }
    });
}
//组卷-规则设定
function condition_group(){
	read_aj_rows();
	
	var selectRows = wjGridconfig.grid.getSelectedRows();
	if (selectRows.length > 0) {
		selectRows.sort(function compare(a, b) {
			return a - b;
		});
		//字段
		for (var i=0;i<ajGridconfig.columns_fields.length;i++) {
			if (ajGridconfig.columns_fields[i].id != "rownum" && ajGridconfig.columns_fields[i].id != "isdoc" && ajGridconfig.columns_fields[i].id != "files") {
				$("#condition_field_aj").append("<option value='"+ajGridconfig.columns_fields[i].id+"'>"+ajGridconfig.columns_fields[i].name+"</option>");
			}
		}
		$("#condition_group").modal({backdrop:'static'});
		
	}else {
		us.openalert('请选择要组卷的记录! ',
				'系统提示',
				'alertbody alert_Information'
		);
	}
}

function condition(){
	
}

//组卷
function wj_group(){
	$('#gdzj').click();
	var selectRows = wjGridconfig.grid.getSelectedRows();
	if (selectRows.length > 0) {
		selectRows.sort(function compare(a, b) {
			return a - b;
		});
		$("#condition_group").modal('hide');
	    
		var deleteRows = [];
		
		for ( var i = 0; i < selectRows.length; i++) {
			var item = wjGridconfig.dataView.getItem(selectRows[i]);
			item.status="1";
			deleteRows.push(item);
		}
		//文件类型
		if(archiveCommon.archiveType == "F"){
			//纯文件级
			wjGridconfig.tabletype = "01";
		}else{
			wjGridconfig.tabletype = '02';
		}
		var par = "importData=" + JSON.stringify(deleteRows) + "&tableTleype="+wjGridconfig.tabletype;//tableTleype=02
//		$.post("updateImportArchive.action",par,function(data){
//				if (data != "保存完毕。") {
//					us.openalert(data,
//							'系统提示',
//							'alertbody alert_Information'
//					);
//				} 
//			}
//		);
		
//		show_zj_archive_list('01',volumeGridconfig);
	}
}
