
//选择的档案条目grid
var attGridConfig = new us.archive.ui.Gridconfig();
//没有对应的全文grid
var attNoGridConfig = new us.archive.ui.Gridconfig();
//对应的全文grid
var attYesGridConfig = new us.archive.ui.Gridconfig();

//设置批量挂接条件
function setcondition() {
	$( "#setrequwindows" ).dialog({
		autoOpen: false,
		height: 280,
		width: 500,
		title:'设置挂接条件',
		modal: true,
		resizable:false,
		create: function(event, ui) {
//			$("#selectfield").empty();
//			for (var i=0;i<ajGridconfig.columns_fields.length;i++) {
//				if (ajGridconfig.columns_fields[i].id != "rownum" && ajGridconfig.columns_fields[i].id != "isdoc" && ajGridconfig.columns_fields[i].id != "files") {
//					$("#selectfield").append("<option value='"+ajGridconfig.columns_fields[i].id+"'>"+ajGridconfig.columns_fields[i].name+"</option>");
//				}
//			}
		},
		open:function(event,ui) {
//			$("#updatetxt").val("");
		},
		buttons: {
			"提交": function() {
//				us.batchUpdate(ajGridconfig.grid,ajGridconfig.dataView,true,'01');
			},
			"关闭": function() {
				$( this ).dialog( "close" );
			}
		},
		close: function() {
			
		}
	});
	$( "#setrequwindows" ).dialog('open');
}
//移除等待挂接的档案文件
function deletearchive() {
	var selectRows = attGridConfig.grid.getSelectedRows();
	selectRows.sort(function compare(a, b) {
		return b - a;
	});
	if (selectRows.length > 0) {
		for ( var i = 0; i < selectRows.length; i++) {
			var item = attGridConfig.dataView.getItem(selectRows[i]);
			attGridConfig.dataView.deleteItem(item.id);

            var removeItem = [];

            //判断当前档案是否挂接了全文
            for (var j = 0;j<archiveCommon.yesItems.length;j++) {
                var tempItem = archiveCommon.yesItems[j];
                //如果当前档案挂接了，移除对应关系
                if (tempItem.fileid == item.id) {
                    removeItem.push(j);
//                    archiveCommon.yesItems.splice(j,1);
                    tempItem.fileid = '';
                    tempItem.treeid = '';
                    //将全文对象加入到未挂接全文列表
                    attNoGridConfig.dataView.addItem(tempItem);
                }
            }

            for (var z = removeItem.length;z > 0;z--) {
                archiveCommon.yesItems.splice(removeItem[z-1],1);
            }

            attYesGridConfig.dataView = [];
		}
	}
	else {
		us.openalert('<span style="color:red">请选择要移除的数据!<span>',
				'系统提示',
				'alertbody alert_Information'
		);
	}
}
//保存挂接
function savearchive() {
	var a = archiveCommon.yesItems;
	if (a.length > 0) {
		var bb = encodeURIComponent(JSON.stringify(a));
		var par = "items=" + bb + "&tableType=" + archiveCommon.tableType + "&treeid=" + archiveCommon.selectTreeid;
		$.post("saveBatchAttArchive.action",par,function(data){
			us.openalert(data,
					'系统提示',
					'alertbody alert_Information'
			);
		});
	}
	else {
		us.openalert('<span style="color:red">没有找到挂接数据!<span></br>请重新挂接或与管理员联系。',
				'系统提示',
				'alertbody alert_Information'
		);
	}
}
// 自动挂接
function autofile() {
	//得到档案记录items
	var attItems = attGridConfig.dataView.getItems();
	if (attItems.length < 1) {
		us.openalert('没有找到要挂接的档案记录，请重新操作。',
				'系统提示',
				'alertbody alert_Information'
		);
		return;
	}
	//得到未挂接的全文items
	var attNoItems = attNoGridConfig.dataView.getItems();
	if (attNoItems.length < 1) {
		us.openalert('没有找到要挂接的全文记录，请重新操作。',
				'系统提示',
				'alertbody alert_Information'
		);
		return;
	}

	for (a in attItems) {
		for (var i=attNoItems.length-1;i>=0;i-- ) {
			if (attItems[a][archiveCommon.archiveField] != "") {
				if (attNoItems[i][archiveCommon.fileField].indexOf(attItems[a][archiveCommon.archiveField]) > -1) {
					//如果符合条件，则移入挂接全文grid
					var fileid = "";
					fileid = attItems[a].id;
					//添加到挂接全文grid
					var item = attNoItems[i];
					item.fileid = fileid;
					item.treeid = archiveCommon.selectTreeid;
					attYesGridConfig.dataView.addItem(item);
                    archiveCommon.yesItems.push(item);
					//
					attNoGridConfig.dataView.deleteItem(item.docid);
				}
			}

		}
	}
//	archiveCommon.yesItems = attYesGridConfig.dataView.getItems();
}
//手动挂接
function selffile() {
	var attSelectRows = attGridConfig.grid.getSelectedRows();
	if (attSelectRows.length != 1) {
		us.openalert('手动挂接只能选择一个档案记录，请重新选择。',
				'系统提示',
				'alertbody alert_Information'
		);
		return;
	}
	var fileid = "";
	var selectArchiveitem = attGridConfig.dataView.getItem(attSelectRows[0]);
	fileid = selectArchiveitem.id;
	var attNoSelectRows = attNoGridConfig.grid.getSelectedRows();
	attNoSelectRows.sort(function compare(a, b) {
		return a - b;
	});
	if (attNoSelectRows.length > 0) {
		attYesGridConfig.dataView.beginUpdate();
		for ( var i = 0; i < attNoSelectRows.length; i++) {
			var item = attNoGridConfig.dataView.getItem(attNoSelectRows[i]);
			item.fileid = fileid;
			item.treeid = archiveCommon.selectTreeid;
			attYesGridConfig.dataView.addItem(item);
            archiveCommon.yesItems.push(item);
		};
		attYesGridConfig.dataView.endUpdate();
		attNoSelectRows.sort(function compare(a, b) {
			return b - a;
		});
		for ( var i = 0; i < attNoSelectRows.length; i++) {
			var item = attNoGridConfig.dataView.getItem(attNoSelectRows[i]);
			attNoGridConfig.dataView.deleteItem(item.docid);
		};
	}
	else {
		us.openalert('请选择要手动挂接的电子全文数据。',
				'系统提示',
				'alertbody alert_Information'
		);
	}
//	archiveCommon.yesItems = attYesGridConfig.dataView.getItems();
}
//上传文件后，刷新“未挂接文件”
function uploadend() {
    getNoAttData();
    attNoGridConfig.dataView.beginUpdate();
    attNoGridConfig.dataView.setItems(attNoGridConfig.rows);
    attNoGridConfig.dataView.endUpdate();
}

