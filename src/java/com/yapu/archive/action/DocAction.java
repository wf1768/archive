package com.yapu.archive.action;

import DBstep.iMsgServer2000;
import com.google.gson.Gson;
import com.yapu.archive.entity.DynamicExample;
import com.yapu.archive.entity.SysAccountTree;
import com.yapu.archive.entity.SysDoc;
import com.yapu.archive.entity.SysDocExample;
import com.yapu.archive.entity.SysDocserver;
import com.yapu.archive.entity.SysDocserverExample;
import com.yapu.archive.entity.SysOrgTree;
import com.yapu.archive.entity.SysTable;
import com.yapu.archive.service.itf.IDocService;
import com.yapu.archive.service.itf.IDocserverService;
import com.yapu.archive.service.itf.IDynamicService;
import com.yapu.archive.service.itf.ITableService;
import com.yapu.archive.service.itf.ITreeService;
import com.yapu.archive.vo.UploadVo;
import com.yapu.system.common.BaseAction;
import com.yapu.system.entity.SysAccount;
import com.yapu.system.entity.SysOrg;
import com.yapu.system.entity.SysRole;
import com.yapu.system.service.itf.IAccountService;
import com.yapu.system.service.itf.IOrgService;
import com.yapu.system.util.CommonUtils;
import com.yapu.system.util.Constants;
import com.yapu.system.util.Coverter;
import com.yapu.system.util.FtpUtil;
import org.apache.struts2.ServletActionContext;
import org.springframework.dao.support.DaoSupport;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * .
 * User: wangf
 * Date: 12-1-13
 * Time: 下午6:52
 */
public class DocAction extends BaseAction{

    private File archive;
    private String archiveFileName;
    private static final int BUFFER_SIZE = 2 * 1024;
    
    private static final String FILESCAN = "filescan";
    private static final String FILEDOWN = "filedown";
    private static final String FILEPRINT = "fileprint";
    
    private IDocserverService docserverService;
    private IDocService docService;
    private String docId;

    private SysDoc doc;
    private SysDocserver docServer;
    private IDynamicService dynamicService;
    private ITableService tableService;
    private IAccountService accountService;
    private IOrgService orgService;
    
    private String tableid;
    private String selectRowid;
    private int chunks;
    private String name;
    private int chunk;
    private File file;
    private String savePath;


    private String docName;
    private String contentType;
    //fileid treeid 为单个档案挂接上传文件时用到
    private String fileid;
    private String treeid;



    //未挂接的电子全文，删除用
    private String nodocid;

    public String getNodocid() {
        return nodocid;
    }

