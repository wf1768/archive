package com.yapu.archive.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yapu.archive.entity.*;
import com.yapu.archive.service.itf.IDocService;
import com.yapu.archive.service.itf.IDynamicService;
import com.yapu.archive.service.itf.ITempletfieldService;
import com.yapu.archive.service.itf.ITreeService;
import com.yapu.archive.vo.Auth;
import com.yapu.system.common.BaseAction;
import com.yapu.system.entity.SysAccount;
import com.yapu.system.entity.SysOrg;
import com.yapu.system.service.itf.IAccountService;
import com.yapu.system.service.itf.IOrgService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArchiveAction extends BaseAction {

	private static final long serialVersionUID = 4009249393951511369L;

	private ITreeService treeService;
	private IDynamicService dynamicService;
	private ITempletfieldService templetfieldService;
	private IAccountService accountService;
	private IOrgService orgService;
	 private IDocService docService;
	private String treeid;
	private String parentid;
	private String tableType;
    private String selectAid;
    private String isAllWj;
    //如果是导入类型，读取字段时，去掉文件级标识
    private String importType; 


	private String par;

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
	        Auth auth = this.isAuth(treeid);
	        if (auth.getFilescan()) {
	        	result.append("if (value>0) {return  '<img src=\"../../images/icons/flag_blue.png\" style=\"cursor:pointer\" title=\"点击查看电子文件\" onclick=\"showDocwindow(\\''+ dataContext.id + '\\',\\'"+tableid+"\\')\" />'  } else { return \"\"}}},");
	        }
	        else {
	        	result.append(" return \"\"}},");
	        }
//	        result.append("if (value>0) {return  '<img src=\"../../images/icons/flag_blue.png\" style=\"cursor:pointer\" title=\"点击查看电子文件\" onclick=\"showDocwindow(\\''+ dataContext.id + '\\',\\'"+tableid+"\\')\" />'  } else { return \"\"}}},");
        }

