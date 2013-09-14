package com.yapu.archive.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yapu.archive.entity.*;
import com.yapu.archive.service.itf.IDynamicService;
import com.yapu.archive.service.itf.ITempletfieldService;
import com.yapu.archive.service.itf.ITreeService;
import com.yapu.system.common.BaseAction;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArchiveGroupAction extends BaseAction {

	private static final long serialVersionUID = 4009249393951511369L;

	private ITreeService treeService;
	private IDynamicService dynamicService;
	private ITempletfieldService templetfieldService;
	private String treeid;
	private String parentid;
	private String tableType;
    private String selectAid;
    private String isAllWj;
    //如果是导入类型，读取字段时，去掉文件级标识
    private String importType; 
    //状态：2为零散文件，1为整编组卷，0为归档后正常文件
    private String status;

	private String par;
	
	private String fieldname; 		//字段
	private String fieldvalue; 	//字段值

	public String showArchive() {
		return SUCCESS;
	}

    public String showWjArchive() {
        return SUCCESS;
    }
	/*
	 * 获得档案动态字段   SlickGrid专用
	 */
	public String getField() throws IOException {

		PrintWriter out = getPrintWriter();

		StringBuilder result = new StringBuilder();
		//如果没有得到树节点id，返回error
		if (null == treeid || "".equals(treeid)) {
			result.append("error");
			out.write(result.toString());
			return null;
		}
		List<SysTempletfield> fieldList = treeService.getTreeOfTempletfield(treeid,tableType);
		result.append("var fields = [");
        //添加【文件级】【是否有全文】2个固定不需要编辑的字段。
        //1。得到treeid对应的templet，templet的type如果是A，则是标准档案，是F则是纯文件级。标准档案才添加文件级按钮
        SysTemplet templet = treeService.getTreeOfTemplet(treeid);
        String tableid = "";
      //增加行号字段
//        result.append("{id:'rownum',name:'行号',field:'rownum',behavior:'select',cssClass:'cell-selection',width:40,cannotTriggerInsert:true,resizable:false,selectable:false,formatter:function(row,column,value) {return row+1;}},");
        result.append("{id:'rownum',name:'行号',field:'rownum',behavior:'select',cssClass:'cell-selection',width:50,cannotTriggerInsert:true,resizable:false,selectable:false,sortable: true},");
        if ("0".equals(importType)) {
	        if ("A".equals(templet.getTemplettype()) && "01".equals(tableType)) {
	            result.append("{id:'files',name:'文件级',field:'files',width:45,cssClass:'cell-center',cannotTriggerInsert:true,resizable:false,selectable:false,");
	            result.append("formatter:function(row,column,value){");
	            result.append("return  '<img src=\"../../images/icons/page.png\" title=\"点击查看文件级\" style=\"cursor:pointer\" onclick=\"showWjTab(\\''+ value +'\\',\\'0\\')\" />'  }},");
	//            result.append("return  '<a href=\"#\" onClick=\"showWjTab(\\''+ value +'\\')\" title=\"点击查看文件级\" class=\"easyui-linkbutton\" plain=\"true\" iconCls=icon-table_add></a>'  }},");
	        }
	        //添加【是否有全文】标识
	        result.append("{id:'isdoc',name:'全文',field:'isdoc',title:'全文',width:40,cssClass:'cell-center',cannotTriggerInsert:true,resizable:false,selectable:false,");
	        result.append("formatter:function(row,column,value,columnDef, dataContext){");
	        //得到表名 ，为了电子全文
	        List<SysTable> tableList = treeService.getTreeOfTable(treeid);
//	        Stringtableid = "";
	        for (SysTable table :tableList) {
	            if (tableType.equals(table.getTabletype())) {
	                tableid = table.getTableid();
	                break;
	            }
	        }
	        result.append("if (value>0) {return  '<img src=\"../../images/icons/flag_blue.png\" style=\"cursor:pointer\" title=\"点击查看电子文件\" onclick=\"showDocwindow(\\''+ dataContext.id + '\\',\\'"+tableid+"\\')\" />'  } else { return \"\"}}},");
        }

//		//返回字段特殊属性，例如默认值，在页面添加时，直接赋值
		StringBuilder fieldsDefaultValue = new StringBuilder();
		fieldsDefaultValue.append("; var fieldsDefaultValue = {");
		for (SysTempletfield field : fieldList) {
			if (field.getSort() >= 0) {
				fieldsDefaultValue.append(field.getEnglishname().toLowerCase());
				fieldsDefaultValue.append(":'").append(field.getDefaultvalue()).append("',");
			}

			if (field.getSort() >= 0 && field.getIsgridshow() == 1) {
				result.append("{id:'");
				result.append(field.getEnglishname().toLowerCase());
				result.append("',name:'");
				result.append(field.getChinesename());
				result.append("',field:'");
				result.append(field.getEnglishname().toLowerCase());
				result.append("',width:");
				result.append(field.getFieldsize() * 1.5);
				result.append(",sortable: true");
				if (field.getFieldtype().contains("INT")) {
					result.append(",cssClass:'cell-center',editor:Slick.Editors.Integer");
				}
				else if (field.getFieldtype().contains("VARCHAR") && field.getIscode() == 0) {
					result.append(",editor:Slick.Editors.Text");
				}

				if (field.getIsrequire() == 1) {
					result.append(",validator:us.requiredFieldValidator");
				}
				String editTxt = "";
				//搞有代码的字段，我靠
				if (field.getIscode() == 1) {
					List<SysCode> codeList = templetfieldService.getTempletfieldOfCode(field.getTempletfieldid());
					if (codeList.size() > 0) {
						editTxt = ",options:'";
						for (SysCode sysCode : codeList) {
							editTxt += sysCode.getColumndata() + ",";
						}
						editTxt = editTxt.substring(0, editTxt.length() -1);
						editTxt += "'";
					}
					result.append(editTxt).append(", editor: SelectCellEditor");
				}
				result.append("},");
			}
		}
		result.deleteCharAt(result.length() - 1).append("]");
		fieldsDefaultValue.deleteCharAt(fieldsDefaultValue.length() - 1).append("}");
		result.append(fieldsDefaultValue.toString());
		//用来返回是否是纯文件级。如果是纯文件级，使档案管理的“全文件”按钮不可用
		if ("F".equals(templet.getTemplettype())){
			result.append(";var templettype = 'F' ");
		}
		else {
			result.append(";var templettype = 'A' ");
		}
		result.append(";var tableid='" + tableid + "'");
		out.write(result.toString());
		return null;
	}

    /**
     * 为多媒体那提供添加新纪录的字段
     * @return
     * @throws IOException
     */
    public String getFieldForMedia() throws IOException {
        PrintWriter out = getPrintWriter();

        String result = "error";
        //如果没有得到树节点id，返回error
        if (null == treeid || "".equals(treeid)) {
            out.write(result);
            return null;
        }
        List<SysTempletfield> fieldList = treeService.getTreeOfTempletfield(treeid,tableType);

        Gson gson = new Gson();
        out.write(gson.toJson(fieldList));
        return null;
    }

	@SuppressWarnings("unchecked")
	public String list() throws IOException {

		PrintWriter out = getPrintWriter();
		StringBuilder result = new StringBuilder();
		//如果没有得到树节点id，返回error
		if (null == treeid || "".equals(treeid)) {
			result.append("error");
			out.write(result.toString());
			return null;
		}

		//得到树节点对应的表集合
		List<SysTable> tableList = treeService.getTreeOfTable(treeid);

		DynamicExample de = new DynamicExample();
        DynamicExample.Criteria criteria = de.createCriteria();
        criteria.andEqualTo("treeid",treeid);
        
        criteria.andEqualTo("status", status);//零散文件
        
        if ("0".equals(isAllWj) && "02".equals(tableType)) {
            criteria.andEqualTo("parentid",selectAid);
        }

		for (int i=0;i<tableList.size();i++) {
			if (tableList.get(i).getTabletype().equals(tableType)) {
				de.setTableName(tableList.get(i).getTablename());
				break;
			}
		}
		List dynamicList = dynamicService.selectByExample(de);

		//得到字段列表
		List<SysTempletfield> templetfieldList = treeService.getTreeOfTempletfield(treeid, tableType);

		StringBuffer sb = new StringBuffer();
		sb.append("var rowList = [");
		SysTemplet templet = treeService.getTreeOfTemplet(treeid);
		if(null!=dynamicList && dynamicList.size()>0){
			for (int i =0; i< dynamicList.size();i++) {
				HashMap tempMap = (HashMap) dynamicList.get(i);
				sb.append("{");
                //添加文件级标识
                if ("A".equals(templet.getTemplettype())) {
                    sb.append("\"files\":\"").append(tempMap.get("ID").toString()).append("\",");
                }
                //添加电子全文标识
                sb.append("\"rownum\":").append(i+1).append(",");
                //grid控件需要小写的id。数据库中存的是大写的id，这里全部采用小写字段名
				for (SysTempletfield sysTempletfield : templetfieldList) {
						if (null == tempMap.get(sysTempletfield.getEnglishname())) {
							sb.append("\""+sysTempletfield.getEnglishname().toLowerCase()+"\":\"\",");
						}
						else {
							sb.append("\""+sysTempletfield.getEnglishname().toLowerCase()+"\":\""+tempMap.get(sysTempletfield.getEnglishname())+"\",");
						}
				}
				sb.deleteCharAt(sb.length() - 1).append("},");
			}
		}else{
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1).append("]");
		out.write(sb.toString());
		return null;
	}

	/**
	 * 预归档
	 * 归档规则
	 * */
	public String getList() throws IOException {

		PrintWriter out = getPrintWriter();
		StringBuilder result = new StringBuilder();
		//如果没有得到树节点id，返回error
		if (null == treeid || "".equals(treeid)) {
			result.append("error");
			out.write(result.toString());
			return null;
		}

		//得到树节点对应的表集合
		List<SysTable> tableList = treeService.getTreeOfTable(treeid);

		DynamicExample de = new DynamicExample();
        DynamicExample.Criteria criteria = de.createCriteria();
        criteria.andEqualTo("treeid",treeid);
        
        criteria.andLike(fieldname, fieldvalue); //查询案卷的条件字段
        
        if ("0".equals(isAllWj) && "02".equals(tableType)) {
            criteria.andEqualTo("parentid",selectAid);
        }

		for (int i=0;i<tableList.size();i++) {
			if (tableList.get(i).getTabletype().equals(tableType)) {
				de.setTableName(tableList.get(i).getTablename());
				break;
			}
		}
		List dynamicList = dynamicService.selectByExample(de);

		//得到字段列表
		List<SysTempletfield> templetfieldList = treeService.getTreeOfTempletfield(treeid, tableType);

		StringBuffer sb = new StringBuffer();
		sb.append("var rowList = [");
		SysTemplet templet = treeService.getTreeOfTemplet(treeid);
		if(null!=dynamicList && dynamicList.size()>0){
			for (int i =0; i< dynamicList.size();i++) {
				HashMap tempMap = (HashMap) dynamicList.get(i);
				sb.append("{");
                //添加文件级标识
                if ("A".equals(templet.getTemplettype())) {
                    sb.append("\"files\":\"").append(tempMap.get("ID").toString()).append("\",");
                }
                //添加电子全文标识
                sb.append("\"rownum\":").append(i+1).append(",");
                //grid控件需要小写的id。数据库中存的是大写的id，这里全部采用小写字段名
				for (SysTempletfield sysTempletfield : templetfieldList) {
						if (null == tempMap.get(sysTempletfield.getEnglishname())) {
							sb.append("\""+sysTempletfield.getEnglishname().toLowerCase()+"\":\"\",");
						}
						else {
							sb.append("\""+sysTempletfield.getEnglishname().toLowerCase()+"\":\""+tempMap.get(sysTempletfield.getEnglishname())+"\",");
						}
				}
				sb.deleteCharAt(sb.length() - 1).append("},");
			}
		}else{
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1).append("]");
		out.write(sb.toString());
		return null;
	}
	
    /**
     * 读取原始的档案表数据。不是为grid
     * @return
     */
    @SuppressWarnings("unchecked")
	public String listForMedia() throws IOException {
        PrintWriter out = this.getPrintWriter();

        //如果没有得到树节点id，返回error
        if (null == treeid || "".equals(treeid)) {
            out.write("error");
            return null;
        }
        //得到树节点对应的表集合
        List<SysTable> tableList = treeService.getTreeOfTable(treeid);

        DynamicExample de = new DynamicExample();
        DynamicExample.Criteria criteria = de.createCriteria();
        criteria.andEqualTo("treeid",treeid);

        for (int i=0;i<tableList.size();i++) {
            if (tableList.get(i).getTabletype().equals(tableType)) {
                de.setTableName(tableList.get(i).getTablename());
                break;
            }
        }
        List dynamicList = dynamicService.selectByExample(de);
        Gson gson = new Gson();
        out.write(gson.toJson(dynamicList));
        return null;
    }
    /**
     * 案卷对应的文件-图片
     * */
    @SuppressWarnings("unchecked")
	public String listForMediaWj() throws IOException {
        PrintWriter out = this.getPrintWriter();

        //如果没有得到树节点id，返回error
        if (null == treeid || "".equals(treeid)) {
            out.write("error");
            return null;
        }
        //得到树节点对应的表集合
        List<SysTable> tableList = treeService.getTreeOfTable(treeid);

        DynamicExample de = new DynamicExample();
        DynamicExample.Criteria criteria = de.createCriteria();
        criteria.andEqualTo("treeid",treeid);
        criteria.andEqualTo("parentid", parentid);
        for (int i=0;i<tableList.size();i++) {
            if (tableList.get(i).getTabletype().equals(tableType)) {
                de.setTableName(tableList.get(i).getTablename());
                break;
            }
        }
        List dynamicList = dynamicService.selectByExample(de);
        Gson gson = new Gson();
        out.write(gson.toJson(dynamicList));
        return null;
    }
    
	@SuppressWarnings("unchecked")
	public String delete() throws IOException {
		String result = "SUCCESS";
		PrintWriter out = getPrintWriter();

		Gson gson = new Gson();
		List<HashMap<String,String>> archiveList = new ArrayList<HashMap<String, String>>();

		try {
			//将传入的json字符串，转换成list
			archiveList = (List)gson.fromJson(par, new TypeToken<List<HashMap<String,String>>>(){}.getType());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			result = "保存失败，请重新尝试，或与管理员联系。";
			out.write(result);
			return null;
		}

		if (archiveList.size() <= 0) {
			result = "没有找到数据，请重新尝试或与管理员联系。";
			out.write(result);
			return null;
		}

		List<SysTable> tableList = treeService.getTreeOfTable(archiveList.get(0).get("treeid").toString());
		SysTemplet templet = treeService.getTreeOfTemplet(archiveList.get(0).get("treeid").toString());
		//sb存储delete语句
		StringBuffer sb = new StringBuffer();
		String tableName = "";
		String tableNameWj = "";
		//得到表名
		for (int i=0;i<tableList.size();i++) {
			if (templet.getTemplettype().equals("F")) {
				if (tableList.get(i).getTabletype().equals(tableType)) {
					tableName = tableList.get(i).getTablename();
					break;
				}
			}
			else if (templet.getTemplettype().equals("A")) {
				if (tableList.get(i).getTabletype().equals(tableType) && tableType.equals("01")) {
					tableName = tableList.get(i).getTablename();
				}
				else if (tableList.get(i).getTabletype().equals(tableType) && tableType.equals("02")){
					tableName = tableList.get(i).getTablename();
					break;
				}
				else {
					tableNameWj = tableList.get(i).getTablename();
				}
			}

		}
		sb.append("delete from ").append(tableName).append(" where id in (");
		StringBuffer sbSql = new StringBuffer();
		for (int z=0;z<archiveList.size();z++) {
			//得到id集合
			HashMap<String,String> row = (HashMap<String,String>) archiveList.get(z);
			sbSql.append("'").append(row.get("id").toString()).append("',");
//			sb.append("'").append(row.get("id").toString()).append("',");
		}
		sb.append(sbSql.deleteCharAt(sbSql.length() - 1)).append(")");
//		sb.deleteCharAt(sbSql.length() - 1).append(")");
		int b = dynamicService.delete((sb.toString()));

		if (b <= 0) {
			result = "删除失败，请重新尝试或与管理员联系。";
		}
		else {
			if (templet.getTemplettype().equals("A") && tableType.equals("01")) {
				sb.setLength(0);
				sb.append("delete from ").append(tableNameWj).append(" where parentid in (").append(sbSql.toString()).append(")");
				dynamicService.delete(sb.toString());
			}

			// 要删除挂接的物理文件
		}

		out.write(result);
		return null;
	}

    /**
     * 获得当前档案模板的类型。可能用于区分普通档案或者是多媒体档案
     * @return
     */
    public String getArchiveType() throws IOException {
        PrintWriter out = this.getPrintWriter();

        if (null == treeid || "".equals(treeid)) {
            return null;
        }
        SysTemplet templet = treeService.getTreeOfTemplet(treeid);
        String a = templet.getTemplettype();
        out.write(a);
        return null;
    }

    /**
     * 查找案卷是否存在
     * @param parentid 案卷ID
     * return true:该案卷存在；false：不存在
     * */
    public boolean isExistArchive(String parentid){
    	//得到树节点对应的表集合
    	List<SysTable> tableList = new ArrayList<SysTable>();
    	DynamicExample de = new DynamicExample();
    	DynamicExample.Criteria criteria = de.createCriteria();
    	criteria.andEqualTo("treeid", treeid);
    	criteria.andEqualTo("id", parentid);// 案卷ID
    	for(int i=0;i<tableList.size();i++){
    		if(tableList.get(i).getTabletype().equals(tableType)){
    			de.setTableName(tableList.get(i).getTablename());
    			break;
    		}
    	}
    	List dynamicList = dynamicService.selectByExample(de);
    	if(dynamicList.size()>0){
    		return true;//存在
    	}else{
    		return false;//不存在
    	}
    }
    /**
     * 查找案卷下是否还存在文件级
     * @param parentid 案卷ID
     * @return true：存在已归档的文件；false：不存在
     * */
    public boolean isExistFile(String parentid){
		//得到树节点对应的表集合
		List<SysTable> tableList = treeService.getTreeOfTable(treeid);

		DynamicExample de = new DynamicExample();
        DynamicExample.Criteria criteria = de.createCriteria();
        criteria.andEqualTo("treeid",treeid);
        criteria.andEqualTo("parentid",parentid);
        
		for (int i=0;i<tableList.size();i++) {
			if (tableList.get(i).getTabletype().equals(tableType)) {
				de.setTableName(tableList.get(i).getTablename());
				break;
			}
		}
		List dynamicList = dynamicService.selectByExample(de);
		if(dynamicList.size()>0){
			return true; //存在
		}else{
			return false;
		}
    }
    /**
     * 拆卷
     * @throws IOException 
     * */
    @SuppressWarnings("unchecked")
	public String hitchBacks() throws IOException{
    	String result = "SUCCESS";
		PrintWriter out = getPrintWriter();
		Gson gson = new Gson();
		List<HashMap<String,String>> archiveList = new ArrayList<HashMap<String, String>>();
		try {
			//将传入的json字符串，转换成list
			archiveList = (List)gson.fromJson(par, new TypeToken<List<HashMap<String,String>>>(){}.getType());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			result = "获取数据失败，请重新尝试，或与管理员联系。";
			out.write(result);
			return null;
		}
		if (archiveList.size() <= 0) {
			result = "没有找到数据，请重新尝试或与管理员联系。";
			out.write(result);
			return null;
		}

		List<SysTable> tableList = treeService.getTreeOfTable(archiveList.get(0).get("treeid").toString());
		SysTemplet templet = treeService.getTreeOfTemplet(archiveList.get(0).get("treeid").toString());
		String tableName = "";
		String tableNameWj = "";
		//得到表名
		for (int i=0;i<tableList.size();i++) {
			if (templet.getTemplettype().equals("F")) {
				if (tableList.get(i).getTabletype().equals(tableType)) {
					tableName = tableList.get(i).getTablename();
					break;
				}
			}else if (templet.getTemplettype().equals("A")) {
				if (tableList.get(i).getTabletype().equals(tableType) && tableType.equals("01")) {
					tableName = tableList.get(i).getTablename();
				}else if (tableList.get(i).getTabletype().equals(tableType) && tableType.equals("02")){
					tableNameWj = tableList.get(i).getTablename();
					break;
				}else {
					tableName = tableList.get(i).getTablename();
				}
			}

		}
		
		if(templet.getTemplettype().equals("F")){
			//纯文件级
			StringBuffer ajSql = new StringBuffer();
			ajSql.append("update ").append(tableName).append(" set status=2 where id in (");
			HashMap<String,String> row = (HashMap<String,String>) archiveList.get(0);
			ajSql.append("'").append(row.get("id").toString()).append("',");
			ajSql.deleteCharAt(ajSql.length() - 1).append(")");
			List<String> sqlList = new ArrayList<String>();
			sqlList.add(ajSql.toString());
			boolean flag = dynamicService.update(sqlList);
			if(!flag){
				result = "拆卷失败！";
			}
		}else if(templet.getTemplettype().equals("A")){
			StringBuffer wjSql = new StringBuffer();
			wjSql.append("update ").append(tableNameWj).append(" set parentid ='',status=2 where parentid in (");
			HashMap<String,String> row = (HashMap<String,String>) archiveList.get(0);
			wjSql.append("'").append(row.get("id").toString()).append("',");
			wjSql.deleteCharAt(wjSql.length() - 1).append(")");
			wjSql.append("and status=1");
			List<String> sqlList = new ArrayList<String>();
			sqlList.add(wjSql.toString());
			
			boolean flag = dynamicService.update(sqlList);
			if(!flag){
				result = "拆卷失败！";
			}else{
				if(isExistFile(row.get("id").toString())){
					//存在 把案卷的状态改回正常的已归档状态
					StringBuffer ajSql = new StringBuffer();
					ajSql.append("update ").append(tableName).append(" set status=0 where id in (");
					ajSql.append("'").append(row.get("id").toString()).append("',");
					ajSql.deleteCharAt(ajSql.length() - 1).append(")");
					List<String> ajSqlList = new ArrayList<String>();
					ajSqlList.add(ajSql.toString());
					
					dynamicService.update(ajSqlList);
				}else{
					//不存在，删除案卷
					StringBuffer ajSql = new StringBuffer();
					ajSql.append("delete from ").append(tableName).append(" where id in (");
					ajSql.append("'").append(row.get("id").toString()).append("',");
					ajSql.deleteCharAt(ajSql.length() - 1).append(")");
					dynamicService.delete((ajSql.toString()));
					
					//案卷的物理文件 删除
				}
			}
		}
		out.write(result);
		return null;
    }
    /**
     * 预组卷-归档组卷 
     * @throws IOException 
     * */
    public String archiveGroup() throws IOException{
    	String result = "SUCCESS";
		PrintWriter out = getPrintWriter();

		Gson gson = new Gson();
		List<HashMap<String,String>> archiveList = new ArrayList<HashMap<String, String>>();

		try {
			//将传入的json字符串，转换成list
			archiveList = (List)gson.fromJson(par, new TypeToken<List<HashMap<String,String>>>(){}.getType());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			result = "获取数据失败，请重新尝试，或与管理员联系。";
			out.write(result);
			return null;
		}

		if (archiveList.size() <= 0) {
			result = "没有找到数据，请重新尝试或与管理员联系。";
			out.write(result);
			return null;
		}

		List<SysTable> tableList = treeService.getTreeOfTable(archiveList.get(0).get("treeid").toString());
		SysTemplet templet = treeService.getTreeOfTemplet(archiveList.get(0).get("treeid").toString());
		HashMap<String,String> row = (HashMap<String,String>) archiveList.get(0);
		
		String tableName = "";
		String tableNameWj = "";
		//得到表名
		for (int i=0;i<tableList.size();i++) {
			if (templet.getTemplettype().equals("F")) {
				if (tableList.get(i).getTabletype().equals(tableType)) {
					tableName = tableList.get(i).getTablename();
					break;
				}
			}else if (templet.getTemplettype().equals("A")) {
				if (tableList.get(i).getTabletype().equals(tableType) && tableType.equals("01")) {
					tableName = tableList.get(i).getTablename();
				}else if (tableList.get(i).getTabletype().equals(tableType) && tableType.equals("02")){
					tableNameWj = tableList.get(i).getTablename();
					break;
				}else {
					tableName = tableList.get(i).getTablename();
				}
			}

		}
	
		//案卷级
		StringBuffer ajSql = new StringBuffer();
		ajSql.append("update ").append(tableName).append(" set status=1 where id in (");
		ajSql.append("'").append(row.get("id").toString()).append("',");
		ajSql.deleteCharAt(ajSql.length() - 1).append(")");
		List<String> ajSqlList = new ArrayList<String>();
		ajSqlList.add(ajSql.toString());
			
		boolean flag = dynamicService.update(ajSqlList);
		if(!flag){
			result = "组卷失败！";
		}
		out.write(result);
		return null;

    }
    
    /**
     * 归档立卷
     * @throws IOException 
     * */
    public String filing() throws IOException{
    	String result = "SUCCESS";
		PrintWriter out = getPrintWriter();

		Gson gson = new Gson();
		List<HashMap<String,String>> archiveList = new ArrayList<HashMap<String, String>>();

		try {
			//将传入的json字符串，转换成list
			archiveList = (List)gson.fromJson(par, new TypeToken<List<HashMap<String,String>>>(){}.getType());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			result = "获取数据失败，请重新尝试，或与管理员联系。";
			out.write(result);
			return null;
		}

		if (archiveList.size() <= 0) {
			result = "没有找到数据，请重新尝试或与管理员联系。";
			out.write(result);
			return null;
		}

		List<SysTable> tableList = treeService.getTreeOfTable(archiveList.get(0).get("treeid").toString());
		SysTemplet templet = treeService.getTreeOfTemplet(archiveList.get(0).get("treeid").toString());
		HashMap<String,String> row = (HashMap<String,String>) archiveList.get(0);
		
		String tableName = "";
		String tableNameWj = "";
		//得到表名
		for (int i=0;i<tableList.size();i++) {
			if (templet.getTemplettype().equals("F")) {
				if (tableList.get(i).getTabletype().equals(tableType)) {
					tableName = tableList.get(i).getTablename();
					break;
				}
			}else if (templet.getTemplettype().equals("A")) {
				if (tableList.get(i).getTabletype().equals(tableType) && tableType.equals("01")) {
					tableName = tableList.get(i).getTablename();
				}else if (tableList.get(i).getTabletype().equals(tableType) && tableType.equals("02")){
					tableNameWj = tableList.get(i).getTablename();
					break;
				}else {
					tableName = tableList.get(i).getTablename();
				}
			}

		}
		//案卷
		StringBuffer ajSql = new StringBuffer();
		ajSql.append("update ").append(tableName).append(" set status=0 where id in (");
		ajSql.append("'").append(row.get("id").toString()).append("',");
		ajSql.deleteCharAt(ajSql.length() - 1).append(")");
		List<String> sqlList = new ArrayList<String>();
		sqlList.add(ajSql.toString());
		boolean flag = dynamicService.update(sqlList);
		if(!flag){
			result = "归档失败！";
		}else{
			//文件
			if(templet.getTemplettype().equals("A")){
				List<String> wJsqlList = new ArrayList<String>();
				StringBuffer wjSql = new StringBuffer();
				wjSql.append("update ").append(tableNameWj).append(" set status=0 where parentid in (");
				wjSql.append("'").append(row.get("id").toString()).append("',");
				wjSql.deleteCharAt(wjSql.length() - 1).append(")");
				wjSql.append("and status=1");
				wJsqlList.add(wjSql.toString());
				dynamicService.update(wJsqlList);
			}
		}

		out.write(result);
		return null;
    }
    
	public String getTreeid() {
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}
	public void setTreeService(ITreeService treeService) {
		this.treeService = treeService;
	}
	public String getTableType() {
		return tableType;
	}
	public void setTableType(String tableType) {
		this.tableType = tableType;
	}
	public void setDynamicService(IDynamicService dynamicService) {
		this.dynamicService = dynamicService;
	}
	public String getPar() {
		return par;
	}
	public void setPar(String par) {
		this.par = par;
	}
	public void setTempletfieldService(ITempletfieldService templetfieldService) {
		this.templetfieldService = templetfieldService;
	}
    public String getSelectAid() {
            return selectAid;
        }
    public void setSelectAid(String selectAid) {
        this.selectAid = selectAid;
    }

	public String getImportType() {
		return importType;
	}

	public void setImportType(String importType) {
		this.importType = importType;
	}

	public String getIsAllWj() {
		return isAllWj;
	}

	public void setIsAllWj(String isAllWj) {
		this.isAllWj = isAllWj;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFieldname() {
		return fieldname;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public String getFieldvalue() {
		return fieldvalue;
	}

	public void setFieldvalue(String fieldvalue) {
		this.fieldvalue = fieldvalue;
	}


}
