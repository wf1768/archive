package com.yapu.archive.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yapu.archive.entity.DynamicExample;
import com.yapu.archive.entity.SysTable;
import com.yapu.archive.entity.SysTempletfield;
import com.yapu.archive.service.itf.IDynamicService;
import com.yapu.archive.service.itf.ITreeService;
import com.yapu.system.common.BaseAction;
import com.yapu.system.util.Excel;

public class ImportAction extends BaseAction {
	
	private static final long serialVersionUID = -3858031783187186618L;
	
	private File selectfile;//得到上传的文件
	private String id; //当前表ID
	private String treeid;
	private String sltPic;
	private String tableType;
	//如果是文件导入，需要在生成导入数据时，增加父节点id，也就是所属案卷的id
	private String selectAid;
	
	private String importData;
	
	private ITreeService treeService;
	private IDynamicService dynamicService;
	
	/*
	 * 接收上传文件，读取文件，返回数据
	 */
	public String upload() throws Exception {

		PrintWriter out = getPrintWriter();
		
		//得到excel表内容
		Excel e = new Excel();
		Vector v = e.readFromExcel(selectfile);
		//得到excel表第一行列头，作为字段名称
		String excelFieldName = "";
		if (v.size() > 2) {
			excelFieldName = (String) v.get(0);
		}
		else {
			out.write("<script>parent.showCallback('failure','Excel文件读取错误，请检查Excel文件中是否包含上传数据。')</script>");
			return null;
		}
		
		List<String> excelField = Arrays.asList(v.get(0).toString().split(","));
		
		//得到数据库字段
		List<SysTempletfield> fieldList = treeService.getTreeOfTempletfield(treeid,tableType);
		//存储导入字段
		List<String> tmpFieldList = new ArrayList<String>();
		//存储没有出现在excel里的字段。
		List<SysTempletfield> noImportFieldList = new ArrayList<SysTempletfield>();
		//数据库字段名称与excel列头对比，找出需要导入哪些字段
		String[] stringArr = new String[excelField.size()];
        int tmpArrNumber = 0;
		for (int i=0;i<fieldList.size();i++) {

			String a = fieldList.get(i).getChinesename();
			int num = excelField.indexOf(a);
			if (num >= 0) {
//				stringArr[tmpArrNumber] = fieldList.get(i).getEnglishname();
//                tmpArrNumber += 1;
                tmpFieldList.add(fieldList.get(i).getEnglishname());
			}
			else if (fieldList.get(i).getSort() >= 0) {
				noImportFieldList.add(fieldList.get(i));
			}
		}
		
//		tmpFieldList = Arrays.asList(stringArr);
		
		if (tmpFieldList.size() <= 0) {
			out.write("<script>parent.showCallback('failure','Excel文件读取错误，请检查Excel文件是否符合本系统的要求！')</script>");
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
//		sb.append("{\"total\":").append(v.size()-1).append(",\"rows\":[");
		sb.append("[");
		//创建json数据
		for (int i=1;i<v.size();i++) {
			List<String> row = Arrays.asList(v.get(i).toString().split(","));
			//生成系统字段
			sb.append("{").append("\"id\":\"").append(UUID.randomUUID()).append("\",\"treeid\":\"");
			sb.append(treeid).append("\",\"isdoc\":\"0\",\"rownum\":\"").append(i).append("\",");
            sb.append("\"status\":\"1\",");
			if (tableType.equals("02")) {
				sb.append("\"parentid\":\"").append(selectAid).append("\",");
			}
			//生成excel导入字段数据
			for (int j=0;j<tmpFieldList.size();j++) {
				sb.append("\"");
				sb.append(tmpFieldList.get(j).toString().toLowerCase());
				sb.append("\":");
				sb.append("\"");
				try {
                    String tmp = row.get(j);
//                    String tmp = row.get(j).replaceAll("\\\\","\\\\\\\\");

                    tmp = tmp.replace("\n"," ");
                    tmp = tmp.replaceAll("\\\\","\\\\\\\\");
//                    tmp = tmp.replace("\\r"," ");
                    tmp = tmp.replace("'","\\\'");
//					sb.append(row.get(j));
                    sb.append(tmp);
				} catch (Exception e2) {
					
				}
				
				sb.append("\",");
			}
			//生成非excel导入字段，并且字段类型是int或有默认值的
			for (SysTempletfield field : noImportFieldList) {
				if (field.getFieldtype().equals("INT")) {
					sb.append("\"");
					sb.append(field.getEnglishname().toString().toLowerCase());
					sb.append("\":");
					sb.append("\"");
					try {
						sb.append("0");
					} catch (Exception e2) {
						
					}
					sb.append("\",");
				}
				else {
					sb.append("\"");
					sb.append(field.getEnglishname().toString().toLowerCase());
					sb.append("\":");
					sb.append("\"");
					try {
						sb.append(field.getDefaultvalue());
					} catch (Exception e2) {
						
					}
					sb.append("\",");
				}
			}
			sb.deleteCharAt(sb.length() - 1).append("},");
		}
//		sb.deleteCharAt(sb.length() - 1).append("]}");
		sb.deleteCharAt(sb.length() - 1).append("]");
//		out.write("<script>parent.showCallback('success','"+sb.toString()+"')</script>");

        out.write("<script>var tmp = "+sb.toString()+"; parent.showCallback('success',tmp);</script>");
		return null;
	}
	
	public String insertImport() throws IOException {
		String result = "保存成功！";
		PrintWriter out = getPrintWriter();
		
		Gson gson = new Gson();
		List<HashMap<String,String>> archiveList = new ArrayList<HashMap<String, String>>();
		
		try {
			//将传入的json字符串，转换成list
			archiveList = (List)gson.fromJson(importData, new TypeToken<List<HashMap<String,String>>>(){}.getType());
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
		String tableName = "";
		//得到表名
		for (int i=0;i<tableList.size();i++) {
			if (tableList.get(i).getTabletype().equals(tableType)) {
				tableName = tableList.get(i).getTablename();
				break;
			}
		}
		List<SysTempletfield> fieldList = treeService.getTreeOfTempletfield(archiveList.get(0).get("treeid").toString(), tableType);

        boolean b = dynamicService.insert(archiveList,tableName,fieldList);
        if (!b) {
            result = "保存错误，操作中止，请检查数据。";
        }
		out.write(result);
		return null;
	}
	
	/**
	 * 删除-案卷
	 * */
	public String deleteImport() throws IOException{
		PrintWriter out = this.getPrintWriter();
        //如果没有得到树节点id，返回error
        if (null == treeid || "".equals(treeid)) {
            out.write("error");
            return null;
        }
        //得到树节点对应的表集合
        List<SysTable> tableList = treeService.getTreeOfTable(treeid);
        String tableName1 = ""; //案卷表名
        String tableName2 = ""; //文件表名
        for (int i=0;i<tableList.size();i++) {
            if (tableList.get(i).getTabletype().equals(tableType)) {
            	tableName1 = tableList.get(i).getTablename();
                break;
            }
        }
        for (int i=0;i<tableList.size();i++) {
            if (tableList.get(i).getTabletype().equals("02")) {
            	tableName2 = tableList.get(i).getTablename();
                break;
            }
        }
        
        String sql1 = "delete from "+tableName1+" where id='"+id+"'";
        String sql2 = "delete from "+tableName2+" where parentid='"+id+"'";
        int num = 1;
        num = dynamicService.delete(sql1);
        if(num>0){
            DynamicExample de = new DynamicExample();
            DynamicExample.Criteria criteria = de.createCriteria();
            criteria.andEqualTo("treeid",treeid);
            criteria.andEqualTo("parentid", id);
            de.setTableName(tableName2);
            List dynamicList = dynamicService.selectByExample(de);
            for(int i=0;i<dynamicList.size();i++){
            	Gson gson = new Gson();
                String str = gson.toJson(dynamicList.get(i));
                try {
     				JSONObject jsonObject = new JSONObject(str);
     				String picName = jsonObject.get("SLT").toString();
     				deleteFile(picName); //删除图片
     			} catch (JSONException e) {
     				e.printStackTrace();
     			}
            }
           
        	dynamicService.delete(sql2);
        	
        	out.write("succ");
        }
        return null;
	}
	
	/**
	 * 删除-文件
	 * @throws IOException 
	 * */
	public String deleteFile() throws IOException{
		PrintWriter out = this.getPrintWriter();
        //如果没有得到树节点id，返回error
        if (null == treeid || "".equals(treeid)) {
            out.write("error");
            return null;
        }
        //得到树节点对应的表集合
        List<SysTable> tableList = treeService.getTreeOfTable(treeid);
		String tableName = ""; //文件表名
		for (int i=0;i<tableList.size();i++) {
            if (tableList.get(i).getTabletype().equals(tableType)) {
            	tableName = tableList.get(i).getTablename();
                break;
            }
        }
		String sql = "delete from "+tableName+" where id='"+id+"'";
		int num = dynamicService.delete(sql);
		if(num>0){
			deleteFile(sltPic);//删除图片
			out.write("succ");
		}
		return null;
	}
	/**  
	 * 删除单个文件  
	 * @param   fileName    被删除文件的文件名  
	 * @return 单个文件删除成功返回true，否则返回false  
	 */  
	public boolean deleteFile(String fileName) {   
		String sPath = ServletActionContext.getServletContext().getRealPath("/webpage/media")+ File.separator;
		sPath = sPath+fileName;
		boolean flag = false;   
	    File file = new File(sPath);   
	    // 路径为文件且不为空则进行删除   
	    if (file.isFile() && file.exists()) {   
	        file.delete();   
	        flag = true;   
	    }   
	    return flag;   
	}  
	
	//得到一个案卷
	public String getImport() throws IOException{
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
        criteria.andEqualTo("id", id);
        for (int i=0;i<tableList.size();i++) {
            if (tableList.get(i).getTabletype().equals(tableType)) {
                de.setTableName(tableList.get(i).getTablename());
                break;
            }
        }
        
        List dynamicList = dynamicService.selectByExample(de);
        
        Gson gson = new Gson();
//        System.out.println(gson.toJson(dynamicList));
        out.write(gson.toJson(dynamicList));
        return null;
	}
	
	//
	public String updateImport() throws IOException {
		String result = "保存完毕。";
		PrintWriter out = getPrintWriter();
		
		Gson gson = new Gson();
		List<HashMap<String,String>> archiveList = new ArrayList<HashMap<String, String>>();
		
		try {
			//将传入的json字符串，转换成list
			archiveList = (List)gson.fromJson(importData, new TypeToken<List<HashMap<String,String>>>(){}.getType());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			result = "修改失败，请重新尝试，或与管理员联系。";
			out.write(result);
			return null;
		}
		
		if (archiveList.size() <= 0) {
			result = "没有找到数据，请重新尝试或与管理员联系。";
			out.write(result);
			return null;
		}
		
		List<SysTable> tableList = treeService.getTreeOfTable(archiveList.get(0).get("treeid").toString());
		String tableName = "";
		//得到表名
		for (int i=0;i<tableList.size();i++) {
			if (tableList.get(i).getTabletype().equals(tableType)) {
				tableName = tableList.get(i).getTablename();
				break;
			}
		}
		List<SysTempletfield> fieldList = treeService.getTreeOfTempletfield(archiveList.get(0).get("treeid").toString(), tableType);
        boolean b = dynamicService.update(archiveList,tableName,fieldList);
		if (!b) {
            result = "修改失败，请检查数据！";
        }
		out.write(result);
		return null;
	}
	
	/**
	 * 设置封面
	 * @throws IOException 
	 * */
	public String setCoverPic() throws IOException{
		PrintWriter out = this.getPrintWriter();
        //如果没有得到树节点id，返回error
        if (null == treeid || "".equals(treeid)) {
            out.write("error");
            return null;
        }
        //得到树节点对应的表集合
        List<SysTable> tableList = treeService.getTreeOfTable(treeid);
		String tableName = ""; //表名
		for (int i=0;i<tableList.size();i++) {
            if (tableList.get(i).getTabletype().equals(tableType)) {
            	tableName = tableList.get(i).getTablename();
                break;
            }
        }
		String sql = "update "+tableName+" set slt='"+sltPic+"'"+" where id='"+id+"'";
		List<String> sqlList = new ArrayList<String>();
		sqlList.add(sql);
		boolean flag = dynamicService.update(sqlList);
		if(flag){
			out.write("succ");
		}else{
			out.write("error");
		}
		return null;
	}

	public File getSelectfile() {
		return selectfile;
	}
	public void setSelectfile(File selectfile) {
		this.selectfile = selectfile;
	}
	public String getTreeid() {
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}
	public String getTableType() {
		return tableType;
	}
	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public void setTreeService(ITreeService treeService) {
		this.treeService = treeService;
	}

	public String getImportData() {
		return importData;
	}

	public void setImportData(String importData) {
		this.importData = importData;
	}

	public void setDynamicService(IDynamicService dynamicService) {
		this.dynamicService = dynamicService;
	}

	public String getSelectAid() {
		return selectAid;
	}

	public void setSelectAid(String selectAid) {
		this.selectAid = selectAid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSltPic() {
		return sltPic;
	}

	public void setSltPic(String sltPic) {
		this.sltPic = sltPic;
	}
	
}
