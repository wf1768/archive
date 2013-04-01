package com.yapu.archive.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SysCodeExample {
    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database table SYS_CODE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    protected String orderByClause;

    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database table SYS_CODE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    protected List oredCriteria;

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_CODE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public SysCodeExample() {
        oredCriteria = new ArrayList();
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_CODE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    protected SysCodeExample(SysCodeExample example) {
        this.orderByClause = example.orderByClause;
        this.oredCriteria = example.oredCriteria;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_CODE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_CODE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_CODE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public List getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_CODE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_CODE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_CODE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_CODE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public void clear() {
        oredCriteria.clear();
    }

    /**
     * This class was generated by Apache iBATIS ibator.
     * This class corresponds to the database table SYS_CODE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public static class Criteria {
        protected List criteriaWithoutValue;

        protected List criteriaWithSingleValue;

        protected List criteriaWithListValue;

        protected List criteriaWithBetweenValue;

        protected Criteria() {
            super();
            criteriaWithoutValue = new ArrayList();
            criteriaWithSingleValue = new ArrayList();
            criteriaWithListValue = new ArrayList();
            criteriaWithBetweenValue = new ArrayList();
        }

        public boolean isValid() {
            return criteriaWithoutValue.size() > 0
                || criteriaWithSingleValue.size() > 0
                || criteriaWithListValue.size() > 0
                || criteriaWithBetweenValue.size() > 0;
        }

        public List getCriteriaWithoutValue() {
            return criteriaWithoutValue;
        }

        public List getCriteriaWithSingleValue() {
            return criteriaWithSingleValue;
        }

        public List getCriteriaWithListValue() {
            return criteriaWithListValue;
        }

        public List getCriteriaWithBetweenValue() {
            return criteriaWithBetweenValue;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteriaWithoutValue.add(condition);
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            Map map = new HashMap();
            map.put("condition", condition);
            map.put("value", value);
            criteriaWithSingleValue.add(map);
        }

        protected void addCriterion(String condition, List values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            Map map = new HashMap();
            map.put("condition", condition);
            map.put("values", values);
            criteriaWithListValue.add(map);
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            List list = new ArrayList();
            list.add(value1);
            list.add(value2);
            Map map = new HashMap();
            map.put("condition", condition);
            map.put("values", list);
            criteriaWithBetweenValue.add(map);
        }

        public Criteria andCodeidIsNull() {
            addCriterion("CODEID is null");
            return this;
        }

        public Criteria andCodeidIsNotNull() {
            addCriterion("CODEID is not null");
            return this;
        }

        public Criteria andCodeidEqualTo(String value) {
            addCriterion("CODEID =", value, "codeid");
            return this;
        }

        public Criteria andCodeidNotEqualTo(String value) {
            addCriterion("CODEID <>", value, "codeid");
            return this;
        }

        public Criteria andCodeidGreaterThan(String value) {
            addCriterion("CODEID >", value, "codeid");
            return this;
        }

        public Criteria andCodeidGreaterThanOrEqualTo(String value) {
            addCriterion("CODEID >=", value, "codeid");
            return this;
        }

        public Criteria andCodeidLessThan(String value) {
            addCriterion("CODEID <", value, "codeid");
            return this;
        }

        public Criteria andCodeidLessThanOrEqualTo(String value) {
            addCriterion("CODEID <=", value, "codeid");
            return this;
        }

        public Criteria andCodeidLike(String value) {
            addCriterion("CODEID like", value, "codeid");
            return this;
        }

        public Criteria andCodeidNotLike(String value) {
            addCriterion("CODEID not like", value, "codeid");
            return this;
        }

        public Criteria andCodeidIn(List values) {
            addCriterion("CODEID in", values, "codeid");
            return this;
        }

        public Criteria andCodeidNotIn(List values) {
            addCriterion("CODEID not in", values, "codeid");
            return this;
        }

        public Criteria andCodeidBetween(String value1, String value2) {
            addCriterion("CODEID between", value1, value2, "codeid");
            return this;
        }

        public Criteria andCodeidNotBetween(String value1, String value2) {
            addCriterion("CODEID not between", value1, value2, "codeid");
            return this;
        }

        public Criteria andTempletfieldidIsNull() {
            addCriterion("TEMPLETFIELDID is null");
            return this;
        }

        public Criteria andTempletfieldidIsNotNull() {
            addCriterion("TEMPLETFIELDID is not null");
            return this;
        }

        public Criteria andTempletfieldidEqualTo(String value) {
            addCriterion("TEMPLETFIELDID =", value, "templetfieldid");
            return this;
        }

        public Criteria andTempletfieldidNotEqualTo(String value) {
            addCriterion("TEMPLETFIELDID <>", value, "templetfieldid");
            return this;
        }

        public Criteria andTempletfieldidGreaterThan(String value) {
            addCriterion("TEMPLETFIELDID >", value, "templetfieldid");
            return this;
        }

        public Criteria andTempletfieldidGreaterThanOrEqualTo(String value) {
            addCriterion("TEMPLETFIELDID >=", value, "templetfieldid");
            return this;
        }

        public Criteria andTempletfieldidLessThan(String value) {
            addCriterion("TEMPLETFIELDID <", value, "templetfieldid");
            return this;
        }

        public Criteria andTempletfieldidLessThanOrEqualTo(String value) {
            addCriterion("TEMPLETFIELDID <=", value, "templetfieldid");
            return this;
        }

        public Criteria andTempletfieldidLike(String value) {
            addCriterion("TEMPLETFIELDID like", value, "templetfieldid");
            return this;
        }

        public Criteria andTempletfieldidNotLike(String value) {
            addCriterion("TEMPLETFIELDID not like", value, "templetfieldid");
            return this;
        }

        public Criteria andTempletfieldidIn(List values) {
            addCriterion("TEMPLETFIELDID in", values, "templetfieldid");
            return this;
        }

        public Criteria andTempletfieldidNotIn(List values) {
            addCriterion("TEMPLETFIELDID not in", values, "templetfieldid");
            return this;
        }

        public Criteria andTempletfieldidBetween(String value1, String value2) {
            addCriterion("TEMPLETFIELDID between", value1, value2, "templetfieldid");
            return this;
        }

        public Criteria andTempletfieldidNotBetween(String value1, String value2) {
            addCriterion("TEMPLETFIELDID not between", value1, value2, "templetfieldid");
            return this;
        }

        public Criteria andColumnnameIsNull() {
            addCriterion("COLUMNNAME is null");
            return this;
        }

        public Criteria andColumnnameIsNotNull() {
            addCriterion("COLUMNNAME is not null");
            return this;
        }

        public Criteria andColumnnameEqualTo(String value) {
            addCriterion("COLUMNNAME =", value, "columnname");
            return this;
        }

        public Criteria andColumnnameNotEqualTo(String value) {
            addCriterion("COLUMNNAME <>", value, "columnname");
            return this;
        }

        public Criteria andColumnnameGreaterThan(String value) {
            addCriterion("COLUMNNAME >", value, "columnname");
            return this;
        }

        public Criteria andColumnnameGreaterThanOrEqualTo(String value) {
            addCriterion("COLUMNNAME >=", value, "columnname");
            return this;
        }

        public Criteria andColumnnameLessThan(String value) {
            addCriterion("COLUMNNAME <", value, "columnname");
            return this;
        }

        public Criteria andColumnnameLessThanOrEqualTo(String value) {
            addCriterion("COLUMNNAME <=", value, "columnname");
            return this;
        }

        public Criteria andColumnnameLike(String value) {
            addCriterion("COLUMNNAME like", value, "columnname");
            return this;
        }

        public Criteria andColumnnameNotLike(String value) {
            addCriterion("COLUMNNAME not like", value, "columnname");
            return this;
        }

        public Criteria andColumnnameIn(List values) {
            addCriterion("COLUMNNAME in", values, "columnname");
            return this;
        }

        public Criteria andColumnnameNotIn(List values) {
            addCriterion("COLUMNNAME not in", values, "columnname");
            return this;
        }

        public Criteria andColumnnameBetween(String value1, String value2) {
            addCriterion("COLUMNNAME between", value1, value2, "columnname");
            return this;
        }

        public Criteria andColumnnameNotBetween(String value1, String value2) {
            addCriterion("COLUMNNAME not between", value1, value2, "columnname");
            return this;
        }

        public Criteria andColumndataIsNull() {
            addCriterion("COLUMNDATA is null");
            return this;
        }

        public Criteria andColumndataIsNotNull() {
            addCriterion("COLUMNDATA is not null");
            return this;
        }

        public Criteria andColumndataEqualTo(String value) {
            addCriterion("COLUMNDATA =", value, "columndata");
            return this;
        }

        public Criteria andColumndataNotEqualTo(String value) {
            addCriterion("COLUMNDATA <>", value, "columndata");
            return this;
        }

        public Criteria andColumndataGreaterThan(String value) {
            addCriterion("COLUMNDATA >", value, "columndata");
            return this;
        }

        public Criteria andColumndataGreaterThanOrEqualTo(String value) {
            addCriterion("COLUMNDATA >=", value, "columndata");
            return this;
        }

        public Criteria andColumndataLessThan(String value) {
            addCriterion("COLUMNDATA <", value, "columndata");
            return this;
        }

        public Criteria andColumndataLessThanOrEqualTo(String value) {
            addCriterion("COLUMNDATA <=", value, "columndata");
            return this;
        }

        public Criteria andColumndataLike(String value) {
            addCriterion("COLUMNDATA like", value, "columndata");
            return this;
        }

        public Criteria andColumndataNotLike(String value) {
            addCriterion("COLUMNDATA not like", value, "columndata");
            return this;
        }

        public Criteria andColumndataIn(List values) {
            addCriterion("COLUMNDATA in", values, "columndata");
            return this;
        }

        public Criteria andColumndataNotIn(List values) {
            addCriterion("COLUMNDATA not in", values, "columndata");
            return this;
        }

        public Criteria andColumndataBetween(String value1, String value2) {
            addCriterion("COLUMNDATA between", value1, value2, "columndata");
            return this;
        }

        public Criteria andColumndataNotBetween(String value1, String value2) {
            addCriterion("COLUMNDATA not between", value1, value2, "columndata");
            return this;
        }

        public Criteria andCodeorderIsNull() {
            addCriterion("CODEORDER is null");
            return this;
        }

        public Criteria andCodeorderIsNotNull() {
            addCriterion("CODEORDER is not null");
            return this;
        }

        public Criteria andCodeorderEqualTo(Integer value) {
            addCriterion("CODEORDER =", value, "codeorder");
            return this;
        }

        public Criteria andCodeorderNotEqualTo(Integer value) {
            addCriterion("CODEORDER <>", value, "codeorder");
            return this;
        }

        public Criteria andCodeorderGreaterThan(Integer value) {
            addCriterion("CODEORDER >", value, "codeorder");
            return this;
        }

        public Criteria andCodeorderGreaterThanOrEqualTo(Integer value) {
            addCriterion("CODEORDER >=", value, "codeorder");
            return this;
        }

        public Criteria andCodeorderLessThan(Integer value) {
            addCriterion("CODEORDER <", value, "codeorder");
            return this;
        }

        public Criteria andCodeorderLessThanOrEqualTo(Integer value) {
            addCriterion("CODEORDER <=", value, "codeorder");
            return this;
        }

        public Criteria andCodeorderIn(List values) {
            addCriterion("CODEORDER in", values, "codeorder");
            return this;
        }

        public Criteria andCodeorderNotIn(List values) {
            addCriterion("CODEORDER not in", values, "codeorder");
            return this;
        }

        public Criteria andCodeorderBetween(Integer value1, Integer value2) {
            addCriterion("CODEORDER between", value1, value2, "codeorder");
            return this;
        }

        public Criteria andCodeorderNotBetween(Integer value1, Integer value2) {
            addCriterion("CODEORDER not between", value1, value2, "codeorder");
            return this;
        }
    }
}