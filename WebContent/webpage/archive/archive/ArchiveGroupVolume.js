
//组卷-AJ
var volumeGridconfig = new us.archive.ui.Gridconfig();
//文件
var organizeGridconfig = new us.archive.ui.Gridconfig();


$(function() {
	
	volumeGridconfig.tabletype = '01';
	volumeGridconfig.options.editable = false;
    volumeGridconfig.is_add_new_item = false;
    volumeGridconfig.is_cellchange = false;
    volumeGridconfig.is_pager = false;
	volumeGridconfig.init_grid(volumeGridconfig,"#archiveGroupVolumeDiv","","");
	
	organizeGridconfig.tabletype = '02';
	organizeGridconfig.options.editable = false;
    organizeGridconfig.is_add_new_item = false;
    organizeGridconfig.is_cellchange = false;
    organizeGridconfig.is_pager = false;
	organizeGridconfig.init_grid(organizeGridconfig,"#organizefileDiv","","");
	
	//归档组卷
	var resizeTimer = null;
	$('#gdzj').click(function(){
		var treeid = archiveCommon.selectTreeid;
		if(treeid!=''){
			if (resizeTimer) clearTimeout(resizeTimer);
			//需要延迟一会，要不然数据不显示
			if(archiveCommon.archiveType == "F"){
				//纯文件级
				$('#gdzjWjDiv').css({"display":"none"});
				$('#gdzjAjDiv').css({"height":"410"});
				
				resizeTimer = setTimeout("show_zj_archive_list('01',volumeGridconfig,0)", 100);
			}else{
				//文件级
				$('#gdzjWjDiv').css({"display":"block"});
				$('#gdzjAjDiv').css({"height":"210"});
				resizeTimer = setTimeout("show_zj_archive_list('01',volumeGridconfig,0)", 100);
				archiveCommon.isAllWj=0; //文件级
		        archiveCommon.selectAid='';//初始为空
		        resizeTimer = setTimeout("show_zj_archive_list('02',organizeGridconfig,1)", 100);
			}
		}
	});
	
});

/**
 *  同步读取数据
 *  @param tableType
 *  @param gridObject
 *  @param type 0:组卷案卷；1:文件级
 **/
function readData(tableType,gridObject,type) {
	//status=1 整编组卷
    var par = "";
    if(type==0){
    	par = "treeid=" + archiveCommon.selectTreeid + "&tableType="+tableType+"&status=1";
    }else{
    	par = "treeid=" + archiveCommon.selectTreeid + "&tableType="+tableType+"&status=1"+"&isAllWj="+ archiveCommon.isAllWj+"&selectAid=" + archiveCommon.selectAid;
    }
    $.ajax({
        async : false,
        url : "listArchiveF.action?" + par,
        type : 'post',
        dataType : 'script',
        success : function(data) {
            if (data != "error") {
                gridObject.rows = rowList;
                gridObject.dataView.setItems(gridObject.rows);
                // 创建checkbox列
                var checkboxSelector = new Slick.CheckboxSelectColumn({
                    cssClass : "slick-cell-checkboxsel"
                });
                var visibleColumns = [];//定义一个数组存放显示的列
                visibleColumns.push(checkboxSelector.getColumnDefinition());//将columns的第一列push进去
//                // 加入其他列
                for ( var i = 0; i < gridObject.columns_fields.length; i++) {
                    visibleColumns.push(gridObject.columns_fields[i]);
                }
                //设置grid的列
                gridObject.grid.setColumns(visibleColumns);
//                gridObject.dataView.beginUpdate();
//                gridObject.dataView.setItems(gridObject.rows);
                gridObject.dataView.setFilterArgs({
                    searchString : archiveCommon.searchString
                });
                gridObject.dataView.setFilter(us.myFilter);
//                gridObject.dataView.endUpdate();
                // 注册grid的checkbox功能插件
                gridObject.grid.registerPlugin(checkboxSelector);
//
//                //生成过滤选择字段
//                $("#selectfield_aj").empty();
//                for (var i=0;i<gridObject.columns_fields.length;i++) {
//                    if (gridObject.columns_fields[i].id != "rownum" && gridObject.columns_fields[i].id != "isdoc" && gridObject.columns_fields[i].id != "files") {
//                        $("#selectfield_aj").append("<option value='"+gridObject.columns_fields[i].id+"'>"+gridObject.columns_fields[i].name+"</option>");
//                    }
//                }

              //新建行时，将系统必须的默认值与字段默认值合并
                gridObject.newItemTemplate = $.extend({},gridObject.newItemTemplate,gridObject.fieldsDefaultValue);
                gridObject.grid.invalidate();
            } else {
                us.openalert('<span style="color:red">读取数据时出错.</span></br>请关闭浏览器，重新登录尝试或与管理员联系!',
                    '系统提示',
                    'alertbody alert_Information'
                );
            }
        }
    });
}

