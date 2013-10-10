(function ($) {
  /***
   * A sample AJAX data store implementation.
   * Right now, it's hooked up to load Hackernews stories, but can
   * easily be extended to support any JSONP-compatible backend that accepts paging parameters.
   */
  function RemoteModel() {
    // private
    var PAGESIZE = 50;
    var data = {length: 0};
    var searchstr = "";
    var fieldname = "";
    var fieldvalue = "";
    var sortcol = null;
    var sortdir = 1;
    var h_request = null;
    var req = null; // ajax request
    var page = 0;
    // events
    var onDataLoading = new Slick.Event();
    var onDataLoaded = new Slick.Event();


    function init() {
    }
    
    function setPage(v_page){
    	page = v_page;
    }

    function isDataLoaded(from, to) {
      for (var i = from; i <= to; i++) {
        if (data[i] == undefined || data[i] == null) {
          return false;
        }
      }

      return true;
    }


    function clear() {
      for (var key in data) {
        delete data[key];
      }
      data.length = 0;
    }


    function ensureData(from, to,url) {
      if (req) {
        req.abort();
        for (var i = req.fromPage; i <= req.toPage; i++)
          data[i * PAGESIZE] = undefined;
      }

      if (from < 0) {
        from = 0;
      }

      if (data.length > 0) {
        to = Math.min(to, data.length - 1);
      }

      var fromPage = Math.floor(from / PAGESIZE);
      var toPage = Math.floor(to / PAGESIZE);

      while (data[fromPage * PAGESIZE] !== undefined && fromPage < toPage)
        fromPage++;

      while (data[toPage * PAGESIZE] !== undefined && fromPage < toPage)
        toPage--;

      if (fromPage > toPage || ((fromPage == toPage) && data[fromPage * PAGESIZE] !== undefined)) {
        // TODO:  look-ahead
        onDataLoaded.notify({from: from, to: to});
        return;
      }
      //var url = "http://api.thriftdb.com/api.hnsearch.com/items/_search?filter[fields][type][]=submission&q=" + searchstr + "&start=" + (fromPage * PAGESIZE) + "&limit=" + (((toPage - fromPage) * PAGESIZE) + PAGESIZE);
      /*
      if (sortcol != null) {
          url += ("&sortby=" + sortcol + ((sortdir > 0) ? "+asc" : "+desc"));
      }*/

      if (h_request != null) {
        clearTimeout(h_request);
      }
      h_request = setTimeout(function () {
        for (var i = fromPage; i <= toPage; i++)
          data[i * PAGESIZE] = null; // null indicates a 'requested but not available yet'

        onDataLoading.notify({from: from, to: to});
        
        //var par = "treeid=C51C96B7E9700001C87C96B019301F54&tableType=02&isAllWj=false"+"&status=2"+"&index="+fromPage*PAGESIZE+"&size="+PAGESIZE;// + archiveCommon.isAllWj ;//+"&selectAid=" + archiveCommon.selectAid;
        $.ajax({
            async : false,
            //url : "listArchiveP.action?" + par,
            url : url+"&fieldname="+fieldname+"&fieldvalue="+fieldvalue+"&index="+fromPage*PAGESIZE+"&size="+PAGESIZE,
            type : 'post',
            dataType : 'script',
            success : function(data) {
                if (data != "error") {
                	onSuccess(rowList);
                }
        	}
        });
        
//        req = $.jsonp({
//          url: url,
//          callbackParameter: "callback",
//          cache: true,
//          success: onSuccess,
//          error: function () {
//            onError(fromPage, toPage)
//          }
//        });
//        req.fromPage = fromPage;
//        req.toPage = toPage;
        
      }, 50);
    }


    function onError(fromPage, toPage) {
      alert("error loading pages " + fromPage + " to " + toPage);
    }

    function onSuccess(resp) {
//    	alert(resp.length);
//    	alert(JSON.stringify(resp));
		
//      var from = resp.request.start, to = from + resp.results.length;
      var from = page*PAGESIZE, to = resp.length;
//      data.length = Math.min(parseInt(resp.hits),1000); // limitation of the API

      for (var i = 0; i < resp.length; i++) {
//        var item = resp.results[i].item; //一行json
        var item = resp[i];
//        alert(JSON.stringify(item));
        // Old IE versions can't parse ISO dates, so change to universally-supported format.
//        item.create_ts = item.create_ts.replace(/^(\d+)-(\d+)-(\d+)T(\d+:\d+:\d+)Z$/, "$2/$3/$1 $4 UTC"); 
//        item.create_ts = new Date(item.create_ts);

        data[from + i] = item;
        data[from + i].index = from + i;
      }

      req = null;
      var flag = true;
      onDataLoaded.notify({from: from, to: to,flag:flag});
      page = page+1;
    }


    function reloadData(from, to) {
      for (var i = from; i <= to; i++)
        delete data[i];

      ensureData(from, to);
    }


    function setSort(column, dir) {
      sortcol = column;
      sortdir = dir;
      clear();
    }

    function setSearch(field_name,field_value) {
      fieldname = field_name;
      fieldvalue = field_value;
      clear();
    }


    init();

    return {
      // properties
      "data": data,
      "page": page,
      // methods
      "clear": clear,
      "isDataLoaded": isDataLoaded,
      "ensureData": ensureData,
      "reloadData": reloadData,
      "setSort": setSort,
      "setSearch": setSearch,
      "setPage":setPage,

      // events
      "onDataLoading": onDataLoading,
      "onDataLoaded": onDataLoaded
    };
  }

  // Slick.Data.RemoteModel
  $.extend(true, window, { Slick: { Data: { RemoteModel: RemoteModel }}});
})(jQuery);
