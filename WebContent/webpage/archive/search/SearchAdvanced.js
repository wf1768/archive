var searchCommon = new us.archive.Search();
var orgid;
var tableFields;
var treeName; //所属档案库
$(function(){
	//隐藏分页信息
	$('.pagination').css('display','none');
	
	$("#archiveTree").jstree({
	    //加载账户组树
	    "json_data" : {
			"ajax" : {
				"url" : "getTree.action",
				"data" : function (n) { 
					return { nodeId : n.attr ? n.attr("id") : 0 }; //没用（该为动态加载用）
				},
				error:function(n){
					window.location.href="../../common/logout.jsp";
				}
			}
		},
		"plugins" : [ "themes","json_data","ui","crrm","types"],
		"core" : {
			"initially_open" : [ "0" ]
		},
		"types" : {
			"valid_children" : [ "root" ],
			"types" : {
				//设置rel=root的参数
				"root" : {
					//图标
					"icon" : { 
						"image" : "../../images/icons/house.png"
					},
					"valid_children" : [ "folder","default" ],
					"max_depth" : 2,
					"hover_node" : false,
					"select_node" : function () {return false;}
	
				},
				"default" : {
					"valid_children" : [ "none" ],
					"icon" : { 
						"image" : "../../images/icons/page.png" 
					}
				},
				"folder" : {
					"valid_children" : [ "default", "folder" ],
					"icon" : {
						"image" : "../../images/icons/folder.png"
					}
				}
			}
		}
	}).bind("select_node.jstree",function(e,data) { 
	    var node = data.rslt.obj, inst = data.inst;
	    orgid = node.attr("id");//选择后给全局orgid赋值
	    var rel = $("#"+node.attr("id")).attr("rel");
	    if(rel=="default"){
	    	treeName = $("#"+node.attr("id")+" a").first().text(); // 文件所属档案库
	    	readField(orgid,'01'); // 初始读取案卷
	   		$(".query-item").remove(); //清空条件
	    	$("#tableType").val("");   //初始
	    }
	    
	    if (node.hasClass('jstree-closed')) { return inst.open_node(node); }
	    if (node.hasClass('jstree-open')) { return inst.close_node(node); }
	});
	
	//删除动态条件
	$('button.delete-query-item').live('click', function () {
	    $(this).parent().remove();
	    return false;
	});

});

//同步读取当前节点的字段
function readField(treeid,tableType) {
    $.ajax({
        async: false,
        url: "getFieldAdvanced.action",
        type: 'post',
        dataType: 'script',
        data: {treeid: treeid, tableType: tableType},
        success: function (data) {
            if (data != "error") {
                field = eval(fields);
                tableFields = field;
                talbeField(field); //字段
                var html = "";
                if(templeType=="A"){ //案卷
                	html += '<label class="radio"><input id="templeType_A" type="radio" name="templeType" onclick="getTableType(this.value)" value="01" /> 案卷级</label>';
					html += '<label class="radio"><input id="templeType_W" type="radio" name="templeType" onclick="getTableType(this.value)" value="02" /> 文件级</label>';
                }else{
					html += '<label class="radio"><input id="templeType_W" type="radio" name="templeType" onclick="getTableType(this.value)" value="01" /> 文件级</label>';                	
                }
                $("#templeType").html(html);
                searchCommon.templettype = templeType;
            } else {
                openalert('读取数据时出错，请尝试重新操作或与管理员联系!');
            }
        }
    });
}
//案卷，文件切换
function readFieldAW(tableType) {
    $.ajax({
        async: false,
        url: "getFieldAdvanced.action",
        type: 'post',
        dataType: 'script',
        data: {treeid: orgid, tableType: tableType},
        success: function (data) {
            if (data != "error") {
                field = eval(fields);
                tableFields = field;
                talbeField(field); //字段
                
                searchCommon.templettype = templeType;
            } else {
                openalert('读取数据时出错，请尝试重新操作或与管理员联系!');
            }
        }
    });
}
//表字段
function talbeField(tableField){
	var html = "";
	if (tableField.length > 0) {
        for (var i=0;i<tableField.length;i++) {
        	html += "<option value=\""+tableField[i].englishname+"\">"+tableField[i].chinesename+"</option>";
        }
    }
    $("#selectField").html(html);
}

