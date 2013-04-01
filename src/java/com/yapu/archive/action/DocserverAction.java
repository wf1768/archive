package com.yapu.archive.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yapu.archive.entity.SysDocserver;
import com.yapu.archive.entity.SysDocserverExample;
import com.yapu.archive.service.itf.IDocserverService;
import com.yapu.system.common.BaseAction;
import com.yapu.system.entity.SysRole;

public class DocserverAction extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	private IDocserverService docserverService;
	
	private String par;
	private String docserverid;
	private SysDocserver docserver = new SysDocserver();
	
	/**
	 * 返回角色的json
	 * @return
	 */
	public String list() throws IOException {
		
		HttpServletResponse response = getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out  = response.getWriter();
		
		SysDocserverExample example = new SysDocserverExample();
		List<SysDocserver> docserverList = docserverService.selectByWhereNotPage(example);
		List<Map> docserList = new ArrayList<Map>();
		if(null!=docserverList && docserverList.size()>0){
			for (SysDocserver sysDoc : docserverList) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("docserverid",sysDoc.getDocserverid());
				map.put("servername",sysDoc.getServername());
				map.put("serverip",sysDoc.getServerip());
				map.put("ftpuser",sysDoc.getFtpuser());
				map.put("ftppassword",sysDoc.getFtppassword());
				map.put("serverport",sysDoc.getServerport());
				map.put("serverpath",sysDoc.getServerpath());
				map.put("servertype",sysDoc.getServertype());
				if(sysDoc.getServerstate()==0){
					map.put("serverstate","备用");
				}else{
					map.put("serverstate","已启用");
				}
				map.put("servermemo",sysDoc.getServermemo());
				
				docserList.add(map);
			}
		}
		else {
			out.write("error");
			return null;
		}
		Gson gson = new Gson();
		String result = "var docList=" + gson.toJson(docserList);
		out.write(result);
		return null;
	}

	public String save() throws IOException {
		HttpServletResponse response = getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out  = response.getWriter();
		String nam = getRequest().getParameter("docserver. Servername ");
		String result = "error";
		docserver.setServerstate(0); //初始状态，添加页不应该有
		if(addDocserver(docserver)){
			result = "succ";
		}
		out.write(result);
		
		return null;
	}
	/**
	 * @throws IOException 
	 * 
	 * */
	public String getSysDocserver() throws IOException{
		HttpServletResponse response = getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out  = response.getWriter();
		SysDocserver docserver = docserverService.selectByPrimaryKey(docserverid);
		if(docserver!=null){
			List<Map> docserverList = new ArrayList<Map>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("docserverid",docserver.getDocserverid());
			map.put("servername",docserver.getServername());
			map.put("serverip",docserver.getServerip());
			map.put("ftpuser",docserver.getFtpuser());
			map.put("ftppassword",docserver.getFtppassword());
			map.put("serverport",docserver.getServerport());
			map.put("serverpath",docserver.getServerpath());
			map.put("servertype",docserver.getServertype());

			map.put("serverstate",docserver.getServerstate());
			map.put("servermemo",docserver.getServermemo());
			docserverList.add(map);
			
			Gson gson = new Gson();
			String result = "var sysDocserver=" + gson.toJson(map);
			out.write(result);
		}else{
			out.write("error");
		}
		return null;
		
	}
	/**
	 * 添加
	 * @param docserver
	 * @return boolean
	 */
	public boolean addDocserver(SysDocserver docserver) {
		boolean result = false;
		if (docserver != null) {
			docserver.setDocserverid(UUID.randomUUID().toString());
			if(docserverService.insertDocserver(docserver)){
				result=true;
			}
		}
		return result;
	}
	/**
	 * 更新
	 * @param docserver
	 * @return
	 * @throws IOException 
	 */
	public String updateDocserver() throws IOException {
		HttpServletResponse response = getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out  = response.getWriter();
		if (docserver != null) {
			if(docserverService.updateDocserver(docserver) > 0){
				out.write("succ");
				return null;
			}
		}
		out.write("error");
		return null;
	}
	/**
	 * 删除
	 * @param 
	 * @return
	 */
	public String delete() throws IOException{
		PrintWriter out  = this.getPrintWriter();
		String result = "error";
		String[] docserverid = par.split(",");
		for(int i=0;i<docserverid.length;i++){
			if(delDocserver(docserverid[i])){
				result = "succ";
			}
		}
		out.write(result);
		return null;
	}
	public boolean delDocserver(String docserverid) {
		boolean result = false;
		if (null != docserverid && !"".equals(docserverid)) {
			int num = docserverService.deleteDocserver(docserverid);
			if (num >0) {
				result = true;
			}
		}
		return result;
	}
	/**
	 * 更改服务器状态(是否启用)
	 * @return
	 * @throws IOException
	 */
	public String setDocserverState() throws IOException {
		HttpServletResponse response = getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out  = response.getWriter();
		
//		String result = "服务器状态已更新完毕。";
		
		try {
			SysDocserverExample example = new SysDocserverExample();
			//将所有服务器状态设置为备用
			SysDocserver docserver = new SysDocserver();
			docserver.setServerstate(0);
			docserverService.updateDocserver(docserver, example);
			
			//将指定的服务器状态修改为启用。
			docserver.setServerstate(1);
			docserver.setDocserverid(docserverid);
			docserverService.updateDocserver(docserver);
		} catch (Exception e) {
//			result = "<span style='color:red'>服务器状态更新失败。</span><br> 请尝试重新操作，或与管理员联系。";
			out.write("error");
		}
		out.write("succ");
		return null;
	}
	
	public String getDocserverid() {
		return docserverid;
	}
	public void setDocserverid(String docserverid) {
		this.docserverid = docserverid;
	}
	public void setDocserverService(IDocserverService docserverService) {
		this.docserverService = docserverService;
	}
	public String getPar() {
		return par;
	}
	public void setPar(String par) {
		this.par = par;
	}
	public SysDocserver getDocserver() {
		return docserver;
	}
	public void setDocserver(SysDocserver docserver) {
		this.docserver = docserver;
	}

}
