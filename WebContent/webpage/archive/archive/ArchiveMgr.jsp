<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<!-- 系统基本css -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/webpage/css/style.css"/>
<!-- 各种图标 按钮 tab等 1-->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/webpage/images/images.css"/>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/webpage/js/jquery-1.7.1.js"></script>

<!-- archive的通用对象 -->
    <script type="text/javascript" src="../../js/us.archive.js"></script>
    <script type="text/javascript" src="../../js/us.archive.ui.js"></script>
    <script type="text/javascript" src="../../js/us.archive.util.js"></script>
    
    <!-- json2 -->
    <script type="text/javascript" src="../../js/json2.js"></script>


    <!-- 模块专用 -->
    <script type="text/javascript" src="ArchiveMgr.js"></script>

    <script type="text/javascript" src="../../js/uuid.js"></script>
    <script type="text/javascript" src="../../js/map.js"></script>
    
    <!-- Bootstrap -->
<link href="${pageContext.request.contextPath}/webpage/js/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath}/webpage/js/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet" />

<link href="${pageContext.request.contextPath}/webpage/js/bootstrap/css/font-awesome.css" rel="stylesheet" />

<link href="${pageContext.request.contextPath}/webpage/js/bootstrap/css/adminia.css" rel="stylesheet" /> 
<link href="${pageContext.request.contextPath}/webpage/js/bootstrap/css/adminia-responsive.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/webpage/js/bootstrap/css/unicorn.main.css" rel="stylesheet">
    
    <script
	src="${pageContext.request.contextPath}/webpage/js/bootstrap/js/bootstrap.js"></script>
	<script type="text/javascript" src="../../js/bootstrap/js/bootbox.min.js"></script>
    
    <!-- slick grid -->
    <link rel="stylesheet" href="../../js/SlickGrid/slick.grid.css" type="text/css"/>
    <link rel="stylesheet" href="../../js/SlickGrid/myGrid.css" type="text/css"/>
    <!-- slick grid分页插件-->
    <link rel="stylesheet" href="../../js/SlickGrid/controls/slick.pager.css" type="text/css"/>
    <script type="text/javascript" src="../../js/SlickGrid/lib/firebugx.js"></script>


    <script type="text/javascript" src="../../js/SlickGrid/lib/jquery.event.drag-2.2.js"></script>

    <script type="text/javascript" src="../../js/SlickGrid/slick.core.js"></script>
    <script type="text/javascript" src="../../js/SlickGrid/plugins/slick.checkboxselectcolumn.js"></script>
    <script type="text/javascript" src="../../js/SlickGrid/plugins/slick.autotooltips.js"></script>
    <script type="text/javascript" src="../../js/SlickGrid/plugins/slick.rowselectionmodel.js"></script>
    <script type="text/javascript" src="../../js/SlickGrid/slick.dataview.js"></script>
    <script type="text/javascript" src="../../js/SlickGrid/slick.formatters.js"></script>
    <script type="text/javascript" src="slick.remotemodel.js"></script>
    <script type="text/javascript" src="../../js/SlickGrid/slick.editors.js"></script>
    <script type="text/javascript" src="../../js/SlickGrid/SelectCellEditor.js"></script>
    <link rel="stylesheet" href="../../js/SlickGrid/controls/slick.columnpicker.css" type="text/css"/>
    <script type="text/javascript" src="../../js/SlickGrid/controls/slick.pager.js"></script>
    <script type="text/javascript" src="../../js/SlickGrid/controls/slick.columnpicker.js"></script>
    <script type="text/javascript" src="../../js/SlickGrid/slick.grid.js"></script>

    <!-- tree -->
    <link type="text/css" href="../../js/jstree/themes/default/style.css" rel="stylesheet">
    <script type="text/javascript" src="../../js/jstree/jquery.jstree.js"></script>

    <link type="text/css" href="../../js/plupload/js/jquery.plupload.queue/css/jquery.plupload.queue.css"
          rel="stylesheet">
    <script type="text/javascript" src="../../js/plupload/js/plupload.full.js"></script>
    <script type="text/javascript" src="../../js/plupload/js/jquery.plupload.queue/jquery.plupload.queue.js"></script>
    <script type="text/javascript" src="../../js/plupload/js/i18n/cn.js"></script>

    <link rel="stylesheet" type="text/css" href="../../js/jquery-ui/css/custom-theme/jquery-ui-1.8.16.custom.css"/>
    <script type="text/javascript" src="../../js/jquery-ui/jquery-ui-1.8.16.custom.min.js"></script>

    <link href="../../js/bootstrap/js/datepicker/datepicker.css" rel="stylesheet">
    <script type="text/javascript" src="../../js/bootstrap/js/datepicker/bootstrap-datepicker.js"></script>

    <script type="text/javascript" src="../../js/account/account.js"></script>
    <script type="text/javascript" src="ArchiveList.js"></script>
    <script type="text/javascript" src="ArchiveWjList.js"></script>
    <script type="text/javascript" src="ArchiveImportList.js"></script>
    <script type="text/javascript" src="MediaList.js"></script>

    <!-- fancyZoom 提供给客户来选择图片展示的一种效果。-->
    <link rel="stylesheet" type="text/css" href="../../js/FancyZoom/fancyzoom.css" />
    <script src="../../js/FancyZoom/js-global/FancyZoom.js" type="text/javascript"></script>
    <script src="../../js/FancyZoom/js-global/FancyZoomHTML.js" type="text/javascript"></script>

    <script type="text/javascript" src="MediaWjList.js"></script>
    <script type="text/javascript" src="ArchiveBatchAttachment.js"></script>
    
    <script type="text/javascript">
        //客户端实现 复制、粘贴 功能用的map
        var copyMap = new HashMap();
    </script>
    <!--[if IE 7]>
      <link href="${pageContext.request.contextPath}/webpage/js/bootstrap/css/font-awesome-ie7.min.css" rel="stylesheet" type="text/css" />
    <![endif]-->
    
    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <link href="${pageContext.request.contextPath}/webpage/js/bootstrap/css/bootstrap-ie78.css" rel="stylesheet" type="text/css" />
  	  <script src="${pageContext.request.contextPath}/webpage/js/bootstrap/js/respond.src.js" type="text/javascript"></script>	
      <script src="${pageContext.request.contextPath}/webpage/js/bootstrap/js/html5.js" type="text/javascript"></script>
    <![endif]-->
    
    <!-- margin: 0 auto; -->
	