//添加条件
function addTerm(){
	var fieldNameId = $("#selectField").val();
	var fieldName = $("#selectField option:selected").text();
	if(fieldNameId!='' && fieldName!=''){
		var html = "";
	    html += '<div class="query-item">';
		html += '    <input type="text" readonly="readonly" value="'+fieldName+'" title="字段名" class="span2" />';
		html += '    <select class="input-small operate-type" title="条件">';
		html += '        <option value="1">等于</option>';
		html += '        <option value="2">不等于</option>';
		html += '        <option value="3">大于</option>';
		html += '        <option value="4">大于等于</option>';
		html += '        <option value="5">小于</option>';
		html += '        <option value="6">小于等于</option>';
		html += '        <option value="7">包含</option>';
		html += '    </select>';
		html += '    <input type="text" name="'+fieldNameId+'" class="input-large" id="'+fieldNameId+'" value="" title="值" />';
		html += '    <select class="input-medium value-type" onchange="getDatepicker(\''+fieldNameId+'\',this.value)">';
		html += '        <option value="1">字符串</option>';
		html += '        <option value="2">数字</option>';
		html += '        <option value="3">时间</option>';
		html += '    </select>';
		html += '    <button type="button" style="margin-bottom: 9px;" class="btn btn-mini btn-danger delete-query-item" title="删除条件">';
		html += '        <i class="icon-minus icon-white"></i>';
		html += '    </button>';
		html += '</div>';
		
		$(".query-group").append(html);
	}else{
		openalert("请选择档案节点并添加过滤条件!");
	}
}

/**
 * 时间控件  效果不理想
 */
function getDatepicker(id_v,value){
	if(value=="3"){
		$('input[name="'+id_v+'"]').tab().addClass('datepicker');
		$('.datepicker').datepicker();
	}else{
		$('input[name="'+id_v+'"]').tab().removeClass('datepicker');
		$('.datepicker dropdown-menu').remove();
	}
	
    //$('.modal').css('z-index','9999');
    //$('.datepicker').css('z-index','99999');
}
/**
 * 条件对象
 */
function QueryItem() {
    this.name = '';
    this.operatorType = 0;
    this.value = '';
    this.valueType = 0;
}
/**
 * 获得条件
 */
function GetQueryGroup(group) {
    group = $(group);
    var queryItems = group.children('.query-item');
    var items = [];
    for (var k = 0; k < queryItems.length; k++) {
        var queryItem = new QueryItem();
        queryItem.name = $(queryItems[k]).find('.input-large').attr('id');
        queryItem.operatorType = parseInt($(queryItems[k]).find('.operate-type').val());
        queryItem.value = $(queryItems[k]).find('.input-large').val();
        queryItem.valueType = parseInt($(queryItems[k]).find('.value-type').val());
        items.push(queryItem);
    }
    return items;
}
/**
 * 根据选择的树节点 查找统计
 */
function doSearch(){
	var qg = GetQueryGroup('.query-group');
	var item = JSON.stringify(qg);
	var field = $("#selectField").val();
	var tableType = $("#tableType").val();
	if(field == "" || field == null){
		openalert("请选择档案节点并添加过滤条件!");
		return ;
	}else{
		if(tableType == ""){
			openalert("请选择档案类型!");
			return;
		}
	}
	$.ajax({
	     async: false,
	     url: "searchAdvanced.action",
	     type: 'post',
	     dataType: 'script',
	     data: {groupitem: item ,tableType:tableType,intPage:0},
	     success: function (data) {
	     	list = eval(dynamicList);
	     	if(list.length>0){
	     		var doc = showResultList(list);
	     		$("#countResult").html(doc);
	     		searchCommon.pages = intPageCount; //总页数
	     		searchCommon.currentPage = 1; //初始
	     		if(intPageCount>1){ //显示分页
	     			pageState();
	     		}else{
	     			$('.pagination').css('display','none');
	     		}
	     	}else{
	     		openalert("没有符合该条件的数据，请重新选择条件");
	     	}
	     }
	});
}
//获得类型
function getTableType(value){
	$("#tableType").val(value);
	searchCommon.tabletype = value;
	readFieldAW(value);//读取字段
}
//创建页面显示
function showResultList(list) {
	var doc = "";
	for (var i=0;i<list.length;i++) {
		doc += "<table class=\"table table-bordered table-condensed\" width=\"100%\">";
		doc += "<tr><td width=\"70px\">所属档案库</td><td>"+treeName+"</td>";
		
		if (searchCommon.tabletype == '01') {
			if (searchCommon.templettype == "A") {
				ajhOrWjh = list[i]["ajh"];
				doc += "<td width=\"70px\">类别</td><td width=\"250px\">案卷级</td></tr>";
				doc += "<tr><td>案卷号</td><td>"+list[i].AJH+"</td><td>责任者</td><td>"+list[i].ZRZ+"</td></tr>";
			}else {
				doc += "<td width=\"70px\">类别</td><td width=\"250px\">文件级</td></tr>";
				doc += "<tr><td>文件号</td><td>"+list[i].WJH+"</td><td>责任者</td><td>"+list[i].ZRZ+"</td></tr>";
			}
		}else {
			doc += "<td width=\"70px\">类别</td><td width=\"250px\">文件级</td></tr>";
			doc += "<tr><td>文件号</td><td>"+list[i].WJH+"</td><td>责任者</td><td>"+list[i].ZRZ+"</td></tr>";
		}
		doc += "<tr><td>归档单位</td><td>"+list[i].GDDW+"</td><td>归档日期</td><td>"+list[i].GDRQ+"</td></tr>";
		doc += "<tr><td>题名</td><td colspan=\"3\">"+list[i].TM+"</td></tr>";
		doc += "<tr><td colspan=\"4\"><button  class=\"btn btn-info btn-small\" onClick=\"tabinfo('content','"+list[i].ID+"')\">查看详细</button>  <button class=\"btn btn-info btn-small\" onclick=\"showDoc('"+list[i].ID+"');\">查看全文</button></td></tr>";
		doc += "</table>";
	}
	return doc;
}


