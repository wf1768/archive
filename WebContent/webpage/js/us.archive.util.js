if (us == null || us == undefined){
    var us = {};
}


/*===================================
 *               public
 ===================================*/
//TODO 打开、切换tab页签(easyui用，等能去掉时删除)
us.showtab=function(tabObject,url, tabname, icon) {
	if (tabObject.tabs('exists', tabname)) {
		tabObject.tabs('select', tabname);
		var tab = tabObject.tabs('getSelected');
		tabObject.tabs('update', {
			tab : tab,
			options : {
				href : url
			}
		});
		tab.panel('refresh');
	} else {
		tabObject.tabs('add', {
			title : tabname,
			iconCls : icon,
			href : url,
			closable : true
		});
	}
};

//打开、切换tab页签
us.addtab=function(ob,txt, type, link) {
	var aa = $.fn.jerichoTab.addTab({
//		tabFirer: $(this),
		tabFirer: ob,
        title:txt,
        closeable: true,
//        iconImg: ico,
        data: {
            dataType: type,
            dataLink: link
        }
    }).showLoader().loadData();
};


/*==============================================
					  grid
==============================================*/
// grid字段录入时创建字段验证是否为空
us.requiredFieldValidator = function(value) {
	if (value == null || value == undefined || !value.length) {
		return {
			valid : false,
			msg : "请填写内容，不能为空。"
		};
	} else {
		return {
			valid : true,
			msg : null
		};
	}
}

//grid 字段排序
us.comparer = function(a, b) {
  var x = a[archiveCommon.sortcol], y = b[archiveCommon.sortcol];
  return (x == y ? 0 : (x > y ? 1 : -1));
}

//grid 过滤功能
var clName = "ajh";
us.myFilter = function(item, args) {//archiveCommon.clName
	if (args.searchString != "" && item[archiveCommon.clName].indexOf(args.searchString) == -1) {
		return false;
	}
	return true;
}
//grid 过滤功能
us.updateFilter = function(dataView) {
	dataView.setFilterArgs({
		searchString : archiveCommon.searchString
	});
	dataView.refresh();
}

//grid批量修改  
/*
 * grid  		是哪个grid对象
 * dataView  	是哪个view对象
 * isSave		批量修改时，是否保存到数据库。
 */
us.batchUpdate = function(grid,dataView,isSave,tabletype) {
	var selectRows = grid.getSelectedRows();
	selectRows.sort(function compare(a, b) {
		return b - a;
	});
	var selectContent = $("#selectfield");
	var updateContent = $("#updatetxt"); 
	var selectFieldName = selectContent.val(); 
	var updateTxt = updateContent.val();
	
	var batchUpdateItems = [];
	for ( var i = 0; i < selectRows.length; i++) {
		var item = dataView.getItem(selectRows[i]);
        //将更改的内容更新到item
        for(p in item){
            if (p == selectFieldName) {
                item[p] = updateTxt;
                break;
            }
        }
		batchUpdateItems.push(item);
	}
    //如果是保存到数据库
	if (isSave) {
		var par = "importData=" + JSON.stringify(batchUpdateItems) + "&tableType=" + tabletype;
        $.post("updateImportArchive.action",par,function(data){
                if (data != "保存完毕。") {
                	us.openalert(data,'系统提示','alertbody alert_Information');
                }
                else {
                    for ( var i = 0; i < batchUpdateItems.length; i++) {
                        var item = batchUpdateItems[i];
                        dataView.updateItem(item.id,item);
                    }
                    us.openalert('批量更改完成。','系统提示','alertbody alert_Information');
                }
            }
        );
	}
	else {
		for ( var i = 0; i < batchUpdateItems.length; i++) {
			var item = batchUpdateItems[i];
			dataView.updateItem(item.id,item);
		}
	}
}

/**
 *  获得url传递参数
 * @param paras
 * @return {*}
 */
