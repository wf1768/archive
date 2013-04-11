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

	//时间控件
    var nowTemp = new Date();
    var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);
     
    var checkin = $('#startTime').datepicker({
   		onRender: function(date) {
    		return date.valueOf() < now.valueOf() ? 'disabled' : '';
    	}
    }).on('changeDate', function(ev) {
    	if (ev.date.valueOf() > checkout.date.valueOf()) {
    		var newDate = new Date(ev.date)
    		newDate.setDate(newDate.getDate() + 1);
    		checkout.setValue(newDate);
    	}
    	checkin.hide();
    	$('#endTime')[0].focus();
    }).data('datepicker');
    	var checkout = $('#endTime').datepicker({
    		onRender: function(date) {
    			return date.valueOf() <= checkin.date.valueOf() ? 'disabled' : '';
    		}
    }).on('changeDate', function(ev) {
    	checkout.hide();
    }).data('datepicker');
    
    
});

//同步读取当前节点的字段
function readField(treeid) {
    $.ajax({
        async: false,
        url: "getCountField.action",
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
		html += '    <input type="text" class="input-large" id="'+fieldNameId+'" value="" title="值" />';
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
		$('.input-large').tab().addClass('datepicker');
		$('.datepicker').datepicker();
		
		//$('.input-large').click(function () {
		    //$(this).addClass('datepicker');
		//});
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
	     url: "queryGroup.action",
	     type: 'post',
	     dataType: 'text',
	     data: {groupitem: item},
	     success: function (data) {
	     	openalert("共检索到 "+data+" 条");
	     }
	});
}