//上传电子文件
function uploadfile() {
    var url = 'docUpload.action';
    us.upload_multi(url,'uploadend');


//	$("#uploader").pluploadQueue({
//        // General settings
//        runtimes : 'flash,html5,html4',
//        url : 'docUpload.action',
//        max_file_size : '200mb',
//        //缩略图形式。
////        resize : {width :32, height : 32, quality : 90},
////        unique_names : true,
//        chunk_size: '2mb',
//        // Specify what files to browse for
////        filters : [
////                   {title : "所有文件", extensions : "*.*"},
////                   {title : "Image files", extensions : "jpg,gif,png"},
////		           {title : "rar files", extensions : "rar"},
////		           {title : "pdf files", extensions : "pdf"},
////		           {title : "office files", extensions : "docx,ppt,pptx,xls,xlsx"},
////		           {title : "exe files", extensions : "exe"},
////		           {title : "Zip files", extensions : "zip,rar,exe"}
////
////        ],
//
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
//        	us.openalert('请先上传数据文件。',
//    				'系统提示',
//    				'alertbody alert_Information'
//    		);
//        }
//        return false;
//    });
//    $( "#uploadFile" ).dialog( "open");
}
//移除未挂接文件
function removefile() {
	var selectRows = attNoGridConfig.grid.getSelectedRows();
	selectRows.sort(function compare(a, b) {
		return b - a;
	});
	if (selectRows.length > 0) {
		for ( var i = 0; i < selectRows.length; i++) {
			var item = attNoGridConfig.dataView.getItem(selectRows[i]);
			attNoGridConfig.dataView.deleteItem(item.docid);
		}
	}
	else {
		us.openalert('请选择要移除的未挂接全文数据。',
				'系统提示',
				'alertbody alert_Information'
		);
	}
}
//物理  删除未挂接电子文件
function deletefile() {
    var selectRows = attNoGridConfig.grid.getSelectedRows();
    selectRows.sort(function compare(a, b) {
        return b - a;
    });
    var idStr = "";
    if (selectRows.length > 0) {
        for ( var i = 0; i < selectRows.length; i++) {
            var item = attNoGridConfig.dataView.getItem(selectRows[i]);
            attNoGridConfig.dataView.deleteItem(item.docid);
            idStr += item.docid + ",";
        }
        idStr = idStr.substring(0,idStr.length-1);
        var par = "nodocid=" + idStr;
        $.ajax({
            async : false,
            url : "deleteno.action?" + par,
            type : 'post',
            dataType : 'script',
            success : function(data) {
                if (data != "error") {
                    uploadend();
                } else {
                    us.openalert('<span style="color:red">删除未挂接文件时出错!<span></br>请关闭浏览器，重新登录尝试或与管理员联系。',
                        '系统提示',
                        'alertbody alert_Information'
                    );
                }
            }
        });
    }
    else {
        us.openalert('请选择要删除的未挂接全文数据。',
            '系统提示',
            'alertbody alert_Information'
        );
    }
}

