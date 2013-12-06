
var searchCommon = new us.archive.Search();

$(function() {
	
	$('.pagination').css('display','none');
	$(".widget-header").css('display','none');	//结果列表
	$(".widget-content").css('display','none');
	
	//同步取当前session帐户能查询的档案类别
	$.ajax({
		async : false,
		url : "getSearchTree.action",
		type : 'post',
		dataType : 'script',
		success : function(data) {
			if (data != "error") {
				searchCommon.treeList = eval(treList);
			} else {
				us.openalert('<span style="color:red">读取数据时出错.</span></br>请关闭浏览器，重新登录尝试或与管理员联系!',
						'系统提示',
						'alertbody alert_Information'
				);
			}
		}
	});
	//填充搜索档案类别select
	$("#searchDropdownBox").empty();
	if (searchCommon.treeList.length > 0) {
		$("#searchDropdownBox").append("<option selected='selected' value='ALL'>全部分类</option>");
		for (var i=0;i<searchCommon.treeList.length;i++) {
			if (searchCommon.treeList[i].parentid == 0) {
				$("#searchDropdownBox").append("<option value='"+searchCommon.treeList[i].treeid+"'>"+searchCommon.treeList[i].treename+"</option>");
			}
		}
	}
	
});

//检索电子全文
function submitSearch() {
	if ( $("#searchTxt").val() == "") {
		return;
	};
	searchCommon.searchTxt = $("#searchTxt").val();
	searchCommon.treeid = $("#searchDropdownBox").val();
	searchCommon.currentPage = 1;
	var list = getSearchData();
	showSearchInfo(list);
	$('.pagination').css('display','block');
	$(".widget-header").css('display','block');	//结果列表
	$(".widget-content").css('display','block');
}

function getSearchData() {
	var list;
	//得到查询值
	$.ajax({
		async : false,
		url : "searchFile.action",
		type : 'post',
		dataType : 'script',
		data:"keyword=" + searchCommon.searchTxt + "&treeid=" + searchCommon.treeid + "&currentPage=" + searchCommon.currentPage,
		success : function(data) {
			if (data != "error") {
				list = eval(data);
			} else {
				us.openalert('<span style="color:red">读取数据时出错.</span></br>请关闭浏览器，重新登录尝试或与管理员联系!',
						'系统提示',
						'alertbody alert_Information'
				);
			}
		}
	});
	
	return list;
}

function showSearchInfo(list) {
	
	searchCommon.rowCount = list[0].ROWCOUNT;
	searchCommon.pages = list[0].PAGES;
	searchCommon.currentPage = list[0].CURRENTPAGE;
	searchCommon.data = list[0]["DATA"];
	
	var doc = showResultList(list);
	$("#searchList").html("");
	$("#searchList").append(doc);
	
	$("#currentPage").val(searchCommon.currentPage);
	$("#pageinfo").html("第  "+searchCommon.currentPage +" 页 / 共 "+searchCommon.pages+" 页");
	$('.pagination').css('display','block');
	pageState();
}

//创建页面显示
function showResultList(list) {
	var list = list[0]["DATA"];
	var doc = "";
	if(list.length>0){
		for (var i=0;i<list.length;i++) {
			doc += "<table class=\"table table-bordered table-condensed\" width=\"100%\">";
			doc += "<tr><td width=\"10%\">文件名</td><td width=\"45%\">"+list[i].docoldname+"</td><td width=\"15%\">所属档案库</td><td width=\"30%\">"+list[i].treename+"</td></tr>";
			doc += "<tr><td>文件类型</td><td>"+list[i].docext+"</td><td>文件长度</td><td>"+list[i].doclength+"</td></tr>";
			doc += "<tr><td>上传人</td><td>"+list[i].creater+"</td><td>上传日期</td><td>"+list[i].createtime+"</td></tr>";
			doc += "<tr><td>摘要</td><td colspan=\"3\">"+list[i].summary+"</td></tr>";
			doc += "<tr><td colspan=\"4\"><button  class=\"btn btn-info btn-small\" onClick=\"openContentDialog('"+list[i].docid+"','"+list[i].treeid+"')\">查看预览</button><button  class=\"btn btn-info btn-small\" onClick=\"fileDown('"+list[i].docid+"','"+list[i].treeid+"')\">下载全文</button> </td></tr>";
			doc += "</table>";
		}
	}else{
		doc="该关键词没有检索到相应的内容。。。";
	}
	return doc;
	//<a href=\"downDoc.action?docId="+list[i].docid+"&treeid="+list[i].treeid+"\" class=\"btn btn-info btn-small\">下载全文</a>
}

//翻页
function selectPage(page) {
	//把搜索框复位
	$("#searchTxt").val(searchCommon.searchTxt);
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
	
	var list = getSearchData();
	showSearchInfo(list);
	
}
//处理翻页按钮状态
function pageState() {
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
//预览
function openContentDialog(selectid,treeid) {
	$.ajax({
		async : false,
		url : "filePreview.action",
		type : 'post',
		dataType : 'text',
		data:"treeid="+treeid+"&docId="+selectid,
		success : function(data) {
			if (data == "0") {
				openalert("对不起，您没有权限预览此文件！");
			} else {
				var a=document.createElement("a");  
				a.target="_blank"; 
				a.href="../../../readFile.html?selectid="+selectid+"&treeid="+treeid;
				document.body.appendChild(a);  
				a.click()
			}
		}
	});
	
}
//下载
function fileDown(docId,treeid){
	$.ajax({
		async : false,
		url : "isDownDoc.action",
		type : 'post',
		dataType : 'text',
		data:"treeid="+treeid,
		success : function(data) {
			if (data == "1") {
				window.location.href="downDoc.action?docId="+docId+"&treeid="+treeid;
			} else {
				openalert("对不起，您没有权限预览此文件！");
			}
		}
	});
}

//系统提示
function openalert(text) {
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
                '<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>' +
            '</div>' +
        '</div>');
    return html.modal()
}