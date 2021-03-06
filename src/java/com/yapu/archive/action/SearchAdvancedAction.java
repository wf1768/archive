package com.yapu.archive.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yapu.archive.entity.DynamicExample;
import com.yapu.archive.entity.QueryItem;
import com.yapu.archive.entity.SysAccountTree;
import com.yapu.archive.entity.SysOrgTree;
import com.yapu.archive.entity.SysTable;
import com.yapu.archive.entity.SysTemplet;
import com.yapu.archive.entity.SysTempletfield;
import com.yapu.archive.service.itf.IDynamicService;
import com.yapu.archive.service.itf.ISearchService;
import com.yapu.archive.service.itf.ITreeService;
import com.yapu.system.common.BaseAction;
import com.yapu.system.entity.SysAccount;
import com.yapu.system.entity.SysOrg;
import com.yapu.system.service.itf.IAccountService;
import com.yapu.system.service.itf.IOrgService;
import com.yapu.system.util.BaseSelector;

public class SearchAdvancedAction extends BaseAction{
	private IAccountService accountService;
	private IOrgService orgService;
	
	private IDynamicService dynamicService;
	private ITreeService treeService;
	private ISearchService searchService;
	private String treeid;
	private String importData;
	
	private String tableType;
	
	private String groupitem;
	/**
     * 读取原始的档案表数据。
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
     * 获得表字段
     * @return
     * @throws IOException
     */
    public String getField() throws IOException {
        PrintWriter out = getPrintWriter();
        //如果没有得到树节点id，返回error
        if (null == treeid || "".equals(treeid)) {
            out.write("error");
            return null;
        }
        
      //得到节点对应的模板。用来判断档案类型
		SysTemplet templet = treeService.getTreeOfTemplet(treeid);
		 String result = "var templeType='"+templet.getTemplettype()+"';";
        List<SysTempletfield> fieldList = treeService.getTreeOfTempletfield(treeid,tableType);

      //返回字段特殊属性，例如默认值，在页面添加时，直接赋值
        List<SysTempletfield> fields = new ArrayList<SysTempletfield>();
		for (SysTempletfield field : fieldList) {
			if (field.getSort() >= 0 && field.getIssearch()>0) {
				fields.add(field);
			}
		}
        Gson gson = new Gson();
        if(fields.size()>0){
        	result += "var fields = "+gson.toJson(fields);
        }
        out.write(result);
        return null;
    }

    /**
     * 高级查询
     * */
    public String searchAdvanced() throws IOException{
//    	int totalRow=0;		//总记录数
//		String pageInfo="";	//分页信息
		
    	PrintWriter out = getPrintWriter();
        //增加多媒体文件级表内容
        List<SysTable> tableList = treeService.getTreeOfTable(treeid);
        String tableName = "";
        HashMap<String, String> fMap = null;
        //得到表名
        for (int i=0;i<tableList.size();i++) {
            if (tableList.get(i).getTabletype().equals(tableType)) {
                tableName = tableList.get(i).getTablename();
                //权限字段
        		fMap = getDataAuthField(treeid,tableType);
                break;
            }
        }
        String fieldAuth_Sql = ""; //权限字段Sql
		if(fMap != null){
	        Set<Map.Entry<String, String>> set = fMap.entrySet();
	        for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
			    Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
		    	fieldAuth_Sql += " AND "+entry.getKey() +"='" + entry.getValue() +"'";
//			    System.out.println(entry.getKey() + "--->" + entry.getValue());
			}
		}
    	Gson gson = new Gson();
    	//把json对象转成实体对象 
    	List<QueryItem> retList = gson.fromJson(groupitem,  
		         new TypeToken<List<QueryItem>>() {  
		         }.getType());  
    	
