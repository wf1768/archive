package com.yapu.archive.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SysTempletExample {
    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database table SYS_TEMPLET
     *
     * @ibatorgenerated Sun Nov 14 02:07:28 GMT+08:00 2010
     */
    protected String orderByClause;

    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database table SYS_TEMPLET
     *
     * @ibatorgenerated Sun Nov 14 02:07:28 GMT+08:00 2010
     */
    protected List oredCriteria;

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_TEMPLET
     *
     * @ibatorgenerated Sun Nov 14 02:07:28 GMT+08:00 2010
     */
    public SysTempletExample() {
        oredCriteria = new ArrayList();
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_TEMPLET
     *
     * @ibatorgenerated Sun Nov 14 02:07:28 GMT+08:00 2010
     */
    protected SysTempletExample(SysTempletExample example) {
        this.orderByClause = example.orderByClause;
        this.oredCriteria = example.oredCriteria;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_TEMPLET
     *
     * @ibatorgenerated Sun Nov 14 02:07:28 GMT+08:00 2010
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_TEMPLET
     *
     * @ibatorgenerated Sun Nov 14 02:07:28 GMT+08:00 2010
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_TEMPLET
     *
     * @ibatorgenerated Sun Nov 14 02:07:28 GMT+08:00 2010
     */
    public List getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_TEMPLET
     *
     * @ibatorgenerated Sun Nov 14 02:07:28 GMT+08:00 2010
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_TEMPLET
     *
     * @ibatorgenerated Sun Nov 14 02:07:28 GMT+08:00 2010
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
     * This method corresponds to the database table SYS_TEMPLET
     *
     * @ibatorgenerated Sun Nov 14 02:07:28 GMT+08:00 2010
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_TEMPLET
     *
     * @ibatorgenerated Sun Nov 14 02:07:28 GMT+08:00 2010
     */
    public void clear() {
        oredCriteria.clear();
    }

    /**
     * This class was generated by Apache iBATIS ibator.
     * This class corresponds to the database table SYS_TEMPLET
     *
     * @ibatorgenerated Sun Nov 14 02:07:28 GMT+08:00 2010
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

        public Criteria andTempletidIsNull() {
            addCriterion("TEMPLETID is null");
            return this;
        }

        public Criteria andTempletidIsNotNull() {
            addCriterion("TEMPLETID is not null");
            return this;
        }

        public Criteria andTempletidEqualTo(String value) {
            addCriterion("TEMPLETID =", value, "templetid");
            return this;
        }

        public Criteria andTempletidNotEqualTo(String value) {
            addCriterion("TEMPLETID <>", value, "templetid");
            return this;
        }

        public Criteria andTempletidGreaterThan(String value) {
            addCriterion("TEMPLETID >", value, "templetid");
            return this;
        }

        public Criteria andTempletidGreaterThanOrEqualTo(String value) {
            addCriterion("TEMPLETID >=", value, "templetid");
            return this;
        }

        public Criteria andTempletidLessThan(String value) {
            addCriterion("TEMPLETID <", value, "templetid");
            return this;
        }

        public Criteria andTempletidLessThanOrEqualTo(String value) {
            addCriterion("TEMPLETID <=", value, "templetid");
            return this;
        }

        public Criteria andTempletidLike(String value) {
            addCriterion("TEMPLETID like", value, "templetid");
            return this;
        }

        public Criteria andTempletidNotLike(String value) {
            addCriterion("TEMPLETID not like", value, "templetid");
            return this;
        }

        public Criteria andTempletidIn(List values) {
            addCriterion("TEMPLETID in", values, "templetid");
            return this;
        }

        public Criteria andTempletidNotIn(List values) {
            addCriterion("TEMPLETID not in", values, "templetid");
            return this;
        }

        public Criteria andTempletidBetween(String value1, String value2) {
            addCriterion("TEMPLETID between", value1, value2, "templetid");
            return this;
        }

        public Criteria andTempletidNotBetween(String value1, String value2) {
            addCriterion("TEMPLETID not between", value1, value2, "templetid");
            return this;
        }

        public Criteria andTempletnameIsNull() {
            addCriterion("TEMPLETNAME is null");
            return this;
        }

        public Criteria andTempletnameIsNotNull() {
            addCriterion("TEMPLETNAME is not null");
            return this;
        }

        public Criteria andTempletnameEqualTo(String value) {
            addCriterion("TEMPLETNAME =", value, "templetname");
            return this;
        }

        public Criteria andTempletnameNotEqualTo(String value) {
            addCriterion("TEMPLETNAME <>", value, "templetname");
            return this;
        }

        public Criteria andTempletnameGreaterThan(String value) {
            addCriterion("TEMPLETNAME >", value, "templetname");
            return this;
        }

        public Criteria andTempletnameGreaterThanOrEqualTo(String value) {
            addCriterion("TEMPLETNAME >=", value, "templetname");
            return this;
        }

        public Criteria andTempletnameLessThan(String value) {
            addCriterion("TEMPLETNAME <", value, "templetname");
            return this;
        }

        public Criteria andTempletnameLessThanOrEqualTo(String value) {
            addCriterion("TEMPLETNAME <=", value, "templetname");
            return this;
        }

        public Criteria andTempletnameLike(String value) {
            addCriterion("TEMPLETNAME like", value, "templetname");
            return this;
        }

        public Criteria andTempletnameNotLike(String value) {
            addCriterion("TEMPLETNAME not like", value, "templetname");
            return this;
        }

        public Criteria andTempletnameIn(List values) {
            addCriterion("TEMPLETNAME in", values, "templetname");
            return this;
        }

        public Criteria andTempletnameNotIn(List values) {
            addCriterion("TEMPLETNAME not in", values, "templetname");
            return this;
        }

        public Criteria andTempletnameBetween(String value1, String value2) {
            addCriterion("TEMPLETNAME between", value1, value2, "templetname");
            return this;
        }

        public Criteria andTempletnameNotBetween(String value1, String value2) {
            addCriterion("TEMPLETNAME not between", value1, value2, "templetname");
            return this;
        }

        public Criteria andTemplettypeIsNull() {
            addCriterion("TEMPLETTYPE is null");
            return this;
        }

        public Criteria andTemplettypeIsNotNull() {
            addCriterion("TEMPLETTYPE is not null");
            return this;
        }

        public Criteria andTemplettypeEqualTo(String value) {
            addCriterion("TEMPLETTYPE =", value, "templettype");
            return this;
        }

        public Criteria andTemplettypeNotEqualTo(String value) {
            addCriterion("TEMPLETTYPE <>", value, "templettype");
            return this;
        }

        public Criteria andTemplettypeGreaterThan(String value) {
            addCriterion("TEMPLETTYPE >", value, "templettype");
            return this;
        }

        public Criteria andTemplettypeGreaterThanOrEqualTo(String value) {
            addCriterion("TEMPLETTYPE >=", value, "templettype");
            return this;
        }

        public Criteria andTemplettypeLessThan(String value) {
            addCriterion("TEMPLETTYPE <", value, "templettype");
            return this;
        }

        public Criteria andTemplettypeLessThanOrEqualTo(String value) {
            addCriterion("TEMPLETTYPE <=", value, "templettype");
            return this;
        }

        public Criteria andTemplettypeLike(String value) {
            addCriterion("TEMPLETTYPE like", value, "templettype");
            return this;
        }

        public Criteria andTemplettypeNotLike(String value) {
            addCriterion("TEMPLETTYPE not like", value, "templettype");
            return this;
        }

        public Criteria andTemplettypeIn(List values) {
            addCriterion("TEMPLETTYPE in", values, "templettype");
            return this;
        }

        public Criteria andTemplettypeNotIn(List values) {
            addCriterion("TEMPLETTYPE not in", values, "templettype");
            return this;
        }

        public Criteria andTemplettypeBetween(String value1, String value2) {
            addCriterion("TEMPLETTYPE between", value1, value2, "templettype");
            return this;
        }

        public Criteria andTemplettypeNotBetween(String value1, String value2) {
            addCriterion("TEMPLETTYPE not between", value1, value2, "templettype");
            return this;
        }
    }
}