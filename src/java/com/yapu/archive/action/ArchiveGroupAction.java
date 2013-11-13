package com.yapu.archive.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yapu.archive.entity.*;
import com.yapu.archive.service.itf.IDocService;
import com.yapu.archive.service.itf.IDocserverService;
import com.yapu.archive.service.itf.IDynamicService;
import com.yapu.archive.service.itf.ITempletfieldService;
import com.yapu.archive.service.itf.ITreeService;
import com.yapu.system.common.BaseAction;
import com.yapu.system.util.CommonUtils;
import com.yapu.system.util.DBUtil;
import com.yapu.system.util.DatabaseManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ArchiveGroupAction extends BaseAction {

	private static final long serialVersionUID = 4009249393951511369L;

	private ITreeService treeService;
	private IDynamicService dynamicService;
	private ITempletfieldService templetfieldService;
	private IDocserverService docserverService;
	private IDocService docService;
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
	//分页
	private int index;
	private int size;
	//文档类型，1.发文，2.收文，3.内请
	private String edoc_property;

	private DatabaseManager manager = new DatabaseManager();
//	private DatabaseManager manager = null;
	
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
//		List dynamicList = dynamicService.selectBySql(de, index, size);
		

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
	 * 分页查询
	 * */
	@SuppressWarnings("unchecked")
	public String pageList() throws IOException {

		PrintWriter out = getPrintWriter();
		StringBuilder result = new StringBuilder();
		//如果没有得到树节点id，返回error
		if (null == treeid || "".equals(treeid)) {
			result.append("error");
			out.write(result.toString());
			return null;
		}
		//计算总行数,为分页
		StringBuffer rowCount = new StringBuffer();

		//得到树节点对应的表集合
		List<SysTable> tableList = treeService.getTreeOfTable(treeid);
		
		DynamicExample de = new DynamicExample();
        DynamicExample.Criteria criteria = de.createCriteria();
		
		for (int i=0;i<tableList.size();i++) {
			if (tableList.get(i).getTabletype().equals(tableType)) {
				de.setTableName(tableList.get(i).getTablename());
				rowCount.append("select count(1) from ").append(tableList.get(i).getTablename());
				break;
			}
		}
		
        criteria.andEqualTo("treeid",treeid);
        rowCount.append(" where treeid = '").append(treeid).append("'");
        criteria.andEqualTo("status", status);//零散文件
        rowCount.append(" and status = ").append(status);
        if(fieldvalue!=null && !fieldvalue.equals("")){
        	criteria.andLike(fieldname, fieldvalue); //字段查询
        	rowCount.append(" and ").append(fieldname).append(" like '%").append(fieldvalue).append("%'");
        	
        }
        if ("0".equals(isAllWj) && "02".equals(tableType)) {
            criteria.andEqualTo("parentid",selectAid);
            rowCount.append(" and parentid='").append(selectAid).append("'");
        }
        
        de.setOrderByClause("order by createtime desc");
		
//		List dynamicList = dynamicService.selectByExample(de);
		int num = dynamicService.rowCount(rowCount.toString());
		
		List dynamicList = dynamicService.selectListPageByExample(de, index, size);
		System.out.println(num + "  ++++++++++++");
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
							String value = tempMap.get(sysTempletfield.getEnglishname()).toString().replaceAll("\\\\","\\\\\\\\");
                            value = value.replace("'","\\\'");
                            value = value.replace("\"","\\\"");
                            value = value.replaceAll("[\\t\\n\\r]", "");
                            sb.append("\""+sysTempletfield.getEnglishname().toLowerCase()+"\":\""+value+"\",");
//							sb.append("\""+sysTempletfield.getEnglishname().toLowerCase()+"\":\""+tempMap.get(sysTempletfield.getEnglishname())+"\",");
						}
				}
				sb.deleteCharAt(sb.length() - 1).append("},");
			}
		}else{
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1).append("]");
		
		sb.append(";var rowCount=").append(num);
		
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
    
    /**
     * 读取OA数据
     * */
    public String readOaList(){
    	//文档类型EDOC_Property，1.发文，2.收文，3.内请
    	String sql = "SELECT Archive_ID AS id,DocNO AS wjh,Title AS tm,SubmitUnit AS cbdw,SubmitUser AS jbr,SubmitDate AS wjrq,CenterName AS zxmc,signuser AS cbswqbr,signcomment AS cbswqbyj,checkuser AS cbswhgr,checkcomment AS cbswhgyj,meetcomment AS hq,leadercomment AS ldps,doccomeunit AS lwdw,sender AS cs, '"+
    	treeid+"' as treeid,2 as status FROM EDoc_Archive where isend=? and EDOC_Property=?";
    	System.out.println("====="+sql+"======");
    	Object[] param = new Object[2];
    	param[0] = 1;
    	param[1] = edoc_property;
    	List list = manager.queryForList(sql, param);
    	if(list.size()>0){
	    	//得到表名
			String tableName = getTableName();
	    	List<SysTempletfield> fieldList = treeService.getTreeOfTempletfield(treeid, tableType);
	    	//添加到本库
	    	dynamicService.insert(list, tableName, fieldList);
	    	//修改OA库
	    	Object[] param_up = new Object[3];
	    	param_up[0] = 0;
	    	param_up[1] = 1;
	    	param_up[2] = edoc_property;
	    	String oa_sql_up = "UPDATE EDoc_Archive SET isend=? WHERE isend=? AND EDOC_Property=?";
	    	manager.updateObject(oa_sql_up, param_up);
	    	//读取文件
	    	for(int i=0;i<list.size();i++){
	    		Map data = (Map) list.get(i);
	    		String archiveId = String.valueOf(data.get("id"));
	    		readSysDoc(archiveId);
	    	}
    	}
    	return null;
    }
    //获取用户名
    private String getTableName(){
    	List<SysTable> tableList = treeService.getTreeOfTable(treeid); 
		String tableName = "";
		//得到表名
		for (int i=0;i<tableList.size();i++) {
			if (tableList.get(i).getTabletype().equals(tableType)) {
				tableName = tableList.get(i).getTablename();
				break;
			}
		}
		return tableName;
    }
    /**
     * 读取物理文件信息入库
     * @param archiveId
     * */
    private void readSysDoc(String archiveId){
    	Object[] param = new Object[1];
    	param[0] = archiveId;
    	String sql ="SELECT id,archive_id,file_name,file_type,content,file_size FROM Doc_Content where archive_id=?";
//    	List sysList = manager.queryForList(sql, param);
    	ResultSet rset = manager.preExecuteSelect(sql, param);
    	try {
			while(rset.next()){
				SysDoc sysDoc = new SysDoc();
	        	String docid = UUID.randomUUID().toString();
	        	sysDoc.setDocid(docid);
	    		String fileName = rset.getString("file_name"); //文件名称
	    		String file_type = rset.getString("file_type");	//文件类型
	    		
	    		sysDoc.setDocoldname(fileName);
	    		sysDoc.setDocext(file_type); //扩展名   
	    		sysDoc.setDocnewname(docid + file_type); //新的文件名
	    		sysDoc.setDocpath(docid + file_type); //
	    		sysDoc.setDoclength(rset.getString("file_size")); //文件大小
	    		InputStream in = rset.getBinaryStream("content"); //文件内容
	    		
	    		
	    		Map<String, String> docServer = getDocServer();
	        	sysDoc.setDocserverid(docServer.get("serverId"));
	        	sysDoc.setDoctype("0");
	        	sysDoc.setCreatetime(CommonUtils.getTimeStamp());
	        	sysDoc.setFileid(archiveId);
	        	sysDoc.setTableid("a189736b-90b0-4ff2-92e9-8de1195d036c");
	        	//
	        	String newFileName = docid + file_type;
	        	writeFile(docServer.get("serverPath"), newFileName, in);
	        	
	        	docService.insertDoc(sysDoc);
	        	String tableName = getTableName();
	        	String archive_sql = "update " + tableName + " set isdoc = 1 where id='" + archiveId + "'";
	        	List<String> sqlList = new ArrayList<String>();
	        	sqlList.add(archive_sql);
	        	dynamicService.update(sqlList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
    }
    //写物理文件
    private void writeFile(String filePath,String fileName,InputStream in ){
    	File file=new File(filePath+"/"+fileName);//可以是任何图片格式.jpg,.png等
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			int tmp =0;
			while((tmp=in.read())!=-1){
				fos.write(tmp);
			}
			in.close();
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    /**
     * 获取DocServer
     * */
    private Map<String, String> getDocServer(){
    	Map<String, String> docServer = new HashMap<String, String>();
    	SysDocserverExample example = new SysDocserverExample();
		List<SysDocserver> docserverList = docserverService.selectByWhereNotPage(example);
		if(null!=docserverList && docserverList.size()>0){
			for (SysDocserver sysDoc : docserverList) {
				if(sysDoc.getServerstate()==1){
					docServer.put("serverId", sysDoc.getDocserverid());
					docServer.put("serverPath", sysDoc.getServerpath());
				}
			}
		}
		return docServer;
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public IDocserverService getDocserverService() {
		return docserverService;
	}

	public void setDocserverService(IDocserverService docserverService) {
		this.docserverService = docserverService;
	}

	public IDocService getDocService() {
		return docService;
	}

	public void setDocService(IDocService docService) {
		this.docService = docService;
	}

	public String getEdoc_property() {
		return edoc_property;
	}

	public void setEdoc_property(String edocProperty) {
		edoc_property = edocProperty;
	}

	
}
