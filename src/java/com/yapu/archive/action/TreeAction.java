package com.yapu.archive.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.yapu.archive.entity.SysAccountTree;
import com.yapu.archive.entity.SysTree;
import com.yapu.archive.entity.SysTreeExample;
import com.yapu.archive.service.itf.ITreeService;
import com.yapu.system.common.BaseAction;
import com.yapu.system.entity.SysAccount;
import com.yapu.system.entity.SysOrg;
import com.yapu.system.service.itf.IAccountService;
import com.yapu.system.service.itf.IOrgService;
import com.yapu.system.util.Constants;

public class TreeAction extends BaseAction {
	
	private static final long serialVersionUID = 4266041911569683983L;
	private ITreeService treeService;
	private IAccountService accountService;
	private IOrgService orgService;
	private String parentid;
	private String treeid;
	private String treename;
	private String treetype;

	
	
	/**
	 * 以树节点形式返回json,ajax方式加载子节点。
	 * @return
	 */
	public String loadTreeData() throws IOException {
		
		PrintWriter out  = this.getPrintWriter();
		String tmpParent = parentid;
		if ("".equals(parentid) || null == parentid || "root".equals(parentid)) {
			tmpParent = "0";
		}
		
		StringBuilder resultStr = new StringBuilder();
		
		//获得帐户组或帐户的节点操作权限
		SysAccount sessionAccount = (SysAccount) this.getHttpSession().getAttribute(Constants.user_in_session);
		
		if (null == sessionAccount) {
			return null;
		}
		
		List<String> treeidList = new ArrayList<String>();
		if (!"admin".equals(sessionAccount.getAccountcode())) {
			//首先得到帐户的档案树节点范围
			List<SysTree> authorityTree = new ArrayList<SysTree>();
			authorityTree = accountService.getTree(sessionAccount.getAccountid());
			
			//判断帐户是否有档案树节点操作
			if (null == authorityTree || authorityTree.size() < 1) {
				//如果未设置帐户自己的树节点范围，读取所属组的范围
				SysOrg org = accountService.getAccountOfOrg(sessionAccount);
				authorityTree = orgService.getTree(org.getOrgid());
				if (null == authorityTree ) {
					resultStr.append("[{\"id\":\"0\",\"text\":\"档案库\",\"iconCls\":\"\",\"state\":\"open\",\"children\":[");
					resultStr.append("]}]");
					out.write(resultStr.toString());
					return null;
				}
			}
			
			if (null != authorityTree && authorityTree.size() > 0) {
				for (SysTree tree : authorityTree) {
					treeidList.add(tree.getTreeid());
				}
			}
		}
		
		
		//获得父节点为nodeId的子节点
		SysTreeExample example = new SysTreeExample();
		example.setOrderByClause("CONVERT(TREENAME USING gbk)");
        example.createCriteria().andParentidEqualTo(tmpParent);
//		if ("admin".equals(sessionAccount.getAccountcode())) {
//			example.createCriteria().andParentidEqualTo(tmpParent);
//		}
//		else {
//			example.createCriteria().andParentidEqualTo(tmpParent).andTreeidIn(treeidList);
//		}
		List<SysTree> treeList = treeService.selectByWhereNotPage(example);
		
		if ("root".equals(parentid)) {
			resultStr.append("[{\"id\":\"0\",\"text\":\"档案库\",\"iconCls\":\"\",\"state\":\"open\",\"children\":[");
			
		}
		else {
			resultStr.append("[");
		}
		
		if(null!=treeList && treeList.size()>0 ){
			for(int i=0;i<treeList.size();i++){
				SysTree temp =(SysTree)treeList.get(i);
				resultStr.append("{");
				if ("0".equals(temp.getParentid())) {
					resultStr.append("\"id\": \""+temp.getTreeid()+"\", \"text\": \""+temp.getTreename()+"\", \"iconCls\": \"icon-book-open\", \"state\": \"closed\"");
				}
				else if ("F".equals(temp.getTreetype())) {
					resultStr.append("\"id\": \""+temp.getTreeid()+"\", \"text\": \""+temp.getTreename()+"\", \"iconCls\": \"\", \"state\": \"closed\"");
				}
				else {
					resultStr.append("\"id\": \""+temp.getTreeid()+"\", \"text\": \""+temp.getTreename()+"\", \"iconCls\": \"icon-page\", \"state\": \"open\"");
				}
				resultStr.append("},");
			}
			resultStr.deleteCharAt(resultStr.length() - 1).append("]");
			if ("root".equals(parentid)) {
				resultStr.append("}]");
			}
			out.write(resultStr.toString());
		}
		else {
			resultStr.append("]");
			if ("root".equals(parentid)) {
				resultStr.append("}]");
			}
			System.out.println(resultStr.toString());
			out.write(resultStr.toString());
		}
		return null;
	}
	
