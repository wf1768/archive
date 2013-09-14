
//组卷
var volumeGridconfigPre = new us.archive.ui.Gridconfig();
//文件
var organizeGridconfigPre = new us.archive.ui.Gridconfig();


$(function() {
	
	volumeGridconfigPre.tabletype = '01';
    volumeGridconfigPre.is_add_new_item = false;
    volumeGridconfigPre.is_cellchange = false;
    volumeGridconfigPre.is_pager = false;
	volumeGridconfigPre.init_grid(volumeGridconfigPre,"#archiveGroupVolumeDivPre","","");
	
	organizeGridconfigPre.tabletype = '02';
	organizeGridconfigPre.options.editable = false;
    organizeGridconfigPre.is_add_new_item = false;
    organizeGridconfigPre.is_cellchange = false;
    organizeGridconfigPre.is_pager = false;
	organizeGridconfigPre.init_grid(organizeGridconfigPre,"#organizefileDivPre","","");
	
	//预归档
	var resizeTimer = null;
	$('#ygd').click(function(){
		var treeid = archiveCommon.selectTreeid;
		if(treeid!=''){
			if (resizeTimer) clearTimeout(resizeTimer);
			//需要延迟一会，要不然数据不显示
			if(archiveCommon.archiveType == "F"){
				//纯文件级
				
			}else{
				//文件级
				resizeTimer = setTimeout("show_zj_archive_list_pre('01',volumeGridconfigPre,0)", 100);
		        resizeTimer = setTimeout("show_zj_archive_list_pre('02',organizeGridconfigPre,1)", 100);
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
function readDataPre(tableType,gridObject,type) {
	//status=1 整编组卷
    var par = "";
    if(type==0){
//    	 gridObject.grid.onCellChange.subscribe(function(e, args) {
//            var item = args.item;
//            var par1 = "par=" + JSON.stringify(item);
//            alert(par1);
//        });
    	gridObject.dataView.setItems(archiveCommon.yesItems);
    }else{
    	gridObject.dataView.setItems(archiveCommon.items);
    }
                
    // 创建checkbox列
//    var checkboxSelector = new Slick.CheckboxSelectColumn({
//        cssClass : "slick-cell-checkboxsel"
//    });
    var visibleColumns = [];//定义一个数组存放显示的列
//    visibleColumns.push(checkboxSelector.getColumnDefinition());//将columns的第一列push进去
    // 加入其他列
    for ( var i = 0; i < gridObject.columns_fields.length; i++) {
        visibleColumns.push(gridObject.columns_fields[i]);
    }
    //设置grid的列
    gridObject.grid.setColumns(visibleColumns);
    gridObject.dataView.setFilterArgs({
        searchString : archiveCommon.searchString
    });
    gridObject.dataView.setFilter(us.myFilter);
    // 注册grid的checkbox功能插件
//    gridObject.grid.registerPlugin(checkboxSelector);

  //新建行时，将系统必须的默认值与字段默认值合并
    gridObject.newItemTemplate = $.extend({},gridObject.newItemTemplate,gridObject.fieldsDefaultValue);
    gridObject.grid.invalidate();
}

/**
 *  归档组卷-显示列表
 *  @param tableType 
 *  @param gridObject
 *  @param type 0：组卷案卷；1：文件级  
 * */
function show_zj_archive_list_pre(tableType,gridObject,type) {
    //同步读取字段
    var par = "treeid=" + archiveCommon.selectTreeid + "&tableType="+tableType+"&importType=1";
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
            } else {
                us.openalert('<span style="color:red">读取字段信息时出错.</span></br>请关闭浏览器，重新登录尝试或与管理员联系!',
                    '系统提示',
                    'alertbody alert_Information'
                );
            }
        }
    });
    readDataPre(tableType,gridObject,type);
}


//归档组卷
function filing(){
	var item = volumeGridconfigPre.dataView.getItems();
	if(item!=''){
		var prompt='确定要归档组卷吗？ ';
		bootbox.confirm(prompt, function(result) {
	        if(result){
	    		//案卷是否为新增
	    		var flag = archiveCommon.isNew;
	    		if(flag){
	    			var par = "importData=" + JSON.stringify(item) + "&tableType=01";
	                $.post("saveImportArchive.action",par,function(data){
	                        if (data != "保存成功！") {
	                            us.openalert(data,
	                                '系统提示',
	                                'alertbody alert_Information'
	                            );
	                        }else{
	                        	updateWj();
	                        }
	                    }
	                );
	    		}else{
	    			var par = "par=" + JSON.stringify(item) + "&tableType=02";
	    			$.post("archiveGroup.action",par,function(data){
	    				if (data == "SUCCESS") {
							updateWj();
	    				}else {
	    					us.openalert(data,'系统提示','alertbody alert_Information');
	    				}
	        		});
	    		}
	        }
	    });
	}else{
		us.openalert('没有要组卷的数据！','系统提示');
	}
}
//文件归档组卷
function updateWj(){
	var par = "importData=" + JSON.stringify(archiveCommon.items) + "&tableType=02";
    $.post("updateImportArchive.action",par,function(data){
            if (data != "保存完毕。") {
            	us.openalert(data,'系统提示','alertbody alert_Information');
            }else{
            	//清空预归档
            	archiveCommon.yesItems = [];
            	archiveCommon.items = [];
            	$('#gdzj').click();
            }
        }
    );
}


/**
 * 打开文件页签
 * @param id		打开的文件所属案卷id
 * @param isAllWj	是否现在该节点下全部文件。
 */
function showWjTab(id,isAllWj) {
	show_zj_archive_list_pre('02',organizeGridconfigPre,1);
}

