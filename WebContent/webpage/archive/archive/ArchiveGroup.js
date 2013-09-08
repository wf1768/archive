
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
            	//加载数据
            	showTreeList(id,text);
            }               
        }           
    }).bind("select_node.jstree",function(e,data) { 
	    var node = data.rslt.obj, inst = data.inst;
	    if (node.hasClass('jstree-closed')) { return inst.open_node(node); }
	    if (node.hasClass('jstree-open')) { return inst.close_node(node); }
	});

	//加载页签 GDH
	//整理文件
	$.ajax({
	  url: "ArchiveGroupOrganize.html",
	  cache: false,
	  success: function(html){
	    $("#tab1").append(html);
	  }
	});
	//预归档
	$.ajax({
	  url: "ArchiveGroupPre.html",
	  cache: false,
	  success: function(html){
	    $("#tab2").html(html);
	  }
	});
	//归档组卷
	$.ajax({
	  url: "ArchiveGroupVolume.html",
	  cache: false,
	  success: function(html){
	    $("#tab3").html(html);
	  }
	});
	//档案
	$.ajax({
	  url: "ArchiveGroupFiling.html",
	  cache: false,
	  success: function(html){
	    $("#tab4").html(html);
	  }
	});
	
});

/**
 * 读取数据
 * */
function showTreeList(id,text) {
    archiveCommon.selectTreeName = text;
    archiveCommon.selectTreeid = id;
    var archiveType = "";
    //同步获得当前所选档案类型。
    var par = "treeid=" + archiveCommon.selectTreeid;
    $.ajax({
        async : false,
        url : "getTempletType.action?" + par,
        type : 'post',
        dataType : 'text',  //靠，这里的参数有很多种。如果是script。那么后台传来的string类型就没有反应
        success : function(data) {
            if (data != null) {
                archiveType = data;
            } else {
            	openalert("读取数据时出错，请关闭浏览器，重新登录尝试或与管理员联系!！");
            }
        }
    });
    //标准档案A/纯文件级F
    if (archiveType == "F") {
    	archiveCommon.archiveType="F";
    	show_wj_list("01");
    }else if(archiveType == "A"){
    	archiveCommon.archiveType="A";
    	show_wj_list("02");
    }else{
    	openalert("类型错误！");
    }
}