//		//返回字段特殊属性，例如默认值，在页面添加时，直接赋值
		StringBuilder fieldsDefaultValue = new StringBuilder();
		fieldsDefaultValue.append("; var fieldsDefaultValue = {");
		for (SysTempletfield field : fieldList) {
//			if (!"".equals(field.getDefaultvalue())) {
			if (field.getSort() >= 0) {
				fieldsDefaultValue.append(field.getEnglishname().toLowerCase());
				fieldsDefaultValue.append(":'").append(field.getDefaultvalue()).append("',");
//				fieldsDefaultValue.append("{fieldname:'");
//				fieldsDefaultValue.append(field.getEnglishname());
//				fieldsDefaultValue.append("',fieldtype:'");
//				fieldsDefaultValue.append(field.getFieldtype());
//				fieldsDefaultValue.append("',value:'");
//				fieldsDefaultValue.append(field.getDefaultvalue());
//				fieldsDefaultValue.append("'},");
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
//					result.append(",formatter:function(row,column,value) {");
//					result.append("if (value == '' || typeof (value) == 'undefined'){");
//					result.append("return 0;");
//					result.append("}");
//					result.append("return value");
//					result.append("}");
				}
				else if (field.getFieldtype().contains("VARCHAR") && field.getIscode() == 0) {
					result.append(",editor:Slick.Editors.Text");
//					if (!"".equals(field.getDefaultvalue())) {
//						result.append(",formatter:function(row,column,value) {");
//						result.append("if (value == '' || typeof (value) == 'undefined'){");
//						result.append("return '").append(field.getDefaultvalue()).append("';");
//						result.append("}");
//						result.append("return value");
//						result.append("}");
//					}
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
//				result.append("}");
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
    
    /**
     * 获取数据访问权限
     * @param 
     * @return
     * */
    public String getDataAuth(String treeid){
    	
    	SysAccount account = super.getAccount();
		//先查看账户本身是否有权限
		List<SysAccountTree> accountTreeList =  accountService.getAccountOfTree(account.getAccountid(), treeid);
		if(accountTreeList.size() >0 && accountTreeList != null){
			SysAccountTree accountTree = accountTreeList.get(0);
			return accountTree.getFilter();
		}else{
			//否则查看该账户的所在组
			SysOrg sysOrg = accountService.getAccountOfOrg(account);
			if(sysOrg!=null){
			 	List<SysOrgTree> orgTreeList = orgService.getOrgOfTree(sysOrg.getOrgid(), treeid);
			 	if(orgTreeList.size() >0 && orgTreeList != null){
			 		return orgTreeList.get(0).getFilter();
			 	}
			}
		}
		return "";
    }

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
		
		StringBuffer sbBuffer = new StringBuffer();
		//获得表名
		for (int i=0;i<tableList.size();i++) {
			if (tableList.get(i).getTabletype().equals(tableType)) {
				sbBuffer.append("select * from ").append(tableList.get(i).getTablename());
				break;
			}
		}
		
		sbBuffer.append(" where (treeid = '").append(treeid).append("'");
		
		if ("0".equals(isAllWj) && "02".equals(tableType)) {
			sbBuffer.append(" and parentid='").append(selectAid).append("'");
			sbBuffer.append(" and status<").append("1").append(")");
        }
        else if ("1".equals(isAllWj) && "02".equals(tableType)) {
        	sbBuffer.append(" and status<").append("1").append(")");
        }
        else if ("01".equals(tableType)){
        	sbBuffer.append(" and status<").append("2").append(")");
        }
		
		//获取数据访问权限   草，还不如自己手写sql。这样的条件类，实现不了
        String filter = getDataAuth(treeid);
        Gson gson = new Gson();
        List list = gson.fromJson(filter, new TypeToken<List>(){}.getType());
        Boolean b = false;
        StringBuffer tmpValue = new StringBuffer();
        if (list != null && list.size() > 0) {
        	for (int i = 0; i < list.size(); i++) {
				HashMap<String, String> map = gson.fromJson(list.get(i).toString(), new TypeToken<HashMap<String, String>>(){}.getType());
				if (map.get("tableType").toString().equals(tableType)) {
					if (tmpValue.length() == 0) {
						b = true;
						tmpValue.append(" and (");
						tmpValue.append(map.get("selectField")).append("='").append(map.get("dataAuthValue")).append("'");
					}
					else {
						tmpValue.append(" and ").append(map.get("selectField")).append("='").append(map.get("dataAuthValue")).append("'");
					}
					
				}
			}
        	if (b) {
        		tmpValue.append(")");
        	}
        }
        
        if (tmpValue.length() > 0) {
        	sbBuffer.append(tmpValue.toString());
        }
        
        sbBuffer.append(" order by createtime desc");
        
        List dynamicList = dynamicService.selectBySql(sbBuffer.toString());
		

		//===================原采用条件类的方式。现在改为手写sql==================
		//得到树节点对应的表集合
//		List<SysTable> tableList = treeService.getTreeOfTable(treeid);
//		DynamicExample de = new DynamicExample();
//        DynamicExample.Criteria criteria = de.createCriteria();
//        criteria.andEqualTo("treeid",treeid);
//
//        if ("0".equals(isAllWj) && "02".equals(tableType)) {
//            criteria.andEqualTo("parentid",selectAid);
//            criteria.andLessThan("status",Long.parseLong("1"));
//        }
//        else if ("1".equals(isAllWj) && "02".equals(tableType)) {
//            criteria.andLessThan("status",Long.parseLong("1"));
//        }
//        else if ("01".equals(tableType)){
//            criteria.andLessThan("status",Long.parseLong("2"));
//        }
//        
//        //获取数据访问权限   草，还不如自己手写sql。这样的条件类，实现不了
//        String filter = getDataAuth(treeid);
//        Gson gson = new Gson();
//        List list = gson.fromJson(filter, new TypeToken<List>(){}.getType());
//        if (list != null && list.size() > 0) {
//        	for (int i = 0; i < list.size(); i++) {
//				HashMap<String, String> map = gson.fromJson(list.get(i).toString(), new TypeToken<HashMap<String, String>>(){}.getType());
//				if (map.get("tableType").toString().equals(tableType)) {
//					if (i==0) {
//						criteria.andEqualTo(map.get("selectField"), map.get("dataAuthValue"));
//					}
//					else {
//						DynamicExample.Criteria tmp = de.createCriteria();
//						tmp.andEqualTo(map.get("selectField"), map.get("dataAuthValue"));
////						de.or(tmp);
//					}
//					
//				}
//			}
//        }
//        
//        de.setOrderByClause("order by createtime desc");
//
//		for (int i=0;i<tableList.size();i++) {
//			if (tableList.get(i).getTabletype().equals(tableType)) {
//				de.setTableName(tableList.get(i).getTablename());
//				break;
//			}
//		}
//		List dynamicList = dynamicService.selectByExample(de);
		
		//===================原采用条件类的方式。现在改为手写sql==================

		//得到字段列表
		List<SysTempletfield> templetfieldList = treeService.getTreeOfTempletfield(treeid, tableType);

		StringBuffer sb = new StringBuffer();
//		sb.append("{\"total\":").append(dynamicList.size()).append(",\"rows\":[");
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
//                sb.append("\"docs\":\"1\",");
//                sb.append("\"docs\":").append(tempMap.get("ISDOC").toString()).append(",");
                sb.append("\"rownum\":").append(i+1).append(",");
                //grid控件需要小写的id。数据库中存的是大写的id，这里全部采用小写字段名
				for (SysTempletfield sysTempletfield : templetfieldList) {
//					if (sysTempletfield.getFieldtype().contains("VARCHAR")) {
						if (null == tempMap.get(sysTempletfield.getEnglishname())) {
							sb.append("\""+sysTempletfield.getEnglishname().toLowerCase()+"\":\"\",");
						}
						else {
                            String value = tempMap.get(sysTempletfield.getEnglishname()).toString().replaceAll("\\\\","\\\\\\\\");
                            value = value.replace("'","\\\'");
                            value = value.replace("\"","\\\"");
                            value = value.replaceAll("[\\t\\n\\r]", "");
//							sb.append("\""+sysTempletfield.getEnglishname().toLowerCase()+"\":\""+tempMap.get(sysTempletfield.getEnglishname())+"\",");
                            sb.append("\""+sysTempletfield.getEnglishname().toLowerCase()+"\":\""+value+"\",");
						}
//					}
//					else if (sysTempletfield.getFieldtype().contains("INT")) {
////						sb.append("\""+sysTempletfield.getEnglishname().toLowerCase()+"\":"+tempMap.get(sysTempletfield.getEnglishname())+",");
//						sb.append("\""+sysTempletfield.getEnglishname().toLowerCase()+"\":\""+tempMap.get(sysTempletfield.getEnglishname())+"\",");
//					}
				}
				sb.deleteCharAt(sb.length() - 1).append("},");
			}
		}
        else {
//            sb.append("{},");
            sb.append(",");
        }