//翻页
function selectPage(page) {
	//得到当前点击翻页按钮  的css类
	var cssClass = $("#" + page).attr("class");
	if (cssClass == "disabled") {
		return ;
	}
	if (page == "next") {
		searchCommon.currentPage = searchCommon.currentPage + 1;
	}
	else if (page == "first") {
		searchCommon.currentPage = 1;
	}
	else if (page == "previous") {
		searchCommon.currentPage = searchCommon.currentPage - 1;
	}
	else if (page == "over") {
		searchCommon.currentPage = searchCommon.pages;
	}
	else {
		if (page > searchCommon.pages) {
			searchCommon.currentPage = searchCommon.pages;
		}
		else if (page < 0) {
			searchCommon.currentPage = 1;
		}
		else {
			searchCommon.currentPage = page;
		}
		
	}
	searchByPage();
}
//分页查询
function searchByPage(){
	var qg = GetQueryGroup('.query-group');
	var item = JSON.stringify(qg);
	var tableType = $("#tableType").val();
	$.ajax({
	     async: false,
	     url: "searchAdvanced.action",
	     type: 'post',
	     dataType: 'script',
	     data: {groupitem: item ,tableType:tableType,intPage:searchCommon.currentPage},
	     success: function (data) {
	     	list = eval(dynamicList);
	     	if(list.length>0){
	     		var doc = showResultList(list);
	     		$("#countResult").html(doc);
	     		pageState();
	     	}else{
	     		openalert("没有符合该条件的数据，请重新选择条件");
	     	}
	     }
	});
}
//处理翻页按钮状态
function pageState() {
	$("#currentPage").val(searchCommon.currentPage);
	$("#pageinfo").html("第  "+searchCommon.currentPage +" 页 / 共 "+searchCommon.pages+" 页");
	
	$('.pagination').css('display','block');
	
	$("#first").removeClass("disabled");
	$("#previous").removeClass("disabled");
	$("#next").removeClass("disabled");
	$("#over").removeClass("disabled");
	//如果是第一页
	if (searchCommon.currentPage == 1) {
		$("#first").addClass("disabled");
		$("#previous").addClass("disabled");
	}
	if (searchCommon.currentPage == searchCommon.pages){
		$("#next").addClass("disabled");
		$("#over").addClass("disabled");
	}
	
}

// 打开电子全文
function showDoc(id) {
	var par = "selectRowid="+ id +"&treeid="+orgid;
	var rowList = [];
	//同步读取数据
	$.ajax({
		async : false,
		url : "listLinkDoc.action?"+ par,
		type : 'post',
		dataType : 'script',
		success : function(data) {
			if(isNotAuth==0){
				openalert('对不起，您没有该档案的查看权！');
				return;
			}
			rowList = eval(docList);
			$("#doclist").html("");
			if(rowList.length>0){
				for (var i=0;i<rowList.length;i++) {
					$("#doclist").append(getDoclist(rowList[i]));
				}
				$("#showdoc").modal('show');
			}else{
				openalert('该记录尚未挂接文件！');
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
	str += "<div class=\"thumbnail\"><div class=\"docdiv\"><a href=\"downDoc.action?docId="+ row.docid +"\">";
	var docType = row.docext;
	var typeCss = "";
	if (docType == "DOC" || docType == "XLS" || docType=="PPT") {
		typeCss = "file-icon-office";
	}
	else if (docType == "TXT") {
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
	str += "<div title=\""+row.docoldname+"\"><div class=\"docfilename\"><a href=\"downDoc.action?docId="+ row.docid +"\">" + name + "</a></div></div></div>";
	str += "</li>";
	return str;
}