    	StringBuffer sql = new StringBuffer(); //列表
    	sql.append("SELECT * FROM "+tableName +" WHERE 1=1 AND TREEID='"+treeid+"'");
    	StringBuffer sql_count = new StringBuffer(); //统计
    	sql_count.append("SELECT COUNT(0) FROM "+tableName +" WHERE 1=1 AND TREEID='"+treeid+"'");
    	for(QueryItem qt:retList){
    		sql.append(" AND " +BaseSelector.getSql(qt.getOperatorType(), qt.getName(), qt.getValue()));
    		sql_count.append(" AND " +BaseSelector.getSql(qt.getOperatorType(), qt.getName(), qt.getValue()));
    	}
    	if(!fieldAuth_Sql.equals("")){
    		sql.append(fieldAuth_Sql);
    		sql_count.append(fieldAuth_Sql);
    	}
//    	System.out.println(sql.toString());
    	//总记录数
    	intRowCount = dynamicService.rowCount(sql_count.toString());
    	//获得分页信息
//    	pageInfo=this.getPageInfoStr(totalRow, intPage, "", "");
    	intPageCount = this.getPageCount();

    	String result = "var intPageCount = '"+intPageCount+"';";
    	result = result+ "var tableName = '"+tableName+"';";
    	
    	
    	List dynamicList = dynamicService.selectBySql(sql.toString(), this.getStartRec(intPage), this.getIntPageSize());
    	result += "var dynamicList = "+gson.toJson(dynamicList);
    	
