
var importGridConfig = new us.archive.ui.Gridconfig();
function openImportWindows(tabletype) {
    archiveCommon.tableType = tabletype;


    var par = "treeid=" + archiveCommon.selectTreeid + "&tableType=" + tabletype + "&importType=1";
    $.ajax({
        async : false,
        url : "getField.action?" + par,
        type : 'post',
        dataType : 'script',
        success : function(data) {
            if (data != "error") {
                importGridConfig.columns_fields = fields;
                importGridConfig.fieldsDefaultValue = fieldsDefaultValue;
            } else {
                us.openalert('读取字段信息时出错，请关闭浏览器，重新登录尝试或与管理员联系! ','系统提示','alertbody alert_Information');
            }
        }
    });

    // 创建checkbox列
    var checkboxSelector_import = new Slick.CheckboxSelectColumn({
        cssClass : "slick-cell-checkboxsel"
    });
    // 加入checkbox列
    importGridConfig.columns.push(checkboxSelector_import.getColumnDefinition());
    importGridConfig.columns = [];
    // 加入其他列
    for ( var i = 0; i < importGridConfig.columns_fields.length; i++) {
        importGridConfig.columns.push(importGridConfig.columns_fields[i]);
    }

//    importGridConfig.dataView = [{}];

    importGridConfig.is_pager = false;
    importGridConfig.tabletype = tabletype;
    importGridConfig.is_add_new_item = false;
    importGridConfig.is_cellchange = false;
    importGridConfig.init_grid(importGridConfig,"#importdiv","","");
    importGridConfig.grid.resizeCanvas();

    // 注册grid的checkbox功能插件
    importGridConfig.grid.registerPlugin(checkboxSelector_import);

    var divHeight  = $('#importdiv').height();
    $('#importdiv').find(".slick-viewport").height(divHeight-27);

    var importTitle = "";
    if (tabletype == '01') {
        importTitle = archiveCommon.selectTreeName + '_案卷导入';
    }
    else {
        importTitle = archiveCommon.selectTreeName + '_文件导入';
    }
    $("#importtitle").html(importTitle);

    $('#importwindows').modal('show');

}

function selectfile() {
    $('#selectfilewindows').modal('show');
}

function importArchive() {
    var importArchiveForm = $('#importArchiveForm');

    var filePath = $('#selectfile').val();
    if (!filePath) {
        us.openalert('请选择导入到文件。','系统提示','alertbody alert_Information');
        return;
    }
    // 判断上传文件类型
    var a = selectfile_Validator(filePath);
    if (a) {
        var url = 'upload.action?treeid=' + archiveCommon.selectTreeid;
        if (archiveCommon.tableType == "01") {
            url += '&tableType=01';
        }
        else {
            url += '&tableType=02&selectAid=' + archiveCommon.selectAid;
        }
        importArchiveForm.attr('action',url);
        importArchiveForm.submit();
    }
}

function showCallback(backType, jsonStr) {

    if (backType == "failure") {
        us.openalert(jsonStr,'系统提示','alertbody alert_Information');
    } else {
//        jsonStr = jsonStr.replace(new RegExp("\\\\","g"),"\\\\\\\\");
//        jsonStr = jsonStr.replace(new RegExp("'","g"),"\\'");

//        alert(jsonStr);
//        var json = JSON.parse(a);
        importGridConfig.dataView.setItems(jsonStr);
        importGridConfig.grid.setColumns(importGridConfig.columns);
    }
}

function selectfile_Validator(selectfile) {
    if (selectfile == " ") {
        us.openalert('请选择Excel类型的导入文件！','系统提示','alertbody alert_Information');
        return false;
    }
    var last = selectfile.match(/^(.*)(\.)(.{1,8})$/)[3]; // 检查上传文件格式
    last = last.toUpperCase();
    if (last == "XLS") {
    } else {
        us.openalert('只能上传Excel文件,请重新选择！ ','系统提示','alertbody alert_Information');
        return false;
    }
    return true;
}

//import data delete
function importdelete() {
    var selectRows = importGridConfig.grid.getSelectedRows();
    selectRows.sort(function compare(a, b) {
        return b - a;
    });
    if (selectRows.length > 0) {
        bootbox.confirm("确定要删除选中的 <span style='color:red'>"+selectRows.length+"</span> 条导入数据吗?<br> <font color='red'>" +
            "注意：删除案卷记录，将同时删除案卷及案卷下所有文件数据、电子全文，请谨慎操作！</font> ", function(result) {
            if(result){
                for ( var i = 0; i < selectRows.length; i++) {
                    var item = importGridConfig.dataView.getItem(selectRows[i]);
                    importGridConfig.dataView.deleteItem(item.id);
                }
            }
        })
    }
    else {
        us.openalert('请选择要删除的数据。 ','系统提示','alertbody alert_Information');
    }
}

// import data save
function importsave() {
//    importGridConfig.grid.getEditorLock().commitCurrentEdit();
//    var a = importGridConfig.dataView.getItems();
//
//    for (var i=0;i< 1;i++) {
//        var item = a[i];
//
//        //同步读取数据
//        var par = "importData=[" + JSON.stringify(item) + "]&tableType=" + archiveCommon.tableType;
//        $.ajax({
//            async : false,
//            url : "saveImportArchive.action",
//            type : 'post',
//            dataType : 'script',
//            data    : par,
//            success : function(data) {
//                if (data != "保存完毕。") {
//                    alert('dd');
//                }
//                else {
//                    alert(i);
//                }
//            }
//        });
//    }

    importGridConfig.grid.getEditorLock().commitCurrentEdit();
    var a = importGridConfig.dataView.getItems();
    if (a.length > 0) {
        var par = "importData=" + JSON.stringify(a) + "&tableType=" + archiveCommon.tableType;

        $.post("saveImportArchive.action",par,function(data){
                us.openalert(data,'系统提示','alertbody alert_Information');
            }
        );
    }
    else {
        us.openalert('没有找到导入数据.<br>请重新读取Excel导入文件或与管理员联系。 ','系统提示','alertbody alert_Information');
    }
}