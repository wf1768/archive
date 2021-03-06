package com.yapu.system.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SysAccountOrgExample {
    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database table SYS_ACCOUNT_ORG
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    protected String orderByClause;

    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database table SYS_ACCOUNT_ORG
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    protected List oredCriteria;

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT_ORG
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public SysAccountOrgExample() {
        oredCriteria = new ArrayList();
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT_ORG
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    protected SysAccountOrgExample(SysAccountOrgExample example) {
        this.orderByClause = example.orderByClause;
        this.oredCriteria = example.oredCriteria;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT_ORG
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT_ORG
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT_ORG
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public List getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT_ORG
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT_ORG
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
     * This method corresponds to the database table SYS_ACCOUNT_ORG
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT_ORG
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public void clear() {
        oredCriteria.clear();
    }

    /**
     * This class was generated by Apache iBATIS ibator.
     * This class corresponds to the database table SYS_ACCOUNT_ORG
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

        public Criteria andAccountOrgIdIsNull() {
            addCriterion("ACCOUNT_ORG_ID is null");
            return this;
        }

        public Criteria andAccountOrgIdIsNotNull() {
            addCriterion("ACCOUNT_ORG_ID is not null");
            return this;
        }

        public Criteria andAccountOrgIdEqualTo(String value) {
            addCriterion("ACCOUNT_ORG_ID =", value, "accountOrgId");
            return this;
        }

        public Criteria andAccountOrgIdNotEqualTo(String value) {
            addCriterion("ACCOUNT_ORG_ID <>", value, "accountOrgId");
            return this;
        }

        public Criteria andAccountOrgIdGreaterThan(String value) {
            addCriterion("ACCOUNT_ORG_ID >", value, "accountOrgId");
            return this;
        }

        public Criteria andAccountOrgIdGreaterThanOrEqualTo(String value) {
            addCriterion("ACCOUNT_ORG_ID >=", value, "accountOrgId");
            return this;
        }

        public Criteria andAccountOrgIdLessThan(String value) {
            addCriterion("ACCOUNT_ORG_ID <", value, "accountOrgId");
            return this;
        }

        public Criteria andAccountOrgIdLessThanOrEqualTo(String value) {
            addCriterion("ACCOUNT_ORG_ID <=", value, "accountOrgId");
            return this;
        }

        public Criteria andAccountOrgIdLike(String value) {
            addCriterion("ACCOUNT_ORG_ID like", value, "accountOrgId");
            return this;
        }

        public Criteria andAccountOrgIdNotLike(String value) {
            addCriterion("ACCOUNT_ORG_ID not like", value, "accountOrgId");
            return this;
        }

        public Criteria andAccountOrgIdIn(List values) {
            addCriterion("ACCOUNT_ORG_ID in", values, "accountOrgId");
            return this;
        }

        public Criteria andAccountOrgIdNotIn(List values) {
            addCriterion("ACCOUNT_ORG_ID not in", values, "accountOrgId");
            return this;
        }

        public Criteria andAccountOrgIdBetween(String value1, String value2) {
            addCriterion("ACCOUNT_ORG_ID between", value1, value2, "accountOrgId");
            return this;
        }

        public Criteria andAccountOrgIdNotBetween(String value1, String value2) {
            addCriterion("ACCOUNT_ORG_ID not between", value1, value2, "accountOrgId");
            return this;
        }

        public Criteria andOrgidIsNull() {
            addCriterion("ORGID is null");
            return this;
        }

        public Criteria andOrgidIsNotNull() {
            addCriterion("ORGID is not null");
            return this;
        }

        public Criteria andOrgidEqualTo(String value) {
            addCriterion("ORGID =", value, "orgid");
            return this;
        }

        public Criteria andOrgidNotEqualTo(String value) {
            addCriterion("ORGID <>", value, "orgid");
            return this;
        }

        public Criteria andOrgidGreaterThan(String value) {
            addCriterion("ORGID >", value, "orgid");
            return this;
        }

        public Criteria andOrgidGreaterThanOrEqualTo(String value) {
            addCriterion("ORGID >=", value, "orgid");
            return this;
        }

        public Criteria andOrgidLessThan(String value) {
            addCriterion("ORGID <", value, "orgid");
            return this;
        }

        public Criteria andOrgidLessThanOrEqualTo(String value) {
            addCriterion("ORGID <=", value, "orgid");
            return this;
        }

        public Criteria andOrgidLike(String value) {
            addCriterion("ORGID like", value, "orgid");
            return this;
        }

        public Criteria andOrgidNotLike(String value) {
            addCriterion("ORGID not like", value, "orgid");
            return this;
        }

        public Criteria andOrgidIn(List values) {
            addCriterion("ORGID in", values, "orgid");
            return this;
        }

        public Criteria andOrgidNotIn(List values) {
            addCriterion("ORGID not in", values, "orgid");
            return this;
        }

        public Criteria andOrgidBetween(String value1, String value2) {
            addCriterion("ORGID between", value1, value2, "orgid");
            return this;
        }

        public Criteria andOrgidNotBetween(String value1, String value2) {
            addCriterion("ORGID not between", value1, value2, "orgid");
            return this;
        }

        public Criteria andAccountidIsNull() {
            addCriterion("ACCOUNTID is null");
            return this;
        }

        public Criteria andAccountidIsNotNull() {
            addCriterion("ACCOUNTID is not null");
            return this;
        }

        public Criteria andAccountidEqualTo(String value) {
            addCriterion("ACCOUNTID =", value, "accountid");
            return this;
        }

        public Criteria andAccountidNotEqualTo(String value) {
            addCriterion("ACCOUNTID <>", value, "accountid");
            return this;
        }

        public Criteria andAccountidGreaterThan(String value) {
            addCriterion("ACCOUNTID >", value, "accountid");
            return this;
        }

        public Criteria andAccountidGreaterThanOrEqualTo(String value) {
            addCriterion("ACCOUNTID >=", value, "accountid");
            return this;
        }

        public Criteria andAccountidLessThan(String value) {
            addCriterion("ACCOUNTID <", value, "accountid");
            return this;
        }

        public Criteria andAccountidLessThanOrEqualTo(String value) {
            addCriterion("ACCOUNTID <=", value, "accountid");
            return this;
        }

        public Criteria andAccountidLike(String value) {
            addCriterion("ACCOUNTID like", value, "accountid");
            return this;
        }

        public Criteria andAccountidNotLike(String value) {
            addCriterion("ACCOUNTID not like", value, "accountid");
            return this;
        }

        public Criteria andAccountidIn(List values) {
            addCriterion("ACCOUNTID in", values, "accountid");
            return this;
        }

        public Criteria andAccountidNotIn(List values) {
            addCriterion("ACCOUNTID not in", values, "accountid");
            return this;
        }

        public Criteria andAccountidBetween(String value1, String value2) {
            addCriterion("ACCOUNTID between", value1, value2, "accountid");
            return this;
        }

        public Criteria andAccountidNotBetween(String value1, String value2) {
            addCriterion("ACCOUNTID not between", value1, value2, "accountid");
            return this;
        }
    }
}