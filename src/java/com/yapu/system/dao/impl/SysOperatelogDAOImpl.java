package com.yapu.system.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.yapu.system.dao.itf.SysOperatelogDAO;
import com.yapu.system.entity.SysOperatelog;
import com.yapu.system.entity.SysOperatelogExample;

public class SysOperatelogDAOImpl extends SqlMapClientDaoSupport implements SysOperatelogDAO {

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_OPERATELOG
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public SysOperatelogDAOImpl() {
        super();
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_OPERATELOG
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int countByExample(SysOperatelogExample example) {
        Integer count = (Integer)  getSqlMapClientTemplate().queryForObject("SYS_OPERATELOG.ibatorgenerated_countByExample", example);
        return count.intValue();
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_OPERATELOG
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int deleteByExample(SysOperatelogExample example) {
        int rows = getSqlMapClientTemplate().delete("SYS_OPERATELOG.ibatorgenerated_deleteByExample", example);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_OPERATELOG
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int deleteByPrimaryKey(String operatelogid) {
        SysOperatelog key = new SysOperatelog();
        key.setOperatelogid(operatelogid);
        int rows = getSqlMapClientTemplate().delete("SYS_OPERATELOG.ibatorgenerated_deleteByPrimaryKey", key);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_OPERATELOG
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public void insert(SysOperatelog record) {
        getSqlMapClientTemplate().insert("SYS_OPERATELOG.ibatorgenerated_insert", record);
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_OPERATELOG
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public void insertSelective(SysOperatelog record) {
        getSqlMapClientTemplate().insert("SYS_OPERATELOG.ibatorgenerated_insertSelective", record);
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_OPERATELOG
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public List selectByExample(SysOperatelogExample example) {
        List list = getSqlMapClientTemplate().queryForList("SYS_OPERATELOG.ibatorgenerated_selectByExample", example);
        return list;
    }
    /*
     * (non-Javadoc)
     * @see com.yapu.system.dao.itf.SysOperatelogDAO#selectByMapPage(java.util.HashMap)
     */
    public List selectByMapPage(HashMap map) {
		List list = getSqlMapClientTemplate().queryForList("SYS_OPERATELOG.ibatorgenerated_selectByExamplePage", map);
		return list;
	}

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_OPERATELOG
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public SysOperatelog selectByPrimaryKey(String operatelogid) {
        SysOperatelog key = new SysOperatelog();
        key.setOperatelogid(operatelogid);
        SysOperatelog record = (SysOperatelog) getSqlMapClientTemplate().queryForObject("SYS_OPERATELOG.ibatorgenerated_selectByPrimaryKey", key);
        return record;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_OPERATELOG
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int updateByExampleSelective(SysOperatelog record, SysOperatelogExample example) {
        UpdateByExampleParms parms = new UpdateByExampleParms(record, example);
        int rows = getSqlMapClientTemplate().update("SYS_OPERATELOG.ibatorgenerated_updateByExampleSelective", parms);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_OPERATELOG
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int updateByExample(SysOperatelog record, SysOperatelogExample example) {
        UpdateByExampleParms parms = new UpdateByExampleParms(record, example);
        int rows = getSqlMapClientTemplate().update("SYS_OPERATELOG.ibatorgenerated_updateByExample", parms);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_OPERATELOG
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int updateByPrimaryKeySelective(SysOperatelog record) {
        int rows = getSqlMapClientTemplate().update("SYS_OPERATELOG.ibatorgenerated_updateByPrimaryKeySelective", record);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_OPERATELOG
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int updateByPrimaryKey(SysOperatelog record) {
        int rows = getSqlMapClientTemplate().update("SYS_OPERATELOG.ibatorgenerated_updateByPrimaryKey", record);
        return rows;
    }

    /**
     * This class was generated by Apache iBATIS ibator.
     * This class corresponds to the database table SYS_OPERATELOG
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    private static class UpdateByExampleParms extends SysOperatelogExample {
        private Object record;

        public UpdateByExampleParms(Object record, SysOperatelogExample example) {
            super(example);
            this.record = record;
        }

        public Object getRecord() {
            return record;
        }
    }
}