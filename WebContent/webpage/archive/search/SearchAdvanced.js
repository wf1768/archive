var searchCommon = new us.archive.Search();
var orgid;
var tableFields;
$(function(){
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
	    readField(orgid);
	    $(".query-item").remove(); //清空条件
	});
	
	//删除动态条件
	$('button.delete-query-item').live('click', function () {
	    $(this).parent().remove();
	    return false;
	});

});

//同步读取当前节点的字段
function readField(treeid) {
    $.ajax({
        async: false,
        url: "getFieldAdvanced.action",
        type: 'post',
        dataType: 'json',
        data: {treeid: treeid, tableType: '01'},
        success: function (data) {
            if (data != "error") {
                field = eval(data);
                tableFields = field;
                talbeField(field);
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
var n = 0; 
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
	$.ajax({
	     async: false,
	     url: "searchAdvanced.action",
	     type: 'post',
	     dataType: 'json',
	     data: {groupitem: item},
	     success: function (data) {
	     	list = eval(data);
	     	if(list.length>0){
	     		var doc = showResultList(list);
	     		alert(doc);
	     		$("#countResult").html(doc);
	     	}else{
	     		openalert("没有符合该条件的数据，请重新选择条件");
	     	}
	     }
	});
}

//创建页面显示
function showResultList(list) {
	//var list = list[0]["DATA"];
	var doc = "";
	for (var i=0;i<list.length;i++) {
		doc += "<table class=\"table table-bordered table-condensed\" width=\"100%\">";
		doc += "<tr><td width=\"70px\">所属档案库</td><td>"+$("#" + list[i].treeid + "-name").html()+"</td>";
		
		var ajhOrWjh = "";
		if (searchCommon.tabletype == '01') {
			if (searchCommon.templettype == "A") {
				ajhOrWjh = list[i]["ajh"];
				doc += "<td width=\"70px\">类别</td><td width=\"250px\">案卷级</td></tr>";
				doc += "<tr><td>案卷号</td><td>"+ajhOrWjh+"</td><td>责任者</td><td>"+list[i].zrz+"</td></tr>";
			}
			else {
				ajhOrWjh = list[i].wjh;
				doc += "<td width=\"70px\">类别</td><td width=\"250px\">文件级</td></tr>";
				doc += "<tr><td>文件号</td><td>"+ajhOrWjh+"</td><td>责任者</td><td>"+list[i].zrz+"</td></tr>";
			}
		}
		else {
			ajhOrWjh = list[i].wjh;
			doc += "<td width=\"70px\">类别</td><td width=\"250px\">文件级</td></tr>";
			doc += "<tr><td>文件号</td><td>"+ajhOrWjh+"</td><td>责任者</td><td>"+list[i].zrz+"</td></tr>";
		}
		doc += "<tr><td>归档单位</td><td>"+list[i].gddw+"</td><td>归档日期</td><td>"+list[i].gdrq+"</td></tr>";
		doc += "<tr><td>题名</td><td colspan=\"3\">"+list[i].tm+"</td></tr>";
		doc += "<tr><td colspan=\"4\"><button  class=\"btn btn-info btn-small\" onClick=\"tabinfo('content','"+list[i].id+"')\">查看详细</button>  <button class=\"btn btn-info btn-small\" onclick=\"showDoc('"+list[i].id+"');\">查看全文</button></td></tr>";
		doc += "</table>";
	}
	return doc;
}
