package com.yapu.archive.entity;

public class SysAccountTree {
    private String accountTreeId;
    private String treeid;
    private String accountid;
    private Integer filescan;
    private Integer filedown;
    private Integer fileprint;
    
    private String filter;
    
    
    public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
    public String getAccountTreeId() {
        return accountTreeId;
    }
    public void setAccountTreeId(String accountTreeId) {
        this.accountTreeId = accountTreeId;
    }
    public String getTreeid() {
        return treeid;
    }
    public void setTreeid(String treeid) {
        this.treeid = treeid;
    }
    public String getAccountid() {
        return accountid;
    }
    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }
    public Integer getFilescan() {
        return filescan;
    }
    public void setFilescan(Integer filescan) {
        this.filescan = filescan;
    }
    public Integer getFiledown() {
        return filedown;
    }
    public void setFiledown(Integer filedown) {
        this.filedown = filedown;
    }
    public Integer getFileprint() {
        return fileprint;
    }
    public void setFileprint(Integer fileprint) {
        this.fileprint = fileprint;
    }
}