package com.yapu.archive.entity;

import java.util.List;

public class QueryGroup {
	private String groupType;		//逻辑关系，and 或 or
	private List<QueryItem> items;
	private List<QueryGroup> groups;
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public List<QueryItem> getItems() {
		return items;
	}
	public void setItems(List<QueryItem> items) {
		this.items = items;
	}
	public List<QueryGroup> getGroups() {
		return groups;
	}
	public void setGroups(List<QueryGroup> groups) {
		this.groups = groups;
	}
	
	
}
