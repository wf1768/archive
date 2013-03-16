package com.yapu.system.action;

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
import com.yapu.system.common.BaseAction;
import com.yapu.system.entity.SysAccount;
import com.yapu.system.entity.SysConfig;
import com.yapu.system.entity.SysConfigExample;
import com.yapu.system.service.itf.IConfigService;

public class ConfigAction extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	private IConfigService configService;
	
	private SysConfig sysConfig = new SysConfig();
	private String par;
	
	/**
	 * 返回系统属性列表
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
		
		SysConfigExample example = new SysConfigExample();
		List<SysConfig> configList = configService.selectByWhereNotPage(example);
		List<Map> sysCList = new ArrayList<Map>();
		if(configList!=null || configList.size()>0){
			for(SysConfig sysC:configList){
				HashMap<String,Object> map = new HashMap<String,Object>();
				map.put("configid", sysC.getConfigid());
				map.put("configname", sysC.getConfigname());
				map.put("configkey", sysC.getConfigkey());
				map.put("configvalue", sysC.getConfigvalue());
				map.put("configmemo", sysC.getConfigmemo());
				sysCList.add(map);
			}
		}else{
			out.write("error");
			return null;
		}
		Gson gson = new Gson();
		result += "var sysList=" + gson.toJson(sysCList);
		out.write(result);
		
		return null;
	}
	/**
	 * 保存系统属性设置
	 * @return
	 * @throws IOException
	 */
	public String save() throws IOException {
		HttpServletResponse response = getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out  = response.getWriter();
		String result = "error";
		
		Gson gson = new Gson();
		Map<String, List<SysConfig>> configMap = new HashMap<String, List<SysConfig>>();
		try {
			configMap = (Map)gson.fromJson(par, new TypeToken<Map<String, List<SysConfig>>>(){}.getType());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		//处理更新的
		List<SysConfig> updateConfigList = configMap.get("updated");
		if (updateConfigList.size() > 0) {
			//循环保存更新的
			for (SysConfig config : updateConfigList) {
				configService.updateConfig(config);
			}
		}
		out.write(result);
		return null;
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
	public SysConfig getSysConfig() {
		return sysConfig;
	}
	public void setSysConfig(SysConfig sysConfig) {
		this.sysConfig = sysConfig;
	}
	public static void main(String[] args) {
		String str="479c83c1-6715-48db-aad0-fc76697c0a93,e79a4599-d347-46ef-92eb-1b600f0e310c";
		String[] s = str.split(",");
		System.out.println(s[0]);
	}
}
