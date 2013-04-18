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
import java.util.Vector;

public class BaseAction extends ActionSupport {
	
	/* 每页的记录数 */
	//public int intPageSize = 15;
	public int intPageSize = 10;

	/* 总页数 */
	public int intPageCount = 1;

	/* 总记录数 */
	public int intRowCount = 0;

	/* 当前的页数 */
	public int intPage = 0;
	
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
    
    
    /**
	 * 取得分页字符串
	 * 
	 * @param totalRow
	 *            总页数
	 * @param intPage
	 *            当前页数
	 * @param url
	 *            提交处理路径
	 * @param param
	 *            参数信息
	 * @return 取得分页信息
	 */
	public String getPageInfoStr(int totalRow, int intPage, String url,
			String param) {
		// 取得分页信息
		Vector pageInfo = this.getPageInfo(totalRow, intPage);
		// 取得分页字符串
		return this.getPageInfoStr(pageInfo, intPage, url, param);
	}
	/**
	 * 说明：取得分页信息（个部分通用）
	 * 
	 * @param totalRow
	 *            总的记录数
	 * @param intPage
	 *            当前页数
	 * @return Vector 分页信息
	 */
	public Vector getPageInfo(int totalRow, int intPage) {
		Vector pageInfo = new Vector();
		// 总记录数
		intRowCount = totalRow;
		// 总页数
		intPageCount = (intRowCount + intPageSize - 1) / intPageSize;
		// 调整当前页数合法
		if (intPage > intPageCount)
			intPage = intPageCount;
		if (intPage < 1)
			intPage = 1;
		// 调整上、下页数
		int prePage = 1;
		int nextPage = intPageCount;
		if (intPage > 1)
			prePage = intPage - 1;
		if (intPage < intPageCount)
			nextPage = intPage + 1;
		// 分页信息存于向量中
		pageInfo.add(new Integer(intRowCount));
		pageInfo.add(new Integer(intPageCount));
		pageInfo.add(new Integer(intPage));
		pageInfo.add(new Integer(prePage));
		pageInfo.add(new Integer(nextPage));
		return pageInfo;
	}
	/**
	 * 取得分页字符串
	 * ************************************************************************
	 * 分页信息必须在名字为“form1”的form中
	 * 当点击时执行checkForm(command)函数(command=turnPage),执行必要的页面处理 页号变量为＝intPage
	 * ************************************************************************
	 * 
	 * @param pageInfo
	 *            分页向量
	 * @param intPage
	 *            页号
	 * @return 取得分页信息
	 */