us.request= function(paras)
{
    var url = location.href;
    var paraString = url.substring(url.indexOf("?")+1,url.length).split("&");
    var paraObj = {}
    for (i=0; j=paraString[i]; i++){
        paraObj[j.substring(0,j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=")+1,j.length);
    }
    var returnValue = paraObj[paras.toLowerCase()];
    if(typeof(returnValue)=="undefined"){
        return "";
    }else{
        return returnValue;
    }
}

us.batchAttachment = function() {
	
}
//制作的通用alert
us.openalert = function(text, title,iconcss) {
    var html = $(
        '<div id="dialog-message" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">' +
            '<div class="modal-header">' +
            '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>' +
            '<h3 id="myModalLabel">系统提示</h3>' +
            '</div>' +
            '<div class="modal-body">' +
            '<p id="continfo">' + text + '</p>' +
            '</div>' +
            '<div class="modal-footer">' +
            '<button class="btn" data-dismiss="modal" id="closeidnow" aria-hidden="true">关闭</button>' +
            '</div>' +
            '</div>');
    return html.modal()
}

//jquery ui dialog 制作的通用alert 
//us.openalert = function(text, title,iconcss) {
//	var html = $(
//		    '<div class="dialog" id="dialog-message">' +
//		    '  <div class="'+iconcss+'">' +
//		    '  	<div style="margin-top: 8px;" >' + text + '</div>' +
//		    '</div>');
//	return html.dialog({
//	      //autoOpen: false,
//	      resizable: false,
//	      modal: true,
//	      title: title || "提示信息",
//	      closeOnEscape : true,
//	      show: {
//	          effect: 'fade',
//	          duration: 300
//	      },
//	      buttons: {
//	        "确定": function() {
//	          var dlg = $(this).dialog("close");
//	        }
//	      },
//	      close: function() {
//
//	      }
//	    });
//}
//jquery ui dialog confirm弹出确认提示
us.openconfirm = function(text, title,fn1, fn2) {
	var html = $(
		    '<div class="dialog" id="dialog-confirm">' +
		    '  <div class="alertbody alert_Question">' +
		    '  	<div style="margin-top: 8px;" >' + text + '</div>' + 
		    '</div>');
	    return $(html).dialog({
	      //autoOpen: false,
	      resizable: false,
	      modal: true,
	      show: {
	        effect: 'fade',
	        duration: 300
	      },
	      title: title || "提示信息",
	      buttons: {
	        "确定": function() {
	          var dlg = $(this).dialog("close");
	          fn1 && fn1.call(dlg, true);
	        },
	        "取消": function() {
	          var dlg = $(this).dialog("close");
	          fn2 && fn2(dlg, false);
	        }
	      }
	    });
}

us.openloading = function(text) {
	if ( $("#dialog-loading").length > 0 ) {
		$("#loadingtext").html(text);
		return $("#dialog-loading").modal({
		    backdrop:false,
		    keyboard:false,
		    show:true
		});
	}
	var html = $(
		    '<div class="modal hide fade" id="dialog-loading" style="display: none;width:400px;left:60%;top:60%">' +
		    '  <div class="modal-body">' +
		    '   <img alt="" src="../../images/icons/loading.gif" height="20" width="20" > <span id="loadingtext">' + text + '</span>'+
		    '  </div>' + 
		    '</div>');
	return $(html).modal({
	    backdrop:false,
	    keyboard:false,
	    show:true
	});
}

us.closeloading = function() {
	return $('#dialog-loading').modal('hide');
}

us.upload_multi = function(url,fun) {

    var html =
        '<div id="upload-dialog" class="modal hide fade" aria-hidden="true">' +
            '<div class="modal-header">' +
            '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>' +
            '上传图片' +
            '</div>' +
            '<div class="modal-body">' +
            '<div style="width: 100%;height:90%;">' +
            '<form id="upload-form" action="" method="post">' +
            '<div id="uploader" style="width: 100%;height: 100%">' +
            '<p>您的浏览器未安装 Flash, Silverlight, Gears, BrowserPlus 或者支持 HTML5 .</p>' +
            '</div>' +
            '</form>' +
            '</div>' +
            '</div>' +
            '<div class="modal-footer">' +
            '<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>' +
            '</div>' +
            '</div>';

    $('body').append(html);

    $("#uploader").pluploadQueue({
        runtimes : 'flash,html5,html4',
        max_file_size : '200mb',
        chunk_size: '2mb',
        url : url,
//        unique_names : true,
        flash_swf_url : '../../js/plupload/js/plupload.flash.swf'
//        filters : [
//            {
//                title : "图片文件",
//                extensions : "png,jpg,jpeg,gif"
//            }
//        ]
    });


//    // Client side form validation
//    $('#upload-form').submit(function(e) {
//        var uploader = $('#uploader').pluploadQueue();
//        if (uploader.files.length > 0) {
//            uploader.bind('StateChanged', function() {
//                if (uploader.files.length === (uploader.total.uploaded + uploader.total.failed)) {
//                    $('#upload-form')[0].submit();
//                }
//            });
//            uploader.start();
//        } else {
//            openalert('请先上传文件!');
//        }
//        return false;
//    });
//
    //上传框隐藏后，执行传入的方法名。
    if (fun != "") {
        $('#upload-dialog').on('hidden', function () {
//        if (fun != '') {
            eval(fun + "()");
//        }
        });

    }

    return $('#upload-dialog').modal();
}

