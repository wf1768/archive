package com.yapu.system.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.google.gson.Gson;
import com.yapu.system.common.BaseAction;
import com.yapu.system.entity.SysAccount;
import com.yapu.system.entity.SysFunction;
import com.yapu.system.entity.SysFunctionExample;
import com.yapu.system.entity.SysOrg;
import com.yapu.system.entity.SysOrgExample;
import com.yapu.system.entity.SysRole;
import com.yapu.system.entity.SysRoleExample;
import com.yapu.system.entity.SysRoleFunction;
import com.yapu.system.service.itf.IFunctionService;
import com.yapu.system.service.itf.IRoleService;

public class RoleAction extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	private IRoleService roleService;
	private IFunctionService functionService;
	
	private String par;
	private String roleid;
	private String functionids;
	private String functionid;
	private SysRole sysRole = new SysRole();
	/**
	 * 返回角色的json
	 * @return
	 */
	public String list() throws IOException {
		
		PrintWriter out = this.getPrintWriter();
		SysAccount account = this.getAccount();
		if(account==null){
			out.write("error");
			return null;
		}
		String result = "var account='" + account.getAccountcode() + "';";
		SysRoleExample example = new SysRoleExample();
		List<SysRole> rolesList = roleService.selectByWhereNotPage(example);
		List<Map> roleList = new ArrayList<Map>();
		if(null!=rolesList && rolesList.size()>0){
			for (SysRole sysRole : rolesList) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("roleid",sysRole.getRoleid());
				map.put("rolename",sysRole.getRolename());
				map.put("rolememo",sysRole.getRolememo());
				roleList.add(map);
			}
		}
		else {
			out.write("error");
			return null;
		}
		Gson gson = new Gson();
		result += "var roleList=" + gson.toJson(roleList);
		out.write(result);
		return null;
	}

	public String save() throws IOException {
		PrintWriter out  = this.getPrintWriter();
		String result = "error";
		if(addRole(sysRole)){
			result = "succ";
		}
		out.write(result);
		return null;
	}
	/**
	 * 添加角色
	 * @param SysRole
	 * @return boolean
	 */
	public boolean addRole(SysRole role) {
		boolean result = false;
		if (role != null) {
			role.setRoleid(UUID.randomUUID().toString());
			if(roleService.insertRole(role)){
				result=true;
			}
		}
		return result;
	}
	/**
	 * 更新
	 * @param SysRole
	 * @return
	 * @throws IOException 
	 */
	public String update() throws IOException{
		PrintWriter out  = this.getPrintWriter();
		String result = "error";
		if(updateRole(sysRole)){
			result = "succ";
		}
		out.write(result);
		return null;
	}
	public boolean updateRole(SysRole role) {
		boolean result = false;
		if (role != null) {
			if(roleService.updateRole(role) > 0){
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * 根据roleid获得对象 
	 * */
	public String getRole() throws IOException{
		PrintWriter out  = this.getPrintWriter();
		SysRole role = roleService.selectByPrimaryKey(roleid);
		if(role!=null){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("roleid",role.getRoleid());
			map.put("rolename",role.getRolename());
			map.put("rolememo",role.getRolememo());
			
			Gson gson = new Gson();
			String result = "var role=" + gson.toJson(map);
			out.write(result);
		}else{
			out.write("error");
		}
		return null;
	}
	/**
	 * 删除
	 * @param 
	 * @return
	 * @throws IOException 
	 */
	public String delete() throws IOException{
		PrintWriter out  = this.getPrintWriter();
		String result = "error";
		String[] roleids = par.split(",");
		for(int i=0;i<roleids.length;i++){
			if(delRole(roleids[i])){
				result = "succ";
			}
		}
		out.write(result);
		return null;
	}
	public boolean delRole(String roleid) {
		boolean result = false;
		if (null != roleid && !"".equals(roleid)) {
			int num = roleService.deleteRole(roleid);
			if (num >0) {
				result = true;
			}
		}
		return result;
	}
	
	
	/**
	 * 角色赋功能权树，带checkbox。
	 * @return
	 * @throws IOException 
	 */
	public String loadRoleFunctionTree() throws IOException {
		
		PrintWriter out  = this.getPrintWriter();
		
		SysRole role = new SysRole();
		role.setRoleid(roleid);
		
		//得到角色id对应的功能
		List<SysFunction> roleFunctionList = roleService.getRoleOfFunction(role);
		
//		StringBuilder resultStr = new StringBuilder();
//		//创建根节点
//		resultStr.append("[{\"id\":\"0\",\"text\":\"系统功能\",\"iconCls\":\"icon_role_go\",\"state\":\"open\",\"children\":");
//		String json = getTreeJson("0",roleFunctionList);
//		resultStr.append(json).append("}]");
//		out.write(resultStr.toString());
		
		String jsonTree = getTree(functionid, roleFunctionList);
		out.write(jsonTree);
		return null;
	}
	
	/**  
     * 无限递归获得tree的json字串  
     *   
     * @param parentId  
     *            父权限id 
     * @return  
     */  
	public String getTree(String id,List<SysFunction> roleFunctionList){
		String jsonData = "";
		//得到节点
		SysFunctionExample example = new SysFunctionExample();
		example.createCriteria().andFunparentEqualTo(id);
		List<SysFunction> functions = functionService.selectByWhereNotPage(example);
		
		if (id.equals("0")){ //初始时只加载根节点
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject = new JSONObject();
			
			for (Iterator iterator = functions.iterator(); iterator.hasNext();){
				SysFunction fun = (SysFunction)iterator.next();
					JSONObject attro = new JSONObject();
					attro.put("id", (new StringBuilder("")).append(fun.getFunctionid()).toString());
					attro.put("rel", "default");
					jsonObject.put("data", fun.getFunchinesename());
					jsonObject.put("attr", attro);
					jsonObject.put("state", "closed");
					jsonArray.add(jsonObject);
			}
			jsonData = jsonArray.toString();
		} else{
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject = new JSONObject();
			for (Iterator iterator1 = functions.iterator(); iterator1.hasNext(); jsonArray.add(jsonObject)){
				SysFunction fun = (SysFunction)iterator1.next();
				JSONObject attro = new JSONObject();
				attro.put("id", (new StringBuilder("")).append(fun.getFunctionid()).toString());
				attro.put("rel", "default");
				//已经应有的权限
				if(roleFunctionList!=null && roleFunctionList.size()>0){
					for(SysFunction function:roleFunctionList){
						if(function.getFunctionid().equals(fun.getFunctionid())){
							attro.put("CLASS", "jstree-checked");//class不能为小写，可能是关键词的原因，搞了半天
						}
					}
				}
				jsonObject.put("data", fun.getFunchinesename());
				jsonObject.put("attr", attro);
				
				SysFunctionExample example1 = new SysFunctionExample();
				example1.createCriteria().andFunparentEqualTo(fun.getFunctionid());
				List<SysFunction> funs = functionService.selectByWhereNotPage(example1);
				if (funs != null && funs.size() >= 1)
					jsonObject.put("state", "closed");
			}

			jsonData = jsonArray.toString();
		}
		System.out.println((new StringBuilder(String.valueOf(jsonData))).append(":::::jsonArray").toString());
		return jsonData;
	}
    private String getTreeJson(String parentId,List<SysFunction> roleFunctionList)
    {
    	String str ="";
    	//得到节点
		SysFunctionExample example = new SysFunctionExample();
		example.createCriteria().andFunparentEqualTo(parentId);
		List<SysFunction> functions = functionService.selectByWhereNotPage(example);
		
		if(null!=functions && functions.size()>0){
			str = "[";
			for(int i=0;i<functions.size();i++){
				SysFunction function =(SysFunction)functions.get(i);
				//判断该节点下是否有子节点
				example.clear();
				example.createCriteria().andFunparentEqualTo(function.getFunctionid());
				if (functionService.selectByWhereNotPage(example).size() >0) {
					str += "{\"id\":\"" + function.getFunctionid() + "\",\"text\":\"" + function.getFunchinesename() + "\",\"iconCls\":\"" + function.getFunicon() + "\", \"state\": \"closed\"";
//					for (int j=0;j<roleFunctionList.size();j++) {
//						if (roleFunctionList.get(j).getFunctionid().equals(function.getFunctionid())) {
//							str += ",\"checked\":true";
//						}
//					}
					str += ",\"children\":";
	                str += this.getTreeJson(function.getFunctionid(),roleFunctionList);
	                str += "}";
				}
				else {
					str += "{\"id\":\"" + function.getFunctionid() + "\",\"text\":\"" + function.getFunchinesename() + "\",\"iconCls\":\"" + function.getFunicon() + "\"";
					if (null != roleFunctionList) {
						for (int j=0;j<roleFunctionList.size();j++) {
							if (roleFunctionList.get(j).getFunctionid().equals(function.getFunctionid())) {
								str += ",\"checked\":true";
							}
						}
					}
					str += "}";
				}
				if (i < functions.size() - 1)
	            {
	                str += ",";
	            }   
			}
			str += " ]";
//			String str = resultStr.substring(0, resultStr.length() - 1);
			
		}
        
        return str;   
    }
    
	
	/**
	 * 保存角色对应的功能
	 * @return
	 */
	public String saveRoleFun() throws IOException {
		PrintWriter out  = this.getPrintWriter();
		String result =	"failure";
		if (null != roleid && !"".equals(roleid)) {
			System.out.println(functionids);
			boolean boo = roleService.insertRoleOfFunction(roleid, functionids);
			if (boo) {
				result = "succ";
			}
		}
		out.write(result);
		return null;
	}
	
	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}
	public void setFunctionService(IFunctionService functionService) {
		this.functionService = functionService;
	}
	public String getPar() {
		return par;
	}
	public void setPar(String par) {
		this.par = par;
	}
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public String getFunctionids() {
		return functionids;
	}
	public void setFunctionids(String functionids) {
		this.functionids = functionids;
	}

	public SysRole getSysRole() {
		return sysRole;
	}

	public void setSysRole(SysRole sysRole) {
		this.sysRole = sysRole;
	}

	public String getFunctionid() {
		return functionid;
	}

	public void setFunctionid(String functionid) {
		this.functionid = functionid;
	}
	
}
