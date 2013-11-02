

var wjGridconfig = new us.archive.ui.Gridconfig();
var loader_wj = new Slick.Data.RemoteModel();
var loading_wj = new us.archive.ui.loading();

//wj insert a new row
function wjadd() {
	wjGridconfig.addItem(wjGridconfig);
	readwjdata();
//	wjGridconfig.grid.setOptions({
//		autoEdit : true
//	});
//	wjGridconfig.grid.gotoCell(wjGridconfig.dataView.getLength(),3,true);
}
//wj data delete
function wjdelete() {
	var selectRows = wjGridconfig.grid.getSelectedRows();
	selectRows.sort(function compare(a, b) {
		return b - a;
	});

    var deleteRows = [];

    for ( var i = 0; i < selectRows.length; i++) {
        var item = wjGridconfig.dataView.getItem(selectRows[i]);
        var item_tmp = {};
        item_tmp.id = item.id;
        item_tmp.treeid = item.treeid;
        deleteRows.push(item_tmp);
    }

	if (deleteRows.length > 0) {
        bootbox.confirm("确定要删除选中的 <span style='color:red'>"+deleteRows.length+"</span> 条文件记录吗?<br> <font color='red'>" +
            "注意：删除文件记录，将同时删除文件数据、电子全文，请谨慎操作！</font> ", function(result) {
            if(result){
            	var jsonString = JSON.stringify(deleteRows);
        		jsonString = jsonString.replace(/%/g,"%25");
        		jsonString = jsonString.replace(/\&/g,"%26");
        		jsonString = jsonString.replace(/\+/g,"%2B");
        		
                var par = "par=" + jsonString + "&tableType=02";
                $.post("deleteArchive.action",par,function(data){
                        if (data == "SUCCESS") {
                            readwjdata();
                            us.openalert('删除成功。 ','系统提示','alertbody alert_Information');
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
//open wj batch update dialog
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
            us.batchUpdate(wjGridconfig.grid,wjGridconfig.dataView,true,'02');
        });
        $('#batchwindows').modal('show');
	}
	else {
		us.openalert('请选择要修改的数据。');
	}
}
//open wj import data tab
function wjimport() {
    openImportWindows('02');
}
//show wj grid filter div
function wjfilter() {
    wjGridconfig.grid.setTopPanelVisibility(!wjGridconfig.grid.getOptions().showTopPanel);
}
// wj Single archive link files
function wjlinkfile() {
	var selectRows = wjGridconfig.grid.getSelectedRows();
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
	var item = wjGridconfig.dataView.getItem(selectRows[0]);
	showDocwindow(item.id,wjGridconfig.selectTableid);
}

//open batch att tab
function wjbatchatt() {
	var selectRows = wjGridconfig.grid.getSelectedRows();
	if (selectRows.length > 0) {
		selectRows.sort(function compare(a, b) {
			return a - b;
		});
		archiveCommon.showBatchAttachment(wjGridconfig,'02',selectRows);
	}
	else {
		us.openalert('请选择要批量挂接的档案记录! ',
				'系统提示',
				'alertbody alert_Information'
		);
	}
}
//refresh wjdata
function wjrefresh() {
//	var data = [];
	readwjdata();
//	wjGridconfig.dataView.setItems(data);
//	wjGridconfig.dataView.setItems(wjGridconfig.rows);
}

//read wj Archive data
function readwjdata() {
	
//	var loading = new us.archive.ui.loading();
	loading_wj.show("wjdiv");
	
    wjGridconfig.dataView.setItems([]);
    
	//同步读取数据
	var par = "treeid=" + archiveCommon.selectTreeid + "&tableType=02&isAllWj=" + archiveCommon.isAllWj +"&selectAid=" + archiveCommon.selectAid;
	var url = "listArchiveP.action?"+par;
    wjGridconfig.pageList(url,wjGridconfig,loader_wj,"txtSearch_wj","selectfield_wj");
    loading_wj.remove();
    
    
//	$.ajax({
//		async : false,
//		url : "listArchive.action?" + par,
//		type : 'post',
//		dataType : 'script',
//		success : function(data) {
//			if (data != "error") {
//				wjGridconfig.rows = rowList;
//
//                wjGridconfig.dataView.setItems(wjGridconfig.rows);

                // 创建checkbox列
                var checkboxSelector_wj = new Slick.CheckboxSelectColumn({
                    cssClass : "slick-cell-checkboxsel"
                });
                var visibleColumns_wj = [];//定义一个数组存放显示的列
                visibleColumns_wj.push(checkboxSelector_wj.getColumnDefinition());//将columns的第一列push进去

//                // 加入其他列
                for ( var i = 0; i < wjGridconfig.columns_fields.length; i++) {
                    visibleColumns_wj.push(wjGridconfig.columns_fields[i]);
                }

                //设置grid的列
                wjGridconfig.grid.setColumns(visibleColumns_wj);

//                wjGridconfig.dataView.beginUpdate();
//                wjGridconfig.dataView.setItems(wjGridconfig.rows);
                wjGridconfig.dataView.setFilterArgs({
                    searchString : archiveCommon.searchString
                });
                wjGridconfig.dataView.setFilter(us.myFilter);
//                wjGridconfig.dataView.endUpdate();

                // 注册grid的checkbox功能插件
                wjGridconfig.grid.registerPlugin(checkboxSelector_wj);

                //生成过滤选择字段
                $("#selectfield_wj").empty();
                for (var i=0;i<wjGridconfig.columns_fields.length;i++) {
                    if (wjGridconfig.columns_fields[i].id != "rownum" && wjGridconfig.columns_fields[i].id != "isdoc" && wjGridconfig.columns_fields[i].id != "files") {
                        $("#selectfield_wj").append("<option value='"+wjGridconfig.columns_fields[i].id+"'>"+wjGridconfig.columns_fields[i].name+"</option>");
                    }
                }

                //新建行时，将系统必须的默认值与字段默认值合并
                if (archiveCommon.isAllWj == '1') {
                    wjGridconfig.options.enableAddRow = false;
                }
                wjGridconfig.tabletype = '02';
                wjGridconfig.newItemTemplate = {
                    isdoc	: "0",
                    parentid : archiveCommon.selectAid,
                    status	: "0"
                };
                wjGridconfig.newItemTemplate = $.extend({},wjGridconfig.newItemTemplate,wjGridconfig.fieldsDefaultValue);

                wjGridconfig.grid.invalidate();
//                loading.remove();
//
//			} else {
//				us.openalert('<span style="color:red">读取数据时出错!<span></br>请尝试重新操作或与管理员联系。',
//						'系统提示',
//						'alertbody alert_Information'
//				);
//			}
//		}
//	});
}

function show_wj_archive_list() {

    var par = "treeid=" + archiveCommon.selectTreeid + "&tableType=02&importType=0";
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
    $('#wjtab').click();
    readwjdata();
    //模拟点击了页签文件级<a>，转到文件级页签
    $("#grid_header_wj").html('<h3>'+archiveCommon.selectTreeName + '_文件级档案列表'+'</h3>');

}

$(function(){

    wjGridconfig.tabletype = '02';
    wjGridconfig.init_grid(wjGridconfig,"#wjdiv","pager_wj","inlineFilterPanel_wj");
	
//	$("#txtSearch_wj").keyup(function(e) {
//		Slick.GlobalEditorLock.cancelCurrentEdit();
//		// clear on Esc
//		if (e.which == 27) {
//			this.value = "";
//		}
//		archiveCommon.clName = $("#selectfield_wj").val();
//		archiveCommon.searchString = this.value;
//		us.updateFilter(wjGridconfig.dataView);
//	});

	//如果是显示全部文件。屏蔽导入按钮
	if (archiveCommon.isAllWj == '1') {
		$('#wjimport').button("disable"); 
		$('#wjadd').button("disable"); 
		
	}
});