    	out.write(result);
    	return null;
    }
    
    /**
     * 自定义统计
     * */
    public int getQueryGroups() throws IOException{
    	PrintWriter out = getPrintWriter();
        //增加多媒体文件级表内容
        List<SysTable> tableList = treeService.getTreeOfTable(treeid);
        String tableName = "";
        //得到表名
        for (int i=0;i<tableList.size();i++) {
            if (tableList.get(i).getTabletype().equals(tableType)) {
                tableName = tableList.get(i).getTablename();
                break;
            }
        }
    	Gson gson = new Gson();
    	//把json对象转成实体对象 
    	List<QueryItem> retList = gson.fromJson(groupitem,  
		         new TypeToken<List<QueryItem>>() {  
		         }.getType());  
    	
    	StringBuffer sql = new StringBuffer();
    	sql.append("SELECT COUNT(0) FROM "+tableName +" WHERE 1=1 ");
    	for(QueryItem qt:retList){
    		sql.append(" AND " +BaseSelector.getSql(qt.getOperatorType(), qt.getName(), qt.getValue()));
    	}
    	System.out.println(sql.toString());
    	
    	int num = dynamicService.rowCount(sql.toString());
    	return num;
    }
	
    public String getQuerySql(){
    	
    	return null;
    }
    
    
    //////////////////////////////////搜索引擎-条件查询////////////////////////////////////////////////
    public String archiveCount() throws IOException{
    	PrintWriter out = getPrintWriter();
        //增加多媒体文件级表内容
        List<SysTable> tableList = treeService.getTreeOfTable(treeid);
        String tableName = "";
        //得到表名
        for (int i=0;i<tableList.size();i++) {
            if (tableList.get(i).getTabletype().equals(tableType)) {
                tableName = tableList.get(i).getTablename();
                break;
            }
        }
        Gson gson = new Gson();
        List<HashMap<String,String>> archiveList = new ArrayList<HashMap<String, String>>();
        archiveList = (List)gson.fromJson(importData, new TypeToken<List<HashMap<String,String>>>(){}.getType());
        System.out.println(gson.toJson(archiveList));
        
        BooleanQuery booleanQuery = new BooleanQuery();
        for(int i=0;i<archiveList.size();i++){
        	Map<String, String> retMap2 = archiveList.get(i);  
            for (String key : retMap2.keySet()) {  
                System.out.println("key:" + key + " values:" + retMap2.get(key));  
                Term term = new Term(key,retMap2.get(key));
                TermQuery termQuery = new TermQuery(term);
                booleanQuery.add(termQuery,BooleanClause.Occur.MUST);
            }
        }
        
        //if(!startTime.equals("") && startTime!=null && !endTime.equals("") && endTime!=null){
	       /* String fieldName = "gdrq"; //时间     
	        //查询归档日期在 "2013-01-01" 到 "2013-01-03" 之间的  
	        TermRangeQuery tq = new TermRangeQuery(fieldName, startTime, endTime, false, true); 
	        booleanQuery.add(tq,BooleanClause.Occur.MUST);*/
        //}
        
        HashMap<String,Integer> numMap = searchService.searchNumber(tableName, booleanQuery, treeid);
        
        int count = numMap.get(treeid); //统计的数量
        System.out.println("统计数量："+count);
        out.write(count+"");
        return null;
    }
    //多条件查询
    public void testTermRangeQuery(){  
//    	http://www.pusuo.net/2010-02-14/111095034.html   分词
//    	http://blog.csdn.net/fx_sky/article/details/8543146
        try {  
//        	String path = "/LUCENE" + "/" + "A_C50BD8FC_01"; 
        	String path = "E:\\Program Files\\apache-tomcat-6.0.29\\webapps\\archive1\\LUCENE\\A_C50BD8FC_01";
            File file = new File(path);  
            Directory mdDirectory = FSDirectory.open(file);  
            IndexReader reader = IndexReader.open(mdDirectory);  
            IndexSearcher searcher = new IndexSearcher(reader);  
            
            Term t1 = new Term("gddw","北京");
            TermQuery q1 = new TermQuery(t1);
            Term t2 = new Term("tm","606");
            TermQuery q2 = new TermQuery(t2);
            
            String fieldName = "gdrq"; //时间     
            //查询出版日期在 "2011-04" 到 "2011-07" 之间的书籍  
            TermRangeQuery tq = new TermRangeQuery(fieldName, "2012-01-01", "2013-01-03", false, true); 
            
            BooleanQuery bq = new BooleanQuery();
//            bq.add(q1,BooleanClause.Occur.MUST);
            bq.add(q2,BooleanClause.Occur.MUST);
            bq.add(tq,BooleanClause.Occur.MUST);
            
            TopDocs tops = searcher.search(bq, null, 10);  
            
            int count = tops.totalHits;  
            System.out.println("totalHits="+count);  //数量
            ScoreDoc[] docs = tops.scoreDocs;  
            for(int i=0;i<docs.length;i++){  
                Document doc = searcher.doc(docs[i].doc);  
                //int id = Integer.parseInt(doc.get("id"));  
                String title = doc.get("gddw");  
                String tm = doc.get("tm");  
//                String publishTime = doc.get("publishTime");  
//                String source = doc.get("source");  
//                String category = doc.get("category");  
//                float reputation = Float.parseFloat(doc.get("reputation"));  
                  
                System.out.println("归档单位："+title+"  题名："+tm);  
            }  
              
            reader.close();  
            searcher.close();  
              
        } catch (CorruptIndexException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
    
    
    /**
	 * 获取数据访问的权限字段
	 * @param treeid
	 * @param object
	 * */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getDataAuthField(String treeid,Object object){
		//获取数据访问权限
		String filter = getDataAuth(treeid);
	    Gson gson = new Gson();
	    List list = gson.fromJson(filter, new TypeToken<List>(){}.getType());
	    //权限字段+值
	    HashMap<String, String> fMap = new HashMap<String, String>();
	    if(list !=null && list.size() >0){
	    	for (int i = 0; i < list.size(); i++) {
				HashMap<String, String> map = gson.fromJson(list.get(i).toString(), new TypeToken<HashMap<String, String>>(){}.getType());
				if (map.get("tableType").toString().equals(object)) {
					String selF = map.get("selectField");
					String selFv = map.get("dataAuthValue");
					fMap.put(selF, selFv);
//					System.out.println(treeid+":"+selF+"="+selFv);
				}
			}
	    }
	    return fMap;
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
    
	public ITreeService getTreeService() {
		return treeService;
	}
	public void setTreeService(ITreeService treeService) {
		this.treeService = treeService;
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
	public void setDynamicService(IDynamicService dynamicService) {
		this.dynamicService = dynamicService;
	}

	public void setSearchService(ISearchService searchService) {
		this.searchService = searchService;
	}

	public String getImportData() {
		return importData;
	}

	public void setImportData(String importData) {
		this.importData = importData;
	}

	public String getGroupitem() {
		return groupitem;
	}

	public void setGroupitem(String groupitem) {
		this.groupitem = groupitem;
	}

	public IAccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}

	public IOrgService getOrgService() {
		return orgService;
	}

	public void setOrgService(IOrgService orgService) {
		this.orgService = orgService;
	}

}
