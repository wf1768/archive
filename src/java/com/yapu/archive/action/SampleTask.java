package com.yapu.archive.action;

import java.io.File;
import java.util.Calendar;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.ActionSupport;

public class SampleTask extends TimerTask {

	private ServletContext context;
	private static boolean isRunning = false; //是否在运行
	private static final int C_SCHEDULE_HOUR = 10; //时间点

	private IndexerAction indexA;
	
	public SampleTask(ServletContext context) {
		this.context = context;
		ApplicationContext contexts = new ClassPathXmlApplicationContext("applicationContext.xml");
		this.indexA = (IndexerAction) contexts.getBean("indexerAction");
	}

	public void run() {
    	Calendar cal = Calendar.getInstance(); 
    	if (!isRunning) { 
	    	if (C_SCHEDULE_HOUR == cal.get(Calendar.HOUR_OF_DAY)) { 
		    	isRunning = true; 
		    	context.log("开始执行指定任务");
		    	//TODO 添加自定义的详细任务，以下只是示例
		    	try {
		    		//创建数据库索引
			    	indexA.createIndexerOnCREATE();
		    		//创建电子全文索引
					indexA.createFilesIndexer();
				} catch (Exception e) {
					e.printStackTrace();
				}
		    	
		    	isRunning = false;
		    	context.log("指定任务执行结束"); 
		    } 
    	} else {
    		context.log("上一次任务执行还未结束");
    	}
    }

	public static void main(String[] args) {
//		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//		IndexerAction indexA = (IndexerAction) context.getBean("indexerAction");
//		indexA.createIndexerOnCREATE();
		try {
//			indexA.createFilesIndexer();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		String temp = "/";
//		String fileIndexDir = ServletActionContext.getServletContext().getRealPath(temp)+ File.separator;
//		
		
		System.out.println(System.getProperty("user.dir"));
	}
	
} 