//移除已挂接的电子文件
function deleteyesfile() {
	var selectRows = attYesGridConfig.grid.getSelectedRows();
	selectRows.sort(function compare(a, b) {
		return a - b;
	});
	if (selectRows.length > 0) {
		//先在未挂接文件grid里增加
		for ( var i = 0; i < selectRows.length; i++) {
			var item = attYesGridConfig.dataView.getItem(selectRows[i]);
			attNoGridConfig.dataView.addItem(item);
		};
		selectRows.sort(function compare(a, b) {
			return b - a;
		});
		for ( var i = 0; i < selectRows.length; i++) {
			var item = attYesGridConfig.dataView.getItem(selectRows[i]);
			attYesGridConfig.dataView.deleteItem(item.docid);
            for(var j=0;j<archiveCommon.yesItems.length;j++) {
                var tempItem = archiveCommon.yesItems[j];
                if (tempItem.docid == item.docid) {
                    archiveCommon.yesItems.splice(j,1);
                    break;
                }
            }
		}
	}
	else {
		us.openalert('请选择要移除的数据。',
				'系统提示',
				'alertbody alert_Information'
		);
	}
//	archiveCommon.yesItems = attYesGridConfig.dataView.getItems();
}


function show_batchAtt_list() {

    archiveCommon.yesItems = [];

    var par = "treeid=" + archiveCommon.selectTreeid + "&tableType=" + archiveCommon.tableType + "&importType=1";
    $.ajax({
        async : false,
        url : "getField.action?" + par,
        type : 'post',
        dataType : 'script',
        success : function(data) {
            if (data != "error") {
                attGridConfig.columns_fields = fields;
//                attGridConfig.fieldsDefaultValue = fieldsDefaultValue;
            } else {
                us.openalert('<span style="color:red">读取字段信息时出错!<span></br>请关闭浏览器，重新登录尝试或与管理员联系。',
                    '系统提示',
                    'alertbody alert_Information'
                );
            }
        }
    });
//    attGridConfig.dataView.length = 0;
//    for ( var i = 0; i < archiveCommon.items.length; i++) {
//        attGridConfig.dataView.addItem(archiveCommon.items[i]);
//    };
    attGridConfig.dataView.setItems(archiveCommon.items);

    var visibleColumns_att = [];//定义一个数组存放显示的列

//                // 加入其他列
    for ( var i = 0; i < attGridConfig.columns_fields.length; i++) {
        visibleColumns_att.push(attGridConfig.columns_fields[i]);
    }

    //设置grid的列
    attGridConfig.grid.setColumns(visibleColumns_att);

    attGridConfig.grid.setSelectionModel(new Slick.RowSelectionModel());

    attGridConfig.grid.onSelectedRowsChanged.subscribe(function(e,args) {
        var attYesItems = archiveCommon.yesItems;  //attYesGridConfig.dataView.getItems();
        if (attYesItems.length > 0) {
            if (args.rows.length > 0) {
                attYesGridConfig.dataView.setItems([]);
                for (var i=0;i<args.rows.length;i++) {
                    var item = attGridConfig.dataView.getItem(args.rows[i]);
                    for (var j=0;j<attYesItems.length;j++) {
                        if (item.id == attYesItems[j].fileid) {
                            attYesGridConfig.dataView.addItem(attYesItems[j]);
                        }
                    }
                }
            }
        }
    });


    //初始化待挂接文件grid
    //创建待挂接文件gird
    getNoAttData();
    attNoGridConfig.columns = [
        {id: "docoldname", name: "文件名", field: "docoldname" },
        {id: "docext", name: "文件类型", field: "docext" },
        {id: "doclength", name: "文件大小", field: "doclength" },
        {id: "creater", name: "上传者", field: "creater" },
        {id: "createtime", name: "上传时间", field: "createtime" }
    ];
    attNoGridConfig.dataView.setItems(attNoGridConfig.rows);
    //设置grid的列
    attNoGridConfig.grid.setColumns(attNoGridConfig.columns);
    attNoGridConfig.grid.setSelectionModel(new Slick.RowSelectionModel());

    //已挂接文件gird
    attYesGridConfig.columns = [
        {id: "docoldname", name: "文件名", field: "docoldname" },
        {id: "docext", name: "文件类型", field: "docext" },
        {id: "doclength", name: "文件大小", field: "doclength" },
        {id: "creater", name: "上传者", field: "creater" },
        {id: "createtime", name: "上传时间", field: "createtime" }
//        {id: "fileid", name: "id", field: "fileid" }
    ];

    //设置grid的列
    attYesGridConfig.grid.setColumns(attYesGridConfig.columns);
    attYesGridConfig.grid.setSelectionModel(new Slick.RowSelectionModel());

}