<title>Insert title here</title>
<style type="text/css">
	body {
	    color: #333333;
	    font-family: "微软雅黑","宋体",Arial,sans-serif;
	    /* font-family: "Helvetica Neue",Helvetica,Arial,sans-serif; */
	    font-size: 12px;
	    line-height: 1.42857;
	    background:url(${pageContext.request.contextPath}/images/body-bg.png) repeat;
	}
</style>
</head>
<body>


<div style="padding-top: 8px;">&nbsp;</div>
<div class="container">
    <div class="row">
        <div class="span3">
            <div id="account"></div>
            <hr>
            <div class="widget-header">
                <h3>档案库</h3>
            </div>
            <div class="widget-content">
                <div id="archivetree"></div>
            </div>
            <hr>
        </div>
        <div class="span9">
            <h1 class="page-title">
                <i class="icon-th-large"></i>
                档案管理
            </h1>
        </div>
        <div class="span9">
            <div class="tabbable" id="archive"> <!-- Only required for left/right tabs -->
                <ul class="nav nav-tabs">
                    <li class="active"><a id="ajtab" href="#aj" data-toggle="tab">案卷</a></li>
                    <li><a href="#wj" id="wjtab" data-toggle="tab">文件</a></li>
                    <li><a href="#batchAtt" id="batchAtttab" data-toggle="tab">批量挂接</a></li>
                </ul>
                <div class="tab-content">
                    <div class="tab-pane active" id="aj">
                        <div class="span9" style="margin-left: 0px;">
                            <div class="widget">
                                <div id="grid_header_aj" class="widget-header">
                                    <h3>案卷管理</h3>
                                </div>
                                <div id="ajGrid" style="width: 100%;height: 700px;">
                                    <!--<div id="grid_header_aj" class="grid-header" style="width: 100%;height:20px">案卷</div>-->
                                    <div style="width:100%; height:35px" class="grid-menu">
                                        <ul class="nav nav-pills" style="background-color: #F5F5F5; border: 1px solid #D3D3D3">
                                            <li><a href="javascript:void(0);" onClick="add()">添加</a></li>
                                            <li class="dropdown"><a href="#" data-toggle="dropdown"
                                                                    class="dropdown-toggle">编辑 <b class="caret"></b></a>
                                                <ul class="dropdown-menu">
                                                    <li><a href="javascript:void(0);" onClick="update()">编辑状态</a></li>
                                                    <li><a href="javascript:void(0);" onClick="endupdate()">取消编辑状态</a></li>
                                                    <li class="divider"></li>
                                                    <li><a href="javascript:void(0);" onClick="batchupdate()">批量修改</a></li>
                                                </ul>
                                            </li>
                                            <li><a href="javascript:void(0);" onClick="del()">删除</a></li>
                                            <li class="dropdown"><a href="#" data-toggle="dropdown"
                                                                    class="dropdown-toggle">数据操作 <b class="caret"></b></a>
                                                <ul class="dropdown-menu">
                                                    <li><a href="javascript:void(0);" onClick="importdata()">导入</a></li>
                                                    <li><a href="javascript:void(0);" onClick="">导出</a></li>
                                                    <li class="divider"></li>
                                                    <li><a href="javascript:void(0);" onClick="filter()">过滤</a></li>
                                                </ul>
                                            </li>
                                            <li><a href="javascript:void(0);" id="allwj" onClick="allwj()">全文件</a></li>
                                            <li class="dropdown"><a href="#" data-toggle="dropdown"
                                                                    class="dropdown-toggle">全文挂接 <b class="caret"></b></a>
                                                <ul class="dropdown-menu">
                                                    <li><a href="javascript:void(0);" onClick="linkfile()">挂接</a></li>
                                                    <li><a href="javascript:void(0);" onClick="batchatt()">批量挂接</a></li>
                                                </ul>
                                            </li>
                                            <li><a href="javascript:void(0);" onClick="refresh()">刷新</a></li>
                                        </ul>
                                    </div>
                                    <div id="archivediv" class="grid-div"
                                         style="position: relative; width: 99.8%; height: 390px;"></div>
                                    <div id="pager_aj" class="grid-pager" style="width: 100%; height: 20px;"></div>

                                    <div id="inlineFilterPanel_aj"
                                         style="display: none; background: #dddddd; padding: 1px; color: black; font-size: 9pt">
                                        <form class="form-inline">
                                            请选择过滤字段：<select id="selectfield_aj"></select> 过滤值：<input type="text"
                                                                                                     id="txtSearch_aj">
                                            <a href="javascript:void(0);" class="btn btn-info btn-small" style="color: #FFFFFF"
                                               onClick="filter()">关闭</a>
                                        </form>
                                    </div>

                                </div>
                            </div>
                            <!-- widget end -->
                        </div>
                        <!-- span9 end -->
                    </div>
                    <div class="tab-pane" id="wj">
                        <div class="span9" style="margin-left: 0px;">
                            <div class="widget">
                                <div id="grid_header_wj" class="widget-header">
                                    <h3>文件管理</h3>
                                </div>
                                <div id="wjGrid" style="width: 100%;height: 700px;">
                                    <div style="width:100%;height:35px" class="grid-menu">
                                        <ul class="nav nav-pills" style="background-color: #F5F5F5; border: 1px solid #D3D3D3">
                                            <li><a href="javascript:void(0);" onClick="wjadd()">添加</a></li>
                                            <li class="dropdown"><a href="#" data-toggle="dropdown"
                                                                    class="dropdown-toggle">编辑 <b class="caret"></b></a>
                                                <ul class="dropdown-menu">
                                                    <li><a href="javascript:void(0);" onClick="wjupdate()">编辑状态</a></li>
                                                    <li><a href="javascript:void(0);" onClick="wjendupdate()">取消编辑状态</a></li>
                                                    <li class="divider"></li>
                                                    <li><a href="javascript:void(0);" onClick="wjbatchupdate()">批量修改</a></li>
                                                </ul>
                                            </li>
                                            <li><a href="javascript:void(0);" onClick="wjdelete()">删除</a></li>
                                            <li class="dropdown"><a href="#" data-toggle="dropdown"
                                                                    class="dropdown-toggle">数据操作 <b class="caret"></b></a>
                                                <ul class="dropdown-menu">
                                                    <li><a href="javascript:void(0);" onClick="wjimport()">导入</a></li>
                                                    <li><a href="javascript:void(0);" onClick="">导出</a></li>
                                                    <li class="divider"></li>
                                                    <li><a href="javascript:void(0);" onClick="wjfilter()">过滤</a></li>
                                                </ul>
                                            </li>
                                            <li class="dropdown"><a href="#" data-toggle="dropdown"
                                                                    class="dropdown-toggle">全文挂接 <b class="caret"></b></a>
                                                <ul class="dropdown-menu">
                                                    <li><a href="javascript:void(0);" onClick="wjlinkfile()">挂接</a></li>
                                                    <li><a href="javascript:void(0);" onClick="wjbatchatt()">批量挂接</a></li>
                                                </ul>
                                            </li>
                                            <li><a href="javascript:void(0);" onClick="wjrefresh()">刷新</a></li>
                                        </ul>
                                    </div>
                                    <div id="wjdiv" class="grid-div" style="position: relative; width: 99.8%; height: 390px;"></div>
                                    <div id="pager_wj" class="grid-pager" style="width: 100%; height: 20px;"></div>
                                </div>

                                <div id="inlineFilterPanel_wj"
                                     style="display: none; background: #dddddd; padding: 1px; color: black; font-size: 9pt">
                                    <form class="form-inline">
                                        请选择过滤字段：<select id="selectfield_wj"></select> 过滤值：<input type="text"
                                                                                                 id="txtSearch_wj">
                                        <a href="javascript:void(0);" class="btn btn-info btn-small" style="color: #FFFFFF" onClick="wjfilter()">关闭</a>
                                    </form>
                                </div>
                            </div>
                            <!-- widget end -->
                        </div>
                        <!-- span9 end -->
                    </div>
                    <div class="tab-pane" id="batchAtt">
                        <div class="span9" style="margin-left: 0px;">
                            <div class="widget">
                                当前挂接条件：<span id="gjtj"></span> 更改挂接条件：<select id="fieldselect"></select> <button class="btn btn-small" onclick="setArchiveField()">更改挂接条件</button>
                                <div id="grid_header_batchAtt" class="widget-header">
                                    <h3>挂接档案</h3>
                                </div>
                                <div id="batch_archive" style="width: 100%;height: 200px;">
                                    <div style="width:100%;height:35px" class="grid-menu">
                                        <ul class="nav nav-pills" style="background-color: #F5F5F5; border: 1px solid #D3D3D3">
                                            <li><a href="javascript:void(0);" onClick="setcondition()">设置挂接条件</a></li>
                                            <li><a href="javascript:void(0);" onClick="deletearchive()">移除档案</a></li>
                                            <li><a href="javascript:void(0);" onClick="savearchive()">保存</a></li>
                                            <li><a href="javascript:void(0);" onClick="closs()">关闭批量挂接</a></li>
                                        </ul>
                                    </div>
                                    <div id="archiveAttachmentDiv" class="grid-div" style="position: relative; width: 99.8%; height: 180px;"></div>
                                </div>
                            </div>
                            <!-- widget end -->
                            <div class="widget">
                                <div id="grid_header_yesfile" class="widget-header">
                                    <h3>已挂接文件</h3>
                                </div>
                                <div id="batch_yesfile" style="width: 100%;height: 200px;">
                                    <div style="width:100%;height:35px" class="grid-menu">
                                        <ul class="nav nav-pills" style="background-color: #F5F5F5; border: 1px solid #D3D3D3">
                                            <li><a href="javascript:void(0);" onClick="deleteyesfile()">移除</a></li>
                                        </ul>
                                    </div>
                                    <div id="archiveAttachmentDiv-yes" class="grid-div" style="position: relative; width: 99.8%; height: 180px;"></div>
                                </div>
                            </div>
                            <!-- widget end -->
                            <div class="widget">
                                <div id="grid_header_nofile" class="widget-header">
                                    <h3>未挂接文件</h3>
                                </div>
                                <div id="batch_nofile" style="width: 100%;height: 200px;">
                                    <div style="width:100%;height:35px" class="grid-menu">
                                        <ul class="nav nav-pills" style="background-color: #F5F5F5; border: 1px solid #D3D3D3">
                                            <li><a href="javascript:void(0);" onClick="autofile()">批量挂接</a></li>
                                            <li><a href="javascript:void(0);" onClick="selffile()">手动挂接</a></li>
                                            <li><a href="javascript:void(0);" onClick="uploadfile()">上传</a></li>
                                            <li><a href="javascript:void(0);" onClick="removefile()">移除</a></li>
                                            <li><a href="javascript:void(0);" onClick="deletefile()">删除</a></li>
                                        </ul>
                                    </div>
                                    <div id="archiveAttachmentDiv-no" class="grid-div" style="position: relative; width: 99.8%; height: 180px;"></div>
                                </div>
                            </div>
                            <!-- widget end -->
                        </div>
                        <!-- span9 end -->
                    </div>
                </div>
            </div>
            <style>
                .show-grid [class*="span"] {
                    background-color: #EEEEEE;
                    border-radius: 3px 3px 3px 3px;
                    line-height: 30px;
                    min-height: 30px;
                    text-align: center;
                }
                .pp {
                    height: 40px;
                }
                .titleN{margin: 8px 5px 5px 5px;}
                .imgSize{width: 249px;height: 139px;}

                .thumbnails .actions {
                    -moz-transition: opacity 0.3s ease-in-out 0s;
                    background-color: #FFFFFF;
                    border-radius: 5px 5px 5px 5px;
                    height: 16px;
                    left: 77%;
                    margin-left: -24px;
                    margin-top: -13px;
                    opacity: 0;
                    padding: 5px 8px;
                    position: absolute;
                    top: 70%;
                    width: 62px;
                }
            </style>
            <div class="tabbable" id="media" style="display: none"> <!-- Only required for left/right tabs -->
                <ul class="nav nav-tabs">
                    <li class="active"><a id="media_ajtab" href="#media_aj" data-toggle="tab">案卷</a></li>
                    <li><a href="#media_wj" id="media_wjtab" data-toggle="tab">文件</a></li>
                </ul>
                <div class="tab-content">
                    <div class="tab-pane active" id="media_aj">
                        <div class="span9" style="margin-left: 0px;">
                            <div class="widget">
                                <div id="grid_header_media_aj" class="widget-header">
                                    <h3>多媒体－案卷管理</h3>
                                </div>
                                <div style="width:100%; height:35px" class="grid-menu">
                                    <ul class="nav nav-pills" style="background-color: #F5F5F5; border: 1px solid #D3D3D3">
                                        <li><a href="javascript:void(0);" onClick="addMedia()">添加</a></li>
                                        <li><a href="javascript:void(0);" onClick="refreshMediaAj()">刷新</a></li>
                                    </ul>
                                </div>
                                <ul id="meidadiv" class="thumbnails">
                                    <li class="span3">
                                        <div class="thumbnail">
                                            <a class="thumbnail" href="gallery.html#">
                                                <img class="imgSize" src="../../media/53ab476d-e08a-4c28-b4f6-0694f15f45c1.jpg">
                                            </a>
                                            <div class="actions">
                                                <a href="gallery.html#" title=""><i class="icon-pencil icon-white"></i></a>
                                                <a href="gallery.html#" title=""><i class="icon-remove icon-white"></i></a>
                                            </div>
                                            <div class="titleN">题名：XXXXX</div>
                                        </div>
                                    </li>
                                </ul>
                                <div id="pager_media" class="grid-pager" style="width: 100%; height: 20px;"></div>
                            </div>
                            <!-- widget end -->
                        </div>
                        <!-- span9 end -->
                    </div>
                    <div class="tab-pane" id="media_wj">
                        <div class="span9" style="margin-left: 0px;">
                            <div class="widget">
                                <div id="grid_header_media_wj" class="widget-header">
                                    <h3>文件管理</h3>
                                </div>
                                <div id="mediaWjGrid" class="gridC" style="position: relative; height:100%;overflow: hidden">
                                    <div id="grid_header_mediawj" class="grid-header" style="width: 100%;height:20px">多媒体-文件管理</div>
                                    <div style="width:100%; height:35px" class="grid-menu">
                                        <ul class="nav nav-pills" style="background-color: #F5F5F5; border: 1px solid #D3D3D3">
                                            <li><a href="javascript:void(0);" onClick="openUploadMedia()">添加</a></li>
                                            <li><a href="javascript:void(0);" onClick="openMediaImg()">预览</a></li>
                                            <li><a href="javascript:void(0);" onClick="refreshWj()">刷新</a></li>
                                        </ul>
                                    </div>
                                    <div class="widget-content">
                                        <ul id="meidawjdiv" class="thumbnails">
                                            <li class="span3">
                                                <div class="thumbnail">
                                                    <a class="thumbnail" href="gallery.html#">
                                                        <img class="imgSize" src="../../media/53ab476d-e08a-4c28-b4f6-0694f15f45c1.jpg">
                                                    </a>
                                                    <div class="actions">
                                                        <a href="gallery.html#" title=""><i class="icon-pencil icon-white"></i></a>
                                                        <a href="gallery.html#" title=""><i class="icon-remove icon-white"></i></a>
                                                    </div>
                                                    <div class="titleN">题名：XXXXX</div>
                                                </div>
                                            </li>
                                            <li class="span3">
                                                <div class="thumbnail">
                                                    <a class="thumbnail" href="gallery.html#">
                                                        <img class="imgSize" src="../../media/53ab476d-e08a-4c28-b4f6-0694f15f45c1.jpg">
                                                    </a>
                                                    <div class="actions">
                                                        <a href="gallery.html#" title=""><i class="icon-pencil icon-white"></i></a>
                                                        <a href="gallery.html#" title=""><i class="icon-remove icon-white"></i></a>
                                                    </div>
                                                    <div class="titleN">题名：XXXXX</div>
                                                </div>
                                            </li>
                                        </ul>
                                    </div>

                                    <div id="pager_media_wj" class="grid-pager" style="width: 100%; height: 20px;"></div>
                                </div>

                            </div>
                            <!-- widget end -->
                        </div>
                        <!-- span9 end -->
                    </div>
                </div>
            </div>
        </div>

    </div>
    <!-- row end -->
