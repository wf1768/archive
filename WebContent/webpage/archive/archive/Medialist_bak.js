var mediaCommon = {
    selectMediaid: '',
    field: {},
    data: {},
    readField: function () {
        //同步读取当前节点的字段
        $.ajax({
            async: false,
            url: "getFieldList.action",
            type: 'post',
            dataType: 'json',
            data: {treeid: archiveCommon.selectTreeid, tableType: '01'},
            success: function (data) {
                if (data != "error") {
                    mediaCommon.field = eval(data);
                } else {
                    us.openalert('读取数据时出错，请尝试重新操作或与管理员联系! ', '系统提示', 'alertbody alert_Information');
                }
            }
        });
    },
    readData: function () {
        //同步读取当前节点的原始数据
        $.ajax({
            async: false,
            url: "listForMediaArchive.action",
            type: 'post',
            dataType: 'json',
            data: {treeid: archiveCommon.selectTreeid, tableType: '01'},
            success: function (data) {
                if (data != "error") {
                    mediaCommon.data = eval(data);
                } else {
                    us.openalert('读取数据时出错，请尝试重新操作或与管理员联系! ', '系统提示', 'alertbody alert_Information');
                }
            }
        });
    },
    showAddDialog: function (values) {
        //处理动态字段，创建dialog里的添加html
        if (mediaCommon.field.length > 0) {
            var html = "<form class=\"form-horizontal\">";
            for (var i = 0; i < mediaCommon.field.length; i++) {
                if (mediaCommon.field[i].isgridshow == 1) {
                    html += "<div class=\"control-group\" id=\"" + mediaCommon.field[i].englishname + "div\">";
                    html += "<label class=\"control-label\" for=\"" + mediaCommon.field[i].englishname + "\">" + mediaCommon.field[i].chinesename + "</label>";
                    html += "<div class=\"controls\">";
                    //弄有代码的
                    if (mediaCommon.field[i].iscode == 1) {
                        var code;
                        $.ajax({
                            async: false,
                            url: "getFieldCode.action",
                            type: 'post',
                            dataType: 'json',
                            data: {templetfieldid: mediaCommon.field[i].templetfieldid},
                            success: function (data) {
                                if (data != "error") {
                                    code = eval(data);
                                } else {
                                }
                            }
                        });
                        html += "<select id='" + mediaCommon.field[i].englishname + "'>";
                        for (var j = 0; j < code.length; j++) {
                            html += "<option value='" + code[j].columnname + "'>" + code[j].columndata + "</option>";
                        }
                        html += "</select>";
                    }
                    else {
                        if (mediaCommon.field[i].fieldtype == "INT") {
                            html += "<input type=\"text\" id=\"" + mediaCommon.field[i].englishname + "\" value=\"0\">";
                        }
                        else {
                            html += "<input type=\"text\" id=\"" + mediaCommon.field[i].englishname + "\" value=\""+values.TM+"\" >";
                        }

                    }

                    html += "</div>";
                    html += "</div>";
                }
            }
			html += "</form>";
            return html;
        }
    },
    picHandle: function () {
        var mw = 300;//设置页面中图片固定宽
        var mh = 200;//设置页面中图片固定高
        var pic = jQuery(".product_image");//获取元素对象
        pic.wrap('<div id="box" class="pp_box" style="cursor:pointer;background-color:#DCDCDC;width:' + mw + 'px;height:' + mh + 'px;"></div>');
        for (var i = 0; i < pic.length; i++) {
            var image = new Image()
            image.src = pic.eq(i).attr("src");
            var pw = image.width;
            var ph = image.height;
            if (pw / ph > mw / mh) {
                pic.eq(i).css("width", mw + "px");
                pic.eq(i).css("height", (mw / pw * ph) + "px");
                pic.eq(i).css("padding-top", parseInt((mh - parseInt(mw / pw * ph)) / 2) + "px")
            } else {
                pic.eq(i).css("height", mh + "px");
                pic.eq(i).css("width", (mh / ph * pw) + "px");
                pic.eq(i).css("padding-left", parseInt((mw - parseInt(mh / ph * pw)) / 2) + "px")
            }
        }
    },
    imageHandle: function (obj,width, height) {
        var img=new Image();
        img.src=obj.src;
        var scale=Math.max(width/img.width, height/img.height);
        var newWidth=img.width*scale;
        var newHeight=img.height*scale;
        var div=obj.parentNode;
        obj.width=newWidth;
        obj.height=newHeight;
        div.style.width=width+"px";
        div.style.height=height+"px";
        div.style.overflow="hidden";
        obj.style.marginLeft=(width-newWidth)/2+"px";
        obj.style.marginTop=(height-newHeight)/2+"px";
        obj.style.maxWidth="300%";

    },
    showData: function () {
        //    显示多媒体数据
        var html = "";
        for (var i = 0; i < mediaCommon.data.length; i++) {
         	var imgsrc = "";
            if (mediaCommon.data[i].SLT == "") {
                imgsrc = "../../media/no_photo_135.png";
            }else {
                imgsrc = "../../media/" + mediaCommon.data[i].SLT;
            }
        	html +='<li class="span3">';
			html +='	<div class="thumbnail">';
			html +='		<a class="thumbnail" href="javascript::" onclick="showMediaWjTab(\''+mediaCommon.data[i].ID+'\')">';
			html +='			<img class="imgSize" src="'+imgsrc+'">';
			html +='		</a>';
			html +='		<div class="actions">';
			html +='			<a href="javascript::" onclick="getMedia(\''+mediaCommon.data[i].ID+'\')" title="修改"><i class="icon-pencil icon-white"></i></a>';
			html +='			<a href="javascript::" onclick="deleteMedia(\''+mediaCommon.data[i].ID+'\')" title="删除"><i class="icon-remove icon-white"></i></a>';
			html +='		</div>';
			html +='		<div class="titleN">题名：'+mediaCommon.data[i].TM+'</div>';
			html +='	</div>';
			html +='</li>';
        }
        $('#meidadiv').html(html);
    },
    initMedia: function () {
        //初始化添加多媒体相册dialog
        $("#addMedia").dialog({
            autoOpen: false,
            height: 470,
            width: 560,
            modal: true,
            buttons: {
                "保存": function () {
                    var items = [];
                    var item = {};
                    item.id = UUID.prototype.createUUID();
                    item.isdoc = 0;
                    item.treeid = archiveCommon.selectTreeid;
                    item.slt = 'no_photo_135.png';
                    for (var i = 0; i < mediaCommon.field.length; i++) {
                        var field = mediaCommon.field[i];
                        if (field.isgridshow == 1) {
                            $('#' + field.englishname + "div").removeClass("error");
                            if (null != $('#' + field.englishname).val() || "" != $('#' + field.englishname).val()) {
                                if (field.fieldtype == "INT") {
                                    //判断值是否正常
                                    var isnum = isNaN($('#' + field.englishname).val());
                                    if (isnum) {
                                        us.openalert('请填写正确的数字类型! ', '系统提示', 'alertbody alert_Information');
                                        $('#' + field.englishname).focus();
                                        $('#' + field.englishname + "div").addClass("error");
                                        return;
                                    }
                                }
                                item[field.englishname.toLowerCase()] = $('#' + field.englishname).val();
                            }
                            else {
                                if (field.fieldtype == "INT") {
                                    item[field.englishname.toLowerCase()] = '0';
                                }
                                else {
                                    item[field.englishname.toLowerCase()] = '';
                                }

                            }
                        }
                    }
                    items.push(item);
                    $.ajax({
                        async: false,
                        url: "saveImportArchive.action",
                        type: 'post',
                        dataType: 'text',
                        data: {importData: JSON.stringify(items), tableType: '01'},
                        success: function (data) {
                            us.openalert(data, '系统提示', 'alertbody alert_Information');
                        }
                    });

                },
                "关闭": function () {
                    $(this).dialog("close");
                }
            },
            close: function () {

            }
        });
        
        //
        $("#updMedia").dialog({
            autoOpen: false,
            height: 470,
            width: 560,
            modal: true,
            buttons: {
                "保存": function () {
                    var items = [];
                    var item = {};
                    item.id = "C5A29EDB26400001DC62BA80B88017E6";
                    item.isdoc = 0;
                    item.treeid = archiveCommon.selectTreeid;
                    item.slt = 'no_photo_135.png';
                    for (var i = 0; i < mediaCommon.field.length; i++) {
                        var field = mediaCommon.field[i];
                        if (field.isgridshow == 1) {
                            $('#' + field.englishname + "div").removeClass("error");
                            if (null != $('#' + field.englishname).val() || "" != $('#' + field.englishname).val()) {
                                if (field.fieldtype == "INT") {
                                    //判断值是否正常
                                    var isnum = isNaN($('#' + field.englishname).val());
                                    if (isnum) {
                                        us.openalert('请填写正确的数字类型! ', '系统提示', 'alertbody alert_Information');
                                        $('#' + field.englishname).focus();
                                        $('#' + field.englishname + "div").addClass("error");
                                        return;
                                    }
                                }
                                item[field.englishname.toLowerCase()] = $('#' + field.englishname).val();
                            }
                            else {
                                if (field.fieldtype == "INT") {
                                    item[field.englishname.toLowerCase()] = '0';
                                }
                                else {
                                    item[field.englishname.toLowerCase()] = '';
                                }

                            }
                        }
                    }
                    items.push(item);
                    $.ajax({
                        async: false,
                        url: "updateImportArchive.action",
                        type: 'post',
                        dataType: 'text',
                        data: {importData: JSON.stringify(items), tableType: '01'},
                        success: function (data) {
                            us.openalert(data, '系统提示', 'alertbody alert_Information');
                        }
                    });

                },
                "关闭": function () {
                    $(this).dialog("close");
                }
            },
            close: function () {

            }
        });
    }
};
/**
 * 新建 相册 (案卷级)
 */