$(function() {
    $('#batchAtttab').hide();

    //创建档案gird
    attGridConfig.options.editable = false;
    attGridConfig.is_add_new_item = false;
    attGridConfig.is_cellchange = false;
    attGridConfig.is_pager = false;
    attGridConfig.init_grid(attGridConfig,"#archiveAttachmentDiv","","");


    attNoGridConfig.options.editable = false;
    attNoGridConfig.is_add_new_item = false;
    attNoGridConfig.is_cellchange = false;
    attNoGridConfig.is_pager = false;
    attNoGridConfig.init_grid(attNoGridConfig,"#archiveAttachmentDiv-no","","","docid");

    attYesGridConfig.options.editable = false;
    attYesGridConfig.is_add_new_item = false;
    attYesGridConfig.is_cellchange = false;
    attYesGridConfig.is_pager = false;
    attYesGridConfig.init_grid(attYesGridConfig,"#archiveAttachmentDiv-yes","","","docid");

});

//读取未挂接电子文件
function getNoAttData() {
	//同步读取当前帐户上传到全文库中的电子文件数据
	$.ajax({
		async : false,
		url : "listNoLinkDocAsAccount.action",
		type : 'post',
		dataType : 'script',
		success : function(data) {
			if (data != "error") {
				attNoGridConfig.rows = eval(data);
			} else {
				us.openalert('<span style="color:red">读取全文库中未挂接数据时出错!<span></br>请尝试重新操作或与管理员联系。',
						'系统提示',
						'alertbody alert_Information'
				);
			}
		}
	});
}