</div>

<!-- 电子全文列表 -->
<!--<div id="docwindows" title="电子全文列表">-->
    <!--<ul id="doclist"></ul>-->
<!--</div>-->

<!-- Modal -->
<div id="upload-doc-dialog" class="modal hide fade" aria-hidden="true" style="left:45%;width:700px">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        电子全文列表
    </div>
    <div class="modal-body">
        <ul id="doclist"></ul>
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
        <a href="javascript:;" id="uploadfiles" onclick="uploadFile()" class="btn btn-primary">上传</a>
    </div>
</div>

<div id="batchwindows" class="modal hide fade" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        批量修改
    </div>
    <div class="modal-body">
        <form class="form-horizontal" style="margin-top:20px;">
            <fieldset>
                <div class="control-group">
                    <label for="selectfield" class="control-label">更改字段</label>
                    <div class="controls">
                        <select id="selectfield" class="span2"></select>
                    </div>
                </div>
                <div class="control-group" id="updatecontentdiv">
                    <label for="updatetxt" class="control-label">更改内容</label>
                    <div class="controls">
                        <input type="text" id="updatetxt" class="span2">
                        <p class="help-block" id="updatetxterror"></p>
                    </div>
                </div>
            </fieldset>
        </form>
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
        <a href="javascript:;" id="save_batch" onclick="" class="btn btn-primary">提交</a>
    </div>