function addMedia() {
   var html = mediaCommon.showAddDialog(Object);
    $('#addMediaDialog').html(html);
    $("#addMedia").dialog("open");
}
/**
 * 修改 相册 (案卷级)
 */
function getMedia(id){
	$.ajax({
	    async: false,
	    url: "getImportArchive.action",
	    type: 'post',
	    dataType: 'json',
	    data: {treeid: archiveCommon.selectTreeid, tableType: '01'},
	    success: function (data) {
	        if (data != "error") {
	            mediaCommon.field = eval(data);
	            //alert(mediaCommon.field[0].TM);
	            var html = mediaCommon.showAddDialog(Object);
	            alert(html);
			    $('#updMediaDialog').html(html);
			    $("#updMedia").dialog("open");
	        } else {
	            us.openalert('读取数据时出错，请尝试重新操作或与管理员联系! ', '系统提示', 'alertbody alert_Information');
	        }
	    }
	});
	
}
function updateMedia(){
	var html = mediaCommon.showAddDialog();
    $('#updMediaDialog').html(html);
    $("#updMedia").dialog("open");
}
/**
 * 修改 相册 (案卷级)
 */
function deleteMedia(id){
	
}

function showMediaWjTab(id) {
    mediaCommon.selectMediaid = id;
    url = "dispatch.action?page=/webpage/archive/archive/MediaWjList.html";
    us.addtab($('#mediawj'), '多媒体--文件管理', 'ajax', url);
}

$(function () {
    setGridResize();
    $('#grid_header_media').html(archiveCommon.selectTreeName + "_多媒体管理");

    mediaCommon.initMedia();
    mediaCommon.readField();
    mediaCommon.readData();
    mediaCommon.showData();

})

