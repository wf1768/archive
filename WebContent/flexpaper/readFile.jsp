<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
 <head>
    
    <title>在线阅读</title>
        <style type="text/css" media="screen"> 
			html, body	{ height:100%; }
			body { margin:0; padding:0; overflow:auto; }   
			#flashContent { display:none; }
        </style> 
		
		<script type="text/javascript" src="js/flexpaper_flash.js"></script>
  </head>
  
  <body>
    <div style="position:absolute;left:200px;top:10px;">
	        <a id="viewerPlaceHolder" style="width:1000px;height:800px;display:block"></a>
	        <input type="hidden" id="swfFile" value="<%request.getAttribute("swfFile"); %>" />
	        <script type="text/javascript">
	        	var swfName = document.getElementById("swfFile").value;
	        	var url = 'http://localhost:8088/archive3/dfa7be61-cefd-4b2f-9562-8e9c4a9c0a3b.swf';
	        	//http://localhost:8088/fileOnlineDemo/file/FileDoc.swf
	        	alert(swfName);
	     		var fp = new FlexPaperViewer(	
						 'FlexPaperViewer',
						 'viewerPlaceHolder', { config : {
						 SwfFile : url,
						 Scale : 0.6, 
						 ZoomTransition : 'easeOut',
						 ZoomTime : 0.5,
						 ZoomInterval : 0.2,
						 FitPageOnLoad : true,
						 FitWidthOnLoad : false,
						 PrintEnabled : false,
						 FullScreenAsMaxWindow : false,
						 ProgressiveLoading : true,
						 MinZoomSize : 0.2,
						 MaxZoomSize : 5,
						 SearchMatchAll : false,
						 InitViewMode : 'Portrait',
						 
						 ViewModeToolsVisible : true,
						 ZoomToolsVisible : true,
						 NavToolsVisible : true,
						 CursorToolsVisible : true,
						 SearchToolsVisible : true,
  						
  						 localeChain: 'zh_CN'
						 }});
	        </script>
        </div>
   </body>
</html>