</div>

<!-- Modal -->
<div id="importwindows" class="modal hide fade" aria-hidden="true" style="left: 40%;width: 800px;margin: -280px 0 0 -280px;">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <div id="importtitle">数据导入</div>
    </div>
    <div class="modal-body" style="height: 400px;">
        <div id="importdiv" class="grid-div"
             style="width: 100%;height: 400px; "></div>
    </div>
    <div class="modal-footer">
        <a href="javascript:;" onclick="selectfile()" class="btn btn-primary">选择文件</a>
        <!--<a href="javascript:;" id="importdelete" onclick="importdelete()" class="btn btn-primary">删除</a>-->
        <!--<a href="javascript:;" id="importbatchupdate" onclick="importsave()" class="btn btn-primary">保存</a>-->
        <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
    </div>
</div>

<!-- Modal -->
<div id="selectfilewindows" class="modal hide fade" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <div>上传导入文件</div>
    </div>
    <div class="modal-body">
        <form id="importArchiveForm" class="form-horizontal" enctype="multipart/form-data"
              action="upload.action" method="post" target="hidden_frame">
            <input type="file" size="40px" class="span2" style="width: 300px" id="selectfile" name="selectfile">
            <iframe name="hidden_frame" id="hidden_frame" style="display: none"></iframe>
        </form>
    </div>
    <div class="modal-footer">
        <a href="javascript:;" onclick="importArchive()" class="btn btn-primary">导入</a>
        <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
    </div>
</div>

<div id="addMedia" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>添加多媒体库</h3>
    </div>
    <div class="modal-body">
        <div id="addMediaDialog"></div>
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal">取 消</button>
        <button class="btn btn-primary" onclick="insert()">保 存</button>
    </div>
</div>
<div id="updMedia" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>修改多媒体库</h3>
    </div>
    <div class="modal-body">
        <input type="hidden" id="ID" >
        <div id="updMediaDialog"></div>
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal">取 消</button>
        <button class="btn btn-primary" onclick="update()">保 存</button>
    </div>
</div>

<div id="updMediaWj" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3 id="myModalLabel">修改多媒体库文件</h3>
    </div>
    <div class="modal-body">
        <input type="hidden" id="ID" >
        <div id="updMediaWjDialog"></div>
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal">取 消</button>
        <button class="btn btn-primary" onclick="updateMediaWj()">保 存</button>
    </div>
</div>

<input type="hidden" id="selectTreeid" value="" />
<input type="hidden" id="selectid" value="" />

<div id="showMediaImg" >
    <iframe id="ifr" width="100%" height="100%"
            name="ifr"  frameborder="no" border="0" marginwidth="0"
            marginheight="0" scrolling="auto" allowtransparency="yes"
            src=""></iframe>
</div>