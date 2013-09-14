/*
 *	命名空间	：us.archive
 *	描述		：档案管理通用js
 *	日期		：2012-07-08
 *			：wangf
*/

//判断us命名空间是否存在
if (us == null || us == undefined){
	var us = {};
}
if (us.archive == null || us.archive == undefined ) {
	us.archive = {};
}
if (us.archive.ui == null || us.archive.ui == undefined ) {
	us.archive.ui = {};
}


//grid
us.archive.ui.Gridconfig = function() {
    // 声明列的数组
    this.columns = [];
    this.columns_fields = [];
    // 声明数据视图
    this.dataView;
    this.rows = [];
    this.grid;
    //是否允许新建行
    this.is_add_new_item = true;
    //是否有分页
    this.is_pager = true;
    //是否列值变动，要提交。档案里列值变动要提交修改。导入时，读取的excel文件列表保存前修改列值就不需要提交。
    this.is_cellchange = true;
    this.fieldsDefaultValue = "";
    //过滤条件
    this.searchString = "";
    // 创建grid配置对象
    this.options = {
        editable : true,
        enableAddRow : false,
        enableCellNavigation : true,
        autoEdit : false,
        enableColumnReorder : true,
        topPanelHeight : 32,
        autoHeight: false
    };
    //声明新建行的系统默认值
    this.newItemTemplate = {
        isdoc	: "0",
        status	: "0"
    }
    this.tabletype = '';

    this.init_grid = function(gridObject,grid_div,pager,filterPanel,idfield) {

        if (idfield) {
            // 创建dataview
            this.dataView = new Slick.Data.DataView({
                inlineFilters : false,
                idField:idfield
            });
        }
        else {
            // 创建dataview
            this.dataView = new Slick.Data.DataView({
                inlineFilters : false
            });
        }

        if (this.is_add_new_item) {
            this.options.enableAddRow = true;
        }
        // 创建grid
        this.grid = new Slick.Grid(grid_div, this.dataView, this.columns, this.options);
        var columnpicker = new Slick.Controls.ColumnPicker(this.columns, this.grid, this.options);
        //设置录入错误时提示。例如不能为空的字段
        this.grid.onValidationError.subscribe(function(e, args) {
            us.openalert(args.validationResults.msg,
                '系统提示',
                'alertbody alert_Information'
            );
        });
        // 设置grid的选择模式。行选择
        // grid.setSelectionModel(new Slick.RowSelectionModel());
        this.grid.setSelectionModel(new Slick.RowSelectionModel({
            selectActiveRow : false
        }));
//        this.grid.setSelectionModel(new Slick.CellSelectionModel());
        gridObject.grid.onKeyDown.subscribe(function(e) {
            // select all rows on ctrl-a
            if (e.which != 65 || !e.ctrlKey) {
                return false;
            }
            var rows = [];
            for ( var i = 0; i < gridObject.dataView.getLength(); i++) {
                rows.push(i);
            }
            gridObject.grid.setSelectedRows(rows);
            e.preventDefault();
        });
        // 设置分页控件
        if (this.is_pager) {
            var pager_aj = new Slick.Controls.Pager(this.dataView, this.grid, $("#"+pager));
        }
        // 注册grid的自动提示插件。只在字段内容过长时出现省略号时提示
        this.grid.registerPlugin(new Slick.AutoTooltips());
        if (filterPanel) {
            $("#"+filterPanel).appendTo(gridObject.grid.getTopPanel()).show();
        }
        if (this.is_add_new_item) {
            //grid的添加新行事件
            gridObject.grid.onAddNewRow.subscribe(function(e, args) {
                var item = $.extend({}, gridObject.newItemTemplate, args.item);
                item.id = UUID.prototype.createUUID ();
                item.treeid = archiveCommon.selectTreeid;
                item.files = item.id;
                item.rownum = (gridObject.dataView.getLength() + 1).toString();
                var par = "importData=[" + JSON.stringify(item) + "]&tableType=" + gridObject.tabletype;
                $.post("saveImportArchive.action",par,function(data){
                        if (data != "保存完毕。") {
                            us.openalert(data,
                                '系统提示',
                                'alertbody alert_Information'
                            );
                        }
                    }
                );
                gridObject.dataView.addItem(item);
            });
        }

        //grid的列值变动事件
        if (this.is_cellchange) {
            this.grid.onCellChange.subscribe(function(e, args) {
                var item = args.item;
                var par = "importData=[" + JSON.stringify(item) + "]&tableType=" + gridObject.tabletype;
                $.post("updateImportArchive.action",par,function(data){
                        if (data != "保存完毕。") {
                            us.openalert(data,
                                '系统提示',
                                'alertbody alert_Information'
                            );
                        }
                    }
                );
            });
        }

        gridObject.grid.onSort.subscribe(function(e, args) {
            archiveCommon.sortdir = args.sortAsc ? 1 : -1;
            archiveCommon.sortcol = args.sortCol.field;
            gridObject.dataView.sort(us.comparer, args.sortAsc);

        });

        gridObject.dataView.onRowCountChanged.subscribe(function(e, args) {
            gridObject.grid.updateRowCount();
            gridObject.grid.render();
        });

        gridObject.dataView.onRowsChanged.subscribe(function(e, args) {
            gridObject.grid.invalidateRows(args.rows);
            gridObject.grid.render();
        });
        gridObject.dataView.syncGridSelection(gridObject.grid, true);

    }
}

//public function
us.archive.ui.Gridconfig.prototype.comparer = function(a, b) {
    var x = a[this.sortcol], y = b[this.sortcol];
    return (x == y ? 0 : (x > y ? 1 : -1));
}




