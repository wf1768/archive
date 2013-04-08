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
	});
	//动态条件
	$('button.add-query-item').live('click', function () {
	    $(this).parent().parent().append($('.query-item-template>.query-item').clone());
	    
	    return false;
	});
	 
	$('button.delete-query-item').live('click', function () {
	    $(this).parent().remove();
	    return false;
	});

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
		//html += "<select class=\"span3\">";
        for (var i=0;i<tableField.length;i++) {
        	html += "<option value=\""+tableField[i].englishname+"\">"+tableField[i].chinesename+"</option>";
        }
        //html += "</select>";
    }
    $("#selectField").html(html);
}
//选择字段
function selectField(fieldName,fieldNameId){
	$("#fieldName").val(fieldName);
	$("#fieldNameId").val(fieldNameId);
}
//添加条件
function addTerm(){
	var fieldName = $("#fieldName").val();
	var fieldNameId = $("#fieldNameId").val();
	var html = "";
    html += '<div class="query-item">';
	html += '    <input type="text" value="'+fieldName+'" title="字段名" class="span2" />';
	html += '    <select class="input-mini operate-type" title="条件">';
	html += '        <option value="1">==</option>';
	html += '        <option value="2">!=</option>';
	html += '        <option value="3">></option>';
	html += '        <option value="4">>=</option>';
	html += '        <option value="5"><</option>';
	html += '        <option value="6"><=</option>';
	html += '        <option value="7">LK</option>';
	html += '    </select>';
	html += '    <input type="text" class="span3" id="'+fieldNameId+'" value="" title="值" />';
	html += '    <select class="input-medium value-type">';
	html += '        <option value="3">String</option>';
	html += '        <option value="1">Int</option>';
	html += '        <option value="2">Double</option>';
	html += '        <option value="4">DateTime</option>';
	html += '    </select>';
	html += '    <button type="button" style="margin-bottom: 9px;" class="btn btn-mini btn-danger delete-query-item" title="删除条件">';
	html += '        <i class="icon-minus icon-white"></i>';
	html += '    </button>';
	html += '</div>';
	
	$(".query-title").append(html);
	$("#fieldName").val("");
	$("#fieldNameId").val("");
}

function search() {
    var items = [];
    var item = {};
    //item.treeid = archiveCommon.selectTreeid;
    for (var i = 0; i < tableFields.length; i++) {
        var field = tableFields[i];
        if (field.isgridshow == 1) {
            if (null != $('#' + field.englishname).val() || "" != $('#' + field.englishname).val()) {
                item[field.englishname.toLowerCase()] = $('#' + field.englishname).val();
            }
        }
    }
    items.push(item);
    var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();
    $.ajax({
	     async: false,
	     url: "archiveCount.action",
	     type: 'post',
	     dataType: 'text',
	     data: {importData: JSON.stringify(items),treeid:orgid,startTime:startTime,endTime:endTime, tableType: '01'},
	     success: function (data) {
	     	$("#resultCount").html(data);
	     }
	});
}
