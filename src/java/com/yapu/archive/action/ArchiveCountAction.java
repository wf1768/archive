package com.yapu.archive.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.apache.struts2.ServletActionContext;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yapu.archive.entity.DynamicExample;
import com.yapu.archive.entity.SysTable;
import com.yapu.archive.entity.SysTempletfield;
import com.yapu.archive.service.itf.IDynamicService;
import com.yapu.archive.service.itf.ISearchService;
import com.yapu.archive.service.itf.ITreeService;
import com.yapu.system.common.BaseAction;

public class ArchiveCountAction extends BaseAction{
	
	private IDynamicService dynamicService;
	private ITreeService treeService;
	private ISearchService searchService;
	private String treeid;
	private String importData;
	
	private String tableType;
	
	private String startTime;
	private String endTime;
	
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
     * 获得统计的字段
     * @return
     * @throws IOException
     */
    public String getCountField() throws IOException {
        PrintWriter out = getPrintWriter();

        String result = "error";
        //如果没有得到树节点id，返回error
        if (null == treeid || "".equals(treeid)) {
            out.write(result);
            return null;
        }
        List<SysTempletfield> fieldList = treeService.getTreeOfTempletfield(treeid,tableType);

        Gson gson = new Gson();
       
        System.out.println(gson.toJson(fieldList));
        out.write(gson.toJson(fieldList));
        return null;
    }
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
	        String fieldName = "gdrq"; //时间     
	        //查询归档日期在 "2013-01-01" 到 "2013-01-03" 之间的  
	        TermRangeQuery tq = new TermRangeQuery(fieldName, startTime, endTime, false, true); 
	        booleanQuery.add(tq,BooleanClause.Occur.MUST);
        //}
        
        HashMap<String,Integer> numMap = searchService.searchNumber(tableName, booleanQuery, treeid);
        
        int count = numMap.get(treeid); //统计的数量
        System.out.println("统计数量："+count);
        out.write(count+"");
        return null;
    }
    //多条件查询
    public void testTermRangeQuery(){  
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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}
