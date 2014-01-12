package com.yapu.system.action;

/**
 * 帐户组相关操作
 * 2011-08-18
 * @author wangf
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.google.gson.Gson;
import com.yapu.archive.entity.SysOrgTree;
import com.yapu.archive.entity.SysTree;
import com.yapu.archive.entity.SysTreeExample;
import com.yapu.archive.service.itf.ITreeService;
import com.yapu.system.common.BaseAction;
import com.yapu.system.entity.SysOrg;
import com.yapu.system.entity.SysOrgExample;
import com.yapu.system.service.itf.IOrgService;

public class OrgAction extends BaseAction {
	
	private static final long serialVersionUID = 1L;

	private IOrgService orgService;
	private ITreeService treeService;
	
	private String nodeId;
	private String orgid;
	private String orgname;
	private String parentid;
	
	private String newParentid;
	
	/**
	 * 读取指定节点下的组，以树节点形式返回json。
	 * @return
	 */
	public String loadOrgTreeData() throws IOException {
		PrintWriter out  = this.getPrintWriter();
		//获得父节点为nodeId的子节点
		SysOrgExample example = new SysOrgExample();
		example.createCriteria().andParentidEqualTo(nodeId);
		List<SysOrg> orgs = orgService.selectByWhereNotPage(example);

		String tree = getTree(nodeId, orgs);
		out.write(tree);
		return null;
	}
	public String getTree(String id,List<SysOrg> orgs){
		String jsonData = "";
		if (id.equals("0")){ //初始时只加载根节点
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject = new JSONObject();
			for (Iterator iterator = orgs.iterator(); iterator.hasNext();){
				SysOrg tree = (SysOrg)iterator.next();
				if (tree.getOrgid().equals("1")){
					JSONObject attro = new JSONObject();
					attro.put("id", (new StringBuilder("")).append(tree.getOrgid()).toString());
					attro.put("rel", "default");
					jsonObject.put("data", tree.getOrgname());
					jsonObject.put("attr", attro);
					jsonObject.put("state", "closed");
					jsonArray.add(jsonObject);
				}
			}
			jsonData = jsonArray.toString();
		} else{
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject = new JSONObject();
			for (Iterator iterator1 = orgs.iterator(); iterator1.hasNext(); jsonArray.add(jsonObject)){
				SysOrg tree = (SysOrg)iterator1.next();
				JSONObject attro = new JSONObject();
				attro.put("id", (new StringBuilder("")).append(tree.getOrgid()).toString());
				attro.put("rel", "default");
				jsonObject.put("data", tree.getOrgname());
				jsonObject.put("attr", attro);
				
				SysOrgExample example = new SysOrgExample();
				example.createCriteria().andParentidEqualTo(tree.getOrgid());
				List<SysOrg> orgss = orgService.selectByWhereNotPage(example);
				//如果还有子节点添加状态
				if (orgss != null && orgss.size() >= 1)
					jsonObject.put("state", "closed");
			}

			jsonData = jsonArray.toString();
		}
		System.out.println((new StringBuilder(String.valueOf(jsonData))).append(":::::jsonArray").toString());
		return jsonData;
	}
	/**
	 * 新建帐户组
	 * @return
	 * @throws IOException
	 */
	public String addOrg() throws IOException {
		String result =	"error";
		PrintWriter out  = this.getPrintWriter();
		if (orgname == null) {
			out.write(result);
			return null;
		}
		SysOrg org = new SysOrg();
		String orgid = UUID.randomUUID().toString();
		org.setOrgid(orgid);
		org.setOrgname(orgname);
		org.setParentid(parentid);
		org.setOrgorder(1);
		if(orgService.insertOrg(org)){
			result=orgid;
		}
		out.write(result);
		return null;
	}
	/**
	 * 更新帐户组
	 * @return
	 */
	public String updateOrg() throws IOException {
		String result = "failure";
		HttpServletResponse response = getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out  = response.getWriter();
		
		if (null != orgid && !"".equals(orgid)) {
			SysOrg org = orgService.selectByPrimaryKey(orgid);
			org.setOrgname(orgname);
			if (orgService.updateOrg(org) > 0) {
				result = "succ";
			}
		}
		
		out.write(result);
		return null;
	}
	/**
	 * 删除账户组
	 * @return
	 */
	public String deleteOrg() throws IOException {
		String result =	"failure";
		
		HttpServletResponse response = getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out  = response.getWriter();
		
		if (null != orgid && !"".equals(orgid)) {
			int num = orgService.deleteOrg(orgid);
			if (num >0) {
				result = "succ";
			}
		}
		out.write(result);
		return null;

	}
	
	/**
	 * 移动组到新的组下
	 * @return
	 */
	public String moveOrg() throws IOException {
		String result = "failure";
		
		HttpServletResponse response = getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out  = response.getWriter();
		
		if (null != orgid && !"".equals(orgid)) {
			//判断
			//获得父节点为orgid的子节点
			SysOrgExample example = new SysOrgExample();
			example.createCriteria().andParentidEqualTo(newParentid); //nodeId
			List<SysOrg> childOrgsList = orgService.selectByWhereNotPage(example);
			if (childOrgsList.size() > 0) {
				for (SysOrg sysOrg : childOrgsList) {
					if (sysOrg.getOrgid().equals(newParentid)) {
						result = "error";
						out.write(result);
						return null;
					}
				}
			}
			SysOrg org = new SysOrg();
			org.setOrgid(orgid);
			org.setParentid(newParentid);
			int num = orgService.updateOrg(org);
			if (num > 0) {
				result = "succ";
			}
		}
		out.write(result);
		return null;
	}
	
	public String getOrgTree() throws IOException {
		PrintWriter out = this.getPrintWriter();
		SysOrg org = new SysOrg();
		org.setOrgid(orgid);
		List<SysOrgTree> treeList = orgService.getOrgOfTree(org);
		Gson gson = new Gson();
		out.write(gson.toJson(treeList));
		return null;
		
	}
	
	/**
	 * 读取档案节点树.全部读取 组
	 * @author guodh 
	 * @return
	 * @throws IOException 
	 */
	public String loadOrgOfTreeData() throws IOException {
		PrintWriter out  = this.getPrintWriter();
		SysOrg org = new SysOrg();
		org.setOrgid(orgid);
		List<SysOrgTree> treeList = orgService.getOrgOfTree(org);
		//创建根节点 档案树节点
		//JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		String children_tree = getTreeJson(parentid, treeList);
		if(parentid.equals("0")){
			JSONObject attro = new JSONObject();
			attro.put("id", "0");
			attro.put("rel", "root");
			jsonObject.put("data", "档案树节点");
			jsonObject.put("attr", attro);
			jsonObject.put("state", "open");
			jsonObject.put("children", children_tree);
			String json_tree = jsonObject.toString();
			out.write(json_tree);
		}else{
			out.write(children_tree);
		}
		
		return null;
	}
	/**
	 * 加载档案树所有节点
	 * */
	public String getTreeJson(String parentId,List<SysOrgTree> treeList){
		String jsonData = "";
		//得到节点
		SysTreeExample example = new SysTreeExample();
		example.createCriteria().andParentidEqualTo(parentId);
		example.setOrderByClause("CONVERT(treename USING gbk)");
		List<SysTree> trees = treeService.selectByWhereNotPage(example);
		JSONArray jsonArray = new JSONArray();
		
		for (Iterator iterator1 = trees.iterator(); iterator1.hasNext(); ){
			SysTree tree = (SysTree)iterator1.next();
			JSONObject jsonObject = new JSONObject();
			JSONObject attro = new JSONObject();
			
			SysTreeExample example1 = new SysTreeExample();
			example1.createCriteria().andParentidEqualTo(tree.getTreeid());
			example1.setOrderByClause("CONVERT(treename USING gbk)");
			List<SysTree> trees1 = treeService.selectByWhereNotPage(example1);
			if (trees1 != null && trees1.size() >= 1){
				attro.put("id", (new StringBuilder("")).append(tree.getTreeid()).toString());
				attro.put("rel", "folder");
				jsonObject.put("attr", attro);
				jsonObject.put("data", tree.getTreename());
				//jsonObject.put("state", "closed");
				String children_tree = getTreeJson(tree.getTreeid(), treeList);
				jsonObject.put("children", children_tree);
			}else{
				
				attro.put("id", (new StringBuilder("")).append(tree.getTreeid()).toString());
				attro.put("rel", "default");
				//已经应有的权限
				if(treeList!=null && treeList.size()>0){
					for(SysOrgTree orgTree:treeList){
						if(orgTree.getTreeid().equals(tree.getTreeid())){
							attro.put("CLASS", "jstree-checked");//class不能为小写，可能是关键词的原因，搞了半天
						}
					}
				}
				jsonObject.put("attr", attro);
				jsonObject.put("data", tree.getTreename());
			}
			jsonArray.add(jsonObject);
		}
		
		jsonData = jsonArray.toString();
//		System.out.println((new StringBuilder(String.valueOf(jsonData))).append(":::::jsonArray账户组").toString());
		return jsonData;
	}
	
	
	public void setOrgService(IOrgService orgService) {
		this.orgService = orgService;
	}
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getOrgid() {
		return orgid;
	}
	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}
	public String getNewParentid() {
		return newParentid;
	}
	public void setNewParentid(String newParentid) {
		this.newParentid = newParentid;
	}
	public void setTreeService(ITreeService treeService) {
		this.treeService = treeService;
	}
	
}
