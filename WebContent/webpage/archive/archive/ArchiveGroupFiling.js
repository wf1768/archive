var filingGridconfigAj = new us.archive.ui.Gridconfig();
var filingGridconfigWj = new us.archive.ui.Gridconfig();
var loader_Aj = new Slick.Data.RemoteModel();
var loader_Wj = new Slick.Data.RemoteModel();
var loading_aj = new us.archive.ui.loading();
var loading_wj = new us.archive.ui.loading();
$(function() {
    filingGridconfigAj.tabletype = '01';
    filingGridconfigAj.options.editable = false;
    filingGridconfigAj.is_add_new_item = false;
    filingGridconfigAj.is_cellchange = false;
    filingGridconfigAj.is_pager = false;
    filingGridconfigAj.init_grid(filingGridconfigAj,"#archiveGroupFilingAjDiv","","");

    filingGridconfigWj.tabletype = '02';
    filingGridconfigWj.options.editable = false;
    filingGridconfigWj.is_add_new_item = false;
    filingGridconfigWj.is_cellchange = false;
    filingGridconfigWj.is_pager = false;
    filingGridconfigWj.init_grid(filingGridconfigWj,"#archiveGroupFilingWjDiv","","");

  //档案
	var resizeTimer = null;
	$('#dan').click(function(){
		archiveCommon.isZjOrDa = true;
		var treeid = archiveCommon.selectTreeid;
		if(treeid!=''){
			if (resizeTimer) clearTimeout(resizeTimer);
			//需要延迟一会，要不然数据不显示
			if(archiveCommon.archiveType == "F"){
				//纯文件级
				$('#daWjDiv').css({"display":"none"});
				$('#daAjDiv').css({"height":"410"});
				
				resizeTimer = setTimeout("show_archive_list('01',filingGridconfigAj,loader_Aj,0)", 100);
			}else{
				//文件级
				$('#daWjDiv').css({"display":"block"});
				$('#daAjDiv').css({"height":"210"});
				resizeTimer = setTimeout("show_archive_list('01',filingGridconfigAj,loader_Aj,0)", 100);
				archiveCommon.isAllWj=0; //文件级
		        archiveCommon.selectAid='';//初始为空
		        resizeTimer = setTimeout("show_archive_list('02',filingGridconfigWj,loader_Wj,1)", 100);
			}
		}
	});
	loader_Aj.onDataLoading.subscribe(function () {
		loading_aj.show("archiveGroupFilingAjDiv");
    });
	loader_Wj.onDataLoading.subscribe(function () {
		loading_wj.show("archiveGroupFilingWjDiv");
    });
})


/**
 *  同步读取数据
 *  @param tableType
 *  @param gridObject
 *  @param loader_Obj
 *  @param type 0:案卷；1:文件级
 **/
function readArchiveData(tableType,gridObject,loader_Obj,type) {
    var par = "";
    if(type==0){
    	par = "treeid=" + archiveCommon.selectTreeid + "&tableType="+tableType+"&status=0";
    }else{
    	par = "treeid=" + archiveCommon.selectTreeid + "&tableType="+tableType+"&status=0"+"&isAllWj="+ archiveCommon.isAllWj+"&selectAid=" + archiveCommon.selectAid;
    }
  //分页查询
    var url = "listArchiveP.action?"+par;
    pageListAj_Wj(url,gridObject,loader_Obj);
	
//    $.ajax({
//        async : false,
//        url : "listArchiveF.action?" + par,
//        type : 'post',
//        dataType : 'script',
//        success : function(data) {
//            if (data != "error") {
//                gridObject.rows = rowList;
//                gridObject.dataView.setItems(gridObject.rows);
                // 创建checkbox列
//                var checkboxSelector = new Slick.CheckboxSelectColumn({
//                    cssClass : "slick-cell-checkboxsel"
//                });
                var visibleColumns = [];//定义一个数组存放显示的列
//                visibleColumns.push(checkboxSelector.getColumnDefinition());//将columns的第一列push进去
//                // 加入其他列
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
//                gridObject.grid.registerPlugin(checkboxSelector);

              //新建行时，将系统必须的默认值与字段默认值合并
                gridObject.newItemTemplate = $.extend({},gridObject.newItemTemplate,gridObject.fieldsDefaultValue);
                gridObject.grid.invalidate();
//            } else {
//                us.openalert('<span style="color:red">读取数据时出错.</span></br>请关闭浏览器，重新登录尝试或与管理员联系!',
//                    '系统提示',
//                    'alertbody alert_Information'
//                );
//            }
//        }
//    });
}	
/**
 * 分页读取数据
 * @param url	
 **/
function pageListAj_Wj(url,gridObject,loader_Obj){
	loader_Obj.clear();
	loader_Obj.setPage(0);
	var data = [];
	gridObject.dataView.setItems(data);
	
	gridObject.grid.onViewportChanged.subscribe(function (e, args) {
      var vp = gridObject.grid.getViewport();
      loader_Obj.ensureData(vp.top, vp.bottom,url);
    });
    gridObject.grid.onSort.subscribe(function (e, args) {
//      loader_Obj.setSort(args.sortCol.field, args.sortAsc ? 1 : -1);
      var vp = gridObject.grid.getViewport();
      loader_Obj.ensureData(vp.top, vp.bottom,url);
    });

    loader_Obj.onDataLoaded.subscribe(function (e, args) {
    	var data = [];
    	if(args.flag){
	    	for (var i = args.from; i < (args.to+args.from); i++) {
	    		loader_Obj.data[i].rownum = i+1;
	    		data.push(loader_Obj.data[i]);
	    		gridObject.dataView.addItem(loader_Obj.data[i]);
	    	}
	    	args.flag = false;
	    	for (var i = args.from; i <= args.to; i++) {
	            gridObject.grid.invalidateRow(i);
	    	}

	    	gridObject.grid.updateRowCount();
	    	gridObject.grid.render();

	    	loading_aj.remove();
	    	loading_wj.remove();
    	}
    });
   
    gridObject.grid.setSortColumn("date", false);
    // load the first page
    gridObject.grid.onViewportChanged.notify();
    
}
/**
 *  档案-显示列表
 *  @param tableType 
 *  @param gridObject
 *  @param loader_Obj 
 *  @param type 0：案卷；1：文件级  
 * */
function show_archive_list(tableType,gridObject,loader_Obj,type) {
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
            } else {
                us.openalert('<span style="color:red">读取字段信息时出错.</span></br>请关闭浏览器，重新登录尝试或与管理员联系!',
                    '系统提示',
                    'alertbody alert_Information'
                );
            }
        }
    });
    readArchiveData(tableType,gridObject,loader_Obj,type);
}
/**
 * 打开文件页签
 * @param id		打开的文件所属案卷id
 * @param isAllWj	是否现在该节点下全部文件。
 */
//function showWjTab(id,isAllWj) {
//	alert('档案');
//	archiveCommon.selectAid = id;
//	archiveCommon.isAllWj = isAllWj;
//	show_archive_list('02',filingGridconfigWj,1);
//}