//		sb.deleteCharAt(sb.length() - 1).append("]}");
		sb.deleteCharAt(sb.length() - 1).append("]");
		out.write(sb.toString());
		return null;
	}

    /**
     * 读取原始的档案表数据。不是为grid
     * @return
     */
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
		List<String> idList = new ArrayList<String>(); //档案Id集合
		for (int z=0;z<archiveList.size();z++) {
			//得到id集合
			HashMap<String,String> row = (HashMap<String,String>) archiveList.get(z);
			sbSql.append("'").append(row.get("id").toString()).append("',");
			idList.add(row.get("id").toString());
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

			//TODO 要删除挂接的物理文件
	        for(String id:idList){
	        	SysDocExample example = new SysDocExample();
		        SysDocExample.Criteria criteria = example.createCriteria();
		        criteria.andFileidEqualTo(id);
		     	List<SysDoc> docList = docService.selectByWhereNotPage(example);
		     	for(SysDoc doc:docList){
		     		docService.deleteDoc(doc.getDocid());
		     	}
	        }
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
     * 是否有（查看、下载、打印）权限
     * @param fileType :filescan(查看) filedown(下载) fileprint(打印)
     * @return
     * */
    public Auth isAuth(String treeid){
    	Auth auth = new Auth();
    	auth.setFilescan(false);
    	int filescan = 0;
    	int filedown = 0;
    	int fileprint = 0;
    	SysAccount account = super.getAccount();
		//先查看账户本身是否有权限
		List<SysAccountTree> accountTreeList =  accountService.getAccountOfTree(account.getAccountid(), treeid);
		if(accountTreeList.size() >0 && accountTreeList != null){
			SysAccountTree accountTree = accountTreeList.get(0);
			if (accountTree.getFilescan() == 1) {
				auth.setFilescan(true);
			}
			if (accountTree.getFiledown() == 1) {
				auth.setFiledown(true);
			}
			if (accountTree.getFileprint() == 1) {
				auth.setFileprint(true);
			}
		}else{
			//否则查看该账户的所在组
			SysOrg sysOrg = accountService.getAccountOfOrg(account);
			if(sysOrg!=null){
			 	List<SysOrgTree> orgTreeList = orgService.getOrgOfTree(sysOrg.getOrgid(), treeid);
			 	if(orgTreeList.size() >0 && orgTreeList != null){
			 		SysOrgTree orgTree = orgTreeList.get(0);
			 		
			 		if (orgTree.getFilescan() == 1) {
						auth.setFilescan(true);
					}
					if (orgTree.getFiledown() == 1) {
						auth.setFiledown(true);
					}
					if (orgTree.getFileprint() == 1) {
						auth.setFileprint(true);
					}
			 	}
			}
		}
		return auth;
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

	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}

	public void setOrgService(IOrgService orgService) {
		this.orgService = orgService;
	}

	public IDocService getDocService() {
		return docService;
	}

	public void setDocService(IDocService docService) {
		this.docService = docService;
	}


}
