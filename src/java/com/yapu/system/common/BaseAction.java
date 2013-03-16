package com.yapu.system.common;

import com.opensymphony.xwork2.ActionSupport;
import com.yapu.system.entity.SysAccount;
import com.yapu.system.util.Constants;

import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class BaseAction extends ActionSupport {
//	/**
//	 *
//	 * 根据名字返回service对象
//	 */
//	protected Object getService(String serviceName){
//		ClassPathResource resource = new ClassPathResource("applicationContext.xml");
//		System.out.println(resource.getPath());
//		BeanFactory factory = new XmlBeanFactory(resource);
//		return factory.getBean(serviceName);
//	}
	/**
	 * 
	 * 返回当前request
	 */
	protected HttpServletRequest getRequest(){
		return ServletActionContext.getRequest();
//		 WebContext context = WebContextManager.get(); 
//		return context.getRequest();
	}
	/**
	 * 
	 * 返回当前session
	 */
	protected HttpSession getHttpSession(){
		return getRequest().getSession();
//		 WebContext context = WebContextManager.get(); 		
//		return context.getSession();
	}
	
	/**
	 *  返回当前Response
	 */
	protected HttpServletResponse getResponse(){
		return ServletActionContext.getResponse();
//		 WebContext context = WebContextManager.get(); 
//		return context.getResponse();
	}
	/**
	 * 返回PrintWriter object
	 * @return
	 * @throws IOException
	 */
    protected PrintWriter getPrintWriter() throws IOException {
        HttpServletResponse response = getResponse();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter out  = response.getWriter();
        return out;
    }
    protected PrintWriter getJsonOuter() throws IOException {
        HttpServletResponse response = getResponse();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter out  = response.getWriter();
        return out;
    }

    protected SysAccount getAccount(){
    	//读取session里的登录帐户
		SysAccount account = (SysAccount) this.getHttpSession().getAttribute(Constants.user_in_session);
		if (null == account) {
			//TODO 这里因为session过期，以后处理
			return null;
		}else{
			return account;
		}
    }
    
}

