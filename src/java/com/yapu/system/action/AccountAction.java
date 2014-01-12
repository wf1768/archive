package com.yapu.system.action;

/**
 * 帐户相关操作
 * 2011-07-18
 * @author wangf
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yapu.archive.entity.SysAccountTree;
import com.yapu.archive.entity.SysOrgTree;
import com.yapu.archive.entity.SysTree;
import com.yapu.archive.entity.SysTreeExample;
import com.yapu.archive.service.itf.ITreeService;
import com.yapu.system.common.BaseAction;
import com.yapu.system.entity.SysAccount;
import com.yapu.system.entity.SysConfig;
import com.yapu.system.entity.SysConfigExample;
import com.yapu.system.entity.SysOrg;
import com.yapu.system.service.itf.IAccountService;
import com.yapu.system.service.itf.IConfigService;
import com.yapu.system.service.itf.IOrgService;
import com.yapu.system.util.Constants;
import com.yapu.system.util.MD5;

public class AccountAction extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	
	private IAccountService accountService;
	private IOrgService orgService;
	private IConfigService configService;
	private ITreeService treeService;
	private SysAccount sysAccount = new SysAccount();
	
	private String accountid;
	private String accountcode;
	private String password;
	private String par;
	private String orgid;
	private String parentid;
	private String selectAccounts;
	private String targetorgid;
	
	public String showListAccount() {
		return SUCCESS;
	}
	
	/**
	 * 得到request的帐户组id，返回所属帐户的json
	 * @return
	 */
	public String list() throws IOException {
		//如果得不到帐户组id，返回空字符
		if (null == orgid || "".equals(orgid)) {
			return null;
		}
		PrintWriter out  = this.getPrintWriter();
		//获得父节点为nodeId的账户节点
		SysOrg sysOrg = new SysOrg();
		sysOrg.setOrgid(orgid);
		List<SysAccount> accounts =orgService.getOrgOfAccount(sysOrg);
		List<Map> accountList = new ArrayList<Map>();
		Gson gson = new Gson();
		String result = "var accountList=";
		if(null!=accounts && accounts.size()>0){
			for (SysAccount sysAccount : accounts) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("accountid", sysAccount.getAccountid());
				map.put("accountcode", sysAccount.getAccountcode());
				if(sysAccount.getAccountstate()==1){
					map.put("accountstate", "启用");
				}else{
					map.put("accountstate", "<span style=\"color: red\">已禁用</span>");
				}
				map.put("accountmemo", sysAccount.getAccountmemo());
				accountList.add(map);
			}
		}else {
			 result += gson.toJson(accountList);
		}
		result += gson.toJson(accountList);
		out.write(result);
		return null;
	}
	
	public String accountList() throws IOException {
		PrintWriter out = this.getPrintWriter();
		
		//如果得不到帐户组id，返回空字符
		if (null == orgid || "".equals(orgid)) {
			return "";
		}
		
		//获得父节点为nodeId的账户节点
		SysOrg sysOrg = new SysOrg();
		sysOrg.setOrgid(orgid);
		List<SysAccount> accounts =orgService.getOrgOfAccount(sysOrg);
		StringBuffer sb = new StringBuffer();
		
		Gson gson = new Gson();
		
		sb.append("var rowList = ").append(gson.toJson(accounts));
		out.write(sb.toString());
		return null;
	}
	
	/**
	 * 添加帐户
	 * @param sysAccount
	 * @return
	 */
	public String save() throws IOException {
		PrintWriter out  = this.getPrintWriter();
		String result = "error";
		if(addAccount(sysAccount)){
			result = "succ";
		}
		out.write(result);
		return null;
	}
	private boolean addAccount(SysAccount sysAccount) {
		boolean result = false;
		if (sysAccount != null) {
			sysAccount.setAccountid(UUID.randomUUID().toString());
//			sysAccount.setOrgBaseID(orgid);
			//读取系统配置表，得到设置的默认密码
			SysConfigExample example = new SysConfigExample();
			example.createCriteria().andConfigkeyEqualTo("PASSWORD");
			
			List<SysConfig> configList = configService.selectByWhereNotPage(example);
			
			if (configList.size() == 1) {
				String pass = MD5.encode(configList.get(0).getConfigvalue());
				sysAccount.setPassword(pass);
			}
			sysAccount.setAccountstate(1);//初始状态1为可用
			if(accountService.insertAccount(sysAccount,orgid)){
				result=true;
			}
		}
		return result;
	}
	
	/**
	 * 查找账户
	 * @throws IOException 
	 * */
	public String getSysaccount() throws IOException{
		PrintWriter out  = this.getPrintWriter();
		SysAccount account = accountService.selectByPrimaryKey(accountid);
		if(account!=null){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("accountid", account.getAccountid());
			map.put("accountcode", account.getAccountcode());
			map.put("accountstate", account.getAccountstate());
			map.put("accountmemo", account.getAccountmemo());
			
			Gson gson = new Gson();
			String result = "var account=" + gson.toJson(map);
			out.write(result);
		}else{
			out.write("error");
		}
		return null;
	}
	
	/**
	 * 更新帐户
	 * @param sysAccount
	 * @return
	 * @throws IOException 
	 */
	public String update() throws IOException{
		PrintWriter out  = this.getPrintWriter();
		if(updateAccount(sysAccount)){
			out.write("succ");
		}else{
			out.write("error");
		}
		return null;
	}
	private boolean updateAccount(SysAccount sysAccount) {
		boolean result = false;
		if (sysAccount != null) {
			//如果密码填写了则修改，否则为原密码
//			if(!password.equals("") || password!=null){
//				sysAccount.setPassword(MD5.encode(password));
//			}
			if(password!=null && !password.equals("")){
				sysAccount.setPassword(MD5.encode(password));
			}
//			
//			if (sysAccount.getPassword() != null && !sysAccount.getPassword().equals("")) {
//				sysAccount.setPassword(MD5.encode(password));
//			}
			if(accountService.updateAccount(sysAccount) > 0){
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * 删除帐户
	 * @param accountid
	 * @return
	 */
	public String delete() throws IOException{
		PrintWriter out  = this.getPrintWriter();
		String result = "error";
		String[] accountids = par.split(",");
		for(int i=0;i<accountids.length;i++){
			if(delAccount(accountids[i])){
				result = "succ";
			}
		}
		out.write(result);
		return null;
	}
	public boolean delAccount(String accountid) {
		boolean result = false;
		if (null != accountid && !"".equals(accountid)) {
			int num = accountService.deleteAccount(accountid);
			if (num >0) {
				result = true;
			}
		}
		return result;
	}
	/**
	 * 移动帐户
	 * @return
	 */
	public String moveAccount() throws IOException {
		String result = "failure";
		
		HttpServletResponse response = getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out  = response.getWriter();
		
		if (null != selectAccounts && !"".equals(selectAccounts)) {
			selectAccounts = selectAccounts.substring(0, selectAccounts.length() - 1);
			String[] s = selectAccounts.split(",");
			List<String> idList = new ArrayList<String>();
			
			idList = Arrays.asList(s);
			boolean num = accountService.updateAccountOfOrg(idList, targetorgid);
			if (num) {
				result = "success";
			}
		}
		out.write(result);
		return null;
	}
	
	public String getAccountTree() throws IOException {
		PrintWriter out = this.getPrintWriter();
		SysAccount account = new SysAccount();
		account.setAccountid(accountid);
		List<SysAccountTree> treeList = accountService.getAccountOfTree(account);
		Gson gson = new Gson();
		out.write(gson.toJson(treeList));
		return null;
		
	}
	
	public String updateAccountPassword() throws IOException {
		String result = "保持完毕。";
		PrintWriter out = this.getPrintWriter();
		Gson gson = new Gson();
		HashMap map = gson.fromJson(par, new TypeToken<HashMap<String,String>>(){}.getType());
		
		// 读取session里的登录帐户
		SysAccount account = (SysAccount) this.getHttpSession().getAttribute(
				Constants.user_in_session);
		if (null == account) {
			// TODO 这里因为session过期，以后处理
			return null;
		}
		String oldpassword = map.get("oldpassword").toString();
		String newpassword = map.get("newpassword").toString();
		if (!account.getPassword().equals(MD5.encode(oldpassword))) {
			result = "原密码错误。请重新操作。";
			out.write(result);
			return null;
		}
		
		account.setPassword(MD5.encode(newpassword));
		int num = accountService.updateAccount(account);
		if (num > 0) {
			out.write(result);
			return null;
		}
		else {
			result = "保存失败。请重新操作。";
			out.write(result);
			return null;
		}
	}
	
	/**
	 * 读取档案节点树.全部读取 账户
	 * @author guodh 
	 * @return
	 * @throws IOException 
	 */
	public String loadAccountTreeData() throws IOException {
		PrintWriter out  = this.getPrintWriter();
		
		SysAccount account = new SysAccount();
		account.setAccountid(accountid);
		List<SysAccountTree> treeList = accountService.getAccountOfTree(account);
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
    private String getTreeJson(String parentId,List<SysAccountTree> treeList){
    	String jsonData = "";
    	//得到节点
		SysTreeExample example = new SysTreeExample();
		example.createCriteria().andParentidEqualTo(parentId);
		example.setOrderByClause("CONVERT(treename USING gbk)");
		List<SysTree> trees = treeService.selectByWhereNotPage(example);
		JSONArray jsonArray = new JSONArray();
		for (Iterator iterator1 = trees.iterator(); iterator1.hasNext();){
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
				String children_tree =getTreeJson(tree.getTreeid(), treeList);
				jsonObject.put("children", children_tree);  //
			}else{
				attro.put("id", (new StringBuilder("")).append(tree.getTreeid()).toString());
				attro.put("rel", "default");
				//已经应有的权限--子节点
				if(treeList!=null && treeList.size()>0){
					for(SysAccountTree stree:treeList){
						if(stree.getTreeid().equals(tree.getTreeid())){
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
		System.out.println((new StringBuilder(String.valueOf(jsonData))).append(":::::jsonArray").toString());
		return jsonData;
    }
    

	public String getAccountcode() {
		return accountcode;
	}
	public void setAccountcode(String accountcode) {
		this.accountcode = accountcode;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOrgid() {
		return orgid;
	}
	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}
	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}
	public void setOrgService(IOrgService orgService) {
		this.orgService = orgService;
	}
	public void setConfigService(IConfigService configService) {
		this.configService = configService;
	}
	public String getPar() {
		return par;
	}
	public void setPar(String par) {
		this.par = par;
	}
//	public void setPublicsAccountService(
//			IPublicsAccountService publicsAccountService) {
//		this.publicsAccountService = publicsAccountService;
//	}
	public String getSelectAccounts() {
		return selectAccounts;
	}
	public void setSelectAccounts(String selectAccounts) {
		this.selectAccounts = selectAccounts;
	}
	public String getTargetorgid() {
		return targetorgid;
	}
	public void setTargetorgid(String targetorgid) {
		this.targetorgid = targetorgid;
	}

	public String getAccountid() {
		return accountid;
	}

	public void setAccountid(String accountid) {
		this.accountid = accountid;
	}

	public SysAccount getSysAccount() {
		return sysAccount;
	}

	public void setSysAccount(SysAccount sysAccount) {
		this.sysAccount = sysAccount;
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
	
}
