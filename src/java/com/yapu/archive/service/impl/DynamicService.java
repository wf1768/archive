package com.yapu.archive.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.yapu.archive.dao.impl.DynamicDAOImpl;
import com.yapu.archive.entity.DynamicExample;
import com.yapu.archive.entity.SysTempletfield;
import com.yapu.archive.service.itf.IDynamicService;

public class DynamicService implements IDynamicService {
	
	private DynamicDAOImpl dynamicDao;

	@SuppressWarnings("unchecked")
	public List selectByExample(DynamicExample example) {
		return dynamicDao.selectByExample(example);
	}
	
	@SuppressWarnings("unchecked")
	public List selectBySql(String sql,int index,int size) {
		return dynamicDao.selectListPageBySql(sql, index, size);
	}
	//分页
	public List selectListPageByExample(DynamicExample example,int index,int size) {
		return dynamicDao.selectListPageByExample(example, index, size);
	}
	
	public int rowCount(String sql){
		return dynamicDao.countByExample(sql);
	}
    
    public boolean insert(List<HashMap<String, String>> list,String tableName,List<SysTempletfield> fieldList) {

        //sb存储insert语句values前的
        StringBuffer sb = new StringBuffer();
        //value 存储values之后的
        StringBuffer value = new StringBuffer();
        try {
            for (int z=0;z<list.size();z++) {
                //创建insert sql
                HashMap<String,String> row = (HashMap<String,String>) list.get(z);
                sb.append("insert into ").append(tableName);

                sb.append(" (");
                value.append(" (");
                for (SysTempletfield field : fieldList) {
                    sb.append(field.getEnglishname()).append(",");
                    Object col_value = row.get(field.getEnglishname().toLowerCase());
                   
                    if (field.getFieldtype().contains("VARCHAR")) {

                        if (col_value == null) {
                            value.append("'',");
                        }
                        else {
//                            Object obj = row.get(field.getEnglishname().toLowerCase());
                            String tmp = String.valueOf(col_value);;
                            tmp = tmp.replace("\n"," ");
//                            tmp = tmp.replaceAll("\\\\"," - ");
//                            tmp = tmp.replace("'"," ");
                            tmp = tmp.replaceAll("\\\\","\\\\\\\\");
                            tmp = tmp.replaceAll("[\\t\\n\\r]", "");
                            tmp = tmp.replace("'","\\\'");

//                            String tmp = row.get(field.getEnglishname().toLowerCase()).replaceAll("\\\\","\\\\\\\\");
//                            tmp = tmp.replace("'","\\\'");
//                            value.append("'").append(row.get(field.getEnglishname().toLowerCase())).append("',");
                            value.append("'").append(tmp).append("',");
                        }
                    }
                    else if (field.getFieldtype().contains("timestamp")) {
                        java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String s = format1.format(new Date());
                        value.append("'").append(s).append("',");
                    }
                    else {
                        if (col_value == null || col_value.equals("")) {
                            value.append("'0',");
                        }
                        else {
                            value.append(String.valueOf(col_value)).append(",");
                        }

                    }
                }
                sb.deleteCharAt(sb.length() -1).append(" ) values ");
                value.deleteCharAt(value.length() - 1).append(" )");

                sb.append(value.toString());
                System.out.println(value.toString());
                boolean b = dynamicDao.insert(sb.toString());
                if (!b) {
                    return false;
                }
                //清空sb和value ，进行创建下一条sql
                sb.setLength(0);
                value.setLength(0);
            }
        } catch (Exception e) {
        	e.printStackTrace();
            return false;
        }
        return true;
    }

    
    public boolean update(List<HashMap<String, String>> list, String tableName, List<SysTempletfield> fieldList) {
        //sb存储update语句
        StringBuffer sb = new StringBuffer();
        //存储sql语句
        List<String> sqlList = new ArrayList<String>();
        try {
            for (int z=0;z<list.size();z++) {
                //创建insert sql
                HashMap<String,String> row = (HashMap<String,String>) list.get(z);
                sb.append("update ").append(tableName).append(" set ");

                for (SysTempletfield field : fieldList) {
//                    if (field.getSort() != -1) { 
                        if (!"id".equals(field.getEnglishname().toLowerCase()) && !"createtime".equals(field.getEnglishname().toLowerCase())) {
                            sb.append(field.getEnglishname().toLowerCase()).append("=");
                            String value = row.get(field.getEnglishname().toLowerCase()).replaceAll("\\\\","\\\\\\\\");
                            value = value.replace("'","\\\'");
                            if (field.getFieldtype().contains("VARCHAR")) {
                                sb.append("'").append(value).append("',");
                            }
                            else {
                                sb.append(value).append(",");
                            }
                        }
//                    }
                }
                sb.deleteCharAt(sb.length() -1).append(" where id='").append(row.get("id").toString()).append("'");

                boolean b = dynamicDao.update(sb.toString());
                if (!b) {
                    return false;
                }
//                sqlList.add(sb.toString());
                //清空sb，进行创建下一条sql
                sb.setLength(0);
            }

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public int delete(String sql) {
		return dynamicDao.delete(sql);
	}
	
	public void setDynamicDao(DynamicDAOImpl dynamicDao) {
		this.dynamicDao = dynamicDao;
	}


	public boolean insert(List<String> sqlList) {
		boolean result = false;
		if (sqlList.size() > 0) {
			for (String sql : sqlList) {
				result = dynamicDao.insert(sql);
			}
		}
		return result;
	}

	
	public boolean update(List<String> sqlList) {
		boolean result = false;
		if (sqlList.size() > 0) {
			for (String sql : sqlList) {
				result = dynamicDao.update(sql);
			}
		}
		return result;
	}
	
	
}