    public void setNodocid(String nodocid) {
        this.nodocid = nodocid;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    /**
     * 打开电子全文页签
     * @return
     */
    public String showDocListTab() {
        return SUCCESS;
    }

    public String list() throws IOException {
        PrintWriter out = this.getPrintWriter();
        SysDocExample example = new SysDocExample();
    	List<SysDoc> docList = docService.selectByWhereNotPage(example);
    	Gson gson = new Gson();
    	out.write(gson.toJson(docList));
    	return null;
    }
    
    /**
     * 根据选择的档案条目读取所属电子全文数据
     * @return
     * @throws IOException
     */
    public String listLinkDoc() throws IOException {
    	PrintWriter out = this.getPrintWriter();
    	//要检查该用户是否有查看的权限
    	String result = "";
    	if(isFieldscan()){
	    	SysDocExample example = new SysDocExample();
	        SysDocExample.Criteria criteria = example.createCriteria();
	        criteria.andFileidEqualTo(selectRowid);
	        criteria.andTableidEqualTo(tableid);
	     	List<SysDoc> docList = docService.selectByWhereNotPage(example);
	     	Gson gson = new Gson();
	     	result = "var isNotAuth = '1';";
	     	result += "var docList = "+gson.toJson(docList);
    	}else{
    		result = "var isNotAuth = '0';"; //没有权限
    	}
    	out.write(result);
     	return null;
    }
    
    /**
     * 是否有（查看、下载、打印）权限
     * @param fileType :filescan(查看) filedown(下载) fileprint(打印)
     * @return
     * */
    public int isAuthority(String fileType){
    	int filescan = 0;
    	int filedown = 0;
    	int fileprint = 0;
    	SysAccount account = super.getAccount();
		//先查看账户本身是否有权限
		List<SysAccountTree> accountTreeList =  accountService.getAccountOfTree(account.getAccountid(), treeid);
		if(accountTreeList.size() >0 && accountTreeList != null){
			SysAccountTree accountTree = accountTreeList.get(0);
			filescan = accountTree.getFilescan();
			filedown = accountTree.getFiledown();
			fileprint = accountTree.getFileprint();
		}else{
			//否则查看该账户的所在组
			SysOrg sysOrg = accountService.getAccountOfOrg(account);
			if(sysOrg!=null){
			 	List<SysOrgTree> orgTreeList = orgService.getOrgOfTree(sysOrg.getOrgid(), treeid);
			 	if(orgTreeList.size() >0 && orgTreeList != null){
			 		SysOrgTree orgTree = orgTreeList.get(0);
			 		filescan = orgTree.getFilescan();
					filedown = orgTree.getFiledown();
					fileprint = orgTree.getFileprint();
			 	}
			}
		}
		if(fileType.equals("filescan")){
			return filescan;
		}else if(fileType.equals("filedown")){
			return filedown;
		}else{
			return fileprint;
		}
    }
    /**
     * 是否有查看权限
     * @return
     * */
    public boolean isFieldscan(){
    	if(isAuthority(FILESCAN)==1){
    		return true;
    	}else{
    		return false;
    	}
    }
    /**
     * 是否有下载权限
     * */
    public boolean isFielddown(){
    	if(isAuthority(FILEDOWN)==1){
    		return true;
    	}else{
    		return false;
    	}
    }
    /**
     * 是否有打印权限
     * */
    public boolean isFieldprint(){
    	if(isAuthority(FILEPRINT)==1){
    		return true;
    	}else{
    		return false;
    	}
    }
    /**
     * 得到当前帐户上传的未挂接的电子全文
     * @return
     * @throws IOException
     */
    public String listNoLinkDocAsAccount() throws IOException {
    	PrintWriter out = this.getPrintWriter();
    	//得到当前登录帐户
    	SysAccount sessionAccount = (SysAccount) this.getHttpSession().getAttribute(Constants.user_in_session);
    	if (null == sessionAccount) {
    		out.write("error");
    		return null;
    	}
        SysDocExample example = new SysDocExample();
        SysDocExample.Criteria criteria = example.createCriteria();
        criteria.andCreaterEqualTo(sessionAccount.getAccountcode());
        criteria.andFileidEqualTo("");
        criteria.andTableidEqualTo("");
        SysDocExample.Criteria criteria1 = example.createCriteria();
        criteria1.andFileidIsNull();
        criteria1.andTableidIsNull();
        example.or(criteria1);
    	List<SysDoc> docList = docService.selectByWhereNotPage(example);
    	Gson gson = new Gson();
    	out.write(gson.toJson(docList));
    	return null;
    }
    public String upload(){
        //得到当前登录帐户
    	System.out.println("上传Start：----------------------------");
        SysAccount sessionAccount = (SysAccount) this.getHttpSession().getAttribute(Constants.user_in_session);
        if (null == sessionAccount) {
            return null;
        }
        String contextPath = ServletActionContext.getServletContext().getRealPath(this.getSavePath())+ File.separator;
        String dstPath =  contextPath+ this.getName();
        File dstFile = new File(dstPath);
        // 文件已存在（上传了同名的文件）
        if (chunk == 0 && dstFile.exists()) {
            dstFile.delete();
            dstFile = new File(dstPath);
        }
        cat(this.file, dstFile);
        if (chunk == chunks - 1) {   // 完成一整个文件;
        	//声明一个标识，是否是单个文件挂接。如果fileid有值，应该把档案条目的isdos设置为1
        	boolean isdoc = false;
        	SysDocserverExample example = new SysDocserverExample();
            example.createCriteria().andServerstateEqualTo(1);
            List<SysDocserver> docserverList = docserverService.selectByWhereNotPage(example);
            SysDocserver docserver = docserverList.get(0);
            
            String docExt = "";//扩展名
            String oldName = dstFile.getName();
            String docId = UUID.randomUUID().toString();

            if (oldName.lastIndexOf(".") >= 0) {
                docExt = oldName.substring(oldName.lastIndexOf("."));
            }
            String newName = docId+docExt;
//            String newName = oldName;
            File newFile =new File(contextPath+newName);
            dstFile.renameTo(newFile);
            SysDoc doc = new SysDoc();
            doc.setDocid(docId);
            doc.setDocnewname(newName);
            doc.setDoclength(CommonUtils.formatFileSize(newFile.length()));
            doc.setDocoldname(oldName);
            doc.setDoctype("0");
            doc.setDocext(docExt.substring(1).toUpperCase());
            doc.setCreater(sessionAccount.getAccountcode());
            doc.setCreatetime(CommonUtils.getTimeStamp());
            if (null != this.getFileid() && !"".equals(this.getFileid())) {
            	doc.setFileid(this.getFileid());
            	isdoc = true;
            }
            else {
            	doc.setFileid("");
            }
            
            if (null != this.getTableid() && !"".equals(this.getTableid())) {
            	doc.setTableid(this.getTableid());
            }
            else {
            	doc.setTableid("");
            }
            
            if (null != this.getTreeid() && !"".equals(this.getTreeid())) {
            	doc.setTreeid(this.getTreeid());
            }
            else {
            	doc.setTreeid("");
            }
            
            
            doc.setParentid(docserver.getDocserverid());
            doc.setLocked("0");
            doc.setMread("1");
            doc.setMwrite("1");
            doc.setHidden("1");
            //TODO 这里要跟阿佘对一下doc的上传字段怎么写。
            
            if ("FTP".equals(docserver.getServertype())) {
                try {
					FtpUtil util = new FtpUtil();
					util.connect(docserver.getServerip(),
					        docserver.getServerport(),
					        docserver.getFtpuser(),
					        docserver.getFtppassword(),
					        docserver.getServerpath());
					FileInputStream s = new FileInputStream(newFile);
					util.uploadFile(s, newName);
					util.closeServer();
				} catch (Exception e) {
					e.printStackTrace();
				}
                //ftp上传完成，删除临时文件
                deleteFile(newFile.getPath());
            } else if ("LOCAL".equals(docserver.getServertype())){
                String serverPath = docserver.getServerpath();
                String savePath = docserver.getServerpath();
                if (null == serverPath || "".equals(serverPath)) {
                        return null;
                }
                else {
                    if (!serverPath.substring(serverPath.length()-1,serverPath.length()).equals("/")) {
                        savePath += "/";
                    }
                }
                System.out.println("上传文件路径+Name=："+savePath+newName);
                newFile.renameTo(new File(savePath + newName));
                System.out.println("上传文件路径+Name=："+savePath+newName+"上传文件结束，upload file over");
                //删除临时文件.renameTO 的同时，已经删除了，这里再删除一次，避免
                deleteFile(newFile.getPath());
            }
            doc.setDocpath( docserver.getServerpath() + newName);
            doc.setDocpath(newName);
            doc.setDocserverid(docserver.getDocserverid());
            docService.insertDoc(doc);
            //把档案条目的isdoc字段设置
            if (isdoc) {
            	SysTable table = tableService.selectByPrimaryKey(doc.getTableid());
            	String sql = "update " + table.getTablename() + " set isdoc = 1 where id='" + this.getFileid() + "'";
            	List<String> sqlList = new ArrayList<String>();
            	sqlList.add(sql);
            	dynamicService.update(sqlList);
            }
        }
        System.out.println("END:---------------------------------");
        return null;
    }
    /**
     * 删除电子全文
     * @return
     * @throws Exception
     */
    public String delete() throws Exception {
    	PrintWriter out = this.getPrintWriter();
    	String result = "success";
    	SysDoc doc = docService.selectByPrimaryKey(this.getDocId());
    	SysTable table = tableService.selectByPrimaryKey(doc.getTableid());
        
    	//修改档案全文标识
    	//检查档案下是否还有全文
    	SysDocExample sde = new SysDocExample();
    	SysDocExample.Criteria sdec = sde.createCriteria();
    	sdec.andFileidEqualTo(doc.getFileid());
    	List<SysDoc> docList = docService.selectByWhereNotPage(sde);
    	boolean b = true;
    	if (docList.size() == 1) {
    		String sql = "update " + table.getTablename() + " set isdoc=0 where id='" + doc.getFileid() + "'";
    		List<String> sqlList = new ArrayList<String>();
    		sqlList.add(sql);
        	b = dynamicService.update(sqlList);
    	}
    	
    	if (b) {
    		int num = docService.deleteDoc(doc.getDocid());
    	}
        else {
        	result = "error";
        	out.write(result);
        }
    	out.write(result);
        return null;
    }

    /**
     * 删除未挂接的电子文件
     * @return
     */
    public String deleteNo() throws IOException {
        PrintWriter out = this.getPrintWriter();
        String result = "success";

        //获取id参数
        String[] idArray = nodocid.split(",");

        List idList = Arrays.asList(idArray);

        int num = docService.deleteDoc(idList);

//        for (String docid :idArray) {
//            docService.deleteDoc(docid);
//        }


        if (num <= 0) {
            result = "error";
            out.write(result);
            return null;
        }

        out.write(result);
        return null;
    }

    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String sPath) {
        boolean flag = false;
        file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }


    public String read() throws Exception {
        String contextPath = ServletActionContext.getServletContext().getRealPath(this.getSavePath())+ File.separator;
        SysDoc doc = docService.selectByPrimaryKey(this.getDocId());
        String filename = doc.getDocnewname();
        File swfFile = null;
        if(filename.toLowerCase().endsWith(".pdf")) {
            swfFile = new File(contextPath+filename+".swf");
            if(!swfFile.exists()) swfFile =  Coverter.PdfToSwf(new File(doc.getDocpath()));
        }else if(CommonUtils.isPdfPrintType(filename)){
            swfFile = new File(contextPath+filename+".pdf.swf");
            if(!swfFile.exists()) {
                File pdf = Coverter.toPdf(new File(doc.getDocpath()));
                swfFile = Coverter.PdfToSwf(pdf);
            }
        }
        outSwf(swfFile);
        return null;
    }
    public String iwebRead() throws Exception{
        SysDoc doc = docService.selectByPrimaryKey(this.getDocId());
        iMsgServer2000 MsgObj = new iMsgServer2000();
        MsgObj.Load(this.getRequest());
        if (MsgObj.GetMsgByName("DBSTEP").equalsIgnoreCase("DBSTEP")) {         //判断是否是合法的信息包，或者数据包信息是否完整
            String mRecordID=MsgObj.GetMsgByName("RECORDID");		//取得文档编号
            String mOption = MsgObj.GetMsgByName("OPTION");                              //取得操作信息
            if (mOption.equalsIgnoreCase("LOADFILE")) {                           //下面的代码为打开服务器数据库里的文件
               MsgObj.MsgTextClear();                                              //清除文本信息
               if (MsgObj.MsgFileLoad(doc.getDocpath())){			            //从文件夹调入文档
                    MsgObj.SetMsgByName("STATUS", "打开成功!");                       //设置状态信息
                    MsgObj.MsgError("");                                              //清除错误信息
                }
                else {
                    MsgObj.MsgError("打开失败!");                                     //设置错误信息
                }
            }
        }
        MsgObj.Send(this.getResponse());                                                    //8.1.0.2新版后台类新增的功能接口，返回信息包数据
        return null;
    }
    private void outSwf(File file)throws Exception{
        this.getResponse().setContentType("application/x-shockwave-flash");
        OutputStream os = this.getResponse().getOutputStream(); // 页面输出流，jsp/servlet都可以
        InputStream is = new FileInputStream(file); // 文件输入流
        byte[] bs = new byte[BUFFER_SIZE];  // 读取缓冲区
        int len;
        while((len=is.read(bs))!=-1){ // 循环读取
            os.write(bs,0,len); // 写入到输出流
        }
        is.close();  // 关闭
        os.close(); // 关闭
    }
    public String uploadFile() throws IOException {
        String extName = "";//扩展名
        String newFileName = "";//新文件名
//        String nowTime = new SimpleDateFormat("yyyymmddHHmmss").format(new Date());//当前时间
//        String savePath = ServletActionContext.getRequest().getRealPath("");
//        savePath = savePath + "/uploads/";

        HttpServletResponse response = getResponse();
        response.setCharacterEncoding("utf-8");

        //获取扩展名
        if (archiveFileName.lastIndexOf(".") >= 0) {
            extName = archiveFileName.substring(archiveFileName.lastIndexOf("."));
        }
        //生成新文件名
        String uuid = UUID.randomUUID().toString();
        newFileName = uuid + extName;

//        archive.renameTo(new File(savePath + newFileName));
        SysDocserver docserver;
        UploadVo vo = (UploadVo) getHttpSession().getAttribute("uploadVo");
        if (null == vo) {
            SysDocserverExample example = new SysDocserverExample();
            example.createCriteria().andServerstateEqualTo(1);
            List<SysDocserver> docserverList = docserverService.selectByWhereNotPage(example);
            docserver = docserverList.get(0);
            UploadVo uploadVo = new UploadVo();
            uploadVo.setDocserver(docserver);
            getHttpSession().setAttribute("uploadVo", uploadVo);
        }
        else {
            docserver = vo.getDocserver();
        }

        if ("FTP".equals(docserver.getServertype())) {
            FtpUtil util = new FtpUtil();
            util.connect(docserver.getServerip(), docserver.getServerport(),
                    docserver.getFtpuser(), docserver.getFtppassword(), docserver.getServerpath());

            FileInputStream s = new FileInputStream(archive);
            util.uploadFile(s, newFileName);
            util.closeServer();
        }
        else if ("LOCAL".equals(docserver.getServertype())) {
            String serverPath = docserver.getServerpath();
            String savePath = docserver.getServerpath();
            if (null == serverPath || "".equals(serverPath)) {
                response.getWriter().print(0);
                return null;
            }
            else {
                if (!serverPath.substring(serverPath.length()-1,serverPath.length()).equals("/")) {
                    savePath += "/";
                }
            }

            archive.renameTo(new File(savePath + newFileName));
        }


//        archive.renameTo(new File(savePath + archiveFileName));

//        response.getWriter().print(archiveFileName +"上传成功");
        response.getWriter().print(0);
        return null;
    }