	/**
	 * 读取档案节点树。
	 * @return
	 * @throws IOException 
	 */
	public String getTree() throws IOException {
		PrintWriter out  = this.getPrintWriter();
		
		//获得帐户组或帐户的节点操作权限
		SysAccount sessionAccount = (SysAccount) this.getHttpSession().getAttribute(Constants.user_in_session);
		
		if (null == sessionAccount) {
			return null;
		}
		//创建跟节点
		List list = new ArrayList();
		HashMap map = new HashMap();
		HashMap attrMap = new HashMap();
		attrMap.put("id", "0");
		attrMap.put("rel", "root");
		map.put("attr", attrMap);
		map.put("data", "档案库");
		
		List<String> treeidList = new ArrayList<String>();
		
		if (!"admin".equals(sessionAccount.getAccountcode())) {
			List<SysTree> authorityTree = new ArrayList<SysTree>();
			//首先得到帐户的档案树节点范围
			authorityTree = accountService.getTree(sessionAccount.getAccountid());
			//判断帐户是否有档案树节点操作
			if (null == authorityTree || authorityTree.size() < 1) {
				//如果未设置帐户自己的树节点范围，读取所属组的范围
				SysOrg org = accountService.getAccountOfOrg(sessionAccount);
				authorityTree = orgService.getTree(org.getOrgid());
				//如果没有找到帐户的树节点范围，也没有找到所属组的节点范围。就只返回跟节点。
				if (null == authorityTree ) {
					list.add(map);
					Gson gson = new Gson();
					out.write(gson.toJson(list));
					return null;
				}
			}
			
			if (null != authorityTree && authorityTree.size() > 0) {
				for (SysTree tree : authorityTree) {
					treeidList.add(tree.getTreeid());
				}
			}
		}
		
		List tmpList = getTreeJson("0",treeidList);
		map.put("children", tmpList);
		list.add(map);
		
//		List temp = new ArrayList();
//		HashMap map = new HashMap();
//		map.put("id", "0");
//		map.put("text", "档案节点树");
//		map.put("iconCls", "");
//		map.put("state", "open");
//		List list = getTreeJson("0",treeList);
//		map.put("children", list);
//		temp.add(map);
		Gson gson = new Gson();
		System.out.println(gson.toJson(list));
		out.write(gson.toJson(list));
		return null;
	}
	
	/**  
     * 无限递归获得tree的json字串  
     *   
     * @param parentId  
     *            父权限id 
     * @return  
     */  
    private List getTreeJson(String parentid,List<String> treeidList) {
    	//获得帐户组或帐户的节点操作权限
    	SysAccount sessionAccount = (SysAccount) this.getHttpSession().getAttribute(Constants.user_in_session);
    	//得到节点
		SysTreeExample example = new SysTreeExample();
		if ("admin".equals(sessionAccount.getAccountcode())) {
			example.createCriteria().andParentidEqualTo(parentid);
		}
		else {
			example.createCriteria().andParentidEqualTo(parentid).andTreeidIn(treeidList);
		}
		example.setOrderByClause("CONVERT(TREENAME USING gbk)");
		List<SysTree> trees = treeService.selectByWhereNotPage(example);
		List list = new ArrayList();
		if(null!=trees && trees.size()>0){
			for(int i=0;i<trees.size();i++){
				HashMap attrMap = new HashMap();
				HashMap map = new HashMap();
				SysTree tree =(SysTree)trees.get(i);
				//判断该节点下是否有子节点
				example.clear();
				example.createCriteria().andParentidEqualTo(tree.getTreeid());
				example.setOrderByClause("CONVERT(TREENAME USING gbk)");
				if (treeService.selectByWhereNotPage(example).size() >0) {
					attrMap.put("id", tree.getTreeid());
					attrMap.put("rel", "folder");
					map.put("attr", attrMap);
					map.put("data", tree.getTreename());
					map.put("children", this.getTreeJson(tree.getTreeid(),treeidList));
				}
				else {
					attrMap.put("id", tree.getTreeid());
					attrMap.put("rel", "default");
					map.put("attr", attrMap);
					map.put("data", tree.getTreename());
				}
				list.add(map);
			}
		}
        
        return list;
    }
	
	
	
	public String add() throws IOException {
		PrintWriter out  = this.getPrintWriter();
		
		String result = "failed";
		
		SysTree tree = new SysTree();
		tree.setTreeid(treeid);
		tree.setTreename(treename);
		tree.setTreetype(treetype);
		tree.setParentid(parentid);
		
		SysTree parent = treeService.selectByPrimaryKey(tree.getParentid()) ;
		tree.setTreenode(parent.getTreenode() + "#" + tree.getTreeid().substring(0, 8));
		
		boolean b = treeService.insertTree(tree);
		if (b) {
			result = "success";
		}
		out.write(result);
		return null;
	}
	
	public String delete() throws IOException {
		PrintWriter out  = this.getPrintWriter();
		
		String result = "删除失败，请重新尝试或与管理员联系！";
		if (null == treeid || "".equals(treeid)) {
			out.write(result);
			return null;
		}
		int delNum = treeService.deleteTree(treeid);
		if (delNum > 0 ) {
			result = "删除完毕！";
		}
		out.write(result);
		return null;
	}
	
	public String update() throws IOException {
		PrintWriter out  = this.getPrintWriter();
		
		String result = "更新失败，请重新尝试或与管理员联系！";
		if (null == treeid || "".equals(treeid)) {
			out.write(result);
			return null;
		}
		SysTree tree = treeService.selectByPrimaryKey(treeid);
		if (null == tree) {
			out.write(result);
			return null;
		}
		tree.setTreename(treename);
		int num = treeService.updateTree(tree);
		if (num > 0 ) {
			result = "更新完毕！";
		}
		out.write(result);
		return null;
	}

	public void setTreeService(ITreeService treeService) {
		this.treeService = treeService;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getTreeid() {
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}
	public String getTreename() {
		return treename;
	}
	public void setTreename(String treename) {
		this.treename = treename;
	}
	public String getTreetype() {
		return treetype;
	}
	public void setTreetype(String treetype) {
		this.treetype = treetype;
	}
	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}
	public void setOrgService(IOrgService orgService) {
		this.orgService = orgService;
	}

}