/**
 *  归档组卷-显示列表
 *  @param tableType 
 *  @param gridObject
 *  @param type 0：组卷案卷；1：文件级  
 * */
function show_zj_archive_list(tableType,gridObject,type) {
    //同步读取字段
    var par = "treeid=" + archiveCommon.selectTreeid + "&tableType="+tableType+"&importType=0";
    $.ajax({
        async : false,
        url : "getField.action?" + par,
        type : 'post',
        dataType : 'script',
        success : function(data) {
            if (data != "error") {
                gridObject.columns_fields = fields;
                gridObject.fieldsDefaultValue = fieldsDefaultValue;
                gridObject.selectTableid = tableid;
//                var temptype = templettype;
//                if (temptype == "F") {
//                    $('#allwj').button("disable");
//                }
            } else {
                us.openalert('<span style="color:red">读取字段信息时出错.</span></br>请关闭浏览器，重新登录尝试或与管理员联系!',
                    '系统提示',
                    'alertbody alert_Information'
                );
            }
        }
    });
    readData(tableType,gridObject,type);
}

//拆卷
function unwind(){
	var selectRows = volumeGridconfig.grid.getSelectedRows();
	if (selectRows.length == 1) {
		selectRows.sort(function compare(a, b) {
			return a - b;
		});
		var prompt='确定要进行拆卷吗？ ';
		bootbox.confirm(prompt, function(result) {
            if(result){
            	var sRows = [];
        		for ( var i = 0; i < selectRows.length; i++) {
					var item = volumeGridconfig.dataView.getItem(selectRows[i]);
					sRows.push(item);
				}
        		var tableType = "tableType=02"; //默认为文件级
        		//文件类型
        		if(archiveCommon.archiveType == "F"){
        			//纯文件级
        			tableType = "tableType=01";
        		}
        		var par = "par=" + JSON.stringify(sRows) + "&"+tableType;//"&tableType=02";
        		$.post("hitchBacks.action",par,function(data){
    				if (data == "SUCCESS") {
    					$('#gdzj').click();
    					us.openalert('已成功拆卷。 ','系统提示','alertbody alert_Information');
    				}else {
    					us.openalert(data,'系统提示','alertbody alert_Information');
    				}
        		});
            }
        });
		
	}else if(selectRows.length > 1){
		us.openalert('只能选择一条档案进行拆卷!',
				'系统提示',
				'alertbody alert_Information'
		);
	}else {
		us.openalert('请选择要拆卷的记录! ',
				'系统提示',
				'alertbody alert_Information'
		);
	}
}

//归档
function file(){
	var selectRows = volumeGridconfig.grid.getSelectedRows();
	if (selectRows.length == 1) {
		selectRows.sort(function compare(a, b) {
			return a - b;
		});
		var prompt='确定要进行归档吗？ <br><span style="color:red">注：归档后将不能进行更改操作！</span>';
		bootbox.confirm(prompt, function(result) {
            if(result){
            	var fileRows = [];
        		for ( var i = 0; i < selectRows.length; i++) {
					var item = volumeGridconfig.dataView.getItem(selectRows[i]);
					fileRows.push(item);
				}
        		var tableType = "tableType=02"; //默认为文件级
        		//文件类型
        		if(archiveCommon.archiveType == "F"){
        			//纯文件级
        			tableType = "tableType=01";
        		}
        		var par = "par=" + JSON.stringify(fileRows) + "&"+tableType;
        		$.post("filing.action",par,function(data){
    				if (data == "SUCCESS") {
        				us.openalert('已成功归档。 ','系统提示','alertbody alert_Information');
        				$('#dan').click();
    				}else {
    					us.openalert(data,'系统提示','alertbody alert_Information');
    				}
        		});
            }
        });
		
	}else if(selectRows.length > 1){
		us.openalert('只能选择一条档案进行归档!',
				'系统提示',
				'alertbody alert_Information'
		);
	}else {
		us.openalert('请选择要归档的记录! ',
				'系统提示',
				'alertbody alert_Information'
		);
	}
}
/**
 * 打开文件页签
 * @param id		打开的文件所属案卷id
 * @param isAllWj	是否现在该节点下全部文件。
 */
function showWjTab(id,isAllWj) {
	archiveCommon.selectAid = id;
	archiveCommon.isAllWj = isAllWj;
	show_zj_archive_list('02',organizeGridconfig,1);
	//档案的--方式不好
	show_archive_list('02',filingGridconfigWj,1);
}