    /**
     * 将原文件，拼接到目标文件dst
     * @param src
     * @param dst
     */
    private void cat(File src, File dst) {
        InputStream in = null;
        OutputStream out = null;
        try {
            if (dst.exists()) {
                out = new BufferedOutputStream(new FileOutputStream(dst, true),BUFFER_SIZE);
            } else {
                out = new BufferedOutputStream(new FileOutputStream(dst),BUFFER_SIZE);
            }
            in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);

            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 全文检索，文件预览
     * @throws IOException 
     * */
    public String filePreview() throws IOException{
    	PrintWriter out = this.getPrintWriter();
    	if(isFieldscan()){
    		out.write("1"); //有权限
    	}else{
    		out.write("0"); //没权限
    	}
    	return null;
    }

    /**
     * 全文检索，文件下载
     * @throws IOException 
     * */
    public String isDownDoc() throws IOException{
    	PrintWriter out = this.getPrintWriter();
    	if(isFielddown()){
    		out.write("1"); //有权限
    	}else{
    		out.write("0"); //
    	}
    	return null;
    }
    public String downDoc() throws IOException {
    	if (null == docId || "".equals(docId)) {
    		return ERROR;
    	}
    	//根据docid读取电子全文信息
    	doc = docService.selectByPrimaryKey(docId);
    	if (null == doc || "".equals(doc)) {
    		return ERROR;
    	}
    	//是否有下载权限
    	if(isFielddown()){
	    	//得到原文件名
	    	docName = doc.getDocoldname();
//            docName = new String(docName.getBytes(),"ISO8859-1");
            contentType = getContentType(doc.getDocext());
	    	//判断文件所属服务器
	    	String serverid = doc.getDocserverid();
	    	//得到所属服务器的信息
	    	docServer = docserverService.selectByPrimaryKey(serverid);
	    	if (null == docServer || "".equals(docServer)) {
	    		return ERROR;
	    	}
	    	//判断服务器类型。根据不同类型，执行不同的操作
	    	String serverType = docServer.getServertype();
	    	if ("LOCAL".equals(serverType)) {
	    		savePath = docServer.getServerpath();
	    	}
            else if ("FTP".equals(serverType)) {
            }

    	}

    	return SUCCESS;
    }
    
    public InputStream getInputStream() throws IOException {
        FileInputStream aa = null;
        //判断服务器类型。根据不同类型，执行不同的操作
        String serverType = docServer.getServertype();
        if ("LOCAL".equals(serverType)) {
            String serverPath = docServer.getServerpath();
            if (!serverPath.substring(serverPath.length()-1,serverPath.length()).equals("/")) {
                serverPath += "/";
            }
            String fileName = serverPath + doc.getDocpath();
//    	String fileName = doc.getDocpath();
            aa = new FileInputStream(fileName);
        }
        else if ("FTP".equals(serverType)) {
            FtpUtil util = new FtpUtil();
            util.connect(docServer.getServerip(), docServer.getServerport(),
                    docServer.getFtpuser(), docServer.getFtppassword(), docServer.getServerpath());

            InputStream bb = util.downFile(doc.getDocnewname());

//            aa = (FileInputStream)bb;
//            aa = util.downFile(doc.getDocnewname());
            util.closeServer();
            return bb;
        }
        else {

        }

    	return aa;
	}
    
    public String readDoc() {
//    	docId = "1f4fe3f4-c285-4600-884c-2fd57f2e0d3a";
    	if (null == docId || "".equals(docId)) {
    		return ERROR;
    	}
    	//根据docid读取电子全文信息
    	doc = docService.selectByPrimaryKey(docId);
    	if (null == doc || "".equals(doc)) {
    		return ERROR;
    	}
    	//得到原文件名
    	docName = doc.getDocoldname();
    	//判断文件所属服务器
    	String serverid = doc.getDocserverid();
    	//得到所属服务器的信息
    	SysDocserver docServer = docserverService.selectByPrimaryKey(serverid);
    	if (null == docServer || "".equals(docServer)) {
    		return ERROR;
    	}
    	//判断服务器类型。根据不同类型，执行不同的操作
    	String serverType = docServer.getServertype();
    	if ("LOCAL".equals(serverType)) {
    		savePath = docServer.getServerpath();
    	}
    	String docType = doc.getDoctype();
    	contentType = getContentType(docType);
    	docName = "inline; filename=" + docName;
    	return SUCCESS;
    }
    /**
     * 返回文件下载类型
     * @param docType
     * @return
     */
    private String getContentType(String docType) {
    	if ("XLS".equals(docType.toUpperCase()) || "XLSX".equals(docType.toUpperCase())) {
            return "application/vnd.ms-excel;charset=utf-8";
    	}
    	else if("DOC".equals(docType.toUpperCase()) || "DOCX".equals(docType.toUpperCase())) {
            return "application/msword;charset=utf-8";
    	}
        else if("PPT".equals(docType.toUpperCase()) || "PPTX".equals(docType.toUpperCase())) {
            return "application/msword;charset=utf-8";
        }
    	else if("PDF".equals(docType.toUpperCase())) {
            return "application/pdf;charset=utf-8";
    	}
    	else {
            contentType = "text/plain;charset=utf-8";
    	}
    	return "";
    }


    public File getArchive() {
        return archive;
    }

    public void setArchive(File archive) {
        this.archive = archive;
    }

    public String getArchiveFileName() {
        return archiveFileName;
    }

    public void setArchiveFileName(String archiveFileName) {
        this.archiveFileName = archiveFileName;
    }

    public void setDocserverService(IDocserverService docserverService) {
        this.docserverService = docserverService;
    }

    public void setDocService(IDocService docService) {
        this.docService = docService;
    }

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    public String getSelectRowid() {
        return selectRowid;
    }

    public void setSelectRowid(String selectRowid) {
        this.selectRowid = selectRowid;
    }

    public int getChunks() {
        return chunks;
    }

    public void setChunks(int chunks) {
        this.chunks = chunks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChunk() {
        return chunk;
    }

    public void setChunk(int chunk) {
        this.chunk = chunk;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

	public String getDocName() throws UnsupportedEncodingException {
		//文件名字转码，为了下载显示中文名不出现乱码
		docName = new String(docName.getBytes("gbk"),"ISO-8859-1");
		return docName;
	}

	public void setDocName(String docName) {
		
		this.docName = docName;
	}

	public SysDoc getDoc() {
		return doc;
	}

	public void setDoc(SysDoc doc) {
		this.doc = doc;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public SysDocserver getDocServer() {
		return docServer;
	}

	public void setDocServer(SysDocserver docServer) {
		this.docServer = docServer;
	}

	public void setDynamicService(IDynamicService dynamicService) {
		this.dynamicService = dynamicService;
	}

	public void setTableService(ITableService tableService) {
		this.tableService = tableService;
	}

	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}

	public void setOrgService(IOrgService orgService) {
		this.orgService = orgService;
	}

	public String getFileid() {
		return fileid;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}

	public String getTreeid() {
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}


}