	public String getPageInfoStr(Vector pageInfo, int intPage, String url,
			String param) {
		String pref1 = "&";
		String pref;
		// 等于-1是表示没有出现过"?"
		if (url.indexOf("?") > -1) { // 当前url含有参数
			pref = "&";
		} else {
			pref = "?";
		}
		if (intPage == 0) {
			intPage = 1;
		}
		// pageStr存放分页信息
		StringBuffer pageStr = new StringBuffer();
		// 分类信息全部存在
		if (pageInfo.size() == 5) {
			pageStr.append("共 " + pageInfo.elementAt(0).toString() + " 条记录");
			pageStr.append(" &nbsp;&nbsp;&nbsp;&nbsp;共 " + pageInfo.elementAt(1).toString() + " 页");
			pageStr.append(" &nbsp;&nbsp;&nbsp;&nbsp;当前第 " + (intPage) + " 页");
			int tal = Integer.parseInt(pageInfo.elementAt(0).toString());
			int cur = Integer.parseInt(pageInfo.elementAt(2).toString());
			int pre = Integer.parseInt(pageInfo.elementAt(3).toString());
			int nex = Integer.parseInt(pageInfo.elementAt(4).toString());
			// 当审批等操作返回时会因为第二页最后一条记录消失而无法返回第一页。
			// 三种情况需出现"上一页",第三种情况比较特殊,比如删除了一条记录后返回第二页,但是第二页已经没有了数据,此时也要显示上一页的链接,否则就返回不到上一页了
			if (((pre == 0 || (pre >= cur)) && ((intPage - 1)
					* this.intPageSize + 1 <= tal))
					|| pageInfo.elementAt(0).toString().equals("0")) {
				pageStr.append(" &nbsp;&nbsp;&nbsp;&nbsp;上一页");
			} else {
				pageStr
						.append(" <a href=\"#this\" onclick=\"javaScript:document.getElementById('intPage').value='"
								+ pre + "';goPage();\">&nbsp;&nbsp;&nbsp;&nbsp;上一页</a>");
			}

			if (nex == 0 || (cur >= nex)) {
				pageStr.append(" &nbsp;&nbsp;&nbsp;&nbsp;下一页");
			} else {
				pageStr
						.append(" <a href=\"#this\" onclick=\"javaScript:document.getElementById('intPage').value='"
								+ nex + "';goPage();\">&nbsp;&nbsp;&nbsp;&nbsp;下一页</a>");
			}

			int totalPage = Integer.parseInt(pageInfo.elementAt(1).toString());

			// 超过1000页,则显示为输入矿的值
			if (totalPage < 1000) {
				pageStr
						.append(" &nbsp;&nbsp;&nbsp;&nbsp;跳到<select id='intPage' name='intPage' onChange=\"goPage()\">");

				for (int i = 1; i <= totalPage; i++) {
					String se = (intPage == i) ? " selected " : "";
					pageStr.append("<option value='" + i + "'" + se + ">" + i
							+ "</option>");
				}
				if (totalPage == 0) {
					pageStr.append("<option value='1'>1</option>");
				}
				pageStr.append("</select>");
			} else {
				pageStr
						.append(" &nbsp;&nbsp;&nbsp;&nbsp;跳到<input id='intPage' type=\"text\" size=4 value='"+intPage+"' class='text_white' name='intPage'>");
				pageStr.append(" <a href=\"#this\" onclick=\"javaScript:goPage()\"> GO </a>");
			}

			//
			pageStr.append("<script language=\"javascript\">\n");
			pageStr.append("function goPage()\n{");
			pageStr.append(" var obj=document.getElementById('intPage').value;");
			pageStr
					.append("if(isNaN(obj) || parseInt(obj)<1){ alert('请输入正确的页码,必须为正整数!');document.all.intPage.focus();return;}");
			pageStr.append(" if(parseInt(obj)>parseInt(" + totalPage
					+ ")){document.getElementById('intPage').value=" + totalPage + ";}");

			pageStr.append("document.forms[0].action=\"" + url + pref + param
					+ pref1 + "intPage=" + "\" + document.getElementById('intPage').value;"
					+ "document.forms[0].submit();}\n");
			pageStr.append("</script>");
		} else {
			pageStr.append("&nbsp;");
		}
		return pageStr.toString();
	}
	public int getPageCount(){
		intPageCount = (intRowCount + intPageSize - 1) / intPageSize;
		return intPageCount;
	}
	public int getStartRec(int intPage) {
        int iStartRec = 0;
        iStartRec = (intPage - 1) * intPageSize;
        return iStartRec;
    }
	public int getIntPageSize() {
		return intPageSize;
	}
	public void setIntPageSize(int intPageSize) {
		this.intPageSize = intPageSize;
	}
	public int getIntPageCount() {
		return intPageCount;
	}
	public void setIntPageCount(int intPageCount) {
		this.intPageCount = intPageCount;
	}
	public int getIntRowCount() {
		return intRowCount;
	}
	public void setIntRowCount(int intRowCount) {
		this.intRowCount = intRowCount;
	}
	public int getIntPage() {
		return intPage;
	}
	public void setIntPage(int intPage) {
		this.intPage = intPage;
	}
	
}

