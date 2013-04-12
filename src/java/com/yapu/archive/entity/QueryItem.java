package com.yapu.archive.entity;

public class QueryItem {
    private String name;		//字段名称
    private int operatorType; //逻辑操作类型
    private String value;		//字段值
    private int valueType;	//属性类型
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOperatorType() {
		return operatorType;
	}
	public void setOperatorType(int operatorType) {
		this.operatorType = operatorType;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getValueType() {
		return valueType;
	}
	public void setValueType(int valueType) {
		this.valueType = valueType;
	}
    
}
