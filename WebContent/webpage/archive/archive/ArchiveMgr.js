
var archiveCommon = new us.archive.Archive();


$(function() {
	//生成档案tree
	var tree = $("#archivetree").jstree({ 
		//生成的jstree包含哪些插件功能
		//json_data：采用json形式数据
		//crrm：允许增加、改名、移动等操作
		//search：允许检索
		"plugins" : ["themes","json_data","ui","types"],
		//jstree的主题样式，可以更换
		"themes" : {
			"theme" : "default"
		},
		json_data:{
			"ajax":{
	            url:"getTree.action",
	            async:false
	        }
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
		},
		"ui" : {
			// 设置初始化选择id为2的节点
//			"initially_select" : [ "0" ]
		},
		// the core plugin - not many options here
		"core" : { 
			//设置默认打开id为1和4的节点
			"initially_open" : [ "0"] 
		}
	})//单击事件
     .bind('click.jstree', function(event) {               
        var eventNodeName = event.target.nodeName;
        if (eventNodeName == 'INS') {
            return;               
        } else if (eventNodeName == 'A') {
            var $subject = $(event.target).parent();                   
            if ($subject.find('ul').length > 0) {
            } else {
            	var id = $(event.target).parents('li').attr('id');
            	var text = $(event.target).text();
            	showTreeList(tree,id,text);
            }               
        }           
    }).bind("select_node.jstree",function(e,data) { 
	    var node = data.rslt.obj, inst = data.inst;
	   /* orgid = node.attr("id");//选择后给全局orgid赋值
	    var rel = $("#"+node.attr("id")).attr("rel");
	    if(rel=="default"){
	    	alert('is ok!');
	    }*/
	    if (node.hasClass('jstree-closed')) { return inst.open_node(node); }
	    if (node.hasClass('jstree-open')) { return inst.close_node(node); }
	});

});

function showTreeList(ob,id,text) {
    archiveCommon.selectTreeName = text;
    archiveCommon.selectTreeid = id;
    var archiveType = "";
    //同步获得当前所选档案类型。
    var par = "treeid=" + archiveCommon.selectTreeid;
    $.ajax({
        async : false,
        url : "getTempletType.action?" + par,
//        url : "listArchive.action?" + par,
        type : 'post',
        dataType : 'text',  //靠，这里的参数有很多种。如果是script。那么后台传来的string类型就没有反应
        success : function(data) {
            archiveType = data;
            if (data != null) {
                archiveType = data;
            } else {
                us.openalert('<span style="color:red">读取数据时出错.</span></br>请关闭浏览器，重新登录尝试或与管理员联系!',
                    '系统提示',
                    'alertbody alert_Information'
                );
            }
        }
    });
    var url = "";
    if (archiveType == "A" || archiveType == "F") {
        show_aj_archive_list();
//        url  = "showArchiveList.action?treeid=" + id;
//        us.addtab(ob,'档案管理','ajax', url);
    }
    else if (archiveType == "P") {
        url = "dispatch.action?page=/webpage/archive/archive/MediaList.html";
        us.addtab($('#media'),'多媒体管理','ajax', url);
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
    show_wj_archive_list();
//	var url = "showArchiveWjList.action?treeid=" + archiveCommon.selectTreeid;
//	us.addtab($("#wjtab"),'文件管理','ajax', url);
}

// 打开案卷导入tab
function showArchiveImportTab(tableType) {
	archiveCommon.tableType = tableType;
	var url = "dispatch.action?page=/webpage/archive/archive/ArchiveImportList.html";
//	us.showtab($('#tab'),url, '案卷导入', 'icon-page');
	us.addtab($("#importtab"),'案卷导入','ajax', url);
}



